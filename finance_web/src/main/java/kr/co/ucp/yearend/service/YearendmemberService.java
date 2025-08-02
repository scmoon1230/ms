package kr.co.ucp.yearend.service;

import java.util.List;
import java.util.Map;

public interface YearendmemberService {

	List<Map<String, String>> selectMember(Map<String, String> args) throws Exception;

}
