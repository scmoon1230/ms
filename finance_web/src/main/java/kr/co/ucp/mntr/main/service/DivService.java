/**
 * ----------------------------------------------------------------------------------------------
 *
 * @Class Name : DivSetMntrgService.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP.CNU All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2014. 11. 6. SaintJuny 최초작성
 * <p>
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.main.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.util.EgovMap;

public interface DivService {
    List<EgovMap> selectEventList(DivVO vo) throws Exception;

    List<DivVO> selectDivList(DivVO vo) throws Exception;

    int mergeDivNormalPosition(DivVO vo) throws Exception;

    int mergeDivSituationPosition(DivVO vo) throws Exception;

    List<EgovMap> selectGrpAuthLvlList(Map<String, Object> params) throws Exception;

    int mergeDivSituationPositionGrpAuthLvl(DivVO vo) throws Exception;

    int deleteGrpAuthLvl(Map<String, Object> params) throws Exception;

    List<EgovMap> selectGeneralDivList(DivVO vo) throws Exception;

    List<EgovMap> selectGeneralDivLcList(DivVO vo) throws Exception;

    List<EgovMap> selectEventBaseDivList(DivVO vo) throws Exception;

    List<EgovMap> selectEventBaseDivLcList(DivVO vo) throws Exception;

    List<EgovMap> selectEventDivList(DivVO vo) throws Exception;

    List<EgovMap> selectEventDivLcList(DivVO vo) throws Exception;

}
