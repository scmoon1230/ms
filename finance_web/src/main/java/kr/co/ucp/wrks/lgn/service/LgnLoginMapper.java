package kr.co.ucp.wrks.lgn.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import kr.co.ucp.cmm.LoginVO;

@Mapper("lgnLoginMapper")
public interface LgnLoginMapper {

	LoginVO selectLogin(Map<String, String> args);
/*
	LoginVO login(Map<String, String> args);

	LoginVO loginEncrypt(Map<String, String> args);

	LoginVO loginSSO(Map<String, String> args);
*/
	LoginVO findPwd(Map<String, String> args);

	int changePwd(Map<String, String> args);

	List<Map<String, String>> getMenuList(Map<String, String> args);

	int insertConnectUserCnt(LoginVO loginVO);
	
	//LoginVO getUserInfo(Map<String, String> args);
	
	//LoginVO selectLoginPass(Map<String, String> args);

	Map<String, String> selectSystemInfo(Map<String, String> args);

	String selectRepTelNo(Map<String, String> args);

}
