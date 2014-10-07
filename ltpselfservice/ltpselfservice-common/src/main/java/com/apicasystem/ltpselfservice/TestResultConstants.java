/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apicasystem.ltpselfservice;

/**
 *
 * @author andras.nemes
 */
public class TestResultConstants
{
    public static final String testEnvironmentKey = "loadtest.environment";
    public static final String testConfigurationId_key = "test.configuration.id";
    public static final int thresholdCount = 2;
    public static final String threshold_1_metric_key = "threshold.1.metric";
    public static final String threshold_1_operator_key = "threshold.1.operator";
    public static final String threshold_1_value_key = "threshold.1.value";
    public static final String threshold_1_result_key = "threshold.1.result";
    public static final String threshold_2_metric_key = "threshold.2.metric";
    public static final String threshold_2_operator_key = "threshold.2.operator";
    public static final String threshold_2_value_key = "threshold.2.value";
    public static final String threshold_2_result_key = "threshold.2.result";    

    public static String thresholdMetricKey(int n)
    {
        return thresholdKey(n, "metric");
    }

    public static String thresholdOperatorKey(int n)
    {
        return thresholdKey(n, "operator");
    }

    public static String thresholdValueKey(int n)
    {
        return thresholdKey(n, "value");
    }

    public static String thresholdResultKey(int n)
    {
        return thresholdKey(n, "result");
    }

    private static String thresholdKey(int n, String field)
    {
        return String.format("threshold.%d.%s", new Object[]
        {
            Integer.valueOf(n), field
        });
    }
}
