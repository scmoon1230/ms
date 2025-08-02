/**
 * ----------------------------------------------------------------------------------------------
 *
 * @Class Name : DivSetMntrgServiceImpl.java
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
package kr.co.ucp.mntr.main.service.impl;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.egov.com.utl.fcc.service.EgovStringUtil;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import kr.co.ucp.mntr.main.service.DivMapper;
import kr.co.ucp.mntr.main.service.DivService;
import kr.co.ucp.mntr.main.service.DivVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("divService")
public class DivServiceImpl implements DivService {

    @Resource(name = "divMapper")
    private DivMapper divMapper;

    @Override
    public List<DivVO> selectDivList(DivVO vo) throws Exception {
        return divMapper.selectDivList(vo);
    }

    @Override
    @Transactional
    public int mergeDivNormalPosition(DivVO vo) throws Exception {
        int r = 0;
        if ("N".equals(vo.getDivLc())) {
            r = divMapper.deleteDivEvt(vo);
        } else {
            r = divMapper.mergeDivNormalPosition(vo);
        }
        return r;
    }

    @Override
    @Transactional
    public int mergeDivSituationPosition(DivVO vo) throws Exception {
        vo.setUserId(SessionUtil.getUserId());
        int r = 0;
        if ("N".equals(vo.getDivLc())) {
            r = divMapper.deleteDivEvt(vo);
        } else {
            r = divMapper.mergeDivSituationPosition(vo);
        }
        return r;
    }

    @Override
    public int mergeDivSituationPositionGrpAuthLvl(DivVO vo) throws Exception {
        int r = 0;
        if ("N".equals(vo.getDivLc())) {
            r = divMapper.deleteDivEvtGrpLvl(vo);
        } else {
            r = divMapper.mergeDivSituationPositionGrpAuthLvl(vo);
        }
        return r;
    }

    @Override
    public List<EgovMap> selectEventList(DivVO vo) throws Exception {
        return divMapper.selectEventList(vo);
    }

    @Override
    public List<EgovMap> selectGrpAuthLvlList(Map<String, Object> params) throws Exception {
        return divMapper.selectGrpAuthLvlList(params);
    }

    @Override
    public int deleteGrpAuthLvl(Map<String, Object> params) throws Exception {
        return divMapper.deleteGrpAuthLvl(params);
    }

    @Override
    public List<EgovMap> selectGeneralDivList(DivVO vo) throws Exception {
        return divMapper.selectGeneralDivList(vo);
    }

    @Override
    public List<EgovMap> selectGeneralDivLcList(DivVO vo) throws Exception {
        return divMapper.selectGeneralDivLcList(vo);
    }

    @Override
    public List<EgovMap> selectEventBaseDivList(DivVO vo) throws Exception {
        return divMapper.selectEventBaseDivList(vo);
    }

    @Override
    public List<EgovMap> selectEventBaseDivLcList(DivVO vo) throws Exception {
        return divMapper.selectEventBaseDivLcList(vo);
    }

    @Override
    public List<EgovMap> selectEventDivList(DivVO vo) throws Exception {
        return divMapper.selectEventDivList(vo);
    }

    @Override
    public List<EgovMap> selectEventDivLcList(DivVO vo) throws Exception {
        String grpId = EgovStringUtil.nullConvert(vo.getGrpId());
        String authLvl = EgovStringUtil.nullConvert(vo.getAuthLvl());
        if (!"".equals(grpId) && !"".equals(authLvl)) {
            List<EgovMap> list = divMapper.selectEventDivEvtGrpLvlLcList(vo);
            if (list != null && list.size() > 0) {
                return list;
            }
        }
        return divMapper.selectEventDivLcList(vo);
    }
}

