package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("grpUserAccMapper")
public interface GrpUserAccMapper {

	List<Map<String, String>> getCM_GRP_AUTH_LVL(Map<String, String> map);

	List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map);

	List<Map<String, String>> getCM_GROUP(Map<String, String> map);

	List<Map<String, String>> getCM_GRP_EVT(Map<String, String> map);

	List<Map<String, String>> getCM_EVENT(Map<String, String> map);

	List<Map<String, String>> getCM_GRP_EVT_USER_LVL(Map<String, String> map);

	List<Map<String, String>> getCM_USER_POPUP(Map<String, String> map);

	List<Map<String, String>> getCM_GRP_EVT_MOBL_LVL(Map<String, String> map);

	List<Map<String, String>> getCM_GRP_MOBL(Map<String, String> map);

	void insert_event(Map<String, String> arg);

	void delete_event(Map<String, String> arg);

	void insert_user_acc(Map<String, String> arg);

	void update_user_acc(Map<String, String> arg);

	void delete_user_acc(Map<String, String> arg);

	void insert_mobl_acc(Map<String, String> arg);

	void update_mobl_acc(Map<String, String> arg);

	void delete_mobl_acc(Map<String, String> arg);

	void insert_user_acc_sel(Map<String, String> arg);

	List<Map<String, String>> getCM_USER_SEL_POPUP(Map<String, String> map);

	List<Map<String, String>> getCM_MOBL_SEL_POPUP(Map<String, String> map);

	void insert_mobl_acc_sel(Map<String, String> arg);

}
