package kr.co.ucp.sheet.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.sheet.service.SheetmngMapper;
import kr.co.ucp.sheet.service.SheetmngService;

@Service("sheetmngService")
public class SheetmngServiceImpl implements SheetmngService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="sheetmngMapper")
	private SheetmngMapper sheetmngMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheetMng(Map<String, String> args) throws Exception {
		return sheetmngMapper.selectSheetMng(args);
	}

	// 합계
	@Override
	public String selectSheetMngTotalAmnt(Map<String, String> args) throws Exception {
		return sheetmngMapper.selectSheetMngTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertSheetMng(Map<String, String> args)  throws Exception {

		int rtn = 0;

		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("stanYmd"  , args.get("stanYmd").toString());
		
		String[] infoData = args.get("infoData").toString().split(";");
		for ( int i=0 ; i<infoData.length ; i++ ) {
			Map<String, String> newSeqNoMap = sheetmngMapper.selectNewSeqNo(map3);
			String newSeqNo = String.valueOf(newSeqNoMap.get("seqNo"));
			args.put("seqNo"      , args.get("stanYmd").toString()+CommUtil.setPad(newSeqNo, 3, "0", "L"));
			
			String[] data = infoData[i].split(":");
			args.put("moneyAmt"   , data[0]);
			args.put("acctRemark" , data[1]);
			rtn = sheetmngMapper.insertSheetMng(args);
		}
		return rtn;
	}

	// 수정
	@Override
	public int updateSheetMng(Map<String, String> args) throws Exception {
		return sheetmngMapper.updateSheetMng(args);
	}

	// 삭제
	@Override
	public int deleteSheetMng(Map<String, String> args) throws Exception {
		return sheetmngMapper.deleteSheetMng(args);
	}

	
	
	
	
	
	
	

	
	
	// 지난주 주간합계 하였는 지 확인
	@Override
	public Map<String, String> selectWeekSum(Map<String, String> args) throws Exception {
		return sheetmngMapper.selectWeekSum(args);
	}

	// 마감처리하였는 지 확인
	@Override
	public Map<String, String> selectSheetCloseCnt(Map<String, String> args) throws Exception {
		args.put("closeYn" , "Y");
		return sheetmngMapper.selectSheetCloseCnt(args);
	}
	
	// 마감처리
	@Override
	public int doClose(Map<String, String> args)  throws Exception {
		
		int rtn = 0;

		Map<String, String> weekSumMap = sheetmngMapper.selectNewWeekSum(args);	// 재정별 주간합계 계산

		
		
		weekSumMap.put("userId", args.get("userId").toString());
		rtn = sheetmngMapper.insertWeekSum(weekSumMap);				// 재정별 주간합계 등록

		
		rtn = registerMonthSum(args, "");			// 재정별 월간합계 등록
		
		args.put("closeYn", "Y");
		rtn = sheetmngMapper.updateSheetMngCloseYn(args);	// 전표 마감 처리
		
		return rtn;
	}

	// 마감취소
	@Override
	public int cancelClose(Map<String, String> args)  throws Exception {
		
		int rtn = 0;
		String stanYmd = args.get("stanYmd").toString();	// 전일 월간합계에서 기준일자를 변경하므로 원래 일자를 보존
		
		rtn = sheetmngMapper.deleteWeekSum(args);			// 재정별 주간합계 삭제
		
		rtn = sheetmngMapper.deleteMonthSum(args);			// 재정별 월간합계 삭제

		// 기준일자를 지난주로 변경하여 월간합계를 재계산한다.
		Map<String, String> prevSeMap = sheetmngMapper.selectPrevStartEndDay(args);
		String prevStanYmd = prevSeMap.get("endYmd").toString().replaceAll("-", "");	// 지난주 기준일자
		rtn = registerMonthSum(args, prevStanYmd);			// 재정별 월간합계 등록
		

		args.put("stanYmd"  , stanYmd);		// 원래 기준일자로 복귀
		args.put("closeYn", "N");
		rtn = sheetmngMapper.updateSheetMngCloseYn(args);	// 전표 마감취소 처리
		
		return rtn;
	}
	
	
	public int registerMonthSum(Map<String, String> args, String stanYmd) {
		int rtn = 0;
		
		try {
			if (!"".equalsIgnoreCase(stanYmd)) {	// 새로운 기준일자 지정
				args.put("stanYmd"  , stanYmd);
				args.put("stanYm"   , stanYmd.substring(0,6));
			}
			logger.info("= registerMonthSum() stanYmd => {}", args.get("stanYmd").toString());
			
			// 재정별 계정별 월간합계 계산
			List<Map<String, String>> monthSumMapList = sheetmngMapper.selectNewMonthSumList(args);
			
			for (int i = 0; i < monthSumMapList.size(); i++) {
				Map<String, String> monthSumMap = monthSumMapList.get(i);
				logger.info("= registerMonthSum() {}, monthSumMap => {}", i, monthSumMap.toString());
				monthSumMap.put("userId", args.get("userId").toString());
				rtn = sheetmngMapper.mergeMonthSum(monthSumMap);		// 재정별 계정별 월간합계 등록 및 수정
			}

			// 당해 재정별 상위합산계정 조회
			List<Map<String, String>> sumAcctCodeMapList = sheetmngMapper.selectSumAcctCodeList(args);

			for (int i = 0; i < sumAcctCodeMapList.size(); i++) {			// 하위합산계정부터 합산 등록 및 수정
				Map<String, String> sumAcctCodeMap = sumAcctCodeMapList.get(i);
				logger.info("= registerMonthSum() {}, sumAcctCodeMap => {}", i, sumAcctCodeMap.toString());

				// 재정별 계정별 하위월간합계 계산
				String acctUpCode = sumAcctCodeMap.get("acctCode").toString();
				if ( "00".equalsIgnoreCase(acctUpCode.substring(4,6)) ) acctUpCode = acctUpCode.substring(0,4);
				if ( "00".equalsIgnoreCase(acctUpCode.substring(2,4)) ) acctUpCode = acctUpCode.substring(0,2);
				sumAcctCodeMap.put("acctUpCode", acctUpCode);
				sumAcctCodeMap.put("stanYm"    , args.get("stanYm").toString());
				sumAcctCodeMap.put("stanYmd"   , args.get("stanYmd").toString());
				// 재정별 계정별 하위월간합계 계산
				Map<String, String> monthSumMap = sheetmngMapper.selectNewMonthSum(sumAcctCodeMap);
				logger.info("= registerMonthSum() {}, monthSumMap => {}", i, monthSumMap.toString());

				if ( !"0".equalsIgnoreCase(String.valueOf(monthSumMap.get("moneyAmt")))) {
					monthSumMap.put("stanYm"  , args.get("stanYm").toString());
					monthSumMap.put("acctGb"  , args.get("acctGb").toString());
					monthSumMap.put("acctCode", sumAcctCodeMap.get("acctCode").toString());
					monthSumMap.put("userId"  , args.get("userId").toString());
					rtn = sheetmngMapper.mergeMonthSum(monthSumMap);			// 재정별 계정별 월간합계 등록 및 수정
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtn;
	}
	
	

	@Override
	public Map<String, String> selectPrevStartEndDay(Map<String, String> args) throws Exception {
		return sheetmngMapper.selectPrevStartEndDay(args);
	}

	@Override
	public Map<String, String> selectNextStartEndDay(Map<String, String> args) throws Exception {
		return sheetmngMapper.selectNextStartEndDay(args);
	}
	
}
