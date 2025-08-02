package kr.co.ucp.wrks.wrkmng.msgmng.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import kr.co.ucp.wrks.wrkmng.msgmng.service.MsgmngSmsTmpMapper;
import kr.co.ucp.wrks.wrkmng.msgmng.service.MsgmngSmsTmpService;

/**
 *
 * ----------------------------------------------------------
 * @Class Name : MsgmngSmsTmpServiceImpl.java
 * @Description : SMS 임시 Service 구현클래스
 * @Version : 1.0
 * Copyright (c) 2015 by KR.CO.UCP.WORK All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------
 * DATA           AUTHOR   DESCRIPTION
 * ----------------------------------------------------------
 * 2015. 11. 4.   pjm       최초작성
 *
 * ----------------------------------------------------------
 *
 */
@Service("msgmngSmsTmpService")
public class MsgmngSmsTmpServiceImpl implements MsgmngSmsTmpService {

    @Resource(name="msgmngSmsTmpMapper")
    private MsgmngSmsTmpMapper msgmngSmsTmpMapper;

    /**
     * SMS 임시 조건 검색
     * @param args
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> list(Map<String, String> args)
            throws Exception {
        return msgmngSmsTmpMapper.list(args);
    }

    /**
     * SMS 임시 수신자 상세 조회
     * @param args
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, String>> list_rcv(Map<String, String> args)
            throws Exception {
        return msgmngSmsTmpMapper.list_rcv(args);
    }

    /**
     * SMS 임시 등록
     * @param map
     * @param rcvList
     * @return
     * @throws Exception
     */
    @Override
    public int insert(Map<String, Object> map, List<Map<String, String>> rcvList)
            throws Exception {
        return msgmngSmsTmpMapper.insert_sms(map);
    }

    /**
     * SMS 임시 수정
     * @param map
     * @param rcvList
     * @return
     * @throws Exception
     */
    @Override
    public int update(Map<String, Object> map, List<Map<String, String>> rcvList)
            throws Exception {

    	int ret = 0;

    	ret =  msgmngSmsTmpMapper.update(map);
    	msgmngSmsTmpMapper.delete(map);

    	for(int i=0; i<rcvList.size(); i++){
			Map<String, String> arg = rcvList.get(i);
			ret = msgmngSmsTmpMapper.insert_rcv(arg);
		}

    	return ret;
//        return msgmngSmsTmpMapper.update(map);
    }

    /**SMS 임시 삭제
     *
     * @param map
     * @throws Exception
     */
    @Override
    public void delete(Map<String, Object> map)
            throws Exception {
        msgmngSmsTmpMapper.delete(map);
    }

    /**
     * SMS 임시 ID얻기
     * @return
     * @throws Exception
     */
    @Override
    public String select_sms_sms_id()
            throws Exception {
        return msgmngSmsTmpMapper.select_sms_sms_id();
    }

    /**
     * SMS 전송 등록
     * @param map
     * @param rcvList
     * @return
     * @throws Exception
     */
    @Override
    public int insertSmsMng(Map<String, Object> map, List<Map<String, String>> rcvList)
            throws Exception {

    	int ret = 0;

    	ret =  msgmngSmsTmpMapper.insert_sms(map);

    	for(int i=0; i<rcvList.size(); i++){
			Map<String, String> arg = rcvList.get(i);
			ret = msgmngSmsTmpMapper.insert_rcv(arg);
		}
    	return ret;
    }

    /**
     * SMS 전송
     * @param map
     * @param rcvList
     * @return
     * @throws Exception
     */
	@Override
	public int updateSmsStatusOk(Map<String, Object> map) {
		return msgmngSmsTmpMapper.update_sms_status_ok(map);
	}

	@Override
	public List<Map<String, String>> grpList(Map<String, String> args)
			throws Exception {
		// TODO Auto-generated method stub
		return msgmngSmsTmpMapper.list_grp(args);
	}

}
