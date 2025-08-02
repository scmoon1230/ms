package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

public interface UsermenuService {

	List<Map<String, String>> menulist(Map<String, String> args) throws Exception;

	int update_grpauth_menu(List<Map<String, String>> args) throws Exception;

//	int copy_grpauth_menu(Map<String, String> args) throws Exception;

//	List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception;

//	List<Map<String, String>> userlist(Map<String, String> args) throws Exception;

}