package kr.co.ucp.yearend.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("yearendmoneyMapper")
public interface YearendmoneyMapper {

	List<Map<String, String>> selectMoney(Map<String, String> args) throws Exception;

}
