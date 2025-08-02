package kr.co.ucp.cmm.util;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.ucp.cmm.util.CommonUtil.SYLLABLE_HANGUL;
import kr.co.ucp.link.event.socket.client.CommSocketClient;

public class CommonUtil {

	static Logger logger = LoggerFactory.getLogger(CommonUtil.class);
	private static String delimeter1E = (char) 0x1E + "";
	private static String delimeter1F = (char) 0x1F + "";

	public static int smsSend(String socketOpt, String sendNo, String sendMsg, List<String> recvNoList, List<String> listRecvNm) throws Exception{

    	String msg = "WKSMS" + delimeter1E;
    	// 수신자번호
    	int listNoCnt = recvNoList.size();
    	for(int i = 0; i < listNoCnt; i++) {
    		msg += recvNoList.get(i);
    		if((i + 1) < listNoCnt) msg += delimeter1F;
    	}
    	// 수신자명
    	String recvNm = "";
    	int listNmCnt = listRecvNm.size();
    	for(int i = 0; i < listNmCnt; i++) {
    		recvNm += listRecvNm.get(i);
    		if((i + 1) < listNmCnt) recvNm += delimeter1F;
    	}

    	msg += delimeter1E + sendNo;
    	msg += delimeter1E + sendMsg;
    	msg += delimeter1E + "";
    	msg += delimeter1E + recvNm;
    	msg += delimeter1E + ";";
        // 1.수신자번호 + 2. 송신자연락처 + 3.전송메세지 + 4.전송예약일시 + 5.수신자명 + 6.;
    	Map<String, Object> mapRet = new HashMap<String, Object>();
		CommSocketClient sockSend = new CommSocketClient();
		String resp = "";
  		try{
			mapRet = sockSend.smsSend(msg, socketOpt);
  		}
  		catch(NullPointerException e) {
  			logger.error("smsSend NullPointerException : {}",e.getMessage());
  		}
  		catch(Exception e){
  			logger.error("smsSend Exception : {}",e.getMessage());
  		}
  		resp = (String) mapRet.get("code").toString().trim();

		logger.debug("--> smsSend return message : {}", resp);
		int rtn = 0;
		// 정상처리시 1로 리턴
		if("00000".equals(resp) || "0".equals(resp)) {
			 rtn = 1;
		}
    	return rtn;
	}

	public static boolean checkDataFilter(Map<String, String> map) {
		for (String key : map.keySet()) {
			String value = map.get(key);
			if (value.indexOf("<") >= 0) return false;
			if (value.indexOf(">") >= 0) return false;
		}
		return true;
	}

	public static boolean checkDataFilterObj(Map<String, Object> map) {
		for (String key : map.keySet()) {
			String value = (String) map.get(key);
			//System.out.println("checkDataFilterObj() => "+key+" : "+value);
			if (value.indexOf("<") >= 0) return false;
			if (value.indexOf(">") >= 0) return false;
		}
		return true;
	}

	/*
	 * ESS단독 사회안전망서비스에 추가(20181029)
	 */
	public static String getClientIp(HttpServletRequest request) {
		String clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) {
			clientIp = request.getHeader("REMOTE_ADDR");
		}
		if (null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) {
			clientIp = request.getRemoteAddr();
		}
		return clientIp;
	}

	public static boolean checkIp(HttpServletRequest request, String pIpTyCd, String pIpCd, String pDbIp) {
		boolean bRet = false;

		String ipTyCd = pIpTyCd.toUpperCase().trim();
		String ipCd = pIpCd.toUpperCase().trim();
		String dbIp = pDbIp.toUpperCase().trim();
		if (ipCd.equals("AL")) {
			return true;
		}

		String clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		if (null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) {
			clientIp = request.getHeader("REMOTE_ADDR");
		}
		if (null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) {
			clientIp = request.getRemoteAddr();
		}

		// clientIp = "127.1.2.3";
		// System.out.println("clientIp : " + clientIp);
		// System.out.println("ipTyCd : " + ipTyCd);
		// System.out.println("ipCd : " + ipCd);
		// System.out.println("dbIp : " + dbIp);

		if (ipTyCd.equals("IPV4")) {
			if (ipCd.equals("IP")) {
				if (clientIp.equals(dbIp)) return true;
				else return false;
			}
			/*else if (ipCd.equals("DC")) {

			}*/
			else if (ipCd.equals("CC")) {
				dbIp = dbIp.split("\\.")[0] + "." + dbIp.split("\\.")[1] + "." + dbIp.split("\\.")[2];
				String ip = clientIp.split("\\.")[0] + "." + clientIp.split("\\.")[1] + "." + clientIp.split("\\.")[2];
				if (ip.equals(dbIp)) return true;
				else return false;
			}
			else if (ipCd.equals("BC")) {
				dbIp = dbIp.split("\\.")[0] + "." + dbIp.split("\\.")[1];
				String ip = clientIp.split("\\.")[0] + "." + clientIp.split("\\.")[1];
				if (ip.equals(dbIp)) return true;
				else return false;
			}
			else if (ipCd.equals("AC")) {
				dbIp = dbIp.split("\\.")[0];
				String ip = clientIp.split("\\.")[0];
				if (ip.equals(dbIp)) return true;
				else return false;
			}

		}
		else {
			if (ipCd.equals("8B")) {
				if (clientIp.equals(dbIp)) return true;
				else return false;
			}
			else if (ipCd.equals("7B")) {
				dbIp = dbIp.split("\\.")[0] + "." + dbIp.split("\\.")[1] + "." + dbIp.split("\\.")[2] + "." + dbIp.split("\\.")[3] + "." + dbIp.split("\\.")[4] + "." + dbIp.split("\\.")[5] + "." + dbIp.split("\\.")[6];
				String ip = clientIp.split("\\.")[0] + "." + clientIp.split("\\.")[1] + "." + clientIp.split("\\.")[2] + "." + clientIp.split("\\.")[3] + "." + clientIp.split("\\.")[4] + "." + clientIp.split("\\.")[5] + "." + clientIp.split("\\.")[6];
				if (ip.equals(dbIp)) return true;
				else return false;
			}
			else if (ipCd.equals("6B")) {
				dbIp = dbIp.split("\\.")[0] + "." + dbIp.split("\\.")[1] + "." + dbIp.split("\\.")[2] + "." + dbIp.split("\\.")[3] + "." + dbIp.split("\\.")[4] + "." + dbIp.split("\\.")[5];
				String ip = clientIp.split("\\.")[0] + "." + clientIp.split("\\.")[1] + "." + clientIp.split("\\.")[2] + "." + clientIp.split("\\.")[3] + "." + clientIp.split("\\.")[4] + "." + clientIp.split("\\.")[5];
				if (ip.equals(dbIp)) return true;
				else return false;
			}
			else if (ipCd.equals("5B")) {
				dbIp = dbIp.split("\\.")[0] + "." + dbIp.split("\\.")[1] + "." + dbIp.split("\\.")[2] + "." + dbIp.split("\\.")[3] + "." + dbIp.split("\\.")[4];
				String ip = clientIp.split("\\.")[0] + "." + clientIp.split("\\.")[1] + "." + clientIp.split("\\.")[2] + "." + clientIp.split("\\.")[3] + "." + clientIp.split("\\.")[4];
				if (ip.equals(dbIp)) return true;
				else return false;
			}
			else if (ipCd.equals("4B")) {
				dbIp = dbIp.split("\\.")[0] + "." + dbIp.split("\\.")[1] + "." + dbIp.split("\\.")[2] + "." + dbIp.split("\\.")[3];
				String ip = clientIp.split("\\.")[0] + "." + clientIp.split("\\.")[1] + "." + clientIp.split("\\.")[2] + "." + clientIp.split("\\.")[3];
				if (ip.equals(dbIp)) return true;
				else return false;
			}
			else if (ipCd.equals("3B")) {
				dbIp = dbIp.split("\\.")[0] + "." + dbIp.split("\\.")[1] + "." + dbIp.split("\\.")[2];
				String ip = clientIp.split("\\.")[0] + "." + clientIp.split("\\.")[1] + "." + clientIp.split("\\.")[2];
				if (ip.equals(dbIp)) return true;
				else return false;
			}
			else if (ipCd.equals("2B")) {
				dbIp = dbIp.split("\\.")[0] + "." + dbIp.split("\\.")[1];
				String ip = clientIp.split("\\.")[0] + "." + clientIp.split("\\.")[1];
				if (ip.equals(dbIp)) return true;
				else return false;
			}
			else if (ipCd.equals("1B")) {
				dbIp = dbIp.split("\\.")[0];
				String ip = clientIp.split("\\.")[0];
				if (ip.equals(dbIp)) return true;
				else return false;
			}

		}
		return bRet;
	}

	public static Map<String, String> checkCar(String carNo) {
		boolean bRet = false;

		// System.out.println("checkCar Start");
		Map<String, String> map = new HashMap<String, String>();

		char[] charArray = carNo.toCharArray();
		for (int j = 0; j < charArray.length; j++) {
			if (charArray[j] >= 'A' && charArray[j] <= 'Z' || charArray[j] >= 'a' && charArray[j] <= 'z') {
				map.put("msg", "잘못된 차량번호입니다.(영문)");
				map.put("ret", "false");
				map.put("retCode", "01");
				return map;
			}
		}
		for (int i = 0; i < carNo.length(); i++) {
			if ("~`!@#$%^&*=+\\|[](){};:'<.,>/?_".indexOf(carNo.substring(i, i + 1)) >= 0) {
				map.put("msg", "잘못된 차량번호입니다.(특수문자)");
				map.put("ret", "false");
				map.put("retCode", "02");
				return map;
			}
		}
		// System.out.println("22222222222");
		if (carNo.split("-").length > 2) {
			map.put("msg", "잘못된 차량번호입니다.('-'갯수 초과)");
			map.put("ret", "false");
			map.put("retCode", "03");
			return map;
		}
		// System.out.println("33333");
		int hanCheck = 0;
		int numCheck = 0;
		if (carNo.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
			// System.out.println("한글포함");
			hanCheck = 1;
		}

		int cnt = 0;
		String numValidate = "/^[0-9]*$/";
		if (carNo.indexOf("-") >= 0) {
			for (int j = 0; j < carNo.split("-").length; j++) {
				String num = carNo.split("-")[j];
				cnt = 0;
				charArray = num.toCharArray();
				for (int i = 0; i < charArray.length; i++) {
					if (charArray[i] >= '0' && charArray[i] <= '9') {
						numCheck = 1;
						cnt++;
					}
					else {
						cnt = 0;
					}
				}
				// '-'로 나눠진 두군데 중 cnt==3이면 OK로 판단함.
				// 만약 아니라면 cnt != 3으로 체크해서 판단해야함.
				if (cnt != 3) {
					map.put("msg", "잘못된 차량번호입니다.('-'있고, 연속된 숫자 갯수 오류)");
					map.put("ret", "false");
					map.put("retCode", "04");
					return map;
				}
			}
		}
		else {
			charArray = carNo.toCharArray();
			for (int i = 0; i < charArray.length; i++) {
				if (charArray[i] >= '0' && charArray[i] <= '9') {
					numCheck = 1;
					cnt++;
				}
				else {
					cnt = 0;
				}
			}
			if (cnt != 4) {
				map.put("msg", "잘못된 차량번호입니다.('-'없고, 연속된 숫자 갯수 오류)");
				map.put("ret", "false");
				map.put("retCode", "05");
				return map;
			}
		}
		if (hanCheck != 1 || numCheck != 1) {
			map.put("msg", "차량번호는 한글, 숫자가 필수로 입력되어야 합니다.");
			map.put("ret", "false");
			map.put("retCode", "06");
			return map;
		}

		map.put("msg", "");
		map.put("ret", "true");
		return map;
	}

	// 한글체크
	private static final int HANGUL_UNICODE_START = 0xAC00;
	private static final int HANGUL_UNICODE_END = 0xD7AF;

	enum SYLLABLE_HANGUL {
		FULL_HANGUL, PART_HANGUL, NOT_HANGUL
	}

	public SYLLABLE_HANGUL IsHangul(String text) {
		int textCount = text.length();
		SYLLABLE_HANGUL isSyllableHangul;

		int isHangulCount = 0;

		for (int i = 0; i < textCount; i++) {
			char syllable = text.charAt(i);

			if ((HANGUL_UNICODE_START <= syllable) && (syllable <= HANGUL_UNICODE_END)) {
				isHangulCount++;
			}
		}
		if (isHangulCount == textCount) {
			isSyllableHangul = SYLLABLE_HANGUL.FULL_HANGUL;
		}
		else if (isHangulCount == 0) {
			isSyllableHangul = SYLLABLE_HANGUL.NOT_HANGUL;
		}
		else {
			isSyllableHangul = SYLLABLE_HANGUL.PART_HANGUL;
		}

		return isSyllableHangul;
	}

	public static boolean match(String input, String regexp) {
		Matcher matcher = Pattern.compile(regexp).matcher(input);
		String[] results = new String[matcher.groupCount() + 1];

		return matcher.find();
	}

	// Excel File Create
	public static HttpServletResponse setExcelDownladHeader(HttpServletResponse response, final String fileName) throws Exception {
		// fileName = new String(fileName.getBytes("KSC5601"), "8859_1");
		URLEncoder.encode(fileName, "UTF-8");

		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
		response.setHeader("Content-Description", "JSP Generated Data");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		return response;
	}

	/**
	 * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
	 *
	 * @param object
	 *            원본 객체
	 * @return resultVal 문자열
	 */
	public static String isNullToString(Object object) {
		String string = "";
		if (object != null) {
			string = object.toString().trim();
		}

		return string;
	}

	public static String isNullToDefaultVal(Object object, String args) {
		String string = "";
		if (object != null) {
			string = object.toString().trim();
		}
		else {
			string = args;
		}

		return string;
	}

	/**
	 * 현재일자(yyyymmdd)
	 *
	 * @return String
	 */
	public static String getCurrentYmd() {
		GregorianCalendar calendar = new GregorianCalendar();
		StringBuffer rtnStr = new StringBuffer();

		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.YEAR)), 4, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.MONTH) + 1), 2, "0"));
		rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.DATE)), 2, "0"));

		return rtnStr.toString();
	}

	/**
	 * 현재시간 구하기 14자리(년월일시분초)
	 *
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
	 *
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
	 *
	 * @param source
	 *            : 문자열
	 * @param digit
	 *            : 자리수
	 * @param fillString
	 *            : 채울문자
	 * @return String
	 */
	public static String checkByte(String source, int digit, String fillString) {
		String rtnStr = "";

		if (source.length() < digit) {
			for (int i = 0; i < digit - source.length(); i++) {
				rtnStr += fillString;
			}
		}

		rtnStr += source;

		return rtnStr;
	}

	/*
	 * getuuid 22자
	 */
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String strUUID = toUnsignedString(uuid.getMostSignificantBits(), 6) + toUnsignedString(uuid.getLeastSignificantBits(), 6);
		return strUUID;
	}

	/*
	 * uuid 자리수 줄리기
	 */
	public static String toUnsignedString(long i, int shift) {
		char[] buf = new char[64];
		int charPos = 64;
		int radix = 1 << shift;
		long mask = radix - 1;
		long number = i;
		do {
			buf[--charPos] = DIGITS[(int) (number & mask)];
			number >>>= shift;
		}
		while (number != 0);
		return new String(buf, charPos, (64 - charPos));
	}

	final static char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_', '*' // '.', '-'
	};

	/**
	 * Compares two number strings.
	 *
	 * Use this instead of String.compareTo() for a non-lexicographical comparison that works for version strings. e.g. "1.10".compareTo("1.6").
	 *
	 * @param v1
	 *            a string of alpha numerals separated by decimal points.
	 * @param v2
	 *            a string of alpha numerals separated by decimal points.
	 * @return The result is 1 if v1 is greater than v2.
	 * The result is 2 if v2 is greater than v1.
	 * The result is -1 if the version format is unrecognized.
	 * The result is zero if the strings are equal.
	 */

	public static int comparesTwoNumberStrings(String v1, String v2) {
		int v1Len = StringUtils.countMatches(v1, ".");
		int v2Len = StringUtils.countMatches(v2, ".");

		if (v1Len != v2Len) {
			int count = Math.abs(v1Len - v2Len);
			if (v1Len > v2Len) for (int i = 1; i <= count; i++)
				v2 += ".0";
			else for (int i = 1; i <= count; i++)
				v1 += ".0";
		}

		if (v1.equals(v2)) return 0;

		String[] v1Str = StringUtils.split(v1, ".");
		String[] v2Str = StringUtils.split(v2, ".");
		for (int i = 0; i < v1Str.length; i++) {
			String str1 = "", str2 = "";
			for (char c : v1Str[i].toCharArray()) {
				if (Character.isLetter(c)) {
					int u = c - 'a' + 1;
					if (u < 10) str1 += String.valueOf("0" + u);
					else str1 += String.valueOf(u);
				}
				else str1 += String.valueOf(c);
			}
			for (char c : v2Str[i].toCharArray()) {
				if (Character.isLetter(c)) {
					int u = c - 'a' + 1;
					if (u < 10) str2 += String.valueOf("0" + u);
					else str2 += String.valueOf(u);
				}
				else str2 += String.valueOf(c);
			}
			v1Str[i] = "1" + str1;
			v2Str[i] = "1" + str2;

			int num1 = Integer.parseInt(v1Str[i]);
			int num2 = Integer.parseInt(v2Str[i]);

			if (num1 != num2) {
				if (num1 > num2) return 1;
				else return 2;
			}
		}
		return -1;
	}
}
