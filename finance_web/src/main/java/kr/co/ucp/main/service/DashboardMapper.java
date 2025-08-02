package kr.co.ucp.main.service;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DashboardMapper {

    EgovMap selectTvoViewState();
    EgovMap selectTvoViewExtnState();
    EgovMap selectTvoOutState();
    EgovMap selectTvoOutExtnState();

    EgovMap selectTvoCctvState();

    EgovMap selectTvoViewAprvStateMonthly();
    EgovMap selectTvoOutAprvStateMonthly();

    List<EgovMap> selectTvoViewAprvStateDaily();
    List<EgovMap> selectTvoOutAprvStateDaily();
    
}
