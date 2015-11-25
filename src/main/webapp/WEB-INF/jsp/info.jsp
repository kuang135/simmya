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
		<script type="text/javascript" src="/script/info.js"></script>
	</head>
	<body>
		<table id="dg" class="easyui-datagrid" 
			data-options="url:'/manage/info/list.do',fit:true,fitColumns:true,rownumbers:true,toolbar:'#toolbar',pagination:true,pageSize:20">
	    <thead>
	        <tr>
	            <th data-options="field:'ck',checkbox:true"></th>
	            <th data-options="field:'id',hidden:true">id</th>
	            <th data-options="field:'name',width:200,align:'center'">标题</th>
	            <th data-options="field:'title',width:200,align:'center'">摘要</th>
	            <th data-options="field:'detail',width:250,align:'center'">详细内容</th>
	            <th data-options="field:'imageAddress',width:250,align:'center'">图片地址</th>
	            <th data-options="field:'source',width:250,align:'center'">咨询出处</th>
	            <th data-options="field:'collectCount',width:250,align:'center',formatter:function(value){return value === null? 0: value}">收藏数</th>
	            <th data-options="field:'shareCount',width:250,align:'center',formatter:function(value){return value === null? 0: value}">分享数</th>
	            <th data-options="field:'discussCount',width:250,align:'center',formatter:function(value){return value === null? 0: value}">评论数</th>
	            <th data-options="field:'agreeCount',width:250,align:'center',formatter:function(value){return value === null? 0: value}">点赞数</th>
	            <th data-options="field:'clickCount',width:250,align:'center',formatter:function(value){return value === null? 0: value}">点击数</th>
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
	
	 <div id="dlg" class="easyui-dialog" style="width:500px;height:500px;" data-options="closed:true,buttons:'#dlg-buttons'">
    	<form id="ff" method="post" enctype="multipart/form-data" action="/manage/info/add.do"> 
    		<br/>&nbsp;标题:<input id="name" class="easyui-textbox" name="name"/>&nbsp;<span id="nameIsNull"></span><br/><br/>
    		&nbsp;摘要:<input id="title" class="easyui-textbox" name="title"/>&nbsp;<span id="titleIsNull"></span><br/><br/>
    		&nbsp;上传图片:<input id="imageAddress" name="file" class="easyui-filebox" data-options="width:300,buttonText:'请选择文件'"/>&nbsp;<span id="imageIsNull"></span><br/><br/>
    		&nbsp;资讯出处:<input id="source" class="easyui-textbox" name="source" style="width:200px"/>&nbsp;<span id="sourceIsNull"></span><br/><br/>
    		&nbsp;详细内容:<input id="detail" class="easyui-textbox" name="detail" data-options="width:300,height:200"/>&nbsp;<span id="detailIsNull"></span>
		</form>  
    </div>
    <div id="dlg-buttons">
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="doAdd()">保存</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeAdd()">取消</a>
	</div>
	
	
	<div id="edit-dlg" class="easyui-dialog" style="width:500px;height:500px;" data-options="closed:true,buttons:'#edit-dlg-buttons'">
    	<form id="edit_ff" method="post" enctype="multipart/form-data" action="/manage/info/edit.do">
    		<input type="hidden" id="edit_id" name="id" value=""/> 
    		<input type="hidden" id="edit_image" name="imageAddress" value=""/> 
    		<br/>&nbsp;标题:<input id="edit_name" class="easyui-textbox" name="name"/>&nbsp;<span id="edit_nameIsNull"></span><br/><br/>
    		&nbsp;摘要:<input id="edit_title" class="easyui-textbox" name="title"/>&nbsp;<span id="edit_titleIsNull"></span><br/><br/>
    		&nbsp;图片:<img id="edit_imageUrl" src=""/><br/><br/>
    		&nbsp;更改图片:<input id="edit_imageAddress" name="edit_file" class="easyui-filebox" data-options="width:300,buttonText:'请选择文件'"/><br/><br/>
    		&nbsp;资讯出处:<input id="edit_source" class="easyui-textbox" name="source" style="width:200px"/>&nbsp;<span id="edit_sourceIsNull"></span><br/><br/>
    		&nbsp;详细内容:<input id="edit_detail" class="easyui-textbox" name="detail" data-options="width:300,height:200"/>&nbsp;<span id="edit_detailIsNull"></span>
		</form>  
    </div>
    <div id="edit-dlg-buttons">
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="doEdit()">修改</a>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="closeEdit()">取消</a>
	</div>
	
	</body>
</html>