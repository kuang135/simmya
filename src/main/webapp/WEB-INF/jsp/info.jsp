<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="/jquery-easyui-1.4.1/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="/jquery-easyui-1.4.1/themes/icon.css">
	</head>
	<body>
		<table id="dg" class="easyui-datagrid" 
			data-options="url:'/manage/info.do',fit:true,fitColumns:true,rownumbers:true,toolbar:'#toolbar',pagination:true,pageSize:20">
	    <thead>
	        <tr>
	            <th data-options="field:'ck',checkbox:true"></th>
	            <th data-options="field:'id',hidden:true">id</th>
	            <th data-options="field:'name',width:200,align:'center'">标题</th>
	            <th data-options="field:'title',width:200,align:'center'">摘要</th>
	            <th data-options="field:'detail',width:250,align:'center'">详细内容</th>
	            <th data-options="field:'imageAddress',width:250,align:'center'">图片地址</th>
	            <th data-options="field:'source',width:250,align:'center'">咨询出处</th>
	            <th data-options="field:'collectCount',width:250,align:'center'">收藏数</th>
	            <th data-options="field:'shareCount',width:250,align:'center'">分享数</th>
	            <th data-options="field:'discussCount',width:250,align:'center'">评论数</th>
	            <th data-options="field:'agreeCount',width:250,align:'center'">点赞数</th>
	            <th data-options="field:'clickCount',width:250,align:'center'">点击数</th>
	        </tr>
	    </thead>
	</table>
	<div id="toolbar" style="padding:5px;height:auto">
		<table width="100%">
			<tr>
				<td align="left">
					资讯标题：<input id="searchName" class="easyui-searchbox" data-options="width:110,searcher:doSearch"/>
            		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="doRefresh()"></a>
				</td>
				<td align="right">
					<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'"  onclick="openAdd()">添加</a>
            		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'" onclick="doDelete()">删除</a>
					<!-- <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'"  onclick="doEdit()">编辑</a> -->
				</td>
			</tr>
		</table>
    </div>
	
	 <div id="dlg" class="easyui-dialog" style="width:500px;height:500px;" data-options="closed:true,buttons:'#dlg-buttons'">
    	<form id="ff" method="post" enctype="multipart/form-data" action="/manage/info/add.do"> 
    		<br/>&nbsp;标题:<input id="name" class="easyui-textbox" name="name"/>&nbsp;<span id="nameIsNull"></span><br/><br/>
    		&nbsp;摘要:<input id="title" class="easyui-textbox" name="title"/>&nbsp;<span id="titleIsNull"></span><br/><br/>
    		&nbsp;上传图片:<input id="imageAddress" name="file" class="easyui-filebox" data-options="width:300,buttonText:'请选择文件'"/>&nbsp;<span id="imageIsNull"></span><br/><br/>
    		&nbsp;资讯出处:<input id="source" class="easyui-textbox" style="width:200px"/>&nbsp;<span id="sourceIsNull"></span><br/><br/>
    		&nbsp;详细内容:<input id="detail" class="easyui-textbox" name="detail" data-options="width:300,height:200"/>&nbsp;<span id="detailIsNull"></span>
		</form>  
    </div>
    
    <div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="doSave()">保存</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="doClose()">取消</a>
	</div>
	
		<script type="text/javascript" src="/jquery-easyui-1.4.1/jquery.min.js"></script>
		<script type="text/javascript" src="/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="/script/info.js"></script>
	</body>
</html>