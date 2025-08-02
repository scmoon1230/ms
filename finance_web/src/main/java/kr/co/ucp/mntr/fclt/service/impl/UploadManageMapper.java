package kr.co.ucp.mntr.fclt.service.impl;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.mntr.cmm.service.CmmCodeVO;
import kr.co.ucp.mntr.fclt.service.FcltRegHisVO;
import kr.co.ucp.mntr.fclt.service.UploadVO;

@Mapper("uploadManageMapper")
public interface UploadManageMapper {

	public int checkSigunguCode(String sigunguCode) throws Exception;
	public int checkDstrtCd(String dstrtCd) throws Exception;
	public int checkCode(CmmCodeVO vo) throws Exception;
	// 시스템코드 NEW 20170330
	public int checkSysCode(CmmCodeVO vo) throws Exception;
	// 시설물아이디 유무 체크 20170419
	public int checkFcltForFacility(String fcltId) throws Exception;
	public int checkPrntFcltForFacility(String prntFcltId) throws Exception;
	public int checkPrntFcltForUpload(String prntFcltId) throws Exception;
	// 시설물용도별유형코드 validation check
	public int checkFcltUsedTyCd(String codeId) throws Exception;
	// 필수항목 체크
	public String strRequierdColumn();
	public String selectFcltSeq(String param) throws Exception;

	//시설물아이디 일련번호 생성
	public String selectFcltSeq2(String param) throws Exception;
	public int duplicateFclt(String fcltId) throws Exception;

	public void deleteUploadCheck(String updUserId) throws Exception;
	public void insertUploadCheck(EgovMap map) throws Exception;
	public void insertUploadCheck(Map<String, Object> map) throws Exception;
	//시설물아이디 New 생성규칙 적용
	public void insertUploadCheck2(Map<String, Object> map) throws Exception;
	public int insertFcltList(UploadVO vo) throws Exception;
	public int uploadFcltList(UploadVO vo) throws Exception;
	public int deleteFcltList(UploadVO vo) throws Exception;
	// 시설물고유식별번호와 시스템코드 유무 체크 20170508
	public int checkFcltUid(CmmCodeVO vo) throws Exception;
	public void insertFcltHis(UploadVO vo) throws Exception;

	/* 2017 시설물 엑셀업로드 이력 */
	public List<FcltRegHisVO> selectFcltRegHisList(FcltRegHisVO vo) throws Exception;
	public int selectFcltRegHisListTotCnt(FcltRegHisVO vo) throws Exception;
}
