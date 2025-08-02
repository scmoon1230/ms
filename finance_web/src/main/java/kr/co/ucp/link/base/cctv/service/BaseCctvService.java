package kr.co.ucp.link.base.cctv.service;

import java.util.List;
import java.util.Map;

public interface BaseCctvService {
	
    List<Map<String, Object>> selectCctvList(Map<String, Object> params) throws Exception;

    List<Map<String, Object>> selectCctvPresetList(Map<String, Object> params) throws Exception;

    Map<String, Object> linkBaseCctvListData(Map<String, Object> rqMap) throws Exception;

    Map<String, Object> linkBaseCctvPresetData(Map<String, Object> bodyMap) throws Exception;

    Map<String, Object> linkBaseCctvStateData(Map<String, Object> bodyMap) throws Exception;
    
}
