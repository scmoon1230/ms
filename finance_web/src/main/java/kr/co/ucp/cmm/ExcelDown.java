package kr.co.ucp.cmm;


import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

public class ExcelDown extends AbstractExcelView {
	@Override
	protected void buildExcelDocument(Map<String,Object> modelMap, HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String workLogTitleKey = modelMap.get("workLogTitleKey").toString();
		String workLogTitleHeader = modelMap.get("workLogTitleHeader").toString();
		//String rcvTitleKey = modelMap.get("rcvTitleKey").toString();
		//String rcvTitleHeader = modelMap.get("rcvTitleHeader").toString();
		String excelName = modelMap.get("fileName").toString();
		
        /*제목 및 검색조건 추가*/
		String title = modelMap.get("title").toString();
		String search = modelMap.get("search").toString();
		int intRow = 0;
        /*제목 및 검색조건 추가*/
        
		HSSFSheet worksheet = null;
		HSSFRow row = null;
	    String[] keyArray;
	    String[] titleArray;
	    //String[] rcvkeyArray;
	    //String[] rcvtitleArray;
         
        excelName = URLEncoder.encode(excelName, "UTF-8");
        worksheet = workbook.createSheet("WorkSheet1");
        
        /*제목 및 검색조건 추가*/
        //제목
        intRow = 0;
        row = worksheet.createRow(intRow);
        row.createCell(0).setCellValue(title);
        //검색조건
        intRow++;
        row = worksheet.createRow(intRow);
        row.createCell(0).setCellValue(search);
        
        intRow++;
        row = worksheet.createRow(intRow);        
        /*제목 및 검색조건 추가*/

        @SuppressWarnings("unchecked")
        List<Map<String, String>> list = (List<Map<String, String>>)modelMap.get("workLogDataList");
        
        //row = worksheet.createRow(0);
        intRow++;
        row = worksheet.createRow(intRow);

        keyArray = workLogTitleKey.split("[|]");
        titleArray = workLogTitleHeader.split("[|]");

        //header
    	//row = worksheet.createRow(0);
        row = worksheet.createRow(intRow);
        
    	for(int col = 0; col < titleArray.length; col++) {
        	row.createCell(col).setCellValue(titleArray[col]);
    	}
    	
    	int y = 0;

    	for(int i=0; i<list.size(); i++){
    		if(list.get(i).get(keyArray[0]) == null){
    			break;
    		}
    		y++;
    	}
    	
        //data
    	intRow++;
        for(int i = 0; i < y; i++){
        	//row = worksheet.createRow(i + 1);
        	row = worksheet.createRow(i + intRow);

        	for(int col = 0; col < keyArray.length; col++) {
        		if(list.get(i).get(keyArray[col]) == null){
        			row.createCell(col).setCellValue(String.valueOf(" "));
        		}else{
        			row.createCell(col).setCellValue(String.valueOf(list.get(i).get(keyArray[col])));
        		}
            	
            	
        	}
        }

        /*
        rcvkeyArray = rcvTitleKey.split("[|]");
        rcvtitleArray = rcvTitleHeader.split("[|]");
        
        //header
    	//row = worksheet.createRow(0);
        intRow++;
        intRow++;
        row = worksheet.createRow(intRow);

    	for(int col = 0; col < rcvtitleArray.length; col++) {
        	row.createCell(col).setCellValue(rcvtitleArray[col]);
    	}

    	intRow++;
        for(int i = y; i < list.size(); i++){
        	//row = worksheet.createRow(i + 1);
        	row = worksheet.createRow(i + intRow);

        	for(int col = 0; col < rcvkeyArray.length; col++) {
            	row.createCell(col).setCellValue(String.valueOf(list.get(i).get(rcvkeyArray[col])));
        	}
        }
        */
        
		response.setContentType("Application/MSExcel");
		response.setHeader("Content-Disposition", "attachment; filename=" + excelName + ").xls");
   }
}

