/**
 * 
 *
 * @author Sam Liew 26 Dec 2022 7:53:38 PM
 */
package avalon.utils;

/**
 * @author Sam Liew 26 Dec 2022 7:53:38 PM
 *
 */
public final class StringUtil {

	public static String capitalize(String s) 
	{
		if (s == null || s.isEmpty())
			return s;
		
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}
	
}

