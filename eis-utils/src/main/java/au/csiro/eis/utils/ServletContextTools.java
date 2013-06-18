package au.csiro.eis.utils;

import javax.servlet.ServletContext;

public class ServletContextTools {

	public static String getRealPath(String filePath, ServletContext c) {
		return c.getRealPath(filePath);
	}
	

	
}
