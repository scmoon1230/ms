package kr.co.ucp.utl.sim.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Base64인코딩/디코딩 방식을 이용한 데이터를 암호화/복호화하는 Business Interface class
 * @author 공통서비스개발팀 박지욱
 * @since 2009.01.19
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.01.19  박지욱          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
public class EgovFileScrty
{
	// 파일구분자
	static final char FILE_SEPARATOR = File.separatorChar;
	// 버퍼사이즈
	static final int BUFFER_SIZE = 1024;
	private static final Logger LOGGER = LoggerFactory.getLogger(EgovFileScrty.class);

	/**
	 * 파일을 암호화하는 기능
	 *
	 * @param String source 암호화할 파일
	 * @param String target 암호화된 파일
	 * @return boolean result 암호화여부 True/False
	 * @exception Exception
	 */
	public static boolean encryptFile(String source, String target) throws Exception
	{
		// 암호화 여부
		boolean result = false;
		
		String sourceFile = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		String targetFile = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile = new File(sourceFile);
		
		BufferedInputStream input = null;
		BufferedOutputStream output = null;
		
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			if (srcFile.exists() && srcFile.isFile())
			{
				input = new BufferedInputStream(new FileInputStream(srcFile));
				output = new BufferedOutputStream(new FileOutputStream(targetFile));

				int length = 0;
				while (input.read(buffer) >= 0)
				{
					length = input.read(buffer);
					byte[] data = new byte[length];
					System.arraycopy(buffer, 0, data, 0, length);
					output.write(encodeBinary(data).getBytes());
					output.write(System.getProperty("line.separator").getBytes());
				}  // End while
				result = true;

			}  // End if
		} catch (Exception ex) {
			LOGGER.debug("{}", ex);
		} finally {
			if (input != null)
			{
				try {
					input.close();
				} catch (Exception ignore) {
					LOGGER.debug("IGNORE: {}" + ignore);
				}
			}  // End if
			if (output != null)
			{
				try {
					output.close();
				} catch (Exception ignore) {
					LOGGER.debug("IGNORE: {}" + ignore);
				}
			}  // End if
		}
		return result;
	}

	/**
	 * 파일을 복호화하는 기능
	 * @param String source 복호화할 파일
	 * @param String target 복호화된 파일
	 * @return boolean result 복호화여부 True/False
	 * @exception Exception
	 */
	public static boolean decryptFile(String source, String target) throws Exception
	{
		// 복호화 여부
		boolean result = false;
		String sourceFile = source.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		String targetFile = target.replace('\\', FILE_SEPARATOR).replace('/', FILE_SEPARATOR);
		File srcFile = new File(sourceFile);

		BufferedReader input = null;
		BufferedOutputStream output = null;

		String line = null;
		try {
			if (srcFile.exists() && srcFile.isFile())
			{
				input = new BufferedReader(new InputStreamReader(new FileInputStream(srcFile)));
				output = new BufferedOutputStream(new FileOutputStream(targetFile));

				while (input.readLine() != null)
				{
					line = input.readLine();					
					byte[] data = line.getBytes();
					output.write(decodeBinary(new String(data)));
				}  // End While
				result = true;

			}
		} catch (Exception ex) {
			LOGGER.debug("EX: {}" + ex);
		} finally {
			if (input != null)
			{
				try {
					input.close();
				} catch (Exception ignore) {
					LOGGER.debug("IGNORE: {}" + ignore);
				}
			}  // End if
			if (output != null)
			{
				try {
					output.close();
				} catch (Exception ignore) {
					LOGGER.debug("IGNORE: {}" + ignore);
				}
			}  // End if
		}  // End finally
		return result;
	}

	/**
	 * 데이터를 암호화하는 기능
	 * @param byte[] data 암호화할 데이터
	 * @return String result 암호화된 데이터
	 * @exception Exception
	 */
	public static String encodeBinary(byte[] data) throws Exception
	{
		if (data == null) { return "";}
		return new String(Base64.encodeBase64(data));
	}

	/**
	 * 데이터를 암호화하는 기능
	 * @param String data 암호화할 데이터
	 * @return String result 암호화된 데이터
	 * @exception Exception
	 */
	public static String encode(String data) throws Exception {
		return encodeBinary(data.getBytes());
	}

	/**
	 * 데이터를 복호화하는 기능
	 * @param String data 복호화할 데이터
	 * @return String result 복호화된 데이터
	 * @exception Exception
	 */
	public static byte[] decodeBinary(String data) throws Exception {
		return Base64.decodeBase64(data.getBytes());
	}

	/**
	 * 데이터를 복호화하는 기능
	 * @param String data 복호화할 데이터
	 * @return String result 복호화된 데이터
	 * @exception Exception
	 */
	public static String decode(String data) throws Exception {
		return new String(decodeBinary(data));
	}

	/**
	 * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 MD5 인코딩 방식 적용)
	 * @param String data 암호화할 비밀번호
	 * @return String result 암호화된 비밀번호
	 * @exception Exception
	 */
	public static String encryptPassword(String data) throws Exception
	{
		if ( null == data ) { return ""; }

		byte[] plainText = null; // 평문
		byte[] hashValue = null; // 해쉬값
		plainText = data.getBytes();

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		hashValue = md.digest(plainText);

	//	BASE64Encoder encoder = new BASE64Encoder();
	//	return encoder.encode(hashValue);

		return new String(Base64.encodeBase64(hashValue));
	}

	/**
	 * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 MD5 인코딩 방식 적용)
	 * @param String data 암호화할 비밀번호
	 * @return String result 암호화된 비밀번호
	 * @exception Exception
	 */
	public static String encryptPassword(String data, String salt) throws Exception
	{
		if (data == null) {
			return "";
		}
		byte[] hashValue = null; // 해쉬값
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.reset();
		md.update(salt.getBytes("UTF-8"));
		hashValue = md.digest(data.getBytes("UTF-8"));
		return new String(Base64.encodeBase64(hashValue));
	}
}