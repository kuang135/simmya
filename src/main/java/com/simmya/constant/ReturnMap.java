package com.simmya.constant;

import java.util.HashMap;
import java.util.Map;

public class ReturnMap {

	public static final Map<String, Object> BLANK;
	public static final Map<String, Object> FAULT;
	public static final Map<String, Object> SUCCESS;
	
	static {
		BLANK = new HashMap<String, Object>();
		BLANK.put("code", "error");
		BLANK.put("desc", 10001);
		
		FAULT = new HashMap<String, Object>();
		FAULT.put("code", "error");
		FAULT.put("desc", 100002);
		
		SUCCESS = new HashMap<String, Object>();
		SUCCESS.put("code", "sucess");
		SUCCESS.put("desc", "成功");
	}
	
	
}