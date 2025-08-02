package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

public interface FinchangeService {

	List<Map<String, String>> selectFinchange(Map<String, String> args) throws Exception;

}
