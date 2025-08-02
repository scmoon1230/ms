package kr.co.ucp.mntr.fclt.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.mntr.fclt.service.UpdErrorVO;
import kr.co.ucp.mntr.fclt.service.UploadVO;
import kr.co.ucp.utl.sim.service.EgovFileScrty;

public class UploadValidCheck {
	// static Logger logger = LoggerFactory.getLogger(UploadValidCheck.class);

	@Resource(name="uploadManageMapper")
	private UploadManageMapper uploadManageMapper;

	@Resource(name="egovFileScrty")
	private EgovFileScrty egovFileScrty;

	@Resource(name="config")
	private Properties config;
	
	private String requierdColumn;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	// public UploadValidCheck(UploadManageMapper uploadManageMapper, EgovFileScrty egovFileScrty, Properties config) {
	// this.uploadManageMapper = uploadManageMapper;
	// this.egovFileScrty = egovFileScrty;
	// this.config = config;
	// }

	public UploadValidCheck(UploadManageMapper uploadManageMapper, EgovFileScrty egovFileScrty, Properties config) {
		this.uploadManageMapper = uploadManageMapper;
		this.egovFileScrty = egovFileScrty;
		this.config = config;
		this.requierdColumn = uploadManageMapper.strRequierdColumn();
		logger.debug("=======UploadValidCheck=======getRequierdColumn==requierdColumn=" + requierdColumn);
	}

	EgovMap uploadValidCheck(int rowNum, Map<Integer, Object> dataMap, List<UpdErrorVO> errorList, String columnIds) {
		String result = "";
		EgovMap eMap = new EgovMap();
		String data = "";
		String columnNmA = ""; // 등록자아이디, 등록일시, 수정자아이디, 수정일시 제외한 컬럼명
		String columnNms = "";
		UploadVO resultVO = new UploadVO();
		// String rgsupdTyId = ""; //등록구분이 삭제(D)일때 채번여부 판단

		try {
			String[] arrColumnIds = columnIds.split(",");
			// 엑설 헤더순서 columnIds
			for (int a = 0; a < arrColumnIds.length; a++) {

				for (UPLOAD_CHECK updChk : UPLOAD_CHECK.values()) {
					// int idx = updChk.ordinal();
					String colNm = "";

					if (arrColumnIds[a].equals(updChk.name())) {

						colNm = updChk.name();
						columnNms = columnNms + updChk.name() + ",";
						data = getStringFromObject(dataMap.get(a)).trim();

						// 등록구분이 I일때 채번여부 판단
						if ("RGSUPD_TY".equals(colNm) && "신규".equals(data)) {
							// rgsupdTyId = "I";
							data = "I";
						} else if ("RGSUPD_TY".equals(colNm) && "수정".equals(data)) {
							// rgsupdTyId = "U";
							data = "U";
						}

						// 정수,문자형식 숫자 입력값에 소수점 들어가는 데이터를 걸러냄.
						if ("SIGUNGU_CD".equals(colNm) || "FCLT_GDSDT_TY".equals(colNm) || "FCLT_STTUS".equals(colNm) || "CONN_PORT".equals(colNm) || "SVR_CONN_PORT".equals(colNm) || "DVC_SEE_CCTV_PORT".equals(colNm) || "DSTRT_CD".equals(colNm) || "PLC_PTR_DIV_CD".equals(colNm)
								|| "CCTV_HEIGHT".equals(colNm)) {
							data = data.replace(".0", "");
						}

						resultVO = validationCheck(updChk, data);

						result = resultVO.getErrorMsg();
						if (!"".equals(result)) {
							errorList.add(new UpdErrorVO(rowNum, a, updChk.getName(), data, result));
							eMap.put(colNm, data);
							eMap.put(colNm + "_CHK", "error");
							eMap.put("RGSUPD_TY_CHK", "error");
							continue;
						}
						/**
						 * 임시테이블 채번 하지 않기로 함. //등록구분이 I일때 채번 if("FCLT_ID".equals(colNm) && "I".equals(rgsupdTyId)){ if("".equals(data)){ //액샐에서 시설물아이디가 빈값이고 RGS이면 채번함. INSERT 시설물 아이디 - 시설물종류(3) + 센터코드(3) + 년도(4) + SEQ_FCLT.NEXTVAL(10) eMap.put("FCLT_SEQ_TY", "1"); }else{ //액샐에서
						 * 시설물아이디가 빈값이 아니고 RGS이면 채번하지 않음. INSERT 입력값 그대로 eMap.put("FCLT_SEQ_TY", "2"); } }
						 */
						if (!"RGS_USER_ID".equals(colNm) && !"RGS_DATE".equals(colNm) && !"UPD_USER_ID".equals(colNm) && !"UPD_DATE".equals(colNm)) {
							eMap.put(colNm, resultVO.getValue());
							eMap.put(colNm + "_CHK", "ok");
							columnNmA = columnNmA + colNm + ",";
						}

					}

				}
			}
			eMap.put("SEQ_NO", Integer.toString(rowNum)); // 엑셀에서 행순서 입력
			if (!"".equals(columnNmA)) resultVO.setColumnNm(columnNmA.substring(0, columnNmA.lastIndexOf(',')));
			eMap.put("columnNm", resultVO.getColumnNm());

		} catch (Exception e) {
			logger.error("uploadValidCheck Exception : {} ", e.getMessage());
			return eMap;
		}

		return eMap;
	}

	private UploadVO validationCheck(UPLOAD_CHECK updChk, String data) {
		UploadVO resultVO = null;
		try {
			updChk.setResource(uploadManageMapper, egovFileScrty, config, requierdColumn);
			resultVO = updChk.validation(data);
		} catch (Exception e) {
			logger.error("validationCheck Exception : {} ", e.getMessage());
			new UploadVO(data, UPD_ERR_MSG.DATE_CONVERT_ERROR.getMessage());
		}
		return resultVO;
	}

	private String getStringFromObject(Object obj) {
		String strObj = "";

		try {
			if (obj == null || "".equals(obj)) return "";

			if (obj instanceof String) {
				strObj = (String) obj;
			} else if (obj instanceof Double) {
				Double obj2 = (Double) obj;
				strObj = Double.toString(obj2);
			} else if (obj instanceof Integer) {
				Integer obj2 = (Integer) obj;
				strObj = Integer.toString(obj2);
			} else if (obj instanceof Boolean) {
				Boolean obj2 = (Boolean) obj;
				strObj = Boolean.toString(obj2);
			} else if (obj instanceof Byte) {
				Byte obj2 = (Byte) obj;
				strObj = Byte.toString(obj2);
			} else {
				strObj = (String) obj;
			}
		} catch (Exception e) {
			logger.error("getStringFromObject Exception : {} ", e.getMessage());
		}
		return strObj;
	}

	public static void main(String args[]) {
		int len = "0123456789012345678901234567890123456789".length();
		System.out.println(len);
		if (len >= 2 && len <= 40) {
			System.out.println("ok");
		} else {
			System.out.println("error");
		}
		;

	}

}