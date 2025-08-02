package kr.co.ucp.fin.service;

import java.util.List;
import java.util.Map;

public interface FinmastService {

	List<Map<String, String>> selectFinmastList(Map<String, String> args) throws Exception;

}
