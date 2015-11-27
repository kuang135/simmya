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
		<script type="text/javascript" src="/script/order.js"></script>
	</head>
	<body>
		<table id="dg" class="easyui-datagrid" 
			data-options="url:'/manage/order/list.do',fit:true,fitColumns:true,rownumbers:true,toolbar:'#toolbar',pagination:true,pageSize:20">
		    <thead>
		        <tr>
		            <th data-options="field:'ck',checkbox:true"></th>
		            <th data-options="field:'id',hidden:true">id</th>
		            <th data-options="field:'name',width:200,align:'center'">标题</th>
		            <th data-options="field:'title',width:300,align:'center'">摘要</th>
		            <th data-options="field:'detail',width:600,align:'center'">详细内容</th>
		            <!-- <th data-options="field:'imageAddress',width:500,align:'center'">图片地址</th> -->
		            <th data-options="field:'source',width:400,align:'center'">咨询出处</th>
		            <th data-options="field:'collectCount',width:80,align:'center',formatter:function(value){return value === null? 0: value}">收藏数</th>
		            <th data-options="field:'shareCount',width:80,align:'center',formatter:function(value){return value === null? 0: value}">分享数</th>
		            <th data-options="field:'discussCount',width:80,align:'center',formatter:function(value){return value === null? 0: value}">评论数</th>
		            <th data-options="field:'agreeCount',width:80,align:'center',formatter:function(value){return value === null? 0: value}">点赞数</th>
		            <th data-options="field:'clickCount',width:80,align:'center',formatter:function(value){return value === null? 0: value}">点击数</th>
		        </tr>
		    </thead>
		</table>
		<div id="toolbar" style="padding:5px;height:auto">
			<table width="100%">
				<tr>
					<td align="left">
						<input id="searchName" class="easyui-searchbox" data-options="prompt:'咨询名称',width:200,searcher:doSearch"/>
	            		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="doRefresh()"></a>
					</td>
					<td align="right">
						<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"  onclick="openAdd()">添加</a>
	            		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="doDelete()">删除</a>
						<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"  onclick="openEdit()">编辑</a>
					</td>
				</tr>
			</table>
	    </div>
	</body>
</html>