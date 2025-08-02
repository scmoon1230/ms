package kr.co.ucp.mntr.fclt.service;

public class UpdErrorVO {

	private int rowNum;
	private int cellNum;
	private String columnName;
	private Object columnValue;
	private String errorMsg;
	
	public UpdErrorVO(int rowNum, int cellNum, String columnName, Object columnValue, String errorMsg){
		this.rowNum = rowNum;
		this.cellNum = cellNum;
		this.columnName = columnName;
		this.columnValue = columnValue;
		this.errorMsg = errorMsg;
	}
	
	public int getRowNum() {
		return rowNum;
	}
	public void setRowNum(int rowNum) {
		this.rowNum = rowNum;
	}
	public int getCellNum() {
		return cellNum;
	}
	public void setCellNum(int cellNum) {
		this.cellNum = cellNum;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public Object getColumnValue() {
		return columnValue;
	}

	public void setColumnValue(Object columnValue) {
		this.columnValue = columnValue;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
