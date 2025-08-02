
$(document).ready(function(){
	let ag = opener.document.getElementById("acctGb");
	let acctGbName = ag.options[ag.selectedIndex].text;
	$('.printTitle').text('('+acctGbName+') 수입/지출 집계표');

	
	let temp = opener.document.getElementById("stanYmd").value;
	let printStanYmd = temp.substring(0,4)+' 년 '+temp.substring(5,7)+' 월 '+temp.substring(8,10)+' 일';
	$('.printStanYmd').text(printStanYmd);

	let stanYmd   = opener.document.getElementById("stanYmd").value.replaceAll("-", "");
	let acctGb    = opener.document.getElementById("acctGb").value;
	
	$.ajax({
		url:contextRoot + "/sheet/sum/list.json",
		type:"POST",
		data:{
			stanYmd    : stanYmd
			, acctGb   : acctGb
			, page : '1'
			, rows : '500'
			, sidx : 'IO_ORDER, ORD'
			, sord : 'ASC'
		},
		success: function(result) {
			var ind = 1;
			var tableBody1 = '';
			var tableBody2 = '';
			var tableBody3 = '';
			var prevAcctName = '';
			
			for ( var i=0;i<result.rows.length;i++) {
				let ord = result.rows[i].ord;
				if ( '39' <= ord && ord < '60' ) {			ind = 2;
				} else if ( '60' < ord ) {	ind = 3;
				}
				
				let acctName = result.rows[i].acctName;
				let moneyAmt = result.rows[i].moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
				if ( acctName == null ) {
					acctName = ' &nbsp; ';
					if ( moneyAmt == '0' ) moneyAmt = ' &nbsp; ';
				}
				
				var tableTr = '';
				if ( (ind == 2 || ind == 3) && acctName == ' &nbsp; ') {
				} else {
		            tableTr = '<tr>';
					if ( acctName.indexOf('계') != -1 || acctName.indexOf('다음주') != -1 ) {
			            tableTr += '<td class="pBig pTd center" style="background-color: #eee; font-weight: bold;color: #444;">' + acctName + '</td>';
			            tableTr += '<td class="pBig pTd money" style="background-color: #eee; font-weight: bold;color: #444;">' + moneyAmt + '</td>';
			            tableTr += '<td class="pBig pTd" style="background-color: #eee; font-weight: bold;color: #444;"></td>';
					} else {
			            tableTr += '<td class="pBig pTd center">' + acctName + '</td>';
			            tableTr += '<td class="pBig pTd money">' + moneyAmt + '</td>';
			            tableTr += '<td class="pBig pTd"></td>';
					}

		            tableTr += '</tr>';
				}
	
				if ( ind == 1 ) {
					tableBody1 = tableBody1 + tableTr;
				} else if ( ind == 2 ) {
					tableBody2 = tableBody2 + tableTr;
				} else if ( ind == 3 ) {
					tableBody3 = tableBody3 + tableTr;
				}
				
			/*	if ( '수입누계' == prevAcctName || '다음주이월' == prevAcctName ) {
					prevAcctName = '';
				} else {
		            $('#tableBody').append(tableTr);
				}	*/
		
			//	if ( '수입누계' == acctName ) {			ind = 2;
			//	} else if ( '다음주이월' == acctName ) {	ind = 3;
			//	}
			/*	if ( '수입누계' == acctName || '다음주이월' == acctName ) {
					prevAcctName = acctName;
					
		            var tableTr = '<tr><td class="pBig pTd" colspan="3">';
					if ( '수입누계' == acctName  )		tableTr += '2.지출</td></tr>';
					if ( '다음주이월' == acctName )		tableTr += '3.현금자산</td></tr>';
					
		            tableTr += '<tr><th class="pBig pTh">내역</th><th class="pBig pTh">금액</th><th class="pBig pTh">비고</th></tr>';
		            $('#tableBody').append(tableTr);
				}	*/
				
			}
			
            $('#tableBody1').append(tableBody1);
            $('#tableBody2').append(tableBody2);
            $('#tableBody3').append(tableBody3);
		}
	});

});
