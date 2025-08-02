/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : MngIconController.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2018. 09. 05. SaintJuny 최초작성
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.mng.web;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.CommonVO;
import kr.co.ucp.mntr.cmm.util.CommonUtil;
import kr.co.ucp.mntr.mng.service.MngIconService;

@Controller
public class MngIconController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ServletContext servletContext;

	@Resource(name="mngIconService")
	private MngIconService mngIconService;

	@Resource(name="commonUtil")
	private CommonUtil commonUtil;


	@RequestMapping(value="/mntr/mngIcon.do")
	public String viewMngIcon(@ModelAttribute CommonVO vo, ModelMap model) throws Exception {
		
//		private String[] commonData = { "아이콘 설정", "mng", "mngIcon" };
//		commonUtil.setCommonVOData(vo, 0, commonData);
//		model.addAttribute("common", vo);
		
		return "nomapL/mng/mngIcon";
	}

	@RequestMapping(value="/mntr/iconList.json", method = RequestMethod.POST)
	public ModelAndView selectIconList(ModelMap model, @RequestParam Map<String, String> params) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");

		List<EgovMap> list = mngIconService.selectIconList(params);
		mav.addObject("list", list);
		return mav;
	}

	@RequestMapping(value="/mntr/iconUpd.json", method = RequestMethod.POST)
	public ModelAndView uploadIconImg(MultipartHttpServletRequest req, HttpServletResponse res, ModelAndView model, SessionStatus status) throws Exception {
		ModelAndView mav = new ModelAndView("jsonView");
		try {
			Iterator<String> oIterator = req.getFileNames();
			MultipartFile oFile = req.getFile(oIterator.next());

			if (oFile != null && oFile.getSize() > 0) {
				String sOriginalFilename = oFile.getOriginalFilename().toUpperCase();
				String sContentType = oFile.getContentType();

				// 1. 파일 크기 체크 ( 1MB 이하 )
				long nMaxSize = 1000000;
				long nSize = oFile.getSize();

				if (nMaxSize < nSize) {
					mav.addObject("msg", "아이콘 업로드 실패. 1MB 이하의 PNG 이미지 파일만 업로드 가능합니다.");
					return mav;
				}

				// 2. 파일 확장자 체크
				boolean isAcceptableFileExtension = sOriginalFilename.endsWith(".PNG");
				if (!isAcceptableFileExtension || !"image/png".equals(sContentType)) {
					mav.addObject("msg", "아이콘 업로드 실패. PNG 이미지 파일만 업로드 가능합니다.");
					return mav;
				}

				// 3. 아이콘 구분 체크
				String sIconGbn = EgovStringUtil.nullConvert(req.getParameter("iconGbn"));
				// 4. 아이콘 타입 체크
				String sIconTy = EgovStringUtil.nullConvert(req.getParameter("iconTy"));
				String sIconFileNm = EgovStringUtil.nullConvert(req.getParameter("iconFileNm"));
				StringBuffer sbPath = new StringBuffer(servletContext.getRealPath("images/mntr/gis/"));

				logger.info("originalFilename: {}, iconGbn: {}, iconTy: {}, iconFileNm: {}, sbPath: {}", sOriginalFilename, sIconGbn, sIconTy, sIconFileNm, sbPath.toString());

				if(!"".equals(sIconGbn) && !"".equals(sIconTy) && !"".equals(sIconFileNm) && !"".equals(sbPath.toString())) {
					sbPath.append("\\");
					sbPath.append(sIconGbn);
					sbPath.append("\\");
					sbPath.append(sIconTy);
					sbPath.append("\\");
					sbPath.append(sIconFileNm);
					sbPath.append(".png");

					String sOsName = System.getProperty("os.name").toLowerCase();
					if(sOsName.indexOf("win") < 0) {	sbPath = new StringBuffer(sbPath.toString().replace("\\", "/"));
					} else {							sbPath = new StringBuffer(sbPath.toString().replace("/", "\\"));
					}

					File f = new File(sbPath.toString());
					oFile.transferTo(f);
					logger.info("--> FilePath: {}, {}", sbPath.toString(), sContentType);
					mav.addObject("msg", "아이콘 업로드가 완료되었습니다.");
				}
				else {
					mav.addObject("msg", "아이콘 업로드 실패. 누락된 파라메터가 있습니다.");
					return mav;
				}
			} else {
				mav.addObject("msg", "아이콘 업로드 실패. 파일이 없거나 사이즈가 0입니다.");
				return mav;
			}
		} catch (Exception e) {
			logger.error("--> Icon Upload Error : " + e.getMessage());
		}

		return mav;
	}
}
