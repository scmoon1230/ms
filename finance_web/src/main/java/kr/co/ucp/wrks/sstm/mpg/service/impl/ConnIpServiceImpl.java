package kr.co.ucp.wrks.sstm.mpg.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.wrks.sstm.mpg.service.ConnIpMapper;
import kr.co.ucp.wrks.sstm.mpg.service.ConnIpService;

@Service("connIpService")
public class ConnIpServiceImpl implements ConnIpService
{
	@Resource(name="connIpMapper")
	private ConnIpMapper connIpMapper;

	@Override
	public List<Map<String, String>> selectConnIpInfoList(Map<String, String> args) throws Exception {
		return connIpMapper.selectConnIpInfoList(args);
	}
	
	@Override
	public int insertConnIpInfo(Map<String, Object> args) throws Exception {
		return connIpMapper.insertConnIpInfo(args);
	}
	
	@Override
	public int updateConnIpInfo(Map<String, Object> args) throws Exception {
		return connIpMapper.updateConnIpInfo(args);
	}
	
	@Override
	public int deleteConnIpInfo(Map<String, Object> args) throws Exception {
		return connIpMapper.deleteConnIpInfo(args);
	}
	
	@Override
    public int deleteConnIpInfoMulti(List<Map<String, Object>> args) throws Exception {
    	int ret = 0;
    	for(int i = 0; i < args.size(); i++) {
    		Map<String, Object> arg = (Map<String, Object>) args.get(i);
    		ret = connIpMapper.deleteConnIpInfo(arg);
    	}
    	return ret;
    }
	
}