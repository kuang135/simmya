<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>什么呀后台管理</title>
	<link rel="stylesheet" type="text/css" href="/jquery-easyui-1.4.1/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="/jquery-easyui-1.4.1/themes/icon.css">
</head>
<body class="easyui-layout">
    <div data-options="region:'north',split:true" style="height:80px;">
    	<table border="0" width="100%" height="100%">
    		<tr>
    			<td align="center"><font size="5" color="green">&nbsp;欢迎您,亲爱的用户</font></td>
    			<td align="right"><font size="4"><a href="#">注销</a></font>&nbsp;</td>
    			<script>
					setInterval("document.getElementById('showTime').innerHTML=new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());",100);
				</script>
    		</tr>
    		<tr><td colspan="2" align="right"><span id="showTime"></span></td></tr>
    	</table>
    </div>
    <div data-options="region:'west',title:'导航',split:true" style="width:130px;">
    	<ul id="nav"></ul>
    </div>
    <div data-options="region:'center'" style="padding:5px;">
    	<div id="tabs">
    		<div title="首页">
    		</div>
    	</div>
    </div>
<script type="text/javascript" src="/jquery-easyui-1.4.1/jquery.min.js"></script>
<script type="text/javascript" src="/jquery-easyui-1.4.1/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/jquery-easyui-1.4.1/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/script/home.js"></script>
</body>
</html>