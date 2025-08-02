package kr.co.ucp.link.base.cctvlog.service;

import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("baseCctvLogCnnctMapper")
public interface BaseCctvLogCnnctMapper {
	
	int updateCctvLogCnnct(Map<String, String> data) throws Exception;
	
}
