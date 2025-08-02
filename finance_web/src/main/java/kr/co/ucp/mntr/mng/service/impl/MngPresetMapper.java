package kr.co.ucp.mntr.mng.service.impl;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.mntr.mng.service.MngSrchVO;

import java.util.List;
import java.util.Map;

@Mapper("mngPresetMapper")
public interface MngPresetMapper {
	List<EgovMap> selectMngPresetDataList(MngSrchVO vo) throws Exception;
	List<EgovMap> selectPresetList(Map<String, String> map) throws Exception;
	int mergePreset(Map<String, String> map) throws Exception;
	int deletePreset(Map<String, String> map) throws Exception;
	int updateFcltOsvt(Map<String, String> map) throws Exception;
}
