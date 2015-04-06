package Util;

import java.util.List;

public class FancyString {
	
	public static String join(String glue, List<String> values) {
		String string = new String();
		boolean first = true;
		for(String value : values) {
			if(first) {
				string = value;
				first = false;
			} else {
				string += glue + value;
			}
		}
		return string;
	}

}
