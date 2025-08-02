package kr.co.ucp.wrks.main.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;
import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("lgnMainMapper")
public interface LgnMainMapper
{

	List<Map<String, String>> getStatsList(Map<String, String> args);

	List<Map<String, String>> getList(Map<String, String> args);

	Map<String, String> getMessengerId(Map<String, String> args);
}
