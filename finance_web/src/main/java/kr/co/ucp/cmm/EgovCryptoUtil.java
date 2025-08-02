/**
 * @Class Name  : EgovStringUtil.java
 * @Description : 문자열 데이터 처리 관련 유틸리티
 * @Modification Information
 *
 *     수정일         수정자                   수정내용
 *     -------          --------        ---------------------------
 *   2009.01.13     박정규          최초 생성
 *   2009.02.13     이삼섭          내용 추가
 *
 * @author 공통 서비스 개발팀 박정규
 * @since 2009. 01. 13
 * @version 1.0
 * @see
 *
 */

package kr.co.ucp.cmm;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.fdl.cryptography.EgovPasswordEncoder;
import egovframework.rte.fdl.cryptography.impl.EgovARIACryptoServiceImpl;

public class EgovCryptoUtil {
	static Logger logger = LoggerFactory.getLogger(EgovCryptoUtil.class);
	private static String cSet= "UTF-8";
	private static String algorithm= "SHA-256";

	/*
	 * pStr : 암호화대상, pEncDec:ENC(암호화)|DEC(복호화),algorithmKey=cm_auth_key.auth_no
	 */
	public static String getEgovCrypto(String pStr, String pEncDec, String algorithmKey, String cryptoYn) {
		String crypto = pStr;
		if (!"Y".equals(cryptoYn)) return crypto;

		byte[] b = null;
		try {
		    MessageDigest md = MessageDigest.getInstance(algorithm);
		    byte[] algorithmKeyByte = md.digest(algorithmKey.getBytes(cSet));
		    String algorithmKeyHash = new String(Base64.encodeBase64(algorithmKeyByte));
		    EgovPasswordEncoder passwordEncoder = new EgovPasswordEncoder();
		    passwordEncoder.setAlgorithm(algorithm);
		    passwordEncoder.setHashedPassword(algorithmKeyHash);
		    EgovARIACryptoServiceImpl cryptoService = new EgovARIACryptoServiceImpl();
		    cryptoService.setPasswordEncoder(passwordEncoder);
		    if ("ENC".equals(pEncDec)) {
		      b = cryptoService.encrypt(pStr.getBytes(cSet), algorithmKey);
		      crypto = new String(Base64.encodeBase64(b));
		    } else if ("DEC".equals(pEncDec)) {
		      b = cryptoService.decrypt(Base64.decodeBase64(pStr), algorithmKey);
		      crypto = new String(b, cSet);
		    }
		    logger.debug("==== EndDnc:{},cryptoYn:{},result:{},source:{}", pEncDec, cryptoYn, crypto,pStr);
		    return crypto;
		} catch (NoSuchAlgorithmException|java.io.UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			return "";
	    }
	}
}

