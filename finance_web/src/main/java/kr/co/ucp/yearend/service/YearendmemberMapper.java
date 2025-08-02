package kr.co.ucp.yearend.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("yearendmemberMapper")
public interface YearendmemberMapper {

	List<Map<String, String>> selectMember(Map<String, String> args) throws Exception;

}
