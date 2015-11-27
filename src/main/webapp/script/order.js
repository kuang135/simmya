//搜索
function doSearch(){
	var searchStatus = $("#searchStatus").combobox('getValue');
	 $("#dg").datagrid('load',{
		 status: searchStatus
	}); 
}

//刷新
function doRefresh(){
	$("#searchStatus").combobox('setValue','');
	$("#dg").datagrid('load', {status: ''}); 
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
	        {title:'最后一次方送时间',field:'updateTime',width:200,align:'center'}
	    ]]
	});
}