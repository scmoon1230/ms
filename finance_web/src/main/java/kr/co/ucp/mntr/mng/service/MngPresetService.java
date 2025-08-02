package kr.co.ucp.mntr.mng.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;
import java.util.Map;

public interface MngPresetService {

	List<EgovMap> selectMngPresetList(MngSrchVO vo) throws Exception;

	EgovMap selectPresetList(Map<String, String> map) throws Exception;

	EgovMap mergePreset(Map<String, String> map) throws Exception;

	EgovMap deletePreset(Map<String, String> map) throws Exception;


}
