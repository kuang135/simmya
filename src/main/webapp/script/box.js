//搜索
function doSearch(){
	var searchName = $("#searchName").searchbox('getValue');
	 $("#dg").datagrid('load',{
		 name: searchName
	}); 
}

//刷新
function doRefresh(){
	$("#searchName").searchbox('setValue','');
	$("#dg").datagrid('load',{
		 name: ''
	}); 
}

//打开添加对话框
function openAdd(){
	$("#dlg").dialog('setTitle','添加盒子');
	$("#dlg").dialog('open');
	addDialogInit();
}
//对话框恢复初始状体
function addDialogInit(){
	$('#name').textbox('setValue','');
	$('#title').textbox('setValue','');
	$('#imageAddress').filebox('setValue', '');
	$('#price').textbox('setValue','');
	$('#detail').textbox('setValue','');
	$("#nameIsNull").html("");
	$("#titleIsNull").html("");
	$("#imageIsNull").html("");
	$("#priceIsNull").html("");
	$("#detailIsNull").html("");
}
//对话框保存操作
function doSave(){
	if (checkBeforeSubmit()) {
		$("#ff").submit();
	}
}
//对话框取消
function doClose(){
	$("#dlg").dialog("close");
}
//删除操作
function doDelete(){
	var rowObjArr=$("#dg").datagrid('getSelections');
	if(rowObjArr.length==0){
		alert("请选择要删除的盒子!");				
		return;
	}
	var idsArr=[];
	for(var i=0;i<rowObjArr.length;i++){
		idsArr.push(rowObjArr[i].id);
	}
	var ids=idsArr.join(",");
	$.messager.confirm('Confirm', '确定删除这'+idsArr.length+'条盒子吗?', 
		function(r){
			if(r) {
				$.post(
					"/manage/box/delete.do",
					{ids:ids},
					function(backData){
						if(backData.status==200){
							$.messager.alert('提示',backData.message,'info',
								function(){
									$("#searchName").searchbox('setValue','')
									$("#dg").datagrid('load',{name: ''});//加载全部数据
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


//修改资讯：添加，删除
function openInfo() {
	var rowObjArr=$("#dg").datagrid('getSelections');
	if(rowObjArr.length !=1 ){
		alert("请选择一个要修改的盒子!");				
		return;
	}
	var row = rowObjArr[0];
	$("#infoDlg").dialog('setTitle',row.name + ' - ' + row.boxPrice + '元/期');
	$("#infoDlg").dialog('open');
	$('#boxId').val(row.id);
	$('#info-dg').datagrid('clearSelections');
	$('#info-dg').datagrid({
	    url:'/manage/box/info.do?boxid='+row.id,
	    fit:true,fitColumns:true,rownumbers:true,pagination:false,pageSize:20,
	    idField:'id',
	    treeField:'name',
	    columns:[[
	        {title:'标题',field:'name',width:150,align:'center'},
	        {title:'摘要',field:'title',width:350,align:'center'}
	    ]],
	    onLoadSuccess : function(data){
	    	var rs = data.rows,
	    		len = data.rows.length;
	    	for (var i = 0; i < len; i++) {
	    		if (rs[i].selected) {
	    			console.log(rs[i].id);
	    			$('#info-dg').datagrid('selectRecord', rs[i].id);
	    		}
	    	} 
	    }
	});
}
//保存资讯更改
function saveInfo(){
	var boxid = $('#boxId').val();
	var rowObjArr=$("#info-dg").datagrid('getSelections');
	var idsArr=[];
	for(var i=0;i<rowObjArr.length;i++){
		idsArr.push(rowObjArr[i].id);
	}
	var infoids=idsArr.join(",");
	$.post(
			"/manage/box/editInfo.do",
			{boxid: boxid, infoids: infoids},
			function(backData){
				if(backData.status==200){
					$.messager.alert('提示',backData.message,'info',
						function(){
							$("#infoDlg").dialog("close");
							$("#searchName").searchbox('setValue','')
							$("#dg").datagrid('load',{name: ''});//加载全部数据
						}
					);
				}else {
					$.messager.alert('提示',backData.message,'warning',function(){});
				}
			}
		);
}
//资讯对话框取消
function closeInfo(){
	$("#infoDlg").dialog("close");
}
//验证
function checkBeforeSubmit(){
	var _name = $('#name').val();
	var _title = $('#title').val();
	var _imageAddress = $('#imageAddress').filebox('getValue');
	var _price = $('#price').val();
	var _detail = $('#detail').val();
	if(_name === ""){
		$("#nameIsNull").html("<font color='red'>名称不能为空!</font>");
		return false;
	}else{
		$("#nameIsNull").html("");
	}
	if(_title === ""){
		$("#titleIsNull").html("<font color='red'>摘要不能为空!</font>");
		return false;
	}else{
		$("#titleIsNull").html("");
	}
	if(_imageAddress === ""){
		$("#imageIsNull").html("<font color='red'>图片不能为空!</font>");
		return false;
	}else{
		$("#imageIsNull").html("");
	}
	if(!/^\d+(\.\d{2})?$/.test(_price)){
		$("#priceIsNull").html("<font color='red'>价格只能是整数或两位小数!</font>");
		return false;
	}else{
		if (/^0+[1-9]+(\.\d{2})?$/.test(_price)) {
			$("#priceIsNull").html("<font color='red'>价格只能是整数或两位小数!</font>");
			return false;
		} else {
			$("#priceIsNull").html("");
		}
	}
	if(_detail === ""){
		$("#detailIsNull").html("<font color='red'>详情不能为空!</font>");
		return false;
	}else{
		$("#detailIsNull").html("");
	}
	return true;
}
