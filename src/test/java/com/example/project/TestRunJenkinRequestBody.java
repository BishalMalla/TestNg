package com.example.project;

public class TestRunJenkinRequestBody {
	public String jenkin_job_name;
	public String jenkin_build_id;
	public String status;
	public String test_method;
	
	public TestRunJenkinRequestBody(String jenkin_job_name, String jenkin_build_id, String status, String test_method) {
		super();
		this.jenkin_job_name = removeQuotes(jenkin_job_name);
		this.jenkin_build_id = jenkin_build_id;
		this.status = status;
		this.test_method = test_method;
	}
	
	private String removeQuotes(String name) {
		int len = name.length();
		
		if(name.charAt(0) == '"' && name.charAt(len-1) == '"') {
			name = name.substring(1, len - 1);
		}
		
		return name;
	}
}
