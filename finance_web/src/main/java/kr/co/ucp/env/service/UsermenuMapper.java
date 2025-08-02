package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("usermenuMapper")
public interface UsermenuMapper {

	List<Map<String, String>> menulist(Map<String, String> args) throws Exception;

	void insertUsermenu(Map<String, String> arg) throws Exception;

	void deleteUsermenu(Map<String, String> arg) throws Exception;

	//void update_grpauth_menu(Map<String, String> arg) throws Exception;

//	List<Map<String, String>> userlist(Map<String, String> args) throws Exception;

//	int copy_grpauth_menu(Map<String, String> args) throws Exception;

//	Map<String, String> get_grpId(Map<String, String> arg) throws Exception;

//	List<Map<String, String>> list_cm_dstrt_cd_mng(Map<String, String> map) throws Exception;

}
