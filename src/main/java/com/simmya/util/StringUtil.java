package com.simmya.util;



public class StringUtil {

	public static String insertTagByDistance(String target, String tag, int distance) {
		int times = target.length()/distance;
		if ((target.length() % distance) != 0)
			times++;
		String[] arr = new String[times];
		if (times != 0) {
			for (int i =1; i <= times; i++) {
				int begin = (i - 1) * distance;
				int end = i * distance;
				if (end > target.length())
					end = target.length();
				arr[i-1] = target.substring(begin, end);
			}
		} else {
			arr[0] = target;
		}
		StringBuilder sb = new StringBuilder();
		for (int i =0; i < arr.length; i++) {
			sb.append(arr[i]);
			if (i != arr.length - 1) 
				sb.append(tag);
		}
		return sb.toString();
	}

}


