package kr.co.ucp.tvo.view.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.ConfigureService;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.tvo.view.service.ViewMapper;
import kr.co.ucp.tvo.view.service.ViewRqstVO;
import kr.co.ucp.tvo.view.service.ViewService;
import kr.co.ucp.tvo.view.service.ViewSrchVO;

@Service("viewService")
public class ViewServiceImpl implements ViewService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

    @Resource(name = "configureService")
    private ConfigureService configureService;

	@Resource(name="viewMapper")
	private ViewMapper viewMapper;

	@Override
	public List<EgovMap> selectViewRqstList(ViewSrchVO vo) throws Exception {
		return viewMapper.selectViewRqstList(vo);
	}

	@Override
	public int selectViewRqstListTotCnt(ViewSrchVO vo) throws Exception {
		return viewMapper.selectViewRqstListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectViewRqstAprvList(ViewSrchVO vo) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			vo.setSaltText(saltText);
		}		
		
		return viewMapper.selectViewRqstList(vo);
	}

	@Override
	public int selectViewRqstAprvListTotCnt(ViewSrchVO vo) throws Exception {
		return viewMapper.selectViewRqstListTotCnt(vo);
	}

	
	
	@Override
	public EgovMap selectViewRqstDtl(Map<String, String> params) throws Exception {
		
		String outFilePlayProd = prprtsService.getString("OUT_FILE_PLAY_PROD");				// 기본재생기간
		String outFilePlayProdThird = prprtsService.getString("OUT_FILE_PLAY_PROD_THIRD");	// 3자재생기간

		params.put("outFilePlayProd", outFilePlayProd+" day");
		params.put("outFilePlayProdThird", outFilePlayProdThird+" day");
		
		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			params.put("saltText", saltText);
		}	
		
		return viewMapper.selectViewRqstDtl(params);
	}

	@Override
	public EgovMap selectNewViewRqstNo(Map<String, String> params) throws Exception {
		return viewMapper.selectNewViewRqstNo(params);
	}
	
	@Override
	public int registerViewRqst(ViewRqstVO vo) throws Exception {

		String viewAutoAprvYn = prprtsService.getString("VIEW_AUTO_APRV_YN");

		//SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		//String yyyyMMddHHmmss = df.format(new Date());

		vo.setViewRqstUserId(SessionUtil.getUserId());
		vo.setRqstSysCd(SessionUtil.getUserInfo().getSysCd());

		String tvoPrgrsCd = EgovStringUtil.nullConvert(vo.getTvoPrgrsCd());
		if ("".equals(tvoPrgrsCd)) {
			vo.setTvoPrgrsCd("10");			// 신청
		}

		if ("Y".equals(viewAutoAprvYn)) {	// 일괄자동승인
			vo.setTvoPrgrsCd("51");
			vo.setViewAprvUserId("SYSTEM");
			vo.setTvoPrgrsYmdhms(vo.getViewRqstYmdhms());
			vo.setViewEndYmdhms(vo.getViewEndYmdhmsWant());
			
		} else if ( "Y".equalsIgnoreCase(vo.getEmrgYn())) {	// 긴급일 때
			if ("U".equals(viewAutoAprvYn)) {	// 긴급시 자동승인 사용 (시간제약없음)
				vo.setTvoPrgrsCd("51");
				vo.setViewAprvUserId("SYSTEM");
				vo.setTvoPrgrsYmdhms(vo.getViewRqstYmdhms());
				vo.setViewEndYmdhms(vo.getViewEndYmdhmsWant());
				
			} else if ("T".equals(viewAutoAprvYn)) {	// 긴급시 자동승인 사용 (시간제약)
				if ( checkProdTime(prprtsService.getString("VIEW_AUTO_APRV_START"), prprtsService.getString("VIEW_AUTO_APRV_END")) ) {	// 현재 시각이 인자로 받은 시간 이내인지 판별
					vo.setTvoPrgrsCd("51");
					vo.setViewAprvUserId("SYSTEM");
					vo.setTvoPrgrsYmdhms(vo.getViewRqstYmdhms());
					vo.setViewEndYmdhms(vo.getViewEndYmdhmsWant());
				}
			}
		}

		// 10-열람후반출신청, 20-열람없이반출신청
		String rqstTyCd = EgovStringUtil.nullConvert(vo.getRqstTyCd());
		if ("".equals(rqstTyCd)) {
			vo.setRqstTyCd("10");
		}

		String rqstRsnDtl = EgovStringUtil.nullConvert(vo.getRqstRsnDtl());
		if (!"".equals(rqstRsnDtl)) {
			vo.setRqstRsnDtl(EgovStringUtil.checkHtmlView(rqstRsnDtl));
		}

		return viewMapper.insertTvoViewRqst(vo);
	}
	
	// 현재 시각이 인자로 받은 시간 이내인지 판별한다.
	public boolean checkProdTime(String st, String en) {
		boolean rtn = false;

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String yyyyMMddHHmmss = df.format(new Date());
		int now = Integer.parseInt(yyyyMMddHHmmss.substring(8,10));
		int start = Integer.parseInt(st);
		int end = Integer.parseInt(en);
		
		if ( start < end ) {
			if ( start<=now && now<end ) {
				rtn = true;
			}
		} else if ( start > end ) {
			if ( start>now && now>=end ) {
			} else {
				rtn = true;
			}
		}
		return rtn;
	}

	@Override
	public int updateViewRqst(ViewRqstVO vo) throws Exception {
		vo.setViewRqstUserId(SessionUtil.getUserId());
		
		String rqstRsnDtl = EgovStringUtil.nullConvert(vo.getRqstRsnDtl());
		if ("".equals(rqstRsnDtl)) {
			vo.setRqstRsnDtl(EgovStringUtil.checkHtmlView(rqstRsnDtl));
		}
		int result = viewMapper.updateTvoViewRqst(vo);
		return result;
	}

	@Override
	public int modifyViewRqst(Map<String, String> params) throws Exception {
		
		if (params.containsKey("tvoPrgrsCd")) {
			
			String viewResultTyCd = EgovStringUtil.nullConvert(params.get("viewResultTyCd"));
			
			if ( !"".equalsIgnoreCase(viewResultTyCd) ) {		// 열람활용결과일 때
				
				
				
				
			} else {
				String tvoPrgrsCd = EgovStringUtil.nullConvert(params.get("tvoPrgrsCd"));

				if ( "10".equalsIgnoreCase(tvoPrgrsCd) ) {	// 신청일 때
					params.put("viewRqstUserId", SessionUtil.getUserId());
					
				} else if ( "30".equalsIgnoreCase(tvoPrgrsCd) || "50".equalsIgnoreCase(tvoPrgrsCd) ) {	// 승인 또는 반려일 때
					params.put("viewAprvUserId", SessionUtil.getUserId());
					if ( "30".equalsIgnoreCase(tvoPrgrsCd) ) {	// 반려일 때
						params.put("viewEndYmdhms", "");
					}
				}
			}
			
		}

		int result = viewMapper.updateTvoViewRqst(params);
		return result;
	}

	@Override
	public int resetViewRqst(Map<String, String> params) throws Exception {
		//params.put("viewAprvUserId", SessionUtil.getUserId());
		
		int result = viewMapper.updateTvoViewRqst(params);
		return result;
	}

	@Override
	public int deleteCompleteViewRqst(Map<String, String> params) throws Exception {

		// 열람연장신청정보 삭제
		int result = viewMapper.deleteTvoViewProdExtn(params);

		// 공문파일 삭제
		EgovMap rqstDtl = viewMapper.selectViewRqstDtl(params);
		String viewRqstYmdhms = EgovStringUtil.nullConvert(rqstDtl.get("viewRqstYmdhms"));
		String paperFileNm = EgovStringUtil.nullConvert(rqstDtl.get("paperFileNm"));
		if ( !"".equalsIgnoreCase(paperFileNm)) {
			String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_DOC");
			String filePath = viewRqstYmdhms.substring(0,4) + File.separator + viewRqstYmdhms.substring(4,8) + File.separator + paperFileNm;
			boolean isDeleted = Files.deleteIfExists(Paths.get(rootPath + File.separator + filePath));
			if (isDeleted) logger.info("--> deleteCompleteViewRqst: {}", rootPath + File.separator + filePath);
		}

		// 열람신청 삭제
		ViewRqstVO vo = new ViewRqstVO();
		vo.setDstrtCd(params.get("dstrtCd"));
		vo.setViewRqstNo(params.get("viewRqstNo"));
		result = viewMapper.deleteTvoViewRqst(vo);
		
		return result;
	}

	@Override
	public int approveViewRqst(Map<String, String> params) throws Exception {
		params.put("viewAprvUserId", SessionUtil.getUserId());
		
		int result = 0;
		String paraTvoPrgrsCd = EgovStringUtil.nullConvert(params.get("tvoPrgrsCd"));
		String paraViewEndYmdhms = EgovStringUtil.nullConvert(params.get("viewEndYmdhms"));
		
		String viewRqstNo = EgovStringUtil.nullConvert(params.get("viewRqstNo"));
		String[] rqstNos = viewRqstNo.split(",");
		for ( int i=0 ; i<rqstNos.length ; i++ ) {
			params.put("viewRqstNo", rqstNos[i]);

			if ( "30".equalsIgnoreCase(paraTvoPrgrsCd) ) {	// 반려일 때
				params.put("viewEndYmdhms", "");
				
			} else if ( "50".equalsIgnoreCase(paraTvoPrgrsCd) ) {	// 승인일 때
				if ( "".equalsIgnoreCase(paraViewEndYmdhms) ) {		// 승인된 열람종료일자가 없을 때(다중승인일 때)
					// 신청한 열람종료일자을 승인한 열람종료일자로 넣는다.
					EgovMap rqstDtl = viewMapper.selectViewRqstDtl(params);
					params.put("viewEndYmdhms", rqstDtl.get("viewEndYmdhmsWant").toString());
				}
			}
			
			result = viewMapper.updateTvoViewRqst(params);
		}
		
		return result;
	}

	@Override
	public int removeViewRqst(ViewRqstVO vo) throws Exception {
		//vo.setViewRqstUserId(SessionUtil.getUserId());

		// 공문파일 삭제
		Map<String, String> params = new HashMap<String, String>();
		params.put("dstrtCd", vo.getDstrtCd());
		params.put("viewRqstNo", vo.getViewRqstNo());
		EgovMap rqstDtl = viewMapper.selectViewRqstDtl(params);
		String viewRqstYmdhms = EgovStringUtil.nullConvert(rqstDtl.get("viewRqstYmdhms"));
		String paperFileNm = EgovStringUtil.nullConvert(rqstDtl.get("paperFileNm"));
		if ( !"".equalsIgnoreCase(paperFileNm)) {
			String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_DOC");
			String filePath = viewRqstYmdhms.substring(0,4) + File.separator + viewRqstYmdhms.substring(4,8) + File.separator + paperFileNm;
			boolean isDeleted = Files.deleteIfExists(Paths.get(rootPath + File.separator + filePath));
			if (isDeleted) logger.info("--> deleteCompleteViewRqst: {}", rootPath + File.separator + filePath);
		}

		// 열람신청 삭제
		int result = viewMapper.deleteTvoViewRqst(vo);
		
		return result;
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public int selectViewExtnHisListTotCnt(ViewSrchVO vo) throws Exception {
		return viewMapper.selectViewExtnHisListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectViewExtnHisList(ViewSrchVO vo) throws Exception {
		return viewMapper.selectViewExtnHisList(vo);
	}

	@Override
	public int selectViewExtnListTotCnt(ViewSrchVO vo) throws Exception {
		vo.setViewExtnRqstUserId(SessionUtil.getUserId());
		return viewMapper.selectViewExtnListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectViewExtnList(ViewSrchVO vo) throws Exception {
		vo.setViewExtnRqstUserId(SessionUtil.getUserId());
		return viewMapper.selectViewExtnList(vo);
	}

	@Override
	public int selectViewExtnAprvListTotCnt(ViewSrchVO vo) throws Exception {
		return viewMapper.selectViewExtnListTotCnt(vo);
	}

	@Override
	public List<EgovMap> selectViewExtnAprvList(ViewSrchVO vo) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			vo.setSaltText(saltText);
		}		
		
		return viewMapper.selectViewExtnList(vo);
	}

	@Override
	public EgovMap selectViewExtnDtl(Map<String, String> params) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			params.put("saltText", saltText);
		}		
		
		return viewMapper.selectViewExtnDtl(params);
	}

	@Override
	public Map<String, String> registerViewExtn(Map<String, String> params) throws Exception {
		params.put("viewExtnRqstUserId", SessionUtil.getUserId());

		String viewExtnAutoAprvYn = prprtsService.getString("VIEW_EXTN_AUTO_APRV_YN");

		if (!params.containsKey("tvoPrgrsCd")) {
			params.put("tvoPrgrsCd", "10");
		}

		if ("Y".equals(viewExtnAutoAprvYn)) {
			params.put("tvoPrgrsCd", "51");
			params.put("viewExtnAprvUserId", "SYSTEM");
		}

		Map<String, String> result = new HashMap<String, String>();
		int r = viewMapper.insertTvoViewProdExtn(params);
		if (r > 0) {
			result.put("result", String.valueOf(r));
		} else {
			result.put("result", "0");
		}
		return result;
	}

	@Override
	public Map<String, String> approveViewExtn(Map<String, String> params) throws Exception {
		params.put("viewExtnAprvUserId", SessionUtil.getUserId());
		
		Map<String, String> result = new HashMap<String, String>();

		if (params.containsKey("tvoPrgrsCd")) {
			String tvoPrgrsCd = EgovStringUtil.nullConvert(params.get("tvoPrgrsCd"));
			int n = CommonUtil.comparesTwoNumberStrings("50", tvoPrgrsCd);
			if (n != 0 && n != 2) {
				params.put("aprvViewEndYmdhms", "");
			}

			int r = viewMapper.updateTvoViewProdExtn(params);

			if (n == 0 || n == 2) {
				//String viewRqstNo = EgovStringUtil.nullConvert(params.get("viewRqstNo"));
				//String viewEndYmdhms = EgovStringUtil.nullConvert(params.get("aprvViewEndYmdhms"));
				Map<String, String> viewRqst = new HashMap<String, String>();
				viewRqst.put("dstrtCd", EgovStringUtil.nullConvert(params.get("dstrtCd")));
				viewRqst.put("viewRqstNo", EgovStringUtil.nullConvert(params.get("viewRqstNo")));
				viewRqst.put("viewEndYmdhms", EgovStringUtil.nullConvert(params.get("aprvViewEndYmdhms")));
				viewMapper.updateTvoViewRqst(viewRqst);
			}

			result.put("result", String.valueOf(r));
		} else {
			result.put("result", "0");
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public List<EgovMap> selectAllTypeRqstList(ViewSrchVO vo) throws Exception {
		
		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			vo.setSaltText(saltText);
		}		
		
		return viewMapper.selectAllTypeRqstList(vo);
	}

//	@Override
//	public List<EgovMap> selectAllTypeWaitList(ViewSrchVO vo) throws Exception {
//		return viewMapper.selectAllTypeWaitList(vo);
//	}

	
	
	
	
	
	

}
