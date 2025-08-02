package kr.co.ucp.tvo.out.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.co.ucp.cmm.FileUpLoader;
import kr.co.ucp.cmm.SecurityAny;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.tvo.out.service.OutService;
import kr.co.ucp.tvo.out.service.OutSrchVO;

@Controller
public class OutAprvController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "configureService")
    private ConfigureService configureService;

	@Resource(name="commonUtil")
	private CommonUtil commonUtil;

	@Resource(name="outService")
	private OutService outService;

	// 반출승인 화면
	@RequestMapping(value="/tvo/out/outAprv.do")
	public String showOutAprv(@ModelAttribute OutSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "반출승인", "aprv", "aprvOut" };
		commonUtil.setCommonVOData(vo, commonData);
		model.addAttribute("common", vo);
		
		return "tvoMap/out/outFirstAprv";
	}

	// 반출신청 목록 조회
	@RequestMapping(value="/tvo/out/selectOutRqstAprvList.json", method = RequestMethod.POST)
	public ModelAndView selectOutRqstAprvList(@ModelAttribute OutSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = outService.selectOutRqstAprvListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = outService.selectOutRqstAprvList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}

	
	
	
	
	
	
	
	
	
	// 반출신청 수정
	@RequestMapping(value="/tvo/out/aprv/updateOutRqst.json", method = RequestMethod.POST)
	public ModelAndView updateOutRqst(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		String yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		params.put("tvoPrgrsYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
		
		String r = "0";
		String errors = "";
		if( "50".equalsIgnoreCase(params.get("tvoPrgrsCd").toString()) ) {		// 입수승인일 때

			String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
			
			String sOsName = System.getProperty("os.name").toLowerCase();
			if (sOsName.indexOf("win") < 0) {	rootPath = rootPath.replace("\\", "/");
			} else {							rootPath = rootPath.replace("/", "\\");
			}

			OutSrchVO vo = new OutSrchVO();
			vo.setDstrtCd(params.get("dstrtCd").toString());
			vo.setOutRqstNo(params.get("outRqstNo").toString());
			vo.setRecordCountPerPage(9999);
			vo.setFirstRecordIndex(0);
			List<EgovMap> list = outService.selectOutFileList(vo);
			
			if ( list.size() != 0 ) {
				logger.debug("--> 이전에 저장한 파일을 삭제한다. <--");
				for ( int i=0;i<list.size();i++) {
					list.get(i).put("rootPath", rootPath);
					outService.removeFile(list.get(i));	// 이전에 저장한 파일을 삭제한다.
				}
			}
			
			Map<String, String> resultMap = outService.registerOutFile(params);	// 반출파일정보등록
			
			String orgVdoAutoRgsYn = prprtsService.getString("ORG_VDO_AUTO_RGS_YN");	// 자동입수 사용여부
			if ("Y".equals(orgVdoAutoRgsYn)) {		// 자동입수를 사용할 때
				logger.info("--> updateOutRqst() 원본영상입수시스템으로 영상요청을 발송한다.");

				String orgVdoSupplier = prprtsService.getString("ORG_VDO_SUPPLIER");
				logger.info("--> requestOrgVdo() orgVdoSupplier : {}", orgVdoSupplier);

				Map<String, Object> responseMap = new HashMap<>();
				if ( "markany".equalsIgnoreCase(orgVdoSupplier) ) {		// 마크애니일 때
					
					//params.put("rootPath", rootPath);
					responseMap = outService.requestMaOrgVdo(params);	// 마크애니 영상입수 요청하기
					
					
				} else if ( "widecube".equalsIgnoreCase(orgVdoSupplier) ) {		// 와이드큐브 영상입수 요청하기
				
				}

				String response_code = "";
				String response_msg = "";
				response_code = responseMap.get("response_code").toString();
				response_msg = responseMap.get("response_msg").toString();
				
				if ("000".equals(response_code)) {
					params.put("outChkStepCd", "25");	// 입수중
					logger.info("--> updateOutRqst() : 원본영상 요청을 성공하였습니다. <--");
				} else {
					params.put("outChkStepCd", "27");	// 입수실패
					errors = "원본영상 요청을 실패하였습니다!\n\n"+response_code+" : "+response_msg;
					logger.info("--> updateOutRqst() errors : {} <--", errors);
				}
				
			} else {	// 자동입수를 사용하지 않을 때
				params.put("outChkStepCd", "20");	// 입수대기
			}
			
			params.put("outChkYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
			
		} else if( "70".equalsIgnoreCase(params.get("tvoPrgrsCd").toString()) ) {	// 반출승인일 때
			params.put("outChkStepCd", "94");	// 승인완료
			params.put("outChkYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
			
		}

		if ("".equalsIgnoreCase(errors)) {
			params.put("outAprvUserId", SessionUtil.getUserId());
			
			Map<String, String> result = new HashMap<String, String>();
			result = outService.approveOutRqst(params);
			r = EgovStringUtil.nullConvert(result.get("result"));
		}
		
		if (!"0".equals(r)) {
			mav.addObject("result", r);
			mav.addObject("errors", "");
		} else {
			mav.addObject("result", "0");
			mav.addObject("errors", errors);
		}
		
		return mav;
	}

	// 반출신청 리셋
	@RequestMapping(value="/tvo/out/aprv/resetOutRqst.json", method = RequestMethod.POST)
	public ModelAndView resetOutRqst(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		int r = outService.resetOutRqst(params);
		mav.addObject("result", r);
		mav.addObject("errors", "");
		return mav;
	}

	// 반출신청 완전삭제
	@RequestMapping(value="/tvo/out/aprv/deleteCompleteOutRqst.json", method = RequestMethod.POST)
	public ModelAndView deleteCompleteOutRqst(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		int r = outService.deleteCompleteOutRqst(params);
		
		mav.addObject("result", r);
		mav.addObject("errors", "");
		return mav;
	}

	// 반출신청 승인
	@RequestMapping(value="/tvo/out/aprv/approveOutRqstMulti.json", method = RequestMethod.POST)
	public ModelAndView approveOutRqstMulti(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		
		String yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		params.put("tvoPrgrsYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
		
		String r = "0";
		String errors = "";

		String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		
		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	rootPath = rootPath.replace("\\", "/");
		} else {							rootPath = rootPath.replace("/", "\\");
		}

		String[] aprvInfos = params.get("aprvInfo").toString().split(",");
		for ( int k=0 ; k<aprvInfos.length ; k++ ) {
			logger.info("--> approveOutRqst(), aprvInfos[{}] : {}", k, aprvInfos[k]);
			String[] aprvInfo = aprvInfos[k].split(":");
			params.put("outRqstNo",aprvInfo[0]);
			params.put("tvoPrgrsCd",aprvInfo[1]);

			
			
			if( "50".equalsIgnoreCase(params.get("tvoPrgrsCd").toString()) ) {		// 입수승인일 때

				OutSrchVO vo = new OutSrchVO();
				vo.setDstrtCd(params.get("dstrtCd"));
				vo.setOutRqstNo(params.get("outRqstNo"));
				vo.setRecordCountPerPage(9999);
				vo.setFirstRecordIndex(0);
				List<EgovMap> list = outService.selectOutFileList(vo);
				
				if ( list.size() != 0 ) {
					logger.debug("--> approveOutRqst(), 이전에 저장한 파일을 삭제한다. <--");
					for ( int i=0;i<list.size();i++) {
						list.get(i).put("rootPath", rootPath);
						outService.removeFile(list.get(i));	// 이전에 저장한 파일을 삭제한다.
					}
				}

				Map<String, String> paramMap = new HashMap<>();
				paramMap.put("outRqstNo", vo.getOutRqstNo());
				EgovMap outRqst = outService.selectOutRqstDtl(paramMap);
				params.put("outRqstYmdhms", outRqst.get("outRqstYmdhms").toString());
				Map<String, String> resultMap = outService.registerOutFile(params);	// 반출파일정보등록
				
				String orgVdoAutoRgsYn = prprtsService.getString("ORG_VDO_AUTO_RGS_YN");	// 자동입수 사용여부
				if ("Y".equals(orgVdoAutoRgsYn)) {		// 자동입수를 사용할 때
					logger.info("--> approveOutRqst(), 원본영상입수시스템으로 영상요청을 발송한다.");

					String orgVdoSupplier = prprtsService.getString("ORG_VDO_SUPPLIER");
					logger.info("--> approveOutRqst(), orgVdoSupplier : {}", orgVdoSupplier);

					Map<String, Object> responseMap = new HashMap<>();
					if ( "markany".equalsIgnoreCase(orgVdoSupplier) ) {		// 마크애니일 때
						
						//params.put("rootPath", rootPath);
						responseMap = outService.requestMaOrgVdo(params);	// 마크애니 영상입수 요청하기
						
						
					} else if ( "widecube".equalsIgnoreCase(orgVdoSupplier) ) {		// 와이드큐브 영상입수 요청하기
					
					}

					String response_code = "";
					String response_msg = "";
					response_code = responseMap.get("response_code").toString();
					response_msg = responseMap.get("response_msg").toString();
					
					if ("000".equals(response_code)) {
						params.put("outChkStepCd", "25");	// 입수중
						logger.info("--> approveOutRqst(), 원본영상 요청을 성공하였습니다. <--");
					} else {
						params.put("outChkStepCd", "27");	// 입수실패
						errors = "원본영상 요청을 실패하였습니다!\n\n"+response_code+" : "+response_msg;
						logger.info("--> approveOutRqst(), errors : {} <--", errors);
					}
					
				} else {	// 자동입수를 사용하지 않을 때
					params.put("outChkStepCd", "20");	// 입수대기
				}
				
				params.put("outChkYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
				
			} else if( "70".equalsIgnoreCase(params.get("tvoPrgrsCd").toString()) ) {	// 반출승인일 때
				params.put("outChkStepCd", "94");	// 승인완료
				params.put("outChkYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
				
			}

			if ("".equalsIgnoreCase(errors)) {
				params.put("outAprvUserId", SessionUtil.getUserId());
				
				Map<String, String> result = new HashMap<String, String>();
				result = outService.approveOutRqst(params);
				r = EgovStringUtil.nullConvert(result.get("result"));
			}
			
		}
		
		
		if (!"0".equals(r)) {
			mav.addObject("result", r);
			mav.addObject("errors", "");
		} else {
			mav.addObject("result", "0");
			mav.addObject("errors", errors);
		}
		
		return mav;
	}

//	// 반출파일 리스트 조회
//	@RequestMapping(value="/tvo/aprv/outRqst/selectOutFileList.json", method = RequestMethod.POST)
//	public ModelAndView selectOutFileList(@ModelAttribute OutSrchVO vo) throws Exception {
//		ModelAndView mav = new ModelAndView("jsonView");
//
//		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
//		int totCnt = outAprvService.selectTvoOutFileListTotCnt(vo);
//		paginationInfo.setTotalRecordCount(totCnt);
//
//		List<EgovMap> list = outAprvService.selectOutFileList(vo);
//
//		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
//		mav.addObject("totalRows", totCnt);
//		mav.addObject("rows", list);
//		mav.addObject("page", vo.getPage());
//		return mav;
//	}

	
	

	
	
	
	
	
	
	
	// 반출기간연장승인 화면으로 이동한다.
	@RequestMapping(value="/tvo/out/outExtnAprv.do")
	public String showOutRqstExtn(@ModelAttribute OutSrchVO vo, ModelMap model) throws Exception {
		String[] commonData = { "반출기간연장승인", "aprv", "aprvOutProdExtn" };
		commonUtil.setCommonVOData(vo, commonData);
		model.addAttribute("common", vo);
		
		return "tvoMap/out/outExtnAprv";
	}

	// 반출기간연장 목록 조회
	@RequestMapping(value="/tvo/out/selectOutExtnAprvList.json", method = RequestMethod.POST)
	public ModelAndView selectOutExtnAprvList(@ModelAttribute OutSrchVO vo) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		PaginationInfo paginationInfo = commonUtil.setPaginationInfo(vo);
		int totCnt = outService.selectOutExtnAprvListTotCnt(vo);
		paginationInfo.setTotalRecordCount(totCnt);

		List<EgovMap> list = outService.selectOutExtnAprvList(vo);

		mav.addObject("totalPages", paginationInfo.getTotalPageCount());
		mav.addObject("totalRows", totCnt);
		mav.addObject("rows", list);
		mav.addObject("page", vo.getPage());
		return mav;
	}

	// 반출기간연장신청 승인
	@RequestMapping(value="/tvo/aprv/outProdExtn/updateOutExtn.json", method = RequestMethod.POST)
	public ModelAndView approveOutRqstExtn(@RequestParam Map<String, String> outExtn, HttpServletRequest request) throws Exception {
		logger.info("--> approveOutRqstExtn() outExtn => {}", outExtn.toString());
		
		ModelAndView mav = new ModelAndView("jsonView");

		Map<String, String> result = outService.approveOutExtn(outExtn);

		String tvoPrgrsCd = EgovStringUtil.nullConvert(outExtn.get("tvoPrgrsCd"));
		if ( "50".equalsIgnoreCase(tvoPrgrsCd)) {	// 승인

			OutSrchVO vo = new OutSrchVO();
			vo.setDstrtCd(EgovStringUtil.nullConvert(outExtn.get("dstrtCd")));
			vo.setOutRqstNo(EgovStringUtil.nullConvert(outExtn.get("outRqstNo")));
			vo.setRecordCountPerPage(9999);
			vo.setFirstRecordIndex(0);
			List<EgovMap> outFileMapList = outService.selectOutFileList(vo);
			
			if ( outFileMapList.size() != 0 ) {
				logger.debug("--> approveOutRqstExtn(), 암호화를 요청한다. <--");

				String outFilePlayCnt = prprtsService.getString("OUT_FILE_PLAY_CNT");	// 영상재생횟수
				if ( !"".equalsIgnoreCase(outFilePlayCnt)) {
					int cnt = Integer.parseInt(outFilePlayCnt);
					if ( cnt < 1 ) {
						outFilePlayCnt = "-1";	// 재생횟수 무제한
					}
				} else {
					outFilePlayCnt = "-1";	// 재생횟수 무제한
				}

				Map<String, String> params = new HashMap<>();
				params.put("dstrtCd"  , vo.getDstrtCd()  );
				params.put("outRqstNo", vo.getOutRqstNo());
				params.put("playCnt"  , outFilePlayCnt   );
				
				for ( int i=0 ; i<outFileMapList.size() ; i++ ) {
					EgovMap outFileMap = outFileMapList.get(i);
					params.put("fileSeq", outFileMap.get("outFileSeq").toString());
					params.put("filePath", outFileMap.get("outFilePath").toString());
					params.put("cptnFileNm", outFileMap.get("cptnFileNm").toString());		// 자막파일명

					String objFileNm = outFileMap.get("outFileNm").toString();				// 대상파일명 : 원본 파일
					EgovMap outRqst = outService.selectOutRqstDtl(params);
					
					if ( "Y".equalsIgnoreCase(outRqst.get("maskingYn").toString()) ) {		// 마스킹할 때
						objFileNm = outFileMap.get("outFileNmMsk").toString();				// 대상파일명 : 마스킹된 파일
					}
					params.put("objFileNm", objFileNm);	// 대상파일명

					//String drmFileNm = objFileNm.replaceAll("_mask", "");
					//drmFileNm = drmFileNm.substring(0,objFileNm.lastIndexOf('.')+1)+"cctv";
					//String drmFileNm = outFileMapList.get(i).get("cctvId").toString()+"_"+outFileMapList.get(i).get("outVdoYmdhmsFr").toString()
					//		+"_"+outFileMapList.get(i).get("outVdoYmdhmsTo").toString()+".cctv";
					String drmFileNm = outFileMap.get("outRqstNo").toString()+"_"+outFileMap.get("fcltLblNm").toString()
							+"_"+outFileMap.get("outVdoYmdhmsFr").toString().substring(0,12)
							+"_"+outFileMap.get("outVdoYmdhmsTo").toString().substring(0,12)+".cctv";
					params.put("drmFileNm", drmFileNm);	// 암호화파일명
					
					logger.info("--> approveOutRqstExtn(), params => {}", params.toString());
					params = outService.sendRequestEncrypt(params);		// 암호화 요청
					
				}
				String yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				params.put("outChkYmdhms", yyyyMMddHHmmss);
				params.put("outAprvUserId", SessionUtil.getUserId());
				logger.info("--> approveOutRqstExtn(), params2 => {}", params.toString());
				result = outService.approveOutRqst(params);
			}
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

	// 원본영상 업로드
	@RequestMapping(value="/tvo/outAprv/uploadOrgVdo.json", method = RequestMethod.POST)
	public ModelAndView uploadOrigVdo(final MultipartHttpServletRequest request, @RequestParam Map<String, String> params) throws Exception {
		for( String strKey : params.keySet() ){ System.out.println("--> uploadOrigVdo() => "+strKey +" : "+ (String) params.get(strKey) ); }
		
		ModelAndView mav = new ModelAndView("jsonView");

		MultipartEntityBuilder builder = MultipartEntityBuilder.create().setCharset(Consts.UTF_8);
		builder.addTextBody("outRqstNo", params.get("outRqstNo").toString(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));				// 반출신청번호
		builder.addTextBody("outFileSeq", params.get("outFileSeq").toString(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));			// 반출신청번호
		//builder.addTextBody("outChkStepCd", params.get("outChkStepCd").toString(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));		// 반출체크단계(OUT_CHK_STEP,10-체크전,20-원본,30-마스킹완료,90-반출불가,92-처리완료)
		builder.addTextBody("cctvId", params.get("cctvId").toString(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));	// 영상시작
		builder.addTextBody("outVdoYmdhmsFr", params.get("outVdoYmdhmsFr").toString(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));	// 영상시작
		builder.addTextBody("outVdoYmdhmsTo", params.get("outVdoYmdhmsTo").toString(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));	// 영상종료
		builder.addTextBody("action", params.get("action").toString(), ContentType.MULTIPART_FORM_DATA.withCharset("UTF-8"));					// 파일삭제여부
		
		// 파일 첨부
		Iterator<String> iter = request.getFileNames();
		if ( iter.hasNext() ) {
			while (iter.hasNext()) {
				String fileKey = iter.next();
				MultipartFile multipartFile = request.getFile(fileKey);
				System.out.println("--> uploadOrigVdo() => fileName : "+multipartFile.getOriginalFilename());
				
				File file = new File(multipartFile.getOriginalFilename());
				multipartFile.transferTo(file);
				
				FileBody binFile = new FileBody(file);
				builder.addPart("file", binFile);			
			}
		}
		HttpEntity multipart = builder.build();
		
		// 영상전송
		String url = prprtsService.getString("PVE_URL") + "/api/responseOrgVdo.xx";	// 원본영상 업로드
		//String url = params.get("tvoUrl").toString() + "/api/responseOrgVdo.xx";	// 원본영상 업로드
		logger.info("--> uploadOrigVdo() url:{}", url);
		HttpPost httpPost = new HttpPost(url);

		httpPost.setEntity(multipart);

		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = httpClient.execute(httpPost);

		String result = "000";
		String msg = "";
		ObjectMapper objectMapper = new ObjectMapper();
		Object objValue = null;
		Map<String, Object> responseMap = new HashMap<String, Object>();
		
		int status = response.getStatusLine().getStatusCode();
		if (status >= 200 && status < 300) {
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity);
			logger.debug("--> outsteptest() response status:{}, result:{}", status, result);
			
			objValue = objectMapper.readValue(result, Object.class);
			responseMap = (Map<String, Object>) objValue;
			logger.debug("--> transFileSave >>>> responseMap : {}", responseMap.toString());
			
			result = responseMap.get("responseCode").toString();
			msg = responseMap.get("responseMessage").toString();
			logger.debug("--> uploadOrigVdo() {} : {}", result, msg);
		} else {
			logger.error("--> uploadOrigVdo() response is error, status:{}", status);
			result = "fail";
		}
		
		if ("000".equalsIgnoreCase(result)) {
			mav.addObject("result", "1");
			mav.addObject("errors", "");
		} else {
			mav.addObject("result", "0");
			mav.addObject("errors", msg);
		}
		return mav;
	}

	// 해시체크용 파일첨부
	@RequestMapping(value="/tvo/out/aprv/uploadChckHashFile.json", method = RequestMethod.POST)
	public ModelAndView uploadChckHashFile(final MultipartHttpServletRequest request, @RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		int r = 1;
		String errors = "";
		
		// 파일 업로드
		Iterator<String> iter = request.getFileNames();
		MultipartFile file = null;
		if (iter.hasNext()) {
			file = request.getFile(iter.next());
		}
		if (file != null && file.getSize() > 0) {
		//	String dstrtCd    = EgovStringUtil.nullConvert(params.get("dstrtCd")   );
			String outRqstNo  = EgovStringUtil.nullConvert(params.get("outRqstNo") );
			String outFileSeq = EgovStringUtil.nullConvert(params.get("outFileSeq"));

			// 임시폴더의 파일을 삭제한다.
			String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
			String filePath = "temp";
			File dir = new File(rootPath + File.separator + filePath);
			File[] files = dir.listFiles();
			if ( files != null ) {
				for( int i=0; i<files.length; i++){
					if( files[i].delete() ){	//System.out.println(files[i].getName()+" 삭제성공");
					}
				}
			}

			String fileName = outRqstNo+":"+outFileSeq+"_"+file.getOriginalFilename();
			FileUpLoader.uploadFile(file.getInputStream(), fileName, rootPath + File.separator + filePath);

			EgovMap tvoOutFile = outService.selectOutFileDtl(params);
			String drmHash = tvoOutFile.get("drmHash").toString();
			logger.info("--> drmHash => {}", drmHash);
			
			String uploadFile = rootPath + File.separator + filePath + File.separator + fileName;
			String sOsName = System.getProperty("os.name").toLowerCase();
			if (sOsName.indexOf("win") < 0) {	uploadFile = uploadFile.replace("\\", "/");
			} else {							uploadFile = uploadFile.replace("/", "\\");
			}
			String newHash = SecurityAny.getFileHash(uploadFile);
			logger.info("--> newHash => {}", newHash);
			
			if ( !drmHash.equalsIgnoreCase(newHash) ) {
				r = 0;
			}
		}
		
		mav.addObject("result", r);
		mav.addObject("errors", errors);
		return mav;
	}

	

	// 원본영상 재입수 요청
	@RequestMapping(value="/tvo/out/aprv/requestGetOrigVdo.json", method = RequestMethod.POST)
	public ModelAndView requestGetOrigVdo(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception {
		for( String strKey : params.keySet() ){ System.out.println("--> requestEncOrigVdo() => "+strKey +" : "+ (String) params.get(strKey) ); }
		
		ModelAndView mav = new ModelAndView("jsonView");

		String orgVdoSupplier = prprtsService.getString("ORG_VDO_SUPPLIER");
		logger.info("--> requestGetOrigVdo() orgVdoSupplier : {}", orgVdoSupplier);

		String r = "0";
		String errors = "";
		Map<String, Object> responseMap = new HashMap<>();
		if ( "markany".equalsIgnoreCase(orgVdoSupplier) ) {		// 마크애니일 때

			responseMap = outService.requestMaOrgVdo(params);	// 마크애니 영상입수 요청하기

			String response_code = "";
			String response_msg = "";
			response_code = responseMap.get("response_code").toString();
			response_msg = responseMap.get("response_msg").toString();
			
			if ("000".equals(response_code)) {
				params.put("outChkStepCd", "25");	// 입수중
				logger.info("--> requestGetOrigVdo() : 원본영상 요청을 성공하였습니다. <--");
			} else {
				params.put("outChkStepCd", "27");	// 입수실패
				errors = "원본영상 요청을 실패하였습니다!\n\n"+response_code+" : "+response_msg;
				logger.info("--> requestGetOrigVdo() errors : {} <--", errors);
			}

			String yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
			params.put("outChkYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
			
		} else {
			
		}
		
		if ("".equalsIgnoreCase(errors)) {
			params.put("outAprvUserId", SessionUtil.getUserId());
			
			Map<String, String> result = new HashMap<String, String>();
			result = outService.approveOutRqst(params);
			r = EgovStringUtil.nullConvert(result.get("result"));
		}
		
		if (!"0".equals(r)) {
			mav.addObject("result", r);
			mav.addObject("errors", "");
		} else {
			mav.addObject("result", "0");
			mav.addObject("errors", errors);
		}

		return mav;
	}

	

	// 원본영상 암호화 요청
	@RequestMapping(value="/tvo/out/aprv/requestEncOrigVdo.json", method = RequestMethod.POST)
	public ModelAndView requestEncOrigVdo(@RequestParam Map<String, String> params, HttpServletRequest request) throws Exception {
		for( String strKey : params.keySet() ){ System.out.println("--> requestEncOrigVdo() => "+strKey +" : "+ (String) params.get(strKey) ); }
		
		ModelAndView mav = new ModelAndView("jsonView");

		OutSrchVO vo = new OutSrchVO();
		vo.setDstrtCd(EgovStringUtil.nullConvert(params.get("dstrtCd")));
		vo.setOutRqstNo(EgovStringUtil.nullConvert(params.get("outRqstNo")));
		vo.setRecordCountPerPage(9999);
		vo.setFirstRecordIndex(0);
		List<EgovMap> outFileMapList = outService.selectOutFileList(vo);
		
		String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		params.put("rootPath", rootPath);

		String outFilePlayCnt = prprtsService.getString("OUT_FILE_PLAY_CNT");	// 영상재생횟수
		if ( !"".equalsIgnoreCase(outFilePlayCnt)) {
			int cnt = Integer.parseInt(outFilePlayCnt);
			if ( cnt < 1 ) {
				outFilePlayCnt = "-1";	// 재생횟수 무제한
			}
		} else {
			outFilePlayCnt = "-1";	// 재생횟수 무제한
		}
		params.put("playCnt", outFilePlayCnt);
		
		// 원본파일별로 암호화 요청을 전송한다.
		for ( int i=0 ; i<outFileMapList.size() ; i++ ) {
			logger.info("--> requestEncOrigVdo(), tvoOutFile => {}", outFileMapList.get(i).toString());

			// 암호화 작업 시작
			params.put("fileSeq", outFileMapList.get(i).get("outFileSeq").toString());
			params.put("filePath", outFileMapList.get(i).get("outFilePath").toString());
			params.put("cptnFileNm", outFileMapList.get(i).get("cptnFileNm").toString());		// 자막파일명

			String objFileNm = outFileMapList.get(i).get("outFileNm").toString();			// 대상파일명 : 원본 파일
			//params.put("targetFileNm", outFileMapList.get(i).get("outFileNmMp4").toString());	// 암호화대상파일명
			//params.put("targetFileNm", outFileMapList.get(i).get("outFileNm").toString());	// 대상파일명 : 원본 파일
			
			EgovMap outRqst = outService.selectOutRqstDtl(params);
			if ( "Y".equalsIgnoreCase(outRqst.get("maskingYn").toString()) ) {		// 마스킹할 때
				//params.put("targetFileNm", outFileMapList.get(i).get("outFileNmMsk").toString());	// 대상파일명 : 마스킹된 파일
				objFileNm = outFileMapList.get(i).get("outFileNmMsk").toString();	// 대상파일명 : 마스킹된 파일
			}
			params.put("objFileNm", objFileNm);	// 대상파일명
			
			String drmFileNm = outFileMapList.get(i).get("outRqstNo").toString()+"_"+outFileMapList.get(i).get("fcltLblNm").toString()
					+"_"+outFileMapList.get(i).get("outVdoYmdhmsFr").toString().substring(0,12)
					+"_"+outFileMapList.get(i).get("outVdoYmdhmsTo").toString().substring(0,12)+".cctv";
			params.put("drmFileNm", drmFileNm);	// 암호화파일명
			
			logger.info("--> requestEncOrigVdo(), params => {}", params.toString());
			params = outService.sendRequestEncrypt(params);		// 암호화 요청
			
		}

		return mav;
	}

	
	
	
	
	
}
