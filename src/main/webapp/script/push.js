//刷新
function doRefresh(){
	$("#dg").datagrid('load'); 
}
//打开添加对话框
function openPush(){
	$("#dlg").dialog('setTitle','推送消息');
	$("#dlg").dialog('open');
	$('#message').textbox('setValue','');
	$("#messageIsNull").html("");
}
//对话框取消
function closePush(){
	$("#dlg").dialog("close");
}
//发送操作
function doPush(){
	if (checkBeforeSubmit()) {
		$.post(
				"/manage/push/add.do",
				{message: $('#message').textbox('getValue')},
				function(backData){
					if(backData.status==200){
						$.messager.alert('提示',backData.message,'info',
							function(){
								$("#dlg").dialog("close");
								$("#dg").datagrid('load');//加载全部数据
							}
						);
					}else {
						$.messager.alert('提示',backData.message,'warning',function(){});
					}
				}
			);
	}
}
//验证
function checkBeforeSubmit(){
	var message = $('#message').textbox('getValue');
	if(message === ""){
		$("#messageIsNull").html("<font color='red'>推送内容不能为空!</font>");
		return false;
	}else{
		$("#messageIsNull").html("");
	}
	return true;
}
