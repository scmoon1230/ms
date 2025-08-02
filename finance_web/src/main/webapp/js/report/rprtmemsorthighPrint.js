
$(document).ready(function(){

	$('.printStartDate').text(opener.document.getElementById("startDate").value);
	$('.printEndDate').text(opener.document.getElementById("endDate").value);

	let startDate   = opener.document.getElementById("startDate").value.replaceAll("-", "");
	let endDate     = opener.document.getElementById("endDate").value.replaceAll("-", "");
	let moneyCode   = opener.document.getElementById("moneyCode").value;
	let worshipCode = opener.document.getElementById("worshipCode").value;
	let idExist     = opener.document.getElementById("idExist").value;
	let moneyAmt    = opener.document.getElementById("moneyAmt").value;

	$.ajax({
		url:contextRoot + "/report/memsorthigh/list.json",
		type:"POST",
		data:{
			startDate      : startDate
			, endDate      : endDate
			, moneyCode    : moneyCode
			, worshipCode  : worshipCode
			, idExist      : idExist
			, moneyAmt     : moneyAmt
			, page : '1'
			, rows : '500'
			, sidx : 'STAN_YMD, MONEY_CODE, MEMBER_ORDER, MONEY_AMT DESC, MEMBER_NAME ASC, MEMBER_TYPE'
			, sord : 'ASC'
		},
		success: function(result) {
			
			let prevMoneyName = '';
			
			for ( var i=0;i<result.rows.length;i++) {
				let moneyName = result.rows[i].moneyName;
				let memberName = result.rows[i].memberName;
				let moneyAmt = result.rows[i].moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
				
	            var tableTr = '<tr>';
				if ( prevMoneyName != moneyName ) {	prevMoneyName = moneyName;
		            tableTr += '<td class="pTd center">' + moneyName + '</td>';
				} else {
		            tableTr += '<td class="pTd center"></td>';
				}
	            tableTr += '<td class="pTd center">' + memberName + '</td>';
	            tableTr += '<td class="pTd money">' + moneyAmt + '</td>';
	            tableTr += '</tr>';
	
	            $('#tableBody').append(tableTr);
				
			}
		}
	});

});
