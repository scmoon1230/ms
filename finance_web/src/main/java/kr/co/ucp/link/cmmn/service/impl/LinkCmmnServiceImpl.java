package kr.co.ucp.link.cmmn.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

//import kr.co.ucp.cmmn.EgovStringUtil;
//import kr.co.ucp.link.cmmn.service.PrprtsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmmn.CommUtil;
import kr.co.ucp.link.cmmn.service.LinkCmmnMapper;
import kr.co.ucp.link.cmmn.service.LinkCmmnService;

@Service("linkCmmnService")
public class LinkCmmnServiceImpl implements LinkCmmnService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

//	@Resource(name = "prprtsService")
//	private PrprtsService prprtsService;

	@Resource(name = "linkCmmnMapper")
	private LinkCmmnMapper linkCmmnMapper;

	@Override
	public List<Map<String, Object>> selectListDstrtInfo(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> list = linkCmmnMapper.selectListDstrtInfo(map);
//		String exeEnv = prprtsService.getGlobals("Globals.ExeEnv", "REAL");
//		if ("DEV".equals(exeEnv)) {
//			for (Map<String, Object> row : list) {
//				String colNm = "linkUrl";
//				String ipBf = EgovStringUtil.nullConvert(row.get(colNm));
//				String ipRegex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
//				String ipAf = ipBf.replaceFirst(ipRegex, "127.0.0.1");
//				row.put(colNm, ipAf);
//				logger.debug("ip changed {} to {}", ipBf, ipAf);
//			}
//		}
		return list;
	}

	@Override
	public int insertWdLinkLog(Map<String, Object> map) throws Exception {
		int rtn = 0;
		String linkYmdHmsLast = CommUtil.objNullToStr(map.get("linkYmdHmsLast"));
		if ("".equals(linkYmdHmsLast)) {
			map.put("linkYmdHmsLast", CommUtil.getCurrentTime14());
		}
		// linkid별 최종 처리 정보
		rtn = linkCmmnMapper.updateWdLinkWork(map);
		// linkid별 최종 처리 로그
		rtn = linkCmmnMapper.insertWdLinkLog(map);
		return rtn;
	}

	@Override
	public int updateWdLinkWork(Map<String, Object> map) throws Exception {
		return linkCmmnMapper.updateWdLinkWork(map);
	}

	@Override
	public int updateLinkLog(Map<String, Object> map) throws Exception {
		// 결과 로그 저장
		linkCmmnMapper.insertWdLinkLog(map);

		// 처리결과 최근값 업데이트
		map.put("linkYmdHmsLast", CommUtil.getCurrentTime14());
		linkCmmnMapper.updateWdLinkWork(map);
		return 1;
	}

	@Override
	public List<Map<String, Object>> selectDstrtLinkAddrList(Map<String, Object> map) throws Exception {
		List<Map<String, Object>> list = linkCmmnMapper.selectDstrtLinkAddrList(map);
//		String exeEnv = prprtsService.getGlobals("Globals.ExeEnv", "REAL");
//		if ("DEV".equals(exeEnv)) {
//			for (Map<String, Object> row : list) {
//				String colNm = "linkAddr";
//				String ipBf = EgovStringUtil.nullConvert(row.get(colNm));
//				String ipRegex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
//				String ipAf = ipBf.replaceFirst(ipRegex, "127.0.0.1");
//				row.put(colNm, ipAf);
//				logger.debug("ip changed {} to {}", ipBf, ipAf);
//			}
//		}
		return list;
	}

	@Override
	public Map<String, Object> selectLinkInfo(String dstrtCd) throws Exception {
		return linkCmmnMapper.selectLinkInfo(dstrtCd);
	}

}
