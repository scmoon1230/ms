
/* 한글입력 방지 */
function removeKoreanChar(obj)
{
	//좌우 방향키, 백스페이스, 딜리트, 탭키에 대한 예외
	if(event.keyCode == 8 || event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 39 || event.keyCode == 46 ) return;
	//obj.value = obj.value.replace(/[\a-zㄱ-ㅎㅏ-ㅣ가-힣]/g, '');
	obj.value = obj.value.replace(/[\ㄱ-ㅎㅏ-ㅣ가-힣]/g, '');
	
	userIdDuplFlag = '';	// 아이디 중복체크 초기화
}

let userIdDuplFlag = '';
function checkUserId() {
	if ( '' == $("#iUserId").val().trim() ) {	alert('아이디를 입력하세요.');		$('#iUserId').focus();		return false;	}
	
	var params = "userId=" + encodeURIComponent($("#iUserId").val());

	$.ajaxEx($('#grid'), {
		type : "POST",
		url : contextRoot + "/wrks/sstm/usr/info/checkUserId.json",
		dataType : "json",
		data: params,
		success:function(data){
			//alert(data.msg);
			if( 0 == data.msg ) {
				alert('사용가능한 아이디입니다.');		userIdDuplFlag = 'POSS';
			} else {
				alert('이미 사용중인 아이디입니다.');	userIdDuplFlag = 'IMPOSS';
			}
		},
		error:function(e){
			alert(data.msg);
		}
	});
}

function insertAction()
{	//alert(loginUserGrpId+" , "+loginUserAuthLvl+" , "+GlobalsRqstAuthLvl );

	var iUserId = $("#iUserId").val().trim();
	if ('' == iUserId )			{	alert('아이디를 입력하세요.');		$('#iUserId').focus();		return false;	}
	if ('' == userIdDuplFlag )	{	alert('아이디 중복검사를 하세요.');	$('#iUserId').focus();		return false;	}
	if ('IMPOSS' == userIdDuplFlag )	{	alert('이미 사용중인 아이디입니다.');	$('#iUserId').focus();		return false;	}
	//if( iUserId.length < 5 ) {	alert("아이디는 5자 이상 입력하세요.");		return false;	}
	var useNotId = "\^test|^demo";	// test, demo 로 시작하는 아이디 사용할 수 없음 - 2017.03.02 - seungJun
	if ((new RegExp(useNotId, "i")).test(iUserId)) {		alert('test, demo로 시작되는 아이디는 등록할 수 없습니다.');	return false;	}

	if ('' == $("#iUserNmKo").val().trim() )			 {	alert('사용자명을 입력하세요.');		$('#iUserNmKo').focus();	return false;	}
	if ('' == $("#iPassword").val().trim() )			 {	alert('비밀번호를 입력하세요.');		$('#iPassword').focus();	return false;	}
	if($("#iPassword").val() != $("#iPasswordCk").val()) {	alert("비밀번호가 일치하지 않습니다.");	$("#ckPs").show();		return false;
	} else {		$("#ckPs").hide();	}	// 비밀번호 확인

	if ($("#iMoblNo1").val().trim()=="" || $("#iMoblNo2").val().trim()=="" || $("#iMoblNo3").val().trim()=="")	{	alert("휴대전화를 입력하세요.");		return false;	}
	if ($("#iEmail1").val().trim()=="" || $("#iEmail2").val().trim()=="")	{	alert("이메일을 입력하세요.");		return false;	}
	
	//if ($("#iOffcTelNo1").val().trim()=="" || $("#iOffcTelNo2").val().trim()=="" || $("#iOffcTelNo3").val().trim()=="")	{	alert("사무실전화를 입력하세요.");		return false;	}

	if ($("#iInsttCd").val().trim()=="" && $("#iInsttNmInput").val().trim()=="")	{	alert("기관명을 선택하거나 입력하세요.");	$('#iInsttCd').focus();	return false;	}
	//if ($("#iDeptNm").val().trim()=="")											{	alert("부서명을 입력하세요.");			$('#iDeptNm').focus();	return false;	}
	//if ($("#iRankNm").val().trim()=="")											{	alert("직급명을 입력하세요.");			$('#iRankNm').focus();	return false;	}
	//if (!$('#adminPw').val().trim())		{	alert('관리자 비밀번호를 입력하세요.');	$('#adminPw').focus();	return false;	}
	
	var moblNo = $("#iMoblNo1").val() +"-"+ $("#iMoblNo2").val() +"-"+ $("#iMoblNo3").val();
	var offcTelNo = $("#iOffcTelNo1").val() +"-"+ $("#iOffcTelNo2").val() +"-"+ $("#iOffcTelNo3").val();
	var email = $("#iEmail1").val() +"@"+ $("#iEmail2").val();
		
	var params = "userId=" + encodeURIComponent(iUserId);
		params += "&password=" + encodeURIComponent($("#iPassword").val());
		params += "&userNmKo=" + encodeURIComponent($("#iUserNmKo").val());
		params += "&userNmEn=" + encodeURIComponent($("#iUserNmEn").val());
		params += "&moblNo=" + encodeURIComponent(moblNo);
		params += "&offcTelNo=" + encodeURIComponent(offcTelNo);
		params += "&email=" + encodeURIComponent(email);
		//params += "&useTyCd=" + encodeURIComponent($("#iUseTyCd").val());
		//params += "&insttNm=" + encodeURIComponent($("#iInsttNm").val());
		params += "&insttCd=" + encodeURIComponent($("#iInsttCd").val());
		params += "&insttNmInput=" + encodeURIComponent($("#iInsttNmInput").val());
		params += "&deptNm=" + encodeURIComponent($("#iDeptNm").val());
		params += "&rankNm=" + encodeURIComponent($("#iRankNm").val());
		params += "&rpsbWork=" + encodeURIComponent($("#iRpsbWork").val());
		params += "&remark=" + encodeURIComponent($("#iRemark").val());
		
		//params += "&dstrtCd=" + encodeURIComponent($("#iDstrtCd").val());
		//params += "&ipAdres=" + encodeURIComponent($("#iIpAdres").val());
		//params += "&ipTyCd=" + encodeURIComponent($("#iIpTyCd").val());
		//params += "&ipCd=" + encodeURIComponent($("#iIpCd").val());
		//params += "&adminPw="+ encodeURIComponent($("#adminPw").val());
		
	//	let wsUrl = "ws://"+window.location.host+contextRoot;
	//	if ( window.location.protocol.indexOf('https') != -1 ) {
	//		wsUrl = "wss://"+window.location.host+contextRoot;
	//	}
	//	console.log('wsUrl = '+wsUrl);
	//	params += "&wsUrl=" + encodeURIComponent(wsUrl);
		
					
	if ( confirm('회원가입을 신청하시겠습니까?') ) {
		$.ajaxEx($('#grid'), {
			type : "POST"
			, url : contextRoot + "/wrks/lgn/apply.json"
			, dataType : "json"
			, data: params
			, success:function(data){
				alert(data.msg);
				if (data.ret == 1) {	// 성공
					window.close();
				} else {
					
				}
			}
			, error:function(e) {
				alert(data.msg);
			}
		});
	}
	
}
