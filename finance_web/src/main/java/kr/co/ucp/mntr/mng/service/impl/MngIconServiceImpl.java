package kr.co.ucp.mntr.mng.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import kr.co.ucp.mntr.mng.service.MngIconService;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

@Service("mngIconService")
public class MngIconServiceImpl implements MngIconService {

	@Resource(name="mngIconMapper")
	private MngIconMapper mngIconMapper;

	@Override
	public List<EgovMap> selectIconList(Map<String, String> map) throws Exception {
		return mngIconMapper.selectIconList(map);
	}
}
