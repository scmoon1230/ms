package kr.co.ucp.main.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

public interface DashboardService {

    EgovMap selectTvoViewState();
    EgovMap selectTvoViewExtnState();
    EgovMap selectTvoOutState();
    EgovMap selectTvoOutExtnState();

    EgovMap selectTvoCctvState();

    List<EgovMap> selectTvoViewAprvStateDaily();
    List<EgovMap> selectTvoOutAprvStateDaily();

    EgovMap selectTvoViewAprvStateMonthly();
    EgovMap selectTvoOutAprvStateMonthly();

    EgovMap getStorageState();
}
