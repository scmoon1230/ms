package kr.co.ucp.tvo.cmn.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.FcltVO;
import kr.co.ucp.mntr.gis.service.GeoDataService;
import kr.co.ucp.tvo.out.service.OutService;
import kr.co.ucp.tvo.view.service.ViewService;


@Controller
public class TvoCmnController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ServletContext servletContext;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	@Resource(name="viewService")
	private ViewService viewService;

	@Resource(name="outService")
	private OutService outService;

	@Resource(name="geoDataService")
	private GeoDataService geoDataService;

	@RequestMapping(value="/tvo/viewTargetDivision.do", method = RequestMethod.POST)
	public String viewDivision(@RequestParam Map<String, String> params, ModelMap model, HttpServletRequest request) throws Exception {
		String referer = request.getHeader("referer");
		int index = referer.lastIndexOf("/");
		String subMenu = referer.substring(index + 1);
		subMenu = subMenu.replace(".do", "");
		
		String target = EgovStringUtil.nullConvert(params.get("target"));
		String targetPath = "blank/layout/div/gnrNotFound";
		if (!"".equals(target)) {
			String[] temp = target.split("/");

			if (temp.length > 0) {
				targetPath = target;
				if ("tvoBlank/view/div/viewRqstReg".equals(target)) {
					String viewRqstNo = EgovStringUtil.nullConvert(params.get("viewRqstNo"));
					if (!"".equals(viewRqstNo)) {
						params.put("tvoPrgrsCd", "10");		// 신청
						EgovMap result = viewService.selectViewRqstDtl(params);
						model.addAttribute("viewRqstDtl", result);
					}
				} else if ("tvoBlank/view/div/viewRqstDtl".equals(target) || "tvoBlank/view/div/viewRqstVdo".equals(target)) {
					String viewRqstNo = EgovStringUtil.nullConvert(params.get("viewRqstNo"));
					if (!"".equals(viewRqstNo)) {
						EgovMap result = viewService.selectViewRqstDtl(params);
						model.addAttribute("viewRqstDtl", result);
					} else {
						model.addAttribute("msg", "잘못된 열람신청번호입니다.");
					}
				} else if ("tvoBlank/view/div/viewExtnDtl".equals(target)) {
					String viewRqstNo = EgovStringUtil.nullConvert(params.get("viewRqstNo"));
					String viewExtnRqstYmdhms = EgovStringUtil.nullConvert(params.get("viewExtnRqstYmdhms"));
					if (!"".equals(viewRqstNo) && !"".equals(viewExtnRqstYmdhms)) {
						EgovMap result = viewService.selectViewExtnDtl(params);
						model.addAttribute("viewExtnDtl", result);
					} else {
						model.addAttribute("msg", "잘못된 열람연장신청번호입니다.");
					}
				} else if ("tvoBlank/out/div/outRqstDtl".equals(target)) {
					String outRqstNo = EgovStringUtil.nullConvert(params.get("outRqstNo"));
					if (!"".equals(outRqstNo)) {
						EgovMap result = outService.selectOutRqstDtl(params);
						model.addAttribute("outRqstDtl", result);
					} else {
						model.addAttribute("msg", "잘못된 반출신청번호입니다.");
					}
				} else if ("tvoBlank/out/div/outExtnDtl".equals(target)) {
					String outRqstNo = EgovStringUtil.nullConvert(params.get("outRqstNo"));
					String outExtnRqstYmdhms = EgovStringUtil.nullConvert(params.get("outExtnRqstYmdhms"));
					if (!"".equals(outRqstNo) && !"".equals(outExtnRqstYmdhms)) {
						EgovMap result = outService.selectOutExtnDtl(params);
						model.addAttribute("outProdExtn", result);
					} else {
						model.addAttribute("msg", "잘못된 반출연장신청번호입니다.");
					}
				}
			} else {
				targetPath = "blank/layout/div/" + target;
			}
		}
		model.addAttribute("subMenu", subMenu);
		logger.info("--> DIV Target Path: {}, {}", targetPath, subMenu);
		return targetPath;
	}

	@RequestMapping(value="/tvo/castNetCctvList.json", method = RequestMethod.POST)
	public @ResponseBody Map<String, Object> getCastNetCctvList(@ModelAttribute FcltVO vo, ModelMap model, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = geoDataService.selectCastNetCctvList(vo);

		Object obj = resultMap.get("cctv");
		if (obj != null) {
			@SuppressWarnings("unchecked")
			List<EgovMap> cctvList = (List<EgovMap>) resultMap.get("cctv");
			resultMap.put("totalPages", 1);
			resultMap.put("totalRows", cctvList.size());
			resultMap.put("rows", cctvList);
			resultMap.put("page", 1);
		} else {
			resultMap.put("totalPages", 1);
			resultMap.put("totalRows", 0);
			resultMap.put("rows", 0);
			resultMap.put("page", 1);
		}
		return resultMap;
	}

//	@RequestMapping(value="/tvo/openVlcPlayer.do")
//	public String openVlcPlayer(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request) throws Exception {
//		String outFileTy = EgovStringUtil.nullConvert(params.get("outFileTy"));
//
//		EgovMap egovMap = outService.selectOutFileDtl(params);
//		
//		String rootPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/"+config.getProperty("Globals.dirCctv");
//		String outFilePath = EgovStringUtil.nullConvert(egovMap.get("outFilePath"));
//
//		StringBuffer url = new StringBuffer();
//		url.append(rootPath);
//		url.append(outFilePath);
//		url.append(File.separator);
//
//		if ("0".equals(outFileTy)) {		// 최초
//			String outFileNm = EgovStringUtil.nullConvert(egovMap.get("outFileNm"));
//			url.append(outFileNm);
//		} else if ("1".equals(outFileTy)) {	// 연장
//			String outFileNmMsk = EgovStringUtil.nullConvert(egovMap.get("outFileNmMsk"));
//			url.append(outFileNmMsk);
//		}
//
//		String filePath = url.toString();
//		logger.info("--> openVlcPlayer url: {}", filePath);
//		String sOsName = System.getProperty("os.name");			//logger.info("--> sOsName : {}", sOsName);
//		if (sOsName.toLowerCase().indexOf("win") < 0) {	filePath = filePath.replace("\\", "/");	}
//		else {											filePath = filePath.replace("/", "\\");	}
//		logger.info("--> openVlcPlayer url: {}", filePath);
//		
//		model.addAttribute("url", filePath);
//
//		return "tvoBlank/cmn/vlcPlayer";
//	}

	@RequestMapping(value="/tvo/openHtmlPlayer.do")
	public String openHtmlPlayer(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request) throws Exception {
		String outFileTy = EgovStringUtil.nullConvert(params.get("outFileTy"));

		EgovMap egovMap = outService.selectOutFileDtl(params);
		
		String rootPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
		String outFilePath = EgovStringUtil.nullConvert(egovMap.get("outFilePath"));
		
		StringBuffer path = new StringBuffer();
		path.append(rootPath);
		path.append(prprtsService.getString("DIR_CCTV"));
		path.append("/contents");
		path.append("/"+outFilePath+"/");
		
		if ("ORG".equals(outFileTy)) {		// 원본파일
			String outFileNm = EgovStringUtil.nullConvert(egovMap.get("outFileNm"));
			path.append(outFileNm);
		} else if ("MP4".equals(outFileTy)) {	// MP4파일
			String outFileNmMp4 = EgovStringUtil.nullConvert(egovMap.get("outFileNmMp4"));
			path.append(outFileNmMp4);
		} else if ("MSK".equals(outFileTy)) {	// 마스킹파일
			String outFileNmMsk = EgovStringUtil.nullConvert(egovMap.get("outFileNmMsk"));
			path.append(outFileNmMsk);
		}
		String filePath = path.toString();

		logger.info("--> openHtmlPlayer videoUrl: {}", filePath);
		
		model.addAttribute("videoUrl", filePath);
		
		// 영상시작시각
		String fr = EgovStringUtil.nullConvert(params.get("vdoYmdhmsFr"));
		String startTime = fr.substring(0,4)+"-"+fr.substring(4,6)+"-"+fr.substring(6,8)+" "+fr.substring(8,10)+":"+fr.substring(10,12)+":"+fr.substring(12,14);
		model.addAttribute("startTime", startTime);
		
		return "tvoBlank/cmn/htmlPlayer";
	}

	// 공문 다운로드
	@RequestMapping(value="/tvo/downloadPaperFile.do", method = RequestMethod.POST)
	public ModelAndView downloadPaperFile(ModelMap model, @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.info("--> downloadPaperFile() params => {}", params.toString());
		
		ModelAndView mav = null;
		String viewRqstYmdhms = EgovStringUtil.nullConvert(params.get("viewRqstYmdhms"));
		String paperFileNm = EgovStringUtil.nullConvert(params.get("paperFileNm"));
		
		if ("".equals(paperFileNm)) {
			mav = new ModelAndView("jsonView");
			mav.addObject("msg", "파일이 존재하지 않습니다.");
			
		} else {
			String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_DOC");
			String filePath = rootPath + File.separator + viewRqstYmdhms.substring(0,4) + File.separator + viewRqstYmdhms.substring(4,8) + File.separator + paperFileNm;

			String sOsName = System.getProperty("os.name").toLowerCase();
			if (sOsName.indexOf("win") < 0) {	filePath = filePath.replace("\\", "/");	}
			else {								filePath = filePath.replace("/", "\\");	}
			logger.info("--> downloadPaperFile() {}", filePath);
			
			File file = new File(filePath);
			if (file.exists()) {
				mav = new ModelAndView("downloadView", "downloadFile", file);
			//	mav = new ModelAndView("jsonView");
			//	downloadFile(file, request, response);
			} else {
				mav = new ModelAndView("jsonView");
				mav.addObject("msg", "파일이 존재하지 않습니다.");
			}
		}
		return mav;
	}

	// 암호화영상 다운로드
	@RequestMapping(value="/tvo/downloadDrmFile.do")
	public ModelAndView downloadDrmFile(ModelMap model, @RequestParam Map<String, String> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = null;
		
		EgovMap egovMap = outService.selectOutFileDtl(params);
		
		if (egovMap == null) {
			mav = new ModelAndView("jsonView");
			mav.addObject("msg", "파일이 존재하지 않습니다.");
			
		} else {
			String outFilePath = EgovStringUtil.nullConvert(egovMap.get("outFilePath"));
			String outFileNmDrm = EgovStringUtil.nullConvert(egovMap.get("outFileNmDrm"));
			
			//String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
			//rootPath = rootPath + prprtsService.getString("FTP_VDO_DIR");
			String DIR_CCTV_CONTENTS = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV_CONTENTS");
			
			String filePath = DIR_CCTV_CONTENTS + File.separator + outFilePath + File.separator + outFileNmDrm;

			String sOsName = System.getProperty("os.name").toLowerCase();
			if (sOsName.indexOf("win") < 0) {	filePath = filePath.replace("\\", "/");	}
			else {								filePath = filePath.replace("/", "\\");	}
			logger.info("--> downloadDrmFile : {}", filePath);
			
			File file = new File(filePath);
			if (file.exists()) {
				outService.insertOutFileDownloadHis(params);
				mav = new ModelAndView("downloadView", "downloadFile", file);
			//	mav = new ModelAndView("drmDownload", "downloadFile", file);
			//	mav = new ModelAndView("jsonView");
			//	downloadFile(file, request, response);
			} else {
				mav = new ModelAndView("jsonView");
				mav.addObject("msg", "파일이 존재하지 않습니다.");
			}
		}
		return mav;
	}

	// 플레이어 다운로드
	@RequestMapping(value="/tvo/downloadPlayerFile.do", method = RequestMethod.POST)
	public ModelAndView downloadPlayerFile(ModelMap model, @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = null;

		String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		String filePath = rootPath + File.separator + "Markany_DRM_Player.zip";

		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	filePath = filePath.replace("\\", "/");	}
		else {								filePath = filePath.replace("/", "\\");	}
		logger.info("--> downloadPlayerFile() {}", filePath);
		
		File file = new File(filePath);
		if (file.exists()) {
			mav = new ModelAndView("downloadView", "downloadFile", file);
		//	mav = new ModelAndView("jsonView");
		//	downloadFile(file, request, response);
		} else {
			mav = new ModelAndView("jsonView");
			mav.addObject("msg", "파일이 존재하지 않습니다.");
		}
		return mav;
	}

	// 마스킹툴 다운로드
	@RequestMapping(value="/tvo/downloadMaskerFile.do", method = RequestMethod.POST)
	public ModelAndView downloadMaskerFile(ModelMap model, @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = null;

		String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		String filePath = rootPath + File.separator + "Markany_Masking_Tool.zip";

		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	filePath = filePath.replace("\\", "/");	}
		else {								filePath = filePath.replace("/", "\\");	}
		logger.info("--> downloadMaskerFile() {}", filePath);
		
		File file = new File(filePath);
		if (file.exists()) {
			mav = new ModelAndView("downloadView", "downloadFile", file);
		//	mav = new ModelAndView("jsonView");
		//	downloadFile(file, request, response);
		} else {
			mav = new ModelAndView("jsonView");
			mav.addObject("msg", "파일이 존재하지 않습니다.");
		}
		return mav;
	}

	// 브라우저 다운로드
	@RequestMapping(value="/tvo/downloadChromeFile.do", method = RequestMethod.POST)
	public ModelAndView downloadChromeFile(ModelMap model, @RequestParam Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = null;

		String rootPath = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_CCTV");
		String filePath = rootPath + File.separator + "ChromeStandaloneSetup64.zip";

		String sOsName = System.getProperty("os.name").toLowerCase();
		if (sOsName.indexOf("win") < 0) {	filePath = filePath.replace("\\", "/");	}
		else {								filePath = filePath.replace("/", "\\");	}
		logger.info("--> downloadChromeFile() {}", filePath);
		
		File file = new File(filePath);
		if (file.exists()) {
			mav = new ModelAndView("downloadView", "downloadFile", file);
		//	mav = new ModelAndView("jsonView");
		//	downloadFile(file, request, response);
		} else {
			mav = new ModelAndView("jsonView");
			mav.addObject("msg", "파일이 존재하지 않습니다.");
		}
		return mav;
	}
	
	public void downloadFile(File file, HttpServletRequest request, HttpServletResponse response) throws Exception {

		//response.setContentType("text/html;charset=ISO-8859-1");
		response.setContentType("text/html;charset=UTF-8");
		response.setContentLength((int) file.length());

		String fileName = null;
//		String userAgent = request.getHeader("User-Agent");
//		boolean ie = userAgent.indexOf("MSIE") > -1;
//		if (ie) {
//			fileName = URLEncoder.encode(file.getName(), "UTF-8");
//		} else {
//			fileName = new String(file.getName().getBytes("UTF-8"));
//		}
		fileName = "한글파일입니다.pdf";

		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis, out);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
		} // try end;
		out.flush();
	}

}
