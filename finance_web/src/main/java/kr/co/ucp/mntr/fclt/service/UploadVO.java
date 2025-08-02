package kr.co.ucp.mntr.fclt.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public class UploadVO {

	private String updUserId = "";

	private int totCnt = 0;
	private int norCnt = 0;
	private int errCnt = 0;
	private String errorMsg = "";
	private List<Map<Integer, Object>> dataList;
	private List<EgovMap> eMapList;
	private List<UpdErrorVO> errorList;
	private MultipartFile file;
	private String value = "";
	private String orgColumnId = "";
	private String orgColumn = "";
	private String orgColumnChk = "";
	private String errColumn = "";
	private String columnNm = "";
	private String columnId = "";
	private String columnIdUpload= "";
	private String[] columnIdUploads = null;
	private String columnNmUpload = "";
	private int insertCnt = 0;
	private int updateCnt = 0;
	private int deleteCnt = 0;

	private int allCnt = 0;
	private int regCnt = 0;
	private int updCnt = 0;
	private int delCnt = 0;
	private String uplCol = "";
	String nextFcltSeq = "";

	public UploadVO(){}

	public UploadVO(String value, String errorMsg) {
		this.value = value;
		this.errorMsg = errorMsg;
	}

	public String getUpdUserId() {
		return updUserId;
	}
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}

	public int getTotCnt() {
		return totCnt;
	}
	public void setTotCnt(int totCnt) {
		this.totCnt = totCnt;
	}
	public int getNorCnt() {
		return norCnt;
	}

	public void setNorCnt(int norCnt) {
		this.norCnt = norCnt;
	}

	public int getInsertCnt() {
		return insertCnt;
	}

	public void setInsertCnt(int insertCnt) {
		this.insertCnt = insertCnt;
	}

	public int getUpdateCnt() {
		return updateCnt;
	}

	public void setUpdateCnt(int updateCnt) {
		this.updateCnt = updateCnt;
	}

	public int getDeleteCnt() {
		return deleteCnt;
	}

	public void setDeleteCnt(int deleteCnt) {
		this.deleteCnt = deleteCnt;
	}

	public int getErrCnt() {
		return errCnt;
	}
	public void setErrCnt(int errCnt) {
		this.errCnt = errCnt;
	}
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOrgColumn() {
		return orgColumn;
	}

	public void setOrgColumn(String orgColumn) {
		this.orgColumn = orgColumn;
	}

	public String getOrgColumnChk() {
		return orgColumnChk;
	}

	public void setOrgColumnChk(String orgColumnChk) {
		this.orgColumnChk = orgColumnChk;
	}

	public String getErrColumn() {
		return errColumn;
	}

	public void setErrColumn(String errColumn) {
		this.errColumn = errColumn;
	}

	public String getColumnNm() {
		return columnNm;
	}

	public void setColumnNm(String columnNm) {
		this.columnNm = columnNm;
	}

	public String getOrgColumnId() {
		return orgColumnId;
	}

	public void setOrgColumnId(String orgColumnId) {
		this.orgColumnId = orgColumnId;
	}

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}

	public String getColumnIdUpload() {
		return columnIdUpload;
	}

	public void setColumnIdUpload(String columnIdUpload) {
		this.columnIdUpload = columnIdUpload;
	}

	public String[] getColumnIdUploads() {
		return columnIdUploads;
	}

	public void setColumnIdUploads(String[] columnIdUploads) {
		this.columnIdUploads = columnIdUploads;
	}

	public String getColumnNmUpload() {
		return columnNmUpload;
	}

	public void setColumnNmUpload(String columnNmUpload) {
		this.columnNmUpload = columnNmUpload;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public List<UpdErrorVO> getErrorList() {
		return errorList;
	}

	public void setErrorList(List<UpdErrorVO> errorList) {
		this.errorList = errorList;
	}

	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public List<Map<Integer, Object>> getDataList() {
		return dataList;
	}
	public void setDataList(List<Map<Integer, Object>> dataList) {
		this.dataList = dataList;
	}
	public List<EgovMap> geteMapList() {
		return eMapList;
	}
	public void seteMapList(List<EgovMap> eMapList) {
		this.eMapList = eMapList;
	}

	public int getAllCnt() {
		return allCnt;
	}
	public void setAllCnt(int allCnt) {
		this.allCnt = allCnt;
	}

	public int getRegCnt() {
		return regCnt;
	}
	public void setRegCnt(int regCnt) {
		this.regCnt = regCnt;
	}

	public int getUpdCnt() {
		return updCnt;
	}
	public void setUpdCnt(int updCnt) {
		this.updCnt = updCnt;
	}

	public int getDelCnt() {
		return delCnt;
	}
	public void setDelCnt(int delCnt) {
		this.delCnt = delCnt;
	}

	public String getUplCol() {
		return uplCol;
	}
	public void setUplCol(String uplCol) {
		this.uplCol = uplCol;
	}

	public String getNextFcltSeq() {
		return nextFcltSeq;
	}

	public void setNextFcltSeq(String nextFcltSeq) {
		this.nextFcltSeq = nextFcltSeq;
	}

	@Override
	public String toString() {
		return "UploadVO [updUserId=" + updUserId + ", totCnt=" + totCnt + ", norCnt=" + norCnt + ", errCnt=" + errCnt
				+ ", errorMsg=" + errorMsg + ", dataList=" + dataList + ", eMapList=" + eMapList + ", errorList="
				+ errorList + ", file=" + file + ", value=" + value + ", orgColumnId=" + orgColumnId + ", orgColumn="
				+ orgColumn + ", orgColumnChk=" + orgColumnChk + ", errColumn=" + errColumn + ", columnNm=" + columnNm
				+ ", columnId=" + columnId + ", columnIdUpload=" + columnIdUpload + ", insertCnt=" + insertCnt
				+ ", updateCnt=" + updateCnt + ", deleteCnt=" + deleteCnt + ", allCnt=" + allCnt + ", regCnt=" + regCnt
				+ ", updCnt=" + updCnt + ", delCnt=" + delCnt + ", uplCol=" + uplCol + ", nextFcltSeq=" + nextFcltSeq
				+ "]";
	}
}
