package com.clommunicate.utils;

import java.util.Map;
import java.util.TreeMap;

public class WebApi {
	
	public static final String API = "http://api-clommunicate.rhcloud.com/api";
	public static final Map<Integer,String> statusCodes;
	
	static{
		
		statusCodes = new TreeMap<Integer, String>();

		statusCodes.put(500, "Internal Server Error.");
		statusCodes.put(501, "Not Implemented.");
		statusCodes.put(502, "Bad gateway.");
		statusCodes.put(500, "Service Unavailable.");
		statusCodes.put(404, "Not found.");
		
	}
	
	public static boolean isBadStatusCode(int code)	{
		
		return statusCodes.containsKey(code);
		
	}
	
}
