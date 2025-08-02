package kr.co.ucp.mntr.main.service;

import java.util.List;
import java.util.Map;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Repository;

@Mapper("divMapper")
@Repository
public interface DivMapper {

    List<EgovMap> selectEventList(DivVO vo) throws Exception;

    List<DivVO> selectDivList(DivVO vo) throws Exception;

    List<EgovMap> selectGrpAuthLvlList(Map<String, Object> params) throws Exception;

    int mergeDivNormalPosition(DivVO vo) throws Exception;

    int mergeDivSituationPosition(DivVO vo) throws Exception;

    int mergeDivSituationPositionGrpAuthLvl(DivVO vo) throws Exception;

    int deleteGrpAuthLvl(Map<String, Object> params) throws Exception;

    List<EgovMap> selectGeneralDivList(DivVO vo) throws Exception;

    List<EgovMap> selectGeneralDivLcList(DivVO vo) throws Exception;

    List<EgovMap> selectEventBaseDivList(DivVO vo) throws Exception;

    List<EgovMap> selectEventBaseDivLcList(DivVO vo) throws Exception;

    List<EgovMap> selectEventDivList(DivVO vo) throws Exception;

    List<EgovMap> selectEventDivLcList(DivVO vo) throws Exception;

    List<EgovMap> selectEventDivEvtGrpLvlLcList(DivVO vo) throws Exception;

    int deleteDivEvt(DivVO vo) throws Exception;

    int deleteDivEvtGrpLvl(DivVO vo) throws Exception;
}
