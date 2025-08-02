package kr.co.ucp.wrks.sstm.process.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * ----------------------------------------------------------
 * @Class Name : ProcessCheckDAO
 * @Description : 프로세스체크 
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-03-25   설준환       최초작성
 * 
 * ----------------------------------------------------------
 * */
@Repository("processCheckDAO")
public class ProcessCheckDAO extends EgovAbstractMapper {
	/*
     * 검색
     */
    @SuppressWarnings("unchecked")
	public List<Map<String, String>>getList(Map<String, String> args) throws Exception {
    	return selectList("wrks_sstm_process_check.list", args);
    }
}