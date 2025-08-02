package kr.co.ucp.env.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.egov.com.utl.fcc.service.EgovDateUtil;
import kr.co.ucp.env.service.MemberMapper;
import kr.co.ucp.env.service.MemberService;

@Service("memberService")
public class MemberServiceImpl implements MemberService {

	@Resource(name="memberMapper")
	private MemberMapper memberMapper;

	// 신도 조건검색
	@Override
	public List<Map<String, String>> selectMember(Map<String, String> args) throws Exception {
		return memberMapper.selectMember(args);
	}

	// 신도 입력
	@Override
	public int insertMember(Map<String, Object> args)  throws Exception {
		
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("stanYmd"  , EgovDateUtil.getToday());
		Map<String, String> newMemberIdMap = memberMapper.selectNewMemberId(map3);
		String memberId = String.valueOf(newMemberIdMap.get("memberId"));

		args.put("memberId", map3.get("stanYmd").toString()+CommUtil.setPad(memberId, 3, "0", "L"));
		return memberMapper.insertMember(args);
	}

	// 신도 수정
	@Override
	public int updateMember(Map<String, Object> args) throws Exception {
		return memberMapper.updateMember(args);
	}

	// 신도 삭제
	@Override
	public int deleteMember(Map<String, String> args) throws Exception {
		return memberMapper.deleteMember(args);
	}

//	// 신도 다중삭제
//	@Override
//	public int deleteMemberMulti(List<Map<String, String>> list) throws Exception{
//		for(int i=0; i<list.size(); i++){
//
//			Map<String, String> arg = (Map<String, String>)list.get(i);
//			memberMapper.deleteMember(arg);
//		}
//
//		return 1;
//	}
	
}
