package kr.co.ucp.tvo.out.web;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.SecurityAny;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.tvo.out.service.OutService;
import kr.co.ucp.tvo.out.service.OutSrchVO;

@RestController
public class TvoRestController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "configureService")
    private ConfigureService configureService;

	@Resource(name="outService")
	private OutService outService;

	private WebSocketClient wsc;

	public TvoRestController() {
	}
	
	
	
	// 원본영상입수요청전송
	@PostMapping("/api/requestOrgVdo.xx")
	@ResponseStatus(value = HttpStatus.OK)
	public HashMap<String, Object> requestOrgVdo(@RequestHeader MultiValueMap<String, String> headers
			, @RequestBody Map<String, Object> reqMap) throws Exception {
		for( String strKey : reqMap.keySet() ){ System.out.println( "--> requestOrgVdo() "+strKey +" : "+ (String) reqMap.get(strKey) ); }
		String responseCd = "000";
		String responseMsg = "success";
		HashMap<String, Object> result_map = new HashMap<String, Object>();

		try {
			
			
			
		} catch (Exception e) {
			logger.error("--> requestOrgVdo() ERROR Exception >>>> {}", e.getMessage());
			responseCd = "901";
			responseMsg	= "Exception";
		}
		logger.debug("--> requestOrgVdo() responseCd:{}, responseMsg:{}", responseCd, responseMsg);
		result_map.put("responseCd",		responseCd);
		result_map.put("responseMsg",	responseMsg);
		return result_map;
	}

	

	// 원본영상 회신
//	@PostMapping("/api/responseOrgVdo.xx")
//	@ResponseStatus(value = HttpStatus.OK)
//	public HashMap<String, Object> responseOrgVdo(@RequestHeader MultiValueMap<String, String> headers
//			, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
//		headers.forEach((key, value) -> { logger.debug(String.format("Header '%s' = %s", key, value.stream().collect(Collectors.joining("|")))); });
//
//  		String responseCode = "000";
//  		String responseMessage = "success";
//  		HashMap<String, Object> result_map = new HashMap<String, Object>();
//		
//		try {
//			request.setCharacterEncoding("utf-8");
//			Enumeration<String> data = request.getParameterNames();
//			while (data.hasMoreElements()) {
//				String name = (String) data.nextElement();
//				String value = request.getParameter(name);
//				System.out.println("--> responseOrgVdo() "+name+" : "+value);
//				logger.debug("--> responseOrgVdo() {} : {}", name, value);
//			}
//			
//			String outRqstNo = request.getParameter("outRqstNo");
//			String outFileSeq = request.getParameter("outFileSeq");
//			//String outChkStepCd = request.getParameter("outChkStepCd");
//			String cctvId = request.getParameter("cctvId");
//			String outVdoYmdhmsFr = request.getParameter("outVdoYmdhmsFr");
//			String outVdoYmdhmsTo = request.getParameter("outVdoYmdhmsTo");
//			String action = request.getParameter("action");
//			
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("outRqstNo", outRqstNo);
//			params.put("outFileSeq", outFileSeq);
//			params.put("outVdoYmdhmsFr", outVdoYmdhmsFr);
//			params.put("outVdoYmdhmsTo", outVdoYmdhmsTo);
//			//params.put("outChkStepCd", outChkStepCd);
//
//			EgovMap outRqst = outService.selectOutRqstDtl(params);	// 반출신청정보조회
//			
//			//if ( "40".equalsIgnoreCase(outChkStepCd) || "50".equalsIgnoreCase(outChkStepCd) || "92".equalsIgnoreCase(outChkStepCd))	{	// 입수완료, 마스킹완료, 암호화처리완료
//			//}
//			MultipartFile inFile = file;
//			if (!inFile.isEmpty()) {
//
//				String outRqstYmdhmsSSS = outRqstNo.split("_")[0];
//				params.put("outRqstYmdhms", outRqstYmdhmsSSS.substring(0,14));
//				
//				String filePath = outRqstYmdhmsSSS.substring(0,4) + "/" + outRqstYmdhmsSSS.substring(4,8);
//				params.put("outFilePath", filePath);
//
//				if ( "DEL_FILE".equalsIgnoreCase(action)) {	// 기존파일 삭제일 때
//					OutSrchVO vo = new OutSrchVO();
//					vo.setOutRqstNo(params.get("outRqstNo"));
//					vo.setRecordCountPerPage(9999);
//					vo.setFirstRecordIndex(0);
//					List<EgovMap> list = outService.selectOutFileList(vo);
//					if ( list.size() != 0 ) {
//						logger.debug("--> 이전에 저장한 파일을 삭제한다. <--");
//						for ( int i=0;i<list.size();i++) {
//							list.get(i).put("rootPath", rootPath);
//							outService.removeFile(list.get(i));	// 이전에 저장한 파일을 삭제한다.
//						}
//					}
//				}
//				
//				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//				String yyyyMMddHHmmss = df.format(new Date());
//				String cptnFileNm = "";
//				String fileNm = new String(inFile.getOriginalFilename().getBytes("8859_1"), "UTF-8");
//				String ext = fileNm.substring(fileNm.indexOf(".")+1).toLowerCase();
//
//				//String outFileNm = yyyyMMddHHmmss+"_"+new String(inFile.getOriginalFilename().getBytes("8859_1"), "UTF-8");
//				String outFileNm = cctvId+"_"+outVdoYmdhmsFr+"_"+outVdoYmdhmsTo+"."+ext;
//				logger.debug("--> responseOrgVdo() outFileNm => {}", outFileNm);
//				
//				saveVideoFile(file.getInputStream(), outFileNm, rootPath + File.separator + filePath);		// 영상파일 저장
//				
//				params.put("outFileNm", outFileNm);
//				Map<String, String> result = outService.modifyMovieFile(params);
//
//				if ( "mp4".contentEquals(ext)) {	// 원본영상이 mp4일 때
//					params.put("outFileNmMp4", outFileNm);
//					result = outService.modifyMovieFile(params);
//					
//				} else {							// 원본영상이 mp4가 아니면 mp4로 변환한다.
//					EgovMap tvoOutFile = new EgovMap();
//					tvoOutFile.put("rootPath", rootPath);
//					tvoOutFile.put("outFilePath",filePath);
//					tvoOutFile.put("outFileNm", outFileNm);
//					tvoOutFile.put("cctvId", cctvId);
//					tvoOutFile.put("outVdoYmdhmsFr", outVdoYmdhmsFr);
//					tvoOutFile.put("outVdoYmdhmsTo", outVdoYmdhmsTo);
//					tvoOutFile.put("outRqstNo", outRqstNo);
//					tvoOutFile.put("outFileSeq", outFileSeq);
//					outService.transOrgVdoFile(tvoOutFile);	// mp4 파일로 변환 후 입수완료 or 암호화대기 상태로 변경
//				}
//				
//				// 마스킹여부 확인
//				if ( "Y".equalsIgnoreCase(outRqst.get("maskingYn").toString()) ) {	// 마스킹 할 때
//					params.put("outChkStepCd", "40");		// 입수완료
//						
//				} else {			// 마스킹 안할 때
//					// 암호화 작업 시작
//					params.put("rootPath", rootPath);
//					
//					EgovMap tvoConfig = configureService.getTvoConfig();
//					String outFilePlayCnt = EgovStringUtil.nullConvert(tvoConfig.get("outFilePlayCnt"));	// 영상재생횟수
//					if ( !"".equalsIgnoreCase(outFilePlayCnt)) {
//						int cnt = Integer.parseInt(outFilePlayCnt);
//						if ( cnt < 1 ) {
//							outFilePlayCnt = "-1";	// 재생횟수 무제한
//						}
//					} else {
//						outFilePlayCnt = "-1";	// 재생횟수 무제한
//					}
//					params.put("playCnt", outFilePlayCnt);
//					
//					params.put("fileSeq", "0");
//					params.put("filePath", filePath);
//					params.put("cptnFileNm", cptnFileNm);	// 자막파일명
//					params.put("targetFileNm", outFileNm);	// 암호화대상파일명
//					
//					logger.info("--> params => {}", params.toString());
//					params = outService.sendRequestEncrypt(params);		// 암호화 요청
//					
//				}
//			}
//
//			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
//			String yyyyMMddHHmmss = df.format(new Date());
//			params.put("outChkYmdhms", yyyyMMddHHmmss);
//			params.put("outAprvUserId", SessionUtil.getUserId());
//			outService.updateOutRqst(params);	// 반출신청정보갱신
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		logger.debug("--> responseOrgVdo() {} : {}", responseCode, responseMessage);
//		result_map.put("responseCode",		responseCode);
//		result_map.put("responseMessage",	responseMessage);
//		return result_map;
//	}

	
	
	
	
	
	
	// 암호화영상요청
	@PostMapping("/api/requestEncVdo.xx")
	@ResponseStatus(value = HttpStatus.OK)
	public HashMap<String, Object> requestEncVdo(@RequestHeader MultiValueMap<String, String> headers
			, @RequestBody Map<String, Object> reqMap) throws Exception {
		for( String strKey : reqMap.keySet() ){ System.out.println( "--> requestEncVdo() "+strKey +" : "+ (String) reqMap.get(strKey) ); }
		String responseCd = "000";
		String responseMsg = "success";
		HashMap<String, Object> result_map = new HashMap<String, Object>();

		try {
		/*	정보를 저장해 두고 VMS로부터 영상을 입수하여 마스킹 및 암호화를 시작한다.
			각 진행단계별로 상황 및 영상파일을 TVO서버로 전송한다.
			*/
			
			
			
		} catch (Exception e) {
			logger.error("--> requestEncVdo() ERROR Exception >>>> {}", e.getMessage());
			responseCd = "901";
			responseMsg	= "Exception";
		}
		logger.debug("--> requestEncVdo() responseCd:{}, responseMsg:{}", responseCd, responseMsg);
		result_map.put("responseCd",	responseCd);
		result_map.put("responseMsg",	responseMsg);
		return result_map;
	}
	
	
	// 암호화영상 회신
	@PostMapping("/api/responseEncVdo.xx")
	@ResponseStatus(value = HttpStatus.OK)
	public HashMap<String, Object> responseEncVdo(@RequestHeader MultiValueMap<String, String> headers
			, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
		headers.forEach((key, value) -> { logger.debug(String.format("Header '%s' = %s", key, value.stream().collect(Collectors.joining("|")))); });

  		String responseCode = "000";
  		String responseMessage = "success";
  		HashMap<String, Object> result_map = new HashMap<String, Object>();
		
		try {
			request.setCharacterEncoding("utf-8");
			Enumeration<String> data = request.getParameterNames();
			while (data.hasMoreElements()) {
				String name = (String) data.nextElement();
				String value = request.getParameter(name);
				System.out.println("--> responseEncVdo() "+name+" : "+value);
				logger.debug("--> responseEncVdo() {} : {}", name, value);
			}
			String outRqstNo = request.getParameter("outRqstNo");
			String outChkStepCd = request.getParameter("outChkStepCd");
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("outRqstNo", outRqstNo);
			
			// 반출신청정보조회
			EgovMap outRqst = outService.selectOutRqstDtl(params);
			
			params.put("outChkStepCd", outChkStepCd);

			if ( "40".equalsIgnoreCase(outChkStepCd) || "50".equalsIgnoreCase(outChkStepCd) || "92".equalsIgnoreCase(outChkStepCd))	{	// 입수완료, 마스킹완료, 암호화처리완료
				MultipartFile inFile = file;
				if (!inFile.isEmpty()) {
					//int seq = 0;
					//if ( "40".equalsIgnoreCase(outChkStepCd)) {	seq = outAprvService.selectNewOutFileSeq(params);	// 입수완료
					//} else {									seq = outAprvService.selectLastOutFileSeq(params);	}
					//String outFileSeq = Integer.toString(seq);
					//params.put("outFileSeq", outFileSeq);

					//if ( "0".equalsIgnoreCase(outFileSeq)) {	params.put("outFileTy", "1");	}	// 최초
					//else {										params.put("outFileTy", "2");	}	// 연장

					String outRqstYmdhmsSSS = outRqstNo.split("_")[0];
					params.put("outRqstYmdhms", outRqstYmdhmsSSS.substring(0,14));
					
					String filePath = outRqstYmdhmsSSS.substring(0,4) + "/" + outRqstYmdhmsSSS.substring(4,8);
					params.put("outFilePath", "/" + filePath + "/");

					SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
					String yyyyMMddHHmmss = df.format(new Date());
					
					String fileName = yyyyMMddHHmmss+"_"+new String(inFile.getOriginalFilename().getBytes("8859_1"), "UTF-8");
					if ( "92".equalsIgnoreCase(outChkStepCd))	{	// 처리완료
						fileName = yyyyMMddHHmmss+"_"+outRqst.get("vdoYmdhmsFrOrgn")+"_"+outRqst.get("vdoYmdhmsToOrgn")+"_"+new String(inFile.getOriginalFilename().getBytes("8859_1"), "UTF-8");
					}
					
					logger.debug("--> responseEncVdo() fileName:{}", fileName);

					String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
					
					saveVideoFile(file.getInputStream(), fileName, rootPath + File.separator + filePath);		// 영상파일 저장

					if ( "40".equalsIgnoreCase(outChkStepCd))		{	params.put("outFileNm", fileName);		}	// 입수완료
					else if ( "50".equalsIgnoreCase(outChkStepCd))	{	params.put("outFileNmMsk", fileName);	}	// 마스킹완료
					else if ( "92".equalsIgnoreCase(outChkStepCd))	{	params.put("outFileNmDrm", fileName);	}	// 처리완료

					Map<String, String> result = outService.modifyMovieFile(params);
				}
			}

			String outDrmAutoAprvYn = prprtsService.getString("OUT_DRM_AUTO_APRV_YN");
			if ("Y".equals(outDrmAutoAprvYn)) {		// 최종자동승인 사용일 때
				params.put("tvoPrgrsCd", "71");		// 자동반출승인
			}

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String yyyyMMddHHmmss = df.format(new Date());
			params.put("outChkYmdhms", yyyyMMddHHmmss);
			params.put("outAprvUserId", SessionUtil.getUserId());
			outService.updateOutRqst(params);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("--> responseEncVdo() {} : {}", responseCode, responseMessage);
		result_map.put("responseCode",		responseCode);
		result_map.put("responseMessage",	responseMessage);
		return result_map;
	}
	
	
	

	// 영상파일 저장
	protected void saveVideoFile(InputStream stream, String fileName, String filePath) throws Exception {
		OutputStream bos = null;
		try {
			String sOsName = System.getProperty("os.name").toLowerCase();
			if (sOsName.indexOf("win") < 0) {	filePath = filePath.replace("\\", "/");	}
			else {								filePath = filePath.replace("/", "\\");	}
			logger.info("--> saveVideoFile() {}", filePath + File.separator + fileName);

			File dir = new File(filePath);
			if (!dir.isDirectory()) {	Files.createDirectories(Paths.get(filePath));	}

			bos = new FileOutputStream(filePath + File.separator + fileName);

			int bytesRead = 0;
			byte[] buffer = new byte[2048];

			while ((bytesRead = stream.read(buffer, 0, 2048)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} finally {
			close(bos, stream);
		}
	}
	public void close(Closeable  ... resources) {
		for (Closeable resource : resources) {
			if (resource != null) {
				try {
					resource.close();
				} catch (Exception ignore) {
					logger.info("Occurred Exception to close resource is ingored!!");
				}
			}
		}
	}

	
	

	// 마크애니 암호화 진행상태 회신
	@PostMapping("/api/encryptStatus.xx")
	@ResponseStatus(value = HttpStatus.OK)
	public HashMap<String, Object> encryptStatus(@RequestHeader MultiValueMap<String, String> headers
			, @RequestBody Map<String, Object> bodyMap, HttpServletRequest request) throws Exception {
		//headers.forEach((key, value) -> { logger.debug(String.format("--> encryptStatus() Header => '%s' : %s", key, value.stream().collect(Collectors.joining("|")))); });
		//logger.debug("--> bodyMap => {}", bodyMap.toString());
		
  		String responseCode = "000";
  		String responseMessage = "success";
  		HashMap<String, Object> result_map = new HashMap<String, Object>();

		try {
			String result      = CommUtil.objNullToVal(bodyMap.get("result"), "");
			String cid         = CommUtil.objNullToVal(bodyMap.get("cid"), "");
			String[] job_code  = CommUtil.objNullToVal(bodyMap.get("job_code"), "").split(":");
			String status      = CommUtil.objNullToVal(bodyMap.get("status"), "");
			String description = CommUtil.objNullToVal(bodyMap.get("description"), "");
			String percent     = CommUtil.objNullToVal(bodyMap.get("percent"), "");
			if ( Integer.parseInt(percent) % 10 == 0 ) {
				logger.debug("--> encryptStatus(), result={}, job_code={}, percent={}", result, job_code, percent);
				
				Map<String, String> params = new HashMap<String, String>();
				params.put("dstrtCd"   , prprtsService.getString("DSTRT_CD") );
				params.put("outRqstNo" , job_code[0]);
				params.put("outFileSeq", job_code[1]);
				params.put("drmPcnt"   , percent    );		// 암호화퍼센트
				outService.updateOutFile(params);
				
				if ( "100".equalsIgnoreCase(percent) ) {	// 암호화 완료
					String yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
					
					params.put("outFilePrgrsCd", "92");	// 암호화완료
					params.put("tvoPrgrsYmdhms", yyyyMMddHHmmss);
					outService.updateOutFile(params);
					
					OutSrchVO vo = new OutSrchVO();
					vo.setOutRqstNo(job_code[0]);
					vo.setRecordCountPerPage(9999);
					vo.setFirstRecordIndex(0);
					List<EgovMap> list = outService.selectOutFileList(vo);

					String outChkStepCd = "92";			// 암호화완료
					if ( list.size() != 0 ) {
						logger.debug("--> encryptStatus(), 모든 영상의 암호화 완료여부를 확인한다. <--");
						for ( int i=0;i<list.size();i++) {
							String outFilePrgrsCd = list.get(i).get("outFilePrgrsCd").toString();
							logger.debug("--> encryptStatus(), outFilePrgrsCd => "+outFilePrgrsCd);
							if ( !"92".equalsIgnoreCase(outFilePrgrsCd)) {		// 암호화완료 아닌 것이 있을 때
								outChkStepCd = "75";	// 암호화중
							//} else if ( "92".equalsIgnoreCase(outChkStepCd) && "77".equalsIgnoreCase(outFilePrgrsCd)) {
							} else if ( "77".equalsIgnoreCase(outFilePrgrsCd)) {	// 암호화실패
								outChkStepCd = "77";	// 암호화실패
							} else {
							}
						}
					}
					logger.debug("--> encryptStatus(), outChkStepCd1 => "+outChkStepCd);

					if ( "92".equalsIgnoreCase(outChkStepCd) ) {	// 암호화완료
						// 이미 반출승인된 건인지 확인(재생기간연장인지 확인)
						EgovMap outRqst = outService.selectOutRqstDtl(params);	// 반출신청정보조회
						String tvoPrgrsCd = outRqst.get("tvoPrgrsCd").toString();
						logger.debug("--> encryptStatus(), tvoPrgrsCd => "+tvoPrgrsCd);
						if ("70".equalsIgnoreCase(tvoPrgrsCd) || "71".equalsIgnoreCase(tvoPrgrsCd)) { // 이미 반출승인일 때

							outChkStepCd = "94";				// 승인완료
						} else {
							String outDrmAutoAprvYn = prprtsService.getString("OUT_DRM_AUTO_APRV_YN");
							if ("Y".equals(outDrmAutoAprvYn)) {		// 최종자동승인 사용일 때
								params.put("tvoPrgrsCd", "71");		// 자동반출승인
								outChkStepCd = "94";				// 승인완료
							}
						}
					}
					logger.debug("--> encryptStatus(), outChkStepCd2 => "+outChkStepCd);
					
					if ( !"".equalsIgnoreCase(outChkStepCd) ) {	// 모든 영상의 암호화를 수행했을 때, 77or92
						params.put("outChkStepCd", outChkStepCd);
						params.put("outChkYmdhms", yyyyMMddHHmmss);
						params.put("outAprvUserId", SessionUtil.getUserId());
						outService.updateOutRqst(params);
					}

					
					// 암호화 파일의 해쉬값을 추출하고 저장한다.
					Thread.sleep(1000);
					EgovMap tvoOutFile = outService.selectOutFileDtl(params);
					
					//String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
					//rootPath = rootPath + prprtsService.getString("FTP_VDO_DIR");
					String DIR_CCTV_CONTENTS = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV_CONTENTS");
					
			    	String outFileDrm = DIR_CCTV_CONTENTS + File.separator + tvoOutFile.get("outFilePath").toString() + File.separator + tvoOutFile.get("outFileNmDrm").toString();
					String sOsName = System.getProperty("os.name").toLowerCase();
					if (sOsName.indexOf("win") < 0) {	outFileDrm = outFileDrm.replace("\\", "/");
					} else {							outFileDrm = outFileDrm.replace("/", "\\");
					}
			    	logger.debug("--> encryptStatus(), outFileDrm => {}", outFileDrm);
					String drmHash = SecurityAny.getFileHash(outFileDrm);
			    	logger.debug("--> encryptStatus(), drmHash => {}", drmHash);
					params.put("drmHash", drmHash);		// 암호화해쉬
					outService.updateOutFile(params);
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//logger.debug("--> encryptStatus() {} : {}", responseCode, responseMessage);
		result_map.put("responseCode",		responseCode);
		result_map.put("responseMessage",	responseMessage);
		return result_map;
	}
	

	@RequestMapping(value="/api/responseMasking.xx")
	public HashMap<String, Object> responseMasking(@RequestHeader MultiValueMap<String, String> headers
			, @RequestParam Map<String, Object> paraMap, HttpServletRequest request, ModelMap model) throws Exception {
		logger.debug("--> responseMasking() => {}", paraMap.toString());
		for( String strKey : paraMap.keySet() ){ System.out.println( "--> responseMasking() "+strKey +" : "+ (String) paraMap.get(strKey) ); }

  		String responseCode = "000";
  		String responseMessage = "success";
  		HashMap<String, Object> result_map = new HashMap<String, Object>();

		try {  		
			Map<String, String> params = new HashMap<String, String>();
			for( String strKey : paraMap.keySet() ) {
				params.put(strKey, EgovStringUtil.nullConvert(paraMap.get(strKey)));
			}
			
			String ymdhms = EgovStringUtil.getTimeStamp("yyyyMMddhhmmss");
			params.put("outChkYmdhms", ymdhms);			
			params.put("dstrtCd"   , prprtsService.getString("DSTRT_CD")                  );
			params.put("outRqstNo" , EgovStringUtil.nullConvert(paraMap.get("reqId"))     );
			params.put("outFileSeq", EgovStringUtil.nullConvert(paraMap.get("contentSeq")));

			String maskingStatus = EgovStringUtil.nullConvert(params.get("maskingStatus"));

			if ("03".equals(maskingStatus)) {	// 성공일 때
				params.put("outFileNmMsk", EgovStringUtil.nullConvert(paraMap.get("maskingFileName")));
				params.put("outFilePrgrsCd", "50");		// 마스킹완료
			} else {
				params.put("outFilePrgrsCd", "47");		// 마스킹실패
				
			}
			outService.updateOutFile(params);

			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			String yyyyMMddHHmmss = df.format(new Date());
			
			OutSrchVO vo = new OutSrchVO();
			vo.setOutRqstNo(params.get("outRqstNo"));
			vo.setRecordCountPerPage(9999);
			vo.setFirstRecordIndex(0);
			List<EgovMap> list = outService.selectOutFileList(vo);
			String outChkStepCd = "50";			// 마스킹완료
			if ( list.size() != 0 ) {
				logger.debug("--> 모든 영상의 마스킹 완료여부를 확인한다. <--");
				for ( int i=0;i<list.size();i++) {
					String outFilePrgrsCd = list.get(i).get("outFilePrgrsCd").toString();
					logger.debug("--> outFilePrgrsCd => "+outFilePrgrsCd);
					if ( "".equalsIgnoreCase(outFilePrgrsCd)) {
						outChkStepCd = "";
					} else if ( "50".equalsIgnoreCase(outChkStepCd) && "47".equalsIgnoreCase(outFilePrgrsCd)) {
						outChkStepCd = "47";	// 마스킹실패
					}
				}
			}
			logger.debug("--> outChkStepCd => "+outChkStepCd);
			if ( !"".equalsIgnoreCase(outChkStepCd) ) {	// 모든 영상의 마스킹을 수행했을 때, 47or50
				params.put("outChkStepCd", outChkStepCd);
				params.put("outChkYmdhms", yyyyMMddHHmmss);
				outService.updateOutRqst(params);
			}
			
		//	if ("03".equals(maskingStatus)) {	// 성공일 때
		//		params.put("maskingEndYn", "Y");
		//		params.put("maskingEndYmdhms", ymdhms);
		//	} else {							// 실패일 때 
		//		params.put("outChkStepCd", "47");	// 마스킹실패
		//	}

			if ("03".equals(maskingStatus)) {	// 성공일 때
				// 암호화 작업 시작				
				EgovMap outFile = outService.selectOutFileDtl(params);

				String outFilePlayCnt = prprtsService.getString("OUT_FILE_PLAY_CNT");	// 영상재생횟수
				if ( !"".equalsIgnoreCase(outFilePlayCnt)) {
					int cnt = Integer.parseInt(outFilePlayCnt);
					if ( cnt < 1 ) {
						outFilePlayCnt = "-1";	// 재생횟수 무제한
					}
				} else {
					outFilePlayCnt = "-1";	// 재생횟수 무제한
				}
				params.put("outRqstNo", outFile.get("outRqstNo").toString());
				params.put("playCnt", outFilePlayCnt);

				params.put("fileSeq", outFile.get("outFileSeq").toString());
				params.put("filePath", outFile.get("outFilePath").toString());
				params.put("cptnFileNm", outFile.get("cptnFileNm").toString());	// 자막파일명
				
				params.put("objFileNm", outFile.get("outFileNmMsk").toString());	// 대상파일명

				String drmFileNm = outFile.get("outRqstNo").toString()+"_"+outFile.get("fcltLblNm").toString()
						+"_"+outFile.get("outVdoYmdhmsFr").toString().substring(0,12)
						+"_"+outFile.get("outVdoYmdhmsTo").toString().substring(0,12)+".cctv";
				params.put("drmFileNm", drmFileNm);	// 암호화파일명
				
				logger.info("--> responseMasking(), params => {}", params.toString());
				params = outService.sendRequestEncrypt(params);		// 암호화 요청

				params.put("outChkYmdhms", yyyyMMddHHmmss);
				params.put("outAprvUserId", SessionUtil.getUserId());
				outService.updateOutRqst(params);
				
				

				JSONObject event = new JSONObject();
				JSONObject data = new JSONObject();
				event.put("evtId", "MASK");
				event.put("data", data);
				//websocketClient(event.toString(), (String) request.getParameter("wsUrl"));
				websocketClient(event.toString());
				
				
				
				
			}
			
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		result_map.put("responseCode",		responseCode);
		result_map.put("responseMessage",	responseMessage);
		return result_map;
	}
	
	

	
	
	
	
	
	
	
	
	
	

	//private void websocketClient(String wsData, String wsUrl) throws Exception {
	private void websocketClient(String wsData) throws Exception {

        String tvoUrl = prprtsService.getString("PVE_URL");
		String uri = tvoUrl+"/ws/evt.do?page=maskCmplt";
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
