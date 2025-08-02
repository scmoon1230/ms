
$(document).ready(function(){

	let ioGb = opener.document.getElementById("inoutGb");
	let aGb = opener.document.getElementById("acctGb");
	
	let ioName = ioGb.options[ioGb.selectedIndex].text;
	let acctGbName = aGb.options[aGb.selectedIndex].text;
	$('.printTitle').text('('+acctGbName+') '+ioName+' 명세표');

	let temp = opener.document.getElementById("stanYmd").value;
	let printStanYmd = temp.substring(0,4)+' 년 '+temp.substring(5,7)+' 월 '+temp.substring(8,10)+' 일';
	$('.printStanYmd').text(printStanYmd);

	let stanYmd   = opener.document.getElementById("stanYmd").value.replaceAll("-", "");
	let acctGb    = opener.document.getElementById("acctGb").value;
	let inoutGb   = opener.document.getElementById("inoutGb").value;

	$.ajax({
		url:contextRoot + "/sheet/inven/list.json",
		type:"POST",
		data:{
			stanYmd    : stanYmd
			, acctGb   : acctGb
			, inoutGb  : inoutGb
			, page : '1'
			, rows : '500'
			, sidx : 'ACCT_GB, UP_MONEY_AMT DESC, MONEY_AMT'
			, sord : 'DESC'
		},
		success: function(result) {
			
			if ( 0 < result.rows.length ) {
				var moneyTotalAmt = 0;
				
				var prevAcctName = '';
				var prevUpMoneyAmt = '';
				
				for ( var i=0;i<result.rows.length;i++) {
					
		            let moneyAmt   = result.rows[i].moneyAmt;
		            let upMoneyAmt = result.rows[i].upMoneyAmt;
					
					moneyTotalAmt += moneyAmt;
									
					//moneyAmt = Math.round(moneyAmt/1000);
					//upMoneyAmt = Math.round(upMoneyAmt/1000);
		            
					if (0 == moneyAmt) { moneyAmt = '';
					} else { moneyAmt   = moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
					}
					
					if (0 == upMoneyAmt) { upMoneyAmt = '';
					} else { upMoneyAmt   = upMoneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
					}
									
		            var tableTr = '<tr>';
					if ( "수입" == ioName ) {
			            tableTr += '<td class="pBig pTd left">' + result.rows[i].acctName + '</td>';
			            tableTr += '<td class="pBig pTd left">' + result.rows[i].acctRemark + '</td>';
			            tableTr += '<td class="pBig pTd money">' + moneyAmt + '</td>';
			            tableTr += '<td class="pBig pTd money">' + moneyAmt + '</td>';

					} else {
						if ( prevAcctName != result.rows[i].upName ) {	prevAcctName = result.rows[i].upName;
				            tableTr += '<td class="pBig pTd left">' + result.rows[i].upName + '</td>';
						} else {
				            tableTr += '<td class="pBig pTd left"></td>';
						}

		    	        tableTr += '<td class="pBig pTd left">' + result.rows[i].acctRemark + '</td>';
		        	    tableTr += '<td class="pBig pTd money">' + moneyAmt + '</td>';

						if ( prevUpMoneyAmt != upMoneyAmt ) {	prevUpMoneyAmt = upMoneyAmt;
				            tableTr += '<td class="pBig pTd money">' + upMoneyAmt + '</td>';
						} else {
				            tableTr += '<td class="pBig pTd money"></td>';
						}
					}
		            tableTr += '</tr>';
		
		            $('#tableBody').append(tableTr);
					
				}
				
	            //moneyTotalAmt = Math.round(moneyTotalAmt/1000);
					
				moneyTotalAmt = moneyTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
				
	            var tableTr = '<tr>';
	            tableTr += '<td class="pBig pTd center" style="background-color: #eee; font-weight: bold;color: #444;" colspan=3>합계</td>';
	            tableTr += '<td class="pBig pTd money" style="background-color: #eee; font-weight: bold;color: #444;" colspan=2>' + moneyTotalAmt + '</td>';
	            tableTr += '</tr>';
	
	            $('#tableBody').append(tableTr);
					
			}
		}
	});

});
