/**
 * -----------------------------------------------------------------
 *
 * @Class Name : WrkrptLogService.java
 * @Description :
 * @Version : 1.0 Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 *
 *               <pre>
 * -----------------------------------------------------------------
 * DATE            AUTHOR      DESCRIPTION
 * -----------------------------------------------------------------
 * 2017. 10. 31.   seungJun      New Write
 * -----------------------------------------------------------------
 *               </pre>
 */
package kr.co.ucp.cmm.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

import kr.co.ucp.cmm.LoginVO;

public interface CmmUserInfoService {

	LoginVO userConfigInfo(LoginVO loginVO, Map<String, String> argMap, HttpServletRequest request, ModelMap model) throws Exception;

}