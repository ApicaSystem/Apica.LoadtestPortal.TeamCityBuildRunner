package com.apicasystem.ltpselfservice;

import com.apicasystem.ltpselfservice.resources.LoadTestResult;
import com.apicasystem.ltpselfservice.resources.LtpEnvironmentType;
import com.apicasystem.ltpselfservice.resources.Operator;
import com.apicasystem.ltpselfservice.resources.StandardMetricResult;
import com.apicasystem.ltpselfservice.resources.StringUtils;
import com.apicasystem.ltpselfservice.resources.Threshold;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jetbrains.buildServer.serverSide.ServerPaths;
import org.jetbrains.annotations.NotNull;

public class ApicaSettings
{

    private final Debug debug = new Debug(this);
    private final String fileName = "apica.properties";
    private final File settingsFile;
    private String apiToken;
    private static ApicaSettings _instance;

    public ApicaSettings()
    {
        settingsFile = new File(fileName);
    }

    public ApicaSettings(@NotNull ServerPaths serverPaths)
    {
        _instance = this;

        this.settingsFile = new File(serverPaths.getConfigDir(), fileName);
        try
        {
            this.settingsFile.createNewFile();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        load();
    }

    public static ApicaSettings instance()
    {
        return _instance;
    }

    public String getApiToken()
    {
        return this.apiToken;
    }

    public void setApiToken(String apiToken)
    {
        this.apiToken = apiToken;
    }

    public File getSettingsFile()
    {
        return this.settingsFile;
    }

    public void store()
    {
        try
        {
            FileWriter out = new FileWriter(this.settingsFile);
            Properties p = new Properties();
            p.store(out, "");
            out.close();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void load()
    {
        try
        {
            FileReader in = new FileReader(this.settingsFile);
            Properties p = new Properties();
            p.load(in);
            in.close();

            this.debug.print("Settings loaded: %s", new Object[]
            {
                p
            });

        } catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public String stringifiedThresholds(Map<String, String> properties)
    {
        try
        {
            StringBuilder builder = new StringBuilder();
            List<Threshold> thresholds = parseThresholds(properties);
            if (thresholds.isEmpty())
            {
                builder.append("No thresholds specified.");
            } else
            {
                for (Threshold threshold : thresholds)
                {
                    builder.append(threshold.toString())
                            .append("\r\n");
                }
            }
            return builder.toString();
        } catch (Exception ex)
        {
            return "";
        }
    }

    public LtpEnvironmentType parseEnvironmentType(Map<String, String> properties) throws Exception
    {
        if (properties.containsKey(TestResultConstants.testEnvironmentKey))
        {
            String value = properties.get(TestResultConstants.testEnvironmentKey);
            return LtpEnvironmentType.valueOf(value);
        }

        throw new NullPointerException("The properties collection does not include the test environment key.");
    }

    public List<Threshold> parseThresholds(Map<String, String> properties) throws Exception
    {
        try
        {
            List<Threshold> thresholds = new ArrayList<Threshold>();
            Pattern thresholdPattern = Pattern.compile("threshold\\.(\\d+)\\.metric");

            Set<Map.Entry<String, String>> entrySet = properties.entrySet();

            HashSet<String> thresholdMetrics = new HashSet<String>();
            for (Map.Entry<String, String> e : entrySet)
            {
                String key = e.getKey();
                Matcher matcher = thresholdPattern.matcher(key);
                if (matcher.matches())
                {
                    String thresholdId = matcher.group(1);
                    String operatorKey = "threshold.".concat(thresholdId).concat(".operator");
                    String valueKey = "threshold.".concat(thresholdId).concat(".value");
                    String actionKey = "threshold.".concat(thresholdId).concat(".result");
                    if (properties.containsKey(operatorKey)
                            && properties.containsKey(valueKey)
                            && properties.containsKey(actionKey))
                    {
                        String metricValue = properties.get(key);
                        boolean canAdd = thresholdMetrics.add(metricValue);
                        if (canAdd)
                        {
                            StandardMetricResult.Metrics metric = Enum.valueOf(StandardMetricResult.Metrics.class, metricValue);
                            String operatorValue = properties.get(operatorKey);
                            Operator op = Enum.valueOf(Operator.class, operatorValue);
                            String valueValue = properties.get(valueKey);
                            int valueParsed = -1;
                            if (StringUtils.tryParseInt(valueValue))
                            {
                                valueParsed = Integer.parseInt(valueValue);
                                if (valueParsed < 0)
                                {
                                    throw new Exception("Make sure you specify a non-negative integer for all threshold values.");
                                }
                                if (metric.valueName.equals("percent"))
                                {
                                    if (valueParsed > 100)
                                    {
                                        throw new Exception("Percentage values should not exceed 100.");
                                    }
                                }
                            } else
                            {
                                throw new Exception("Make sure you specify a non-negative integer for all threshold values.");
                            }

                            String actionValue = properties.get(actionKey);
                            LoadTestResult lRes = Enum.valueOf(LoadTestResult.class, actionValue);
                            Threshold th = new Threshold(Integer.parseInt(thresholdId), metric, op, valueParsed, lRes);
                            thresholds.add(th);
                        } else
                        {
                            throw new Exception("Make sure you specify a threshold metric only once.");
                        }
                    }
                }
            }
            return thresholds;
        } catch (Exception ex)
        {
            throw ex;
        }
    }
}
