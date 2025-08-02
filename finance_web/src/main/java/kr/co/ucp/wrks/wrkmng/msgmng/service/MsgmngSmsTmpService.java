package kr.co.ucp.wrks.wrkmng.msgmng.service;

import java.util.List;
import java.util.Map;

public interface MsgmngSmsTmpService {

    /**
     * SMS 임시 조건 검색
     * @param args
     * @return
     * @throws Exception
     */
	List<Map<String, String>> list(Map<String, String> args) throws Exception;

    /**
     * SMS 임시 수신자 상세 조회
     * @param args
     * @return
     * @throws Exception
     */
	List<Map<String, String>> list_rcv(Map<String, String> args) throws Exception;

    /**
     * SMS 임시 등록
     * @param map
     * @param rcvList
     * @return
     * @throws Exception
     */
	int insert(Map<String, Object> map, List<Map<String, String>> rcvList) throws Exception;

    /**
     * SMS 임시 수정
     * @param map
     * @param rcvList
     * @return
     * @throws Exception
     */
	int update(Map<String, Object> map, List<Map<String, String>> rcvList) throws Exception;

    /**SMS 임시 삭제
     *
     * @param map
     * @throws Exception
     */
	void delete(Map<String, Object> map) throws Exception;

	String select_sms_sms_id() throws Exception;

    /**
     * SMS 전송 등록
     * @param map
     * @param rcvList
     * @return
     * @throws Exception
     */
	int insertSmsMng(Map<String, Object> map, List<Map<String, String>> rcvList) throws Exception;

    /**
     * SMS 전송 등록
     * @param map
     * @param rcvList
     * @return
     * @throws Exception
     */
	int updateSmsStatusOk(Map<String, Object> map);
    /**
     * SMS 수신자 추가
     * @param args
     * @return
     * @throws Exception
     */
	List<Map<String, String>> grpList(Map<String, String> args) throws Exception;


}
