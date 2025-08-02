package kr.co.ucp.mntr.cmm.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.CommFile;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;

@Component("fileUtil")
public class FileUtil {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource(name="config")
	private Properties config;

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

	public String getRootFilePath() throws Exception {
        return prprtsService.getString("FILE_UPLOAD_ROOT");
	}

	public String getDefaultFilePath() throws Exception {
        return prprtsService.getString("FILE_UPLOAD_PATH_DEFAULT");
	}

	public String getProperties(String arg) throws Exception {
		return config.getProperty(arg);
	}

	public File getFile(String fileName) throws Exception {
		File file = new File(getRootFilePath() + "/" + fileName);
		return file;
	}

	public String saveFile(MultipartFile file) throws Exception {
        String directory = prprtsService.getString("FILE_UPLOAD_PATH_DEFAULT");
		logger.info("=========== FILE_UPLOAD_PATH_DEFAULT : {}", directory);
		return saveFile(directory, file);
	}

	@SuppressWarnings("unused")
	public String saveFile(String uploadPath, MultipartFile file) throws Exception {
		if (null == file || null == uploadPath) {
			return null;
		}

		try {
            String root = prprtsService.getString("FILE_UPLOAD_ROOT");
			uploadPath = makeDiractory(root + uploadPath);

			String orgNm = file.getOriginalFilename().toLowerCase();

			if (orgNm.endsWith(".exe") || orgNm.endsWith(".bat") || orgNm.endsWith(".dll") || orgNm.endsWith(".sh") || orgNm.endsWith(".msi") || orgNm.endsWith(".cmd") || orgNm.endsWith(".vds")) throw new RuntimeException("not available file type.");

			if (orgNm == null) throw new RuntimeException("not available file name.");

			long size = file.getSize();
			long maxSize = Long.parseLong(config.getProperty("file.upload.maxSize"));
			if (size > maxSize) throw new RuntimeException("file size is too large.");

			String ext = orgNm.substring(orgNm.lastIndexOf(".") + 1);
			String reg = "hwp|xls|xlsx|doc|docx|ppt|pptx|gif|jpg|jpeg|png";
			if (reg.indexOf(ext) < 0) {
				return "-1";
			} else {

				String sysNm = System.currentTimeMillis() + "." + ext;
				if (sysNm != null && !"".equals(sysNm)) {
					sysNm = sysNm.replaceAll("/", "");
					sysNm = sysNm.replaceAll("\\\\", "");
					sysNm = sysNm.replaceAll("&", "");
				}

				File f = new File(uploadPath + "/" + sysNm);
				file.transferTo(f);

				return sysNm;
			}
		} catch (Exception e) {
			logger.error("saveFile Exception : {}" + e.getMessage());
			return null;
		}
	}

	public boolean deleteFile(String fileNm) throws Exception {
        return deleteFile("FILE_UPLOAD_PATH_DEFAULT", fileNm);
	}

	public boolean deleteFile(String Folder, String fileNm) throws Exception {
		Folder = config.getProperty(Folder);
        String filePath = prprtsService.getString("FILE_UPLOAD_ROOT") + Folder;

		File file = null;
		try {

			if (fileNm != null && !"".equals(fileNm)) {
				fileNm = fileNm.replaceAll("/", "");
				fileNm = fileNm.replaceAll("\\\\", "");
				fileNm = fileNm.replaceAll("&", "");
			}

			file = new File(filePath + fileNm);
			if (!file.delete()) return false;

		} catch (NullPointerException e) {
			logger.error("deleteFile NullPointerException : {}" + e.getMessage());
			return false;

		} catch (Exception e) {
			logger.error("deleteFile Exception : {}" + e.getMessage());
			return false;
		}
		return true;
	}

	private String makeDiractory(String path) throws Exception {
		String folder = "";
		try {

			if (path != null && !"".equals(path)) {
				path = path.replaceAll("\\.", "");
				path = path.replaceAll("\\&", "");
			}

			File directory = new File(path);

			if (!directory.exists()) {
				boolean result = directory.mkdirs();
				CommFile.fileChmod(path);
			}
			folder = directory.getAbsolutePath();
		} catch (NullPointerException e) {
			logger.error("makeDiractory NullPointerException : {}" + e.getMessage());
            folder = prprtsService.getString("FILE_UPLOAD_ROOT") + prprtsService.getString("FILE_UPLOAD_PATH_DEFAULT");
		} catch (Exception e) {
			logger.error("makeDiractory Exception : {}" + e.getMessage());
            folder = prprtsService.getString("FILE_UPLOAD_ROOT") + prprtsService.getString("FILE_UPLOAD_PATH_DEFAULT");
		}
		return folder;
	}

	public String makeThumbnail(String orgFolder, String thumbFolder, String fileNm) throws Exception {
        String root = prprtsService.getString("FILE_UPLOAD_ROOT");
		int thumbnail_width = 230;
		int thumbnail_height = 140;
		String thumbFileNm = "";

		try {
			orgFolder = root + orgFolder;

			/*
			 * if (fileNm != null && !"".equals(fileNm)) { fileNm = fileNm.replaceAll("/", ""); fileNm = fileNm.replaceAll("\\\\",""); fileNm = fileNm.replaceAll("&", ""); }
			 */
			File orgFile = new File(orgFolder + "/" + fileNm);
			BufferedImage buffer_original_image = ImageIO.read(orgFile);

			thumbFolder = root + thumbFolder;
			thumbFolder = makeDiractory(thumbFolder);
			// thumbFileNm = thumbFolder + "\\thumbnail_"+fileNm;
			File thumbFile = new File(thumbFolder + "/thumbnail_" + fileNm);
			BufferedImage buffer_thumbnail_image = new BufferedImage(thumbnail_width, thumbnail_height, BufferedImage.TYPE_3BYTE_BGR);

			Graphics2D graphic = buffer_thumbnail_image.createGraphics();
			graphic.drawImage(buffer_original_image, 0, 0, thumbnail_width, thumbnail_height, null);
			ImageIO.write(buffer_thumbnail_image, "jpg", thumbFile);
		} catch (NullPointerException e) {
			logger.error("makeThumbnail NullPointerException : {}" + e.getMessage());
		} catch (Exception e) {
			logger.error("makeThumbnail Exception : {}" + e.getMessage());
			e.printStackTrace();
		}
		return thumbFileNm;
	}

	public void fileDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String folderName = EgovStringUtil.nullConvert(request.getParameter("filePath"));
			String fileName = EgovStringUtil.nullConvert(request.getParameter("fileName"));

			if ("".equals(folderName) && "".equals(fileName)) {
				folderName = EgovStringUtil.nullConvert(request.getAttribute("filePath"));
				fileName = EgovStringUtil.nullConvert(request.getAttribute("fileName"));

				if ("".equals(folderName) && "".equals(fileName)) {
					response.setCharacterEncoding("UTF-8");
					response.setContentType("text/html; charset=UTF-8");
					PrintWriter printwriter = response.getWriter();
					printwriter.println("<script>");
					printwriter.println("alert('파일이 존재 하지 않습니다.')");
					printwriter.println("window.close();");
					printwriter.println("</script>");
					printwriter.flush();
					printwriter.close();
				}
			}

			logger.debug("===========folderName : {}", folderName);
			logger.debug("===========fileName : {}", fileName);
			String filePath = getRootFilePath() + config.getProperty(folderName) + config.getProperty(fileName);
			logger.debug("===========filePath : {}", filePath);
			File file = new File(filePath);
			int fSize = (int) file.length();

			if (fSize > 0) {

				String orgNm = file.getName();
				logger.debug("===========orgNm=" + orgNm);
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
				String mimetype = "text/html";
				orgNm = new String(orgNm.getBytes("KSC5601"), "8859_1");

				response.setBufferSize(fSize);
				response.setContentType(mimetype);
				response.setHeader("Content-Disposition", "attachment; filename=\"" + orgNm + "\"");
				response.setContentLength(fSize);

				FileCopyUtils.copy(in, response.getOutputStream());
				in.close();
				response.getOutputStream().flush();
				response.getOutputStream().close();
			} else {
				throw new RuntimeException("not available file");
			}
		} catch (Exception e) {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter printwriter = response.getWriter();
			printwriter.println("<script>");
			printwriter.println("alert('파일이 존재 하지 않습니다.')");
			printwriter.println("window.close();");
			printwriter.println("</script>");
			printwriter.flush();
			printwriter.close();
		}
	}

}
