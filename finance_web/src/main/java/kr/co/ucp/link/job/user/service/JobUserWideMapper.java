package kr.co.ucp.link.job.user.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("JobUserWideMapper")
public interface JobUserWideMapper {
	
	List<Map<String, Object>> selectUserList(Map<String, Object> map);
	
}
