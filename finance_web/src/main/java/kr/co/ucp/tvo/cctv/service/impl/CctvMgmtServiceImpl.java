
package kr.co.ucp.tvo.cctv.service.impl;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.tvo.cctv.service.CctvMgmtMapper;
import kr.co.ucp.tvo.cctv.service.CctvMgmtService;
import kr.co.ucp.mntr.cmm.service.CmmCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("cctvMgmtService")
public class CctvMgmtServiceImpl implements CctvMgmtService {

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "cmmCodeService")
	private CmmCodeService cmmCodeService;

	@Resource(name = "cctvMgmtMapper")
	private CctvMgmtMapper cctvMgmtMapper;

	@Override
	public List<EgovMap> selectTvoCctvList(Map<String, Object> params) {
		return cctvMgmtMapper.selectTvoCctvList(params);
	}

	@Override
	public int updateCctv(Map<String, Object> params) {
		return cctvMgmtMapper.updateCctv(params);
	}

//	@Override
//	public List<EgovMap> selectApiDataList(Map<String, Object> params) throws Exception {
//		List<EgovMap> apiDataList = new ArrayList<>();
//		Map<String, String> args = new HashMap<>();
//		args.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
//		List<EgovMap> list = cmmCodeService.selectListDstrtInfo(args);
//		if (list != null && !list.isEmpty()) {
//			params.put("gwUrl", list.get(0).get("vrsWebrtcAddr"));
//			params.put("rtspUrl", list.get(0).get("vrsRtspAddr"));
//			apiDataList = cctvMgmtMapper.selectApiDataList(params);
//		}
//
//		return apiDataList;
//	}
	
}
