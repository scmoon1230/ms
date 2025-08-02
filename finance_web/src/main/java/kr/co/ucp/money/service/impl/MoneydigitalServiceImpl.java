package kr.co.ucp.money.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.env.service.DigitalmoneyMapper;
import kr.co.ucp.env.service.MemberMapper;
import kr.co.ucp.money.service.MoneydigitalMapper;
import kr.co.ucp.money.service.MoneydigitalService;
import kr.co.ucp.money.service.MoneymngMapper;

@Service("moneydigitalService")
public class MoneydigitalServiceImpl implements MoneydigitalService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="memberMapper")
	private MemberMapper memberMapper;

	@Resource(name="moneymngMapper")
	private MoneymngMapper moneymngMapper;

	@Resource(name="digitalmoneyMapper")
	private DigitalmoneyMapper digitalmoneyMapper;

	@Resource(name="moneydigitalMapper")
	private MoneydigitalMapper moneydigitalMapper;

	// 조건검색
	@Override
	public List<Map<String, String>> selectMastUpload(Map<String, String> args) throws Exception {
		return moneydigitalMapper.selectMastUpload(args);
	}

	// 합계
	@Override
	public String selectTotalAmnt(Map<String, String> args) throws Exception {
		return moneydigitalMapper.selectTotalAmnt(args);
	}

	// 입력
	@Override
	public int insertMastUpload(Map<String, String> args)  throws Exception {
		return moneydigitalMapper.insertMastUpload(args);
	}

	// 수정
//	@Override
//	public int updateMastUpload(Map<String, Object> args) throws Exception {
//		return moneydigitalMapper.updateMastUpload(args);
//	}

	// 삭제
	@Override
	public int deleteMastUpload(Map<String, String> args) throws Exception {
		return moneydigitalMapper.deleteMastUpload(args);
	}

	// 다중삭제
	@Override
	public int deleteMastUploadMulti(List<Map<String, String>> list) throws Exception{
		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			moneydigitalMapper.deleteMastUpload(arg);
		}

		return 1;
	}

	// 엑셀업로드
	@Override
 	public Map<String, Object> excelUpload(List<Map<String, String>> args) {
		logger.debug("args.size => "+args.size());
		
		Map<String, Object> mapRet = new HashMap<String, Object>();

		int inCnt = 0;

		for (Map<String, String> map : args) {
			try {
				logger.debug("map({}) => {}",inCnt+1,map.toString());
				
				Map<String, String> args2 = new HashMap<String, String>();
				args2.put("stanYmd"    , map.get("stanYmd").toString());
				args2.put("stanGb"     , map.get("stanGb").toString());
				//args2.put("index"      , CommUtil.setPad(String.valueOf(inCnt+1), 3, "0", "L"));
				args2.put("seqNo"      , String.valueOf(inCnt+1));
				args2.put("moneyTime"  , map.get("moneyTime").toString());
				args2.put("memberName" , map.get("memberName").toString());
				args2.put("birthDay"   , map.get("birthDay").toString());
				args2.put("moneyAmt"   , map.get("moneyAmt").toString());
				args2.put("prayTitle"  , EgovStringUtil.nullConvert(map.get("prayTitle")).toString());
				args2.put("etcRemark"  , EgovStringUtil.nullConvert(map.get("etcRemark")).toString());
				args2.put("hpNo"       , EgovStringUtil.nullConvert(map.get("hpNo")).toString());
				args2.put("moneyType"  , map.get("moneyType").toString());
				//args2.put("termGb"     , map.get("termGb").toString());
				//args2.put("settleGb"   , map.get("settleGb").toString());
				args2.put("userId"     , map.get("rgsUserId").toString());
				args2.put("closeYn"    , "N");
				args2.put("memNumType" , "1");
				logger.debug("args2({}) => {}",inCnt+1,args2.toString());
				
				
				List<Map<String, String>> dmList = digitalmoneyMapper.selectDigitalmoneyExt(args2);
				if ( dmList.size() == 1 ) {
					args2.put("moneyName1", dmList.get(0).get("moneyName").toString());
					args2.put("moneyCode", dmList.get(0).get("moneyCode").toString());
				}
				
				//if ( map.get("memberNo") != null ) {
				//	List<Map<String, String>> mamList = memberMapper.selectMemberExt(map);
				//	if ( mamList.size() == 1 ) {
				//		args2.put("memberId", mamList.get(0).get("memberId").toString());
				//	}
				//}

				//if ( args2.get("memberId") != null ) {		
				//}
				logger.debug("args2({}) => {}",inCnt+1,args2.toString());
				inCnt += moneydigitalMapper.insertMastUpload(args2);
			}
			catch (Exception e) {
				e.printStackTrace();
				mapRet.put("error", -1);
			}
		}
		mapRet.put("error", 1);
		mapRet.put("inCnt", inCnt);
		return mapRet;
	}

	// 다중등록처리
	@Override
	public int insertMastMoneyMulti(List<Map<String, String>> list) throws Exception{

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		for(int i=0; i<list.size(); i++){
			Map<String, String> arg = (Map<String, String>)list.get(i);

			Map<String, String> arg2 = moneydigitalMapper.selectMastUploadOne(arg);
			arg2.put("stanYmd", arg2.get("stanYmd").replaceAll("-", ""));

			String tmp = arg2.get("birthDay");
			String memberNo = tmp.substring(2,4)+tmp.substring(5,7)+tmp.substring(8,10);
			arg.put("memberNo", memberNo);
			arg.put("memberName", arg2.get("memberName"));
			arg.put("stanYmd", arg2.get("stanYmd"));
			arg.put("moneyCode", arg2.get("moneyCode"));
			
			List<Map<String, String>> mamList = memberMapper.selectMemberExt(arg);	// 신도정보를 검색한다.
			
			if ( mamList.size() != 1 ) {	// 신도정보에 없을 때
				Map<String, String> map3 = new HashMap<String, String>();
				map3.put("stanYmd"  , EgovDateUtil.getToday());
				Map<String, String> newMemberIdMap = memberMapper.selectNewMemberId(map3);
				String memberId = String.valueOf(newMemberIdMap.get("memberId"));

				Map<String, Object> argMem = new HashMap<String, Object>();
				argMem.put("memberId", map3.get("stanYmd").toString()+CommUtil.setPad(memberId, 3, "0", "L"));
				argMem.put("memberNo", arg.get("memberNo"));
				argMem.put("memberName", arg.get("memberName"));
				argMem.put("useYn", "Y");	// 사용여부
				memberMapper.insertMember(argMem);	// 신도정보를 등록한다.
			}
			mamList = memberMapper.selectMemberExt(arg);
			
			if ( mamList.size() == 1 ) {	// 신도정보에 있을 때
				arg2.put("memberId", mamList.get(0).get("memberId").toString());
				arg2.put("worshipCode", "020");		// 디지털헌금
				arg2.put("userId"     , sesUserId);
				arg2.put("index"      , CommUtil.setPad(String.valueOf(i+1), 3, "0", "L"));
				String detSeq = moneydigitalMapper.selectNewDetSeq(arg);
				arg2.put("detSeq"     , detSeq);

				int rtn = moneydigitalMapper.insertMastMoney(arg2);	// 헌금으로 등록한다.
				if ( rtn == 1 ) {
					arg.put("memberId", arg2.get("memberId"));
					arg.put("detSeq", detSeq);
					arg.put("closeYn", "Y");		// 헌금등록여부
					rtn = moneydigitalMapper.updateMastUpload(arg);
				}
			}
		}

		return 1;
	}

	// 다중등록취소
	@Override
	public int deleteMastMoneyMulti(List<Map<String, String>> list) throws Exception{

		for(int i=0; i<list.size(); i++){

			Map<String, String> arg = (Map<String, String>)list.get(i);
			
			Map<String, String> arg2 = moneydigitalMapper.selectMastUploadOne(arg);
			arg2.put("stanYmd", arg2.get("stanYmd").replaceAll("-", ""));

			arg.put("stanYmd"   , arg2.get("stanYmd").toString());			
			arg.put("moneyCode" , arg2.get("moneyCode").toString());
			arg.put("detSeq"    , String.valueOf(arg2.get("detSeq")));

			Map<String, String> mm = moneymngMapper.selectMastMoneyOne(arg);
			if ( mm != null ) {
				if ( "Y".equalsIgnoreCase(mm.get("closeYn").toString())) {
					return 0;
					
				} else {
					int rtn = moneymngMapper.deleteMoneyMng(arg);
					if ( rtn == 1 ) {
						arg.put("memberId", "");
						arg.put("detSeq", "");
						arg.put("closeYn", "N");		// 헌금등록여부
						rtn = moneydigitalMapper.updateMastUpload(arg);
					}
				}
			}
			
		}

		return 1;
	}

}
