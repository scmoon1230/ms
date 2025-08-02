package kr.co.ucp.wrks.sstm.menu.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("menuGrpAuthMapper")
public interface MenuGrpAuthMapper {

	List<Map<String, String>> list_cm_dstrt_cd_mng(Map<String, String> map) throws Exception;

	List<Map<String, String>> list_grpauth(Map<String, String> args) throws Exception;

	List<Map<String, String>> list_grpauth_menu(Map<String, String> args) throws Exception;

	Map<String, String> get_grpId(Map<String, String> arg) throws Exception;

	void delete_grpauth_menu(Map<String, String> arg) throws Exception;

	void insert_grpauth_menu(Map<String, String> arg) throws Exception;

	void update_grpauth_menu(Map<String, String> arg) throws Exception;

	int copy_grpauth_menu(Map<String, String> args) throws Exception;

}
