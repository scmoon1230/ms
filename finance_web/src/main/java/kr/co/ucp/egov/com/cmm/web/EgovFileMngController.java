package kr.co.ucp.egov.com.cmm.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.co.ucp.cmm.interceptor.AuthenticInterceptor;
import kr.co.ucp.cmm.service.FileVO;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.cmm.service.EgovFileMngService;
import kr.co.ucp.egov.com.cmm.service.EgovFileMngUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 파일 조회, 삭제, 다운로드 처리를 위한 컨트롤러 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.3.25  이삼섭          최초 생성
 *   2016.10.13 장동한           deleteFileInf 메소드 return 방식 수정
 *
 * </pre>
 */
@Controller
public class EgovFileMngController {
	static Logger logger = LoggerFactory.getLogger(AuthenticInterceptor.class);

    @Resource(name="EgovFileMngService")
    private EgovFileMngService fileService;
    
    @Resource(name="EgovFileMngUtil")
    private EgovFileMngUtil fileUtil;

    /**
     * 첨부파일에 대한 목록을 조회한다.
     *
     * @param fileVO
     * @param atchFileId
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cmm/fms/selectFileInfs.do")
    public String selectFileInfs(@ModelAttribute("searchVO") FileVO fileVO, @RequestParam Map<String, Object> commandMap, ModelMap model) throws Exception {
	String atchFileId = (String)commandMap.get("param_atchFileId");

		fileVO.setAtchFileId(atchFileId);
	List<FileVO> result = fileService.selectFileInfs(fileVO);

	model.addAttribute("fileList", result);
	model.addAttribute("updateFlag", "N");
	model.addAttribute("fileListCnt", result.size());
	model.addAttribute("atchFileId", atchFileId);

	return "egov/com/cmm/fms/EgovFileList";
    }

    /**
     * 첨부파일 변경을 위한 수정페이지로 이동한다.
     *
     * @param fileVO
     * @param atchFileId
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cmm/fms/selectFileInfsForUpdate.do")
    public String selectFileInfsForUpdate(@ModelAttribute("searchVO") FileVO fileVO, @RequestParam Map<String, Object> commandMap,
	    //SessionVO sessionVO,
	    ModelMap model) throws Exception {

	String atchFileId = (String)commandMap.get("param_atchFileId");

	fileVO.setAtchFileId(atchFileId);

	List<FileVO> result = fileService.selectFileInfs(fileVO);

	model.addAttribute("fileList", result);
	model.addAttribute("updateFlag", "Y");
	model.addAttribute("fileListCnt", result.size());
	model.addAttribute("atchFileId", atchFileId);

	return "egov/com/cmm/fms/EgovFileList";
    }

    /**
     * 첨부파일에 대한 삭제를 처리한다.
     *
     * @param fileVO
     * @param returnUrl
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cmm/fms/deleteFileInfs.do")
    public String deleteFileInf(@ModelAttribute("searchVO") FileVO fileVO,
	    //SessionVO sessionVO,
	    HttpServletRequest request,
	    ModelMap model) throws Exception {

		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		if (isAuthenticated) {
		    fileService.deleteFileInf(fileVO);
		}

		 return "blank";
		 
		//--------------------------------------------
		// contextRoot가 있는 경우 제외 시켜야 함
		//--------------------------------------------
		////return "forward:/cmm/fms/selectFileInfs.do";
		//return "forward:" + returnUrl;
		/* *******************************************************
		 *  modify by jdh
		 *******************************************************
		if ("".equals(request.getContextPath()) || "/".equals(request.getContextPath())) {
		    return "forward:" + returnUrl;
		}

		if (returnUrl.startsWith(request.getContextPath())) {
		    return "forward:" + returnUrl.substring(returnUrl.indexOf("/", 1));
		} else {
		    return "forward:" + returnUrl;
		}
		*/
		////------------------------------------------
    }
    @RequestMapping(value="/egov/com/cmm/fms/deleteFileBbs.json")
	@ResponseBody
    public Map<String, Object> deleteFileBbs(@ModelAttribute("searchVO") FileVO fileVO,
	    //SessionVO sessionVO,
	    HttpServletRequest request,
	    ModelMap model) throws Exception {

		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

		Map<String, Object> mapRet = new HashMap<String, Object>();
		if (isAuthenticated) {

			try {
				FileVO fvo = fileService.selectFileInf(fileVO);

				fileUtil.deleteFileBbs(fvo.getStreFileNm());

				fileService.deleteFileInf(fileVO);
			}catch(Exception e){
				logger.error("deleteFileBbs Exception : {}", e.getMessage());
	  	  		mapRet.put("msg", "처리중 에러가 발생했습니다.");
	  	  		return mapRet;
			}
		}
  		mapRet.put("session", 1);
  		mapRet.put("msg", "삭제하였습니다.");
  		return mapRet;
	  		
//		 return "blank";
		 
		//--------------------------------------------
		// contextRoot가 있는 경우 제외 시켜야 함
		//--------------------------------------------
		////return "forward:/cmm/fms/selectFileInfs.do";
		//return "forward:" + returnUrl;
		/* *******************************************************
		 *  modify by jdh
		 *******************************************************
		if ("".equals(request.getContextPath()) || "/".equals(request.getContextPath())) {
		    return "forward:" + returnUrl;
		}

		if (returnUrl.startsWith(request.getContextPath())) {
		    return "forward:" + returnUrl.substring(returnUrl.indexOf("/", 1));
		} else {
		    return "forward:" + returnUrl;
		}
		*/
		////------------------------------------------
    }

    /**
     * 이미지 첨부파일에 대한 목록을 조회한다.
     *
     * @param fileVO
     * @param atchFileId
     * @param sessionVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cmm/fms/selectImageFileInfs.do")
    public String selectImageFileInfs(@ModelAttribute("searchVO") FileVO fileVO, @RequestParam Map<String, Object> commandMap,
	    //SessionVO sessionVO,
	    ModelMap model) throws Exception {

	String atchFileId = (String)commandMap.get("atchFileId");

	fileVO.setAtchFileId(atchFileId);
	List<FileVO> result = fileService.selectImageFileList(fileVO);

	model.addAttribute("fileList", result);

	return "egov/com/cmm/fms/EgovImgFileList";
    }
}
