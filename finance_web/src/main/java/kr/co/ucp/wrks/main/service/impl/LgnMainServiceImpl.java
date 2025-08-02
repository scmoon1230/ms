package kr.co.ucp.wrks.main.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.wrks.main.service.LgnMainMapper;
import kr.co.ucp.wrks.main.service.LgnMainService;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;


@Service("lgnMainService")
public class LgnMainServiceImpl extends EgovAbstractServiceImpl implements LgnMainService
{
//	@Resource(name="lgnMainDAO")
//	private LgnMainDAO lgnMainDAO;
	@Resource(name="lgnMainMapper")
	private LgnMainMapper lgnMainDAO;

	@Override
	public List<Map<String, String>> getStatsList( Map<String, String> args) throws Exception {
		List<Map<String, String>> list = lgnMainDAO.getStatsList(args);
		return list;
	}

	@Override
	public List<Map<String, String>> getList( Map<String, String> args) throws Exception {
		List<Map<String, String>> list = lgnMainDAO.getList(args);
		return list;
	}

	@Override
	public Map<String, String> getMessengerId(Map<String, String> args) throws Exception {
		return lgnMainDAO.getMessengerId(args);
	}
}