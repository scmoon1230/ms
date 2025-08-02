package kr.co.ucp.fin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.fin.service.FinmastMapper;
import kr.co.ucp.fin.service.FinmngMapper;
import kr.co.ucp.fin.service.FintransMapper;
import kr.co.ucp.fin.service.FintransService;
import kr.co.ucp.sheet.service.SheetmngMapper;

@Service("fintransService")
public class FintransServiceImpl implements FintransService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="fintransMapper")
	private FintransMapper fintransMapper;

	@Resource(name="finmngMapper")
	private FinmngMapper finmngMapper;

	@Resource(name="finmastMapper")
	private FinmastMapper finmastMapper;

	@Resource(name="sheetmngMapper")
	private SheetmngMapper sheetmngMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectFintrans(Map<String, String> args) throws Exception {
		return fintransMapper.selectFintrans(args);
	}

	// 합계
	@Override
	public List<Map<String, String>> selectTotalAmnt(Map<String, String> args) throws Exception {
		return fintransMapper.selectTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertFintrans(Map<String, Object> args)  throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		
		map.put("stanYmd", args.get("stanYmd").toString());
		Map<String, String> info = fintransMapper.selectNewDetSeq(map);
		args.put("detSeq", info.get("detSeq"));

		//map.put("assetCode", args.get("assetCode").toString());
		//Map<String, String> infoList = finmngMapper.selectFinanceOne(map);
		//args.put("acctGb", infoList.get("acctGb"));
		
		return fintransMapper.insertFintrans(args);
	}

	// 수정
	@Override
	public int updateFintrans(Map<String, String> args) throws Exception {
		return fintransMapper.updateFintrans(args);
	}

	// 삭제
	@Override
	public int deleteFintrans(Map<String, String> args) throws Exception {
		return fintransMapper.deleteFintrans(args);
	}

	// 다중삭제
	@Override
	public int deleteFintransMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			fintransMapper.deleteFintrans(arg);
		}

		return 1;
	}

	// 마감처리하였는 지 확인
	@Override
	public Map<String, String> selectFintransCloseCnt(Map<String, String> args) throws Exception {

		Map<String, String> args2 = new HashMap<String, String>();
		args2.put("stanYmd" , args.get("stanYmd").toString());
		//args2.put("startYmd", args.get("startYmd").toString());
		//args2.put("endYmd"  , args.get("endYmd").toString());
		//args2.put("acctGb"  , args.get("acctGb").toString());
		args2.put("closeYn" , "Y");

		return fintransMapper.selectFintransCloseCnt(args2);
	}
	
	// 마감처리
	@Override
	public int doClose(Map<String, String> args)  throws Exception {
		
		int rtn = 0;
		
		try {

			Map<String, String> args2 = new HashMap<String, String>();
			args2.put("stanYmd"     , args.get("stanYmd"));
			args2.put("pageNo"     , "1");
			args2.put("rowsPerPage", "999");
			args2.put("sidx"       , "DET_SEQ");
			args2.put("sord"       , "ASC");
			List<Map<String, String>> fintransList = fintransMapper.selectFintrans(args2);

			String stanYmd = args.get("stanYmd");
			String lastStanYmd = finmastMapper.selectFinmastLastStanYmd();		// 지난번 기준일자 조회
			
			for (int i = 0; i < fintransList.size(); i++) {
				Map<String, String> map = fintransList.get(i);
				map.put("stanYmd", stanYmd);
				map.put("lastStanYmd", lastStanYmd);
				
				if ( map.get("acctCode") != null && !"".equalsIgnoreCase(map.get("acctCode"))) {	// 계정과목 있을 때 (전표처리 대상일 때)
					args.put("acctGb"   , map.get("acctGb").toString());
					args.put("acctCode" , map.get("acctCode").toString());
					
					if ( "3".equalsIgnoreCase(map.get("acctProcGb"))) {	// 처리구분이 '원금/이자'일 때
						int tmp = Integer.parseInt(map.get("moneyAmt")) + Integer.parseInt(map.get("intAmt"));
						args.put("moneyAmt"   , String.valueOf(tmp));
					} else {
						args.put("moneyAmt"   , String.valueOf(map.get("moneyAmt")));
					}
					
					String rem = map.get("assetName").toString()+" - "+map.get("acctProcGbName");
					args.put("acctRemark" , rem);
					
					Map<String, String> newSeqNoMap = sheetmngMapper.selectNewSeqNo(args);
					String newSeqNo = String.valueOf(newSeqNoMap.get("seqNo"));
					args.put("seqNo"      , args.get("stanYmd")+CommUtil.setPad(newSeqNo, 3, "0", "L"));
					
					args.put("userId", args.get("userId"));

					rtn = sheetmngMapper.insertSheetMng(args);	// 전표 생성
					
					map.put("acctYn" , "Y");
					map.put("acctYmd", args.get("stanYmd"));
					map.put("seqNo"  , args.get("seqNo"));
				}

				makeFinmast(map);		// 금융자산원장 생성
				
				map.put("closeYn", "Y");
				rtn = fintransMapper.updateFintrans(map);		// 마감 처리
			}
			

			// 이번주 거래가 없는 금융자산 명세 생성 
			Map<String, String> args3 = new HashMap<String, String>();
			args3.put("stanYmd"    , lastStanYmd);
			args3.put("pageNo"     , "1");
			args3.put("rowsPerPage", "999");
			args3.put("sidx"       , "FI.ACCT_GB, FI.ASSET_NAME, FI.BANK_NAME, FI.ACCOUNT_NO");
			args3.put("sord"       , "ASC");
			
			List<Map<String, String>> lastFinmastList = finmastMapper.selectFinmastList(args3);
			for (int i = 0; i < lastFinmastList.size(); i++) {
				Map<String, String> map = lastFinmastList.get(i);
				map.put("stanYmd"    , stanYmd);
				Map<String, String> thisFinmast = finmastMapper.selectFinmastOne(map);
				if ( thisFinmast == null ) {
					long addAmt = 0;
					long initAmt = 0;
					initAmt = Long.parseLong(String.valueOf(map.get("totalAmt")));

					Map<String, Object> paraMap = new HashMap<String, Object>();
					paraMap.put("stanYmd"  , map.get("stanYmd"));
					paraMap.put("assetCode", map.get("assetCode"));
					paraMap.put("initAmt"  , initAmt);
					paraMap.put("addAmt"   , addAmt);
					int r = finmastMapper.insertFinmast(paraMap);
				}
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rtn;
	}

	
	public void makeFinmast(Map<String, String> para) throws Exception {

		long initAmt = 0;
		
		long addAmt = 0;
		if ( "I".equalsIgnoreCase(para.get("inoutGb"))) {
			addAmt = addAmt + Long.parseLong(String.valueOf(para.get("moneyAmt")));
		} else {
			addAmt = addAmt - Long.parseLong(String.valueOf(para.get("moneyAmt")));
		}

		Map<String, String> args3 = new HashMap<String, String>();
		args3.put("stanYmd"    , para.get("lastStanYmd"));
		args3.put("assetCode"  , para.get("assetCode"));
		Map<String, String> lastFinmast = finmastMapper.selectFinmastOne(args3);

		args3.put("stanYmd"    , para.get("stanYmd"));
		Map<String, String> thisFinmast = finmastMapper.selectFinmastOne(args3);

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("stanYmd"  , para.get("stanYmd"));
		paraMap.put("assetCode", para.get("assetCode"));
		
		if ( thisFinmast == null ) {		// 이번주 명세가 없을 때
			if ( lastFinmast == null ) {		// 지난주명세가 없을 때
				paraMap.put("initAmt"  , initAmt);
				paraMap.put("addAmt"   , addAmt);
				int r = finmastMapper.insertFinmast(paraMap);
			} else {							// 지난주 명세가 있을 때
				initAmt = Long.parseLong(String.valueOf(lastFinmast.get("totalAmt")));
				paraMap.put("initAmt"  , initAmt);
				paraMap.put("addAmt"   , addAmt);
				int r = finmastMapper.insertFinmast(paraMap);
			}
		} else {							// 이번주 명세가 있을 때
			initAmt = Long.parseLong(String.valueOf(thisFinmast.get("initAmt")));
			addAmt = addAmt + Long.parseLong(String.valueOf(thisFinmast.get("addAmt")));
			paraMap.put("initAmt"  , initAmt);
			paraMap.put("addAmt"   , addAmt);
			int r = finmastMapper.updateFinmast(paraMap);
		}
				
	}
	
	
	
	// 마감취소
	@Override
	public int cancelClose(Map<String, String> args)  throws Exception {
		
		int rtn = 0;

		Map<String, String> args2 = new HashMap<String, String>();
		args2.put("stanYmd"     , args.get("stanYmd"));
		//args2.put("startYmd"    , args.get("startYmd").toString());
		//args2.put("endYmd"      , args.get("endYmd").toString());
		//args2.put("acctGb"      , args.get("acctGb").toString());
		args2.put("pageNo"     , "1");
		args2.put("rowsPerPage", "999");
		args2.put("sidx"       , "DET_SEQ");
		args2.put("sord"       , "ASC");
		List<Map<String, String>> fintransList = fintransMapper.selectFintrans(args2);

		for (int i = 0; i < fintransList.size(); i++) {
			Map<String, String> map = fintransList.get(i);
			if ( map.get("seqNo") != null ) {
				args.put("seqNo"   , String.valueOf(map.get("seqNo")));
				rtn = sheetmngMapper.deleteSheetMng(args);		// 전표 삭제
			}			
		}

		args.put("closeYn", "N");
		args.put("acctYmd", "");
		args.put("seqNo"  , "");
		rtn = fintransMapper.updateFintransClose(args);		// 마감취소 처리

		// 금융자산원장 삭제
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("stanYmd"  , args.get("stanYmd"));
		int r = finmastMapper.deleteFinmast(paraMap);
		
		return rtn;
	}

}
