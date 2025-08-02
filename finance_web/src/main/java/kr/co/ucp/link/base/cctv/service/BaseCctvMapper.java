package kr.co.ucp.link.base.cctv.service;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository("baseCctvMapper")
public interface BaseCctvMapper {
	
    List<Map<String, Object>> selectCctvList(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> selectCctvPresetList(Map<String, Object> params) throws Exception;
    
}
