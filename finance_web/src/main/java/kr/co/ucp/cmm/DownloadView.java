package kr.co.ucp.cmm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

public class DownloadView extends AbstractView {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public DownloadView() {
        setContentType("application/octet-stream; charset=utf-8");
    } // 객체 생성할 때 실행되는 부분(생성자)

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = (File) model.get("downloadFile");
        // ModelAndView 객체에 있는 참조변수 downloadFile를 꺼내서 저장

        response.setContentType(getContentType());
        response.setContentLength((int) file.length());

        String userAgent = request.getHeader("User-Agent");

        // 익스플로러와 그외 브라우저에 따른 헤더정보 설정
        boolean ie = userAgent.indexOf("MSIE") > -1;
        String fileName = null;
        if (ie) {
            fileName = URLEncoder.encode(file.getName(), "utf-8");
        } else {
            fileName = new String(file.getName().getBytes("utf-8"), "iso-8859-1");
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");

        response.setHeader("Content-Transfer-Encoding", "binary");
        OutputStream out = response.getOutputStream();

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
        } finally {
            if (fis != null) try {
                fis.close();
            } catch (IOException ex) {
            }
        }
        out.flush();
    }
}
