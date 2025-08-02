package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("grpAuthCctvMapper")
public interface GrpAuthCctvMapper {

	List<Map<String, String>> list_cm_dstrt_cd_mng(Map<String, String> map);

	List<Map<String, String>> list_cm_group(Map<String, String> map);

	List<Map<String, String>> list_cm_grp_user(Map<String, String> map);

	List<Map<String, String>> list_cm_cctv_used(Map<String, String> map);

	List<Map<String, String>> list_cm_cctv_auth(Map<String, String> map);

	List<Map<String, String>> list_fclt_use(Map<String, String> map);

	void insert_cctv_used(Map<String, String> arg);

	void delete_cctv_used(Map<String, String> arg);

	void insert_cctv_auth(Map<String, String> arg);

	void delete_cctv_auth(Map<String, String> arg);

}
