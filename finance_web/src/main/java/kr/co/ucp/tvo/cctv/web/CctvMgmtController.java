
package kr.co.ucp.tvo.cctv.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.fdl.string.EgovNumericUtil;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.CommonVO;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.tvo.cctv.service.CctvMgmtService;

@Controller
public class CctvMgmtController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

//	@Resource(name = "cmmService")
//	private CmmService cmmService;

	@Resource(name = "commonUtil")
	private CommonUtil commonUtil;

	@Resource(name = "cctvMgmtService")
	private CctvMgmtService cctvMgmtService;

	// 영상저장CCTV관리
	@RequestMapping(value = "/tvo/cctv/strgMgmt.do")
	public String shotStrgMgmt(@ModelAttribute CommonVO common, HttpServletRequest request, ModelMap model) throws Exception {
		return "tvoMapR/cctv/strgMgmt";
	}

	// 영상반출CCTV관리
	@RequestMapping(value = "/tvo/cctv/tvoMgmt.do")
	public String showTvoMgmt(@ModelAttribute CommonVO common, HttpServletRequest request, ModelMap model) throws Exception {
		return "tvoMapR/cctv/tvoMgmt";
	}

	@RequestMapping(value = "/tvo/cctv/cctvMgmt/list.json")
	public ModelAndView getCctvMgmtList(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("--> getCctvMgmtList() params => {}", params.toString());
		
		ModelAndView mav = new ModelAndView("jsonView");
		String currentPageNo = EgovStringUtil.nullConvert(params.get("page"));
		String recordCountPerPage = EgovStringUtil.nullConvert(params.get("rows"));
		if (EgovNumericUtil.isNumber(currentPageNo) && EgovNumericUtil.isNumber(recordCountPerPage)) {
			PaginationInfo paginationInfo = new PaginationInfo();
			paginationInfo.setCurrentPageNo(Integer.parseInt(currentPageNo));
			paginationInfo.setRecordCountPerPage(Integer.parseInt(recordCountPerPage));

			params.put("firstIndex", paginationInfo.getFirstRecordIndex() + 1);
			params.put("lastIndex", paginationInfo.getLastRecordIndex());

			List<EgovMap> list = cctvMgmtService.selectTvoCctvList(params);

			int totCnt = commonUtil.setTotalRecordCount(list);
			paginationInfo.setTotalRecordCount(totCnt);
			paginationInfo.setPageSize(1);

			mav.addObject("totalPages", paginationInfo.getTotalPageCount());
			mav.addObject("totalRows", totCnt);
			mav.addObject("rows", list);
			mav.addObject("page", Integer.parseInt(currentPageNo));
		}
		return mav;
	}

	@RequestMapping(value = "/tvo/cctv/cctvMgmt/update.json")
	public ModelAndView updateCctvMgmt(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		int r = cctvMgmtService.updateCctv(params);
		if (r == 1) {
			mav.addObject("session", 1);
		} else {
			mav.addObject("session", 0);
		}
		return mav;
	}

//	@RequestMapping(value = "/tvo/cctv/cctvMgmt/send.json")
//	public ModelAndView sendCctvMgmt(@RequestParam Map<String, Object> params) throws Exception {
//		ModelAndView mav = new ModelAndView("jsonView");
//
//		// CCTV리스트
//		Map<String, Object> codeMap = new HashMap<String, Object>();
//		codeMap = cmmService.getCdInfo("URI_LINK_LFP_110", "URI_LINK_LFP");
//		String apiUri = CommUtil.objNullToStr(codeMap.get("cdDscrt"));
//		String authKey = CommUtil.objNullToStr(codeMap.get("cdGrpNm"));
//		if ("".equals(apiUri)) {
//			mav.addObject("session", 0);
//			mav.addObject("msg", "API 접속 정보가 없습니다.");
//			logger.error("--> ERROR >>>> CM_TC_CODE CD_ID=URI_LINK_LFP_130 ");
//			return mav;
//		}
//
//		String url = prprtsService.getString("LINK_LFP_ADDR") + apiUri;
//		String xAuthKey = authKey;
//
//		if (!"".equals(url) && !"".equals(xAuthKey)) {
//			// 0. Set Request Head Data
//			Map<String, String> headMap = new HashMap<String, String>();
//			headMap.put("x-auth-key", xAuthKey);
//
//			// 1. Set Request Body Data
//			LinkedHashMap<String, Object> bodyData = new LinkedHashMap<String, Object>();
//			bodyData.put("dstrtCd", prprtsService.getString("DSTRT_CD"));
//
//			List<EgovMap> list = cctvMgmtService.selectApiDataList(params);
//			if (list == null || list.isEmpty()) {
//				mav.addObject("session", "0");
//				mav.addObject("msg", "영상분석CCTV 목록이 없습니다.");
//				return mav;
//			}
//			bodyData.put("cctvCnt", list.size());
//			bodyData.put("data", list);
//
//
//			Map<String, Object> responseMap = RestUtil.postHttp1(url, bodyData, 0, headMap);
//			String responseCd = EgovStringUtil.nullConvert(responseMap.get("responseCd"));
//			String responseMsg = EgovStringUtil.nullConvert(responseMap.get("responseMsg"));
//
//			logger.info("--> requestDetection: {}", responseMap.toString());
//
//			if ("000".equals(responseCd)) {
//				mav.addObject("session", 1);
//				mav.addObject("msg", "영상분석시스템으로 전송하였습니다.");
//			} else {
//				mav.addObject("session", 0);
//				mav.addObject("msg", "오류발생 : " + responseMsg + "<br>잠시후 다시 시도해주세요");
//			}
//		}
//		return mav;
//	}
	
}
