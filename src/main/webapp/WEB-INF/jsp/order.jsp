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
			data-options="url:'/manage/order/list.do',fit:true,singleSelect:true,fitColumns:true,
				rownumbers:true,toolbar:'#toolbar',pagination:true,pageSize:20,onDblClickRow:showBox">
		    <thead>
		        <tr>
		            <th data-options="field:'id',width:400,align:'center'">订单号</th>
		            <th data-options="field:'status',width:200,align:'center'">订单状态</th>
		            <th data-options="field:'totalPrice',width:200,align:'center'">总额</th>
		            <th data-options="field:'createTime',width:200,align:'center'">创建时间</th>
		            <th data-options="field:'address',width:400,align:'center'">收货地址</th>
		            <th data-options="field:'getName',width:200,align:'center'">收货人姓名</th>
		            <th data-options="field:'phone',width:200,align:'center'">手机号</th>
		            <th data-options="field:'zipcode',width:100,align:'center'">邮编</th>
		        </tr>
		    </thead>
		</table>
		<div id="toolbar" style="padding:5px;height:auto">
			<table width="100%">
				<tr>
					<td align="left">
						<select id="searchStatus" class="easyui-combobox" data-options="width:120,panelWidth:100,panelHeight:105">
						    <option value="">不限</option>
						    <option value="未付款">未付款</option>
						    <option value="已付款">已付款</option>
						    <option value="已完成">已完成</option>
						    <option value="已退订">已退订</option>
						</select>
						<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch()">搜索</a>
	            		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'" onclick="doRefresh()"></a>
					</td>
				</tr>
			</table>
	    </div>
	    
	    <div id="box-dlg" class="easyui-dialog" style="width:900px;height:95%;" data-options="closed:true">
   		<table id="box-dg" class="easyui-datagrid"></table>
    </div>
	</body>
</html>