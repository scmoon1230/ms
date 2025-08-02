package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

public interface UserlogService {

	List<Map<String, String>> selectUserlog(Map<String, String> args) throws Exception;

}
