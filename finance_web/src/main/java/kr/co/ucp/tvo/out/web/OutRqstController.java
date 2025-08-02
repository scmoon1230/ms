package kr.co.ucp.tvo.out.web;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.CommonVO;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.tvo.out.service.OutSrchVO;
import kr.co.ucp.tvo.cmn.util.TvoUtil;
import kr.co.ucp.tvo.out.service.OutService;

@Controller
public class OutRqstController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="commonUtil")
	private CommonUtil commonUtil;

    @Resource(name = "tvoUtil")
    private TvoUtil tvoUtil;

    @Resource(name = "configureService")
    private ConfigureService configureService;

	@Resource(name="outService")
	private OutService outService;

	private WebSocketClient wsc;

	// 반출확인 화면
	@RequestMapping(value="/tvo/out/outRqst.do")
	public String showOutRqst(@ModelAttribute OutSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "반출확인", "rqst", "rqstOut" };
		commonUtil.setCommonVOData(vo, commonData);
		model.addAttribute("common", vo);
		
		return "tvoMap/out/outFirstRqst";
	}

	// 반출신청 목록 조회
	@RequestMapping(value="/tvo/out/selectOutRqstList.json", method = RequestMethod.POST)
	public ModelAndView selectOutRqstList(@ModelAttribute OutSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		vo.setOutRqstUserId(SessionUtil.getUserId());

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = outService.selectOutRqstListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = outService.selectOutRqstList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}

	// 반출신청 목록 건수 조회
	@RequestMapping(value="/tvo/out/selectOutRqstListTotCnt.json", method = RequestMethod.POST)
	public ModelAndView selectOutRqstListTotCnt(@ModelAttribute OutSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		vo.setOutRqstUserId(SessionUtil.getUserId());

		int totCnt = outService.selectOutRqstListTotCnt(vo);
		mav.addObject("totalRows", totCnt);
		return mav;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 반출신청 등록
	@RequestMapping(value="/tvo/rqst/outRqst/insertOutRqst.json", method = RequestMethod.POST)
	public ModelAndView registerOutRqst(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		String userId = SessionUtil.getUserId();
		params.put("outRqstUserId", userId);

		String yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		params.put("outRqstYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
		params.put("ymdhms", yyyyMMddHHmmssSSS.substring(0,14));
		

		//String tvoPrgrsCd = EgovStringUtil.nullConvert(params.get("tvoPrgrsCd")).trim();
		String emrgYn = EgovStringUtil.nullConvert(params.get("emrgYn")).trim();
		String outAutoAprvYn = prprtsService.getString("OUT_AUTO_APRV_YN");
		if ("Y".equals(outAutoAprvYn)) {	// 일괄자동승인 사용일 때
			params.put("tvoPrgrsCd", "51");
			params.put("outAprvUserId", "SYSTEM");
		} else if ( "Y".equalsIgnoreCase(emrgYn)) {	// 긴급일 때
			if ("U".equals(outAutoAprvYn)) {	// 긴급시 자동승인 사용 (시간제약없음)
				params.put("tvoPrgrsCd", "51");
				params.put("outAprvUserId", "SYSTEM");
			} else if ("T".equals(outAutoAprvYn)) {	// 긴급시 자동승인 사용 (시간제약)
				if ( tvoUtil.checkProdTime(prprtsService.getString("VIEW_AUTO_APRV_START"), prprtsService.getString("VIEW_AUTO_APRV_END")) ) {	// 현재 시각이 인자로 받은 시간 이내인지 판별
					params.put("tvoPrgrsCd", "51");
					params.put("outAprvUserId", "SYSTEM");
				}
			}
		}
		
		Map<String, String> result = outService.registerOutRqst(params);

		String r = EgovStringUtil.nullConvert(result.get("result"));
		if (!"0".equals(r)) {
			mav.addObject("result", r);
			mav.addObject("errors", "");
		} else {
			mav.addObject("result", "0");
		}
		
		
		// 승인처리알림(영상반출-승인레벨 사용자) - 화면알림
		String approveNotifyTy = prprtsService.getString("APPROVE_NOTIFY_TY");	// 승인알림구분
		if ( "Y".equals(approveNotifyTy) ) {
			JSONObject event = new JSONObject();
			JSONObject data = new JSONObject();
			//data.put("outRqstNo", params.get("outRqstNo").toString());					// 반출신청번호

			
			
			
			event.put("evtId", "APPROVE");
			event.put("data", data);
			//websocketClient(event.toString(), params.get("wsUrl").toString());
			websocketClient(event.toString());
		}
		
		return mav;
	}

	// 반출신청 삭제
	@RequestMapping(value="/tvo/out/deleteOutRqst.json", method = RequestMethod.POST)
	public ModelAndView removeOutRqst(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		params.put("outRqstUserId", SessionUtil.getUserId());

		Map<String, String> result = outService.removeOutRqst(params);
		
		String r = EgovStringUtil.nullConvert(result.get("result"));
		if (!"0".equals(r)) {
			mav.addObject("result", r);
			mav.addObject("errors", "");
		}
		else {
			mav.addObject("result", "0");
		}
		return mav;
	}

	
	// 반출파일 리스트 조회
	@RequestMapping(value="/tvo/out/selectOutFileList.json", method = RequestMethod.POST)
	public ModelAndView selectOutFileList(@ModelAttribute OutSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = outService.selectOutFileListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = outService.selectOutFileList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}


	// 반출진행건수와 예상소요시간 계산
	@RequestMapping(value="/tvo/out/calculate.json", method = RequestMethod.POST)
	public ModelAndView calculate(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
 
		EgovMap map = outService.selectOutRqstWorkCnt();	// 반출대기건수
		mav.addObject("ingCnt", map.get("ingCnt").toString());
		mav.addObject("drmCnt", map.get("drmCnt").toString());

		int vdoDurationSum = outService.selectOutRqstVdoDurationSum();	// 반출영상 다운로드 시간
		int costMin = vdoDurationSum + Integer.parseInt(map.get("ingCnt").toString()); 	// 예상소요시간(분)
		mav.addObject("costMin", costMin);
		
		return mav;
	}













	// 반출기간연장조회 화면
	@RequestMapping(value="/tvo/out/outExtnRqst.do")
	public String showOutRqstExtn(@ModelAttribute CommonVO vo, ModelMap model) throws Exception {
		String[] commonData = { "반출기간연장조회", "rqst", "rqstOutProdExtn" };
		commonUtil.setCommonVOData(vo, commonData);
		model.addAttribute("common", vo);
		
		return "tvoMap/out/outExtnRqst";
	}

	// 반출기간연장 목록 조회
	@RequestMapping(value="/tvo/rqst/outProdExtn/selectOutProdExtnList.json", method = RequestMethod.POST)
	public ModelAndView selectOutRqstExtnList(@ModelAttribute OutSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = outService.selectOutExtnListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = outService.selectOutExtnList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}

	// 반출기간연장신청 목록 조회
	@RequestMapping(value="/tvo/rqst/outRqst/selectOutProdExtnHisList.json", method = RequestMethod.POST)
	public ModelAndView selectOutRqstExtnHisList(@ModelAttribute OutSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = outService.selectOutExtnHisListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = outService.selectOutExtnHisList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}

	//	반출기간연장신청 등록
	@RequestMapping(value="/tvo/rqst/outRqst/insertOutProdExtn.json", method = RequestMethod.POST)
	public ModelAndView registerOutRqstExtn(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		Map<String, String> result = outService.registerOutExtn(params);

		// 승인처리알림(영상반출-승인레벨 사용자) - 화면알림
		String approveNotifyTy = prprtsService.getString("APPROVE_NOTIFY_TY");	// 승인알림구분
		if ( "Y".equals(approveNotifyTy) ) {
			JSONObject event = new JSONObject();
			JSONObject data = new JSONObject();
			//data.put("outRqstNo", params.get("outRqstNo").toString());					// 반출신청번호
			

			
			event.put("evtId", "APPROVE");
			event.put("data", data);
			//websocketClient(event.toString(), params.get("wsUrl").toString());
			websocketClient(event.toString());
		}
		
		String r = EgovStringUtil.nullConvert(result.get("result"));
		if (!"0".equals(r)) {
			mav.addObject("result", r);
			mav.addObject("errors", "");
		} else {
			mav.addObject("result", "0");
		}
		return mav;
	}

	
	
	//private void websocketClient(String wsData, String wsUrl) throws Exception {
	private void websocketClient(String wsData) throws Exception {

        String tvoUrl = prprtsService.getString("PVE_URL");
		String uri = tvoUrl+"/ws/evt.do?page=prvdApprv";
		logger.info("--> websocketClient(), uri => {}", uri);
		
		wsc = new WebSocketClient(new URI(uri)) {
			@Override
			public void onOpen(ServerHandshake handshakedata) {
				logger.info("websocketClient.onOpen: {}", handshakedata);
				this.send(wsData);
			}

			@Override
			public void onMessage(String message) {
				logger.info("websocketClient.onMessage: {}", message);
			}

			@Override
			public void onClose(int code, String reason, boolean remote) {
				logger.info("websocketClient.onClose: {}", reason);
				wsc.close();
			}

			@Override
			public void onError(Exception ex) {
				logger.info("websocketClient.onError: {}", ex.getMessage());
				wsc.close();
			}
		};
		wsc.connect();
	}

}
