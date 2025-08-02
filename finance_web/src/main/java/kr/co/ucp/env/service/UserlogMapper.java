package kr.co.ucp.env.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;

@Mapper("userlogMapper")
public interface UserlogMapper {

	List<Map<String, String>> selectUserlog(Map<String, String> args) throws Exception;

}
