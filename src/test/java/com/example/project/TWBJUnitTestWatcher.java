package com.example.project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TWBJUnitTestWatcher implements TestWatcher {
	// URI Endpoints
	private String jenkinEndppoint = "http://10.25.33.47:8084/ords/dev_anthem/twb/service/jenkin";

	@Override
	public void testDisabled(ExtensionContext context, Optional<String> reason) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testSuccessful(ExtensionContext context) {
		// TODO Auto-generated method stub
		try {
			// fetch the object instance
			Object testObj = context.getRequiredTestInstance();
			
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
				String testMethod = context.getDisplayName();
				
				System.out.println(this.getClass().getResource("/proj.properties").getPath());
				System.out.println(jenkinJobName);
				System.out.println(jenkinBuildId);
				//this.logTestRunJenkinResult(jenkinJobName, jenkinBuildId, "PASS", testMethod);
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

	@Override
	public void testAborted(ExtensionContext context, Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void testFailed(ExtensionContext context, Throwable cause) {
		// TODO Auto-generated method stub
		try {
			// fetch the object instance
			Object testObj = context.getRequiredTestInstance();
			
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
				String testMethod = context.getDisplayName();
				
				System.out.println(this.getClass().getResource("/proj.properties").getPath());
				System.out.println(jenkinJobName);
				System.out.println(jenkinBuildId);
				//this.logTestRunJenkinResult(jenkinJobName, jenkinBuildId, "FAIL", testMethod);
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


	/**
	 * call TWB REST Services for logging Jenkin Build Results
	 * @param bambooPlanKey
	 * @param bambooJobKey
	 * @param bambooBuildKey
	 * @param status
	 * @param testMethod
	 */
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
}
