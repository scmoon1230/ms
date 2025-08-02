/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : CmmCodeServiceImpl.java
 * @Description :
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
package kr.co.ucp.mntr.cmm.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.mntr.cmm.service.CmmCodeMapper;
import kr.co.ucp.mntr.cmm.service.CmmCodeService;
import kr.co.ucp.mntr.cmm.service.CmmCodeVO;
import kr.co.ucp.mntr.cmm.service.CommonVO;
import kr.co.ucp.mntr.cmm.service.FcltVO;
import kr.co.ucp.mntr.cmm.util.SessionUtil;

@Service("cmmCodeService")
public class CmmCodeServiceImpl implements CmmCodeService {

	@Resource(name="cmmCodeMapper")
	private CmmCodeMapper cmmCodeMapper;

	@Override
	public List<EgovMap> selectCodeList(CmmCodeVO vo) throws Exception {
		return cmmCodeMapper.selectCodeList(vo);
	}

	@Override
	public List<EgovMap> selectFcltKindCodeList() throws Exception {
		return cmmCodeMapper.selectFcltKindCodeList();
	}

	@Override
	public List<EgovMap> selectDistrictList() throws Exception {
		return cmmCodeMapper.selectDistrictList();
	}

	@Override
	public List<EgovMap> selectPlcList(CmmCodeVO vo) throws Exception {
		return cmmCodeMapper.selectPlcList(vo);
	}

	@Override
	public List<EgovMap> selectFcltUsedTyList(CmmCodeVO vo) throws Exception {
		return cmmCodeMapper.selectFcltUsedTyList(vo);
	}

	@Override
	public List<EgovMap> selectFcltUsedTyAll() throws Exception {
		LoginVO loginVO = SessionUtil.getUserInfo();
		CommonVO vo = new CommonVO();
		vo.setUserId(loginVO.getUserId());
		vo.setGrpId(loginVO.getGrpId());
		vo.setAuthLvl(loginVO.getAuthLvl());
		return cmmCodeMapper.selectFcltUsedTyAll(vo);
	}

	@Override
	public List<EgovMap> selectFcltUsedTy(FcltVO vo) throws Exception {
		LoginVO loginVO = SessionUtil.getUserInfo();
		vo.setUserId(loginVO.getUserId());
		vo.setGrpId(loginVO.getGrpId());
		vo.setAuthLvl(loginVO.getAuthLvl());
		return cmmCodeMapper.selectFcltUsedTy(vo);
	}

	@Override
	public List<EgovMap> selectSigunguList() throws Exception {
		return cmmCodeMapper.selectSigunguList();
	}

	@Override
	public List<EgovMap> selectSysList() throws Exception {
		return cmmCodeMapper.selectSysList();
	}
	
	@Override
	public int insertCmMuneUsedLog(Map<String, Object> args) throws Exception {
		return cmmCodeMapper.insertCmMuneUsedLog(args);
	}

	@Override
	public List<EgovMap> selectListDstrtInfo(Map<String, String> params) throws Exception {
		return cmmCodeMapper.selectListDstrtInfo(params);
	}

	@Override
	public List<EgovMap> selectUserList(Map<String, Object> params) throws Exception {
		return cmmCodeMapper.selectUserList(params);
	}
}
