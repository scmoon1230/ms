package kr.co.ucp.tvo.cmn.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

public class DownloadView extends AbstractView {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void Download() {
		setContentType("application/download; UTF-8");
	} // 파일 다운로드

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		File file = (File) model.get("downloadFile");

		response.setContentType(getContentType());
		response.setContentLength((int) file.length());

		String userAgent = request.getHeader("User-Agent");

		boolean ie = userAgent.indexOf("MSIE") > -1;

		String fileName = null;

		if (ie) {
			fileName = URLEncoder.encode(file.getName(), "UTF-8");
		} else {
			fileName = new String(file.getName().getBytes("UTF-8"));
		} // end if;

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
