/**
* ----------------------------------------------------------------------------------------------
* @Class Name : CamelMap.java
* @Description : 공통유틸리티
* @Version : 1.0
* Copyright (c) 2015 by KR.CO.UCP All rights reserved.
* @Modification Information
* ----------------------------------------------------------------------------------------------
* DATE AUTHOR DESCRIPTION
* ----------------------------------------------------------------------------------------------
* 2015.01.08.   widecube Space  최초작성
*
* ----------------------------------------------------------------------------------------------
*/
package kr.co.ucp.cmns;

import java.util.HashMap;

public class CamelMap extends HashMap<String, Object>  {

	private static final long serialVersionUID = -7700790403928325865L;

	@Override
	public Object put(String key, Object value)  {
		return super.put(convertUnderscoreNameToPropertyName((String) key), value);
//		return super.put(JdbcUtils.convertUnderscoreNameToPropertyName((String)key), value);
	}
	/**
	 * Convert a column name with underscores to the corresponding property name using "camel case".
	 * A name like "customer_number" would match a "customerNumber" property name.
	 * @param name the column name to be converted
	 * @return the name using "camel case"
	 */

	public static String convertUnderscoreNameToPropertyName(String name) {
		StringBuilder result = new StringBuilder();
		boolean nextIsUpper = false;
		if (name != null && name.length() > 0) {
			if (!name.contains("_") && Character.isLowerCase(name.charAt(0))) {
				return name;
			}
			/*
			if (name.length() > 1 && name.charAt(1) == '_') {
				result.append(Character.toUpperCase(name.charAt(0)));
			}
			else {
				result.append(Character.toLowerCase(name.charAt(0)));
			}
			*/
			result.append(Character.toLowerCase(name.charAt(0)));
			for (int i = 1; i < name.length(); i++) {
				char c = name.charAt(i);
				if (c == '_') {
					nextIsUpper = true;
				}
				else {
					if (nextIsUpper) {
						result.append(Character.toUpperCase(c));
						nextIsUpper = false;
					}
					else {
						result.append(Character.toLowerCase(c));
					}
				}
			}
		}
		return result.toString();
	}

}
