package kr.co.ucp.sheet2.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.sheet.service.SheetmngMapper;
import kr.co.ucp.sheet2.service.Sheet2mngMapper;
import kr.co.ucp.sheet2.service.Sheet2mngService;

@Service("sheet2mngService")
public class Sheet2mngServiceImpl implements Sheet2mngService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="sheetmngMapper")
	private SheetmngMapper sheetmngMapper;

	@Resource(name="sheet2mngMapper")
	private Sheet2mngMapper sheet2mngMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectSheet2Mng(Map<String, String> args) throws Exception {
		return sheet2mngMapper.selectSheet2Mng(args);
	}

	// 합계
	@Override
	public String selectSheet2MngTotalAmnt(Map<String, String> args) throws Exception {
		return sheet2mngMapper.selectSheet2MngTotalAmnt(args);
	}

	// 마감처리
	@Override
	public int doClose(Map<String, String> args)  throws Exception {
		
		int rtn = 0;
		args.put("acctGb"   , "1");		// 1재정
		Map<String, String> weekSumMap = sheetmngMapper.selectNewWeekSum(args);	// 재정별 주간합계 계산
		
		weekSumMap.put("acctGb", "A");	// 종교인
		weekSumMap.put("preAmt", "0");		weekSumMap.put("inAmt", "0");
		weekSumMap.put("userId", args.get("userId").toString());
		rtn = sheetmngMapper.insertWeekSum(weekSumMap);				// 재정별 주간합계 등록

		args.put("acctGb"   , "A");		// 종교인
		rtn = registerMonthSum2(args, "");			// 재정별 월간합계 등록
		
//		args.put("closeYn", "Y");
//		rtn = sheetmngMapper.updateSheetMngCloseYn(args);	// 전표 마감 처리 <= 종교인 전표는 없다. 
		
		return rtn;
	}

	// 마감취소
	@Override
	public int cancelClose(Map<String, String> args)  throws Exception {
		
		int rtn = 0;
		String stanYmd = args.get("stanYmd").toString();	// 전일 월간합계에서 기준일자를 변경하므로 원래 일자를 보존

		args.put("acctGb"   , "A");		// 종교인
		rtn = sheetmngMapper.deleteWeekSum(args);			// 재정별 주간합계 삭제
		
		rtn = sheetmngMapper.deleteMonthSum(args);			// 재정별 월간합계 삭제
		
		// 기준일자를 지난주로 변경하여 월간합계를 재계산한다.
		Map<String, String> prevSeMap = sheetmngMapper.selectPrevStartEndDay(args);
		String prevStanYmd = prevSeMap.get("endYmd").toString().replaceAll("-", "");	// 지난주 기준일자
		rtn = registerMonthSum2(args, prevStanYmd);			// 재정별 월간합계 등록
		

//		args.put("stanYmd"  , stanYmd);		// 원래 기준일자로 복귀
//		args.put("closeYn", "N");
//		rtn = sheetmngMapper.updateSheetMngCloseYn(args);	// 전표 마감취소 처리 <= 종교인 전표는 없다. 
		
		return rtn;
	}
	
	
	public int registerMonthSum2(Map<String, String> args, String stanYmd) {
		int rtn = 0;
		
		try {
			if (!"".equalsIgnoreCase(stanYmd)) {	// 새로운 기준일자 지정
				args.put("stanYmd"  , stanYmd);
				args.put("stanYm"   , stanYmd.substring(0,6));
			}
			logger.info("= registerMonthSum2() stanYmd => {}", args.get("stanYmd").toString());

			// 종교인재정 계정별 월간합계 계산
			List<Map<String, String>> monthSumMapList = sheet2mngMapper.select2NewMonthSumList(args);
			
			for (int i = 0; i < monthSumMapList.size(); i++) {
				Map<String, String> monthSumMap = monthSumMapList.get(i);
				logger.info("= registerMonthSum2() {}, monthSumMap => {}", i, monthSumMap.toString());
				monthSumMap.put("userId", args.get("userId").toString());
				rtn = sheetmngMapper.mergeMonthSum(monthSumMap);		// 재정별 계정별 월간합계 등록 및 수정
			}

			// 당해 재정별 상위합산계정 조회
			args.put("acctGb"   , "A");		// 종교인
			List<Map<String, String>> sumAcctCodeMapList = sheetmngMapper.selectSumAcctCodeList(args);

			for (int i = 0; i < sumAcctCodeMapList.size(); i++) {			// 하위합산계정부터 합산 등록 및 수정
				Map<String, String> sumAcctCodeMap = sumAcctCodeMapList.get(i);
				logger.info("= registerMonthSum2() {}, sumAcctCodeMap => {}", i, sumAcctCodeMap.toString());

				// 재정별 계정별 하위월간합계 계산
				String acctUpCode = sumAcctCodeMap.get("acctCode").toString();
				if ( "00".equalsIgnoreCase(acctUpCode.substring(4,6)) ) acctUpCode = acctUpCode.substring(0,4);
				if ( "00".equalsIgnoreCase(acctUpCode.substring(2,4)) ) acctUpCode = acctUpCode.substring(0,2);
				sumAcctCodeMap.put("acctUpCode", acctUpCode);
				sumAcctCodeMap.put("stanYm"    , args.get("stanYm").toString());
				sumAcctCodeMap.put("stanYmd"   , args.get("stanYmd").toString());
				// 재정별 계정별 하위월간합계 계산
				Map<String, String> monthSumMap = sheet2mngMapper.select2NewMonthSum(sumAcctCodeMap);
				logger.info("= registerMonthSum2() {}, monthSumMap => {}", i, monthSumMap.toString());

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
	
}
