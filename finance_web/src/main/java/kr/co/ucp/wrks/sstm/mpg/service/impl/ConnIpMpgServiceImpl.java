package kr.co.ucp.wrks.sstm.mpg.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.wrks.sstm.mpg.service.ConnIpMpgMapper;
import kr.co.ucp.wrks.sstm.mpg.service.ConnIpMpgService;

@Service("connIpMpgService")
public class ConnIpMpgServiceImpl implements ConnIpMpgService
{
	@Resource(name="connIpMpgMapper")
	private ConnIpMpgMapper connIpMpgMapper;

	@Override
	public List<Map<String, String>> selectConnIpMpgInfoList(Map<String, String> args) throws Exception {
		return connIpMpgMapper.selectConnIpMpgInfoList(args);
	}
	
	@Override
	public int insertConnIpMpgInfo(Map<String, Object> args) throws Exception {
		return connIpMpgMapper.insertConnIpMpgInfo(args);
	}
	
	@Override
	public int updateConnIpMpgInfo(Map<String, Object> args) throws Exception {
		return connIpMpgMapper.updateConnIpMpgInfo(args);
	}
	
	@Override
	public int deleteConnIpMpgInfo(Map<String, Object> args) throws Exception {
		return connIpMpgMapper.deleteConnIpMpgInfo(args);
	}
	
	@Override
    public int deleteConnIpMpgInfoMulti(List<Map<String, Object>> args) throws Exception {
    	int ret = 0;
    	for(int i = 0; i < args.size(); i++) {
    		Map<String, Object> arg = (Map<String, Object>) args.get(i);
    		ret = connIpMpgMapper.deleteConnIpMpgInfo(arg);
    	}
    	return ret;
    }
	
}