	$(function() {
		oTvoStsRqstList = new tvoStsRqstList();
		oTvoStsRqstList.init();
	});
	
	function tvoStsRqstList() {
		this.init = function() {
			setInputDate('strDateStart', 'strDateEnd', 30);
			
			$('.datetimepicker-ymd').datetimepicker({
				ignoreReadonly : true,
				format : 'YYYY-MM-DD',
				locale : 'ko',
				minDate : '2023-01',
				maxDate : moment()
			});
	
			$('.datetimepicker-ymd').on('dp.show', function(e) {
				var $element = $('.bootstrap-datetimepicker-widget.dropdown-menu');
				var rect = $element.get(0).getBoundingClientRect();
				$('.bootstrap-datetimepicker-widget.dropdown-menu').css({
					'position' : 'fixed',
					'top' : rect.top,
					'left' : rect.left,
					'right' : rect.right,
					'bottom' : rect.bottom,
					'width' : rect.width,
					'height' : rect.height
				});
			});
			
			oTvoStsRqstList.grid();
			
			$('#searchKeyword').keypress(function(event) {
				if (event.which == 13) {
					oTvoStsRqstList.reloadGrid();
				}
			});
	
		}
		
		this.grid = function() {
	
			var sStrDateStart = $("#strDateStart").val();
			momentStsRqstYmdhms = moment(sStrDateStart, 'YYYY-MM-DD HH:mm:ss');
			sStrDateStart = momentStsRqstYmdhms.format('YYYYMMDD');		
	
			var sStrDateEnd = $("#strDateEnd").val();
			momentStsEndYmdhms = moment(sStrDateEnd, 'YYYY-MM-DD HH:mm:ss');
			sStrDateEnd = momentStsEndYmdhms.format('YYYYMMDD');
			
			$('#grid-sts').jqGrid({
				url:contextRoot + '/tvo/sts/tvoStsViewListData.json',
				datatype: 'json',
				mtype: 'POST',
				height : 'auto',
				rowNum : 20,
				autowidth : true,
				shrinkToFit : true,
				multiselect : false,
				multiboxonly : false,
				beforeRequest: function() {
					// validate check here!				
				},				
				postData: {
					searchTermStart : sStrDateStart
					, searchTermEnd : sStrDateEnd			
					, searchUserType: $('#searchUserType').val()
				},	
				loadComplete : function (data){
					checkGridNodata('sts', data);
					pagenationReload('sts', data, oTvoStsRqstList.getGridParams());
				},			
				colNames :
					[ '번호', '구분', '신청일시 ', '신청자ID', '신청자', '사건번호', '사건명', '발생일시', '발생주소', '신청목적CD', '신청사유', '신청사유상세', '열람종료일시', '진행상태'
					],
				colModel :
					[     { name : 'rk'				, hidden : true}
					    , { name : 'gubun'			, hidden : true} 
						, { name : 'viewRqstYmdhms'	, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}
						//	, formatter : function(cellvalue, options, rowObject) {
						//		var sViewRqstYmdhms = rowObject.viewRqstYmdhms;
						//		momentViewRqstYmdhms = moment(sViewRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');

						//		sViewRqstYmdhms = momentViewRqstYmdhms.format('YYYY-MM-DD HH:mm');
						//		return sViewRqstYmdhms;
						//	}
						  }	
						, { name : 'viewRqstUserId'	, hidden : true}
						, { name : 'viewRqstUserNm'	, width:70 , align:'center', cellattr : function() {return 'style="width: 70px;"';}}
						, { name : 'evtNo'			, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}}
						, { name : 'evtNm'			, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}}							
						, { name : 'evtYmdHms'		, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}}
						, { name : 'evtAddr'		, width:200, align:'left'  , cellattr : function() {return 'style="width: 200px;"';}}
					    , { name : 'rqstRsnTyCd'	, hidden : true} 						
						, { name : 'rqstRsnTyNm'	, width:50 , align:'center', cellattr : function() {return 'style="width: 50px;"';}}		
						, { name : 'rqstRsnDtl'		, hidden : true}						
						, { name : 'viewEndYmdhms'	, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}
						//	, formatter : function(cellvalue, options, rowObject) {
						//		var sViewEndYmdhms = rowObject.viewEndYmdhms;
						//		momentViewEndYmdhms = moment(sViewEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						//		if (momentViewEndYmdhms.isValid()) {
						//			sViewEndYmdhms = momentViewEndYmdhms.format('YYYY-MM-DD HH:mm');
						//		}else{
						//			sViewEndYmdhms = '';
						//		}
						//		return sViewEndYmdhms;
						//	}
						  }
						, { name : 'tvoPrgrsNm'	, width:100, align:'left'  , cellattr : function() {return 'style="width: 100px;"';}}
					],			
				jsonReader:
				{
					root : "rows"
					,total : "totalPages"
					,records: function(obj) { $('#rowCnt').text(obj.totalRows); return obj.totalRows; 
					}				
				},
				cmTemplate : {
					sortable : false,
					resizable : false
				}
			
			});
			
		};
	
		this.reloadGrid = function() {
			//alert($("#strDateStart").val());
			if($("#strDateStart").val() > $("#strDateEnd").val() ){ 	oCommon.modalAlert('modal-alert', '알림', '시작일이 종료일보다 큽니다.');		return false;	}
			//var oParams = oTvoStsRqstList.getGridParams();
			gridReload('sts', 1, oTvoStsRqstList.getGridParams());		
	
		}
		
		this.getGridParams = function() {
			
			var sStrDateStart = $("#strDateStart").val();
			momentStsRqstYmdhms = moment(sStrDateStart, 'YYYY-MM-DD HH:mm:ss');
			sStrDateStart = momentStsRqstYmdhms.format('YYYYMMDD');		
	
			var sStrDateEnd = $("#strDateEnd").val();
			momentStsEndYmdhms = moment(sStrDateEnd, 'YYYY-MM-DD HH:mm:ss');
			sStrDateEnd = momentStsEndYmdhms.format('YYYYMMDD');
			
			var searchTermStart = sStrDateStart;
			var searchTermEnd = sStrDateEnd;		
			var searchUserType = $('#searchUserType option:selected').val();
	
			var params = {
				searchTermStart : searchTermStart,
				searchTermEnd   : searchTermEnd,
				searchUserType  : searchUserType,
			};
			return params;
		}
		
		this.gotoExcelDownload = function() {
			var sStrDateStart = $("#strDateStart").val();
			momentStsRqstYmdhms = moment(sStrDateStart, 'YYYY-MM-DD HH:mm:ss');
			sStrDateStart = momentStsRqstYmdhms.format('YYYYMMDD');		
	
			var sStrDateEnd = $("#strDateEnd").val();
			momentStsEndYmdhms = moment(sStrDateEnd, 'YYYY-MM-DD HH:mm:ss');
			sStrDateEnd = momentStsEndYmdhms.format('YYYYMMDD');			
			//alert("sStrDateEnd="+sStrDateEnd);
			
			document.excelDownFrm.searchTermStart.value = sStrDateStart;
			document.excelDownFrm.searchTermEnd.value = sStrDateEnd;
			//document.excelDownFrm.searchUserType.value = $("#searchUserType option:selected").val();
			document.excelDownFrm.action = contextRoot + '/tvo/sts/tvoStsViewListData.excel';
			document.excelDownFrm.submit();
		}		
	
	//	this.search = function() {};
	//	this.refresh = function() {};
	}