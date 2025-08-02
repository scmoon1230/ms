<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/mntr/cmm/commonTags.jsp"%>
<div class="panel panel-default panel-ucp" id="div-gnrCastnet" style="background-color: #e1f0fa">
	<div class="panel-body">
		<div class="row">
			<div class="col-xs-3 text-left">
				<h3 class="panel-title" style="padding-top: 10px;">${divData.divTitle}</h3>
			</div>
			<div class="col-xs-8" style="background: #fff;">
				<div class="col-xs-4 text-center">
					<button class="btn btn-primary btn-sm btn-ucp" onclick="doCastNetByEvtOcrNo();">발생위치</button>
				</div>
				<!-- 
				<div class="col-xs-4 text-center">
					<button class="btn btn-primary btn-sm btn-ucp" onclick="doCastNet();">현재위치</button>
				</div>
				 -->
				<div class="col-xs-4">
					<button class="btn btn-primary btn-sm btn-ucp" onclick="stopCastNet();">해제</button>
				</div>
			</div>
		</div>
	</div>
</div>