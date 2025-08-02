package kr.co.ucp.wrks.sstm.usr.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommUtil;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.tvo.view.service.ViewMapper;
import kr.co.ucp.tvo.view.service.ViewSrchVO;
import kr.co.ucp.wrks.sstm.code.service.CodeDtcdMapper;
import kr.co.ucp.wrks.sstm.usr.service.UsrInfoMapper;
import kr.co.ucp.wrks.sstm.usr.service.UsrInfoService;

@Service("usrInfoService")
public class UsrInfoServiceImpl implements UsrInfoService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="codeDtcdMapper")
	private CodeDtcdMapper codeDtcdMapper;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="usrInfoMapper")
	private UsrInfoMapper usrInfoMapper;

	@Resource(name="viewMapper")
	private ViewMapper viewMapper;

	@Override
	public List<Map<String, String>> getCmGroupList(Map<String, String> args) throws Exception {
		return usrInfoMapper.selectCmGroupList(args);
	}
	
	@Override
	public List<Map<String, String>> getCmGrpUserList(Map<String, String> args) throws Exception {
		return usrInfoMapper.selectCmGrpUserList(args);
	}
	
	@Override
	public List<Map<String, String>> selectUserList(Map<String, String> args) throws Exception {

		// dbms 암호화함수 사용
	//	String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
	//	if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
	//		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
	//		args.put("saltText", saltText);
	//	}

		return usrInfoMapper.selectUserList(args);
	}

	@Override
	public List<Map<String, String>> selectAllUserList(Map<String, String> args) throws Exception {
		return usrInfoMapper.selectAllUserList(args);
	}

	@Override
	public Map<String, String> selectUserDtl(Map<String, String> args) throws Exception {

		// dbms 암호화함수 사용
		String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			args.put("saltText", saltText);
		}

		return usrInfoMapper.selectUserDtl(args);
	}

	@Override
	public int selectUserIdCnt(Map<String, Object> map) throws Exception {
		return usrInfoMapper.selectUserIdCnt(map);
	}
	
	@Override
	//public int insertUser(Map<String, Object> map, List<Map<String, String>> grplist, List<Map<String, String>> arealist, Map<String, String> dstrtMap) throws Exception {
	public int insertUser(Map<String, Object> map, List<Map<String, String>> grplist, Map<String, String> dstrtMap) throws Exception {
		
		int insertResult = usrInfoMapper.selectUserIdCnt(map);
		if (insertResult > 0){
			return -1;
		}

        String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
        if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
			map.put("saltText", saltText);
        }
        
		// 사용자 등록
		insertResult = usrInfoMapper.insertUserInfo(map);

		// 사용자 그룹 등록
		if(grplist.size() > 0) {
			for(int i=0; i<grplist.size(); i++) {
				Map<String, String> arg = grplist.get(i);
				insertResult = usrInfoMapper.insertCmGrpUser(arg);				
			}
		}

		// 사용자 지구 등록
		//if(dstrtMap.size() > 0) {
			//insertResult = usrInfoMapper.deleteUserDstrt(dstrtMap);
			//insertResult = usrInfoMapper.insertUserDstrt(dstrtMap);
		//}
		
		return insertResult;		
	}
	
	@Override
	public int insertMultiGrpUser(List<Map<String, String>> args) throws Exception {
		int result =  0;
		
		for(int i=0; i<args.size(); i++){
			Map<String, String> arg = (Map<String, String>)args.get(i);
			//result += delete("wrks_sstm_usr_info.insertCmGrpUser", arg);
			result += usrInfoMapper.insertCmGrpUser(arg);
		}
		return result;		
	}
	
	@Override
	public int update(Map<String, Object> map) throws Exception {
		int result = usrInfoMapper.updateUser(map);
		return result;		
	}

	@Override
	public int updateUser(Map<String, Object> map, List<Map<String, String>> grplist) throws Exception {

        String USER_INFO_CRYPTO_USE_YN = prprtsService.getString("USER_INFO_CRYPTO_USE_YN");
		logger.info("--> modifyUser(), USER_INFO_CRYPTO_USE_YN : {}", USER_INFO_CRYPTO_USE_YN);
        if ("Y".equalsIgnoreCase(USER_INFO_CRYPTO_USE_YN)) {	// 사용자 개인정보 암호화 사용할 때
			String saltText = prprtsService.getString("SALT_TEXT", "scmpworld");
    		map.put("saltText", saltText);
        }
		
		int result = usrInfoMapper.updateUserInfo(map);

		for(int i=0; i< grplist.size(); i++)
		{
			Map<String, String> arg = (Map<String, String>)grplist.get(i);
			result += usrInfoMapper.updateCmGrpUser(arg);
		}
		return result;		
	}
	
	@Override
	public int deleteUser(Map<String, Object> map) throws Exception {
		int r = 0;
		
		// 신청이력이 있는 지 확인
		String userId = map.get("USER_ID").toString();
		ViewSrchVO vo = new ViewSrchVO();
		vo.setViewRqstUserId(userId);
		int cnt =viewMapper.selectViewRqstListTotCnt(vo);
		//System.out.println("--> cnt ==> "+cnt);
		
		if ( cnt != 0 ) {
			return -1;
			
		} else {
			// 사용자 그룹 삭제
			Map<String, String> arg = new HashMap<String, String>();
			arg.put("USER_ID",userId);
			r = usrInfoMapper.deleteCmGrpUser(arg);

			// 사용자 지구 삭제
			//r = usrInfoMapper.deleteUserDstrt(arg);
			
			// 사용자 삭제
			r = usrInfoMapper.deleteUser(map);
		}
		
		return r;
	}
	
	@Override
	public int deleteMulti(List<Map<String, Object>> list) throws Exception {
		//return usrInfoMapper.deleteMulti(list);
		for(int i=0; i<list.size(); i++){
			Map<String, Object> arg = (Map<String, Object>)list.get(i);
			//delete("wrks_sstm_usr_info.delete", arg);
			//usrInfoMapper.delete2(arg);
			update(arg);
		}
		return 1;		
	}
	
	@Override
	public int deleteMultiGrpUser(List<Map<String, String>> args) throws Exception {
		//return usrInfoMapper.deleteMultiGrpUser(args);
		int result =  0;
		
		for(int i=0; i<args.size(); i++){
			Map<String, String> arg = (Map<String, String>)args.get(i);
			//result += delete("wrks_sstm_usr_info.deleteCmGrpUser", arg);
			result += usrInfoMapper.deleteCmGrpUser(arg);			
		}
		return result;		
	}
	
//	@Override
//	public int insertUserDstrt(Map<String, String> map) throws Exception {
//		return usrInfoMapper.insertUserDstrt(map);
//	}
	
//	@Override
//	public int deleteUserDstrt(Map<String, String> map) throws Exception {
//		return usrInfoMapper.deleteUserDstrt(map);
//	}
	
	@Override
	public List<Map<String, String>> getDstrtCd(Map<String, String> args) throws Exception {
		return usrInfoMapper.getDstrtCd(args);
	}
	
	@Override
	public List<Map<String, String>> getGrpId(Map<String, String> args) throws Exception {
		return usrInfoMapper.getGrpId(args);
	}
	
	@Override
	public List<Map<String, String>> getDstrtCdList(Map<String, String> map) throws Exception {
		return usrInfoMapper.getDstrtCdList(map);
	}

	@Override
	public Map<String, String> registerInsttInfo(Map<String, String> paraMap) throws Exception {

		Map<String, String> insttMap = new HashMap<String, String>();
		
		String insttNm = paraMap.get("insttNm").toString();
		String dstrtCd = paraMap.get("dstrtCd").toString();
		
		Map<String, String> args = new HashMap<String, String>();
		args.put("CD_GRP_ID", "USER_INSTT_NM");
		args.put("CD_TY", "C");
		args.put("USE_TY_CD", "Y");
		args.put("PRNT_GRP_ID", dstrtCd);
		args.put("CD_NM_KO", insttNm);
		List<Map<String, String>> insttList = codeDtcdMapper.selectUserInsttInfoList(args);
		
		if ( insttList.size()==0) {		// 등록되지 않은 기관명일 때
			String cdId = dstrtCd+"001";
			String sortOrder = "1";
			
			args.put("CD_NM_KO", null);
			insttList = codeDtcdMapper.selectUserInsttInfoList(args);
			
			if ( insttList.size() != 0 ) {		// 등록된 다른 기관명이 있을 때
				for ( int j=0 ; j<insttList.size() ; j++ ) {
					Map<String, String> lastInstt = insttList.get(j);
					String tmpCdId = lastInstt.get("cdId").toString();
					if ( tmpCdId.indexOf(dstrtCd) != -1 ) {		// 해당 지구일 때
						String tmpInd = tmpCdId.replace(dstrtCd, "");
						int ind = Integer.parseInt(tmpInd);
						int newInd = ind + 1;
						cdId = dstrtCd+CommUtil.setPad(String.valueOf(newInd), 3, "0", "L");
						sortOrder = String.valueOf(newInd);
					}
				}
			}
			
	    	LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
	    	String sesUserId = lgnVO.getUserId();

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("RGS_USER_ID", sesUserId);
			map.put("CD_GRP_ID", "USER_INSTT_NM");
			map.put("CD_ID", cdId);
			map.put("CD_NM_KO", insttNm);
			map.put("CD_NM_EN", "");
			map.put("USE_TY_CD", "Y");
			map.put("CD_DSCRT", "기관명");
			map.put("SORT_ORDR", sortOrder);
			
			map.put("PRNT_GRP_ID", cdId.substring(0,5));
			map.put("CHLD_GRP_ID", cdId.substring(5));
			
			codeDtcdMapper.insert(map);
		}

		args.put("ORDER_BY", "order by SORT_ORDR desc");
		List<Map<String, String>> insttList2 = codeDtcdMapper.selectUserInsttInfoList(args);
		if ( insttList2.size()!=0) {
			insttMap = insttList2.get(0);		// 새로 등록한 기관정보를 가져온다.
		}
		return insttMap;
		
	}
	
}
