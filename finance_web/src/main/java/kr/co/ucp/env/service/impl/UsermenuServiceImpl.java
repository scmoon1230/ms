package kr.co.ucp.env.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.env.service.UsermenuMapper;
import kr.co.ucp.env.service.UsermenuService;
import kr.co.ucp.wrks.sstm.menu.service.MenuGrpAuthMapper;
import kr.co.ucp.wrks.sstm.menu.service.MenuGrpAuthService;

@Service("usermenuService")
public class UsermenuServiceImpl implements UsermenuService {

	@Resource(name="usermenuMapper")
	private UsermenuMapper usermenuMapper;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 그룹권한별 프로그램 메뉴 메뉴 리스트
	@Override
	public List<Map<String, String>> menulist(Map<String, String> args) throws Exception {
		return usermenuMapper.menulist(args);
	}

	// 그룹권한별 프로그램 메뉴 수정
	@Override
	public int update_grpauth_menu(List<Map<String, String>> args) throws Exception {
		
    	for(int i = 0; i < args.size(); i++) {
    		Map<String, String> arg = (Map<String, String>) args.get(i);
    		
    		if ("Y".equalsIgnoreCase(arg.get("menuId").split(":")[1])) {	// 프로그램이 연결된 메뉴일 때
    			
    			String sgroup3 = arg.get("menuId").split(":")[0];
    			arg.put("sgroup3", sgroup3);
       			usermenuMapper.deleteUsermenu(arg);
       			
            	if(arg.get("yn").equals("Y") ) {
        			arg.put("sysGubun", "A");
        			arg.put("sgroup1", sgroup3.substring(0,1)+"000");
        			arg.put("sgroup2", sgroup3.substring(0,2)+"00");
           			usermenuMapper.insertUsermenu(arg);
        		}	
    		}
    	}
    	return 1;
		//return usermenuMapper.update_grpauth_menu(args);
	}

//	// 그룹권한별 프로그램 메뉴 복사
//	@Override
//	public int copy_grpauth_menu(Map<String, String> args) throws Exception {
//		return usermenuMapper.copy_grpauth_menu(args);
//	}

//	// 그룹권한별 프로그램 메뉴 지구코드 조회
//	@Override
//	public List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception {
//		return usermenuMapper.list_cm_dstrt_cd_mng(map);
//	}

//	// 그룹권한별 프로그램 메뉴 리스트
//	@Override
//	public List<Map<String, String>> userlist(Map<String, String> args) throws Exception {
//		return usermenuMapper.userlist(args);
//	}

}
