	$(function() {
		oTvoStsOutList = new tvoStsOutList();
		oTvoStsOutList.init();
	});
	
	function tvoStsOutList() {
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
			
			oTvoStsOutList.grid();
			
			$('#searchKeyword').keypress(function(event) {
				if (event.which == 13) {
					oTvoStsOutList.reloadGrid();
				}
			});
	
		}
		
		this.grid = function() {
	
			var sStrDateStart = $("#strDateStart").val();
			momentStsOutYmdhms = moment(sStrDateStart, 'YYYY-MM-DD HH:mm:ss');
			sStrDateStart = momentStsOutYmdhms.format('YYYYMMDD');		
	
			var sStrDateEnd = $("#strDateEnd").val();
			momentStsEndYmdhms = moment(sStrDateEnd, 'YYYY-MM-DD HH:mm:ss');
			sStrDateEnd = momentStsEndYmdhms.format('YYYYMMDD');
			
			$('#grid-sts').jqGrid({
				url:contextRoot + '/tvo/sts/tvoStsOutListData.json',
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
					pagenationReload('sts', data, oTvoStsOutList.getGridParams());
				},			
				colNames :
					[  '번호', '구분', '신청일시 ', '신청자ID', '신청자', '사건번호', '사건명', '신청사유', 'CCTVID', 'CCTV명', '영상요청구간', '마스킹여부', '반출파일명', '재생종료일시', '진행상태'
					],
				colModel :
					[     { name : 'rk'				, hidden : true}
					    , { name : 'gubun'			, hidden : true} 
						, { name : 'outRqstYmdhms'	, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}
						//	, formatter : function(cellvalue, options, rowObject) {
						//		var sOutRqstYmdhms = rowObject.outRqstYmdhms;
						//		momentOutRqstYmdhms = moment(sOutRqstYmdhms, 'YYYY-MM-DD HH:mm:ss');
						
						//		sOutRqstYmdhms = momentOutRqstYmdhms.format('YYYY-MM-DD HH:mm');
						//		return sOutRqstYmdhms;
						//	}
						  }						
						, { name : 'outRqstUserId'	, hidden : true}
						, { name : 'outRqstUserNm'	, width:70 , align:'center', cellattr : function() {return 'style="width: 70px;"';}}
						, { name : 'evtNo'			, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}}
						, { name : 'evtNm'			, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}}
						, { name : 'rqstRsnTyNm'	, width:50 , align:'center', cellattr : function() {return 'style="width: 50px;"';}}
						, { name : 'cctvId'			, hidden : true}
						, { name : 'cctvNm'			, width:150, align:'left'  , cellattr : function() {return 'style="width: 150px;"';}}
						, { name : 'vdoYmdhmsFrTo'	, width:200, align:'center', cellattr : function() {return 'style="width: 200px;"';}}
						, { name : 'maskingYn'		, width:50 , align:'center', cellattr : function() {return 'style="width: 50px;"';}}
						, { name : 'outFileNmDrm'	, hidden : true}
						, { name : 'playEndYmdhms'	, width:100, align:'center', cellattr : function() {return 'style="width: 100px;"';}
						//	, formatter : function(cellvalue, options, rowObject) {
						//		var sPlayEndYmdhms = rowObject.playEndYmdhms;
						//		momentPlayEndYmdhms = moment(sPlayEndYmdhms, 'YYYY-MM-DD HH:mm:ss');
						//		if (momentPlayEndYmdhms.isValid()) {
						//			sPlayEndYmdhms = momentPlayEndYmdhms.format('YYYY-MM-DD HH:mm');
						//		}
						//		else {
						//			sPlayEndYmdhms = '';
						//		}
						//		return sPlayEndYmdhms;
						//	}
						  }
						, { name : 'tvoPrgrsNm'		, width:100, align:'left'  , cellattr : function() {return 'style="width: 100px;"';}}							
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
			//alert($("#stsRqstYmdhms").val());
			if($("#strDateStart").val() > $("#strDateEnd").val() ){	oCommon.modalAlert('modal-alert', '알림', '시작일이 종료일보다 큽니다.');		return false;	}
			//var oParams = oTvoStsRqstList.getGridParams();
			gridReload('sts', 1, oTvoStsOutList.getGridParams());		
	
		}
		
		this.getGridParams = function() {
			
			var sStrDateStart = $("#strDateStart").val();
			momentStsOutYmdhms = moment(sStrDateStart, 'YYYY-MM-DD HH:mm:ss');
			sStrDateStart = momentStsOutYmdhms.format('YYYYMMDD');		
	
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
			momentStsOutYmdhms = moment(sStrDateStart, 'YYYY-MM-DD HH:mm:ss');
			sStrDateStart = momentStsOutYmdhms.format('YYYYMMDD');		
	
			var sStrDateEnd = $("#strDateEnd").val();
			momentStsEndYmdhms = moment(sStrDateEnd, 'YYYY-MM-DD HH:mm:ss');
			sStrDateEnd = momentStsEndYmdhms.format('YYYYMMDD');			
			//alert("sStrDateEnd="+sStrDateEnd);
			
			document.outExcelDownFrm.searchTermStart.value = sStrDateStart;
			document.outExcelDownFrm.searchTermEnd.value = sStrDateEnd;
			//document.outExcelDownFrm.searchUserType.value = $("#searchUserType option:selected").val();
			document.outExcelDownFrm.action = contextRoot + '/tvo/sts/tvoStsOutListData.excel';
			document.outExcelDownFrm.submit();
		}		
	
	//	this.search = function() {};
	//	this.refresh = function() {};
	}