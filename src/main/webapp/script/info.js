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
	$("#dg").datagrid('load', {name: ''}); 
}
		
//打开添加对话框
function openAdd(){
	$("#dlg").dialog('setTitle','添加资讯');
	$("#dlg").dialog('open');
	addDialogInit();
}

//编辑操作
function doEdit(){
	var rowObjArr=$("#dg").datagrid('getSelections');
	if(rowObjArr.length != 1){
		alert("请选择一条要编辑的信息!");				
		return;
	}
	$("#dlg").dialog('setTitle','编辑资讯');
	$("#dlg").dialog('open');
	for(var i in rowObjArr[0]) {
		alert(i + "--" + rowObjArr[0][i]);
	}
	editDialogInit(rowObjArr[0]);
}
		
//删除操作
function doDelete(){
	var rowObjArr=$("#dg").datagrid('getSelections');
	if(rowObjArr.length==0){
		alert("请选择要删除的资讯!");				
		return;
	}
	var idsArr=[];
	for(var i=0;i<rowObjArr.length;i++){
		idsArr.push(rowObjArr[i].id);
	}
	var ids=idsArr.join(",");
	$.messager.confirm('Confirm', '确定删除这'+idsArr.length+'条资讯吗?', 
		function(r){
			if(r) {
				$.post(
					"/manage/info/delete.do",
					{ids:ids},
					function(backData){
						if(backData.status==200){
							$.messager.alert('提示',backData.message,'info',
								function(){
									$("#searchName").searchbox('setValue','');
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

//验证
function checkBeforeSubmit(){
	var _name = $('#name').val();
	var _title = $('#title').val();
	var _imageAddress = $('#imageAddress').filebox('getValue');
	var _source = $('#source').val();
	var _detail = $('#detail').val();
	if(_name === ""){
		$("#nameIsNull").html("<font color='red'>标题不能为空!</font>");
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
	if(_source === ""){
		$("#sourceIsNull").html("<font color='red'>出处不能为空!</font>");
		return false;
	}else{
		$("#sourceIsNull").html("");
	}
	if(_detail === ""){
		$("#detailIsNull").html("<font color='red'>详情不能为空!</font>");
		return false;
	}else{
		$("#detailIsNull").html("");
	}
	return true;
}
//对话框恢复初始状体
function addDialogInit(){
	$('#name').textbox('setValue','');
	$('#title').textbox('setValue','');
	$('#imageAddress').filebox('setValue', '');
	$('#source').textbox('setValue','');
	$('#detail').textbox('setValue','');
	$("#nameIsNull").html("");
	$("#titleIsNull").html("");
	$("#imageIsNull").html("");
	$("#sourceIsNull").html("");
	$("#detailIsNull").html("");
}

function editDialogInit(row){
	$('#name').textbox('setValue', row.name);
	$('#title').textbox('setValue', row.title);
	$('#imageAddress').filebox('setValue', row.imageAddress);
	$('#source').textbox('setValue', row.source);
	$('#detail').textbox('setValue', row.detail);
	$("#nameIsNull").html("");
	$("#titleIsNull").html("");
	$("#imageIsNull").html("");
	$("#sourceIsNull").html("");
	$("#detailIsNull").html("");
}