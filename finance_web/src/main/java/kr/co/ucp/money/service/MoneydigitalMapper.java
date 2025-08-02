package kr.co.ucp.money.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("moneydigitalMapper")
public interface MoneydigitalMapper {

	List<Map<String, String>> selectMastUpload(Map<String, String> args) throws Exception;

	String selectTotalAmnt(Map<String, String> args) throws Exception;

	Map<String, String> selectMastUploadOne(Map<String, String> args) throws Exception;

	int insertMastUpload(Map<String, String> args) throws Exception;

	int updateMastUpload(Map<String, String> args) throws Exception;

	int deleteMastUpload(Map<String, String> args) throws Exception;

	String selectNewDetSeq(Map<String, String> args) throws Exception;

	int insertMastMoney(Map<String, String> args) throws Exception;

}
