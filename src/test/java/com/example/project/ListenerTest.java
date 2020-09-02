package com.example.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ListenerTest implements ITestListener {
	private String jenkinEndppoint = "http://10.25.33.47:8084/ords/dev_anthem/twb/service/jenkin";
	
	@Override
	public void onTestFailure(ITestResult tr) {
		try {
			// fetch the object instance
			Object testObj = tr.getInstance();
			
			// try to fetch the test plan key & test case key
			Field testPlanKeyField = null;
			Field testCaseKeyField = null;
			Object testPlanKeyValue = null;
			Object testCaseKeyValue = null;
			
			try {
				testPlanKeyField = testObj.getClass().getDeclaredField("testPlanKey");
				testPlanKeyValue = testPlanKeyField.get(testObj);
			} catch (NoSuchFieldException e) {
				testPlanKeyField = null;
			}
			
			try {
				testCaseKeyField = testObj.getClass().getDeclaredField("testCaseKey");
				testCaseKeyValue = testCaseKeyField.get(testObj);
			} catch (NoSuchFieldException e) {
				testCaseKeyField = null;
			}
			
			// if test plan key / test case key is null then try to find the bamboo build number
			if(testPlanKeyValue == null || testCaseKeyValue == null) {
				InputStream stream = this.getClass().getResourceAsStream("/proj.properties"); 
				Properties prop = new Properties();
				prop.load(stream);
				
				String jenkinJobName = prop.getProperty("jenkin.job.name");
				String jenkinBuildId = prop.getProperty("jenkin.build.id");
				String testMethod = tr.getName();
				
				System.out.println(this.getClass().getResource("/proj.properties").getPath());
				System.out.println(jenkinJobName);
				System.out.println(jenkinBuildId);
				System.out.println("Failed");
				System.out.println(testMethod);
				this.logTestRunJenkinResult(jenkinJobName, jenkinBuildId, "FAIL", testMethod);
			} else {
				//this.logTestRunResult(testPlanKeyValue.toString(), testCaseKeyValue.toString(), "FAIL");
			}
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	  public void onTestSkipped(ITestResult tr) {
		System.out.println("Skipped");
	  }
	  
	 @Override
	  public void onTestSuccess(ITestResult tr) {
			// TODO Auto-generated method stub
			try {
				// fetch the object instance
				Object testObj = tr.getInstance();
				
				// try to fetch the test plan key & test case key
				Field testPlanKeyField = null;
				Field testCaseKeyField = null;
				Object testPlanKeyValue = null;
				Object testCaseKeyValue = null;
				
				try {
					testPlanKeyField = testObj.getClass().getDeclaredField("testPlanKey");
					testPlanKeyValue = testPlanKeyField.get(testObj);
				} catch (NoSuchFieldException e) {
					testPlanKeyField = null;
				}
				
				try {
					testCaseKeyField = testObj.getClass().getDeclaredField("testCaseKey");
					testCaseKeyValue = testCaseKeyField.get(testObj);
				} catch (NoSuchFieldException e) {
					testCaseKeyField = null;
				}
				
				// if test plan key / test case key is null then try to find the bamboo build number
				if(testPlanKeyValue == null || testCaseKeyValue == null) {
					InputStream stream = this.getClass().getResourceAsStream("/proj.properties"); 
					Properties prop = new Properties();
					prop.load(stream);
					
					String jenkinJobName = prop.getProperty("jenkin.job.name");
					String jenkinBuildId = prop.getProperty("jenkin.build.id");
					String testMethod = tr.getName();
					
					System.out.println(this.getClass().getResource("/proj.properties").getPath());
					System.out.println(jenkinJobName);
					System.out.println(jenkinBuildId);
					System.out.println("Passed");
					System.out.println(testMethod);
					this.logTestRunJenkinResult(jenkinJobName, jenkinBuildId, "PASS", testMethod);
				} else {
					//this.logTestRunResult(testPlanKeyValue.toString(), testCaseKeyValue.toString(), "PASS");
				}			
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	 
	 private void logTestRunJenkinResult(String jenkinJobName, String jenkinBuildId, String status, String testMethod) {
			URL url;
			try {
				url = new URL(this.jenkinEndppoint);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("PUT");
				conn.setRequestProperty("Content-Type", "application/json");
				
				TestRunJenkinRequestBody bodyObj = new TestRunJenkinRequestBody(jenkinJobName, jenkinBuildId, status, testMethod);
							
				ObjectMapper mapper = new ObjectMapper();
				String body = mapper.writeValueAsString(bodyObj);			
				
				OutputStream os = conn.getOutputStream();
				os.write(body.getBytes());
				os.flush();
				
				int resp = conn.getResponseCode();
				
				System.out.println("PUT Request SENT " + resp);
				
				conn.disconnect();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

	@Override
	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		
	}
}
