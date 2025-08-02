package kr.co.ucp.link.job.cctvlog.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("jobCctvLogCnnctMapper")
public interface JobCctvLogCnnctMapper {
	
	List<Map<String,Object>> selectCctvLogCnnctSend(Map<String, Object> map);
	
}
