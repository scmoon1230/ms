package kr.co.ucp.pve.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.ucp.cmns.EgovStringUtil;
import kr.co.ucp.cmns.RestUtilMA;
import kr.co.ucp.cmns.TvoUtil;
import kr.co.ucp.pve.mapper.PveMapper;

@Service("pveService")
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
public class PveServiceImpl implements PveService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PveMapper pveMapper;

	public HashMap<String, String> selectCmPrprts(HashMap<String, String> params) throws Exception {

		return pveMapper.selectCmPrprts(params);
		
	}

	// 암호화대기 상태의 파일을 DB에서 조회하여 암호화를 요청한다.
	@Override
	public void requestMaEncVdo(HashMap<String, String> params) throws Exception {

		logger.info("--> requestMaEncVdo() 영상암호화 함수 시작 <----------------------------------------------------------------------------------------------------");

		int ONETIMECNT = 4;
		
		try {

			List<HashMap> egovMapList = (ArrayList<HashMap>) pveMapper.selectPrprtsList(params.get("dstrtCd").toString());
			
			String outFilePlayCnt = TvoUtil.getPrprtsValue(egovMapList,"OUT_FILE_PLAY_CNT");	// 영상재생횟수
			if ( !"".equalsIgnoreCase(outFilePlayCnt)) {
				int cnt = Integer.parseInt(outFilePlayCnt);
				if ( cnt < 1 ) {
					outFilePlayCnt = "-1";	// 재생횟수 무제한
				}
			} else {
				outFilePlayCnt = "-1";	// 재생횟수 무제한
			}

			String maVdoEncUrls = getMaAddedInfos(egovMapList, "VDO_ENC_URL");	// 복수개의 암호화시스템 url 을 합쳐서 가져온다.
			String[] vdoEncUrls = maVdoEncUrls.split(",");		// 암호화시스템 url


			String maVdoEncFilePaths = getMaAddedPaths(egovMapList, "VDO_ENC_FILE_PATH");	// 복수개의 암호화  파일경로를 합쳐서 가져온다.
			String[] vdoEncFilePaths = maVdoEncFilePaths.split(",");		// 암호화 파일경로

			
			for ( int i=0 ; i<vdoEncUrls.length ; i++ ) {	// 암호화시스템 url 개수만큼 반복
				logger.info("--> requestMaEncVdo(), {}, {} <--", i, vdoEncUrls[i]);

				HashMap<String, Object> paramsWaiting = new HashMap<String, Object>();
				String[] waitingOutFilePrgrsCdList = {"70", "77"};	// 암호화대기, 암호화실패
				paramsWaiting.put("outFilePrgrsCdList",waitingOutFilePrgrsCdList);
				List<HashMap<String, String>> waitingList = pveMapper.selectOutFileWaitingList(paramsWaiting);
				
				if ( waitingList.size() != 0 ) {	// 암호화 대상이 있을 때

					params.put("vdoEncUrl"      , vdoEncUrls[i]);		// 암호화 url 지정
					params.put("outFilePrgrsCd" , "75");				// 암호화중
					List<HashMap<String, String>> workingList = pveMapper.selectOutFileList(params);
					
					//if ( workingList.size() >= ONETIMECNT ) {	// 암호화url별로 암호화중인 파일이 정해진 개수 이상일 때
					if ( workingList.size() >= (ONETIMECNT/2) ) {	// 암호화url별로 암호화중인 파일이 정해진 개수의 절반 이상일 때
						logger.info("--> requestMaEncVdo(), {}, {} 경로의 암호화모듈에서 한번에 {}개 파일 암호화를 요청할 수 있는데, 진행중인 파일이 {}개 있으므로 요청하지 않는다. <--", i, vdoEncUrls[i], ONETIMECNT, workingList.size());
						
					} else {
						logger.info("--> requestMaEncVdo(), {}, {} 경로의 암호화모듈에서 진행중인 파일이 {}개 있으므로 영상 암호화를 요청한다. <--", i, vdoEncUrls[i], workingList.size());

						int ind = 0;
						for ( int j=0 ; j<waitingList.size() ; j++ ) {
							ind ++;
							
							if ( ONETIMECNT < ind ) break;	// 정해진 개수만 요청한다.
								
							HashMap<String, String> tvoOutFile = waitingList.get(j);

							HashMap<String, String> params2 = new HashMap<String, String>();
							params2.put("dstrtCd"   , tvoOutFile.get("dstrtCd").toString()    );
							params2.put("outRqstNo" , tvoOutFile.get("outRqstNo").toString()  );
							HashMap<String, String> outRqst = pveMapper.selectOutRqstDtl(params2);
							
							if ( "".equalsIgnoreCase(EgovStringUtil.nullConvert(outRqst.get("vdoEncUrl")))) {	// 암호화시스템 url이 지정되지 않았을 때
								params2.put("vdoEncUrl", vdoEncUrls[i]);
								pveMapper.updateOutRqst(params2);
							}
							
							params2.put("vdoEncFilePath" , vdoEncFilePaths[i]);	// 암호화 파일경로 지정

							params2.put("playCnt"   , outFilePlayCnt                          );
							params2.put("fileSeq"   , String.valueOf(tvoOutFile.get("outFileSeq")) );
							params2.put("filePath"  , tvoOutFile.get("outFilePath").toString());
							params2.put("cptnFileNm", tvoOutFile.get("cptnFileNm").toString() );	// 자막파일명
							
							String objFileNm = tvoOutFile.get("outFileNm").toString();				// 대상파일명 : 원본 파일
							if ( "Y".equalsIgnoreCase(outRqst.get("maskingYn").toString()) ) {		// 마스킹할 때
								objFileNm = tvoOutFile.get("outFileNmMsk").toString();				// 대상파일명 : 마스킹된 파일
							}
							params2.put("objFileNm", objFileNm);	// 대상파일명

							String mngSn = tvoOutFile.get("mngSn").toString();
							if ( !"".equalsIgnoreCase(mngSn) ) mngSn = mngSn+"_";
							
							String drmFileNm = tvoOutFile.get("outRqstNo").toString().substring(7)
									+"_" + mngSn + tvoOutFile.get("fcltLblNm").toString()
									+"_"+tvoOutFile.get("outVdoYmdhmsFr").toString().substring(0,14)
									+"_"+tvoOutFile.get("outVdoYmdhmsTo").toString().substring(0,14)+".cctv";
							params2.put("drmFileNm", drmFileNm);	// 암호화파일명
							
							logger.info("--> requestMaEncVdo() params2 => {}", params2.toString());
							
							sendRequestEncrypt(params2);		// 암호화 요청
							
						}
						logger.info("--> requestMaEncVdo() 암호화 요청을 완료한다. <--");	
					}
				}
			}
			
		} catch (Exception e) {
			logger.error("=== requestMaEncVdo Exception >>>> :[{}]", e.getMessage() );
			e.printStackTrace();
		}
		
		logger.info("--> requestMaEncVdo() 영상암호화 함수 끝 <====================================================================================================");

	}


	public void sendRequestEncrypt(HashMap<String, String> params) throws Exception {

		//String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		//rootPath = rootPath + prprtsService.getString("FTP_VDO_DIR");
		List<HashMap> egovMapList = (ArrayList<HashMap>) pveMapper.selectPrprtsList(params.get("dstrtCd").toString());

		//String DIR_CCTV_CONTENTS = TvoUtil.getPrprtsValue(egovMapList,"DIR_WRKS_HOME") + TvoUtil.getPrprtsValue(egovMapList,"DIR_CCTV_CONTENTS");		
		String DIR_CCTV_CONTENTS = params.get("vdoEncFilePath").toString();
		logger.info("--> sendRequestEncrypt() DIR_CCTV_CONTENTS => "+DIR_CCTV_CONTENTS);

		
		//String playCnt = params.get("playCnt").toString();
		String fileSeq = params.get("fileSeq").toString();
		String filePath = params.get("filePath").toString();
		String fileFullPath = DIR_CCTV_CONTENTS + File.separator + filePath;
		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	fileFullPath = fileFullPath.replace("\\", "/");
		} else {							fileFullPath = fileFullPath.replace("/", "\\");
		}

		String cptnFileNm = params.get("cptnFileNm").toString();
		String caption = "";
		if ( !"".equalsIgnoreCase(cptnFileNm) ) {	// 자막파일이 있을 때
			String cptnFilePath = fileFullPath + File.separator + cptnFileNm;
			caption = TvoUtil.getBase64Content(cptnFilePath);
		}
		params.put("caption", caption);

		String objFileNm = params.get("objFileNm").toString();
		String objFileFullPath = fileFullPath + File.separator + objFileNm;
		logger.info("--> sendRequestEncrypt() objFileFullPath => "+objFileFullPath);
		params.put("objFileFullPath", objFileFullPath);

		String drmFileNm = params.get("drmFileNm").toString();
		String drmFileFullPath = fileFullPath + File.separator + drmFileNm;
		logger.info("--> sendRequestEncrypt() drmFileFullPath => "+drmFileFullPath);
		params.put("drmFileFullPath", drmFileFullPath);

		HashMap<String, String> tvoOutRqst = pveMapper.selectOutRqstDtl(params);
		
		params.put("thirdPartyPw",tvoOutRqst.get("thirdPartyPw").toString());
		params.put("playStartYmdhms",tvoOutRqst.get("playStartYmdhms").toString());
		params.put("playEndYmdhms",tvoOutRqst.get("playEndYmdhms").toString());
		params.put("outRqstUserId",tvoOutRqst.get("outRqstUserId").toString());
		
		params.put("userId",tvoOutRqst.get("outRqstUserId").toString());	// 신청자 정보 조회용
		HashMap<String, String> userInfo = pveMapper.selectUserInfo(params);
		
		String playPwd = userInfo.get("playPwd").toString();
		//logger.info("--> requestMaEncrypt(), playPwd : {}", playPwd);
		playPwd = new String(Base64.decodeBase64(playPwd.getBytes()));
		logger.info("--> sendRequestEncrypt(), playPwd : {}", playPwd);
		params.put("playPwd",playPwd);
		
		
		Map<String, Object> responseMap = new HashMap<String, Object>();

		String encModuleSupplier = TvoUtil.getPrprtsValue(egovMapList,"ENC_MODULE_SUPPLIER");
		logger.info("--> sendRequestEncrypt() encModuleSupplier => {}", encModuleSupplier);
		
		if ( "markany".equalsIgnoreCase(encModuleSupplier) ) {	// 마크애니 암호화 요청하기
	        
			String VDO_ENC_URL = EgovStringUtil.nullConvert(tvoOutRqst.get("vdoEncUrl"));
			params.put("vdoEncUrl", VDO_ENC_URL);

			params.put("pveUrl", TvoUtil.getPrprtsValue(egovMapList,"PVE_URL"));			
			params.put("imgsaferYn", TvoUtil.getPrprtsValue(egovMapList,"IMGSAFER_YN"));
			
			responseMap = requestMaEncrypt(params);
			
			logger.info("--> sendRequestEncrypt() responseMap.result => {}", responseMap.get("result").toString());
			
			if ( "200".equalsIgnoreCase(responseMap.get("result").toString())) {
				params.put("outFileSeq", fileSeq);
				params.put("outFileNmDrm", drmFileNm);
				params.put("drmPcnt", responseMap.get("percent").toString());		// 암호화퍼센트
				params.put("outFilePrgrsCd", "75");	// 암호화중
				
				params.put("outChkStepCd", "75");	// 암호화중
				
			} else {
				params.put("outFilePrgrsCd", "77");	// 암호화실패
				
				params.put("outChkStepCd", "77");	// 암호화실패
				logger.error("--> sendRequestEncrypt(), contentEncrypt : 암호화 실패 !!!");
			}
			pveMapper.updateOutFile(params);
			
			pveMapper.updateOutRqst(params);
			
		}
		
	}
	

	// 마크애니 영상암호화 요청
	public Map<String, Object> requestMaEncrypt(Map<String, String> params) throws Exception {

		String tmp = params.get("objFileFullPath").toString();
		String objFileExt = tmp.substring(tmp.lastIndexOf('.')+1).toUpperCase();
		
		Map<String, Object> responseMap = new HashMap<String, Object>();

		//List<HashMap> egovMapList = (ArrayList<HashMap>) pveMapper.selectPrprtsList(params.get("dstrtCd").toString());
		
		String VDO_ENC_URL = params.get("vdoEncUrl").toString();
		String keyUrl = VDO_ENC_URL + "/api/v3.0/generateCek/512";	// 마크애니 cek 생성 함수
		String encUrl = VDO_ENC_URL + "/api/v3.0/contentEncrypt";	// 마크애니 암호화 함수
		logger.info("--> requestMaEncrypt(), keyUrl => {}", keyUrl);
		logger.info("--> requestMaEncrypt(), encUrl => {}", encUrl);

        String PVE_URL = params.get("pveUrl").toString();
		String staUrl = PVE_URL + "/api/encryptStatusXx.do";		// 암호화 진행률 회신받을 함수
		logger.info("--> requestMaEncrypt(), staUrl => {}", staUrl);
		
		responseMap = RestUtilMA.getHttp(keyUrl, 0, "");
		logger.info("--> requestMaEncrypt(), generateCek >>>> responseMap : {}", responseMap.toString());

		if ( responseMap.get("result") == null ) {
			logger.info("--> requestMaEncrypt(), generateCek >>>> result is null !!! ====");
			
		} else {
			String result = responseMap.get("result").toString();
			if ( "200".equalsIgnoreCase(result)) {
				Map<String, Object> dataMap = new HashMap<>();
				dataMap.put("cek", responseMap.get("cek").toString());
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				dataMap.put("cid"       , df.format(new Date()));
				dataMap.put("domain"    , "WIDECUBE_DRM");
				dataMap.put("job_code"  , params.get("outRqstNo").toString()+":"+params.get("fileSeq").toString());
				dataMap.put("src"       , params.get("objFileFullPath").toString());
				dataMap.put("ext"       , objFileExt);	
				dataMap.put("dst"       , params.get("drmFileFullPath").toString());	
				dataMap.put("caption"   , params.get("caption").toString());	
				dataMap.put("caption_charset", "utf-8");
				dataMap.put("user"      , params.get("outRqstUserId").toString());
				dataMap.put("pwd"       , params.get("playPwd").toString());
				dataMap.put("export_pwd", params.get("thirdPartyPw").toString());
				dataMap.put("begin"     , params.get("playStartYmdhms").toString());
				dataMap.put("end"       , params.get("playEndYmdhms").toString());
				dataMap.put("count"     , params.get("playCnt").toString());
				dataMap.put("interface" , staUrl);

				if ( "N".equalsIgnoreCase(params.get("imgsaferYn").toString())) {
					dataMap.put("imgsafer"  , false);
				} else {
					dataMap.put("imgsafer"  , true);
				}
				
			//	dataMap.put("enc_type"  , "1");		// 구버전 암호화모듈용
				dataMap.put("enc_type"  , "2");		// 신버전 암호화모듈용
				
				logger.info("--> requestMaEncrypt(), contentEncrypt >>>> dataMap : {}", dataMap);
				
				responseMap = RestUtilMA.postHttp(encUrl, dataMap, 0, "");
				logger.info("--> requestMaEncrypt(), contentEncrypt >>>>> responseMap : {}", responseMap.toString());
				
				result = responseMap.get("result").toString();
				responseMap.put("result", result);
				
			} else {
				responseMap.put("result", result);
			}
		}
				
		return responseMap;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	// 마크애니 영상입수 결과파일 result.xml 을 읽어서 DB에 등록하고 결과파일은 이동한다.
	@Override
	public void workMaOrgVdoResult(HashMap<String, String> params) throws Exception {

		logger.info("--> workMaOrgVdoResult() 영상이동 함수 시작 <----------------------------------------------------------------------------------------------------");

		try {

			List<HashMap> egovMapList = (ArrayList<HashMap>) pveMapper.selectPrprtsList(params.get("dstrtCd").toString());
			
			String maResultPaths = getMaAddedPaths(egovMapList, "RESULT_FILE_PATH");	// result.xml 경로는 복수개 가능
			String[] resultPaths = maResultPaths.split(",");
			
			for ( int i=0 ; i<resultPaths.length ; i++ ) {
				String resultPath = resultPaths[i];
				logger.info("--> [{}] workMaOrgVdoResult() resultPath => {}", i, resultPath);

				File dir = new File(resultPath);
				String[] filenames = dir.list();
				
				if ( filenames != null ) {
					for (String filename : filenames) {			// 파일별로 작업한다.
						
						String resultXmlFullPath = resultPath + File.separator + filename;
						//logger.info("--> workMaOrgVdoResult() resultXmlFullPath => " + resultXmlFullPath);
						
						boolean isTempFile = false;
						File tempFile = new File(resultXmlFullPath);
						if (tempFile.exists()) {
							if (tempFile.isDirectory()) {
								//System.out.println("디렉토리가 존재합니다");
							} else if (tempFile.isFile()) {
								isTempFile = true;		//System.out.println("파일이 존재합니다");
							}
						} else {
							//System.out.println("파일이나 디렉토리가 존재하지 않습니다");
						}
						
						if ( isTempFile ) { // result 파일일 때
							logger.info("--> [{}] workMaOrgVdoResult() resultXmlFullPath file => {}", i, resultXmlFullPath);


							try {
								

								
								Map<String, Object> resultMap = new HashMap();
								List<Map<String, Object>> fileMapList = new ArrayList<Map<String, Object>>();
								//int fileSeq = -1;
							//	Document document = DocumentHelper.parseText(str);			// 1-1. 문자열 파싱 시
								Document document = new SAXReader().read(resultXmlFullPath);// 1-2. 파일 파싱 시
								Element rootElement =  document.getRootElement();			// 2. Root Element
								List<Element> branchElements = rootElement.elements();		// 3. Branch Element
								for(Element branchElement : branchElements) {
								//	String branchAttributeId = branchElement.attributeValue("id");
								//	System.out.println("branch attribute id : " + branchAttributeId);
									String name = branchElement.getName();
									String value = branchElement.getText().trim();
									if ( !"".equalsIgnoreCase(value) ) {
										// System.out.println("--- "+name + " : " + value);
										resultMap.put(name, value);
									}
									if ( "File".equalsIgnoreCase(name) ) {
										List<Element> childElements =  branchElement.elements();
										// 4. Children Element
										if ( childElements.size() != 0 ) {
											Map<String, Object> fileMap = new HashMap();
											for(Element childElement : childElements){
												String childName = childElement.getName();
												String childValue = childElement.getText();
												// System.out.println("----- "+childName + " : " + childValue);
												fileMap.put(childName, childValue);
											}
											fileMapList.add(fileMap);
										}
									}
								}
								resultMap.put("File", fileMapList);
								logger.info("--> workMaOrgVdoResult() resultMap => " + resultMap.toString());
								
								
								
								
								
								String currRootPath = resultPaths[i].substring(0,resultPaths[i].indexOf("vms")-1);
								String mainRootPath = resultPaths[0].substring(0,resultPaths[0].indexOf("vms")-1);
								
								String ORG_VDO_DVD_RQST_YN = TvoUtil.getPrprtsValue(egovMapList,"ORG_VDO_DVD_RQST_YN");	// 원본영상 분할요청 여부
								logger.info("--> workMaOrgVdoResult() ORG_VDO_DVD_RQST_YN => " + ORG_VDO_DVD_RQST_YN);
								
								if ( "N".equalsIgnoreCase(ORG_VDO_DVD_RQST_YN) ) {		// 원본영상 미분할요청 할 때
									doOrgFullVdoResultMap(params.get("dstrtCd").toString(), currRootPath, mainRootPath, resultMap);	// 원본영상 미분할요청 결과처리
									
								} else {
									doOrgDvdVdoResultMap(params.get("dstrtCd").toString(), currRootPath, mainRootPath, resultMap);	// 원본영상 분할요청 결과처리
								}

								
								
								
								// result 파일을 log 폴더로 이동시킨다.
								SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
								String yyyyMMddHHmmss = df.format(new Date());
								String tmp = filename.substring(0,filename.indexOf("."));
								String ext = filename.substring(filename.indexOf(".")+1);
								String logXmlPath = resultPaths[0] + File.separator + "complete";	// dataHome으로 복사한다.
								String logFileName = tmp+"_"+yyyyMMddHHmmss.substring(0,8)+"_"+yyyyMMddHHmmss.substring(8,14)+"."+ext;
								if ( !"1".equalsIgnoreCase(resultMap.get("Status").toString()) ) {	// 성공이 아닐 때 다른 폴더에 보관한다.
									logXmlPath = resultPaths[0] + File.separator + "error";			// dataHome으로 복사한다.
									logFileName = tmp+"_"+yyyyMMddHHmmss.substring(0,8)+"_"+yyyyMMddHHmmss.substring(8,14)+"-error."+ext;
								}
								String logXmlFullPath = logXmlPath + File.separator + logFileName;
								logger.info("--> workMaOrgVdoResult() logXmlFullPath => "+logXmlFullPath);
								
								File resultXmlFile = new File(resultXmlFullPath);
								File logXmlFile = new File(logXmlFullPath);
								FileUtils.copyFile(resultXmlFile, logXmlFile);	// 파일복사
								logger.info("--> workMaOrgVdoResult() => " + filename +" 파일을 log 폴더로 복사하였음. <--");
								resultXmlFile.delete();							// 파일삭제
								logger.info("--> workMaOrgVdoResult() => " + filename +" 파일을 삭제하였음. <--");
								
								
								
								
								
							} catch (Exception e) {
								logger.error("--> [{}] workMaOrgVdoResult() resultXmlFullPath file => {}", i, resultXmlFullPath);
								logger.error("=== workMaOrgVdoResult() Exception >>>> :[{}]", e.getMessage() );
								e.printStackTrace();
							}
							

							
						}
					}
						
				}
			}

		} catch (Exception e) {
			logger.error("=== workMaOrgVdoResult Exception() >>>> :[{}]", e.getMessage() );
			e.printStackTrace();
		}
		
		logger.info("--> workMaOrgVdoResult() 영상이동 함수 끝   <====================================================================================================");

	}

	// 원본 분할영상 요청결과 처리
	public void doOrgDvdVdoResultMap(String dstrtCd, String currRootPath, String mainRootPath, Map<String, Object> resultMap) throws Exception {
		logger.info("--> doOrgDvdVdoResultMap() resultMap => "+resultMap.toString());
		
		String[] reqId = resultMap.get("ReqId").toString().split("-");
		String outRqstNo = reqId[0];
		String outFileSeq = reqId[1];
		//String outRqstNo = resultMap.get("ReqId").toString();

		HashMap<String, String> params = new HashMap();
		params.put("dstrtCd", dstrtCd);
		params.put("outRqstNo", outRqstNo);
		//params.put("outFileSeq", outFileSeq);

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String yyyyMMddHHmmss = df.format(new Date());
		params.put("outChkYmdhms", yyyyMMddHHmmss);
		params.put("tvoPrgrsYmdhms", yyyyMMddHHmmss);
		
		if ( !"1".equalsIgnoreCase(resultMap.get("Status").toString()) ) {	// 성공이 아닐 때
			String statusString = "";
			if ( resultMap.get("StatusString") != null ) {
				statusString = resultMap.get("StatusString").toString();	
			}
			logger.error("--> doOrgDvdVdoResultMap(), 영상입수 실패하였음!!!, outRqstNo:"+outRqstNo+", statusString:"+statusString+" <--");

			params.put("outFilePrgrsCd", "27");				// 입수실패
			params.put("outFileStepDtl", statusString);		// 상세내용
			pveMapper.updateOutFile(params);
			
			params.put("outChkStepCd", "27");				// 입수실패
			params.put("outChkStepDtl", statusString);		// 상세내용
			pveMapper.updateOutRqst(params);
			
		} else {	// 성공일 때

			String outFilePath = resultMap.get("OrgFilePath").toString().substring(1,10);
			params.put("outFilePath", outFilePath);
			logger.info("--> doOrgDvdVdoResultMap() outFilePath => "+outFilePath);
			
			String filePath = resultMap.get("RootPath").toString()+resultMap.get("OrgFilePath").toString();
			
			String tempPath = filePath.substring(0,filePath.length()-1);
			String upperPath = tempPath.substring(0,tempPath.lastIndexOf("/")+1);
					
			String resultFilePath = currRootPath + filePath;
			String upperFilePath = mainRootPath + upperPath;
			
			String sOsName = System.getProperty("os.name").toLowerCase();
			if (sOsName.indexOf("win") < 0) {	resultFilePath = resultFilePath.replace("\\", "/");
												upperFilePath = upperFilePath.replace("\\", "/");
			} else {							resultFilePath = resultFilePath.replace("/", "\\");
												upperFilePath = upperFilePath.replace("/", "\\");
			}
			logger.info("--> doOrgDvdVdoResultMap() resultFilePath => "+resultFilePath);
			logger.info("--> doOrgDvdVdoResultMap() upperFilePath  => "+upperFilePath);

			String maskingYn = "";
			HashMap<String, String> outRqstDtl = pveMapper.selectOutRqstDtl(params);	// 반출신청정보조회
			if ( outRqstDtl != null ) {
				maskingYn = outRqstDtl.get("maskingYn").toString();
			}
			
			List<Map<String, Object>> fileMapList = (List<Map<String, Object>>) resultMap.get("File");
			
			for ( int i=0 ; i<fileMapList.size() ; i++ ) {
				Map<String, Object> fileMap = (Map<String, Object>) fileMapList.get(i);
				String fileType = fileMap.get("OrgFileType").toString();
				String fileName = fileMap.get("OrgFileName").toString();
				String fileSize = fileMap.get("OrgFileSize").toString();
				logger.info("--> doOrgDvdVdoResultMap(), fileType:{}, fileName:{}, fileSize:{} ", fileType, fileName, fileSize);
				
				if ( fileName.indexOf(outRqstNo) == -1 ) {	// 해당 요청의 파일이 아닐 때 (이전 요청의(이미 이동된) 파일의 정보가 들어가 있기도 함)
					logger.info("--> doOrgDvdVdoResultMap() => " + fileName +" 파일은 해당 요청의 파일이 아님. <--");
				} else {
					// 영상 및 자막 파일을 상위 폴더로 이동시킨다.
					String resultMovFullPath = resultFilePath + fileName;
					String upperMovFullPath = upperFilePath + fileName;
					logger.info("--> doOrgDvdVdoResultMap(), resultMovFullPath => "+resultMovFullPath);
					logger.info("--> doOrgDvdVdoResultMap(), upperMovFullPath  => "+upperMovFullPath);
					
					File resultMovFile = new File(resultMovFullPath);
					File upperMovFile = new File(upperMovFullPath);
					FileUtils.copyFile(resultMovFile, upperMovFile);	// 파일복사
					logger.info("--> doOrgDvdVdoResultMap() => " + fileName +" 파일을 상위 폴더로 복사하였음. <--");
					resultMovFile.delete();								// 파일삭제
					logger.info("--> doOrgDvdVdoResultMap() => " + fileName +" 파일을 삭제하였음. <--");
					
					// 영상 및 자막 파일정보를 등록한다.
					//logger.info("--> doOrgDvdVdoResultMap() => " + fileName.substring(0,fileName.indexOf(".")));
					//String[] temps = fileName.substring(0,fileName.indexOf(".")).split("_");

					//String outFileSeq = "0";
					//if ( temps.length == 4 ) {
						//outFileSeq = String.valueOf(Integer.parseInt(temps[3]));						
					//}		
					params.put("outFileSeq", outFileSeq);
					
					//String[] outVdoYmdhmsFrTo = temps[2].split("-");
					//params.put("outVdoYmdhmsFr", outVdoYmdhmsFrTo[0]);
					//params.put("outVdoYmdhmsTo", outVdoYmdhmsFrTo[1]);

					if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
						params.put("outFilePrgrsCd", "40");			// 입수완료
					} else {
						params.put("outFilePrgrsCd", "70");			// 암호화대기
					}
					
					
					if ( fileType.indexOf("smi") != -1 ) {		// 자막파일일 때
					//	if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
					//		params.put("outFilePrgrsCd", "40");			// 입수완료
					//	} else {
					//		params.put("outFilePrgrsCd", "70");			// 암호화대기
					//	}
						params.put("cptnFileNm", fileName);		// 자막파일명
						params.put("cptnFileSize", fileSize);
						params.put("outFileStepDtl", "");		// 상세내용
						pveMapper.updateOutFile(params);		// 반출파일정보 수정
						//pveMapper.mergeOutFile(params);		// 반출파일정보 등록/수정
						
					} else {									// 자막파일아닐 때(원본파일일 때)
					//	if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
					//		params.put("outFilePrgrsCd", "40");			// 입수완료
					//	} else {									// 마스킹 안할 때
					//		params.put("outFilePrgrsCd", "70");			// 암호화대기
					//	}
						params.put("outFileNm", fileName);		// 원본파일명
						params.put("outFileSize", fileSize);
						params.put("outFileStepDtl", "");		// 상세내용
						pveMapper.updateOutFile(params);		// 반출파일정보 수정
						//pveMapper.mergeOutFile(params);		// 반출파일정보 등록/수정
					}
					
				}
				
			}

			if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
				params.put("outChkStepCd", "40");			// 입수완료
			} else {									// 마스킹 안할 때
				params.put("outChkStepCd", "70");			// 암호화대기
			}

			params.put("outChkStepDtl", "");		// 상세내용
			pveMapper.updateOutRqst(params);		// 반출신청정보 수정
		}
	}

	// 원본 미분할영상 요청결과 처리
	public void doOrgFullVdoResultMap(String dstrtCd, String currRootPath, String mainRootPath, Map<String, Object> resultMap) throws Exception {
		logger.info("--> doOrgFullVdoResultMap(), resultMap => "+resultMap.toString());
		
		//String[] reqId = resultMap.get("ReqId").toString().split("-");
		//String outRqstNo = reqId[0];
		//String outFileSeq = reqId[1];
		String outRqstNo = resultMap.get("ReqId").toString();

		HashMap<String, String> params = new HashMap();
		params.put("dstrtCd", dstrtCd);
		params.put("outRqstNo", outRqstNo);
		//params.put("outFileSeq", outFileSeq);

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String yyyyMMddHHmmss = df.format(new Date());
		params.put("outChkYmdhms", yyyyMMddHHmmss);
		params.put("tvoPrgrsYmdhms", yyyyMMddHHmmss);
		
		if ( !"1".equalsIgnoreCase(resultMap.get("Status").toString()) ) {	// 성공이 아닐 때
			String statusString = "";
			if ( resultMap.get("StatusString") != null ) {
				statusString = resultMap.get("StatusString").toString();
			}
			logger.error("--> doOrgFullVdoResultMap(), 영상입수 실패하였음!!!, outRqstNo:"+outRqstNo+", statusString:"+statusString+" <--");

			params.put("outFilePrgrsCd", "27");				// 입수실패
			params.put("outFileStepDtl", statusString);		// 상세내용
			pveMapper.updateOutFile(params);
			
			params.put("outChkStepCd", "27");				// 입수실패
			params.put("outChkStepDtl", statusString);		// 상세내용
			pveMapper.updateOutRqst(params);
			
		} else {	// 성공일 때

			String outFilePath = resultMap.get("OrgFilePath").toString().substring(1,10);
			params.put("outFilePath", outFilePath);
			logger.info("--> doOrgFullVdoResultMap() outFilePath => "+outFilePath);
			
			String filePath = resultMap.get("RootPath").toString()+resultMap.get("OrgFilePath").toString();
			
			String tempPath = filePath.substring(0,filePath.length()-1);
			String upperPath = tempPath.substring(0,tempPath.lastIndexOf("/")+1);
					
			String resultFilePath = currRootPath + filePath;
			String upperFilePath = mainRootPath + upperPath;
			
			String sOsName = System.getProperty("os.name").toLowerCase();
			if (sOsName.indexOf("win") < 0) {	resultFilePath = resultFilePath.replace("\\", "/");
												upperFilePath = upperFilePath.replace("\\", "/");
			} else {							resultFilePath = resultFilePath.replace("/", "\\");
												upperFilePath = upperFilePath.replace("/", "\\");
			}
			logger.info("--> doOrgFullVdoResultMap() resultFilePath => "+resultFilePath);
			logger.info("--> doOrgFullVdoResultMap() upperFilePath  => "+upperFilePath);

			String maskingYn = "";
			HashMap<String, String> outRqstDtl = pveMapper.selectOutRqstDtl(params);	// 반출신청정보조회
			if ( outRqstDtl != null ) {
				maskingYn = outRqstDtl.get("maskingYn").toString();
			}
			
			List<Map<String, Object>> fileMapList = (List<Map<String, Object>>) resultMap.get("File");
			
			for ( int i=0 ; i<fileMapList.size() ; i++ ) {
				Map<String, Object> fileMap = (Map<String, Object>) fileMapList.get(i);
				String fileType = fileMap.get("OrgFileType").toString();
				String fileName = fileMap.get("OrgFileName").toString();
				String fileSize = fileMap.get("OrgFileSize").toString();
				logger.info("--> doOrgFullVdoResultMap(), fileType:{}, fileName:{}, fileSize:{} ", fileType, fileName, fileSize);
				
				if ( fileName.indexOf(outRqstNo) == -1 ) {	// 해당 요청의 파일이 아닐 때 (이전 요청의(이미 이동된) 파일의 정보가 들어가 있기도 함)
					logger.info("--> doOrgFullVdoResultMap() => " + fileName +" 파일은 해당 요청의 파일이 아님. <--");
				} else {
					// 영상 및 자막 파일을 상위 폴더로 이동시킨다.
					String resultMovFullPath = resultFilePath + fileName;
					String upperMovFullPath = upperFilePath + fileName;
					logger.info("--> doOrgFullVdoResultMap(), resultMovFullPath => "+resultMovFullPath);
					logger.info("--> doOrgFullVdoResultMap(), upperMovFullPath  => "+upperMovFullPath);
					
					File resultMovFile = new File(resultMovFullPath);
					File upperMovFile = new File(upperMovFullPath);
					FileUtils.copyFile(resultMovFile, upperMovFile);	// 파일복사
					logger.info("--> doOrgFullVdoResultMap() => " + fileName +" 파일을 상위 폴더로 복사하였음. <--");
					resultMovFile.delete();								// 파일삭제
					logger.info("--> doOrgFullVdoResultMap() => " + fileName +" 파일을 삭제하였음. <--");
					
					// 영상 및 자막 파일정보를 등록한다.
					logger.info("--> doOrgFullVdoResultMap() => " + fileName.substring(0,fileName.indexOf(".")));
					String[] temps = fileName.substring(0,fileName.indexOf(".")).split("_");
					
					String outFileSeq = "0";
					
					//동일한 파일명의 자막 또는 영상파일이 이미 등록되었는 지 확인한다.
					HashMap<String, Object> params2 = new HashMap<String, Object>();
					params2.put("dstrtCd"   , params.get("dstrtCd").toString()    );
					params2.put("outRqstNo" , params.get("outRqstNo").toString()  );
					params2.put("fileNm"    , fileName.substring(0,fileName.indexOf(".")) );		// 파일명
					HashMap<String, Object> outFileSeqMap = pveMapper.selectOutFileSeq(params2);
					
					if ( outFileSeqMap != null ) {	// 동일한 파일명으로 등록한 정보가 있을 때	
						outFileSeq = outFileSeqMap.get("outFileSeq").toString();
					} else {						// 동일한 파일명으로 등록한 정보가 없을 때
						outFileSeqMap = pveMapper.selectOutFileSeqMax(params2);	//동일한 반출요청에 대하여 마지막으로 등록된 정보의 outFileSeq를 가져온다.
						if ( outFileSeqMap != null ) {	// 등록한 정보가 있을 때
							int tmp = Integer.parseInt(outFileSeqMap.get("outFileSeq").toString())+1;
							outFileSeq = String.valueOf(tmp);
						}
					}
					params.put("outFileSeq", outFileSeq);
					
					String[] outVdoYmdhmsFrTo = temps[2].split("-");
					params.put("outVdoYmdhmsFr", outVdoYmdhmsFrTo[0]);
					params.put("outVdoYmdhmsTo", outVdoYmdhmsFrTo[1]);
					
					if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
						params.put("outFilePrgrsCd", "40");			// 입수완료
					} else {
						params.put("outFilePrgrsCd", "70");			// 암호화대기
					}
					
					if ( fileType.indexOf("smi") != -1 ) {		// 자막파일일 때
					//	if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
					//		params.put("outFilePrgrsCd", "40");			// 입수완료
					//	} else {
					//		params.put("outFilePrgrsCd", "70");			// 암호화대기
					//	}
						params.put("cptnFileNm", fileName);		// 자막파일명
						params.put("cptnFileSize", fileSize);
						params.put("outFileStepDtl", "");		// 상세내용
						//pveMapper.updateOutFile(params);		// 반출파일정보 수정
						pveMapper.mergeOutFile(params);			// 반출파일정보 등록/수정
						
					} else {									// 자막파일아닐 때(원본파일일 때)
					//	if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
					//		params.put("outFilePrgrsCd", "40");			// 입수완료
					//	} else {									// 마스킹 안할 때
					//		params.put("outFilePrgrsCd", "70");			// 암호화대기
					//	}
						params.put("outFileNm", fileName);		// 원본파일명
						params.put("outFileSize", fileSize);
						params.put("outFileStepDtl", "");		// 상세내용
						//pveMapper.updateOutFile(params);		// 반출파일정보 수정
						pveMapper.mergeOutFile(params);			// 반출파일정보 등록/수정
					}
					
				}
				
			}

			if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
				params.put("outChkStepCd", "40");			// 입수완료
			} else {									// 마스킹 안할 때
				params.put("outChkStepCd", "70");			// 암호화대기
			}

			params.put("outChkStepDtl", "");		// 상세내용
			pveMapper.updateOutRqst(params);		// 반출신청정보수정
		}
	}

	

	public String getMaAddedPaths(List<HashMap> egovMapList, String key) throws Exception {
		
		String paths = TvoUtil.getPrprtsValue(egovMapList,key);	// 주경로 가졍오기

		List<HashMap> egovMapList2 = (ArrayList<HashMap>) pveMapper.selectPrprtsListStartWith(key+"_");
		String sub = TvoUtil.getAddedPrprtsValue(egovMapList2,",");		// 보조경로 가져오기
		
		if ( !"".equalsIgnoreCase(sub)) paths = paths+","+sub;

		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	paths = paths.replace("\\", "/");
		} else {							paths = paths.replace("/", "\\");
		}
		logger.info("--> getMaAddedPaths(), {} => {}", key, paths);
		return paths;
	}

	public String getMaAddedInfos(List<HashMap> egovMapList, String key) throws Exception {
		
		String infos = TvoUtil.getPrprtsValue(egovMapList,key);	// 주정보 가졍오기

		List<HashMap> egovMapList2 = (ArrayList<HashMap>) pveMapper.selectPrprtsListStartWith(key+"_");
		String sub = TvoUtil.getAddedPrprtsValue(egovMapList2,",");		// 보조정보 가져오기
		
		if ( !"".equalsIgnoreCase(sub)) infos = infos+","+sub;

		logger.info("--> getMaAddedInfos(), {} => {}", key, infos);
		return infos;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public int dumpCmTcFcltUsed(List<HashMap<String, Object>> listMap) throws Exception {
		if (listMap == null || listMap.isEmpty()) {
			logger.info("==== dumpFcltUsedTy list isEmpty >>>>");
			return 0;
		}
		
    	int cnt = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		cnt = pveMapper.deleteCmTcFcltUsed(map);	// 이전에 받은 정보를 삭제한다.
    	
		for(int i=0;i<listMap.size();i++){
			logger.info("==== dumpFcltUsedTy => {} / {}",i,listMap.size());
			map = (HashMap<String, Object>) listMap.get(i);
			cnt = pveMapper.insertCmTcFcltUsed(map);	// 새로운 정보를 등록한다.
		}
		return cnt;
	}

	@Override
	public int dumpCmFacility(List<HashMap<String, Object>> listMap) throws Exception {
		if (listMap == null || listMap.isEmpty()) {
			logger.info("==== dumpCmFacility list isEmpty >>>>");
			return 0;
		}
		
    	int cnt = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		cnt = pveMapper.deleteCmFacility(map);	// 이전에 받은 정보를 삭제한다.
    	
		for(int i=0;i<listMap.size();i++){
			logger.info("==== dumpCmFacility => {} / {}",i,listMap.size());
			map = (HashMap<String, Object>) listMap.get(i);
			cnt = pveMapper.insertCmFacility(map);	// 새로운 정보를 등록한다.
		}
		return cnt;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public  List <HashMap<String, Object>> selectCctvList() throws Exception {
		return pveMapper.selectCmFacility();
	}

	@Override
	public int updatePointXY(List<HashMap<String, Object>> listMap, String updateYn) throws Exception {
		if (listMap == null || listMap.isEmpty()) {
			logger.info("==== updatePointXY list isEmpty >>>>");
			return 0;
		}

	   	int listCnt = listMap.size();
		logger.info("======================================");
		logger.info("==== 위치/용도 수정 리스트 건수 >>>> {} ",listCnt);
		logger.info("--------------------------------------");
    	int upd_cnt = 0;
    	int r = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(int i=0;i<listCnt;i++){
			map = (HashMap<String, Object>) listMap.get(i);
			if ("Y".equals(updateYn)) {
				r = pveMapper.updatePointXY(map);
	    		upd_cnt = upd_cnt + r;
			} else {
				logger.debug("==== updateYn=N >>>> {}", map.toString());
			}
		}
		return upd_cnt;
	}

	@Override
	public int updatePointAgXY(List<HashMap<String, Object>> listMap, String updateYn) throws Exception {
		if (listMap == null || listMap.isEmpty()) {
			logger.info("==== updatePointAgXY list isEmpty >>>>");
			return 0;
		}
	   	int listCnt = listMap.size();
		logger.info("======================================");
		logger.info("==== 방향각 수정 리스트 건수 >>>> {} ",listCnt);
		logger.info("--------------------------------------");
    	int upd_cnt = 0;
    	int r = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(int i=0;i<listCnt;i++){
			map = (HashMap<String, Object>) listMap.get(i);
			if ("Y".equals(updateYn)) {
				r = pveMapper.updatePointAgXY(map);
	    		upd_cnt = upd_cnt + r;
			} else {
				logger.debug("==== updateYn=N >>>> {}", map.toString());
			}
		}
		return upd_cnt;
	}

	@Override
	public  List<HashMap<String, Object>> selectFcltUsedTyInfo(Map<String, Object> map) throws Exception {
		return pveMapper.selectFcltUsedTyInfo(map);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public int dumpCmFacilityGis(List<HashMap<String, Object>> listMap) throws Exception {
		if (listMap == null || listMap.isEmpty()) {
			logger.info("==== dumpCmFacilityGis list isEmpty >>>>");
			return 0;
		}
		
    	int cnt = 0;
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		cnt = pveMapper.deleteCmFacilityGis(map);	// 이전에 받은 정보를 삭제한다.
    	
		for(int i=0;i<listMap.size();i++){
			logger.info("==== dumpCmFacilityGis => {} / {}",i,listMap.size());
			map = (HashMap<String, Object>) listMap.get(i);
			cnt = pveMapper.insertCmFacilityGis(map);	// 새로운 정보를 등록한다.
		}
		return cnt;
	}

}
