package kr.co.ucp.wrks.cop.com.service.impl;
import java.util.List;

import kr.co.ucp.wrks.cop.com.service.TemplateInf;
import kr.co.ucp.wrks.cop.com.service.TemplateInfVO;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 * 템플릿 정보관리를 위한 데이터 접근 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.03.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.17  이삼섭          최초 생성
 *   2011.08.31  JJY            경량환경 템플릿 커스터마이징버전 생성
 *
 * </pre>
 */
@Repository("TemplateManageDAO")
public class TemplateManageDAO extends EgovAbstractMapper {

    /**
     * 템플릿 정보를 삭제한다.
     *
     * @param tmplatInf
     * @throws Exception
     */
    public void deleteTemplateInf(TemplateInf tmplatInf) throws Exception {
	update("EgovTemplate_SQL.deleteTemplateInf", tmplatInf);
    }

    /**
     * 템플릿 아이디 생성
     * @param tmplatInf
     * @return
     * @throws Exception
     */
	public String getNextTemplateInfId(TemplateInf tmplatInf) throws Exception {
		return (String)selectOne("EgovTemplate_SQL.getNextTemplateInfId", tmplatInf);
	}
    /**
     * 템플릿 정보를 등록한다.
     *
     * @param tmplatInf
     * @throws Exception
     */
    public void insertTemplateInf(TemplateInf tmplatInf) throws Exception {
	insert("EgovTemplate_SQL.insertTemplateInf", tmplatInf);
    }

    /**
     * 템플릿 정보를 수정한다.
     *
     * @param tmplatInf
     * @throws Exception
     */
    public void updateTemplateInf(TemplateInf tmplatInf) throws Exception {
	update("EgovTemplate_SQL.updateTemplateInf", tmplatInf);
    }

    /**
     * 템플릿에 대한 목록를 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    //@SuppressWarnings("unchecked")
    public List<TemplateInfVO> selectTemplateInfs(TemplateInfVO tmplatInfVO) throws Exception {
	return selectList("EgovTemplate_SQL.selectTemplateInfs", tmplatInfVO);
    }

    /**
     * 템플릿에 대한 목록 전체 건수를 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    public int selectTemplateInfsCnt(TemplateInfVO tmplatInfVO) throws Exception {
	return (Integer)selectOne("EgovTemplate_SQL.selectTemplateInfsCnt", tmplatInfVO);
    }

    /**
     * 템플릿에 대한 상세정보를 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    public TemplateInfVO selectTemplateInf(TemplateInfVO tmplatInfVO) throws Exception {
	return (TemplateInfVO)selectOne("EgovTemplate_SQL.selectTemplateInf", tmplatInfVO);

    }

    /**
     * 템플릿에 대한 미리보기 정보를 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    public TemplateInfVO selectTemplatePreview(TemplateInfVO tmplatInfVO) throws Exception {
	return null;
    }

    /**
     * 템플릿 구분에 따른 목록을 조회한다.
     *
     * @param tmplatInfVO
     * @return
     * @throws Exception
     */
    //@SuppressWarnings("unchecked")
    public List<TemplateInfVO> selectTemplateInfsByCode(TemplateInfVO tmplatInfVO) throws Exception {
	return selectList("EgovTemplate_SQL.selectTemplateInfsByCode", tmplatInfVO);
    }

}
