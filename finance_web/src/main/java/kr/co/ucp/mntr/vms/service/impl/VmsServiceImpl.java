/**
 * ----------------------------------------------------------------------------------------------
 *
 * @Class Name : VmsServiceImpl.java
 * @Description :
 * @Version : 1.0
 * Copyright (c) 2014 by KR.CO.UCP All rights reserved.
 * @Modification Information
 * ----------------------------------------------------------------------------------------------
 * DATE AUTHOR DESCRIPTION
 * ----------------------------------------------------------------------------------------------
 * 2015. 11. 25. SaintJuny@ubolt.co.kr 최초작성
 * <p>
 * ----------------------------------------------------------------------------------------------
 */
package kr.co.ucp.mntr.vms.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import kr.co.ucp.mntr.cmm.service.ConfigureVO;
import kr.co.ucp.mntr.cmm.util.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import kr.co.ucp.cmm.service.PrprtsService;
import kr.co.ucp.cmm.util.EgovStringUtil;
import kr.co.ucp.mntr.vms.service.VmsLogVO;
import kr.co.ucp.mntr.vms.service.VmsMapper;
import kr.co.ucp.mntr.vms.service.VmsService;

@Service("vmsService")
public class VmsServiceImpl implements VmsService {

    @Resource(name = "prprtsService")
    private PrprtsService prprtsService;

    @Resource(name = "cctvPtzLogIdGnr")
    private EgovIdGnrService ptzIdGnr;

    @Resource(name = "cctvViewLogIdGnr")
    private EgovIdGnrService viewIdGnr;

    @Resource(name = "vmsMapper")
    private VmsMapper vmsMapper;

    @Override
    public long insertPtzLog(VmsLogVO vo) throws Exception {
        vo.setSeqNo(ptzIdGnr.getNextLongId());
        vmsMapper.insertPtzLog(vo);
        return vo.getSeqNo();
    }

    @Override
    public long insertViewLog(VmsLogVO vo) throws Exception {
        vo.setSeqNo(viewIdGnr.getNextLongId());
        vmsMapper.insertViewLog(vo);
        return vo.getSeqNo();
    }

    @Override
    public List<EgovMap> selectMngMemberList(Map<String, String> params) throws Exception {
        String vmsLinkYn = EgovStringUtil.nullConvert(prprtsService.getString("VMS_LINK_YN"));
        if ("Y".equals(vmsLinkYn)) {
            params.put("vmsLinkYn", vmsLinkYn);
        }

        Object objConfigure = SessionUtil.getAttribute("configure");
        if (objConfigure != null && objConfigure.getClass().isAssignableFrom(ConfigureVO.class)) {
            ConfigureVO configureVO = (ConfigureVO) objConfigure;
            params.put("networkIp", configureVO.getNetworkIp());
        } else if (objConfigure != null && objConfigure.getClass() == EgovMap.class) {
    		EgovMap configureVO = (EgovMap) objConfigure;
            params.put("networkIp", configureVO.get("networkIp").toString());
        }

        return vmsMapper.selectMngMemberList(params);
    }
}
