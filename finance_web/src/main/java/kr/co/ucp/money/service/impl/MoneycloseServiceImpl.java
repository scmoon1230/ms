package kr.co.ucp.money.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.money.service.MoneycloseMapper;
import kr.co.ucp.money.service.MoneycloseService;
import kr.co.ucp.sheet.service.SheetmngMapper;

@Service("moneycloseService")
public class MoneycloseServiceImpl implements MoneycloseService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="moneycloseMapper")
	private MoneycloseMapper moneycloseMapper;

	@Resource(name="sheetmngMapper")
	private SheetmngMapper sheetmngMapper;

	@Override
	public Map<String, String> selectStartEndDay(Map<String, String> args) throws Exception {
		return moneycloseMapper.selectStartEndDay(args);
	}
	
	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyClose(Map<String, String> args) throws Exception {
		return moneycloseMapper.selectMoneyClose(args);
	}

	// 합계
	@Override
	public String selectMoneyCloseTotalAmnt(Map<String, String> args) throws Exception {
		return moneycloseMapper.selectMoneyCloseTotalAmnt(args);
	}

	// 마감처리하였는 지 확인
	@Override
	public Map<String, String> selectMoneyCloseCnt(Map<String, String> args) throws Exception {

		Map<String, String> args2 = new HashMap<String, String>();
		//args2.put("stanYmd" , args.get("stanYmd").toString());
		args2.put("startYmd", args.get("startYmd").toString());
		args2.put("endYmd"  , args.get("endYmd").toString());
		args2.put("acctGb"  , args.get("acctGb").toString());
		args2.put("closeYn" , "Y");

		return moneycloseMapper.selectMoneyCloseCnt(args2);
	}
	
	// 마감처리
	@Override
	public int doClose(Map<String, String> args)  throws Exception {
		
		int rtn = 0;

		args.put("stanYmd"     , args.get("endYmd").toString());	// 전표기준일자(전표등록 시 사용하는 변수)
		
		Map<String, String> args2 = new HashMap<String, String>();
		args2.put("stanYmd"     , args.get("stanYmd").toString());
		args2.put("startYmd"    , args.get("startYmd").toString());
		args2.put("endYmd"      , args.get("endYmd").toString());
		args2.put("acctGb"      , args.get("acctGb").toString());
		args2.put("pageNo"     , "1");
		args2.put("rowsPerPage", "999");
		args2.put("sidx"       , "MONEY_CODE");
		args2.put("sord"       , "ASC");
		List<Map<String, String>> list = moneycloseMapper.selectMoneyClose(args2);

		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			args.put("acctCode"   , map.get("acctCode").toString());
			args.put("moneyAmt"   , String.valueOf(map.get("moneyAmt")));
			String rem = map.get("acctName").toString()+" / "+map.get("stanYmd").toString().replaceAll("-", "");
			rem += " / "+String.valueOf(map.get("totalCnt"))+"건";
			args.put("acctRemark" , rem);
			
			Map<String, String> newSeqNoMap = sheetmngMapper.selectNewSeqNo(args2);
			String newSeqNo = String.valueOf(newSeqNoMap.get("seqNo"));
			args.put("seqNo"      , args.get("stanYmd").toString()+CommUtil.setPad(newSeqNo, 3, "0", "L"));

			rtn = sheetmngMapper.insertSheetMng(args);		// 전표 생성
		}

		args2.put("closeYn", "Y");
		rtn = moneycloseMapper.updateMoneyClose(args2);		// 마감 처리
		
		return rtn;
	}

	// 마감취소
	@Override
	public int cancelClose(Map<String, String> args)  throws Exception {
		
		int rtn = 0;

		List<Map<String, String>> list = sheetmngMapper.selectSheetMng(args);

		for (int i = 0; i < list.size(); i++) {
			Map<String, String> map = list.get(i);
			logger.info("= cancelClose() {}, map => {}", i, map.toString());

			if ( "자동".equalsIgnoreCase(map.get("acctType").toString().trim()) ) {
				if ( map.get("acctRemark").split("/").length == 3
					&& map.get("acctRemark").indexOf("건") != -1 ) {		// 헌금 마감일 때
					logger.info("= cancelClose() {}:del, map => {}", i, map.toString());

					args.put("seqNo"   , String.valueOf(map.get("seqNo")));
					rtn = sheetmngMapper.deleteSheetMng(args);		// 전표 삭제
				}
			}
		}

		args.put("closeYn", "N");
		rtn = moneycloseMapper.updateMoneyClose(args);		// 마감취소 처리
		
		return rtn;
	}

}
