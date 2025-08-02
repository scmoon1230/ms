/**
 * -----------------------------------------------------------------
 *
 * @Class Name : WrkrptLogServiceImpl.java
 * @Description :
 * @Version : 1.0 Copyright (c) 2018 by KR.CO.UCP All rights reserved.
 * @Modification Information
 *
 * <pre>
 * -----------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * 2017. 10. 31. seungJun New Write
 * -----------------------------------------------------------------
 *               </pre>
 */
package kr.co.ucp.cmm.service.impl;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.service.CmmMapper;
import kr.co.ucp.cmm.service.CmmService;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.egov.com.utl.sim.service.EgovFileScrty;

@Service("cmmService")
public class CmmServiceImpl implements CmmService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

    @Resource(name = "cmmMapper")
    private CmmMapper cmmMapper;

    @Override
    public Map<String, String> getYmdHms() throws Exception {
        return cmmMapper.getYmdHms();
    }

    @Override
    public Map<String, String> getYmdHmsCal(Map<String, Object> args) throws Exception {
        return cmmMapper.getYmdHmsCal(args);
    }

    @Override
    public List<EgovMap> selectTableInfoList(String tableName) throws Exception {
        Map<String, Object> args = new HashMap<String, Object>();
        String dbType = prprtsService.getGlobals("Globals.DbType");
        if ("postgresql".equals(dbType)) {
            tableName = EgovStringUtil.lowerCase(tableName);
            String schemaName = prprtsService.getGlobals("db.pgs.url");
            int lastIndex = schemaName.lastIndexOf("/") + 1;
            schemaName = schemaName.substring(lastIndex);
            logger.debug("schemaName >>> {}", schemaName);
    		args.put("schemaName", schemaName);
        }
		args.put("tableName", tableName);
        return cmmMapper.selectTableInfoList(args);
    }

    @Override
    public Map<String, Object> cmmChgPw(String userId) throws Exception {
        String newPwd = "";
        int ty = 0;
        char[] spclChar = {'~', '!', '@', '(', ')'};
        char[] numChar = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        int numInt = 0;
        char char_val;
        for (int i = 0; i < 12; i++) {
            ty = (int) (Math.random() * 4);
            int i_value = (int) (Math.random() * 26);
            if (ty == 0) {
                char_val = (char) (int) (i_value + 97); //랜덤 소문자 값 저장
            } else if (ty == 1) {
                char_val = (char) (int) (i_value + 65); //랜덤 대문자 값 저장
            } else if (ty == 2) {
                numInt = (int) (Math.random() * 9);
                char_val = numChar[numInt]; //랜덤 숫자 값 저장
            } else {
                int spclInt = (int) (Math.random() * 5);
                char_val = spclChar[spclInt]; //랜덤 특수문자
            }

//			logger.debug("value : {}-{}",char_val,ty);
            newPwd += Character.toString(char_val);
        }

//		20230731 salt항목추가
//		String saltText = prprtsService.getString("SALT_TEXT", "scmpworld"); // 암호화 보안 적용
        String encPwd = getPwd(newPwd, "C", "");

        logger.debug("value : {},{}", newPwd, encPwd);
        Map<String, Object> args = new HashMap<String, Object>();
		args.put("userId", userId);
		args.put("newPwd", newPwd);
		args.put("password", encPwd);
        cmmMapper.updateUserPwd(args);
        return args;
    }

    @Override
    public Map<String, Object> getCdInfo(String cdId, String cdGrpId) throws Exception {
        Map<String, Object> args = new HashMap<String, Object>();
		args.put("cdId", cdId);
		args.put("cdGrpId", cdGrpId);
        return cmmMapper.getCdInfo(args);
    }

    /* 비번생성 및 읽기
     * ty C-생성, R-읽기
     */
    @Override
    public String getPwd(String pwd, String ty, String userId) throws Exception {
        String saltText = "";
        String pw = pwd;
        if ("".equals(pwd) || pwd == null) return "";
        String dbEncryptTag = prprtsService.getString("DB_ENCRYPT", "UCP"); // DBEncrypt 적용 (Egov)
        if ("UCP".equals(dbEncryptTag)) {
            if ("C".equals(ty)) {
                saltText = prprtsService.getSalt(16); // random
            } else {
                saltText = prprtsService.getDigest(userId);
            }

            if (saltText == null) return "";

            pw = EgovFileScrty.encryptPassword(pwd, saltText);
            if ("scmpworld".equals(saltText)) {
                saltText = "";
            }
            pw = pw + saltText;
        }
//		logger.debug("value : {},{},{}",saltText,pwd,pw);
        return pw;
    }

	@Override
	public void buildExcelDocument(Map<String, Object> modelMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
		logger.debug("==== buildExcelDocument >>>> modelMap => {}", modelMap);

        String titleKey = modelMap.get("titleKey").toString();
        String titleHeader = modelMap.get("titleHeader").toString();
        String dataAlignment = EgovStringUtil.nullConvert(modelMap.get("dataAlignment")).toString();

        String excelName = modelMap.get("fileName").toString();
        /*제목 및 검색조건 추가*/
        String title = modelMap.get("title").toString();
        String search = "";
        int intRow = 0;

        //Workbook workbook = new HSSFWorkbook(); // 엑셀 97 ~ 2003
        Workbook workbook = new SXSSFWorkbook(); // 엑셀 2007 버전 이상

        // 타이틀스타일1
        CellStyle styleTitle = workbook.createCellStyle();
        Font fontTitle = workbook.createFont();
        styleTitle.setFont(fontTitle);
        fontTitle.setBold(true);
        styleTitle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styleTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitle.setAlignment(HorizontalAlignment.CENTER);

        // 타이틀 스타일2(필수항목)
        CellStyle styleTitReq = workbook.createCellStyle();
        Font fontTitReq = workbook.createFont();
        fontTitReq.setBold(true);
        styleTitReq.setFont(fontTitReq);
        styleTitReq.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        styleTitReq.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitReq.setAlignment(HorizontalAlignment.CENTER);

        CellStyle styleDataL = workbook.createCellStyle();        styleDataL.setAlignment(HorizontalAlignment.LEFT);        // 데이타스타일-L
        CellStyle styleDataC = workbook.createCellStyle();        styleDataC.setAlignment(HorizontalAlignment.CENTER);      // 데이타스타일-C
        CellStyle styleDataR = workbook.createCellStyle();        styleDataR.setAlignment(HorizontalAlignment.RIGHT);       // 데이타스타일-R

        /*제목 및 검색조건 추가*/
        Sheet worksheet = null;
        Row row = null;
        String[] titleArray;
        String[] keyArray;
        String[] alignmentArray;

        excelName = URLEncoder.encode(excelName, "UTF-8").replace("+", "%20");
        worksheet = workbook.createSheet("WorkSheet1");

        if (modelMap.containsKey("searchMap")) {
            Map<String, String> searchMap = (Map<String, String>) modelMap.get("searchMap");
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                String[] split = value.split(Pattern.quote("|"));
                row = worksheet.createRow(intRow);
                if (split.length != 0) {
                    for (int i = 0; i < split.length; i++) {
                        row.createCell(i).setCellValue(split[i]);
                    }
                } else {
                    row.createCell(0).setCellValue(value);
                }
                intRow++;
            }
        }

        /*제목 및 검색조건 추가*/
        // 제목
        if (!"".equals(title)) {
            row = worksheet.createRow(intRow);
            row.createCell(0).setCellValue(title);
            intRow++;
        }

        // 검색조건
        if (!"".equals(search)) {
            row = worksheet.createRow(intRow);
            row.createCell(0).setCellValue(search);
            intRow++;
        }
        /*검색조건 추가*/
        if ("".equals(title) && "".equals(search) && !modelMap.containsKey("searchMap")) intRow = 0;


        /* 항목별 제목 */
        titleArray = titleHeader.split(Pattern.quote("|"));
        row = worksheet.createRow(intRow);
        for (int col = 0; col < titleArray.length; col++) {
            row.createCell(col).setCellValue(titleArray[col]);
            if (titleArray[col].contains("(필수)")) {       row.getCell(col).setCellStyle(styleTitReq);
            } else {						               row.getCell(col).setCellStyle(styleTitle);
            }
            worksheet.createFreezePane(0, intRow+1);
        }

        //data
        keyArray = titleKey.split(Pattern.quote("|"));
        alignmentArray = dataAlignment.split(Pattern.quote("|"));
        intRow++;
        @SuppressWarnings("unchecked")
        List<Map<String, String>> list = (List<Map<String, String>>) modelMap.get("dataList");
        for (int i = 0; i < list.size(); i++) {
            row = worksheet.createRow(i + intRow);
            for (int col = 0; col < keyArray.length; col++) {
                row.createCell(col).setCellValue( String.valueOf(list.get(i).get(keyArray[col])) == "null" ? "" : String.valueOf(list.get(i).get(keyArray[col])) );

                if (!"".equalsIgnoreCase(dataAlignment)) {		// 데이타정렬정보가 있을 때
                    if ( "L".equalsIgnoreCase(alignmentArray[col] )) {row.getCell(col).setCellStyle(styleDataL); }
                    else if ( "C".equalsIgnoreCase(alignmentArray[col] )) {row.getCell(col).setCellStyle(styleDataC); }
                    else if ( "R".equalsIgnoreCase(alignmentArray[col] )) {row.getCell(col).setCellStyle(styleDataR); }
                }
            }
        }

        response.setContentType("ms-vnd/excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + excelName + ".xlsx");
        //response.setHeader("Content-Transfer-Encoding", "binary");

        workbook.write(response.getOutputStream());
        workbook.close();
    }

}
