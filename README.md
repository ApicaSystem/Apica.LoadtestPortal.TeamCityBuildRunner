Apica Loadtest plugin for TeamCity uses Maven to build the project. Also, you'll need JDK 7 or later.

1. Build the plugin:

Run the Maven "package" goal in ltploadtest/pom.xml.
This should create a zip file called "Apica_Loadtest_TeamCity_Plugin.zip" within the ltploadtest/target/ folder

Then copy the zip file to the plugins folder of the TeamCity installation folder. You'll need to restart the TeamCity server and agent processes.

2. Usage:

The build runner will appear as "Apica Loadtest" in the list of build runners in TeamCity.

You will need to know the following values in order to insert an Apica Loadtest build runner:

- where your account is located: Enterprise or Trial - this is selectable under the "Load Test Environment" section
- your LTP authentication token: you can find this under Setup -> User Settings within the Loadtest Portal. Locate and copy the value of "LTP API Auth Ticket"
- the preset name which includes the load test parameters such as duration, user count etc. You can save your presets under New Test in LTP. Copy the name of the preset as it is, e.g. "my Apica plugin preset"
- the load test file name: the name of the load test file that you would like to execute, e.g. "mytest.class" or "test_package.zip"

You'll see various exception messages in case the values cannot be validated. If that happens then review the values and retest.

3. Thresholds:

Optionally you can enter thresholds to mark a test as failed. E.g. if the average response time was below 5000ms then you can mark the test as failed.
If you don't set any thresholds then the test will be marked as failed in case of an exception otherwise it will pass.

4. Execution:

You'll be able to follow the job progress in the usual TeamCity build messages. The job status is reported every 10 seconds.

4. Statistics:

The job statisics will be available after a successful run in the following formats:

- a file called load-test-results.txt will be created as an artifact and includes the JSON representation of some basic ob stats such as average response time per page
- you can view the same set of job statistics under the Apica Load Test Summary tab in a tabular format
- you can also view a trend chart under the Apica Load Test Trends tab which shows a Continuous Integration page from the Loadtest Portal