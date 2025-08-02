package kr.co.ucp.wrks.sstm.grp.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("grpUserMapper")
public interface GrpUserMapper {

	List<Map<String, String>> list_cm_dstrt_cd_mng(Map<String, String> map);

	List<Map<String, String>> list_cm_group(Map<String, String> map);

	List<Map<String, String>> list_cm_grp_user(Map<String, String> map);

}
