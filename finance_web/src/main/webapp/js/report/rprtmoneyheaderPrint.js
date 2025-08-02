
$(document).ready(function(){
	
	$('.printStanYmd').text(opener.document.getElementById("stanYmd").value);
	
	let stanYmd     = opener.document.getElementById("stanYmd").value.replaceAll("-", "");
	let moneyCode   = opener.document.getElementById("moneyCode").value;
	let worshipCode = opener.document.getElementById("worshipCode").value;
	let idExist     = opener.document.getElementById("idExist").value;

	$.ajax({
		url:contextRoot + "/report/moneyheader/list.json",
		type:"POST",
		data:{
			stanYmd        : stanYmd
			, moneyCode    : moneyCode
			, worshipCode  : worshipCode
			, idExist      : idExist
			, page : '1'
			, rows : '50000'
			, sidx : sortBy
			, sord : sortOr
		},
		success: function(result) {
            $('#tableBody').empty();

			//console.log(result.rows.length);
			if ( 0 < result.rows.length ) {
				var moneyTotalAmt = 0;
				var prevAmt = result.rows[0].moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
				var memName = '';
				//console.log(prevAmt);
				
				var tableTr = '<tr>';
	            tableTr += '<td class="pTd money" style="width:100px;">' + prevAmt + '</td>';

				let cnt = 1;
				for ( var i=0;i<result.rows.length;i++) {
					let memberName = result.rows[i].memberName;
					let moneyAmt = result.rows[i].moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
					moneyTotalAmt += result.rows[i].moneyAmt;
					
					if ( prevAmt != moneyAmt ) {	prevAmt = moneyAmt;
			            tableTr += '<td class="pTd left">' + memName + '</td>';
						tableTr += '</tr>';
						$('#tableBody').append(tableTr);
						tableTr = '<tr>';
			            tableTr += '<td class="pTd money">' + moneyAmt + '</td>';
						memName = memberName;
					} else {
						if ( memName == '' ) {	memName = memberName;	}
						else {		memName = memName+', '+memberName;	}
					}
					
				/*	if ( cnt%10 == 0 ) {	tableTr += '</tr>';	$('#tableBody').append(tableTr);	tableTr = '<tr>'; }
					if ( prevAmt != moneyAmt ) {	prevAmt = moneyAmt;
			            tableTr += '<td>' + moneyAmt + '</td>';		cnt++;
						if ( cnt%10 == 0 ) {	tableTr += '</tr>';	$('#tableBody').append(tableTr);	tableTr = '<tr>'; }
			            tableTr += '<td>' + memberName + '</td>';	cnt++;
					} else {
			            tableTr += '<td>' + memberName + '</td>';	cnt++;
					}	*/

				}
	            tableTr += '<td class="pTd left">' + memName + '</td></tr>';

	            tableTr += '<tr><td class="pBig pTd center" style="background-color: #eee; font-weight: bold;color: #444;">합계</td>';
	            tableTr += '<td class="pBig pTd" style="background-color: #eee; font-weight: bold;color: #444;">';
	            tableTr += moneyTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",")+'원';
	            tableTr += ' ('+result.rows.length+'건)</td></tr>';

	            $('#tableBody').append(tableTr);
			}
			
		}
	});

});
