
$(document).ready(function(){

	let temp = opener.document.getElementById("stanYmd").value;
	let printStanYmd = '( '+temp.substring(0,4)+' 년 '+temp.substring(5,7)+' 월 '+temp.substring(8,10)+' 일 )';
	$('.printStanYmd').text(printStanYmd);

	let stanYmd   = opener.document.getElementById("stanYmd").value.replaceAll("-", "");
	//let acctGb    = opener.document.getElementById("acctGb").value;

	$.ajax({
		url:contextRoot + "/sheet/inouttotal/list.json",
		type:"POST",
		data:{
			stanYmd    : stanYmd
		//	, acctGb   : acctGb
			, page : '1'
			, rows : '500'
			, sidx : 'IO_ORDER , ORD'
			, sord : 'ASC'
		},
		success: function(result) {
			
			let preTotalAmt   = 0;
			let moneyTotalAmt = 0;
			let outTotalAmt   = 0;
			let nextTotalAmt  = 0;
						
			for ( var i=0;i<result.rows.length;i++) {
				
				let preAmt   = result.rows[i].preAmt;
	            let moneyAmt = result.rows[i].moneyAmt;
	            let outAmt   = result.rows[i].outAmt;
				let nextAmt  = result.rows[i].preAmt + result.rows[i].inAmt - result.rows[i].outAmt;
								
				preAmt   = Math.round(preAmt/1000);
	            moneyAmt = Math.round(moneyAmt/1000);
	            outAmt   = Math.round(outAmt/1000);
				nextAmt  = Math.round(nextAmt/1000);
				
				preTotalAmt   += preAmt;
				moneyTotalAmt += moneyAmt;
				outTotalAmt   += outAmt;
				nextTotalAmt  += nextAmt;
				
				if (0 == preAmt) { preAmt = '';
				} else { preAmt   = preAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
				}

				if (0 == moneyAmt) { moneyAmt = '';
				} else { moneyAmt   = moneyAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
				}
				
				if (0 == outAmt) { outAmt = '';
				} else { outAmt   = outAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
				}
				
				if (0 == nextAmt) { nextAmt = '';
				} else { nextAmt   = nextAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
				}
								
	            var tableTr = '<tr>';
	            tableTr += '<td class="pBig pTd center">' + result.rows[i].acctGbName + '</td>';
	            tableTr += '<td class="pBig pTd center">' + result.rows[i].acctName + '</td>';
	            tableTr += '<td class="pBig pTd money">' + preAmt + '</td>';
	            tableTr += '<td class="pBig pTd money">' + moneyAmt + '</td>';
	            tableTr += '<td class="pBig pTd money">' + outAmt + '</td>';
	            tableTr += '<td class="pBig pTd money">' + nextAmt + '</td>';
	            tableTr += '</tr>';
	
	            $('#tableBody').append(tableTr);
				
			}
			
			//preTotalAmt   = Math.round(preTotalAmt/1000);
            //moneyTotalAmt = Math.round(moneyTotalAmt/1000);
            //outTotalAmt   = Math.round(outTotalAmt/1000);
			//nextTotalAmt  = Math.round(nextTotalAmt/1000);
				
			preTotalAmt   = preTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
			moneyTotalAmt = moneyTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
			outTotalAmt   = outTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
			nextTotalAmt  = nextTotalAmt.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
			
            var tableTr = '<tr>';
            tableTr += '<td class="pBig pTd center" style="background-color: #eee; font-weight: bold;color: #444;" colspan=2>합계</td>';
            tableTr += '<td class="pBig pTd money" style="background-color: #eee; font-weight: bold;color: #444;">' + preTotalAmt + '</td>';
            tableTr += '<td class="pBig pTd money" style="background-color: #eee; font-weight: bold;color: #444;">' + moneyTotalAmt + '</td>';
            tableTr += '<td class="pBig pTd money" style="background-color: #eee; font-weight: bold;color: #444;">' + outTotalAmt + '</td>';
            tableTr += '<td class="pBig pTd money" style="background-color: #eee; font-weight: bold;color: #444;">' + nextTotalAmt + '</td>';
            tableTr += '</tr>';

            $('#tableBody').append(tableTr);
				
		}
	});

});
