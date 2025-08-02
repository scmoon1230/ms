package kr.co.ucp.mntr.cmm.util;

import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import org.springframework.stereotype.Component;

import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import kr.co.ucp.mntr.cmm.service.CommonVO;

@Component
public class CommonUtil {
	private String[] operation = { "", "목록", "상세조회", "등록", "수정", "삭제" };

	public void setCommonVOData(CommonVO common, String... strings) {
		setCommonVOData(common, 0, strings);
	}

	public void setCommonVOData(CommonVO common, int flag, String... strings) {
		common.setTitle(strings[0] + operation[flag]);
		common.setMainMenu(strings[1]);
		common.setSubMenu(strings[2]);
	}

	public PaginationInfo setPaginationInfo(CommonVO common) {
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(common.getPage());
		paginationInfo.setRecordCountPerPage(common.getRows());
		paginationInfo.setPageSize(common.getRows());
		// FOR ORACL_TIBERO DB
		common.setFirstIndex(paginationInfo.getFirstRecordIndex() + 1);
		common.setLastIndex(paginationInfo.getLastRecordIndex());
		// FOR MYSQL_MARIA DB
		common.setRecordCountPerPage(common.getRows());
		common.setFirstRecordIndex(paginationInfo.getFirstRecordIndex());
		common.setLastRecordIndex(paginationInfo.getLastRecordIndex());
		return paginationInfo;
	}

	public int setTotalRecordCount(List<EgovMap> list) {
     int totCnt = 0;
     if (list != null && !list.isEmpty()) {
         EgovMap map = list.get(0);
         if (map.containsKey("rowcnt")) {
             String rowcnt = EgovStringUtil.nullConvert(map.get("rowcnt"));
             try {
                 totCnt = Integer.parseInt(rowcnt);
             } catch (NumberFormatException e) {
                 totCnt = 0;
             }
         }
     }
     return totCnt;
 }

	public HttpServletResponse setExcelDownladHeader(HttpServletResponse response, String fileName) throws Exception {
		fileName = URLEncoder.encode(fileName, "UTF-8");

		response.setContentType("application/vnd.ms-excel;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
		response.setHeader("Content-Description", "JSP Generated Data");
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);

		return response;
	}

}
