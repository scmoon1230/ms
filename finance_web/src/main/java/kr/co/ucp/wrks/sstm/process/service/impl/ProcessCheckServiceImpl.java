package kr.co.ucp.wrks.sstm.process.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.ucp.wrks.sstm.process.service.ProcessCheckMapper;
import kr.co.ucp.wrks.sstm.process.service.ProcessCheckService;

/**
 * ----------------------------------------------------------
 * @Class Name : ProcessCheckServiceImpl
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
@Service("processCheckService")
public class ProcessCheckServiceImpl extends EgovAbstractServiceImpl implements ProcessCheckService {
	@Resource(name="processCheckMapper")
    private ProcessCheckMapper processCheckMapper;
    /*
     * 검색
     */
	@Override
    public List<Map<String, String>> getList(Map<String, String> args) throws Exception {

    	List<Map<String, String>> list = processCheckMapper.list(args);

    	return list;
    }
}