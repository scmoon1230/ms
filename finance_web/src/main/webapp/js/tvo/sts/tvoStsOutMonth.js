$(function() {

	oTvoStsOutMonth = new tvoStsOutMonth();
	oTvoStsOutMonth.init();
});

function tvoStsOutMonth() {
	this.init = function() {

		oTvoStsOutMonth.grid();
		oTvoStsOutMonth.chart();

		$(window).bind('resize', function() {
			$('#grid-rain').setGridWidth($(window).width() - 10);
		}).trigger('resize');


	};


	function getGridSize(param)
	{
		var size = 12;
		if(param == 2) {
			size = 28;
		} else if (param == 1 || param == 3 || param == 5 || param == 7 || param == 8 || param == 10 || param == 12) {
			size = 31;
		} else if (param == 4 || param == 6 || param == 9 || param == 11) {
			size = 30;
		}
		return size;
	}

	function getColNames(gubun, param)
	{
		var unit = (param == '') ? "월" : "일";
		var size = getGridSize(param);

		var names = new Array();
		if(gubun != '') names.push(gubun);
		for(var i=1; i<=size ; i++) {
			names.push(i+unit);
		}
		return names;
	}

	function getColModel(param)
	{
		var size = getGridSize(param);

		var models = new Array();
		var wid = "'white-space: normal;width=80px;'";
		var firstModel = '({name:"GUBUN", align:"center", cellattr: function(){ return "style=' + wid + '" }})';

		models.push(eval(firstModel));

		for(var i=1; i<=size ; i++) {
			var string = "{name:'TERM" + i +"', align:'center', formatter:'currency',formatoptions: {thousandsSeparator: ',', decimalPlaces:0}}";
			var obj = eval('(' + string + ')');
			models.push(obj);
		}
		return models;
	}

		/** 그리드설정 */
		$('#grid').jqGrid(
		{
				  url: '<c:url value='/'/>wrks/info/sts/userConnStsList/getUserConnStatsData.json'
				, datatype : "json"
				, postData :
				{
					  searchUserType:$("#searchUserType option:selected").val()
					, searchYear:$("#searchYear option:selected").val()
					, searchMonth:$("#searchMonth option:selected").val()
				}
				, colNames : getColNames("구분", $("#searchMonth option:selected").val())
				, colModel : getColModel($("#searchMonth option:selected").val())
				, rowNum : 15
				, viewrecords:true
				, multiselect: false
				, loadonce:false
				, height : 'auto'
				, autowidth : true
				, jsonReader: {repeatitems: false, id: "ref"}

		});

		$(".btnS").bind("click",function()
		{
			if ($.validate(".tableTypeHalf.seachT") == false) {
				return;
			}
			$("#grid").setGridParam({rowNum : $('#rowPerPageList').val()});
			var myPostData = $("#grid").jqGrid('getGridParam', 'postData');
			//검색할 조건의 값을 설정한다.
			myPostData.searchUserType = $("#searchUserType option:selected").val();
			myPostData.searchYear = $("#searchYear option:selected").val();
			myPostData.searchMonth = $("#searchMonth option:selected").val();

			$("#grid").trigger("reloadGrid");
		});

		function search()
		{
			//document.listForm.action = "<c:url value='/wrks/info/sts/userConnStsList.do' />";
			//document.listForm.submit();
			$("#grid").trigger("reloadGrid");
		}



}