package kr.co.ucp.wrks.sstm.mbl.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.wrks.sstm.mbl.service.MblInfoMapper;
import kr.co.ucp.wrks.sstm.mbl.service.MblInfoService;

/**
 * ----------------------------------------------------------
 *
 * @Class Name : MblInfoServiceImpl
 * @Description : 모바일 기기정보
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-01-22   김정원       최초작성
 *
 * ----------------------------------------------------------
 *               </pre>
 */
@Service("mblInfoService")
public class MblInfoServiceImpl extends EgovAbstractServiceImpl implements MblInfoService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="mblInfoMapper")
	private MblInfoMapper mblInfoMapper;

	@Override
	public Map<String, String> getUser(String userid) throws Exception {
		Map<String, String> mapD = mblInfoMapper.getUser(userid);

		return mapD;
	}

	/*
	 * 기기정보 리스트
	 */
	@Override
	public List<Map<String, String>> getList(Map<String, String> args) throws Exception {
		List<Map<String, String>> list = mblInfoMapper.list(args);

		return list;
	}

	/** 모바일 계정정보 상세 조회 */
	@Override
	public List<Map<String, String>> getListDetail(String mppId) throws Exception {
		List<Map<String, String>> list = mblInfoMapper.getListDetail(mppId);

		return list;
	}

	/** 모바일 계정정보_콤보박스_모바일기기종류 */
	@Override
	public List<Map<String, String>> getListMapType() throws Exception {
		List<Map<String, String>> list = mblInfoMapper.getListMapType();

		return list;
	}

	/** 모바일 계정정보_콤보박스_모바일기기OS유형 */
	@Override
	public List<Map<String, String>> getListOsType() throws Exception {
		List<Map<String, String>> list = mblInfoMapper.getListOsType();

		return list;
	}

	/*
	 * 기기정보 입력
	 */
	@Override
	public int insert(Map<String, Object> args, List<Map<String, String>> list) throws Exception {
		int ret = 0;

		ret = mblInfoMapper.insert(args);

		for (int i = 0; i < list.size(); i++) {
			Map<String, String> arg = list.get(i);
			ret = mblInfoMapper.insertRcvTime(arg);
		}

		return ret;
	}

	/*
	 * 기기정보 수정
	 */
	@Override
	public int update(Map<String, Object> args) throws Exception {
		int ret = mblInfoMapper.update(args);

		return ret;
	}

	/*
	 * 기기정보 삭제
	 */
	@Override
	public int delete(Map<String, Object> args) throws Exception {
		int ret = mblInfoMapper.delete(args);

		return ret;
	}

	/*
	 * 기기정보 다중삭제
	 */
	@Override
	public int deleteMulti(List<Map<String, Object>> args) throws Exception {

		int ret = 0;
		for (int i = 0; i < args.size(); i++) {

			Map<String, Object> arg = (Map<String, Object>) args.get(i);

			ret = mblInfoMapper.delete(arg);

		}
		return ret;
	}

	/*
	 * 기기정보 엑셀업로드
	 */
	@Override
 	public Map<String, Object> excelUpload(List<Map<String, String>> args) {
		Map<String, Object> mapRet = new HashMap<String, Object>();

		int inCnt = 0;

		for (Map<String, String> map : args) {
			try {
				inCnt += mblInfoMapper.mobileExcelInsert(map);
			}
			catch (DataIntegrityViolationException e) {
				mapRet.put("error", -1);
					}
			catch (UncategorizedSQLException e) {
					mapRet.put("error", -1);
			}
			catch (Exception e) {
				mapRet.put("error", -1);
			}
		}
		mapRet.put("error", 1);
		mapRet.put("inCnt", inCnt);
		return mapRet;
	}


	/*
	 * 기기정보 모바일 수신 시간조회
	 */
	@Override
	public List<Map<String, String>> rcvTimeList(Map<String, Object> args) throws Exception {
		return mblInfoMapper.rcvTimeList(args);
	}

	/*
	 * 기기정보 메세지 수신시간삭제
	 */
	@Override
	public int deleteRcvTime(Map<String, Object> args) throws Exception {
		return mblInfoMapper.deleteRcvTime(args);
	}

	/* 모바일 계정정보 수신요일별 시간 수정 */
	@Override
	public int updateRcvTime(List<Map<String, String>> args) throws Exception {
		int ret = 0;

		for (int i = 0; i < args.size(); i++) {
			Map<String, String> arg = (Map<String, String>) args.get(i);
			ret = mblInfoMapper.updateRcvTime(arg);
		}

		return ret;
	}

	@Override
	public int insertRcvTime(List<Map<String, String>> args) throws Exception {
		for (int i = 0; i < args.size(); i++) {
			Map<String, String> arg = args.get(i);
			mblInfoMapper.insertRcvTime(arg);
		}
		return 1;
	}

	@Override
	public List<Map<String, String>> getUserList(Map<String, Object> args) throws Exception {

        String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> getUserList(), USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
        if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			args.put("saltText", saltText);
        }
		
		List<Map<String, String>> list = mblInfoMapper.getUserList(args);
		return list;
	}

}
