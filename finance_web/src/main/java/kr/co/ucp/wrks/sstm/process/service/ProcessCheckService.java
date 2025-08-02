package kr.co.ucp.wrks.sstm.process.service;

import java.util.List;
import java.util.Map;

/**
 * ----------------------------------------------------------
 * @Class Name : ProcessCheckService
 * @Description : 프로세스체크
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015-03-26   설준환       최초작성
 *
 * ----------------------------------------------------------
 * */
public interface ProcessCheckService {

	List<Map<String, String>> getList(Map<String, String> args)throws Exception;
}