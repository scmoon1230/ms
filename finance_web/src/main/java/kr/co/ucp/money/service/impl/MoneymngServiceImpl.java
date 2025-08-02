package kr.co.ucp.money.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.MemberMapper;
import kr.co.ucp.money.service.MoneymngMapper;
import kr.co.ucp.money.service.MoneymngService;

@Service("moneymngService")
public class MoneymngServiceImpl implements MoneymngService {

	@Resource(name="memberMapper")
	private MemberMapper memberMapper;

	@Resource(name="moneymngMapper")
	private MoneymngMapper moneymngMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectMoneyMng(Map<String, String> args) throws Exception {
		return moneymngMapper.selectMoneyMng(args);
	}

	// 합계
	@Override
	public String selectTotalAmnt(Map<String, String> args) throws Exception {
		return moneymngMapper.selectTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertMoneyMng(Map<String, Object> args)  throws Exception {
		
		int rtn = 0;
		
		String infoType = args.get("infoType").toString();
		String[] infoData = args.get("infoData").toString().split(",");
		for ( int i=0 ; i<infoData.length ; i++ ) {
			String[] data = infoData[i].split(":");
			
			if ( "name".equalsIgnoreCase(infoType) ) {
				args.put("memberName", data[0]);
				args.put("moneyAmt"  , data[1]);
				args.put("index"     , CommUtil.setPad(String.valueOf(i+1), 3, "0", "L"));
				rtn = moneymngMapper.insertMoneyMngByName(args);
				
			} else if ( "id".equalsIgnoreCase(infoType) ) {
				args.put("memberId"  , data[0]);
				args.put("moneyAmt"  , data[1]);
				args.put("index"     , CommUtil.setPad(String.valueOf(i+1), 3, "0", "L"));
				rtn = moneymngMapper.insertMoneyMngById(args);
			} 
		}

		return rtn;
	}

	// 수정
	@Override
	public int updateMoneyMng(Map<String, Object> args) throws Exception {
		return moneymngMapper.updateMoneyMng(args);
	}

	// 삭제
	@Override
	public int deleteMoneyMng(Map<String, String> args) throws Exception {
		return moneymngMapper.deleteMoneyMng(args);
	}

	// 다중삭제
	@Override
	public int deleteMoneyMngMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			moneymngMapper.deleteMoneyMng(arg);
		}

		return 1;
	}

	// 다중수정
	@Override
	public int modifyMoneyMngMulti(List<Map<String, String>> list) throws Exception{
		int inCnt = 0;

		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			arg.put("index"      , CommUtil.setPad(String.valueOf(inCnt+1), 3, "0", "L"));
			moneymngMapper.modifyMoneyMng(arg);
			
		}

		return 1;
	}

	// 엑셀업로드
	@Override
 	public Map<String, Object> excelUpload(List<Map<String, String>> args) {
		Map<String, Object> mapRet = new HashMap<String, Object>();

		int inCnt = 0;

		for (Map<String, String> map : args) {
			try {
				Map<String, Object> args2 = new HashMap<String, Object>();
				args2.put("stanYmd"    , map.get("stanYmd").toString());
				args2.put("moneyCode"  , map.get("moneyCode").toString());
				args2.put("worshipCode", map.get("worshipCode").toString());
				args2.put("memberName" , map.get("memberName").toString());
				args2.put("moneyAmt"   , map.get("moneyAmt").toString());
				args2.put("index"      , CommUtil.setPad(String.valueOf(inCnt+1), 3, "0", "L"));
				args2.put("userId"     , map.get("rgsUserId").toString());

				if ( map.get("memberNo") != null ) {
					List<Map<String, String>> list = memberMapper.selectMemberExt(map);
					if ( list.size() == 1 ) {
						args2.put("memberId", list.get(0).get("memberId").toString());
					}
				}

				if ( args2.get("memberId") == null ) {
					inCnt += moneymngMapper.insertMoneyMngByName(args2);
					
				} else {			
					inCnt += moneymngMapper.insertMoneyMngById(args2);		
				}
			}
			catch (Exception e) {
				mapRet.put("error", -1);
			}
		}
		mapRet.put("error", 1);
		mapRet.put("inCnt", inCnt);
		return mapRet;
	}

}
