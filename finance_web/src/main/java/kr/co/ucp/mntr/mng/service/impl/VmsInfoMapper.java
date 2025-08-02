package kr.co.ucp.mntr.mng.service.impl;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("vmsInfoMapper")
public interface VmsInfoMapper {
	List<Map<String, Object>> selectVmsList(Map<String, Object> params);

	List<Map<String, String>> vmsGrpList(Map<String, Object> params);

	List<Map<String, Object>> dstrtNmCd(Map<String, Object> params);

	int insertVmsInfo(Map<String, Object> params);

	int updateVmsInfo(Map<String, Object> map);

	int deleteVmsInfo(Map<String, String> map);
}
