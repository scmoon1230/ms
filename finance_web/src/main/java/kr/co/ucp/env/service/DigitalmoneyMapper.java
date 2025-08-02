package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("digitalmoneyMapper")
public interface DigitalmoneyMapper {

	List<Map<String, String>> selectDigitalmoney(Map<String, String> args) throws Exception;

	List<Map<String, String>> selectDigitalmoneyExt(Map<String, String> args) throws Exception;

	int insertDigitalmoney(Map<String, Object> args) throws Exception;

	int updateDigitalmoney(Map<String, Object> args) throws Exception;

	int deleteDigitalmoney(Map<String, String> args) throws Exception;

}
