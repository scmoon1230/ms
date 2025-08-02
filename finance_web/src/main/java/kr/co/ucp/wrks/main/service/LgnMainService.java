package kr.co.ucp.wrks.main.service;

import java.util.List;
import java.util.Map;

public interface LgnMainService
{
	List<Map<String, String>> getStatsList( Map<String, String> args) throws Exception;
	List<Map<String, String>> getList( Map<String, String> args) throws Exception;
	Map<String, String> getMessengerId( Map<String, String> args) throws Exception;
}