package kr.co.ucp.wrks.wrkmng.msgmng.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 
 * ----------------------------------------------------------
 * @Class Name : MsgmngSmsTmpDAO.java
 * @Description : SMS 임시 DAO
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
@Repository("msgmngSmsTmpDAO")
public class MsgmngSmsTmpDAO extends EgovAbstractMapper {
    
    /**
     * SMS 임시 조건검색
     * @param args
     * @return
     */
    public List<Map<String, String>> list(Map<String, String> args) {
        return selectList("wrks_wrkmng_msgmng_sms_tmp.list", args);
    }
    
    /**
     * SMS 수신자 조회
     * @param args
     * @return
     */
    public List<Map<String, String>> list_rcv(Map<String, String> args) {
        return selectList("wrks_wrkmng_msgmng_sms_tmp.list_rcv", args);
    }
    
    /**
     * SMS 임시 등록
     * @param map
     * @param rcvList
     * @return
     */
    public int insert(Map<String, Object> map, List<Map<String, String>> rcvList) {
        int insertResult = 0;
        
        /* SMS */
        insertResult = insert("wrks_wrkmng_msgmng_sms_tmp.insert_sms", map);
        
        /* RCV */
        for(int i=0; i<rcvList.size(); i++){
            Map<String, String> arg = rcvList.get(i);
            insertResult = insert("wrks_wrkmng_msgmng_sms_tmp.insert_rcv", arg);    
        }
        
        return insertResult;
    }
    
    /**
     * SMS 임시 수정
     * @param map
     * @param rcvList
     * @return
     */
    public int update(Map<String, Object> map, List<Map<String, String>> rcvList) {
        int updateResult = 0;
        
        /* SMS 임시 수정*/
        updateResult = insert("wrks_wrkmng_msgmng_sms_tmp.update_sms", map);
        
        /* SMS 수신자 정보 삭제 */
        updateResult = delete("wrks_wrkmng_msgmng_sms_tmp.delete_rcv", map);
        
        /* SMS 수신자 정보 등록 */
        for(int i=0; i<rcvList.size(); i++){
            Map<String, String> arg = rcvList.get(i);
            updateResult = insert("wrks_wrkmng_msgmng_sms_tmp.insert_rcv", arg);    
        }
        
        return updateResult;
    }
    
    /**
     * SMS 임시 삭제
     * @param map
     */
    public void delete(Map<String, Object> map) {
        /* SMS 수신자 정보 삭제 */
        delete("wrks_wrkmng_msgmng_sms_tmp.delete_rcv", map);
        
        /* SMS 임시 삭제 */
        delete("wrks_wrkmng_msgmng_sms_tmp.delete_sms", map);
    }
    
    /**
     * SMS 임시 ID얻기
     * @return
     */
    public String select_sms_sms_id() {
        return selectOne("select_sms_sms_id");
    }
    
    /**
     * SMS 임시 수정
     * @param map
     * @return
     */
    public int updateSmsSend(Map<String, Object> map) {
        return update("wrks_wrkmng_msgmng_sms_tmp.update_sms", map);
    }

    /**
     * SMS 임시 수정
     * @param map
     * @return
     */
    public int updateSmsStatusOk(Map<String, Object> map) {
    	return update("wrks_wrkmng_msgmng_sms_tmp.update_sms_status_ok", map);
    }
    
}
