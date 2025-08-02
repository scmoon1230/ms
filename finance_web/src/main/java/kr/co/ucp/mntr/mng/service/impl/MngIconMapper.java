package kr.co.ucp.mntr.mng.service.impl;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

@Mapper("mngIconMapper")
public interface MngIconMapper {
	public List<EgovMap> selectIconList(Map<String, String> map) throws Exception;
}
