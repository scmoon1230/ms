package kr.co.ucp.mntr.mng.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface MngIconService {
	public List<EgovMap> selectIconList(Map<String, String> map) throws Exception;
}
