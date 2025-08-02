package kr.co.ucp.wrks.sstm.code.web;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.wrks.sstm.code.service.CodeCmcdService;
import kr.co.ucp.wrks.sstm.code.service.CodeFcltcdService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.rte.fdl.cmmn.trace.LeaveaTrace;
import egovframework.rte.fdl.property.EgovPropertyService;

/**
 * 시설물용도별유형코드 현황을 관리
 * 
 * @author 대전도안 설준환
 * @version 1.00 2015-03-19
 * @since JDK 1.7.0_45(x64)
 * @revision / /** ----------------------------------------------------------
 * @Class Name : CodeFcltcdController
 * @Description : 시설물용도별유형코드
 * @Version : 1.0 Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information ---------------------------------------------------------- DATA AUTHOR DESCRIPTION ---------------------------------------------------------- 2015-03-19 설준환 최초작성 ----------------------------------------------------------
 */
@Controller
public class CodeFcltcdController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());	
	
	/** EgovMessageSource */
	@Resource(name="egovMessageSource")
	EgovMessageSource egovMessageSource;

	@Resource(name = "prprtsService")
	private PrprtsService prprtsService;

	/** TRACE */
	@Resource(name="leaveaTrace")
	LeaveaTrace leaveaTrace;

	@Resource(name="codeFcltcdService")
	CodeFcltcdService codeFcltcdSerivce;

	@Resource(name="codeCmcdService")
	CodeCmcdService codeCmcdService;

	/*
	 * 시설물용도별유형코드 리스트
	 */
	@RequestMapping(value="/wrks/sstm/code/fcltcd.do")
	public String view(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> args = new HashMap<String, Object>();

		/* 시설물종류 코드 설정 */
		args.put("cdGrpId", "FCLT_KND");
		args.put("cdTy", "C");
		//args.put("cdId", "CTV");	// CCTV 만 조회한다.
		args.put("orderBy", "ORDER BY CD_ID ASC");

		request.setAttribute("fcltKndList", codeCmcdService.grpList(args));

	    request.setAttribute("useGrpList", prprtsService.getList("useGrpList"));		/* 사용유형 */

	    request.setAttribute("rowPerPageList", prprtsService.getList("rowPerPageList"));	/* row per page설정 */

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		request.setAttribute("rowPerPageSession", (lgnVO.getRowPerPage() == null || lgnVO.getRowPerPage().equals("")) ? "40" : lgnVO.getRowPerPage());

		return "wrks/sstm/code/fcltcd";
	}

	/*
	 * 시설물용도별유형코드 조건검색
	 */
	@RequestMapping(value="/wrks/sstm/code/fcltcd/list.json")
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();

		String fcltKndCd = EgovStringUtil.nullConvert(request.getParameter("fcltKndCd"));
		String fcltUsedTyNm = EgovStringUtil.nullConvert(request.getParameter("fcltUsedTyNm"));
		String useTyCd = EgovStringUtil.nullConvert(request.getParameter("useTyCd"));
		String pageNo = EgovStringUtil.nullConvert(request.getParameter("page"));
		String rowsPerPage = EgovStringUtil.nullConvert(request.getParameter("rows"));
		String sidx = EgovStringUtil.nullConvert(request.getParameter("sidx"));
		String sord = EgovStringUtil.nullConvert(request.getParameter("sord"));

		Map<String, String> args = new HashMap<String, String>();

		args.put("fcltKndCd", fcltKndCd);
		args.put("fcltUsedTyNm", fcltUsedTyNm);
		args.put("useTyCd", useTyCd);
		args.put("pageNo"     , pageNo);
		args.put("rowsPerPage", rowsPerPage);
		args.put("sidx"       , sidx);
		args.put("sord"       , sord);

		List<Map<String, String>> list = codeFcltcdSerivce.fcltcdList(args);

		map.put("rows", list);
		map.put("page", pageNo);

		return map;
	}

	/*
	 * 시설물용도별유형코드 등록
	 */
	@SuppressWarnings(
	{ "unused" })
	@RequestMapping(value="/wrks/sstm/code/fcltcd/insert.json")
	@ResponseBody
	public Map<String, Object> insert(HttpServletRequest request, HttpServletResponse response) throws Exception {

		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();

		request.setCharacterEncoding("UTF-8");
		final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

		Map<String, Object> map = new HashMap<String, Object>();

		String fcltUsedTyCd = URLDecoder.decode(request.getParameter("fcltUsedTyCd"), "UTF-8");

		map.put("fcltUsedTyCd", fcltUsedTyCd);
		map.put("fcltUsedTyNm", URLDecoder.decode(request.getParameter("fcltUsedTyNm"), "UTF-8"));
		map.put("fcltKndCd", URLDecoder.decode(request.getParameter("fcltKndCd"), "UTF-8"));
		map.put("useTyCd", URLDecoder.decode(request.getParameter("useTyCd"), "UTF-8"));

		map.put("rgsUserId", sesUserId);
		map.put("updUserId", sesUserId);

		// String[] fcltTyIconNm;
		// String fcltTyIcon0 = URLDecoder.decode(request.getParameter("fcltTyIcon0"), "UTF-8");
		// String fcltTyIcon1 = URLDecoder.decode(request.getParameter("fcltTyIcon1"), "UTF-8");
		// String fcltTyIcon2 = URLDecoder.decode(request.getParameter("fcltTyIcon2"), "UTF-8");
		//
		String div = URLDecoder.decode(request.getParameter("div"), "UTF-8");
		//
		// map.put("FCLT_TY_ICON0", fcltTyIcon0);
		// map.put("FCLT_TY_ICON1", fcltTyIcon1);
		// map.put("FCLT_TY_ICON2", fcltTyIcon2);

		/*
		 * int cnt = 0; int z = 0; 이미지 업로드 카운트 if(!(fcltTyIcon0.equals(""))){ cnt += 1; } if(!(fcltTyIcon1.equals(""))){ cnt += 1; } if(!(fcltTyIcon2.equals(""))){ cnt += 1; }
		 */

		/* 파일이름을 담을 배열 */
		// fcltTyIconNm = new String[9];
		//
		// int z = 0;
		// /*파일명생성*/
		// //if(cnt != 0){
		// for(int i=0; i<3; i++){
		// z = i*2;
		//
		// fcltTyIconNm[i + z] = "b0_"+ fcltUsedTyCd +"_"+ i +".png";
		// fcltTyIconNm[i + z +1] = "b3_"+ fcltUsedTyCd +"_"+ i +".png";
		// fcltTyIconNm[i + z +2] = "b5_"+ fcltUsedTyCd +"_"+ i +".png";
		// }
		// //}

		Map<String, Object> mapRet = new HashMap<String, Object>();
		//
		// String saveDir = propertiesService.getString("Globals.dirWrksHome")
		// + propertiesService.getString("Globals.dirTemp");
		//
		// if(saveDir == null || saveDir.equals("")) {
		// map_ret.put("session", 1);
		// map_ret.put("msg", "이미지 파일을 저장할 위치 정보가 없습니다.");
		// return map_ret;
		// }

		try {
			int insertResult;
			try {
				if (div.equals("INST")) {
					/* 등록 */
					insertResult = codeFcltcdSerivce.insert(map);
				} else {
					/* 수정 */
					insertResult = codeFcltcdSerivce.update(map);
				}
			} catch (DataIntegrityViolationException e) {
				
				if (e.getRootCause().getMessage().indexOf("UNIQUE constraint violation") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "이미 등록된 코드입니다.");
					return mapRet;
				} else {
					logger.error(e.getRootCause().getMessage());
				}
				
			} catch (UncategorizedSQLException e) {
				// dup error
				if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "이미 등록된 코드입니다.");
					return mapRet;
				} else {
					logger.error(e.getRootCause().getMessage());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			// final Map<String, MultipartFile> files = multiRequest.getFileMap();
			// InputStream fis = null;
			//
			// Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
			// MultipartFile file;
			//
			// /*for문 제어*/
			// int forCnt = 0;
			//
			// while (itr.hasNext()) {
			// Entry<String, MultipartFile> entry = itr.next();
			//
			// file = entry.getValue();
			//
			// if (!"".equals(file.getOriginalFilename())) {
			// // 업로드 파일에 대한 확장자를 체크
			// if (file.getOriginalFilename().toUpperCase().endsWith(".PNG")){
			// String fileName;
			//
			// /*하나의 이미지당 세개의 사이즈 업로드*/
			// for(int i=forCnt; i<(3+forCnt); i++){
			// fileName = saveDir + File.separator + fcltTyIconNm[i];
			//
			// OutputStream fos = new FileOutputStream(fileName);
			//
			// fis = file.getInputStream();
			//
			// try {
			// byte[] buffer = new byte[1024];
			//
			// while(true){
			// int count = fis.read(buffer);
			// if(count == -1){
			// //System.out.println("더이상 읽은 데이터가 없다.");
			// break;
			// }
			// fos.write(buffer, 0, count);
			// }
			// } catch (Exception e) {
			// throw e;
			// } finally {
			// if (fis != null) {
			// fis.close();
			// }
			// if (fos != null) {
			// fos.close();
			// }
			// } // end finally
			// } // end for
			// }
			// else {
			// map_ret.put("session", 1);
			// map_ret.put("msg", "처리할 수 없는 파일입니다.");
			// return map_ret;
			// }
			// } // end if
			//
			// forCnt = forCnt + 3;
			// } // end while
			//
			// /*리사이즈할 이미지 크기*/
			// int[] imgSize = {30, 44, 62, 30, 44, 62, 30, 44, 62};
			//
			// /*이미지 리사이즈를 위한 함수*/
			// ImageResize.reSize(saveDir, fcltTyIconNm, imgSize, 9);

			mapRet.put("session", 1);
			mapRet.put("msg", "저장하였습니다.");
		} catch (Exception e) {
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리
		}

		return mapRet;
	}

	/*
	 * 입력함수로 통합됨(2015.04.06) 시설물용도별유형코드 수정
	 * @SuppressWarnings("unused")
	 * @RequestMapping(value="/wrks/sstm/code/fcltcd/update.json")
	 * @ResponseBody public Map<String, Object> update(HttpServletRequest request, HttpServletResponse response) throws Exception{ LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser(); String sesUserId = lgnVO.getUserId(); Map<String, Object> map = new
	 * HashMap<String, Object>(); map.put("fcltUsedTyCd", request.getParameter("fcltUsedTyCd")); map.put("fcltUsedTyNm", request.getParameter("fcltUsedTyNm")); map.put("fcltKndCd", request.getParameter("fcltKndCd")); map.put("useTyCd", request.getParameter("useTyCd"));
	 * map.put("updUserId", sesUserId); Map<String, Object> map_ret = new HashMap<String, Object>(); try{ int insertResult; try { insertResult = codeFcltcdSerivce.update(map); } catch(DataIntegrityViolationException e) { System.out.println(e.getRootCause()); }
	 * catch(UncategorizedSQLException e) { //dup error if(e.getCause().getMessage().indexOf("JDBC-590730") >= 0) { map_ret.put("session", 0); map_ret.put("msg", "이미 등록된 코드입니다."); return map_ret; } else { System.out.println(e.getCause()); } }catch(Exception e){
	 * e.printStackTrace(); } map_ret.put("session", 1); map_ret.put("msg", "저장하였습니다."); } catch(Exception e){ map_ret.put("session", 0); map_ret.put("msg", "알수없는 에러!!!"); // TODO: SQL오류 메세지 처리 } return map_ret; }
	 */

	/*
	 * 시설물용도별유형코드 삭제
	 */
	@RequestMapping(value="/wrks/sstm/code/fcltcd/delete.json")
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String fcltUsedTyCd = URLDecoder.decode(request.getParameter("fcltUsedTyCd"), "UTF-8");

		Map<String, String> map = new HashMap<String, String>();

		map.put("updUserId", sesUserId);
		map.put("fcltUsedTyCd", fcltUsedTyCd);

		// String saveDir = propertiesService.getString("Globals.dirWrksHome")
		// + propertiesService.getString("Globals.dirTemp");

		Map<String, Object> mapRet = new HashMap<String, Object>();

		// if(saveDir == null || saveDir.equals("")) {
		// map_ret.put("session", 1);
		// map_ret.put("msg", "이미지 파일의 위치 정보가 없습니다.");
		// return map_ret;
		// }

		try {
			@SuppressWarnings("unused")
			int ret;
			try {
				ret = codeFcltcdSerivce.delete(map);
			} catch (DataIntegrityViolationException e) {
				logger.error(e.getRootCause().getMessage());
			} catch (UncategorizedSQLException e) {
				// dup error
				if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "삭제처리중 오류가 발생했습니다.잠시후 다시 시도해주세요");
					return mapRet;
				} else {
					logger.error(e.getRootCause().getMessage());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			/* 업로드 이미지 삭제 함수 */
			// deleteAtchFile(saveDir, fcltUsedTyCd, null);

			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		} catch (Exception e) {
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러입니다."); // TODO: SQL오류 메세지 처리
		}
		return mapRet;
	}

	/*
	 * 시설물용도별유형코드 다중삭제
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="/wrks/sstm/code/fcltcd/deleteMulti.json")
	@ResponseBody
	public Map<String, Object> deleteMulti(HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoginVO lgnVO = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String sesUserId = lgnVO.getUserId();
		String[] fcltUsedTyCds = request.getParameterValues("fcltUsedTyCd");

		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		for (int i = 0; i < fcltUsedTyCds.length; i++) {
			Map<String, String> mapId = new HashMap<String, String>();
			mapId.put("updUserId", sesUserId);
			mapId.put("fcltUsedTyCd", fcltUsedTyCds[i]);

			list.add(mapId);
		}

		// String saveDir = propertiesService.getString("Globals.dirWrksHome")
		// + propertiesService.getString("Globals.dirTemp");

		Map<String, Object> mapRet = new HashMap<String, Object>();

		// if(saveDir == null || saveDir.equals("")) {
		// map_ret.put("session", 1);
		// map_ret.put("msg", "이미지 파일의 위치 정보가 없습니다.");
		// return map_ret;
		// }

		try {
			int ret;
			try {
				ret = codeFcltcdSerivce.deleteMulti(list);
			} catch (DataIntegrityViolationException e) {
				logger.error(e.getRootCause().getMessage());
			} catch (UncategorizedSQLException e) {

				if (e.getCause().getMessage().indexOf("JDBC-590730") >= 0) {
					mapRet.put("session", 0);
					mapRet.put("msg", "처리중 에러가 발생했습니다.");
					return mapRet;
				} else {
					logger.error(e.getRootCause().getMessage());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			/* 업로드 이미지 삭제 함수 */
			// deleteAtchFile(saveDir, null, fcltUsedTyCds);

			mapRet.put("session", 1);
			mapRet.put("msg", "삭제하였습니다.");
		} catch (Exception e) {
			mapRet.put("session", 0);
			mapRet.put("msg", "알수없는 에러가 발생했습니다."); // TODO: SQL오류 메세지 처리
		}

		return mapRet;
	}

	/* 첨부파일 삭제 */
	public void deleteAtchFile(String saveDir, String fcltUsedTyCd, String[] fcltUsedTyCds) {
		/* for문 제어 */
		int z = 0;

		String fileName;
		String[] fcltTyIconNm;

		if (fcltUsedTyCds != null) {
			/* 다중삭제일 경우 */
			fcltTyIconNm = new String[fcltUsedTyCds.length * 9];

			for (int i = 0; i < fcltUsedTyCds.length; i++) {
				/* 이미지 갯수제어 */
				int x = 0;
				/* 내부 for문 제어 */
				int jCnt = i * 3;

				for (int j = jCnt; j < (i + 1) * 3; j++) {
					z = j * 2;

					fcltTyIconNm[j + z] = "b0_" + fcltUsedTyCds[i] + "_" + x + ".png";
					fcltTyIconNm[j + z + 1] = "b3_" + fcltUsedTyCds[i] + "_" + x + ".png";
					fcltTyIconNm[j + z + 2] = "b5_" + fcltUsedTyCds[i] + "_" + x + ".png";

					x++;

					if (x > 2) {
						x = 0;
					}
				}
			}
		} else {
			/* 하나 삭제일 경우 */
			fcltTyIconNm = new String[9];

			for (int i = 0; i < 3; i++) {
				z = i * 2;

				fcltTyIconNm[i + z] = "b0_" + fcltUsedTyCd + "_" + i + ".png";
				fcltTyIconNm[i + z + 1] = "b3_" + fcltUsedTyCd + "_" + i + ".png";
				fcltTyIconNm[i + z + 2] = "b5_" + fcltUsedTyCd + "_" + i + ".png";
			}
		}

		/* 업로드 이미지 삭제 */
		for (int i = 0; i < fcltTyIconNm.length; i++) {
			fileName = saveDir + File.separator + fcltTyIconNm[i];
			File f = new File(fileName);
			f.delete();
		}
	}
}