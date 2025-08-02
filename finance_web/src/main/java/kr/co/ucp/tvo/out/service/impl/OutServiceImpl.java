package kr.co.ucp.tvo.out.service.impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.RestUtil;
import kr.co.ucp.cmm.RestUtilMA;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.cmm.util.ConfigUtil;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.tvo.cmn.util.TvoUtil;
import kr.co.ucp.tvo.out.service.OutMapper;
import kr.co.ucp.tvo.out.service.OutService;
import kr.co.ucp.tvo.out.service.OutSrchVO;

@Service("outService")
public class OutServiceImpl implements OutService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "tvoUtil")
    private TvoUtil tvoUtil;

	@Resource(name = "configureService")
	private ConfigureService configureService;

	@Resource(name="outMapper")
	private OutMapper outMapper;

	@Override
	public int selectOutRqstListTotCnt(OutSrchVO vo) throws Exception {
		return outMapper.selectOutRqstListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectOutRqstList(OutSrchVO vo) throws Exception {
		return outMapper.selectOutRqstList(vo);
	}

	@Override
	public int selectOutRqstAprvListTotCnt(OutSrchVO vo) throws Exception {
		return outMapper.selectOutRqstListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectOutRqstAprvList(OutSrchVO vo) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			vo.setSaltText(saltText);
		}		
		
		return outMapper.selectOutRqstList(vo);
	}


	@Override
	public EgovMap selectOutRqstWorkCnt() throws Exception {
		return outMapper.selectOutRqstWorkCnt();
	}

	@Override
	public int selectOutRqstVdoDurationSum() throws Exception {
		int sum = 0;		// 분 합계
		int second = 0;		// 초 합계
		
		List<EgovMap> list = outMapper.selectOutRqstVdoDuration();
		for ( int i=0 ; i<list.size() ; i++ ) {
			EgovMap map = (EgovMap) list.get(i);
			String fcltSysCd = map.get("fcltSysCd").toString();		// VMS구분
			String maskingYn = map.get("maskingYn").toString();
			int vdoDuration = Integer.parseInt(map.get("vdoDuration").toString());
			//logger.info("--> selectOutRqstVdoDurationSum(), {}, {}, {} => {}", fcltSysCd, maskingYn, vdoDuration, sum);
			
			if ( "MTV".equalsIgnoreCase(fcltSysCd)) {		// 마일스톤일 때
				if ( "Y".equalsIgnoreCase(maskingYn)) {		// 마스킹할 때
					second += vdoDuration*60;					// avi변환하므로 영상길이만큼 소요
				} else {
					second += Math.ceil(vdoDuration*60/30);		// 600초 영상입수에 약 20초 이내 소요
				}
				
			} else {
				
				
			}
			//logger.info("--> selectOutRqstVdoDurationSum(), {}, {}, 영상 {} 분 => 누적 {} 초", fcltSysCd, maskingYn, vdoDuration, second);
		}
		sum = (int) Math.ceil(second/60);
		logger.info("--> selectOutRqstVdoDurationSum(), 전체입수소요시간 => {} 분", sum);
		
		return sum;
	}

	
	
	@Override
	public EgovMap selectOutRqstDtl(Map<String, String> params) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			params.put("saltText", saltText);
		}	
		
		return outMapper.selectOutRqstDtl(params);
	}

	@Override
	public Map<String, String> registerOutRqst(Map<String, String> params) throws Exception {
		
//		params.put("outRqstUserId", SessionUtil.getUserId());
//
//		String yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
//		params.put("outRqstYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
//		params.put("ymdhms", yyyyMMddHHmmssSSS.substring(0,14));
//		
//
//		String tvoPrgrsCd = EgovStringUtil.nullConvert(params.get("tvoPrgrsCd")).trim();
//		params.put("tvoPrgrsCd", tvoPrgrsCd);
//		
//		String emrgYn = EgovStringUtil.nullConvert(params.get("emrgYn")).trim();
//		EgovMap tvoConfig = configureService.getTvoConfig();
//		String outAutoAprvYn = EgovStringUtil.nullConvert(tvoConfig.get("outAutoAprvYn"));
//		if ("Y".equals(outAutoAprvYn)) {	// 일괄자동승인 사용일 때
//			params.put("tvoPrgrsCd", "51");
//			params.put("outAprvUserId", "SYSTEM");
//		} else if ( "Y".equalsIgnoreCase(emrgYn)) {	// 긴급일 때
//			if ("U".equals(outAutoAprvYn)) {	// 긴급시 자동승인 사용 (시간제약없음)
//				params.put("tvoPrgrsCd", "51");
//				params.put("outAprvUserId", "SYSTEM");
//			} else if ("T".equals(outAutoAprvYn)) {	// 긴급시 자동승인 사용 (시간제약)
//				if ( tvoUtil.checkProdTime(tvoConfig.get("viewAutoAprvStart").toString(), tvoConfig.get("viewAutoAprvEnd").toString()) ) {	// 현재 시각이 인자로 받은 시간 이내인지 판별
//					params.put("tvoPrgrsCd", "51");
//					params.put("outAprvUserId", "SYSTEM");
//				}
//			}
//		}

		String cctvListStr = EgovStringUtil.nullConvert(params.get("cctvList")).trim();
		Map<String, String> result = new HashMap<String, String>();
		if (!"".equals(cctvListStr)) {
			String[] cctvListArr = cctvListStr.split(Pattern.quote(","));
			int r = 0;

			//params.put("dstrtCd",prprtsService.getString("DSTRT_CD"));

			Map<String, String> pMap = new HashMap<String, String>();
			pMap.put("dstrtCd", params.get("dstrtCd"));
			EgovMap map = outMapper.selectNewOutRqstNo(pMap);
			int newOutRqstNo = Integer.parseInt(map.get("outRqstNo").toString());
			
			for (int i = 0; i < cctvListArr.length; i++) {

				params.put("outRqstNo", params.get("dstrtCd")+"OR"+CommUtil.setPad(String.valueOf(newOutRqstNo), 6, "0", "L"));
				
				newOutRqstNo ++; 

				String[] infos = cctvListArr[i].split(":");				
				params.put("cctvId", infos[0]);
				params.put("vdoYmdhmsFr", infos[1]);
				params.put("vdoYmdhmsTo", infos[2]);

				r += outMapper.insertTvoOutRqst(params);
				
				String errors = "";
				String tvoPrgrsCd = EgovStringUtil.nullConvert(params.get("tvoPrgrsCd")).trim();
				if ( "51".equalsIgnoreCase(tvoPrgrsCd)) {	// 자동입수승인일 때

					String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
					
					String sOsName = System.getProperty("os.name").toLowerCase();
					if (sOsName.indexOf("win") < 0) {	rootPath = rootPath.replace("\\", "/");
					} else {							rootPath = rootPath.replace("/", "\\");
					}

					Map<String, String> resultMap = registerOutFile(params);	// 반출파일정보등록

					String orgVdoAutoRgsYn = prprtsService.getString("ORG_VDO_AUTO_RGS_YN");	// 자동입수 사용여부
					if ("Y".equals(orgVdoAutoRgsYn)) {		// 자동입수를 사용할 때
						logger.info("--> insertTvoOutRqst() 원본영상입수시스템으로 영상요청을 발송한다.");

						String orgVdoSupplier = prprtsService.getString("ORG_VDO_SUPPLIER");
						logger.info("--> requestOrgVdo() orgVdoSupplier : {}", orgVdoSupplier);

						Map<String, Object> responseMap = new HashMap<>();
						if ( "markany".equalsIgnoreCase(orgVdoSupplier) ) {		// 마크애니일 때
							
							//params.put("rootPath", rootPath);
							responseMap = requestMaOrgVdo(params);				// 마크애니 영상입수 요청하기
							
							
						} else if ( "widecube".equalsIgnoreCase(orgVdoSupplier) ) {		// 와이드큐브 영상입수 요청하기
						
						}

						String response_code = "";
						String response_msg = "";
						response_code = responseMap.get("response_code").toString();
						response_msg = responseMap.get("response_msg").toString();
						
						if ("000".equals(response_code)) {
							logger.info("--> insertTvoOutRqst() : 원본영상 요청을 성공하였습니다. <--");
						} else {
							errors = "원본영상 요청을 실패하였습니다!\n\n"+response_code+" : "+response_msg;
							logger.info("--> insertTvoOutRqst() errors : {} <--", errors);
						}
						
					}
				}
				Thread.sleep(10);		// outRqstNo 중복방지용
			}
			result.put("result", String.valueOf(r));
		}
		return result;
	}

	@Override
	public int updateOutRqst(Map<String, String> params) throws Exception {
		int result = outMapper.updateTvoOutRqst(params);
		return result;
	}

	@Override
	public int resetOutRqst(Map<String, String> params) throws Exception {
		//String yyyyMMddHHmmssSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		//params.put("tvoPrgrsYmdhms", yyyyMMddHHmmssSSS.substring(0,14));
		//params.put("outChkYmdhms", "");
		params.put("outAprvUserId", SessionUtil.getUserId());
		
		int result = outMapper.updateTvoOutRqst(params);
		return result;
	}

	@Override
	public int deleteCompleteOutRqst(Map<String, String> params) throws Exception {
		
		// 반출연장신청정보 삭제
		int result = outMapper.deleteTvoOutProdExtn(params);

		// 영상파일 삭제
		OutSrchVO vo = new OutSrchVO();
		vo.setDstrtCd(params.get("dstrtCd"));
		vo.setOutRqstNo(params.get("outRqstNo"));
		vo.setRecordCountPerPage(9999);
		vo.setFirstRecordIndex(0);
		List<EgovMap> outFileList = outMapper.selectOutFileList(vo);
		if ( outFileList.size() != 0 ) {
			logger.debug("--> deleteCompleteOutRqst(), 이전에 저장한 파일을 삭제한다. <--");
			for ( int i=0;i<outFileList.size();i++) {
				removeFile(outFileList.get(i));	// 이전에 저장한 파일을 삭제한다.
			}
		}

		// 반출신청 삭제
		result = outMapper.deleteTvoOutRqst(params);
		
		return result;
	}

	@Override
	public Map<String, String> approveOutRqst(Map<String, String> params) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		int r = outMapper.updateTvoOutRqst(params);
		result.put("result", String.valueOf(r));
		return result;
	}

//	@Override
//	public Map<String, String> modifyOutRqst(Map<String, String> params) throws Exception {
//	//	if (params.containsKey("tvoPrgrsCd")) {
//	//		String tvoPrgrsCd = EgovStringUtil.nullConvert(params.get("tvoPrgrsCd"));
//	//		int n = CommonUtil.comparesTwoNumberStrings("50", tvoPrgrsCd);
//	//		if (n != 0 && n != 2) {
//	//			params.put("playEndYmdhms", "");
//	//		}
//	//	}
//
//		Map<String, String> result = new HashMap<String, String>();
//		int r = outMapper.updateTvoOutRqst(params);
//		result.put("result", String.valueOf(r));
//		return result;
//	}

	@Override
	public Map<String, String> removeOutRqst(Map<String, String> params) throws Exception {
		Map<String, String> result = new HashMap<String, String>();
		int r = outMapper.deleteTvoOutRqst(params);
		if (r > 0) {
			result.put("result", String.valueOf(r));
		} else {
			result.put("result", "0");
		}
		return result;
	}

	@Override
	public int selectOutFileListTotCnt(OutSrchVO vo) throws Exception {
		return outMapper.selectOutFileListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectOutFileList(OutSrchVO vo) throws Exception {
		return outMapper.selectOutFileList(vo);
	}

	@Override
	public List<EgovMap> selectOutFileNeedMp4List() throws Exception {
		return outMapper.selectOutFileNeedMp4List();
	}

	@Override
	public EgovMap selectOutFileDtl(Map<String, String> params) throws Exception {
		return outMapper.selectOutFileDtl(params);
	}

//	@Override
//	public int insertOutFile(Map<String, String> params) throws Exception {
//		return outMapper.insertTvoOutFile(params);
//	}

	@Override
	public int updateOutFile(Map<String, String> params) throws Exception {
		return outMapper.updateTvoOutFile(params);
	}
	
//	@Override
//	public int deleteOutFile(Map<String, String> params) throws Exception {
//		return outMapper.deleteTvoOutFile(params);
//	}

	// 영상파일 삭제
	@Override
	public void removeFile(EgovMap outFileMap) throws Exception {

		//String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		//rootPath = rootPath + prprtsService.getString("FTP_VDO_DIR");
		String DIR_CCTV_CONTENTS = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV_CONTENTS");
		logger.info("--> removeFile(), DIR_CCTV_CONTENTS => "+DIR_CCTV_CONTENTS);
		
		String filePath = DIR_CCTV_CONTENTS + File.separator + outFileMap.get("outFilePath").toString() + File.separator;
		
		String prevCptnFile = filePath + outFileMap.get("cptnFileNm").toString();
		String prevOutFile = filePath + outFileMap.get("outFileNm").toString();
		String prevOutFileMsk = filePath + outFileMap.get("outFileNmMsk").toString();
		String prevOutFileMp4 = filePath + outFileMap.get("outFileNmMp4").toString();
		String prevOutFileDrm = filePath + outFileMap.get("outFileNmDrm").toString();

		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	prevCptnFile = prevCptnFile.replace("\\", "/");
											prevOutFile = prevOutFile.replace("\\", "/");
											prevOutFileMp4 = prevOutFileMp4.replace("\\", "/");
											prevOutFileMsk = prevOutFileMsk.replace("\\", "/");
											prevOutFileDrm = prevOutFileDrm.replace("\\", "/");
		} else {						prevCptnFile = prevCptnFile.replace("/", "\\");
										prevOutFile = prevOutFile.replace("/", "\\");
										prevOutFileMp4 = prevOutFileMp4.replace("/", "\\");
										prevOutFileMsk = prevOutFileMsk.replace("/", "\\");
										prevOutFileDrm = prevOutFileDrm.replace("/", "\\");
		}
		
		logger.debug("--> removeFile() search => {}", prevCptnFile);
		File f = new File(prevCptnFile);
		if( f.exists() ){	if(f.delete()){	logger.debug("--> removeFile() delete => {}", prevCptnFile);	}	}

		logger.debug("--> removeFile() search => {}", prevOutFile);
		f = new File(prevOutFile);
		if( f.exists() ){	if(f.delete()){	logger.debug("--> removeFile() delete => {}", prevOutFile);		}	}
				
		logger.debug("--> removeFile() search => {}", prevOutFileMp4);
		f = new File(prevOutFileMp4);
		if( f.exists() ){	if(f.delete()){	logger.debug("--> removeFile() delete => {}", prevOutFileMp4);	}	}

		logger.debug("--> removeFile() search => {}", prevOutFileMsk);
		f = new File(prevOutFileMsk);
		if( f.exists() ){	if(f.delete()){	logger.debug("--> removeFile() delete => {}", prevOutFileMsk);	}	}
				
		logger.debug("--> removeFile() search => {}", prevOutFileDrm);
		f = new File(prevOutFileDrm);
		if( f.exists() ){	if(f.delete()){	logger.debug("--> removeFile() delete => {}", prevOutFileDrm);	}	}
		
		Map<String, String> para = new HashMap<String, String>();
		para.put("dstrtCd"   , outFileMap.get("dstrtCd").toString());
		para.put("outRqstNo" , outFileMap.get("outRqstNo").toString());
		para.put("outFileSeq", outFileMap.get("outFileSeq").toString());
		outMapper.deleteTvoOutFile(para);
	}

	// 반출파일 테이블에 지정한 시간 단위로 영상을 잘라 적정 개수의 레코드를 등록한다.	
	@Override
	public Map<String, String> registerOutFile(Map<String, String> params) throws Exception {
		
		Map<String, String> para = new HashMap<String, String>();
		para.put("dstrtCd", params.get("dstrtCd").toString());
		para.put("outRqstNo", params.get("outRqstNo").toString());
		outMapper.deleteTvoOutFile(para);
		
		params.put("outAprvUserId", SessionUtil.getUserId());

		params.put("outFilePrgrsCd","20");		// 입수대기

		String outRqstYmdhmsSSS = params.get("outRqstYmdhms").toString();
		String filePath = outRqstYmdhmsSSS.substring(0,4) + "/" + outRqstYmdhmsSSS.substring(4,8);

		EgovMap outRqst = outMapper.selectOutRqstDtl(params);	// 반출신청정보조회
		String vdoYmdhmsFr = outRqst.get("vdoYmdhmsFr").toString();		// 시작시각
		String vdoYmdhmsTo = outRqst.get("vdoYmdhmsTo").toString();		// 종료시각
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		LocalDateTime dtVdoYmdhmsFr = LocalDateTime.parse(vdoYmdhmsFr,dtf);
		LocalDateTime dtVdoYmdhmsTo = LocalDateTime.parse(vdoYmdhmsTo,dtf);
		Duration duration = Duration.between(dtVdoYmdhmsFr, dtVdoYmdhmsTo);
		float durTime = duration.getSeconds() / 60;
		logger.info("--> registerOutFile(), durTime => "+durTime);

		String stdSplitTime = "60";		// 영상을 자르는 기준시간(분)
		float splitTime = Float.parseFloat(stdSplitTime);
		logger.info("--> registerOutFile(), splitTime => "+splitTime);
		
		if ( durTime <= splitTime ) {	// 기준시간 이하일 때
			params.put("outFileSeq","0");
			params.put("outVdoYmdhmsFr",vdoYmdhmsFr);
			params.put("outVdoYmdhmsTo",vdoYmdhmsTo);
			params.put("outFilePath",filePath);
			outMapper.insertTvoOutFile(params);		// 반출파일정보 한건 등록
			
		} else {	// 기준시간 초과일 때
			double loopTime = Math.ceil(durTime / splitTime);
			logger.info("--> registerOutFile(), loopTime => "+loopTime);
			
			LocalDateTime timeFr = dtVdoYmdhmsFr;
			LocalDateTime timeTo = timeFr;
			for ( int i=0 ; i<loopTime ; i++ ) {
				timeTo = timeFr.plusMinutes((int)splitTime);
				if ( dtVdoYmdhmsTo.isBefore(timeTo) ) {
					timeTo = dtVdoYmdhmsTo;
				}
				String sTimeFr = timeFr.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				String sTimeTo = timeTo.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				logger.info("--> registerOutFile(), ("+i+") => "+sTimeFr+" ~ "+sTimeTo);
				
				params.put("outFileSeq",Integer.toString(i));
				params.put("outVdoYmdhmsFr",sTimeFr);
				params.put("outVdoYmdhmsTo",sTimeTo);
				params.put("outFilePath",filePath);
				outMapper.insertTvoOutFile(params);		// 반출파일정보 여러건 등록
				
				timeFr = timeTo;
			}
		}

		Map<String, String> result = new HashMap<String, String>();
		
		return result;
	}
	
	@Override
	public Map<String, String> modifyMovieFile(Map<String, String> params) throws Exception {
		params.put("outAprvUserId", SessionUtil.getUserId());

		Map<String, String> result = new HashMap<String, String>();
		int r = 0;

		if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(params.get("outFileNm")))) {
			r = outMapper.insertTvoOutFile(params);
		}

		if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(params.get("outFileNmMp4")))) {
			r = outMapper.updateTvoOutFile(params);
		}

		if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(params.get("outFileNmMsk")))) {
			r = outMapper.updateTvoOutFile(params);
		}

		if ( !"".equalsIgnoreCase(EgovStringUtil.nullConvert(params.get("outFileNmDrm")))) {
			//r = outAprvMapper.insertOutFileDrm(params);
			r = outMapper.updateTvoOutFile(params);
		}
		
		result.put("result", String.valueOf(r));
		return result;
	}

	@Override
	public Map<String, String> registerOutExtn(Map<String, String> params) throws Exception {
		params.put("outRqstUserId", SessionUtil.getUserId());
		params.put("outExtnRqstUserId", SessionUtil.getUserId());
		Map<String, String> result = new HashMap<String, String>();

		String outExtnAutoAprvYn = prprtsService.getString("OUT_EXTN_AUTO_APRV_YN");

		params.put("tvoPrgrsCd", "10");
		if ("Y".equals(outExtnAutoAprvYn)) {
			params.put("tvoPrgrsCd", "51");
			params.put("outExtnAprvUserId", "SYSTEM");
		}
		
		int r = 0;
		String outRqstNo = EgovStringUtil.nullConvert(params.get("outRqstNo"));
		if (!"".equals(outRqstNo)) {

			String[] outRqstNos = outRqstNo.split(Pattern.quote(","));
			int length = outRqstNos.length;
			if (length == 1) {
				//EgovMap map = outMapper.selectOutRqstDtl(params);
				//String outTvoPrgrsCd = EgovStringUtil.nullConvert(map.get("tvoPrgrsCd"));
				//if ("50".equals(outTvoPrgrsCd) || "51".equals(outTvoPrgrsCd)) {
				//if ("50".equals(outTvoPrgrsCd) || "51".equals(outTvoPrgrsCd) || "70".equals(outTvoPrgrsCd) || "71".equals(outTvoPrgrsCd)) {
					r = outMapper.insertTvoOutProdExtn(params);
				//}
			} else {
				for (int i = 0; i < length; i++) {
					params.put("outRqstNo", outRqstNos[i]);
					//EgovMap map = outMapper.selectOutRqstDtl(params);
					//String outTvoPrgrsCd = EgovStringUtil.nullConvert(map.get("tvoPrgrsCd"));
					//if ("50".equals(outTvoPrgrsCd) || "51".equals(outTvoPrgrsCd)) {
					//if ("50".equals(outTvoPrgrsCd) || "51".equals(outTvoPrgrsCd) || "70".equals(outTvoPrgrsCd) || "71".equals(outTvoPrgrsCd)) {
						r += outMapper.insertTvoOutProdExtn(params);
					//}
				}
			}
		}
		result.put("result", String.valueOf(r));
		return result;
	}

	@Override
	public Map<String, String> approveOutExtn(Map<String, String> params) throws Exception {
		params.put("outExtnAprvUserId", SessionUtil.getUserId());
		
		Map<String, String> result = new HashMap<String, String>();
			
		String tvoPrgrsCd = EgovStringUtil.nullConvert(params.get("tvoPrgrsCd"));
		if ( "30".equalsIgnoreCase(tvoPrgrsCd)) {	// 반려
			params.put("aprvPlayEndYmdhms", "");
		}
		int r = outMapper.updateTvoOutProdExtn(params);
		
		if ( "50".equalsIgnoreCase(tvoPrgrsCd)) {	// 승인
			Map<String, String> outRqst = new HashMap<String, String>();
			outRqst.put("dstrtCd", EgovStringUtil.nullConvert(params.get("dstrtCd")));
			outRqst.put("outRqstNo", EgovStringUtil.nullConvert(params.get("outRqstNo")));
			outRqst.put("playEndYmdhms", EgovStringUtil.nullConvert(params.get("aprvPlayEndYmdhms")));
			outMapper.updateTvoOutRqst(outRqst);
		}
			
		result.put("result", String.valueOf(r));
		return result;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public int selectOutExtnListTotCnt(OutSrchVO vo) throws Exception {
		vo.setOutExtnRqstUserId(SessionUtil.getUserId());
		return outMapper.selectOutExtnListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectOutExtnList(OutSrchVO vo) throws Exception {
		vo.setOutExtnRqstUserId(SessionUtil.getUserId());
		return outMapper.selectOutExtnList(vo);
	}

	@Override
	public int selectOutExtnAprvListTotCnt(OutSrchVO vo) throws Exception {
		return outMapper.selectOutExtnListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectOutExtnAprvList(OutSrchVO vo) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			vo.setSaltText(saltText);
		}		
		
		return outMapper.selectOutExtnList(vo);
	}

	@Override
	public int selectOutExtnHisListTotCnt(OutSrchVO vo) throws Exception {
		vo.setOutExtnRqstUserId(SessionUtil.getUserId());
		return outMapper.selectOutExtnHisListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectOutExtnHisList(OutSrchVO vo) throws Exception {
		vo.setOutExtnRqstUserId(SessionUtil.getUserId());
		return outMapper.selectOutExtnHisList(vo);
	}

	@Override
	public EgovMap selectOutExtnDtl(Map<String, String> params) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			params.put("saltText", saltText);
		}	
		
		return outMapper.selectOutExtnDtl(params);
	}

	@Override
	public int insertOutFileDownloadHis(Map<String, String> params) throws Exception {
		params.put("downloadUserId", SessionUtil.getUserId());
		int r = outMapper.insertOutFileDownloadHis(params);
		return r;
	}

	@Override
	public Map<String, Object> requestMaOrgVdo(Map<String, String> params) throws Exception {

		String responseCd = "000";
		String responseMsg = "success";
		Map<String, Object> responseMap = new HashMap<String, Object>();

		String maRootPaths = getMaOrderRootPaths();	// order.xml 경로는 복수개 가능
		String[] rootPaths = maRootPaths.split(",");
		
		for ( int i=0 ; i<rootPaths.length ; i++ ) {
		//	checkCreateDir(rootPaths[i]);	// 경로존재를 확인하고 없으면 만든다. 네트웍 드라이브에서는 에러 발생한다.
		}
		

		Map<String, String> paramMap = new HashMap<>();
		paramMap.put("dstrtCd", params.get("dstrtCd").toString());
		paramMap.put("outRqstNo", params.get("outRqstNo").toString());
		EgovMap outRqst = outMapper.selectOutRqstDtl(paramMap);
		
		String fcltSysCd = outRqst.get("fcltSysCd").toString();
		logger.info("--> requestMaOrgVdo(), fcltSysCd => {}", fcltSysCd);
		
		
		String recommVdoDuration = prprtsService.getString("RECOMM_VDO_DURATION");		// 권장영상길이
		logger.info("--> requestMaOrgVdo() recommVdoDuration => {}", recommVdoDuration);
		
		OutSrchVO vo = new OutSrchVO();
		vo.setDstrtCd(EgovStringUtil.nullConvert(params.get("dstrtCd")));
		vo.setOutRqstNo(EgovStringUtil.nullConvert(params.get("outRqstNo")));
		vo.setRecordCountPerPage(9999);
		vo.setFirstRecordIndex(0);
		List<EgovMap> outFileList = outMapper.selectOutFileList(vo);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String yyyyMMddHHmmss = df.format(new Date());
		long tempFolderIndex = Long.parseLong(yyyyMMddHHmmss);	// 영상입수용 임시폴더 생성을 위한 인덱스로 사용
		
		// 반출파일별로 영상입수요청을 전송한다.
		for ( int i=0 ; i<outFileList.size() ; i++ ) {
			EgovMap outFileMap = outFileList.get(i);
			logger.info("--> requestMaOrgVdo(), outFileMap => {}", outFileMap.toString());
		
			// 반출영상길이에 따라 order.xml 파일 경로가 달라진다.
			String outVdoDuration = outFileMap.get("outVdoDuration").toString();		// 반출영상길이(분)
			String rootPath = rootPaths[0];
			if ( Integer.parseInt(recommVdoDuration) < Integer.parseInt(outVdoDuration) ) {	// 반출영상길이가 권장길이 초과일 때
				if ( rootPaths.length == 1 ) {			// 경로가 한개 일 때

				} else if ( rootPaths.length == 2 ) {	// 경로가 두개 일 때
					rootPath = rootPaths[1];
				} else {								// 경로가 세개 이상일 때
					logger.info("--> requestMaOrgVdo() 경로 배열의 데이타 개수가 세개 이상입니다!!!");
				}
			}
			String orderPath = rootPath + File.separator + "vms" + File.separator + "order";
			logger.info("--> requestMaOrgVdo() orderPath => {}", orderPath);
			
			try {
				tempFolderIndex ++;	// 임시폴더명은 각각 달라야 한다.
				
				outFileMap.put("fcltSysCd", fcltSysCd);
				outFileMap.put("tempFolderIndex", String.valueOf(tempFolderIndex));

				String strXml = tvoUtil.getOrderXmlText(outFileMap);	// order.xml 파일 내용 만들기
				logger.info("--> requestMaOrgVdo() strXml => {}", strXml);

				String outRqstNo = outFileMap.get("outRqstNo").toString();		// 반출신청번호
				String outFileSeq = outFileMap.get("outFileSeq").toString();	// 반출파일연번
				String orderFileName = "Order_Video_"+outRqstNo+"-"+outFileSeq+".xml";
				logger.info("--> requestMaOrgVdo() orderFile => {}", orderPath+ File.separator + orderFileName);
				
				File orderFile = new File(orderPath+ File.separator + orderFileName);
				
				BufferedWriter writer = new BufferedWriter(new FileWriter(orderFile));
				writer.write(strXml);
				writer.close();
				
				outFileMap.put("outFilePrgrsCd", "25");	// 입수중
				outMapper.updateTvoOutFile(outFileMap);
				
			} catch (Exception e) {
				logger.error("--> requestMaOrgVdo() ERROR Exception >>>> {}", e.getMessage()+" #####");
				responseCd = "333";
				responseMsg	= "Exception";
			} 

		}			
		
		responseMap.put("response_code", responseCd);
		responseMap.put("response_msg",	responseMsg);
		return responseMap;
	}

	public void checkCreateDir(String rootPath) throws Exception {

		String orderPath = rootPath + File.separator + "vms" + File.separator + "order";
		logger.info("--> requestMaOrgVdo(), orderPath => {}", orderPath);
		File dir1 = new File(orderPath);		// 경로 존재 여부
		if (!dir1.exists()) {	Files.createDirectories(Paths.get(orderPath));	}	// order 폴더 생성

		String resultPath = rootPath + File.separator + "vms" + File.separator + "result";
		logger.info("--> requestMaOrgVdo(), resultPath => {}", resultPath);
		File dir3 = new File(resultPath);		// 경로 존재 여부
		if (!dir3.exists()) {	Files.createDirectories(Paths.get(resultPath));	}	// result 폴더 생성

		String logPath = rootPath + File.separator + "vms" + File.separator + "log";
		logger.info("--> requestMaOrgVdo(), logPath => {}", logPath);
		File dir5 = new File(logPath);		// 경로 존재 여부
		if (!dir5.exists()) {	Files.createDirectories(Paths.get(logPath));	}	// log 폴더 생성
	}
	
	
	// 마크애니 원본영상 요청결과 처리
	@Override
	public void actMaResultMap(String resultRootPath, String upperRootPath, Map<String, Object> resultMap) throws Exception {

		String dstrtCd = prprtsService.getString("DSTRT_CD");
		
		String[] reqId = resultMap.get("ReqId").toString().split("-");
		String outRqstNo = reqId[0];
		String outFileSeq = reqId[1];

		Map<String, String> params = new HashMap();
		params.put("dstrtCd", dstrtCd);
		params.put("outRqstNo", outRqstNo);
		params.put("outFileSeq", outFileSeq);

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String yyyyMMddHHmmss = df.format(new Date());
		params.put("outChkYmdhms", yyyyMMddHHmmss);
		params.put("tvoPrgrsYmdhms", yyyyMMddHHmmss);
		
		if ( !"1".equalsIgnoreCase(resultMap.get("Status").toString()) ) {	// 성공이 아닐 때
			String statusString = resultMap.get("StatusString").toString();
			logger.info("--> actMaResultMap(), 영상입수 실패하였음!!!, outRqstNo:"+outRqstNo+", outFileSeq:"+outFileSeq+", statusString:"+statusString+" <--");

			params.put("outFilePrgrsCd", "27");				// 입수실패
			params.put("outFileStepDtl", statusString);		// 상세내용
			outMapper.updateTvoOutFile(params);
			
			params.put("outChkStepCd", "27");				// 입수실패
			params.put("outChkStepDtl", statusString);		// 상세내용
			outMapper.updateTvoOutRqst(params);
			
		} else {	// 성공일 때
			String filePath = resultMap.get("RootPath").toString()+resultMap.get("OrgFilePath").toString();
			
			String tempPath = filePath.substring(0,filePath.length()-1);
			String upperPath = tempPath.substring(0,tempPath.lastIndexOf("/")+1);
					
			String resultFilePath = resultRootPath + filePath;
			String upperFilePath = upperRootPath + upperPath;
			
			String sOsName = System.getProperty("os.name").toLowerCase();
			if (sOsName.indexOf("win") < 0) {	resultFilePath = resultFilePath.replace("\\", "/");
												upperFilePath = upperFilePath.replace("\\", "/");
			} else {							resultFilePath = resultFilePath.replace("/", "\\");
												upperFilePath = upperFilePath.replace("/", "\\");
			}
			logger.info("--> actMaResultMap() resultFilePath => "+resultFilePath);
			logger.info("--> actMaResultMap() upperFilePath  => "+upperFilePath);
			
			EgovMap outRqst = outMapper.selectOutRqstDtl(params);	// 반출신청정보조회
			String maskingYn = outRqst.get("maskingYn").toString();
			
			//String NeedMovTransYn = "";						// 영상변환필요여부
			List<Map<String, Object>> fileMapList = (List<Map<String, Object>>) resultMap.get("File");
			for ( int i=0 ; i<fileMapList.size() ; i++ ) {
				Map<String, Object> fileMap = (Map<String, Object>) fileMapList.get(i);
				//String fileSeq = fileMap.get("OrgFileSeq").toString();
				String fileType = fileMap.get("OrgFileType").toString();
				String fileName = fileMap.get("OrgFileName").toString();
				String fileSize = fileMap.get("OrgFileSize").toString();
				logger.info("--> actMaResultMap(), fileInfo => fileSeq:"+outFileSeq+", "+fileType+", "+fileName+", "+fileSize);
				
				
				if ( fileName.indexOf(outRqstNo) == -1 ) {	// 해당 요청의 파일이 아닐 때 (이전 요청의(이미 이동된) 파일의 정보가 들어가 있기도 함)
					logger.info("--> actMaResultMap() => " + fileName +" 파일은 해당 요청의 파일이 아님. <--");
				}
				else {
					// 파일을 상위 폴더로 이동시킨다.
					String resultMovFullPath = resultFilePath + fileName;
					String upperMovFullPath = upperFilePath + fileName;
					logger.info("--> actMaResultMap(), resultMovFullPath => "+resultMovFullPath);
					logger.info("--> actMaResultMap(), upperMovFullPath  => "+upperMovFullPath);
					
					File resultMovFile = new File(resultMovFullPath);
					File upperMovFile = new File(upperMovFullPath);
					FileUtils.copyFile(resultMovFile, upperMovFile);	// 파일복사
					resultMovFile.delete();								// 파일삭제
					logger.info("--> actMaResultMap() => " + fileName +" 파일을 상위 폴더로 이동하였음. <--");
					
					
					params.put("outFileSeq", outFileSeq);
					
					if ( fileType.indexOf("smi") != -1 ) {		// 자막파일일 때
						params.put("cptnFileNm", fileName);			// 자막파일명
						params.put("cptnFileSize", fileSize);
						params.put("outFileStepDtl", "");			// 상세내용
						outMapper.updateTvoOutFile(params);			// 반출파일정보수정
						
					} else {											// 자막파일아닐 때(원본파일일 때)
						if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
							params.put("outFilePrgrsCd", "40");			// 입수완료
						} else {									// 마스킹 안할 때
							params.put("outFilePrgrsCd", "70");			// 암호화대기
						}
						params.put("outFileNm", fileName);		// 원본파일명
						params.put("outFileSize", fileSize);
						params.put("outFileStepDtl", "");		// 상세내용
						outMapper.updateTvoOutFile(params);		// 반출파일정보수정
					}
					
				}
				
				
			}

			//if ( "Y".equalsIgnoreCase(NeedMovTransYn)) {	// 영상변환 필요할 때
				//params.put("outChkStepCd", "25");				// 입수중
			//} else {										// 영상변환 필요없을 때(mp4일 때)
				if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
					params.put("outChkStepCd", "40");			// 입수완료
				} else {									// 마스킹 안할 때
					params.put("outChkStepCd", "70");			// 암호화대기
				}
			//}
			params.put("outChkStepDtl", "");		// 상세내용
			outMapper.updateTvoOutRqst(params);		// 반출신청정보수정
		}
	}

	@Override
	public Map<String, String> sendRequestEncrypt(Map<String, String> params) throws Exception {

		//String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		//rootPath = rootPath + prprtsService.getString("FTP_VDO_DIR");
		String DIR_CCTV_CONTENTS = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV_CONTENTS");
		
		logger.info("--> sendRequestEncrypt() DIR_CCTV_CONTENTS => "+DIR_CCTV_CONTENTS);

		String playCnt = params.get("playCnt").toString();
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
			caption = tvoUtil.getBase64Content(cptnFilePath);
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

		EgovMap tvoOutRqst = outMapper.selectOutRqstDtl(params);
		
		params.put("thirdPartyPw",tvoOutRqst.get("thirdPartyPw").toString());
		params.put("playStartYmdhms",tvoOutRqst.get("playStartYmdhms").toString());
		params.put("playEndYmdhms",tvoOutRqst.get("playEndYmdhms").toString());
		params.put("outRqstUserId",tvoOutRqst.get("outRqstUserId").toString());
		
		params.put("userId",tvoOutRqst.get("outRqstUserId").toString());	// 신청자 정보 조회용
		EgovMap userInfo = outMapper.selectUserInfo(params);
		
		String playPwd = userInfo.get("playPwd").toString();
		//logger.debug("--> requestMaEncrypt(), playPwd : {}", playPwd);
		playPwd = new String(Base64.decodeBase64(playPwd.getBytes()));
		logger.debug("--> sendRequestEncrypt(), playPwd : {}", playPwd);
		params.put("playPwd",playPwd);
		
		
		Map<String, Object> responseMap = new HashMap<String, Object>();

		String encModuleSupplier = prprtsService.getString("ENC_MODULE_SUPPLIER");
		logger.info("--> sendRequestEncrypt() encModuleSupplier => {}", encModuleSupplier);
		
		if ( "markany".equalsIgnoreCase(encModuleSupplier) ) {	// 마크애니 암호화 요청하기
			
			responseMap = requestMaEncrypt(params);
			
			if ( "200".equalsIgnoreCase(responseMap.get("result").toString())) {
				params.put("outFileSeq", fileSeq);
				params.put("outFileNmDrm", drmFileNm);
				params.put("drmPcnt", responseMap.get("percent").toString());		// 암호화퍼센트
				params.put("outFilePrgrsCd", "75");	// 암호화중
				
				params.put("outChkStepCd", "75");	// 암호화중
			} else {
				params.put("outFilePrgrsCd", "77");	// 암호화실패
				
				params.put("outChkStepCd", "77");	// 암호화실패
				logger.debug("--> requestEncrypt(), contentEncrypt : 암호화 실패 !!!");
			}
			outMapper.updateTvoOutFile(params);
			
			outMapper.updateTvoOutRqst(params);
			
		} else if ( "widecube".equalsIgnoreCase(encModuleSupplier) ) {		// 와이드큐브 암호화 요청하기
			
			
			
		}
		
		return params;
	}
	

	// 마크애니 영상암호화 요청
	@Override
	public Map<String, Object> requestMaEncrypt(Map<String, String> params) throws Exception {

		String tmp = params.get("objFileFullPath").toString();
		String objFileExt = tmp.substring(tmp.lastIndexOf('.')+1).toUpperCase();
		
		Map<String, Object> responseMap = new HashMap<String, Object>();

		String keyUrl = prprtsService.getString("VDO_ENC_URL") + "/api/v3.0/generateCek/512";	// 마크애니 cek 생성 함수
		String encUrl = prprtsService.getString("VDO_ENC_URL") + "/api/v3.0/contentEncrypt";	// 마크애니 암호화 함수
		logger.info("--> requestMaEncrypt(), keyUrl => {}", keyUrl);
		logger.info("--> requestMaEncrypt(), encUrl => {}", encUrl);

        String tvoUrl = prprtsService.getString("PVE_URL");
		//logger.info("--> requestMaEncrypt(), tvoUrl => {}", tvoUrl);
		String staUrl = tvoUrl + "/api/encryptStatus.xx";		// 암호화 진행률 회신받을 함수
		logger.info("--> requestMaEncrypt(), staUrl => {}", staUrl);
		
		responseMap = RestUtilMA.getHttp(keyUrl, 0, "");
		logger.debug("--> requestMaEncrypt(), generateCek >>>> responseMap : {}", responseMap.toString());

		if ( responseMap.get("result") == null ) {
			logger.debug("--> requestMaEncrypt(), generateCek >>>> result is null !!! ====");
			
		} else {
			String result = responseMap.get("result").toString();
			if ( "200".equalsIgnoreCase(result)) {
				Map<String, Object> dataMap = new HashMap<>();
				dataMap.put("cek", responseMap.get("cek").toString());
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				dataMap.put("cid", df.format(new Date()));
				dataMap.put("job_code", params.get("outRqstNo").toString()+":"+params.get("fileSeq").toString());
				dataMap.put("src", params.get("objFileFullPath").toString());
				dataMap.put("ext", objFileExt);	
				dataMap.put("dst", params.get("drmFileFullPath").toString());	
				dataMap.put("caption", params.get("caption").toString());	
				//dataMap.put("caption_char", "utf8");
				dataMap.put("caption_charset", "utf8");
				dataMap.put("user", params.get("outRqstUserId").toString());
				dataMap.put("pwd", params.get("playPwd").toString());
				dataMap.put("export_pwd", params.get("thirdPartyPw").toString());
				dataMap.put("begin", params.get("playStartYmdhms").toString());
				dataMap.put("end", params.get("playEndYmdhms").toString());
				dataMap.put("count", params.get("playCnt").toString());
				dataMap.put("imgsafer", true);
				dataMap.put("interface", staUrl);
				dataMap.put("enc_type", "1");
				logger.debug("--> requestMaEncrypt(), contentEncrypt >>>> dataMap : {}", dataMap);
				
				responseMap = RestUtilMA.postHttp(encUrl, dataMap, 0, "");
				logger.debug("--> requestMaEncrypt(), contentEncrypt : {}", responseMap.toString());
				result = responseMap.get("result").toString();
				responseMap.put("result", result);
				
			} else {
				responseMap.put("result", result);
			}
		}
				
		return responseMap;
	}

	
	
	// mp4 파일로 변환
	@Override
	public void transOrgVdoFile(EgovMap outFileMap) throws Exception {
		
		String FFMPEG_FILE_FULL_PATH = prprtsService.getString("FFMPEG_FILE_FULL_PATH");
		
		//String rootPath = outFileMap.get("rootPath").toString();
		//rootPath = rootPath + prprtsService.getString("FTP_VDO_DIR");
		String DIR_CCTV_CONTENTS = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV_CONTENTS");
				
		String filePath = DIR_CCTV_CONTENTS + File.separator + outFileMap.get("outFilePath").toString();
		
		String outFileNm = outFileMap.get("outFileNm").toString();
		String prevOutFile = filePath + File.separator + outFileNm;
		
		String mp4FileName = outFileNm.substring(0,outFileNm.lastIndexOf('.')+1)+"mp4";
		//String mp4FileName = outFileMap.get("cctvId").toString()+"_"+outFileMap.get("outVdoYmdhmsFr").toString()+"_"+outFileMap.get("outVdoYmdhmsTo").toString()+".mp4";
		//String mp4FileName = outFileMap.get("outRqstNo").toString()+"-"+outFileMap.get("outFileSeq").toString()
		//		+"_"+outFileMap.get("outVdoYmdhmsFr").toString().substring(0,12)+"_"+outFileMap.get("outVdoYmdhmsTo").toString().substring(0,12)+".mp4";
		String nextOutFile = filePath + File.separator + mp4FileName;

		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	prevOutFile = prevOutFile.replace("\\", "/");
											FFMPEG_FILE_FULL_PATH = FFMPEG_FILE_FULL_PATH.replace("\\", "/");
		} else {							prevOutFile = prevOutFile.replace("/", "\\");
											FFMPEG_FILE_FULL_PATH = FFMPEG_FILE_FULL_PATH.replace("/", "\\");
		}
		logger.debug("--> transOrgVdoFile() prev => {}", prevOutFile);
		logger.debug("--> transOrgVdoFile() next => {}", nextOutFile);
		
		File f = new File(prevOutFile);
		if( !f.exists() ){
			logger.info("--> transOrgVdoFile() 원본파일이 없습니다!!! <--");
		} else {
			
			f = new File(nextOutFile);
			if( f.exists() ){
				logger.debug("--> transOrgVdoFile() 변환파일 삭제 => {}",nextOutFile);
				f.delete();		// 새로 만들기 위해 삭제한다.
			}
			logger.info("--> transOrgVdoFile() mp4로 변환을 시작합니다. <--");
			

			Map<String, String> params = new HashMap<String, String>();
			params.put("outRqstNo", outFileMap.get("outRqstNo").toString());
			params.put("outFileSeq", outFileMap.get("outFileSeq").toString());
			//params.put("outFilePrgrsCd", "35");			// 영상변환중
			//outMapper.updateTvoOutFile(params);	
			
			
			
			

			String option = "-i";
			//String option2 = "-c:v libx264 -c:a aac -strict experimental";
			String option2 = "";
			String ffmpegCommand = FFMPEG_FILE_FULL_PATH+" "+option+" "+prevOutFile+" "+option2+" "+nextOutFile;
			logger.debug("--> ffmpeg Command => {}",ffmpegCommand);

			Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(ffmpegCommand);

    		String line1 = null;
    		BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
    		while ((line1 = error.readLine()) != null) {	logger.info(line1);	}	// 출력해야만 진행이 된다.

    		process.waitFor();

    		int exitValue = process.exitValue();
    		if (exitValue != 0) {
    			throw new RuntimeException("--> ffmpeg 비정상종료입니다 !!! => " + exitValue + " <--");
    		}

			process.destroy();
			runtime = null;

			if (exitValue == 0) {	// 정상적으로 완료했을 때
				//EgovMap outRqst = outMapper.selectOutRqstDtl(params);	// 반출신청정보조회
				//String maskingYn = outRqst.get("maskingYn").toString();
				//if ( "Y".equalsIgnoreCase(maskingYn) ) {	// 마스킹 할 때
					//params.put("outFilePrgrsCd", "40");				// 입수완료
				//} else {									// 마스킹 안할 때
					//params.put("outFilePrgrsCd", "70");				// 암호화대기
				//}
				params.put("outFileNmMp4", mp4FileName);
				
			} else {
				params.put("outFilePrgrsCd", "37");			// 영상변환실패
				
			}
			outMapper.updateTvoOutFile(params);	
			
		}
	}

	public void deleteExpiredVdo() throws Exception {

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String yyyyMMddHHmmss = df.format(new Date());
		String expireYmdhms = yyyyMMddHHmmss;	// 삭제기준일시

		String fileKeepDuration = prprtsService.getString("FILE_KEEP_DURATION");	// 파일보관일수
		int dayCnt = Integer.parseInt(fileKeepDuration);
		if ( 0 < dayCnt ) {
			Calendar cal = Calendar.getInstance();
			Date dt = df.parse(yyyyMMddHHmmss);
	        cal.setTime(dt);
			cal.add(Calendar.DATE, -dayCnt);
			expireYmdhms = df.format(cal.getTime());
		}
		logger.info("--> deleteExpiredVdo(), expireYmdhms => "+expireYmdhms);
		
		OutSrchVO outVo = new OutSrchVO();
		//outVo.setExpireCompFlag("PLAY_START_YMDHMS");	// 재생시작일자를 삭제기준으로
		outVo.setExpireCompFlag("PLAY_END_YMDHMS");	// 재생종료일자를 삭제기준으로
		outVo.setExpireYmdhms(expireYmdhms);
		outVo.setOutFileDelYn("N");
		outVo.setRecordCountPerPage(9999);
		outVo.setFirstRecordIndex(0);
		List<EgovMap> outRqstList = outMapper.selectOutRqstList(outVo);
		if ( outRqstList.size() == 0 ) {
			logger.info("--> deleteExpiredVdo() 삭제대상 파일정보 없음 <--");
			return;
		}
		
		//String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		//rootPath = rootPath + prprtsService.getString("FTP_VDO_DIR");
		String DIR_CCTV_CONTENTS = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV_CONTENTS");
		logger.info("--> deleteExpiredVdo(), DIR_CCTV_CONTENTS => "+DIR_CCTV_CONTENTS);

		for ( int i=0 ; i<outRqstList.size() ; i++ ) {
			EgovMap outRqst = outRqstList.get(i);
			String dstrtCd = outRqst.get("dstrtCd").toString();
			String outRqstNo = outRqst.get("outRqstNo").toString();
			String playEndYmdhms = outRqst.get("playEndYmdhms").toString();
			logger.info("--> deleteExpiredVdo() outRqst({}) => {}, {}", i, outRqstNo, playEndYmdhms);

			OutSrchVO vo = new OutSrchVO();
			vo.setDstrtCd(dstrtCd);
			vo.setOutRqstNo(outRqstNo);
			vo.setRecordCountPerPage(9999);
			vo.setFirstRecordIndex(0);
			List<EgovMap> outFileList = outMapper.selectOutFileList(vo);
			
			for ( int j=0 ; j<outFileList.size() ; j++ ) {
				EgovMap outFileMap = outFileList.get(j);
				String outFilePath = outFileMap.get("outFilePath").toString();
				String FileFullPath = DIR_CCTV_CONTENTS + File.separator + outFilePath;
				
				String fileNms = "";
				if ( !"".equalsIgnoreCase(outFileMap.get("cptnFileNm").toString()) ) {
					fileNms = fileNms+","+outFileMap.get("cptnFileNm").toString();
				}
				if ( !"".equalsIgnoreCase(outFileMap.get("outFileNm").toString()) ) {
					fileNms = fileNms+","+outFileMap.get("outFileNm").toString();
				}
				if ( !"".equalsIgnoreCase(outFileMap.get("outFileNmMp4").toString()) ) {
					fileNms = fileNms+","+outFileMap.get("outFileNmMp4").toString();
				}
				if ( !"".equalsIgnoreCase(outFileMap.get("outFileNmMsk").toString()) ) {
					fileNms = fileNms+","+outFileMap.get("outFileNmMsk").toString();
				}
				if ( !"".equalsIgnoreCase(outFileMap.get("outFileNmDrm").toString()) ) {
					fileNms = fileNms+","+outFileMap.get("outFileNmDrm").toString();
				}
				fileNms = fileNms.substring(1);
				logger.info("--> deleteExpiredVdo() outFileMap({},{}) fileNms   => {}", i, j, fileNms);
				
				// 파일삭제
				String[] fileNmArr = fileNms.split(",");
				for ( int k=0 ; k<fileNmArr.length ; k++ ) {
					String fileFullPath = FileFullPath + File.separator + fileNmArr[k];
					String sOsName = System.getProperty("os.name").toLowerCase();
					if (sOsName.indexOf("win") < 0) {	fileFullPath = fileFullPath.replace("\\", "/");
					} else {							fileFullPath = fileFullPath.replace("/", "\\");
					}
					logger.info("--> deleteExpiredVdo() outFileMap({},{},{}) fileFullPath   => {}", i, j, k, fileFullPath);
					
					// 파일을 삭제한다.
					File f = new File(fileFullPath);
					if( f.exists() ){
						f.delete();		// 파일을 삭제한다.
						logger.info("--> deleteExpiredVdo() 파일삭제 => {}",fileFullPath);
					} else {
						logger.info("--> deleteExpiredVdo() 파일없음 => {}",fileFullPath);
					}	
				}

				// 파일삭제일시를 기록한다.
				Map<String, String> params = new HashMap<String, String>();
				params.put("dstrtCd", outFileMap.get("dstrtCd").toString());
				params.put("outRqstNo", outFileMap.get("outRqstNo").toString());
				params.put("outFileSeq", outFileMap.get("outFileSeq").toString());
				params.put("outFileDelYmdhms", yyyyMMddHHmmss);			// 삭제일시
				outMapper.updateTvoOutFile(params);	
				
			}
			
			// 파일삭제여부를 Y로 변경한다.
			Map<String, String> params2 = new HashMap<String, String>();
			params2.put("dstrtCd", EgovStringUtil.nullConvert(outRqst.get("dstrtCd")));
			params2.put("outRqstNo", EgovStringUtil.nullConvert(outRqst.get("outRqstNo")));
			params2.put("outFileDelYn", "Y");
			outMapper.updateTvoOutRqst(params2);
			
		}
	}
	
	
	

	@Override
	public String getMaOrderRootPaths() throws Exception {

		String paths = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		
		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	paths = paths.replace("\\", "/");
		} else {							paths = paths.replace("/", "\\");
		}

		String dataHome2 = EgovStringUtil.nullConvert(prprtsService.getString("DIR_WRKS_HOME_2"));
		if ( !"".equalsIgnoreCase(dataHome2) ) {	// dataHome2 를 지정했을 때
			String rootPath2 = prprtsService.getString("DIR_WRKS_HOME_2") + prprtsService.getString("DIR_CCTV");
			if (sOsName.toLowerCase().indexOf("win") < 0) {	rootPath2 = rootPath2.replace("\\", "/");
			} else {										rootPath2 = rootPath2.replace("/", "\\");
			}
			paths = paths+","+rootPath2;	
		}
		
		return paths;
	}
	
	
	
	
	
	
	
}
