package kr.co.ucp.env.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.AcctcodeMapper;
import kr.co.ucp.env.service.AcctcodeService;

@Service("acctcodeService")
public class AcctcodeServiceImpl implements AcctcodeService {

	@Resource(name="acctcodeMapper")
	private AcctcodeMapper acctcodeMapper;

	@Override
	public List<Map<String, String>> stanYyList(Map<String, String> args) throws Exception {
		return acctcodeMapper.stanYyList(args);
	}

	// 계정과목 조건검색
	@Override
	public List<Map<String, String>> selectAcctcode(Map<String, Object> args) throws Exception {
		return acctcodeMapper.selectAcctcode(args);
	}

	// 계정과목 입력
	@Override
	public int insertAcctcode(Map<String, Object> args)  throws Exception {
		return acctcodeMapper.insertAcctcode(args);
	}

	// 계정과목 수정
	@Override
	public int updateAcctcode(Map<String, Object> args) throws Exception {
		return acctcodeMapper.updateAcctcode(args);
	}

	// 계정과목 삭제
	@Override
	public int deleteAcctcode(Map<String, String> args) throws Exception {
		return acctcodeMapper.deleteAcctcode(args);
	}

	// 계정과목 다중삭제
	@Override
	public int deleteAcctcodeMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			acctcodeMapper.deleteAcctcode(arg);
		}

		return 1;
	}

	@Override
	public int selectAcctcodeCnt(Map<String, Object> map) throws Exception {
		return acctcodeMapper.selectAcctcodeCnt(map);
	}

	// 계정과목 복사생성
	@Override
	public int makeNextFromPrev(Map<String, Object> args) throws Exception {
		int r = 0;
		
		args.put("stanYy"      , args.get("nextStanYy"));	// 다음년도
		List<Map<String, String>> list = acctcodeMapper.selectAcctcode(args);
		if ( list.size() == 0 ) {
			args.put("stanYy"      , args.get("prevStanYy"));	// 이전년도
			list = acctcodeMapper.selectAcctcode(args);
			for ( int i=0 ; i<list.size() ; i++ ) {
				Map<String, String> acctMap = list.get(i);
				
				Map<String, Object> para = new HashMap<String, Object>();
				para.put("stanYy"      , args.get("nextStanYy"));	// 다음년도
				para.put("acctGb"      , acctMap.get("acctGb"));
				para.put("acctCode"    , acctMap.get("acctCode"));
				para.put("acctName"    , acctMap.get("acctName"));
				para.put("printName"   , acctMap.get("printName"));
				para.put("acctLevel"   , acctMap.get("acctLevel"));
				para.put("sumYn"       , acctMap.get("sumYn"));
				para.put("inoutGb"     , acctMap.get("inoutGb"));
				para.put("inType"      , acctMap.get("inType"));
				para.put("acctUp"      , acctMap.get("acctUp"));
				para.put("linkAcctGb"  , acctMap.get("linkAcctGb"));
				para.put("linkAcctCode", acctMap.get("linkAcctCode"));
				para.put("useYn"       , acctMap.get("useYn"));
				para.put("userId"      , args.get("userId"));
				r = acctcodeMapper.insertAcctcode(para);
			}
		}
		
		return r;
	}

}
