<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default" style="margin-bottom: 0px;">
	<div class="panel-heading">
		<c:choose>
			<c:when test="${data.role eq 'C'}">즐겨찾기 추가</c:when>
			<c:when test="${data.role eq 'R'}">
				${data.bookmarkNm}
				<c:if test="${data.shareYn eq 'Y'}">
					<i class="fas fa-share-alt" title="공유됨"></i>
				</c:if>
			</c:when>
			<c:when test="${data.role eq 'U'}">즐겨찾기 수정</c:when>
		</c:choose>
	</div>
	<div class="panel-body">
		<c:choose>
			<c:when test="${data.role eq 'C'}">
				<form id="form-bookmark" method="post">
					<div class="form-group">
						<label for="bookmark-nm">경도</label>
						<input type="text" class="form-control input-sm" id="bookmark-point-x" value="${data.pointX}" readonly="readonly" required="required" />
					</div>
					<div class="form-group">
						<label for="bookmark-nm">위도</label>
						<input type="text" class="form-control input-sm" id="bookmark-point-y" value="${data.pointY}" readonly="readonly" required="required" />
					</div>
					<div class="form-group">
						<label for="bookmark-nm">제목</label>
						<input type="text" class="form-control input-sm" id="bookmark-nm" maxlength="200" placeholder="제목" required="required" />
					</div>
					<div class="form-group">
						<label for="bookmark-desc">내용</label>
						<textarea class="form-control input-sm" id="bookmark-desc" rows="2" maxlength="500"></textarea>
					</div>
					<div class="checkbox">
						<label>
							<input type="checkbox" id="bookmark-share-yn"> 전체 공유
						</label>
					</div>
				</form>
			</c:when>
			<c:when test="${data.role eq 'R'}">
				${empty data.bookmarkDesc ? '내용없음' : data.bookmarkDesc}
			</c:when>
			<c:when test="${data.role eq 'U'}">
				<form id="form-bookmark" method="post">
					<div class="form-group">
						<label for="bookmark-nm">경도</label>
						<input type="text" class="form-control input-sm" id="bookmark-point-x" value="${data.pointX}" readonly="readonly" required="required" />
					</div>
					<div class="form-group">
						<label for="bookmark-nm">위도</label>
						<input type="text" class="form-control input-sm" id="bookmark-point-y" value="${data.pointY}" readonly="readonly" required="required" />
					</div>
					<div class="form-group">
						<label for="bookmark-nm">제목</label>
						<input type="text" class="form-control input-sm" id="bookmark-nm" maxlength="200" placeholder="제목" required="required" value="${data.bookmarkNm}" />
					</div>
					<div class="form-group">
						<label for="bookmark-desc">내용</label>
						<textarea class="form-control input-sm" id="bookmark-desc" rows="2" maxlength="500">${data.bookmarkDesc}</textarea>
					</div>
					<div class="checkbox">
						<label>
							<input type="checkbox" id="bookmark-share-yn" ${data.shareYn eq 'Y' ? ' checked' : ''}> 전체 공유
						</label>
					</div>
					<input type="hidden" id="bookmark-id" value="${data.bookmarkId}" />
				</form>
			</c:when>
		</c:choose>
	</div>
	<div class="panel-footer">
		<c:choose>
			<c:when test="${data.role eq 'C'}">
				<button type="button" class="btn btn-default btn-sm" onclick="oMntrMap.bookmark.add();">저장</button>
			</c:when>
			<c:when test="${data.role eq 'U'}">
				<button type="button" class="btn btn-default btn-sm" onclick="oMntrMap.bookmark.update();">저장</button>
			</c:when>
			<c:when test="${data.role eq 'R'}">
				<c:if test="${data.rgsUserId eq LoginVO.userId}">
					<button type="button" class="btn btn-default btn-sm" data-point-x="${data.pointX}" data-point-y="${data.pointY}" data-bookmark-id="${data.bookmarkId}" onclick="oMntrMap.bookmark.popup.update(this);">수정</button>
					<button type="button" class="btn btn-default btn-sm" data-point-x="${data.pointX}" data-point-y="${data.pointY}" data-bookmark-id="${data.bookmarkId}" onclick="oMntrMap.bookmark.del(this);">삭제</button>
				</c:if>
			</c:when>
		</c:choose>
	</div>
</div>