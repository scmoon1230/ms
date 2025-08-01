package kr.co.ucp.egov.com.cop.cmt.web;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springmodules.validation.commons.DefaultBeanValidator;

import kr.co.ucp.cmm.EgovMessageSource;
import kr.co.ucp.cmm.LoginVO;
import kr.co.ucp.cmm.util.EgovUserDetailsHelper;
import kr.co.ucp.egov.com.cop.bbs.web.EgovArticleController;
import kr.co.ucp.egov.com.cop.cmt.service.Comment;
import kr.co.ucp.egov.com.cop.cmt.service.CommentVO;
import kr.co.ucp.egov.com.cop.cmt.service.EgovArticleCommentService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;

@Controller
public class EgovArticleCommentController {
	private static final Logger LOGGER = LoggerFactory.getLogger(EgovArticleCommentController.class);

	@Resource(name="EgovArticleCommentService")
    protected EgovArticleCommentService egovArticleCommentService;
    
    @Resource(name="propertiesService")
    protected EgovPropertyService propertyService;
    
    @Resource(name="egovMessageSource")
    EgovMessageSource egovMessageSource;
    
    @Autowired
    private DefaultBeanValidator beanValidator;
    
    //protected Logger log = Logger.getLogger(this.getClass());
    
    /**
     * 댓글관리 목록 조회를 제공한다.
     * 
     * @param boardVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cop/cmt/selectArticleCommentList.do")
    public String selectArticleCommentList(@ModelAttribute("searchVO") CommentVO commentVO, ModelMap model) throws Exception {

    	CommentVO articleCommentVO = new CommentVO();
    	
		// 수정 처리된 후 댓글 등록 화면으로 처리되기 위한 구현
		if (commentVO.isModified()) {
		    commentVO.setCommentNo("");
		    commentVO.setCommentCn("");
		}
		
		// 수정을 위한 처리
		if (!commentVO.getCommentNo().equals("")) {
		    return "forward:/egov/com/cop/cmt/updateArticleCommentView.do";
		}
		
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		
		model.addAttribute("sessionUniqId", user.getUniqId());
		
		commentVO.setWrterNm(user.getUserNmKo());
		
//		commentVO.setSubPageUnit(propertyService.getInt("pageUnit"));
//		commentVO.setSubPageSize(propertyService.getInt("pageSize"));
	
		PaginationInfo paginationInfo = new PaginationInfo();
		paginationInfo.setCurrentPageNo(commentVO.getSubPageIndex());
		paginationInfo.setRecordCountPerPage(commentVO.getSubPageUnit());
		paginationInfo.setPageSize(commentVO.getSubPageSize());
	
		commentVO.setSubFirstIndex(paginationInfo.getFirstRecordIndex());
		commentVO.setSubLastIndex(paginationInfo.getLastRecordIndex());
		commentVO.setSubRecordCountPerPage(paginationInfo.getRecordCountPerPage());
	
		Map<String, Object> map = egovArticleCommentService.selectArticleCommentList(commentVO);
		int totCnt = Integer.parseInt((String)map.get("resultCnt"));
		
		paginationInfo.setTotalRecordCount(totCnt);
	
		model.addAttribute("resultList", map.get("resultList"));
		model.addAttribute("resultCnt", map.get("resultCnt"));
		model.addAttribute("paginationInfo", paginationInfo);
		model.addAttribute("type", "body");	// 댓글 페이지 body import용
		
		model.addAttribute("articleCommentVO", articleCommentVO);	// validator 용도 
		
		commentVO.setCommentCn("");	// 등록 후 댓글 내용 처리
	
		return "egov/com/cop/cmt/EgovArticleCommentList";
    }
    
    
    /**
     * 댓글을 등록한다.
     * 
     * @param commentVO
     * @param comment
     * @param bindingResult
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cop/cmt/insertArticleComment.do")
    public String insertArticleComment(@ModelAttribute("searchVO") CommentVO commentVO, @ModelAttribute("comment") Comment comment, 
	    BindingResult bindingResult, ModelMap model, @RequestParam HashMap<String, String> map) throws Exception {

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
	
		beanValidator.validate(comment, bindingResult);
		if (bindingResult.hasErrors()) {
		    model.addAttribute("msg", "댓글내용은 필수 입력값입니다.");
		    
		    return "forward:/egov/com/cop/bbs/selectArticleDetail.do";
		}
	
		if (isAuthenticated) {
		    comment.setFrstRegisterId(user.getUniqId());
		    comment.setWrterId(user.getUniqId());
		    comment.setWrterNm(user.getUserNmKo());
		    
		    egovArticleCommentService.insertArticleComment(comment);
		    
		    commentVO.setCommentCn("");
		    commentVO.setCommentNo("");
		}
		
		String chkBlog = map.get("blogAt");
		
		if(("Y").equals(chkBlog)){
			return "forward:/egov/com/cop/bbs/selectArticleBlogList.do";
		}else{
			return "forward:/egov/com/cop/bbs/selectArticleDetail.do";
		}
    }
    
    
    /**
     * 댓글을 삭제한다.
     * 
     * @param commentVO
     * @param comment
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cop/cmt/deleteArticleComment.do")
    public String deleteArticleComment(@ModelAttribute("searchVO") CommentVO commentVO, @ModelAttribute("comment") Comment comment, 
    		ModelMap model, @RequestParam HashMap<String, String> map) throws Exception {
		@SuppressWarnings("unused")
		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
		
		if (isAuthenticated) {
		    egovArticleCommentService.deleteArticleComment(commentVO);
		}
		
		commentVO.setCommentCn("");
		commentVO.setCommentNo("");
		
		String chkBlog = map.get("blogAt");
		
		if(chkBlog.equals("Y")){
			return "forward:/egov/com/cop/bbs/selectArticleBlogList.do";
		}else{
			return "forward:/egov/com/cop/bbs/selectArticleDetail.do";
		}
    }
    
    
    /**
     * 댓글 수정 페이지로 이동한다.
     * 
     * @param commentVO
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cop/cmt/updateArticleCommentView.do")
    public String updateArticleCommentView(@ModelAttribute("searchVO") CommentVO commentVO, ModelMap model) throws Exception {

	LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();

	CommentVO articleCommentVO = new CommentVO();
	
	commentVO.setWrterNm(user.getUserNmKo());

	commentVO.setSubPageUnit(propertyService.getInt("pageUnit"));
	commentVO.setSubPageSize(propertyService.getInt("pageSize"));

	PaginationInfo paginationInfo = new PaginationInfo();
	paginationInfo.setCurrentPageNo(commentVO.getSubPageIndex());
	paginationInfo.setRecordCountPerPage(commentVO.getSubPageUnit());
	paginationInfo.setPageSize(commentVO.getSubPageSize());

	commentVO.setSubFirstIndex(paginationInfo.getFirstRecordIndex());
	commentVO.setSubLastIndex(paginationInfo.getLastRecordIndex());
	commentVO.setSubRecordCountPerPage(paginationInfo.getRecordCountPerPage());

	Map<String, Object> map = egovArticleCommentService.selectArticleCommentList(commentVO);
	int totCnt = Integer.parseInt((String)map.get("resultCnt"));
	
	paginationInfo.setTotalRecordCount(totCnt);

	model.addAttribute("resultList", map.get("resultList"));
	model.addAttribute("resultCnt", map.get("resultCnt"));
	model.addAttribute("paginationInfo", paginationInfo);
	model.addAttribute("type", "body");	// body import
	
	articleCommentVO = egovArticleCommentService.selectArticleCommentDetail(commentVO);
	
	model.addAttribute("articleCommentVO", articleCommentVO);
	
	
	return "egov/com/cop/cmt/EgovArticleCommentList";
    }
    
    
    /**
     * 댓글을 수정한다.
     * 
     * @param commentVO
     * @param comment
     * @param bindingResult
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/egov/com/cop/cmt/updateArticleComment.do")
    public String updateArticleComment(@ModelAttribute("searchVO") CommentVO commentVO, @ModelAttribute("comment") Comment comment, 
	    BindingResult bindingResult, ModelMap model) throws Exception {

		LoginVO user = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
	
		beanValidator.validate(comment, bindingResult);
		if (bindingResult.hasErrors()) {
		    model.addAttribute("msg", "내용은 필수 입력 값입니다.");
		    
		    return "forward:/egov/com/cop/bbs/selectArticleDetail.do";
		}
	
		if (isAuthenticated) {
		    comment.setLastUpdusrId(user.getUniqId());
		    
		    egovArticleCommentService.updateArticleComment(comment);
		    
		    commentVO.setCommentCn("");
		    commentVO.setCommentNo("");
		}
	
		return "forward:/egov/com/cop/bbs/selectArticleDetail.do";
    }
    
	
}
