package kr.co.ucp.wrks.lgn.service.impl;
import java.util.Map;

import org.springframework.stereotype.Repository;

import kr.co.ucp.cmm.EgovAbstractMapperSms;

@Repository("lgnLoginSmsDAO")
public class LgnLoginSmsDAO extends EgovAbstractMapperSms  {

	/**
	 * 일반 로그인을 처리한다
	 * @param vo LoginVO
	 * @return LoginVO
	 * @exception Exception
	 */
    public int insert(Map<String, String> args) throws Exception {
    	return insert("sms_main.insert", args);
    }

}
