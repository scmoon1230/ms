package kr.co.ucp.wrks.evtctl.stats.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.wrks.evtctl.stats.service.CctvConnFcltMapper;
import kr.co.ucp.wrks.evtctl.stats.service.CctvConnFcltService;

@Service("cctvConnFcltService")
public class CctvConnFcltServiceImpl implements CctvConnFcltService {
	@Resource(name = "cctvConnFcltMapper")
	private CctvConnFcltMapper cctvConnFcltMapper;

	@Override
	public List<Map<String, String>> getList(Map<String, String> params) throws Exception {
		String searchMonth = EgovStringUtil.nullConvert(params.get("searchMonth"));
		if ("".equals(searchMonth)) {
			return cctvConnFcltMapper.selectMonthList(params);
		} else {
			return cctvConnFcltMapper.selectDayList(params);
		}
	}

	@Override
	public List<Map<String, String>> getExcel(Map<String, String> params) throws Exception {
		String searchMonth = EgovStringUtil.nullConvert(params.get("searchMonth"));
		if ("".equals(searchMonth)) {
			return cctvConnFcltMapper.selectMonthExcel(params);
		} else {
			return cctvConnFcltMapper.selectDayExcel(params);
		}
	}
}
