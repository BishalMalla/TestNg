package com.example.project;


import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
@Listeners(ListenerTest.class)
public class TestNgTest {
	@Test(priority=1)
	public void doLogin() {
		System.out.println("DO login test");
	}
	
	@Test(priority=2)
	public void doPasswordChange() {
		System.out.println("changing password");
	}
	
	@Test(priority=3)
	public void doLogout() {
		System.out.println("Logging out");
	}
	
	@Test(priority=4)
	public void valueMatch() {
		String expVal = "A";
		String actVal = "A";
		Assert.assertEquals(actVal, expVal);
	}
	
	@Test(priority=5)
	void negativeTestCase() {
		Assert.assertTrue(1>2,"The item is lesser than");
	}
}
