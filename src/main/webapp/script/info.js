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
//对话框保存操作
function doAdd(){
	if (checkBeforeAddSubmit()) {
		$("#ff").submit();
	}
}
//对话框取消
function closeAdd(){
	$("#dlg").dialog("close");
}

//编辑操作
function openEdit(){
	var rowObjArr=$("#dg").datagrid('getSelections');
	if(rowObjArr.length != 1){
		alert("请选择一条要编辑的信息!");				
		return;
	}
	$("#edit-dlg").dialog('setTitle','编辑资讯');
	$("#edit-dlg").dialog('open');
	editDialogInit(rowObjArr[0]);
}
//编辑对话框保存操作
function doEdit(){
	if (checkBeforeEditSubmit()) {
		$("#edit_ff").submit();
	}
}
//编辑对话框取消
function closeEdit(){
	$("#edit-dlg").dialog("close");
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

//验证
function checkBeforeAddSubmit(){
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
//编辑验证
function checkBeforeEditSubmit(){
	var _name = $('#edit_name').val();
	var _title = $('#edit_title').val();
	var _source = $('#edit_source').val();
	var _detail = $('#edit_detail').val();
	if(_name === ""){
		$("#edit_nameIsNull").html("<font color='red'>标题不能为空!</font>");
		return false;
	}else{
		$("#edit_nameIsNull").html("");
	}
	if(_title === ""){
		$("#edit_titleIsNull").html("<font color='red'>摘要不能为空!</font>");
		return false;
	}else{
		$("#edit_titleIsNull").html("");
	}
	if(_source === ""){
		$("#edit_sourceIsNull").html("<font color='red'>出处不能为空!</font>");
		return false;
	}else{
		$("#edit_sourceIsNull").html("");
	}
	if(_detail === ""){
		$("#edit_detailIsNull").html("<font color='red'>详情不能为空!</font>");
		return false;
	}else{
		$("#edit_detailIsNull").html("");
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
	$('#edit_id').val(row.id);
	$('#edit_image').val(row.imageAddress);
	$('#edit_name').textbox('setValue', row.name);
	$('#edit_title').textbox('setValue', row.title);
	$('#edit_imageUrl').attr('src', '../../' + row.imageAddress);
	$('#edit_source').textbox('setValue', row.source);
	$('#edit_detail').textbox('setValue', row.detail);
	$("#edit_nameIsNull").html("");
	$("#edit_titleIsNull").html("");
	$("#edit_sourceIsNull").html("");
	$("#edit_detailIsNull").html("");
}