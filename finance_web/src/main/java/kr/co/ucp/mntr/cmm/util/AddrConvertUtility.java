package kr.co.ucp.mntr.cmm.util;

/**
 * ----------------------------------------------------------------------------------------------
 * @Class Name : AddrConvertUtility.java
 * @Description : 주소변환과 관련된 유틸리티 모음
 * @Version : 1.0
 * Copyright (c) 2017 by KR.CO.WIDECUBE All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2017. 10. 25. saintjuny 최초작성
 *
 * ----------------------------------------------------------------------------------------------
 */

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kr.co.ucp.egov.com.cmm.util.EgovResourceCloseHelper;
import kr.co.ucp.mntr.gis.util.GisUtil;


public class AddrConvertUtility {
	private final GisUtil gu = new GisUtil();
	private static final String FILEPATH = "C:\\Users\\XPS9570\\Downloads\\new\\";
	private static final String FILENAME = "entrc_seoul";
	private static final String TABLENAME = "UM_GIS_ENTRC";
	private final String inFilepath = FILEPATH + FILENAME + ".txt";
	private final String outFileWgsPath = FILEPATH + FILENAME + "_wgs84.txt";
	private final String outFileEmptyPath = FILEPATH + FILENAME + "_empty.txt";
	private final String outFileErrorPath = FILEPATH + FILENAME + "_error.txt";
	private final String outFileSqlPath = FILEPATH + FILENAME + "_wgs84.sql";

	/**
	 * 도로명주소 개발자 센터에서 제공하는 위치정보요약DB text 파일을 위경도 좌표계로 변환한다. filePath 확인 필수
	 * fileName 확인 필수
	 *
	 * @param agrs
	 * @throws Exception
	 * @see http://www.juso.go.kr/addrlink/addressBuildDevNew.do?menu=geodata
	 */
	public static void main(String[] agrs) throws Exception {
		AddrConvertUtility acu = new AddrConvertUtility();
		acu.convertEntrc();
	}

	public String setEscapeOn(StringBuilder sb) {
		String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
		Pattern pattern = Pattern.compile(match);
		Matcher matcher = pattern.matcher(sb.toString());
		int index = 0;
		while (matcher.find()) {
			int offset = matcher.start();
			sb.insert(offset + index, "\\");
			index++;
		}
		return sb.toString();
	}

	public void convertEntrc() {

		File f = new File(inFilepath);
		try {
			if (f.exists()) {
				BufferedWriter bwWgs84 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileWgsPath), "euc-kr"));

				BufferedWriter bwEmpty = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileEmptyPath), "euc-kr"));

				BufferedWriter bwError = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileErrorPath), "euc-kr"));

				BufferedWriter bwSql = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileSqlPath), "UTF-8"));

				bwWgs84.write("SIGUNGU_CD|EXIT_SRL_NO|LG_DONG_CD|SIDO_NM|SIGUNGU_NM|LG_EMD_NM|ROAD_CD|ROAD_NM|IS_UNDGRND|BULD_MAIN_NO|BULD_SUB_NO|BULD_NM|ZIP_CD|BULD_USED_TY|BULD_GRP_TY|JUSTN_ADM_DONG|POINT_X|POINT_Y");
				bwWgs84.newLine();
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFilepath), "euc-kr"));
				String s = "";
				int cnt = 0;
				//while ((s = reader.readLine()) != null) {
				while (true) {s = reader.readLine();if(s==null) {break;}
					String[] split = s.split(Pattern.quote("|"));
					List<String> list = new ArrayList<String>(Arrays.asList(split));
					if (split.length != 18) {
						list.add("0");
						list.add("0");

						int size = list.size();
						StringBuffer sb = new StringBuffer("(");
						for (int i = 0; i < size; i++) {
							if (i != 0) {
								sb.append(", '" + list.get(i) + "'");
							} else {
								sb.append("'" + list.get(i) + "'");
							}
						}
						sb.append(")");
						bwEmpty.write(sb.toString());
						bwEmpty.newLine();
					} else {
						String lon = list.get(16);
						String lat = list.get(17);
						Point2D.Double doubles = gu.convertUTMK2WGS84(lon, lat);
						list.set(16, String.valueOf(doubles.getX()));
						list.set(17, String.valueOf(doubles.getY()));
					}
					int size = list.size();
					if (size == 18) {

						StringBuffer sb = new StringBuffer();
						StringBuffer sbSql = new StringBuffer();
						sbSql.append("INSERT INTO ");
						sbSql.append(TABLENAME);
						sbSql.append(" (SIGUNGU_CD, EXIT_SRL_NO, LG_DONG_CD, SIDO_NM, SIGUNGU_NM, LG_EMD_NM, ROAD_CD, ROAD_NM, IS_UNDGRND, BULD_MAIN_NO, BULD_SUB_NO, BULD_NM, ZIP_CD, BULD_USED_TY, BULD_GRP_TY, JUSTN_ADM_DONG, POINT_X, POINT_Y) VALUES (");
						for (int i = 0; i < size; i++) {
							StringBuilder sbCol = new StringBuilder(list.get(i));

							if (i != 0) {
								//sb.append("|" + sbCol.toString());
								sb.append("|").append(sbCol.toString());
								sbSql.append(", '");
							} else {
								sb.append(sbCol.toString());
								sbSql.append("'");
							}

							if(i == 16 || i == 17) sbSql.append(sbCol.toString());
							else sbSql.append(sbCol.toString()); // sbSql.append(setEscapeOn(sbCol));

							sbSql.append("'");
						}
						bwWgs84.write(sb.toString());
						bwWgs84.newLine();

						sbSql.append(");");
						bwSql.write(sbSql.toString());
						bwSql.newLine();
					} else {
						bwError.write(list.toString());
						bwError.newLine();
					}

					if (cnt != 0 && cnt % 10000 == 0) {
						bwSql.write("commit;");
						bwSql.newLine();
					}

					cnt = cnt + 1;
				}

				bwWgs84.flush();
				bwEmpty.flush();
				bwError.flush();
				bwSql.write("commit;");
				bwSql.flush();
				EgovResourceCloseHelper.close(reader, bwWgs84, bwEmpty, bwError, bwSql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void convertNavi() {

		File f = new File(inFilepath);
		try {
			if (f.exists()) {
				BufferedWriter bwWgs84 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileWgsPath), "euc-kr"));

				BufferedWriter bwEmpty = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileEmptyPath), "euc-kr"));

				BufferedWriter bwError = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileErrorPath), "euc-kr"));

				BufferedWriter bwSql = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFileSqlPath), "euc-kr"));

				bwWgs84.write("SIGUNGU_CD|EXIT_SRL_NO|LG_DONG_CD|SIDO_NM|SIGUNGU_NM|LG_EMD_NM|ROAD_CD|ROAD_NM|IS_UNDGRND|BULD_MAIN_NO|BULD_SUB_NO|BULD_NM|ZIP_CD|BULD_USED_TY|BULD_GRP_TY|JUSTN_ADM_DONG|POINT_X|POINT_Y");
				bwWgs84.newLine();
				BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFilepath), "euc-kr"));
				String s = "";
				int cnt = 0;
				//while ((s = reader.readLine()) != null) {
				while (true) {s = reader.readLine();if(s==null) {break;}
					String[] split = s.split(Pattern.quote("|"));
					List<String> list = new ArrayList<String>(Arrays.asList(split));
					if (split.length != 18) {
						list.add("0");
						list.add("0");

						int size = list.size();
						StringBuffer sb = new StringBuffer("(");
						for (int i = 0; i < size; i++) {
							if (i != 0) {
								sb.append(", '" + list.get(i) + "'");
							} else {
								sb.append("'" + list.get(i) + "'");
							}
						}
						sb.append(")");
						bwEmpty.write(sb.toString());
						bwEmpty.newLine();
					} else {
						String lon = list.get(16);
						String lat = list.get(17);
						Point2D.Double doubles = gu.convertUTMK2WGS84(lon, lat);
						list.set(16, String.valueOf(doubles.getX()));
						list.set(17, String.valueOf(doubles.getY()));
					}
					int size = list.size();
					if (size == 18) {

						StringBuffer sb = new StringBuffer();
						StringBuffer sbSql = new StringBuffer();
						sbSql.append("INSERT INTO ");
						sbSql.append(TABLENAME);
						sbSql.append(" (SIGUNGU_CD, EXIT_SRL_NO, LG_DONG_CD, SIDO_NM, SIGUNGU_NM, LG_EMD_NM, ROAD_CD, ROAD_NM, IS_UNDGRND, BULD_MAIN_NO, BULD_SUB_NO, BULD_NM, ZIP_CD, BULD_USED_TY, BULD_GRP_TY, JUSTN_ADM_DONG, POINT_X, POINT_Y) VALUES (");
						for (int i = 0; i < size; i++) {
							StringBuilder sbCol = new StringBuilder(list.get(i));

							if (i != 0) {
								//sb.append("|" + sbCol.toString());
								sb.append("|").append(sbCol.toString());
								sbSql.append(", '");
							} else {
								sb.append(sbCol.toString());
								sbSql.append("'");
							}

							if(i == 16 || i == 17) sbSql.append(sbCol.toString());
							else sbSql.append(sbCol.toString()); // sbSql.append(setEscapeOn(sbCol));

							sbSql.append("'");
						}
						bwWgs84.write(sb.toString());
						bwWgs84.newLine();

						sbSql.append(");");
						bwSql.write(sbSql.toString());
						bwSql.newLine();
					} else {
						bwError.write(list.toString());
						bwError.newLine();
					}

					if (cnt != 0 && cnt % 10000 == 0) {
						bwSql.write("commit;");
						bwSql.newLine();
					}

					cnt = cnt + 1;
				}

				bwWgs84.flush();
				bwEmpty.flush();
				bwError.flush();
				bwSql.write("commit;");
				bwSql.flush();
				EgovResourceCloseHelper.close(reader, bwWgs84, bwEmpty, bwError, bwSql);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
