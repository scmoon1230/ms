package kr.co.ucp.mntr.fclt.service.impl;

import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang3.math.NumberUtils;

import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.service.CmmCodeVO;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.mntr.fclt.service.UploadVO;
import kr.co.ucp.utl.sim.service.EgovFileScrty;

public enum UPLOAD_CHECK {

	RGSUPD_TY {
		public String getName() {
			return "등록구분";
		}

		public UploadVO validation(String arg) {
			if (arg == null || "".equals(arg)) {
				return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			}
			rgsUpdYn = arg;
			if (!"I".equals(arg) && !"U".equals(arg) && !"D".equals(arg)) {
				return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("I, U"));
			}
			return new UploadVO(arg, "");
		}
	},
	FCLT_ID {
		public String getName() {
			return "시설물아이디";
		}

		public UploadVO validation(String arg) {

			arg = arg.trim();
			arg = arg.replaceAll(" ", "");
			arg = arg.replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));

			if ("I".equals(rgsUpdYn) && !"".equals(arg)) {
				if (UPLOAD_CHECK.FCLT_ID.isExistUpdFaltId(arg)) {
					return new UploadVO(arg, "시설아이디(등록구분:I)가 이미 존재합니다.");
				}
				return new UploadVO(arg, "");
			}

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if ("U".equals(rgsUpdYn) && "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if ("U".equals(rgsUpdYn) && !"".equals(arg)) {
				if (!UPLOAD_CHECK.FCLT_ID.isExistUpdFaltId(arg)) return new UploadVO(arg, "중요 오류:시설아이디(등록구분:U)가 존재하지 않습니다.");
				return new UploadVO(arg, "");
			}
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if ("D".equals(rgsUpdYn) && "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if ("D".equals(rgsUpdYn) && !"".equals(arg)) {
				if (!UPLOAD_CHECK.FCLT_ID.isExistUpdFaltId(arg)) return new UploadVO(arg, "중요 오류:시설아이디(등록구분:D)가 존재하지 않습니다.");
				return new UploadVO(arg, "");
			}
			/**
			 * 임시테이블 채번 하지 않기로 함. 시설물 아이디 - 시설물종류(3) + 센터코드(3) + 년도(4) + SEQ_FCLT.NEXTVAL(10) = 20 //String fcltId = ""; //fcltId = selectFcltSeq(); //logger.debug("=======UPLOAD_CHECK=======FCLT_ID==fcltId="+fcltId); if("".equals(fcltId)) { return new UploadVO(arg,
			 * UPD_ERR_MSG.GET_DATA_ERROR.getMessage()); }
			 */
			/*
			 * int loop = 0; int dupChk = UPLOAD_CHECK.FCLT_ID.duplicateFclt(fcltId); while(dupChk != 0) { loop++; fcltId = selectFcltSeq(); if(loop >= 5 && dupChk != 0) return new UploadVO(arg, UPD_ERR_MSG.DATE_CONVERT_ERROR.getMessage()); }
			 */
			return new UploadVO(arg, "");
		}
	},
	SIGUNGU_CD {
		public String getName() {
			return "시군구코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) {
					return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				}

				boolean isExistSigunguCd = UPLOAD_CHECK.SIGUNGU_CD.isExistSigunguCd(arg);
				if (!isExistSigunguCd) {
					return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
				}
			}
			return new UploadVO(arg, "");
		}
	},
	FCLT_LBL_NM {
		public String getName() {
			return "시설물라벨명칭";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 100) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("100"));
				if ((arg.length() <= 3 || arg.length() > 100)) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("4", "100"));
			}

			return new UploadVO(arg, "");
		}
	},
	FCLT_KND_CD {
		public String getName() {
			return "시설물종류코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			fcltKndCd = arg;
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!UPLOAD_CHECK.FCLT_KND_CD.isExistCode("FCLT_KND", arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}

			return new UploadVO(arg, "");
		}
	},
	FCLT_KND_DTL_CD {
		public String getName() {
			return "시설물종류세부코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!UPLOAD_CHECK.FCLT_KND_DTL_CD.isExistCode(fcltKndCd, arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}

			return new UploadVO(arg, "");
		}
	},
	SYS_CD {
		public String getName() {
			return "시스템코드";
		}

		public UploadVO validation(String arg) {
			sysCd = arg;
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 10) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("10"));
				// 시스템코드 NEW 20170330
				if (!UPLOAD_CHECK.SYS_CD.isExistSysCode("SYS_CD", arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}

			return new UploadVO(arg, "");
		}
	},
	FCLT_USED_TY_CD {
		public String getName() {
			return "시설물용도별유형코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!UPLOAD_CHECK.FCLT_USED_TY_CD.isExistFcltUsedTycd(arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}
			return new UploadVO(arg, "");
		}
	},
	FCLT_GDSDT_TY {
		public String getName() {
			return "시설물물품구분";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 1) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("1"));
				if (!"0".equals(arg) && !"1".equals(arg) && !"2".equals(arg)) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("0, 1, 2"));
				if (!UPLOAD_CHECK.FCLT_GDSDT_TY.isExistCode("FCLT_GDSDT_TY", arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}
			return new UploadVO(arg, "");
		}
	},
	LOTNO_ADRES_NM {
		public String getName() {
			return "지번주소명";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 200) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("200"));

			return new UploadVO(arg, "");
		}
	},
	ROAD_ADRES_NM {
		public String getName() {
			return "도로주소명";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 200) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("200"));

			return new UploadVO(arg, "");
		}
	},
	POINT_X {

		public String getName() {
			return "좌표X";
		}

		public UploadVO validation(String arg) throws Exception {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			// Double minVal = 127.21661091657;
			// Double maxVal = 127.577703206848;
			if (!"0".equals(String.valueOf(arg.length()))) {
				String strGisBoundsLeft = "";
				String strGisBoundsRight = "";

				Object obj = SessionUtil.getAttribute("cmConfig");
				if (obj != null && obj.getClass() == EgovMap.class) {
					EgovMap cmConfig = (EgovMap) obj;
					strGisBoundsLeft = EgovStringUtil.nullConvert(cmConfig.get(("gisExtentLeft")));
					strGisBoundsRight = EgovStringUtil.nullConvert(cmConfig.get(("gisExtentRight")));
				}

				Double minVal = Double.parseDouble(strGisBoundsLeft);
				Double maxVal = Double.parseDouble(strGisBoundsRight);
				Double pointX = Double.parseDouble((String) arg);
				if (pointX < minVal || pointX > maxVal) return new UploadVO(arg, UPD_ERR_MSG.RANGE_RESTRICT.getMessage(Double.toString(minVal), Double.toString(maxVal)));
				// if (arg.length() < 8) return new UploadVO(arg, UPD_ERR_MSG.DECIMAL_RESTRICT.getMessage("5"));
			}
			return new UploadVO(arg, "");
		}
	},
	POINT_Y {
		public String getName() {
			return "좌표Y";
		}

		public UploadVO validation(String arg) throws Exception {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			// if(arg == null || "".equals(arg)) return new UploadVO("", "");
			if (!"0".equals(String.valueOf(arg.length()))) {
				// Double minVal = 36.1585286082587;
				// Double maxVal = 36.5081779463163;
				String strGisBoundsTop = "";
				String strGisBoundsBottom = "";

				Object obj = SessionUtil.getAttribute("cmConfig");
				if (obj != null && obj.getClass() == EgovMap.class) {
					EgovMap cmConfig = (EgovMap) obj;
					strGisBoundsTop = EgovStringUtil.nullConvert(cmConfig.get("gisExtentTop"));
					strGisBoundsBottom = EgovStringUtil.nullConvert(cmConfig.get("gisExtentBottom"));
				}

				Double minVal = Double.parseDouble(strGisBoundsBottom);
				Double maxVal = Double.parseDouble(strGisBoundsTop);

				Double pointX = Double.parseDouble((String) arg);
				if (pointX < minVal || pointX > maxVal) return new UploadVO(arg, UPD_ERR_MSG.RANGE_RESTRICT.getMessage(Double.toString(minVal), Double.toString(maxVal)));
				// if (arg.length() < 8) return new UploadVO(arg, UPD_ERR_MSG.DECIMAL_RESTRICT.getMessage("5"));
			}
			return new UploadVO(arg, "");
		}
	},
	POINT_Z {
		public String getName() {
			return "좌표Z";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				Double minVal = -100.00000;
				Double maxVal = 3000.00000;
				Double pointX = Double.parseDouble((String) arg);
				if (pointX < minVal || pointX > maxVal) return new UploadVO(arg, UPD_ERR_MSG.RANGE_RESTRICT.getMessage(Double.toString(minVal), Double.toString(maxVal)));
			}
			return new UploadVO(arg, "");
		}
	},
	USE_TY_CD {
		public String getName() {
			return "사용유형코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 1) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("1"));
				if (!"Y".equals(arg) && !"N".equals(arg) && !"D".equals(arg)) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("Y, N, D"));
				if (!UPLOAD_CHECK.USE_TY_CD.isExistCode("USE_TY", arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}
			return new UploadVO(arg, "");
		}
	},
	FCLT_ICON_URL_0 {
		public String getName() {
			return "시설물아이콘정상";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 255) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("255"));
			return new UploadVO(arg, "");
		}
	},
	FCLT_ICON_URL_1 {
		public String getName() {
			return "시설물아이콘불량";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 255) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("255"));
			return new UploadVO(arg, "");
		}
	},
	FCLT_ICON_URL_2 {
		public String getName() {
			return "시설물아이콘미측정";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 255) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("255"));
			return new UploadVO(arg, "");
		}
	},
	FCLT_STTUS {
		public String getName() {
			return "시설물상태";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"".equals(arg)) arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 1) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("1"));
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (!"0".equals(arg) && !"1".equals(arg) && !"2".equals(arg)) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("0, 1, 2"));
				if (!UPLOAD_CHECK.FCLT_STTUS.isExistCode("FCLT_STTUS", arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}
			return new UploadVO(arg, "");
		}
	},
	FCLT_MNGR_TEL_NO {
		public String getName() {
			return "시설물관리자정연락처";
		}

		public UploadVO validation(String arg) {
			arg = arg.trim();
			arg = arg.replaceAll(" ", "");
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 20) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("20"));
				if (!arg.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('-')"));
				// if(!arg.matches("^(\\d{2,3})*\\)[),(,-](\\d{3,4})*[),(,-](\\d{4})$")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('-', ')', '(')"));
			}
			return new UploadVO(arg, "");
		}
	},
	FCLT_UID {
		public String getName() {
			return "시설물고유식별번호";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			// CTV이지만 고유아이디 안들어오는 경우가 있는 등 모두 체크가 불가능하므로 막음
			// if("0".equals(String.valueOf(arg.length())) && ("CTV".equals(fcltKndCd) || "AVI".equals(fcltKndCd))) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {

				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!(arg.length() >= 2 && arg.length() <= 40)) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("2", "40"));

				try {
					Double dbe = Double.parseDouble(arg);
					Integer itg = dbe.intValue();
					arg = itg + "";
				} catch (Exception e) {
				}

				if ("I".equals(rgsUpdYn)) { // 등록구분이 INSERT일 경우만 중복 오류 체크

					if (UPLOAD_CHECK.FCLT_UID.checkFcltUidForFacility(arg, sysCd)) { // 0이 아니면 TRUE가 되어 오류로 체크됨.
						return new UploadVO(arg, "시스템 중복 오류 : 시설물고유식별번호와 시스템코드가 이미 존재합니다.");
					}
					return new UploadVO(arg, "");
				}

			}

			return new UploadVO(arg, "");
		}
	},
	CONN_IP {
		public String getName() {
			return "접속아이피";
		}

		public UploadVO validation(String arg) {
			arg = arg.trim();
			arg = arg.replaceAll(" ", "");
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 255) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("255"));
			return new UploadVO(arg, "");
		}
	},
	CONN_PORT {
		public String getName() {
			return "접속포트";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 5) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("5"));
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));
			}
			return new UploadVO(arg, "");
		}
	},
	CONN_ID {
		public String getName() {
			return "접속아이디";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
			/*
			 * if(!"0".equals(String.valueOf(arg.length()))){ String strRtsp = ""; String strRemd = ""; String[] array; if(arg.length()>8){ strRtsp = arg.substring(0,7); strRemd = arg.substring(7); } if("rtsp://".equals(strRtsp.toLowerCase())){ //ip4와 ip6 구분 ':'가 1개 이상이면 ip6로 정함.
			 * array = strRemd.split(":"); if(strRemd.split(":").length > 1){ for(int i=0; i<array.length; i++){ if(!array[i].matches("[a-fA-F0-9]*")){ return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("a-f A-F 0-9, 구분자(':') (rtsp제외)")); } } }else{ array =
			 * strRemd.split("\\."); if(strRemd.split("\\.").length > 1){ for(int i=0; i<array.length; i++){ if(!array[i].matches("[0-9]*")){ return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('.') (rtsp제외)")); } } }else{ return new UploadVO(arg,
			 * UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('.') (rtsp제외)")); } //if(!strRemd.matches("^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$")){ // return new UploadVO(arg,
			 * UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('.') (rtsp제외)")); //} } }else{ array = arg.split(":"); if(arg.split(":").length > 1){ //ip6 for(int i=0; i<array.length; i++){ if(!array[i].matches("[a-fA-F0-9]*")){ return new UploadVO(arg,
			 * UPD_ERR_MSG.INPUT_RESTRICT.getMessage("a-f A-F 0-9, 구분자(':') (rtsp제외)")); } } }else{ //ip4 //if(!arg.matches("^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$")){ array = arg.split("\\.");
			 * if(arg.split("\\.").length > 1){ for(int i=0; i<array.length; i++){ if(!array[i].matches("[0-9]*")){ return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('.') (rtsp제외)")); }else{
			 * logger.debug("=======UPLOAD_CHECK==접속아이피4=else else ip4 숫자만 matches====array["+i+"]="+array[i]); } } }else{ return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('.') (rtsp제외)")); }
			 * //if(!arg.matches("^([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})$")){ // return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('.') (rtsp제외)")); //} } } } //Pattern p = Pattern.compile("rtsp://127.0.0.1"); //ip4에 대해서만 자세히 적용
			 * //if(!arg.matches("^([1-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])(\\.([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])){3}$")){ // return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('.')")); //} // String ip4 =
			 * "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$"; // String ip6 = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
			 */
			return new UploadVO(arg, "");
		}
	},
	CONN_PW {
		public String getName() {
			return "접속비밀번호";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			// 접속 비밀번호 - DB에 등록시에는 암호화 처리하여 저장
			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 200) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("200"));
			// 임시테이블에 암호화 적용 저장 안함.
			// arg = UPLOAD_CHECK.CONN_PW.encryptPassword(arg);
			return new UploadVO(arg, "");
		}
	},
	FCLT_MAC_ADRES {
		public String getName() {
			return "시설물MAC주소";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!arg.matches("^[a-zA-Z0-9.-]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 영문자, 구분자('.', '-')"));
			}
			// String mac = "^([0-9a-fA-F][0-9a-fA-F]:){5}([0-9a-fA-F][0-9a-fA-F])$";
			return new UploadVO(arg, "");
		}
	},
	GATE_WAY {
		public String getName() {
			return "게이트웨이";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (!arg.matches("^[a-zA-Z0-9.]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 영문자, 구분자('.')"));
				if (arg.length() > 15) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("15"));
			}

			return new UploadVO(arg, "");
		}
	},
	SUBNET {
		public String getName() {
			return "서브넷";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (!arg.matches("^[a-zA-Z0-9.]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 영문자, 구분자('.')"));
				if (arg.length() > 15) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("15"));
			}

			return new UploadVO(arg, "");
		}
	},
	FCLT_INSTL_YMD {
		public String getName() {
			return "시설물설치일";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 8) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("8"));
				if (arg.length() < 8) return new UploadVO(arg, UPD_ERR_MSG.FORMAT_RESTRICT.getMessage("yyyymmdd"));

				try {
					Double dbe = Double.parseDouble(arg);
					Integer itg = dbe.intValue();
					arg = itg + "";
					new SimpleDateFormat("yyyymmdd").parse(arg);
				} catch (Exception e) {
					return new UploadVO(arg, UPD_ERR_MSG.FORMAT_RESTRICT.getMessage("yyyymmdd"));
				}
			}
			return new UploadVO(arg, "");
		}
	},
	SVR_CONN_IP {
		public String getName() {
			return "서버접속아이피";
		}

		public UploadVO validation(String arg) {
			arg = arg.trim();
			arg = arg.replaceAll(" ", "");
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 255) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("255"));
			return new UploadVO(arg, "");
		}
	},
	SVR_CONN_PORT {
		public String getName() {
			return "서버접속포트";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 5) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("5"));
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));
			}

			return new UploadVO(arg, "");
		}
	},
	SVR_CONN_ID {
		public String getName() {
			return "서버접속아이디";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));

			return new UploadVO(arg, "");
		}
	},
	SVR_CONN_PW {
		public String getName() {
			return "서버접속비밀번호";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 200) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("200"));
			// 임시테이블에 암호화 적용 저장 안함.
			// arg = UPLOAD_CHECK.SVR_CONN_PW.encryptPassword(arg);

			return new UploadVO(arg, "");
		}
	},
	DVC_SEE_CCTV_IP {
		public String getName() {
			return "장치확인CCTV아이피";
		}

		public UploadVO validation(String arg) {
			arg = arg.trim();
			arg = arg.replaceAll(" ", "");
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 255) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("255"));
			return new UploadVO(arg, "");
		}
	},
	DVC_SEE_CCTV_PORT {
		public String getName() {
			return "장치확인CCTV포트";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 5) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("5"));
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));
			}

			return new UploadVO(arg, "");
		}
	},
	DVC_SEE_CCTV_ID {
		public String getName() {
			return "장치확인CCTV아이디";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));

			return new UploadVO(arg, "");
		}
	},
	DVC_SEE_CCTV_PW {
		public String getName() {
			return "장치확인CCTV비밀번호";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 200) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("200"));
			// 임시테이블에 암호화 적용 저장 안함.
			// arg = UPLOAD_CHECK.DVC_SEE_CCTV_PW.encryptPassword(arg);

			return new UploadVO(arg, "");
		}
	},
	TRA_LINK_ID {
		public String getName() {
			return "교통링크아이디";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));
			}

			return new UploadVO(arg, "");
		}
	},
	CTR_CD {
		public String getName() {
			return "센타코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!UPLOAD_CHECK.CTR_CD.isExistCode("UCP_ID", arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
				if (UPLOAD_CHECK.CTR_CD.isEqualsProps("Globals.SiteId", arg)) return new UploadVO(arg, "");
			}

			return new UploadVO(arg, "");
		}
	},
	DSTRT_CD {
		public String getName() {
			return "지구코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"".equals(arg)) arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!UPLOAD_CHECK.DSTRT_CD.isExistDstrtCd(arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}

			return new UploadVO(arg, "");
		}
	},
	ICON_GIS_DSP_YN {
		public String getName() {
			return "아이콘GIS표출여부";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 1) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("1"));
				if (!"Y".equals(arg) && !"N".equals(arg)) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("Y, N"));
			}

			return new UploadVO(arg, "");
		}
	},
	BEFORE_FCLT_ID {
		public String getName() {
			return "이전전시설물아이디";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));

			return new UploadVO(arg, "");
		}
	},
	LPR_CCTV_YN {
		public String getName() {
			return "번호인식카메라여부";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 1) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("1"));
				if (!"Y".equals(arg) && !"N".equals(arg)) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("Y, N"));
			}
			return new UploadVO(arg, "");
		}
	},
	PRNT_FCLT_ID {
		public String getName() {
			return "대표시설물아이디";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			// if(!UPLOAD_CHECK.PRNT_FCLT_ID.isExistPrntFclt(arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_PRNT.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.getBytes().length > 40) { // 한글입력시 byte계산
					arg = "중요 오류:" + arg.substring(0, 20);
					return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				}
				if (!UPLOAD_CHECK.PRNT_FCLT_ID.isExistPrntFclt(arg)) {
					return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_PRNT.getMessage());
				}
			}

			return new UploadVO(arg, "");
		}
	},
	CPLT_PRDT_TY {
		public String getName() {
			return "완제품구분";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 1) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("1"));
				if (!"S".equals(arg) && !"P".equals(arg)) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("S, P"));
				if (!UPLOAD_CHECK.CPLT_PRDT_TY.isExistCode("CPLT_PRDT", arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}

			return new UploadVO(arg, "");
		}
	},
	VT_POINT_TEL_NO {
		public String getName() {
			return "비상벨현장단말전화번호";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 26) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("26"));
				if (!arg.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('-')"));
				// if(!arg.matches("^(\\d{2,3})*\\)[),(,-](\\d{3,4})*[),(,-](\\d{4})$")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('-', ')', '(')"));
			}

			return new UploadVO(arg, "");
		}
	},
	VT_CENTER_TEL_NO {
		public String getName() {
			return "비상벨센터단말전화번호";
		}

		public UploadVO validation(String arg) {

			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 26) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("26"));
				if (!arg.matches("^\\d{2,3}-\\d{3,4}-\\d{4}$")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('-')"));
				// if(!arg.matches("^(\\d{2,3})*\\)[),(,-](\\d{3,4})*[),(,-](\\d{4})$")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자, 구분자('-', ')', '(')"));
			}
			return new UploadVO(arg, "");
		}
	},
	MNG_AREA_CD {
		public String getName() {
			return "관리지역코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!UPLOAD_CHECK.MNG_AREA_CD.isExistCode("UCP_ID", arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}

			return new UploadVO(arg, "");
		}
	},
	RGS_USER_ID {
		public String getName() {
			return "등록자아이디";
		}

		public UploadVO validation(String arg) {

			return new UploadVO(arg, "");
		}
	},
	RGS_DATE {
		public String getName() {
			return "등록일시";
		}

		public UploadVO validation(String arg) {

			return new UploadVO(arg, "");
		}
	},
	UPD_USER_ID {
		public String getName() {
			return "수정자아이디";
		}

		public UploadVO validation(String arg) {

			return new UploadVO(arg, "");
		}
	},
	UPD_DATE {
		public String getName() {
			return "수정일시";
		}

		public UploadVO validation(String arg) {

			return new UploadVO(arg, "");
		}
	},
	CCTV_VOD_ID {
		public String getName() {
			return "CCTV비디오아이디";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));

			return new UploadVO(arg, "");
		}
	},
	PLC_PTR_DIV_CD {
		public String getName() {
			return "경찰지구대";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"".equals(arg)) arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				// if(!UPLOAD_CHECK.PLC_PTR_DIV_CD.isExistCode("PLC_PTR_DIV", arg)) return new UploadVO(arg, UPD_ERR_MSG.NOT_EXIST_CODE.getMessage());
			}

			return new UploadVO(arg, "");
		}
	},
	CCTV_DUE_NT_AG {
		public String getName() {
			return "CCTV정북각도";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 3) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("3"));
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("중요 오류:숫자"));
			}

			return new UploadVO(arg, "");
		}
	},
	CCTV_OSVT_DSTC {
		public String getName() {
			return "CCTV감시거리";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 10) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("10"));
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));
			}

			return new UploadVO(arg, "");
		}
	},
	CCTV_OSVT_X {
		public String getName() {
			return "CCTV감시좌표X";
		}

		public UploadVO validation(String arg) throws Exception {

			// Logger logger = LoggerFactory.getLogger(this.getClass());
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));
				// if (arg.length() < 9 && !"0".equals(arg)) return new UploadVO(arg, UPD_ERR_MSG.DECIMAL_RESTRICT.getMessage("5"));
				// Double minVal = 127.21661091657;
				// Double maxVal = 127.577703206848;
				String strGisBoundsLeft = "";
				String strGisBoundsRight = "";

				Object obj = SessionUtil.getAttribute("cmConfig");
				if (obj != null && obj.getClass() == EgovMap.class) {
					EgovMap cmConfig = (EgovMap) obj;
					strGisBoundsLeft = EgovStringUtil.nullConvert(cmConfig.get("gisExtentLeft"));
					strGisBoundsRight = EgovStringUtil.nullConvert(cmConfig.get("gisExtentRight"));
				}

				Double minVal = Double.parseDouble(strGisBoundsLeft);
				Double maxVal = Double.parseDouble(strGisBoundsRight);
				Double pointX = Double.parseDouble((String) arg);
				// logger.debug("=======UPLOAD_CHECK=======CCTV_OSVT_X==strGisBoundsLeft="+strGisBoundsLeft);
				// logger.debug("=======UPLOAD_CHECK=======CCTV_OSVT_X==strGisBoundsRight="+strGisBoundsRight);
				// logger.debug("=======UPLOAD_CHECK=======CCTV_OSVT_X==pointX="+pointX);
				// logger.debug("=======UPLOAD_CHECK=======CCTV_OSVT_X=="+strGisBoundsLeft+"<"+pointX+"<"+strGisBoundsRight);
				if (!"0".equals(arg) && (pointX < minVal || pointX > maxVal)) return new UploadVO(arg, UPD_ERR_MSG.RANGE_RESTRICT.getMessage(Double.toString(minVal), Double.toString(maxVal)));
			}

			return new UploadVO(arg, "");
		}
	},
	CCTV_OSVT_Y {
		public String getName() {
			return "CCTV감시좌표Y";
		}

		public UploadVO validation(String arg) throws Exception {
			// Logger logger = LoggerFactory.getLogger(this.getClass());
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));
				// if (arg.length() < 9 && !"0".equals(arg)) return new UploadVO(arg, UPD_ERR_MSG.DECIMAL_RESTRICT.getMessage("5"));
				// Double minVal = 36.1585286082587;
				// Double maxVal = 36.5081779463163;
				String strGisBoundsTop = "";
				String strGisBoundsBottom = "";

				Object obj = SessionUtil.getAttribute("cmConfig");
				if (obj != null && obj.getClass() == EgovMap.class) {
					EgovMap cmConfig = (EgovMap) obj;
					strGisBoundsTop = EgovStringUtil.nullConvert(cmConfig.get("gisExtentTop"));
					strGisBoundsBottom = EgovStringUtil.nullConvert(cmConfig.get("gisExtentBottom"));
				}

				Double minVal = Double.parseDouble(strGisBoundsBottom);
				Double maxVal = Double.parseDouble(strGisBoundsTop);
				Double pointY = Double.parseDouble((String) arg);
				// logger.debug("=======UPLOAD_CHECK=======CCTV_OSVT_X=="+strGisBoundsBottom+"<"+pointY+"<"+strGisBoundsTop);
				if (!"0".equals(arg) && (pointY < minVal || pointY > maxVal)) return new UploadVO(arg, UPD_ERR_MSG.RANGE_RESTRICT.getMessage(Double.toString(minVal), Double.toString(maxVal)));
			}
			return new UploadVO(arg, "");
		}
	},
	CCTV_OSVT_Z {
		public String getName() {
			return "CCTV감시좌표Z";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				Double minVal = -100.00000;
				Double maxVal = 3000.00000;
				Double pointX = Double.parseDouble((String) arg);
				if (pointX < minVal || pointX > maxVal) return new UploadVO(arg, UPD_ERR_MSG.RANGE_RESTRICT.getMessage(Double.toString(minVal), Double.toString(maxVal)));
			}
			return new UploadVO(arg, "");
		}
	},
	CCTV_OSVT_AG {
		public String getName() {
			return "CCTV감시방향";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			arg = arg.replace(".0", "");

			if (!"0".equals(String.valueOf(arg.length())) && !arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("중요 오류:숫자"));

			return new UploadVO(arg, "");
		}
	},
	CCTV_HEIGHT {
		public String getName() {
			return "CCTV높이";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length())) && !arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));

			return new UploadVO(arg, "");
		}
	},
	CCTV_VIEW_AG {
		public String getName() {
			return "CCTV화각";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			arg = arg.replace(".0", "");
			if (!"0".equals(String.valueOf(arg.length())) && !arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));

			return new UploadVO(arg, "");
		}
	},
	EGB_YN {
		public String getName() {
			return "비상벨여부";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 1) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("1"));
				if (!"Y".equals(arg) && !"N".equals(arg)) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("Y, N"));
			}

			return new UploadVO(arg, "");
		}
	},
	ETC {
		public String getName() {
			return "기타";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 300) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("300"));

			return new UploadVO(arg, "");
		}
	},
	DONG_CD {
		public String getName() {
			return "행정동코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
			}

			return new UploadVO(arg, "");
		}
	},
	DONG_NM {
		public String getName() {
			return "행정동명";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 200) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("200"));

			return new UploadVO(arg, "");
		}
	},
	LG_DONG_CD {
		public String getName() {
			return "법정동코드";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("40"));
				if (!arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("숫자"));
			}
			return new UploadVO(arg, "");
		}
	},
	LG_DONG_NM {
		public String getName() {
			return "법정동명";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 200) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("200"));

			return new UploadVO(arg, "");
		}
	},
	CCTV_AG_YN {
		public String getName() {
			return "CCTV방향각지원여부";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() > 1) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("1"));
				if (!"Y".equals(arg) && !"N".equals(arg)) {
					return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("Y, N"));
				}
			}

			return new UploadVO(arg, "");
		}
	},
	CCTV_CHANNEL {
		public String getName() {
			return "CCTV체널";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			arg = arg.replace(".0", "");

			if (!"0".equals(String.valueOf(arg.length())) && !arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("중요 오류:숫자"));

			return new UploadVO(arg, "");
		}
	},
	PRESET_BDW_START_NUM {
		public String getName() {
			return "프리셋대역시작번호";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());
			arg = arg.replace(".0", "");

			if (!"0".equals(String.valueOf(arg.length())) && !arg.matches("[0-9]*")) return new UploadVO(arg, UPD_ERR_MSG.INPUT_RESTRICT.getMessage("중요 오류:숫자"));

			return new UploadVO(arg, "");
		}
	},
	LINK_VMS_UID {
		public String getName() {
			return "중계서버UID";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length())) && arg.length() > 100) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("100"));

			return new UploadVO(arg, "");
		}
	},
	MNG_SN {
		public String getName() {
			return "관리번호";
		}

		public UploadVO validation(String arg) {
			if (requierdColumn.contains(itemNmDelimiter + getName() + itemNmDelimiter)) if (arg == null || "".equals(arg)) return new UploadVO("", UPD_ERR_MSG.REQUIERD.getMessage());

			if (!"0".equals(String.valueOf(arg.length()))) {
				if (arg.length() <= 1 || arg.length() > 40) return new UploadVO(arg, UPD_ERR_MSG.DIGIT_RESTRICT.getMessage("2", "40"));
			}

			return new UploadVO(arg, "");
		}
	};

	@Resource(name="facilityIdGnr")
	private EgovIdGnrService fcltIdGnr;
	private UploadManageMapper uploadManageMapper;
	private EgovFileScrty egovFileScrty;
	private Properties config;
	static private String rgsUpdYn = "";
	static private String fcltKndCd = "";
	static private String sysCd = "";
	public String requierdColumn = "";
	public String itemNmDelimiter = "^";

	public abstract String getName();

	public abstract UploadVO validation(String arg) throws Exception;

	public int getIndex() {
		return ordinal();
	}

	public void setResource(UploadManageMapper uploadManageMapper, EgovFileScrty egovFileScrty, Properties config, String requierdColumn) {
		this.uploadManageMapper = uploadManageMapper;
		this.egovFileScrty = egovFileScrty;
		this.config = config;
		this.requierdColumn = requierdColumn;
	}

	public boolean isExistDstrtCd(String dstrtCd) {
		try {
			if (uploadManageMapper.checkDstrtCd(dstrtCd) == 0) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean isExistSigunguCd(String sigunguCd) {
		try {
			if (sigunguCd != null && sigunguCd.length() == 5) {
				int lastIndexOf = sigunguCd.lastIndexOf("0");
				if (lastIndexOf > -1) {
					sigunguCd = sigunguCd.replace("0", "%");
				}
			}

			int n = uploadManageMapper.checkSigunguCode(sigunguCd);
			if (n == 0) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean isExistSysCode(String cdGrpId, String cdId) {
		CmmCodeVO vo = new CmmCodeVO();
		try {
			vo.setgCodeId(cdGrpId);
			vo.setCodeId(cdId);
			if (uploadManageMapper.checkSysCode(vo) == 0) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean isExistCode(String cdGrpId, String cdId) {

		CmmCodeVO vo = new CmmCodeVO();
		try {
			vo.setgCodeId(cdGrpId);
			vo.setCodeId(cdId);
			if (uploadManageMapper.checkCode(vo) == 0) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// 시설물용도별유형코드 validation check
	public boolean isExistFcltUsedTycd(String codeId) {
		try {
			if (uploadManageMapper.checkFcltUsedTyCd(codeId) == 0) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean isExistPrntFclt(String fcltId) {
		try {
			int x = uploadManageMapper.checkPrntFcltForFacility(fcltId);
			int y = uploadManageMapper.checkPrntFcltForUpload(fcltId);
			if ((x + y) == 0) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean isEqualsProps(String key, String value) {
		try {
			if (config.getProperty(key).equals(value)) return true;
			else return false;
		} catch (Exception e) {
			return false;
		}
	}

	public String selectFcltSeq() {
		try {
			// return uploadManageMapper.selectFcltSeq(System.currentTimeMillis()+"");

			String nextFcltSeq = "";
			String dbType = EgovStringUtil.nullConvert(config.get("Globals.dbType"));
			if ("maria_mysql".equals(dbType)) {
				nextFcltSeq = fcltIdGnr.getNextStringId();
			} else {
				nextFcltSeq = uploadManageMapper.selectFcltSeq2(System.currentTimeMillis() + "");
			}
			return nextFcltSeq;
		} catch (Exception e) {
			return "";
		}
	}

	public int duplicateFclt(String fcltId) {
		try {
			return uploadManageMapper.duplicateFclt(fcltId);
		} catch (Exception e) {
			return 0;
		}
	}

	public String encryptPassword(String password) {
		try {
			return egovFileScrty.encryptPassword(password);
		} catch (Exception e) {
			return "";
		}
	}
	/*
	 * 1) 숫자만 : ^[0-9]*$ 2) 영문자만 : ^[a-zA-Z]*$ 3) 한글만 : ^[가-힣]*$ 4) 영어 & 숫자만 : ^[a-zA-Z0-9]*$ 5) E-Mail : ^[a-zA-Z0-9]+@[a-zA-Z0-9]+$ 6) 휴대폰 : ^01(?:0|1|[6-9]) - (?:\d{3}|\d{4}) - \d{4}$ 7) 일반전화 : ^\d{2.3} - \d{3,4} - \d{4}$ 8) 주민등록번호 : \d{6} \- [1-4]\d{6} 9) IP 주소 : ([0-9]{1,3})
	 * \. ([0-9]{1,3}) \. ([0-9]{1,3}) \. ([0-9]{1,3}) IPV4 - "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$" IPV6 - "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$"
	 */

	public boolean isExistUpdFaltId(String faltId) {
		try {
			if (uploadManageMapper.checkFcltForFacility(faltId) == 0) return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean checkFcltUidForFacility(String cdGrpId, String cdId) {

		CmmCodeVO vo = new CmmCodeVO();

		try {
			vo.setgCodeId(cdGrpId);
			vo.setCodeId(cdId);

			if (uploadManageMapper.checkFcltUid(vo) == 0) {
				// if(uploadManageMapper.isExistFcltUidForFacility(vo) == 0) {
				return false;
			}

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String gisBounds(String key) {
		String str = "0";
		try {
			// 좌표X, 좌표Y, CCTV감시좌표X, CCTV감시좌표Y
			str = config.getProperty(key);
			return str;
		} catch (Exception e) {
			return str;
		}
	}

}
