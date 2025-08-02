/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : CmmCodeController.java
 * @Description : GIM 공통 요청 컨트롤러
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2014. 11. 7. is 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.cmm.web;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.mntr.cmm.service.CmmCodeService;
import kr.co.ucp.mntr.cmm.service.CmmCodeVO;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class CmmCodeController {

	@Resource(name="cmmCodeService")
	private CmmCodeService cmmCodeService;

	// 공통코드 리스트를 가져온다.
	@RequestMapping(value="/mntr/cmm/selectCodeList.json")
	public @ResponseBody Map<String, Object> selectCodeList(@ModelAttribute("cmmCodeVO") CmmCodeVO cmmCodeVO) throws Exception {
		List<EgovMap> list = cmmCodeService.selectCodeList(cmmCodeVO);

		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("list", list);
		return resultMap;
	}

	// 유효성검사
	@RequestMapping(value="/mntr/validator.do")
	public String validate() {
		return "blank/cmm/validator";
	}

	// 시설물 종류 리스트를 가져온다.
	@RequestMapping(value="/mntr/cmm/selectFcltKindList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectFcltKindList() throws Exception {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("list", cmmCodeService.selectFcltKindCodeList());
		return resultMap;
	}

	// 시설물 사용유형 종류 리스트를 가져온다.
	@RequestMapping(value="/mntr/cmm/selectFcltUsedTyList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectFcltUsedTyList(@ModelAttribute("cmmCodeVO") CmmCodeVO cmmCodeVO) throws Exception {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("list", cmmCodeService.selectFcltUsedTyList(cmmCodeVO));
		return resultMap;
	}

	// 경찰지구대 리스트.
	@RequestMapping(value="/mntr/cmm/selectPlcList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectPlcList(@ModelAttribute("cmmCodeVO") CmmCodeVO cmmCodeVO) throws Exception {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("list", cmmCodeService.selectPlcList(cmmCodeVO));
		return resultMap;
	}

	// 시설물 사용유형 종류 리스트 모두 가져온다.
	@RequestMapping(value="/mntr/cmm/selectFcltUsedTyAll.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectFcltUsedTyAll(HttpServletRequest request) throws Exception {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("list", cmmCodeService.selectFcltUsedTyAll());
		return resultMap;
	}

	// 지구(district) 리스트를 가져온다.
	@RequestMapping(value="/mntr/cmm/selectDistrictList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> selectDistrictList() throws Exception {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("list", cmmCodeService.selectDistrictList());
		return resultMap;
	}

	// 시도(sigungu) 리스트를 가져온다.
	@RequestMapping(value="/mntr/cmm/sigunguList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sigunguList() throws Exception {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("list", cmmCodeService.selectSigunguList());
		return resultMap;
	}

	// 시스템(sysList) 리스트를 가져온다.
	@RequestMapping(value="/mntr/cmm/sysList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> sysList() throws Exception {
		Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
		resultMap.put("list", cmmCodeService.selectSysList());
		return resultMap;
	}
}
