package com.pukai.stream.util;

import java.util.Collection;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class ValidateUtil {
	
	public static boolean isValid(String str) {
		if (str == null || "".equals(str.trim())) {
			return false ;
		}
		return true ;
	}
	
	@SuppressWarnings("unchecked")
	public static boolean isValid(Collection col) {
		if (col == null || col.isEmpty()) {
			return false ;
		}
		return true ;
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isValid(Map map) {
		if (map == null || map.isEmpty()) {
			return false ;
		}
		return true ;
	}
	
	public static boolean isValid(JSONObject json) {
		if (json == null || json.isEmpty()) {
			return false ;
		}
		return true ;
	}
	
	public static boolean isValid(Object[] arr) {
		if (arr == null || arr.length == 0) {
			return false ;
		}
		return true ;
	}
	
	public static boolean isValid(Object obj) {
		if (obj == null ) {
			return false ;
		}
		return true ;
	}
}
