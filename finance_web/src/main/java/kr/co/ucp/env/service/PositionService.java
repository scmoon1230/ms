package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

public interface PositionService {

	List<Map<String, String>> selectPosition(Map<String, String> args) throws Exception;

	int insertPosition(Map<String, Object> args) throws Exception;

	int updatePosition(Map<String, Object> args) throws Exception;

	int deletePosition(Map<String, String> args) throws Exception;

	int deletePositionMulti(List<Map<String, String>> args) throws Exception;

}
