/**
* ----------------------------------------------------------------------------------------------
* @Class Name : CommUtil.java
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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommUtil {
	static Logger logger = LoggerFactory.getLogger(CommUtil.class);

	private static int SEQ = 0;

	// 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
	public static String objNullToVal(Object object, String dVal) {
		String rtn = dVal;
		try {
			if (object != null) {
				rtn = String.valueOf(object).replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
				if ("null".equalsIgnoreCase(rtn)) {
					rtn = dVal;
				}
			}
		} catch (Exception e) {
			rtn = dVal;
		}
		return rtn;
	}

	public static String nullConvert(Object src) {
		// if (src != null && src.getClass().getName().equals("java.math.BigDecimal")) {
		if (src != null && src instanceof java.math.BigDecimal) {
			return ((BigDecimal) src).toString();
		}

		if (src != null && src instanceof java.lang.Number) {
			return ((Number) src).toString();
		}

		String rtn = "";
		try {
			if (src != null) {
				rtn = String.valueOf(src).replaceAll("(^\\p{Z}+|\\p{Z}+$)", "").trim();
				if ("null".equalsIgnoreCase(rtn)) {
					rtn = "";
				}
			}
		} catch (Exception e) {
			rtn = "";
		}
		return rtn;
	}
	/**
	 * null 이면 ""공백으로
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public static String nullChk(String str)  {

		if(str == null){
			return "";
		}else if(str.equals("null")){
			return "";
		}else if(str.equals("NULL")){
			return "";
		}else{
			return str;
		}
	}

	public static Boolean getObjBoolean(Object object) {
		try {
			if (object != null)	{
				return Boolean.valueOf(object.toString());
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}


	public static boolean isNum(String str)
	{
	  try
	  {
	    double d = Double.parseDouble(str);
	  }
	  catch(NumberFormatException nfe)
	  {
	    return false;
	  }
	  return true;
	}

	public static String isNullObjToVal(Object object, String dVal) {
		String rtn = dVal;
		try {
			if (object != null)	{
				rtn = String.valueOf(object).replaceAll("(^\\p{Z}+|\\p{Z}+$)","");
				if ("null".equalsIgnoreCase(rtn)) {
					rtn = dVal;
				}
			}
		} catch (Exception e) {
			rtn = dVal;
		}
		return rtn;
	}


	/**
	 * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
	 * @param object 원본 객체
	 * @return resultVal 문자열
	 */
	public static String isNullToString(Object object)
	{
		String string = "";
		if (object != null) {
			string = object.toString().trim();
		}

		return string;
	}
	/*
	 * str to int null 이면 zero
	 */
	public static int objStrToInt(Object object) {
		String str = "0";
		int val = 0;
		if (object != null) {
			str = object.toString().replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
		}

		try {
			val = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			val = 0;
		}
		return val;
	}

	/*
	 * string to double null 이면 zero
	 */
	public static double objStrToDouble(Object object) {
		String str = "0";
		double val = 0;
		if (object != null) {
			str = object.toString().replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
		}

		try {
			val = Double.valueOf(str);
		} catch (NumberFormatException e) {
			val = 0;
		}
		return val;
	}

	/**
	 * String 문자열을 boolean으로 변환
	 * @param 	paramStr : 변경할 문자열
	 * @return 	boolean
	 */
	public static boolean strToBoolean(String paramStr){
		boolean result = false;
		if(paramStr.equals("Y")||paramStr.equals("y"))
		{
			result = true;
		}
		return result;
	}

	/**
	 * LPAD, RPAD 처리
	 * @param 	str 	: 입력문자열
	 * @param 	padLen	: 총자리수
	 * @param 	padChar	: 채워야할 문자
	 * @param 	padFlag	: 'L' LPAD, 'R' RPAD 구분
	 * @return 	String
	 */
	public static String setPad(String str, int padLen, String padChar, String padFlag){
		if (str == null) str = "";

		// LPAD
		if( padFlag.equals("L") ){
			while (str.length() < padLen)
			{
				str = padChar + str;
			}
		// RPAD
		}else if( padFlag.equals("R") ){
			while (str.length() < padLen)
			{
				str =  str + padChar;
			}
		}

		return str;
	}

	/**
	 * 한글을 EUC-KR 형식의 캐릭터 셋으로 변경
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String toHan(String str) throws UnsupportedEncodingException {

		if ( null == str ) {
			return null;
		} else {
			return new String(str.getBytes("ISO-8859-1"),"euc-kr");
		}
	}

	/**
	 * 주어진 문자열이 문자로만 구성되어 있는지 확인한다. 공백은 false를 반환한다.
	 * 입력된 String을 대문자로 모두 변환한다. (알파벳은 65(A)에서 90(Z)까지이다.)
	 * @param	String	: 문자열
	 * @return 	boolean	: 문자열이 모두 문자로만 이루어진경우 true
	 */
	public static boolean isAlpha(String str) {

		if ( null == str || "".equals(str)) return false;

		for (int i=0; i<str.length(); i++)
		{
			if ((int)str.charAt(i) < 65 && ((int)str.charAt(i) != 32)) return false;
		}
		return true;
	}

	/**
	 * 주어진 문자열이 숫자로만 구성되어 있는지 확인한다. 공백이 있으면 false를 반환한다.
	 * char값이 48에서 57사이이면 숫자이다.
	 * @param	String	: 문자열
	 * @return 	boolean	: 문자열이 모두 숫자로만 이루어진경우 true
	 */
	public static boolean isNumeric(String str) {
		if ( null == str || "".equals(str)) return false;

		for (int i=0; i<str.length(); i++)
		{
			if ((int)str.charAt(i) < 48 || (int)str.charAt(i) > 57) return false;
		}
		return true;
	}

	/**
	 * escape 된 문자열 unescape
	 * @param str
	 * @return
	 */
	public static String unescape(String str) {

		String rtnStr = new String();
		char [] arrInp = str.toCharArray();
		int cnt = arrInp.length;
		int i;

		for(i=0; i<cnt; i++)
		{
			if(arrInp[i] == '%') {
				String hex;
				if(arrInp[i+1] == 'u') {    //유니코드.
					hex = str.substring(i+2, i+6);
					i += 5;
				} else {    //ascii
					hex = str.substring(i+1, i+3);
					i += 2;
				}

				try{
					rtnStr += new String(Character.toChars(Integer.parseInt(hex, 16)));
				} catch(NumberFormatException e) {
					rtnStr += "%";
					i -= (hex.length()>2 ? 5 : 2);
				}
			} else {
				rtnStr += arrInp[i];
			}
		}
		return rtnStr;
	}

	/**
	 * UniqueID 생성(20자리)
	 * @param prefix : prefix
	 * @return prefix_현재시간17자리
	 */
	public static String getUniqueID(String prefix){
		String rtnStr = "";
		rtnStr = prefix + "_" + getCurrentTime14()+getNextSequenceValue();

		return rtnStr;
	}

	/**
	 * UniqueID 생성(20자리)
	 * @return	String(현재시간17자리 + 랜덤3자리)
	 */
	public static String getUniqueID(){
		String rtnStr = "";
		rtnStr =getCurrentTime17()+getNextSequenceValue();
		return rtnStr;
	}

	/**
	 * Random 숫자 구하기
	 * @param 	length : 자리수
	 * @return	String
	 */
	public static String getRandomValue(int length){
		String rtnStr = "";
		Random rand = new Random(System.currentTimeMillis());

		int ranVal = Math.abs(rand.nextInt());
		int ranVal2 = Math.abs(rand.nextInt());
		int ranVal3 = ranVal + ranVal2;
		String ranStr = Integer.toString(ranVal3);
		rtnStr = ranStr.substring(ranStr.length()- length);

		return rtnStr;
	}

	/**
	 * 현재시간 구하기 14자리(년월일시분초)
	 * @return String
	 */
	public static String getCurrentTime14() {
		GregorianCalendar calendar = new GregorianCalendar();
		StringBuffer rtnStr = new StringBuffer();

		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.YEAR)), 4, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.MONTH) + 1), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.DATE)), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.MINUTE)), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.SECOND)), 2, "0"));

		return rtnStr.toString();
	}

	/**
	 * 현재시간 구하기 17자리(년월일시분초SSS)
	 * @return String
	 */
	public static String getCurrentTime17() {
		GregorianCalendar calendar = new GregorianCalendar();
		StringBuffer rtnStr = new StringBuffer();

		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.YEAR)), 4, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.MONTH) + 1), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.DATE)), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.HOUR_OF_DAY)), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.MINUTE)), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.SECOND)), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.MILLISECOND)), 3, "0"));

		return rtnStr.toString();
	}

	/**
	 * 자리수 채우기
	 * @param 	source : 문자열
	 * @param 	digit  : 자리수
	 * @param 	fillString : 채울문자
	 * @return	String
	 */
	public static String checkByte(String source, int digit, String fillString) {

		String rtnStr = "";

		if (source.length() < digit)
		{
			for (int i = 0; i < digit - source.length(); i++)
			{
				rtnStr += fillString;
			}
		}

		rtnStr += source;

		return rtnStr;

	}

	public static String removeInvalidXMLCharacters(String s) {

		StringBuilder out = new StringBuilder();

		int i=0;
		int codePoint;

		while(i<s.length())
		{
			// This is the unicode code of the character.
			codePoint = s.codePointAt(i);
			// Consider testing larger ranges first to improve speed.
			if ((codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || (codePoint == 0xdc21) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
					|| ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)))
			{
				out.append(Character.toChars(codePoint));
			}

			i+= Character.charCount(codePoint);		// Increment with the number of code units(java chars) needed to represent a Unicode char.
		}

		return out.toString();

	}

	/**
	 * 시퀀스값 생성 3자리
	 * @return String
	 */
	public static String getNextSequenceValue(){

		String rtnStr = Integer.toString(SEQ);
		if( 999 == SEQ ) {
			SEQ = 0;
		}

		if( 1 == rtnStr.length() ) {
			rtnStr = "00" + rtnStr;
		} else if ( 2 == rtnStr.length() ) {
			rtnStr = "0" + rtnStr;
		}

		SEQ++;
		return rtnStr;
	}
	public static String getCurrentTimeYear() {
		GregorianCalendar calendar = new GregorianCalendar();
		StringBuffer rtnStr = new StringBuffer();

		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.YEAR)), 4, "0"));
		return rtnStr.toString();
	}

	public static Map<String, Object> jsonToMap(String json) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		// json to map
		ObjectMapper objMap = new ObjectMapper();
		try {
			jsonMap  = objMap.readValue(json, new TypeReference<Map<String, Object>>(){});
		} catch (JsonParseException e) {
			logger.error(e.getMessage());
		} catch (JsonMappingException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		return jsonMap;
   }

	public static Map<String, Object> xmlToMap(String xmlStr)  throws Exception {
		Map<String, Object> xmlMap = new HashMap<String, Object>();

        JSONObject json = XML.toJSONObject(xmlStr);
        String jsonStr = json.toString();

        xmlMap = CommUtil.jsonToMap(jsonStr);

		return xmlMap;
   }

	public String mapToJson(Map<String, Object> map) throws Exception
	{
	    ObjectMapper mapper = new ObjectMapper();

	    return mapper.writeValueAsString(map);
	}
}
