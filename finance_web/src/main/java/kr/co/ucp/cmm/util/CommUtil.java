/**
 * ----------------------------------------------------------------------------------------------
 *
 * @Class Name : CommUtil.java
 * @Description : 공통유틸리티
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015.01.08.   widecube Space  최초작성
 * <p>
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.cmm.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CommUtil {
	static Logger logger = LoggerFactory.getLogger(CommUtil.class);

    private static int SEQ = 0;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);

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
    /**
     * null 이면 ""공백으로
     *
     * @param str
     * @return
     * @throws Exception
     */
    public static String nullChk(String str) {
        if (str == null) {
            return "";
        } else if ("null".equalsIgnoreCase(str)) {
            return "";
        } else {
            return str;
        }
    }

    public static String objNullToStr(Object object) {
        String str = "";
        if (object != null) {
			str = object.toString().replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
            if ("null".equalsIgnoreCase(str)) {
                str = "";
            }
        }
        return str;
    }

    /**
     * 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
     *
     * @param object 원본 객체
     * @return resultVal 문자열
     */
    public static String isNullToString(Object object) {
        String str = "";
        if (object != null) {
			str = String.valueOf(object).replaceAll("(^\\p{Z}+|\\p{Z}+$)","");
            if ("null".equalsIgnoreCase(str)) {
                str = "";
            }
        }
        return str;
    }

    /*
     * str to int null 이면 zero
     */
    public static int objStrToInt(Object object) {
        String str = "0";
        int val = 0;
        if (object != null) {
            str = object.toString().trim();
        }

        try {
            val = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            val = 0;
        }
        return val;
    }

    /**
     * String 문자열을 boolean으로 변환
     *
     * @param paramStr : 변경할 문자열
     * @return boolean
     */
    public static boolean strToBoolean(String paramStr) {
        boolean result = false;
        if (paramStr.equals("Y") || paramStr.equals("y")) {
            result = true;
        }
        return result;
    }

    /**
     * LPAD, RPAD 처리
     *
     * @param str     : 입력문자열
     * @param padLen  : 총자리수
     * @param padChar : 채워야할 문자
     * @param padFlag : 'L' LPAD, 'R' RPAD 구분
     * @return String
     */
    public static String setPad(String str, int padLen, String padChar, String padFlag) {
        if (str == null) str = "";

        // LPAD
        if (padFlag.equals("L")) {
            while (str.length() < padLen) {
                str = padChar + str;
            }
            // RPAD
        } else if (padFlag.equals("R")) {
            while (str.length() < padLen) {
                str = str + padChar;
            }
        }

        return str;
    }

    /**
     * 한글을 EUC-KR 형식의 캐릭터 셋으로 변경
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String toHan(String str) throws UnsupportedEncodingException {

        if (null == str) {
            return null;
        } else {
            return new String(str.getBytes("ISO-8859-1"), "euc-kr");
        }
    }

    /**
     * 주어진 문자열이 문자로만 구성되어 있는지 확인한다. 공백은 false를 반환한다.
     * 입력된 String을 대문자로 모두 변환한다. (알파벳은 65(A)에서 90(Z)까지이다.)
     *
     * @return boolean    : 문자열이 모두 문자로만 이루어진경우 true
     */
    public static boolean isAlpha(String str) {

        if (null == str || "".equals(str)) return false;

        for (int i = 0; i < str.length(); i++) {
            if ((int) str.charAt(i) < 65 && ((int) str.charAt(i) != 32)) return false;
        }
        return true;
    }

    /**
     * 주어진 문자열이 숫자로만 구성되어 있는지 확인한다. 공백이 있으면 false를 반환한다.
     * char값이 48에서 57사이이면 숫자이다.
     *
     * @return boolean    : 문자열이 모두 숫자로만 이루어진경우 true
     */
    public static boolean isNumeric(String str) {
        if (null == str || "".equals(str)) return false;

        for (int i = 0; i < str.length(); i++) {
            if ((int) str.charAt(i) < 48 || (int) str.charAt(i) > 57) return false;
        }
        return true;
    }

    public static boolean isAlphabetAndNumeric(String str) {
    	return Pattern.matches("^[0-9a-zA-Z]*$", str);
    }

    public static boolean isAlphabetAndNumeric2(String str, boolean isAlphabetFirstChar) {
        char chrInput;
        for (int i = 0; i < str.length(); i++) {
            chrInput = str.charAt(i); // 입력받은 텍스트에서 문자 하나하나 가져와서 체크
            if (chrInput >= 0x61 && chrInput <= 0x7A) {
                // 영문(소문자) OK!
            } else if (chrInput >= 0x41 && chrInput <= 0x5A) {
                // 영문(대문자) OK!
            } else if (chrInput >= 0x30 && chrInput <= 0x39) {
                // 숫자 OK!
                if(isAlphabetFirstChar && i == 0) return false;
            } else {
                return false;   // 영문자도 아니고 숫자도 아님!
            }
        }
        return true;
    }

    /**
     * escape 된 문자열 unescape
     *
     * @param str
     * @return
     */
    public static String unescape(String str) {

        String rtnStr = "";
        char[] arrInp = str.toCharArray();
        int cnt = arrInp.length;
        int i;

        for (i = 0; i < cnt; i++) {
            if (arrInp[i] == '%') {
                String hex;
                if (arrInp[i + 1] == 'u') {    //유니코드.
                    hex = str.substring(i + 2, i + 6);
                    i += 5;
                } else {    //ascii
                    hex = str.substring(i + 1, i + 3);
                    i += 2;
                }

                try {
                    rtnStr += new String(Character.toChars(Integer.parseInt(hex, 16)));
                } catch (NumberFormatException e) {
                    rtnStr += "%";
                    i -= (hex.length() > 2 ? 5 : 2);
                }
            } else {
                rtnStr += arrInp[i];
            }
        }
        return rtnStr;
    }

    /**
     * UniqueID 생성(20자리)
     *
     * @param prefix : prefix
     * @return prefix_현재시간17자리
     */
    public static String getUniqueID(String prefix) {
        String rtnStr = "";
        rtnStr = prefix + "_" + getCurrentTime14() + getNextSequenceValue();

        return rtnStr;
    }

    /**
     * UniqueID 생성(20자리)
     *
     * @return String(현재시간17자리 + 랜덤3자리)
     */
    public static String getUniqueID() {
        String rtnStr = "";
        rtnStr = getCurrentTime17() + getNextSequenceValue();
        return rtnStr;
    }

    /**
     * Random 숫자 구하기
     *
     * @param length : 자리수
     * @return String
     */
    public static String getRandomValue(int length) {
        String rtnStr = "";
        Random rand = new Random(System.currentTimeMillis());

        int ranVal = Math.abs(rand.nextInt(Integer.MAX_VALUE));
        int ranVal2 = Math.abs(rand.nextInt(Integer.MAX_VALUE));
        int ranVal3 = ranVal + ranVal2;
        String ranStr = Integer.toString(ranVal3);
        rtnStr = ranStr.substring(ranStr.length() - length);

        return rtnStr;
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

    public static String getCurrentTimeYmd() {
        GregorianCalendar calendar = new GregorianCalendar();
        StringBuffer rtnStr = new StringBuffer();

        rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.YEAR)), 4, "0"));
        rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.MONTH) + 1), 2, "0"));
        rtnStr.append(checkByte(Integer.toString(calendar.get(Calendar.DATE)), 2, "0"));

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
     * 현재시간기준 계산
     *
     * @return Date
     */
    public static Date getCalDate(String calType, int calValue) {

        Calendar nowTime = Calendar.getInstance();

        if ("YEAR".equals(calType)) {
            nowTime.add(Calendar.YEAR, -calValue);
        } else if ("MONTH".equals(calType)) {
            nowTime.add(Calendar.MONTH, -calValue);
        } else if ("DATE".equals(calType)) {
            nowTime.add(Calendar.DATE, -calValue);
        } else if ("HOUR_OF_DAY".equals(calType)) {
            nowTime.add(Calendar.HOUR_OF_DAY, -calValue);
        } else if ("MINUTE".equals(calType)) {
            nowTime.add(Calendar.MINUTE, -calValue);
        } else if ("SECOND".equals(calType)) {
            nowTime.add(Calendar.SECOND, -calValue);
        }
        return nowTime.getTime();
    }

    /**
     * 현재시간기준 계산
     *
     * @return String
     */
    public static String getCalDateString(String calType, int calValue) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss", Locale.KOREAN);
        Calendar nowTime = Calendar.getInstance();

        if ("YEAR".equals(calType)) {
            nowTime.add(Calendar.YEAR, -calValue);
        } else if ("MONTH".equals(calType)) {
            nowTime.add(Calendar.MONTH, -calValue);
        } else if ("DATE".equals(calType)) {
            nowTime.add(Calendar.DATE, -calValue);
        } else if ("HOUR_OF_DAY".equals(calType)) {
            nowTime.add(Calendar.HOUR_OF_DAY, -calValue);
        } else if ("MINUTE".equals(calType)) {
            nowTime.add(Calendar.MINUTE, -calValue);
        } else if ("SECOND".equals(calType)) {
            nowTime.add(Calendar.SECOND, -calValue);
        }
        return sdf1.format(nowTime.getTime());
    }

    /**
     * 현재시간기준 계산
     *
     * @return String
     */

    public static String getCalDateStr(String calType, int calValue, String fmt) {

//		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMddHHmmss");

        SimpleDateFormat sdf1 = new SimpleDateFormat(fmt, Locale.KOREAN);
        Calendar nowTime = Calendar.getInstance();

        if ("YEAR".equals(calType)) {
            nowTime.add(Calendar.YEAR, +calValue);
        } else if ("MONTH".equals(calType)) {
            nowTime.add(Calendar.MONTH, +calValue);
        } else if ("DATE".equals(calType)) {
            nowTime.add(Calendar.DATE, +calValue);
        } else if ("HOUR_OF_DAY".equals(calType)) {
            nowTime.add(Calendar.HOUR_OF_DAY, +calValue);
        } else if ("MINUTE".equals(calType)) {
            nowTime.add(Calendar.MINUTE, +calValue);
        } else if ("SECOND".equals(calType)) {
            nowTime.add(Calendar.SECOND, +calValue);
        }
        return sdf1.format(nowTime.getTime());
    }

    public static String getTimeStr(String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.KOREA);
		Calendar cal = Calendar.getInstance();
		return sdf.format(cal.getTime());
    }

    public static boolean isValidTimeStr(String fmt, String date) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(fmt, Locale.KOREA);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * 자리수 채우기
     *
     * @param source     : 문자열
     * @param digit      : 자리수
     * @param fillString : 채울문자
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

    public static String removeInvalidXMLCharacters(String s) {

        StringBuilder out = new StringBuilder();

        int i = 0;
        int codePoint;

        while (i < s.length()) {
            // This is the unicode code of the character.
            codePoint = s.codePointAt(i);
            // Consider testing larger ranges first to improve speed.
            if ((codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || (codePoint == 0xdc21) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF))
                    || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF))) {
                out.append(Character.toChars(codePoint));
            }

            i += Character.charCount(codePoint);        // Increment with the number of code units(java chars) needed to represent a Unicode char.
        }

        return out.toString();

    }

    /**
     * 시퀀스값 생성 3자리
     *
     * @return String
     */
    public static String getNextSequenceValue() {

        String rtnStr = Integer.toString(SEQ);
        if (999 == SEQ) {
            SEQ = 0;
        }

        if (1 == rtnStr.length()) {
            rtnStr = "00" + rtnStr;
        } else if (2 == rtnStr.length()) {
            rtnStr = "0" + rtnStr;
        }

        SEQ++;
        return rtnStr;
    }

    /**
     * 널, 빈값 체크
     *
     * @param str
     * @return
     */

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 요청 데이터를 Map 형태로 리턴
     *
     * @param request
     * @return
     */
    public static Map<String, Object> jsonPars(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();

        String json = request.getParameter("sData");
        try {
            map = mapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            // TODO Auto-generated catch block
            map.put("", "");
        }
//		JSONObject jo = JSONObject.fromObject(json);
//
//		Iterator<String> iterator = jo.keySet().iterator();				//맵의 모든 키를 받아옴
//		while(iterator.hasNext())
//		{
//			String key = iterator.next();
//			String value = jo.getString(key);
//			param.put(key, value);
//		}
        return map;
    }

    /**
     * 요청 데이터를 Map 형태로 리턴
     *
     * @param request
     * @return
     */
    public static Map<String, Object> dataPars(HttpServletRequest request) {
        Map<String, Object> param = new HashMap();
        Map<String, String[]> pMap = request.getParameterMap();

        Iterator<String> iterator = pMap.keySet().iterator();                //맵의 모든 키를 받아옴

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = request.getParameter(key);
            if (key.equals("mobileNo")) {
                if (0 == value.indexOf("+")) {
                    value = "0" + value.substring(3);
                }
            }
            param.put(key, value);
        }
        return param;
    }

    private static boolean makeDiractory(String path) {
        boolean result = false;
        try {
            if (path != null && !"".equals(path)) {
                path = path.replaceAll("\\.", "");
                path = path.replaceAll("\\&", "");
            }

            File directory = new File(path);

            if (!directory.exists()) {
                result = directory.mkdirs();
            } else {
                result = true;
            }
        } catch (NullPointerException e) {
            result = false;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /*
     * List Map to csv
     */
    public static int listMapToCsv(List<LinkedHashMap<String, String>> listMap, String path, String fileName) {
        int val = 0;
        if (makeDiractory(path)) {
            String filePath = path + File.separator + fileName;
            List<String> headers = listMap.stream().flatMap(map -> map.keySet().stream()).distinct().collect(Collectors.toList());
            try {
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, false), "UTF-8"));

                for (String string : headers) {
//	                 writer.write(string.replaceAll("\"", ""));
                    writer.write(string.replaceAll(",", ""));
                    writer.write(",");
                }
                writer.write(";");
                writer.write("\r\n");

                for (LinkedHashMap<String, String> lhmap : listMap) {
                    for (Entry<String, String> string2 : lhmap.entrySet()) {
//	                        writer.write(string2.getValue().replaceAll("\"", ""));
                        writer.write(string2.getValue().replaceAll(",", ""));
                        writer.write(",");
                    }
                    writer.write(";");
                    writer.write("\r\n");
                }
                writer.close();
                val = 0;
            } catch (Exception e) {
                val = 1;
            }
        } else {
            val = 1;
        }
        return val;
    }

    // 객체가 null인지 확인하고 null인 경우 "" 로 바꾸는 메서드
    public static String isNullToStringDefault(Object object, String dVal) {
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

    public static List<FieldError> validateMap(Validator validator, Map<String, ?> params) {
        List<FieldError> fieldErrorList = new ArrayList<FieldError>();
        final Errors errors = new MapBindingResult(params, "params");
        validator.validate(params, errors);
        if (errors.hasFieldErrors()) fieldErrorList = errors.getFieldErrors();
        return fieldErrorList;
    }

	/*
	 * url stream to base64
	 */
	public static String urlStreamBase64(String url) {
		String img = "";
		String srcUrl = url;
		if (!srcUrl.toLowerCase().startsWith("http")) {
			srcUrl = "http://" + srcUrl;
		}
		try {
			URL targetUrl = new URL(srcUrl);
			InputStream is;
			is = targetUrl.openStream();
			byte[] bytes = IOUtils.toByteArray(is);
			img = Base64.encodeBase64String(bytes);
			is.close();
		} catch (IOException e) {
		}
		return img;
	}
	// 이미지파일체크 -----------------------------------------------------------------;
	// 이미지 파일 체크 로직
	// 확장자가 jpg인데 파일속성은 jpg가 아닌 이미지 파일인 경우 jpg파일로 변환
	// 확장자가 jpg아니면 jpg파일로 변환후 확장자를 jpg로 변환
	public static String chkImgFileToJPG(String imgFileFullName){
		// 이미지 파일이 아니면 원본파일면 그대로 return;
		if(!fileUploadCheckJpg(imgFileFullName)){
			return imgFileFullName;
		}

		String rtnFileName 		= imgFileFullName;
		String outFileFullName  = imgFileFullName;
		int pos = imgFileFullName.lastIndexOf(".");
		String ext  = imgFileFullName.substring(pos);
		String name = imgFileFullName.substring(0,pos);
		if(!ext.equalsIgnoreCase(".jpg")) {
			outFileFullName = name + ".jpg";
		}

		String newFileName = imgFileFullName + "_new";
	    String mimeType = "";
		try {
//			mimeType = Files.probeContentType(source);
			mimeType = isImageFileType(imgFileFullName);
			// jpeg가 아닌경우 변환처리
			if (!"JPG".equals(mimeType) && !"".equals(mimeType)) {
				File frFile 	= new File(imgFileFullName);
				File jpgFile 	= new File(newFileName);
				BufferedImage frImage = ImageIO.read(frFile);
				// 이미지 jpg변환
				BufferedImage result = new BufferedImage(
						frImage.getWidth(),
						frImage.getHeight(),
	                    BufferedImage.TYPE_INT_RGB);
	            result.createGraphics().drawImage(frImage, 0, 0, Color.WHITE, null);
				ImageIO.write(result, "jpg", jpgFile);
				Path jpgFilePath =  Paths.get(newFileName);

				// 변환된 파일 overwrite
				Path source = Paths.get(outFileFullName);
				logger.debug("--> file to jpg >>>> oldfile:{},newfile:{}", imgFileFullName, outFileFullName);
				Files.move(jpgFilePath, source, StandardCopyOption.REPLACE_EXISTING);
				if (imgFileFullName.compareTo(outFileFullName) != 0)  {
					Path sourceDel = Paths.get(imgFileFullName);
					Files.delete(sourceDel);
					logger.debug("--> file delete >>>> oldfile:{},newfile:{}", imgFileFullName, outFileFullName);
				}
				logger.debug("--> file to jpg >>>> oldfile:{},newfile:{}", imgFileFullName, outFileFullName);
				rtnFileName = outFileFullName;
			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			//	e1.printStackTrace();
		}
		// -------------------------------- 이미지 속성변경 end;

		return rtnFileName;
	}
	public static String isImageFileType(String szFilePath){
        boolean isRst = false;
        int idx = -1;
        String szFileHeader = "";
        String imgType = "";
        String [] szArrImgHeader =
             {"47494638", "474946383761","474946383761","474946383761" //GIF Header
              ,"89504E470D0A1A0A0000000D49484452" // PNG Header
              , "FFD8FF" //JPG Header
              , "424D" //BMP Header
         };
        try {
         if(szFilePath != null && !szFilePath.equals("")) {
//         logger.debug(" ==== isImageFileType >>>> {}",  szFilePath);
         if(fileUploadCheckJpg(szFilePath)){
                    szFileHeader = fileToByte(szFilePath); //업로드 하려는 파일 헤더 체크
                    if(szFileHeader != null && !szFileHeader.equals("")) {
                        for(int i=0;i<szArrImgHeader.length;i++) {
                           int len = szArrImgHeader[i].length();
                           if(szArrImgHeader[i].equals(szFileHeader.substring(0, len))) {
                              isRst = true;
                              idx = i;
                              break;
                           }
                       }
                    }
               }
            }
        }catch(Exception e){
        	//
        }
        if (idx < 4) {
        	imgType = "GIF";
        } else if (idx == 4){
        	imgType = "PNG";
        } else if (idx == 5){
        	imgType = "JPG";
        } else if (idx == 6){
        	imgType = "BMP";
        }
        return imgType;
	}
	 //파일 헤더 체크
	 public static String fileToByte(String szFilePath)throws Exception{
	       byte [] b = new byte[16];
	       String szFileHeader = "";
	       DataInputStream in = null;
	       try{
	            //파일을 DataInputStream에 넣고 byte array로 읽어들임.(담기)
	            in = new DataInputStream(new FileInputStream(szFilePath));
	            in.read(b);
	            for(int i=0;i<b.length;i++) {
	                szFileHeader += byteToHex(b[i]);
	            }
	       }catch(Exception e){
	    	   	//
	      }finally{
	        if(in != null){in.close();}
	      }
	     return szFileHeader;
	  }

	 //byte -> Hex(String)로 변경
	 public static String byteToHex(byte in) {
	       byte ch = 0x00;
	       String pseudo[] = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
	       StringBuffer out = new StringBuffer();
	       ch = (byte) (in & 0xF0);
	       ch = (byte) (ch >>> 4);
	       ch = (byte) (ch & 0x0F);
	       out.append(pseudo[ (int) ch]);
	       ch = (byte) (in & 0x0F);
	       out.append(pseudo[ (int) ch]);
	       String rslt = new String(out);
	       return rslt;
	   }
   //파일 확장자 체크
   public static boolean fileUploadCheckJpg(String fileName){
       boolean result= false;
       String check = fileName.substring(fileName.lastIndexOf("."));
       if(check.equalsIgnoreCase(".jpg")||check.equalsIgnoreCase(".bmp")
       ||check.equalsIgnoreCase(".gif")||check.equalsIgnoreCase(".png")){
           result = true;
       }
       return result;
   }
   // 이미지파일체크 -----------------------------------------------------------------;

}
