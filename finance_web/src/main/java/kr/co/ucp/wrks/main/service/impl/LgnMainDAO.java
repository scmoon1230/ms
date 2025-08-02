package kr.co.ucp.wrks.main.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("lgnMainDAO")
public class LgnMainDAO extends EgovAbstractMapper
{
	public List<Map<String, String>>getStatsList(Map<String, String> args) throws Exception {
		return selectList("wrks_lgn.statsList", args);
	}

	public List<Map<String, String>>getList(Map<String, String> args) throws Exception {
		return selectList("wrks_lgn.list", args);
	}

	public Map<String, String> getMessengerId(Map<String, String> args) throws Exception {
		return selectOne("wrks_lgn.messengerId", args);
	}
}
