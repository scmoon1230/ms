package kr.co.ucp.tvo.view.web;

import java.io.File;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.co.ucp.cmm.FileUpLoader;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.CommonVO;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.tvo.out.service.OutService;
import kr.co.ucp.tvo.out.service.OutSrchVO;
import kr.co.ucp.tvo.view.service.ViewRqstVO;
import kr.co.ucp.tvo.view.service.ViewService;
import kr.co.ucp.tvo.view.service.ViewSrchVO;
import kr.co.ucp.wrks.sstm.grp.service.GrpUserAccService;

@Controller
public class ViewController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="commonUtil")
	private CommonUtil commonUtil;

	@Resource(name="viewService")
	private ViewService viewService;

	@Resource(name="outService")
	private OutService outService;

	private WebSocketClient wsc;

	// 열람신청 화면
	@RequestMapping(value="/tvo/view/viewRqst.do")
	public String viewRqst(@ModelAttribute ViewSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "열람신청", "rqst", "rqstView" };
		commonUtil.setCommonVOData(vo, commonData);
		model.addAttribute("common", vo);
		
		model.addAttribute("dstrtCd", prprtsService.getString("DSTRT_CD"));
		
		String viewRqstDuration = prprtsService.getString("VIEW_RQST_DURATION");	// 열람신청기간
		
		logger.info("--> viewRqst(), viewRqstDuration => "+viewRqstDuration);
		model.addAttribute("viewRqstDuration", viewRqstDuration);
		
		return "tvoMap/view/viewFirstRqst";
	}

	// 열람신청 목록 조회
	@RequestMapping(value="/tvo/view/selectViewRqstList.json", method = RequestMethod.POST)
	public ModelAndView selectViewRqstList(@ModelAttribute ViewSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = viewService.selectViewRqstListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = viewService.selectViewRqstList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}


	// 열람승인 화면
	@RequestMapping(value="/tvo/view/viewAprv.do")
	public String showViewAprv(@ModelAttribute ViewSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "열람승인", "aprv", "aprvView" };
		commonUtil.setCommonVOData(vo, commonData);
		model.addAttribute("common", vo);
		
		return "tvoMap/view/viewFirstAprv";
	}

	// 열람승인 목록 조회
	@RequestMapping(value="/tvo/view/selectViewRqstAprvList.json", method = RequestMethod.POST)
	public ModelAndView selectViewRqstAprvList(@ModelAttribute ViewSrchVO vo) throws Exception {
		//logger.info("--> selectViewRqstAprvList(), vo.getRecordCountPerPage() => {}", vo.getRecordCountPerPage());
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = viewService.selectViewRqstAprvListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = viewService.selectViewRqstAprvList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}

	
	
	
	
	// 열람신청 상세
	@RequestMapping(value="/tvo/rqst/viewRqst/selectViewRqstDtl.json", method = RequestMethod.POST)
	public ModelAndView selectViewRqstDtl(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		EgovMap result = viewService.selectViewRqstDtl(params);
		mav.addObject("viewRqst", result);
		return mav;
	}
	
	// 열람신청 등록
	@RequestMapping(value="/tvo/rqst/viewRqst/insertViewRqst.json", method = RequestMethod.POST)
	public ModelAndView registerViewRqst(final MultipartHttpServletRequest request, @ModelAttribute @Valid ViewRqstVO vo, BindingResult result) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			mav.addObject("result", "0");
			mav.addObject("errors", list);
			
		} else {
			vo.setDstrtCd(prprtsService.getString("DSTRT_CD"));

			Map<String, String> pMap = new HashMap<String, String>();
			pMap.put("dstrtCd", vo.getDstrtCd());
			EgovMap rMap = viewService.selectNewViewRqstNo(pMap);
			vo.setViewRqstNo(vo.getDstrtCd()+"VR"+rMap.get("viewRqstNo").toString());

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String yyyyMMddHHmmss = df.format(new Date());
			vo.setViewRqstYmdhms(yyyyMMddHHmmss);
			//vo.setViewRqstNo(yyyyMMddHHmmss);
			
			// 공문파일 업로드
			Iterator<String> iter = request.getFileNames();
			MultipartFile file = null;
			if (iter.hasNext()) {
				file = request.getFile(iter.next());
			}
			if (file != null && file.getSize() > 0) {
				String viewRqstYmdhms = vo.getViewRqstYmdhms();
				String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_DOC");				
				String filePath = viewRqstYmdhms.substring(0,4) + File.separator + viewRqstYmdhms.substring(4,8);
				String fileName = viewRqstYmdhms+"_"+file.getOriginalFilename();

				FileUpLoader.uploadFile(file.getInputStream(), fileName, rootPath + File.separator + filePath);
				
				vo.setPaperNm(file.getOriginalFilename());
				vo.setPaperFileNm(fileName);
			}

			if ( "N".equalsIgnoreCase(vo.getEmrgYn())) {	// 일반일 때
				vo.setEmrgRsn("");
			}
			
			int r = viewService.registerViewRqst(vo);
			
			mav.addObject("result", r);
			mav.addObject("errors", "");

			// 승인처리알림(영상반출-승인레벨 사용자) - 화면알림
			String approveNotifyTy = prprtsService.getString("APPROVE_NOTIFY_TY");	// 승인알림구분
			if ( "Y".equals(approveNotifyTy) ) {
				JSONObject event = new JSONObject();
				JSONObject data = new JSONObject();
				//data.put("viewRqstNo", vo.getViewRqstNo());
				//data.put("evtNm", vo.getEvtNm());
				//data.put("evtYmdhms", vo.getEvtYmdhms());

				event.put("evtId", "APPROVE");
				event.put("data", data);
				//websocketClient(event.toString(), vo.getWsUrl());
				websocketClient(event.toString());
			}
			
		}
		
		return mav;
	}

	// 열람신청 수정
	@RequestMapping(value="/tvo/rqst/viewRqst/updateViewRqst.json", method = RequestMethod.POST)
	public ModelAndView changeViewRqst(final MultipartHttpServletRequest request, @ModelAttribute @Valid ViewRqstVO vo, BindingResult result) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			mav.addObject("result", "0");
			mav.addObject("errors", list);
		} else {
			// 공문 업로드
			Iterator<String> iter = request.getFileNames();
			MultipartFile file = null;
			if (iter.hasNext()) {
				file = request.getFile(iter.next());
			}
			if (file != null && file.getSize() > 0) {
				//SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				//String viewRqstNo = vo.getViewRqstNo();
				String viewRqstYmdhms = vo.getViewRqstYmdhms();
				
				//long nMaxSize = 5242880;
				//long nSize = file.getSize();
				//if (nMaxSize < nSize) {
				//	mav.addObject("msg", "공문첨부 실패. 5MByte 이하의 파일만 업로드 가능합니다.");
				//	return mav;
				//}

				// 기존 공문파일을 삭제한다.
				if ( !"".equalsIgnoreCase(vo.getPaperFileNm())) {
					String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_DOC");
					String fileInfo = viewRqstYmdhms.substring(0,4) + File.separator + viewRqstYmdhms.substring(4,8) + File.separator + vo.getPaperFileNm();
					boolean isDeleted = Files.deleteIfExists(Paths.get(rootPath + File.separator + fileInfo));
					if (isDeleted) logger.info("--> deleted: {}", rootPath + File.separator + fileInfo);
				}

				String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_DOC");
				String filePath = viewRqstYmdhms.substring(0,4) + File.separator + viewRqstYmdhms.substring(4,8);
				String fileName = viewRqstYmdhms+"_"+file.getOriginalFilename();
				FileUpLoader.uploadFile(file.getInputStream(), fileName, rootPath + File.separator + filePath);

				vo.setPaperNm(file.getOriginalFilename());
				vo.setPaperFileNm(fileName);
			}

			if ( "N".equalsIgnoreCase(vo.getEmrgYn())) {	// 일반일 때
				vo.setEmrgRsn("");
			}
			
			int r = viewService.updateViewRqst(vo);
			
			mav.addObject("result", r);
			mav.addObject("errors", "");
		}
		return mav;
	}

	// 열람신청 수정
	@RequestMapping(value="/tvo/aprv/viewRqst/updateViewRqst.json", method = RequestMethod.POST)
	public ModelAndView updateViewRqst(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		int r = viewService.modifyViewRqst(params);
		mav.addObject("result", r);
		mav.addObject("errors", "");
		return mav;
	}

	// 열람신청 리셋
	@RequestMapping(value="/tvo/view/aprv/resetViewRqst.json", method = RequestMethod.POST)
	public ModelAndView resetViewRqst(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		int r = viewService.resetViewRqst(params);
		mav.addObject("result", r);
		mav.addObject("errors", "");
		return mav;
	}

	// 열람신청 완전삭제
	@RequestMapping(value="/tvo/view/aprv/deleteCompleteViewRqst.json", method = RequestMethod.POST)
	public ModelAndView deleteCompleteViewRqst(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		OutSrchVO outVo = new OutSrchVO();
		outVo.setDstrtCd(params.get("dstrtCd"));
		outVo.setViewRqstNo(params.get("viewRqstNo"));
		outVo.setRecordCountPerPage(9999);
		outVo.setFirstRecordIndex(0);
		List<EgovMap> outRqstList = outService.selectOutRqstList(outVo);
		
		if ( outRqstList.size() != 0 ) {
			logger.debug("--> deleteCompleteViewRqst(), 반출신청을 삭제한다. <--");
			for ( int i=0;i<outRqstList.size();i++) {
				params.put("outRqstNo", outRqstList.get(i).get("outRqstNo").toString());
				int r = outService.deleteCompleteOutRqst(params);	// 반출신청 삭제
			}
		}
		
		int r = viewService.deleteCompleteViewRqst(params);	// 열람신청 삭제	
		
		mav.addObject("result", r);
		mav.addObject("errors", "");
		return mav;
	}

	// 열람신청 승인
	@RequestMapping(value="/tvo/view/aprv/approveViewRqst.json", method = RequestMethod.POST)
	public ModelAndView approveViewRqst(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		int r = viewService.approveViewRqst(params);
		mav.addObject("result", r);
		mav.addObject("errors", "");
		return mav;
	}

	// 열람신청 삭제
	@RequestMapping(value="/tvo/view/removeViewRqst.json", method = RequestMethod.POST)
	public ModelAndView removeViewRqst(@ModelAttribute ViewRqstVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		int r = viewService.removeViewRqst(vo);
		mav.addObject("result", r);
		return mav;
	}

	// 열람 활용결과 등록
	@RequestMapping(value="/tvo/rqst/viewRqst/registerViewResult.json", method = RequestMethod.POST)
	public ModelAndView registerViewResult(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		int r = viewService.modifyViewRqst(params);
		mav.addObject("result", r);
		return mav;
	}

	// 공문첨부
	@RequestMapping(value="/tvo/rqst/viewRqst/insertPaperFile.json", method = RequestMethod.POST)
	public ModelAndView registerPaperFile(final MultipartHttpServletRequest request, @RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		// 공문 업로드
		Iterator<String> iter = request.getFileNames();
		MultipartFile file = null;
		if (iter.hasNext()) {
			file = request.getFile(iter.next());
		}
		if (file != null && file.getSize() > 0) {
			//SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			//String viewRqstNo = EgovStringUtil.nullConvert(params.get("viewRqstNo"));
			String viewRqstYmdhms = EgovStringUtil.nullConvert(params.get("viewRqstYmdhms"));
			//String paperNo = EgovStringUtil.nullConvert(params.get("paperNo"));
			String paperFileNm = EgovStringUtil.nullConvert(params.get("paperFileNm"));
			
			//long nMaxSize = 5242880;
			//long nSize = file.getSize();
			//if (nMaxSize < nSize) {
			//	mav.addObject("msg", "공문첨부 실패. 5MByte 이하의 파일만 업로드 가능합니다.");
			//	return mav;
			//}

			// 기존 공문파일을 삭제한다.
			if ( !"".equalsIgnoreCase(paperFileNm)) {
				String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_DOC");
				String filePath = viewRqstYmdhms.substring(0,4) + File.separator + viewRqstYmdhms.substring(4,8) + File.separator + paperFileNm;
				boolean isDeleted = Files.deleteIfExists(Paths.get(rootPath + File.separator + filePath));
				if (isDeleted) logger.info("--> deleted: {}", rootPath + File.separator + filePath);
			}

			String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_DOC");
			String filePath = viewRqstYmdhms.substring(0,4) + File.separator + viewRqstYmdhms.substring(4,8);
			String fileName = viewRqstYmdhms+"_"+file.getOriginalFilename();
			FileUpLoader.uploadFile(file.getInputStream(), fileName, rootPath + File.separator + filePath);

			params.put("paperNm",file.getOriginalFilename());
			params.put("paperFileNm",fileName);
		}
		
		int r = viewService.modifyViewRqst(params);
		mav.addObject("result", r);
		mav.addObject("errors", "");
		return mav;
	}

	// 공문파일을 삭제
	@RequestMapping(value="/tvo/rqst/viewRqst/deletePaperFile.json", method = RequestMethod.POST)
	public ModelAndView removePaperFile(final MultipartHttpServletRequest request
			, @ModelAttribute @Valid ViewRqstVO vo, BindingResult result) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		if (result.hasErrors()) {
			List<ObjectError> list = result.getAllErrors();
			mav.addObject("result", "0");
			mav.addObject("errors", list);
			
		} else {
			// 기존 공문파일을 삭제한다.
			if ( !"".equalsIgnoreCase(vo.getPaperFileNm())) {
				String viewRqstYmdhms = vo.getViewRqstYmdhms();
				String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_DOC");
				String fileInfo = viewRqstYmdhms.substring(0,4) + File.separator + viewRqstYmdhms.substring(4,8) + File.separator + vo.getPaperFileNm();
				boolean isDeleted = Files.deleteIfExists(Paths.get(rootPath + File.separator + fileInfo));
				if (isDeleted) logger.info("--> deleted: {}", rootPath + File.separator + fileInfo);
			}
			
			vo.setPaperNm("");
			vo.setPaperFileNm("");
			
			int r = viewService.updateViewRqst(vo);
			mav.addObject("result", r);
			mav.addObject("errors", "");
		}
		return mav;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	

	// 열람기간연장신청 화면
	@RequestMapping(value="/tvo/view/viewExtnRqst.do")
	public String viewExtnRqst(@ModelAttribute CommonVO vo, ModelMap model) throws Exception {
		String[] commonData = { "열람기간연장조회", "rqst", "rqstViewProdExtn" };
		commonUtil.setCommonVOData(vo, commonData);
		model.addAttribute("common", vo);
		
		return "tvoMap/view/viewExtnRqst";
	}

	// 열람기간연장신청 목록 조회
	@RequestMapping(value="/tvo/rqst/viewProdExtn/selectViewExtnRqstList.json", method = RequestMethod.POST)
	public ModelAndView selectViewExtnRqstList(@ModelAttribute ViewSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = viewService.selectViewExtnListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = viewService.selectViewExtnList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}

	
	
	
	

	
	// 열람기간연장승인 화면
	@RequestMapping(value="/tvo/view/viewExtnAprv.do")
	public String viewExtnAprv(@ModelAttribute ViewSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "열람기간연장승인", "aprv", "aprvViewProdExtn" };
		commonUtil.setCommonVOData(vo, commonData);
		model.addAttribute("common", vo);
		
		return "tvoMap/view/viewExtnAprv";
	}

	// 열람기간연장승인 목록 조회
	@RequestMapping(value="/tvo/view/selectViewExtnAprvList.json", method = RequestMethod.POST)
	public ModelAndView selectViewExtnAprvList(@ModelAttribute ViewSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = viewService.selectViewExtnAprvListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = viewService.selectViewExtnAprvList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// 열람기간연장신청 목록 조회
	@RequestMapping(value="/tvo/rqst/viewRqst/selectViewProdExtnHisList.json", method = RequestMethod.POST)
	public ModelAndView selectViewRqstExtnHisList(@ModelAttribute ViewSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = viewService.selectViewExtnHisListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = viewService.selectViewExtnHisList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}

	// 열람기간연장신청 등록
	@RequestMapping(value="/tvo/rqst/viewRqst/insertViewProdExtn.json", method = RequestMethod.POST)
	public ModelAndView registerViewRqstExtn(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, String> result = viewService.registerViewExtn(params);

		// 승인처리알림(영상반출-승인레벨 사용자) - 화면알림
		String approveNotifyTy = prprtsService.getString("APPROVE_NOTIFY_TY");	// 승인알림구분
		if ( "Y".equals(approveNotifyTy) ) {
			JSONObject event = new JSONObject();
			JSONObject data = new JSONObject();
			//data.put("viewRqstNo", params.get("viewRqstNo").toString());					// 열람신청번호

			
			
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

	// 열람기간연장신청 승인
	@RequestMapping(value="/tvo/aprv/viewProdExtn/updateViewProdExtn.json", method = RequestMethod.POST)
	public ModelAndView approveViewRqstExtn(@RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		Map<String, String> result = viewService.approveViewExtn(params);
		String r = EgovStringUtil.nullConvert(result.get("result"));
		if (!"0".equals(r)) {
			mav.addObject("result", r);
			mav.addObject("errors", "");
		} else {
			mav.addObject("result", "0");
		}
		return mav;
	}

	
	
	
	

	
	
	
	

	// 모튼 유형 알림대상 목록 조회
	@RequestMapping(value="/tvo/aprv/selectAllTypeRqstList.json", method = RequestMethod.POST)
	public ModelAndView selectAllTypeRqstList(@ModelAttribute ViewSrchVO vo) throws Exception {
		logger.info("--> selectAllTypeRqstList(), vo.getRecordCountPerPage() => {}", vo.getRecordCountPerPage());
		
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);

		List<EgovMap> list = viewService.selectAllTypeRqstList(vo);

		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}


	
	

	

//	// 모튼 유형 승인대기 목록 조회
//	@RequestMapping(value="/tvo/aprv/selectAllTypeWaitList.json", method = RequestMethod.POST)
//	public ModelAndView selectAllTypeWaitList(@ModelAttribute ViewSrchVO vo) throws Exception {
//		logger.info("--> selectAllTypeWaitList(), vo.getRecordCountPerPage() => {}", vo.getRecordCountPerPage());
//		
//		ModelAndView mav = new ModelAndView("jsonView");
//
//		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
//
//		List<EgovMap> list = viewService.selectAllTypeWaitList(vo);
//
//		mav.addObject("rows", list);
//		mav.addObject("page", vo.getPage());
//		return mav;
//	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
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
