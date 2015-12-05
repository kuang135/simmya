//搜索
function doSearch(){
	var searchStatus = $("#searchStatus").combobox('getValue');
	var searchOrderId = $("#searchOrderId").textbox('getValue');
	 $("#dg").datagrid('load',{
		 status: searchStatus,
		 orderid: searchOrderId
	}); 
}

//刷新
function doRefresh(){
	$("#searchStatus").combobox('setValue','');
	$("#searchOrderId").textbox('setValue','');
	$("#dg").datagrid('load', {status: '', orderid: ''}); 
}
		
function showBox(index, row) {
	$("#box-dlg").dialog('setTitle', '订单号: ' + row.id);
	$("#box-dlg").dialog('open');
	$('#box-dg').datagrid({
	    url:'/manage/order/box.do?orderid='+row.id,
	    fit:true,fitColumns:true,rownumbers:true,pagination:false,singleSelect:true,
	    columns:[[
	        {title:'盒子名称',field:'name',width:200,align:'center'},
	        {title:'盒子价格',field:'boxPrice',width:100,align:'center'},
	        {title:'订阅期限',field:'orderCount',width:80,align:'center',formatter:function(value){return value === null? 0: value}},
	        {title:'投递方式',field:'orderWay',width:100,align:'center'},
	        {title:'状态',field:'status',width:100,align:'center'},
	        {title:'已发次数',field:'sendCount',width:80,align:'center',formatter:function(value){return value === null? 0: value}},
	        {title:'最后一次发送时间',field:'updateTime',width:200,align:'center'},
	        {title:'发送状态',field:'sendStatus',width:100,align:'center'}
	    ]]
	});
}

//发送
function doSend() {
	var rowObjArr=$("#box-dg").datagrid('getSelections');
	if(rowObjArr.length != 1){
		alert("请选择一个发送的盒子!");				
		return;
	}
	if (rowObjArr[0].sendCount >= rowObjArr[0].orderCount) {
		alert("该盒子以全部发送，不能再次发送!");				
		return;
	}
	var orderid = $("#dg").datagrid('getSelections')[0].id;
	var boxid = rowObjArr[0].id;
	$.messager.confirm('Confirm', '确定已经发送这个条盒子?', 
		function(r){
			if(r) {
				$.post(
					"/manage/order/sendBox.do",
					{boxid : boxid, orderid: orderid},
					function(backData){
						if(backData.status==200){
							$.messager.alert('提示',backData.message,'info',
								function(){
									$("#box-dg").datagrid('load');//加载全部数据
								}
							);
						}else {
							$.messager.alert('提示',backData.message,'warning',function(){});
						}
					}
				);
			}
		}
	);
}