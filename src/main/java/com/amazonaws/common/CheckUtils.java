package com.amazonaws.common;

public class CheckUtils {

	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isStrEquals(String str1, String str2) {
		if (isNull(str1)) {
			str1 = "";
		}
		if (isNull(str2)) {
			str2 = "";
		}
		return str1.equals(str2);
	}

}
