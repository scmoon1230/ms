package kr.co.ucp.egov.com.cmm.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.SessionVO;
import kr.co.ucp.egov.com.cmm.service.EgovFileMngService;
import kr.co.ucp.cmm.service.FileVO;
import kr.co.ucp.cmm.service.PrprtsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.bind.annotation.CommandMap;


/**
 * @Class Name : EgovImageProcessController.java
 * @Description :
 * @Modification Information
 *
 *    수정일       수정자         수정내용
 *    -------        -------     -------------------
 *    2009. 4. 2.     이삼섭
 *    2011.08.31.     JJY        경량환경 템플릿 커스터마이징버전 생성
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 4. 2.
 * @version
 * @see
 *
 */
@Controller
public class EgovImageProcessController extends HttpServlet {

    /**
	 *  serialVersion UID
	 */
	private static final long serialVersionUID = -6339945210971171173L;

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

	@Resource(name="EgovFileMngService")
    private EgovFileMngService fileService;

    private static final Logger LOGGER = LoggerFactory.getLogger(EgovImageProcessController.class);

    /** EgovPropertyService */
//    @Resource(name="propertiesService")
//    @Autowired protected EgovPropertyService propertiesService;

    /**
     * 첨부된 이미지에 대한 미리보기 기능을 제공한다.
     *
     * @param atchFileId
     * @param fileSn
     * @param sessionVO
     * @param model
     * @param response
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cmm/fms/getImage.do")
    public void getImageInf(SessionVO sessionVO, ModelMap model, @CommandMap Map<String, Object> commandMap, HttpServletResponse response) throws Exception {

		//@RequestParam("atchFileId") String atchFileId,
		//@RequestParam("fileSn") String fileSn,
		String atchFileId = (String)commandMap.get("atchFileId");
		String fileSn = (String)commandMap.get("fileSn");

		FileVO vo = new FileVO();

		vo.setAtchFileId(atchFileId);
		vo.setFileSn(fileSn);

		FileVO fvo = fileService.selectFileInf(vo);

		//String fileLoaction = fvo.getFileStreCours() + fvo.getStreFileNm();

		File file = new File(fvo.getFileStreCours(), fvo.getStreFileNm());
		FileInputStream fis = null;

		BufferedInputStream in = null;
		ByteArrayOutputStream bStream = null;
		try{
			fis = new FileInputStream(file);
			in = new BufferedInputStream(fis);
			bStream = new ByteArrayOutputStream();
			int imgByte;
			while ((imgByte = in.read()) != -1) {
			    bStream.write(imgByte);
			}

			String type = "";

			if (fvo.getFileExtsn() != null && !"".equals(fvo.getFileExtsn())) {
			    if ("jpg".equals(fvo.getFileExtsn().toLowerCase())) {
				type = "image/jpeg";
			    } else {
				type = "image/" + fvo.getFileExtsn().toLowerCase();
			    }
			    type = "image/" + fvo.getFileExtsn().toLowerCase();

			} else {
				LOGGER.debug("Image fileType is null.");
			}

			response.setHeader("Content-Type", type);
			response.setContentLength(bStream.size());

			bStream.writeTo(response.getOutputStream());

			response.getOutputStream().flush();
			response.getOutputStream().close();

		}catch(NullPointerException e){
			LOGGER.error("getImageInf NullPointerException  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}catch(FileNotFoundException e){
			LOGGER.error("getImageInf FileNotFoundException  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}catch(IOException e){
			LOGGER.error("getImageInf IOException  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}catch(Exception e){
			LOGGER.error("getImageInf Exception  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}finally{
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (Exception efis) {
					LOGGER.debug("IGNORED: {}", efis.getMessage());
				}
			}
			if (bStream != null) {
				try {
					bStream.close();
					bStream = null;
				} catch (Exception est) {
					LOGGER.debug("IGNORED: {}", est.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception ei) {
					LOGGER.debug("IGNORED: {}", ei.getMessage());
				}
			}
		}
    }

    @RequestMapping(value="/cmm/getImage.image")
    public void getImage(SessionVO sessionVO, ModelMap model, @CommandMap Map<String, Object> commandMap, HttpServletResponse response) throws Exception {

		//@RequestParam("atchFileId") String atchFileId,
		//@RequestParam("fileSn") String fileSn,
		String fileNm = (String)commandMap.get("fileNm");

		//String saveDir = propertiesService.getString("Globals.dirWrksHome").trim() + propertiesService.getString("Globals.dirCarImg").trim();
		String saveDir = "";
		if(saveDir == null || saveDir.equals("")) {
			return;
		}

		String ext = fileNm.substring(fileNm.lastIndexOf(".") + 1);

		FileVO vo = new FileVO();

		File file = new File(saveDir, fileNm);
		FileInputStream fis = null;

		BufferedInputStream in = null;
		ByteArrayOutputStream bStream = null;
		try{
			fis = new FileInputStream(file);
			in = new BufferedInputStream(fis);
			bStream = new ByteArrayOutputStream();
			int imgByte;
			while ((imgByte = in.read()) != -1) {
			    bStream.write(imgByte);
			}

			String type = "";

			if (ext != null && !"".equals(ext)) {
			    if ("jpg".equals(ext.toLowerCase())) {
				type = "image/jpeg";
			    } else {
				type = "image/" + ext.toLowerCase();
			    }
			    type = "image/" + ext.toLowerCase();

			} else {
				LOGGER.debug("Image fileType is null.");
			}

			response.setHeader("Content-Type", type);
			response.setContentLength(bStream.size());

			bStream.writeTo(response.getOutputStream());

			response.getOutputStream().flush();
			response.getOutputStream().close();

			bStream.close();
			in.close();
			fis.close();
		}catch(NullPointerException e){
			LOGGER.error("getImage NullPointerException  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}catch(FileNotFoundException e){
			LOGGER.error("getImage FileNotFoundException  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}catch(IOException e){
			LOGGER.error("getImage IOException  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}catch(Exception e){
			LOGGER.error("getImage Exception  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}finally{
			if (bStream != null) {
				try {
					bStream.close();
					bStream = null;
				} catch (Exception est) {
					LOGGER.debug("IGNORED: {}", est.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception ei) {
					LOGGER.debug("IGNORED: {}", ei.getMessage());
				}
			}
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (Exception efis) {
					LOGGER.debug("IGNORED: {}", efis.getMessage());
				}
			}
		}
    }

    @RequestMapping(value="/cmm/getImageEvnt.image")
    public void getImageEvnt(SessionVO sessionVO, ModelMap model, @CommandMap Map<String, Object> commandMap, HttpServletResponse response) throws Exception {

		//@RequestParam("atchFileId") String atchFileId,
		//@RequestParam("fileSn") String fileSn,
		String fileNm = (String)commandMap.get("fileNm");

		//String saveDir = propertiesService.getString("Globals.dirWrksHome").trim() + propertiesService.getString("Globals.dirEventImg").trim();
		String saveDir = prprtsService.getString("DIR_WRKS_HOME") + prprtsService.getString("DIR_EVENT_IMG");
		if(saveDir == null || saveDir.equals("")) {
			return;
		}

		String ext = fileNm.substring(fileNm.lastIndexOf(".") + 1);

		FileVO vo = new FileVO();

		File file = new File(saveDir, fileNm);
		FileInputStream fis = null;

		BufferedInputStream in = null;
		ByteArrayOutputStream bStream = null;
		try{
			fis = new FileInputStream(file);
			in = new BufferedInputStream(fis);
			bStream = new ByteArrayOutputStream();
			int imgByte;
			while ((imgByte = in.read()) != -1) {
			    bStream.write(imgByte);
			}

			String type = "";

			if (ext != null && !"".equals(ext)) {
			    if ("jpg".equals(ext.toLowerCase())) {
				type = "image/jpeg";
			    } else {
				type = "image/" + ext.toLowerCase();
			    }
			    type = "image/" + ext.toLowerCase();

			} else {
				LOGGER.debug("Image fileType is null.");
			}

			response.setHeader("Content-Type", type);
			response.setContentLength(bStream.size());

			bStream.writeTo(response.getOutputStream());

			response.getOutputStream().flush();
			response.getOutputStream().close();

		}catch(NullPointerException e){
			LOGGER.error("getImageEvnt NullPointerException  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}catch(FileNotFoundException e){
			LOGGER.error("getImageEvnt FileNotFoundException  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}catch(IOException e){
			LOGGER.error("getImageEvnt IOException  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}catch(Exception e){
			LOGGER.error("getImageEvnt Exception  : {}", e.getMessage());
			LOGGER.debug("{}", e);
		}finally{
			if (bStream != null) {
				try {
					bStream.close();
					bStream = null;
				} catch (Exception est) {
					LOGGER.debug("IGNORED: {}", est.getMessage());
				}
			}
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception ei) {
					LOGGER.debug("IGNORED: {}", ei.getMessage());
				}
			}
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (Exception efis) {
					LOGGER.debug("IGNORED: {}", efis.getMessage());
				}
			}
		}
    }

    @RequestMapping(value="/cmm/getImageFclt.image")
    public void getImageFclt(SessionVO sessionVO, ModelMap model, @CommandMap Map<String, Object> commandMap, HttpServletResponse response) throws Exception {

		//@RequestParam("atchFileId") String atchFileId,
		//@RequestParam("fileSn") String fileSn,
//		String fileNm = (String)commandMap.get("fileNm");
//
//		String saveDir = propertiesService.getString("Globals.FcltUploadDirectory").trim();
//		if(saveDir == null || saveDir.equals("")) {
//			return;
//		}
//
//		String ext = fileNm.substring(fileNm.lastIndexOf(".") + 1);
//
//		FileVO vo = new FileVO();
//
//		File file = new File(saveDir, fileNm);
//		FileInputStream fis = null;
//		BufferedInputStream in = null;
//		ByteArrayOutputStream bStream = null;
//		try{
//			fis = new FileInputStream(file);
//			in = new BufferedInputStream(fis);
//			bStream = new ByteArrayOutputStream();
//			int imgByte;
//			while ((imgByte = in.read()) != -1) {
//			    bStream.write(imgByte);
//			}
//
//			String type = "";
//
//			if (ext != null && !"".equals(ext)) {
//			    if ("jpg".equals(ext.toLowerCase())) {
//				type = "image/jpeg";
//			    } else {
//				type = "image/" + ext.toLowerCase();
//			    }
//			    type = "image/" + ext.toLowerCase();
//
//			} else {
//				LOGGER.debug("Image fileType is null.");
//			}
//
//			response.setHeader("Content-Type", type);
//			response.setContentLength(bStream.size());
//
//			bStream.writeTo(response.getOutputStream());
//
//			response.getOutputStream().flush();
//			response.getOutputStream().close();
//
//			bStream.close();
//			in.close();
//			fis.close();
//		}catch(NullPointerException e){
//			LOGGER.error("getImageFclt NullPointerException  : {}", e.getMessage());
//		}catch(FileNotFoundException e){
//			LOGGER.error("getImageFclt FileNotFoundException  : {}", e.getMessage());
//		}catch(IOException e){
//			LOGGER.error("getImageFclt IOException  : {}", e.getMessage());
//		}catch(Exception e){
//			LOGGER.error("getImageFclt Exception  : {}", e.getMessage());
//		}finally{
//			if (bStream != null) {
//				try {
//					bStream.close();
//					bStream = null;
//				} catch (Exception est) {
//					LOGGER.debug("IGNORED: {}", est.getMessage());
//				}
//			}
//			if (in != null) {
//				try {
//					in.close();
//					in = null;
//				} catch (Exception ei) {
//					LOGGER.debug("IGNORED: {}", ei.getMessage());
//				}
//			}
//			if (fis != null) {
//				try {
//					fis.close();
//					fis = null;
//				} catch (Exception efis) {
//					LOGGER.debug("IGNORED: {}", efis.getMessage());
//				}
//			}
//		}
    }
}
