$(function(){
		//添加对话框的学年初始化
		$('#schoolyearcodeId').combobox({    
		    url:'/dd/withoutplease/学年.do',  
		    valueField:'code',textField:'name',width:120,panelWidth:100,panelHeight:63
		});
		//添加对话框的学期初始化
		$('#semestercodeId').combobox({
			url:'/dd/withoutplease/学期.do', 
			valueField:'code',textField:'name',width:120,panelWidth:100,panelHeight:42
		});
		//添加对话框的考试类型初始化
		$('#examtypecodeId').combobox({
			url:'/dd/withoutplease/考试类型.do', 
			valueField:'code',textField:'name',width:120,panelWidth:100,panelHeight:84
		});
		//考试名称离焦事件
		$("input[name=name]").blur(function(){
			checkName();	
		});
	});
	
	//搜索
	function doSearch(){
		var fromDate=$("#fromDateId").datebox('getValue');
		var toDate=$("#toDateId").datebox('getValue');
		if(fromDate>toDate){
			alert("请重新选择日期范围!");
			return;
		}
		 $("#dg").datagrid('load',{
			 fromDate: fromDate,
			 toDate: toDate
		}); 
	}
		
		
		
		//打开添加对话框
		function openAdd(){
			$("#dlg").dialog('setTitle','添加考试');
			$("#dlg").dialog('open');
			dialogInit();
		}
		//编辑操作
		function doEdit(){
			var rowObjArr=$("#dg").datagrid('getSelections');
			if(rowObjArr.length==0){
				alert("请选择要升班的班级!");				
				return;
			}
			var idsArr=[];
			for(var i=0;i<rowObjArr.length;i++){
				idsArr.push(rowObjArr[i].id);
			}
			var ids=idsArr.join(",");
			alert(ids);
		}
		//删除操作
		function doDelete(){
			var rowObjArr=$("#dg").datagrid('getSelections');
			if(rowObjArr.length==0){
				alert("请选择要删除的班级!");				
				return;
			}
			var idsArr=[];
			for(var i=0;i<rowObjArr.length;i++){
				idsArr.push(rowObjArr[i].id);
			}
			var ids=idsArr.join(",");
			$.post("/exam/delete.do",{ids:ids},function(backData){
				if(backData.status==200){
					$.messager.alert('提示','成功删除'+backData.data+'个班级!','info',
							function(){
								$("#schoolyearcodeId").combobox('setValue',0);//设置搜索框为请选择
								$("#dg").datagrid('load',{schoolyearcode:0});//加载全部数据
							});
				}else{
					$.messager.alert('提示','删除班级失败','warning',
							function(){});
				}
			});
		}
		
		//对话框保存操作
		function doSave(){
			var params=$("#ff").serialize();
			//ajax提交数据,添加成功关闭对话框,刷新数据
			if(checkName()){
				$.post("/exam/add.do",params,
					function(backData){
						if(backData.status==200){
							$.messager.alert('提示','添加考试成功','info',
									function(){
										$("#dlg").dialog('close');//关闭添加对话框
										$("#fromDateId").datebox('setValue',null);//设置日期选择为空
										$("#toDateId").datebox('setValue',null);
										$("#dg").datagrid('load',{//加载全部数据
											 fromDate: "",
											 toDate: ""
										});
									});
						}else{
							$.messager.alert('提示','添加班级失败','warning',
									function(){$("#dlg").dialog('close');});
						}
					});
			}
		}
		//对话框取消
		function doClose(){
			$("#dlg").dialog("close");
		}
		
		//验证考试名称不能为空
		function checkName(){
			if($("input[name=name]").val()==""){
				$("#nameIsNull").html("<font color='red'>考试名称不能为空!</font>");
				return false;
			}else{
				$("#nameIsNull").html("");
				return true;
			}
		}
		//对话框恢复初始状体
		function dialogInit(){
			var today=new Date();
			var year=today.getFullYear();
			var month=today.getMonth()+1;
			var day=today.getDate();
			$('#examdateId').datebox('setValue', year+"-"+month+"-"+day);	
			$("input[name=name]").val("");
			$("#nameIsNull").html("");
		}