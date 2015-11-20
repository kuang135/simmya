package com.simmya.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilePathUtil {
	
	public static String createPath() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(date);
		return File.separator + format.replace("-", File.separator);
	}

}
