package kr.co.ucp.wrks.sstm.menu.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import kr.co.ucp.wrks.sstm.menu.service.MenuGrpAuthMapper;
import kr.co.ucp.wrks.sstm.menu.service.MenuGrpAuthService;

@Service("menuGrpAuthService")
public class MenuGrpAuthServiceImpl implements MenuGrpAuthService {

	@Resource(name="menuGrpAuthMapper")
	private MenuGrpAuthMapper menuGrpAuthMapper;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	// 그룹권한별 프로그램 메뉴 지구코드 조회
	@Override
	public List<Map<String, String>> getCM_DSTRT_CD_MNG(Map<String, String> map) throws Exception {
		return menuGrpAuthMapper.list_cm_dstrt_cd_mng(map);
	}

	// 그룹권한별 프로그램 메뉴 리스트
	@Override
	public List<Map<String, String>> list_grpauth(Map<String, String> args) throws Exception {
		return menuGrpAuthMapper.list_grpauth(args);
	}

	// 그룹권한별 프로그램 메뉴 메뉴 리스트
	@Override
	public List<Map<String, String>> list_grpauth_menu(Map<String, String> args) throws Exception {
		return menuGrpAuthMapper.list_grpauth_menu(args);
	}

	// 그룹권한별 프로그램 메뉴 수정
	@Override
	public int update_grpauth_menu(List<Map<String, String>> args) throws Exception {
    	for(int i = 0; i < args.size(); i++) {

    		Map<String, String> arg = (Map<String, String>) args.get(i);
    		Map<String, String> resultMap =  (Map<String, String>) menuGrpAuthMapper.get_grpId(arg);

    		//if(arg.get("RGS_AUTH_YN").equals("N") && arg.get("SEA_AUTH_YN").equals("N") && arg.get("UPD_AUTH_YN").equals("N") && arg.get("DEL_AUTH_YN").equals("N")) {
        	if(arg.get("RGS_AUTH_YN").equals("N") ) {
    			if(resultMap != null) {
        			menuGrpAuthMapper.delete_grpauth_menu(arg);
    			}
    		}
    		else {
        		if(resultMap == null)
            		//insert("wrks_sstm_menu_grpauth.insert", arg);
        			menuGrpAuthMapper.insert_grpauth_menu(arg);
        		else {
            		if(!(
            			arg.get("RGS_AUTH_YN").equals(resultMap.get("RGS_AUTH_YN"))
            			&& arg.get("SEA_AUTH_YN").equals(resultMap.get("SEA_AUTH_YN"))
            			&& arg.get("UPD_AUTH_YN").equals(resultMap.get("UPD_AUTH_YN"))
            			&& arg.get("DEL_AUTH_YN").equals(resultMap.get("DEL_AUTH_YN"))
            			)
            		){
            			//update("wrks_sstm_menu_grpauth.update", arg);
            			menuGrpAuthMapper.update_grpauth_menu(arg);
            		}

        		}
    		}
    	}
    	return 1;
		//return menuGrpAuthMapper.update_grpauth_menu(args);
	}

	// 그룹권한별 프로그램 메뉴 복사
	@Override
	public int copy_grpauth_menu(Map<String, String> args) throws Exception {
		return menuGrpAuthMapper.copy_grpauth_menu(args);
	}
}




