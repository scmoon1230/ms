package kr.co.ucp.mntr.mng.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.CmmService;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.cmm.util.CommonUtil;
import kr.co.ucp.cmm.validator.MapValidator;
import kr.co.ucp.egov.com.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.mng.service.VmsInfoService;

@Controller
public class VmsInfoController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "messageSource")
	private MessageSource messageSource;

	@Resource(name = "cmmService")
	private CmmService cmmService;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name = "vmsInfoService")
	private VmsInfoService vmsInfoService;

	@RequestMapping(value = "/mntr/vmsInfo.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		List<Map<String, Object>> list = vmsInfoService.dstrtNmCd(null);
		
		request.setAttribute("list", list);
		request.setAttribute("listSize", list.size());

		return "/mntr/mng/vmsInfo";
	}

	// VMS정보 조회
	@RequestMapping(value = "/mntr/mng/vmsInfo/list.json")
	@ResponseBody
	public Map<String, Object> getList(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		args.put("vmsNm", EgovStringUtil.nullConvert(request.getParameter("vmsNm")));
		List<Map<String, Object>> resultRows = vmsInfoService.selectVmsList(args);

		map.put("rows", resultRows);

		return map;
	}

	// VMS그룹 목록
	@RequestMapping(value = "/mntr/mng/vmsinfo/vms_grp_list.json")
	@ResponseBody
	public Map<String, Object> getVmsGrpList(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {
		Map<String, Object> args = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String dstrtNm = EgovStringUtil.nullConvert(request.getParameter("dstrtCd"));
		
		if (dstrtNm.matches("\\d+")) {
			args.put("dstrtCd", dstrtNm);
	    } else {
	    	args.put("dstrtNm", dstrtNm);
			String dstrtCd = vmsInfoService.dstrtNmCd(args).get(0).get("dstrtCd").toString();
			args.put("dstrtCd", dstrtCd);
	    }

		List<Map<String, String>> resultRows = vmsInfoService.vmsGrpList(args);
		
		map.put("rows", resultRows);

		return map;
	}

	// VMS정보 등록
	@SuppressWarnings("unused")
	@RequestMapping(value = "/mntr/mng/vmsinfo/insert.json")
	@ResponseBody
	public Map<String, Object> insert(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> args = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String dstrtNm = EgovStringUtil.nullConvert(request.getParameter("dstrtCd"));
		
		if (dstrtNm.matches("\\d+")) {
			map.put("dstrtCd", dstrtNm);
	    } else {
	    	args.put("dstrtNm", dstrtNm);
			String dstrtCd = vmsInfoService.dstrtNmCd(args).get(0).get("dstrtCd").toString();
			map.put("dstrtCd", dstrtCd);
	    }

		map.put("vmsId", EgovStringUtil.nullConvert(request.getParameter("vmsId")));
		map.put("vmsNm", EgovStringUtil.nullConvert(request.getParameter("vmsNm")));
		map.put("playbackSpeed", EgovStringUtil.nullConvert(request.getParameter("playbackSpeed")));
		map.put("playbackUseYnLfp", EgovStringUtil.nullConvert(request.getParameter("playBackUseYnLfp")));
		map.put("recordingTy", EgovStringUtil.nullConvert(request.getParameter("recordingTy")));
		map.put("rgsUserId", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();
		List<EgovMap> tableInfoList = cmmService.selectTableInfoList("CM_VMS_INFO");
		Validator validator = new MapValidator(messageSource, tableInfoList);
		List<FieldError> fieldErrorList = CommUtil.validateMap(validator, request.getParameterMap());

		if (!fieldErrorList.isEmpty()) {
			// Error 가 존재함
			mapRet.put("session", 0);
			mapRet.put("msg", "오류가 발생했습니다.");
			mapRet.put("errors", fieldErrorList);
			return mapRet;
		}

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 0);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		try {
			int insertResult;
			try {
				insertResult = vmsInfoService.insertVmsInfo(map);
			} catch (DataIntegrityViolationException e) {
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 등록된 데이터입니다.");
				return mapRet;
			} catch (UncategorizedSQLException e) {
				// dup error
				mapRet.put("session", 0);
				mapRet.put("msg", "입력 값 확인후 등록해주세요.");
				return mapRet;
			} catch (Exception e) {
				logger.error(e.getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "alert");
				return mapRet;
			}
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		} catch (Exception e) {
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러입니다.");
		}
		return mapRet;
	}

	// VMS정보 수정
	@SuppressWarnings("unused")
	@RequestMapping(value = "/mntr/mng/vmsinfo/update.json")
	@ResponseBody
	public Map<String, Object> update(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, Object> map = new HashMap<String, Object>();

		map.put("dstrtCd", EgovStringUtil.nullConvert(request.getParameter("dstrtCd")));
		map.put("vmsId", EgovStringUtil.nullConvert(request.getParameter("vmsId")));
		map.put("vmsNm", EgovStringUtil.nullConvert(request.getParameter("vmsNm")));
		map.put("playbackSpeed", EgovStringUtil.nullConvert(request.getParameter("playbackSpeed")));
		map.put("playbackUseYnLfp", EgovStringUtil.nullConvert(request.getParameter("playBackUseYnLfp")));
		map.put("recordingTy", EgovStringUtil.nullConvert(request.getParameter("recordingTy")));
		map.put("rgsUserId", sesUserId);

		Map<String, Object> mapRet = new HashMap<String, Object>();

		List<EgovMap> tableInfoList = cmmService.selectTableInfoList("CM_VMS_INFO");
		Validator validator = new MapValidator(messageSource, tableInfoList);
		List<FieldError> fieldErrorList = CommUtil.validateMap(validator, request.getParameterMap());

		if (!fieldErrorList.isEmpty()) {
			// Error 가 존재함
			mapRet.put("session", 0);
			mapRet.put("msg", "오류가 발생했습니다.");
			mapRet.put("errors", fieldErrorList);
			return mapRet;
		}

		if (!CommonUtil.checkDataFilterObj(map)) {
			mapRet.put("session", 0);
			mapRet.put("msg", "특수문자를 포함한 자료는 저장할 수 없습니다.");
			return mapRet;
		}

		try {
			int updateResult;
			try {
				updateResult = vmsInfoService.updateVmsInfo(map);
			} catch (DataIntegrityViolationException e) {
				logger.error(e.getRootCause().getMessage());
				mapRet.put("session", 0);
				mapRet.put("msg", "이미 존재하는 VMS입니다.");

				return mapRet;
			} catch (UncategorizedSQLException e) {
				// dup error
				if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "이미 등록된 코드입니다.");

					return mapRet;
				} else {
					logger.error(e.getRootCause().getMessage());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");

		} catch (Exception e) {
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러!!!");
		}
		return mapRet;
	}

	// VMS정보 삭제
	@SuppressWarnings("unused")
	@RequestMapping(value = "/mntr/mng/vmsinfo/delete.json")
	@ResponseBody
	public Map<String, Object> delete(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		Map<String, String> map = new HashMap<String, String>();

		map.put("dstrtCd", EgovStringUtil.nullConvert(request.getParameter("dstrtCd")));
		map.put("vmsId", EgovStringUtil.nullConvert(request.getParameter("vmsId")));
		map.put("vmsNm", EgovStringUtil.nullConvert(request.getParameter("vmsNm")));
		map.put("updUserId", sesUserId);

		int ret = vmsInfoService.deleteVmsInfo(map);
		Map<String, Object> mapRet = new HashMap<String, Object>();

		mapRet.put("session", 1);
		mapRet.put("msg", "삭제하였습니다.");

		return mapRet;
	}
}
