<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="/jquery-easyui-1.4.1/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="/jquery-easyui-1.4.1/themes/icon.css">
		<script type="text/javascript" src="/jquery-easyui-1.4.1/jquery.min.js"></script>
		<script type="text/javascript" src="/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="/script/push.js"></script>
	</head>
	<body>
		<table id="dg" class="easyui-datagrid" 
				data-options="url:'/manage/push/list.do',fit:true,singleSelect:true,fitColumns:true,
					rownumbers:true,toolbar:'#toolbar',pagination:true,pageSize:20">
			    <thead>
			        <tr>
			            <th data-options="field:'createTimeShow',width:200,align:'center'">推送时间</th>
			            <th data-options="field:'status',width:200,align:'center'">推送状态</th>
			            <th data-options="field:'message',width:600,align:'center'">推送内容</th>
			        </tr>
			    </thead>
			</table>
			<div id="toolbar" style="padding:5px;height:auto">
				<table width="100%">
					<tr>
						<td align="left">
							<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="doRefresh()"></a>
		            		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="openPush()"></a>
						</td>
					</tr>
				</table>
		    </div>
		    
		    <div id="dlg" class="easyui-dialog" style="width:400px;height:350px;" data-options="closed:true,buttons:'#dlg-buttons'">
		    	<form id="ff" method="post" action="/manage/push/add.do"> 
		    		<br/>
		    		&nbsp;推送内容:<input id="message" class="easyui-textbox" name="message" data-options="multiline:true,width:300,height:200"/>
		    		<br/><br/>&nbsp;<span id="messageIsNull"></span>
				</form>  
		    </div>
		    <div id="dlg-buttons">
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="doPush()">发送</a>
				<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closePush()">取消</a>
			</div>
	</body>
</html>