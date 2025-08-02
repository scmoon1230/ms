package kr.co.ucp.cmm;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.lang.StringUtils;

public class SecurityAny {
	/**
	 * Security Hash Algorithm-2 (SHA256사용)를 사용하여 해시코드 생성
	 * 	
	 * @param input 암호화할 문자
	 * @return 암호화된 해시문자
	 */
	public static String encryptSHA256(String input) {
		if (StringUtils.isEmpty(input)) {
			return "";
		}
		MessageDigest md;
		StringBuffer buffer = new StringBuffer();
		
		try {
			md = MessageDigest.getInstance("SHA-256");
			// hash값 취득
			md.update(input.getBytes("UTF-8"));
			
			byte[] digest = md.digest();
			
			// 16진수 문자열로 변환
			for(int i = 0; i < digest.length; i++) {
				String tmp = Integer.toHexString(digest[i] & 0xff);
				if ( tmp.length() == 1 ) {
					buffer.append('0').append(tmp);
				} else {
					buffer.append(tmp);
				}
			}
		} catch (NoSuchAlgorithmException e) {
			return "";
		} catch (UnsupportedEncodingException e) {
			return "";
		}
		return buffer.toString();
	}
	
	

    public static String getFileHash(String path) throws IOException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        FileInputStream fileInputStream = new FileInputStream(path);
        byte[] dataBytes = new byte[1024];
        Integer nRead = 0;
        while((nRead = fileInputStream.read(dataBytes)) != -1) {
            messageDigest.update(dataBytes, 0, nRead);
        }
        byte[] mdBytes = messageDigest.digest();
        StringBuffer stringBuffer = new StringBuffer();
        for(Integer i = 0; i < mdBytes.length; i++) {
            stringBuffer.append(Integer.toString((mdBytes[i] & 0xff) + 0x100, 16)).substring(1);
        }
        return stringBuffer.toString();
    }

	
	
	
	
	
	
	

}
