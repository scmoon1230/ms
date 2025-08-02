package kr.co.ucp.cmm.util;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
//import java.util.Base64;
//
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
public class ScrtUtil
{
	public static byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
	public static String strKey = "tkghlwjrdirwkdus";
	public  static final Key getAESKey(String strKey) throws Exception
	{
		String iv;
		Key keySpec;
		String key = strKey;
		iv = key.substring(0, 16);
		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}
		System.arraycopy(b, 0, keyBytes, 0, len);
		keySpec = new SecretKeySpec(keyBytes, "AES");
		return keySpec;
	}

	public static final String ecAES(String str, String strKey) throws Exception
	{
		//Key keySpec = getAESKey(strKey);
		SecretKeySpec keySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		return enStr;
	}

	public static final String dcAES(String enStr, String strKey) throws Exception
	{
		//Key keySpec = getAESKey(strKey);
		//SecretKeySpec keySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");
		Key keySpec = new SecretKeySpec(strKey.getBytes("UTF-8"), "AES");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
		byte[] byteStr = Base64.decodeBase64(enStr.getBytes("UTF-8"));
		String decStr = new String(c.doFinal(byteStr), "UTF-8");
		return decStr;
	}

	public static final String DecryptOld(String text, String key) throws Exception
	{
		return "";
//		System.out.println("Decrypt 0");
//		byte[] textBytes = Base64.decodeBase64(text);
//		//byte[] textBytes = str.getBytes("UTF-8");
//		System.out.println("Decrypt 1");
//		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
//		System.out.println("Decrypt 2");
//		SecretKeySpec newKey = new SecretKeySpec(ivBytes, "AES");
//		System.out.println("Decrypt 3" + newKey.toString());
//		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		System.out.println("Decrypt 4");
//		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
//		System.out.println("Decrypt 5");
//		String rtn = new String(cipher.doFinal(textBytes), "UTF-8");
//		String rtn = new String(Base64.decodeBase64(new String(cipher.doFinal(textBytes), "UTF-8").getBytes()));
//		System.out.println("Decrypt 6");
//		return rtn;
	}

	public static final String Decrypt(String text, String key) throws Exception
	{
		byte[] textBytes = Base64.decodeBase64(text);
		//byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		return new String(cipher.doFinal(textBytes), "UTF-8");
	}

	public static final String DecryptAndBase64(String text, String key) throws Exception
	{
		byte[] textBytes = Base64.decodeBase64(text);
		//byte[] textBytes = str.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
		String Result="";
		// tried but doens't work
		//    Result = new String(cipher.doFinal(new Base64().decode(text.getBytes())), "UTF-8");

		Result = new String(Base64.decodeBase64(new String(cipher.doFinal(textBytes), "UTF-8").getBytes()));
		return Result;
	}

	public static final String Encrypt(String text, String key) throws Exception
	{
		byte[] textBytes = text.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		//cipher = Cipher.getInstance("AES/CBC");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
		byte[] sResult = cipher.doFinal(textBytes);
		String ssResult = Base64.encodeBase64String(sResult);
		return ssResult;
	}

	public static final String EncryptAndBase64(String text, String key) throws Exception
	{
		String plaintext = "";
		plaintext = new String(Base64.encodeBase64(text.getBytes()));
		byte[] textBytes = plaintext.getBytes("UTF-8");
		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);
		return Base64.encodeBase64String(cipher.doFinal(textBytes));
	}

	public static final String encAES256(String text, String key) throws Exception
	{
		byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		SecretKey keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		AlgorithmParameterSpec ivspec = new IvParameterSpec(ivBytes);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		//Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
		byte[] cipherBytes = cipher.doFinal(text.getBytes("UTF-8"));
		String afterCiphered = new String(Base64.decodeBase64(cipherBytes));
		return afterCiphered;
	}

	public static final String decAES256(String text, String key) throws Exception
	{
		byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
//		BASE64Decoder base64Decoder = new BASE64Decoder();
		SecretKey keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
		// AlgorithmParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));
		AlgorithmParameterSpec ivspec = new IvParameterSpec(ivBytes);

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		//Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

		byte[] base64decoded = Base64.decodeBase64(text);
		String origin = new String(cipher.doFinal(base64decoded), "UTF-8");
		return origin;
	}
//	public static final String encAES256(String text, String key) throws Exception
//	{
//		byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
//		Encoder base64Encoder = java.util.Base64.getEncoder();
////		BASE64Encoder base64Encoder = new BASE64Encoder();
//		SecretKey keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//		AlgorithmParameterSpec ivspec = new IvParameterSpec(ivBytes);
//
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		//Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//		cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
//		byte[] cipherBytes = cipher.doFinal(text.getBytes("UTF-8"));
//		String afterCiphered = new String(base64Encoder.encode(cipherBytes));
//		return afterCiphered;
//	}

//	public static final String decAES256(String text, String key) throws Exception
//	{
//		byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
//		BASE64Decoder base64Decoder = new BASE64Decoder();
//		SecretKey keyspec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
//		// AlgorithmParameterSpec ivspec = new IvParameterSpec(iv.getBytes("UTF-8"));
//		AlgorithmParameterSpec ivspec = new IvParameterSpec(ivBytes);
//
//		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//		//Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
//		cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
//
//		byte[] base64decoded = base64Decoder.decodeBuffer(text);
//		String origin = new String(cipher.doFinal(base64decoded), "UTF-8");
//		return origin;
//	}
}
