<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/mntr/layout/header.css' />" />
<style>
.dropdown-menu { min-width:220px; }
</style>
<c:if test="${not empty LoginVO.sysCd}">
	<c:set var="systemCd" value=" system-${fn:toLowerCase(LoginVO.sysCd)}" scope="request" />
</c:if>
<c:set var="webSocketConnUseYn" scope="request"><prprts:value key="WEB_SOCKET_CONN_USE_YN"/></c:set>
<nav id="main-nav" class="navbar navbar-default navbar-fixed-top${systemCd}" role="navigation" data-main-menu="${common.mainMenu}" data-sub-menu="${common.subMenu}">
	<div class="container-fluid">
		<!-- Header Image -->
		<div class="navbar-header navbar-right">
			<button class="collapsed navbar-toggle" type="button" data-toggle="collapse" data-target=".bs-example-js-navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</button>
			<%-- <a href="<c:out value='${pageContext.request.contextPath}' />/" class="navbar-brand">재정부${LoginVO.menuSysNm} --%>
				<%-- <c:if test="${not empty configure.ucpNm}">(${configure.ucpNm})</c:if> --%>
				<%-- 이미지 작업중...
				<a href="/mntr/main/main.do" class="navbar-brand">
				<c:choose>
					<c:when test="${not empty sysCd}">
						<img src="<c:url value='/'/>images/${sysCd}/logo.png" alt="로고 이미지">
					</c:when>
					<c:otherwise>
						<img src="<c:url value='/'/>images/logo.png" alt="${LoginVO.menuSysNm}">
					</c:otherwise>
				</c:choose>
				</a>
				--%>
			</a>
		</div>
		<!-- User Info -->
		<ul class="nav navbar-nav navbar-right">
			<li class="dropdown" id="dropdown-user-info">
				<a href="#" class="dropdown-toggle" data-toggle="dropdown">
					${LoginVO.userName}님(${LoginVO.positionName}/${LoginVO.userGb})
					<span class="caret"></span>
					<input type="hidden" id="loginUserId" value="${LoginVO.userId}">
					<input type="hidden" id="loginUserGb" value="${LoginVO.userGb}">
				</a>
				<ul class="dropdown-menu" role="menu">
					<li role="separator" class="divider"></li>
					<%--
					<li>
						<a href="#" onclick="oHeader.opener('wrks/lgn/myinfo.do', '내정보보기', '1000', '550');">
							내정보보기 <i class="fas fa-external-link-alt"></i>
						</a>
					</li>
					<li>
						<a href="#" onclick="oHeader.opener('wrks/lgn/changepwd.do', '비밀번호수정', '1000', '300');">
							비밀번호수정 <i class="fas fa-external-link-alt"></i>
						</a>
					</li>
					 --%>
					<li>
						<a href="<c:url value='/wrks/lgn/logout.do'/>"> 로그아웃</a>
					</li>
					<%--
					<c:if test="${LoginVO.loginDplctnYn ne 'X' && webSocketConnUseYn eq 'Y'}">
						<li role="separator" class="divider" style="margin: 5px 0;"></li>
						<li>
							<a href="#" id="anchor-login-dplctn-yn" data-toggle="tooltip" data-placement="left" title="스위치 버튼 클릭시 중복로그인 가능 또는 불가로 설정합니다." style="cursor: default;">
								<span class="pull-left" style="line-height: 22px; margin-right: 3px;">중복로그인</span>
								<input type="checkbox" id="toggle-login-dplctn-yn" style="cursor: pointer;" data-toggle="toggle" data-on="가능" data-off="불가" data-onstyle="primary" data-offstyle="danger" data-size="mini" data-width="45" <c:if test="${LoginVO.loginDplctnYn eq 'Y'}">checked="checked"</c:if>>
							</a>
						</li>
					</c:if>
					 --%>
				</ul>
			</li>
		</ul>
		
		<!-- 영상반출센터 승인자레벨 알림수신 -->
	<%--<c:if test="${webSocketConnUseYn eq 'Y' and LoginVO.grpId eq 'PVECENTER' and (LoginVO.authLvl eq '1' or LoginVO.authLvl eq '2')}">
			<ul class="nav navbar-nav navbar-right hide">
				<li class="dropdown" id="dropdown-event-receive">
					<a href="#" class="dropdown-toggle" data-toggle="dropdown">
							승인처리알림
						<span class="caret"></span>
					</a>
					<ul class="dropdown-menu" id="dropdown-menu-event-received" role="menu"></ul>
				</li>
			</ul>
		</c:if>--%>
		
		<!-- System Menu -->
		<ul class="nav navbar-nav menu-system">
		
			<c:forEach var="menuLvl1" items="${LoginVO.menuList}">
				<c:if test="${menuLvl1.menuLevel eq 1}">
					<li class="dropdown menu-lvl-1">
						<a href="/<c:url value='${menuLvl1.runProgram}'/>" class="dropdown-toggle" id="" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
							<i class="${menuLvl1.menuImg}"></i> ${menuLvl1.sgroupNm}
						</a>
						<ul class="dropdown-menu" role="menu" aria-labelledby="${menuLvl1.menuId}">
							<c:forEach var="menuLvl2" items="${LoginVO.menuList}">
								<c:if test="${menuLvl2.menuLevel eq 2 and menuLvl1.menuId eq menuLvl2.parentMenuId}">
									<li role="separator" class="divider"></li>
									<li class="dropdown-header menu-lvl-2" role="presentation">
										<c:choose>
											<c:when test="${not empty menuLvl2.runProgram and menuLvl2.runProgram ne 'dir.do'}">
												<span>
													<a href="<c:out value='${pageContext.request.contextPath}' />/<c:url value='${menuLvl2.runProgram}'/>"> ${menuLvl2.sgroupNm}</a><a href="<c:out value='${pageContext.request.contextPath}' />/<c:url value='${menuLvl2.runProgram}'/>" target="_blank">[n]</a>
												</span>
											</c:when>
											<c:otherwise>
												<strong>${menuLvl2.sgroupNm}</strong>
											</c:otherwise>
										</c:choose>
									</li>
									<c:forEach var="menuLvl3" items="${LoginVO.menuList}">
										<c:if test="${menuLvl3.menuLevel eq 3 and menuLvl2.menuId eq menuLvl3.parentMenuId}">
											<li class="menu-lvl-3">
												<span>
													<a href="<c:out value='${pageContext.request.contextPath}' />/<c:url value='${menuLvl3.runProgram}'/>">${menuLvl3.sgroupNm}</a><a href="<c:out value='${pageContext.request.contextPath}' />/<c:url value='${menuLvl3.runProgram}'/>" target="_blank">[n]</a>
												</span>
											</li>
										</c:if>
									</c:forEach>
								</c:if>
							</c:forEach>
						</ul>
					</li>
				</c:if>
			</c:forEach>
			
		
			
			<%-- 
			<li class="dropdown menu-lvl-1 menu-all">
				<a href="#" class="dropdown-toggle" id="menu-all" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
					<i class="fas fa-sitemap"></i> 전체
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu" role="menu" aria-labelledby="menu-all">
					<c:forEach var="menuAllLvl1" items="${LoginVO.menuList}">
						<c:if test="${menuAllLvl1.lvl eq 1 and menuAllLvl1.pgmMenuVisibleYn eq 'Y'}">
							<li class="dropdown-header" role="presentation">
								<span>
									<strong> ${menuAllLvl1.pgmMenuNmKo} </strong>
								</span>
								<ul>
									<c:forEach var="menuAllLvl2" items="${LoginVO.menuList}">
										<c:if test="${menuAllLvl2.lvl eq 2 and menuAllLvl1.pgmMenuId eq menuAllLvl2.prntPgmMenuId and menuAllLvl2.pgmMenuVisibleYn eq 'Y'}">
											<li role="separator" class="divider"></li>
											<li class="dropdown-header menu-all-lvl-2" role="presentation">
												<c:choose>
													<c:when test="${not empty menuAllLvl2.pgmUrl and menuAllLvl2.pgmUrl ne 'dir.do'}">
														<!-- 새창 -->
														<c:choose>
															<c:when test="${menuAllLvl2.newWdwYn eq 'Y'}">
																<a href="#"
																	onclick="oHeader.opener('${menuAllLvl2.pgmUrl}', '${menuAllLvl2.pgmMenuNmKo}', '${menuAllLvl2.newWinWidth}', '${menuAllLvl2.newWinHeight}');">
																	${menuAllLvl2.pgmMenuNmKo} <i class="fas fa-external-link-alt"></i>
																</a>
															</c:when>
															<c:otherwise>
																<a href="<c:out value='${pageContext.request.contextPath}' />/<c:url value='${menuAllLvl2.pgmUrl}'/>"> ${menuAllLvl2.pgmMenuNmKo}</a>
															</c:otherwise>
														</c:choose>
													</c:when>
													<c:otherwise>
														<strong>${menuAllLvl2.pgmMenuNmKo}</strong>
													</c:otherwise>
												</c:choose>
												<ul>
													<c:forEach var="menuAllLvl3" items="${LoginVO.menuList}">
														<c:if test="${menuAllLvl3.lvl eq 3 and menuAllLvl2.pgmMenuId eq menuAllLvl3.prntPgmMenuId and menuAllLvl3.pgmMenuVisibleYn eq 'Y'}">
															<li>
																<!-- 새창 -->
																<c:choose>
																	<c:when test="${menuAllLvl3.newWdwYn eq 'Y'}">
																		<a href="#"
																			onclick="oHeader.opener('${menuAllLvl3.pgmUrl}', '${menuAllLvl3.pgmMenuNmKo}', '${menuAllLvl3.newWinWidth}', '${menuAllLvl3.newWinHeight}');">
																			${menuAllLvl3.pgmMenuNmKo} <i class="fas fa-external-link-alt"></i>
																		</a>
																	</c:when>
																	<c:otherwise>
																		<a href="<c:out value='${pageContext.request.contextPath}' />/<c:url value='${menuAllLvl3.pgmUrl2}'/>top=${menuAllLvl1.pgmMenuId}&left=${menuAllLvl2.pgmMenuId}&m=${menuAllLvl3.pgmMenuId}">${menuAllLvl3.pgmMenuNmKo}
																		</a>
																	</c:otherwise>
																</c:choose>
															</li>
														</c:if>
													</c:forEach>
												</ul>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</li>
			 --%>
			
			
			
		</ul>
		
		<!-- 메뉴경로 -->
		<ul class="nav navbar-nav" style="padding: 15px;">
			<span id='menutitle'>[메뉴경로]</span>
		</ul>
		
		
	</div>
</nav>

<script src="<c:url value='/js/mntr/header.js' />"></script>

<c:if test="${webSocketConnUseYn eq 'Y'}">
	<%@include file="/WEB-INF/jsp/mntr/layout/websocket.jsp" %>
</c:if>

<%--
<c:if test="${configProp['Globals.WebSocketConnUseYn'] eq 'Y'}">
	<%@include file="/WEB-INF/jsp/mntr/layout/socket.jsp"%>
	<script>
		//  alert('${LoginVO.grpId}' + ':'+ '${LoginVO.userId}');
		/* 소켓연결  */
		//var url = $("#msgServerIp").val() + ":" + $("#msgServerPort").val();
		var url = '<spring:eval expression="@config['Globals.ServerIp']"/>';
		var port = '<spring:eval expression="@config['Globals.MsgServerPort']"/>';
		url += ":" + port;

		var socket = io.connect(url);
		//var userId = sesUserId;
		var userId = $('#gUserId').val();

		socket.emit('join', {
			room : userId
		});

		socket.on('invite', function(msg) {
			console.log('MSG Received : ' + JSON.stringify(msg));
			if (confirm(msg.msg) == false) return false;

			//roomJoin(roomNm, userList);
			javascript: $.openMenuCenter('<c:url value='/'/>wrks/wrkmng/msgmng/messenger_chat.do?roomNm=' + escape(encodeURIComponent(msg.room)) + '&userList=' + msg.userid + '&grpSeqNo=' + msg.grpSeqNo, 'CHAT', 510, 810);
		});
	</script>
</c:if>

<script>
	// 승인대기중인 신청이 있는 지 주기적으로 확인해서 알림
	let sMsg = '';
	let sUrl = '';
	
	function doWork() {
		//clearInterval(timer);	// 반복중단
		let approveNotifyMuteYn = '${tvoConfig.approveNotifyMuteYn}' || 'N';
		if (approveNotifyMuteYn == 'N') {
			const oAudio = new Audio(contextRoot + '/sound/approve/approve_notify.mp3');
			oAudio.loop = false;
			oAudio.play();
		}
		oCommon.modalAlert('modal-notice', '알림', '새로운 '+sMsg+' 조회로 이동합니다.', function () {
			setTimeout(() => {
				location.href = sUrl;
			}, 5000 * 1);
		});
	}
	
	// 새로운 신청이 있는 지 확인
	function checkRequest() {
		$.post(contextRoot + '/tvo/aprv/selectAllTypeWaitList.json', {
			'page': 1,
			'rows': 10,
			'firstRecordIndex': 0
		}).done((data) => {
			let oApproveList = data?.rows || [];

			if (oApproveList.length) {
				oApproveList.forEach(element => {
					if ( '' == sUrl ) {
						console.log('element.ty => '+element.ty);
						if (element.ty === 'VIEW') {
							sMsg = '열람신청';				sUrl = contextRoot + '/tvo/view/viewAprv.do';
						} else if (element.ty === 'VIEWEXTN') {
							sMsg = '열람기간연장신청';			sUrl = contextRoot + '/tvo/view/viewExtnAprv.do';
						} else if (element.ty === 'OUT') {
							sMsg = '반출신청';				sUrl = contextRoot + '/tvo/out/outAprv.do';
						} else if (element.ty === 'OUTEXTN') {
							sMsg = '반출기간연장신청';			sUrl = contextRoot + '/tvo/out/outExtnAprv.do';
						} else if (element.ty === 'APPLY') {
							sMsg = '사용자승인신청';		sUrl = contextRoot + '/wrks/sstm/usr/usrApprove.do';
						}
					}
				});

				if ( '' != sUrl ) {		doWork();		}
				
			}
		});
	}
	//var timer = setInterval(checkRequest, 10000);	// 10초 반복
</script>
--%>

	
