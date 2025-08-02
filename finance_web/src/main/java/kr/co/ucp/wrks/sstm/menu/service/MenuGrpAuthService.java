package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;

public interface MenuGrpAuthService {

	List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception;

	List<Map<String, String>> list_grpauth(Map<String, String> args) throws Exception;

	List<Map<String, String>> list_grpauth_menu(Map<String, String> args) throws Exception;

	int update_grpauth_menu(List<Map<String, String>> args) throws Exception;

	int copy_grpauth_menu(Map<String, String> args) throws Exception;

}