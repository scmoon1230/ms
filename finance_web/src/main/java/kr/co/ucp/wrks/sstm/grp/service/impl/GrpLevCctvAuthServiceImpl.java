/**
 * -----------------------------------------------------------------
 * @Class Name : GrpLevCctvAuthServiceImpl.java
 * @Description : 
 * @Version : 1.0
 * Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * -----------------------------------------------------------------
 * DATE           AUTHOR      DESCRIPTION
 * -----------------------------------------------------------------
 * 2018. 4. 9.   seungJun      New Write
 * -----------------------------------------------------------------
 */

package kr.co.ucp.wrks.sstm.grp.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import kr.co.ucp.wrks.sstm.grp.service.GrpLevCctvAuthService;

import org.springframework.stereotype.Service;

@Service("grpLevCctvAuthService")
public class GrpLevCctvAuthServiceImpl implements GrpLevCctvAuthService
{
	@Resource(name="grpLevCctvAuthDAO")
	private GrpLevCctvAuthDAO grpLevCctvAuthDAO;

	@Override
	public List<HashMap<String, String>> grpList() throws Exception {
		return grpLevCctvAuthDAO.grpList();
	}

	@Override
	public List<HashMap<String, String>> grpAuthlist(HashMap<String, String> args) throws Exception {
		return grpLevCctvAuthDAO.grpAuthlist(args);
	}

	@Override
	public List<HashMap<String, String>> list(HashMap<String, String> args) throws Exception {
		return grpLevCctvAuthDAO.list(args);
	}

	@Override
	public int insert(HashMap<String, Object> args) throws Exception {
		return grpLevCctvAuthDAO.insert(args);
	}

	@Override
	public int update(HashMap<String, Object> args) throws Exception {
		return grpLevCctvAuthDAO.update(args);
	}

	@Override
	public int delete(HashMap<String, Object> args) throws Exception {
		return grpLevCctvAuthDAO.delete(args);
	}
}
