$(function(){
	//加载树形导航
	$("#nav").tree({
		url:"/json/nav_data.json",//加载树状json数据
		animate:true,//设置动画效果
		onClick:function(node){
					if(node.attributes && node.attributes.url){//有url的窗口才能打开
						if($("#tabs").tabs("exists",node.text)){ //如果窗口已经打开
							$("#tabs").tabs("select",node.text);
						}else{	//如果窗口还没有打开
							var content="<iframe frameborder='0' scrolling='auto' style='width:100%;height:100%' src="+node.attributes.url+"></iframe>";
							$('#tabs').tabs('add',{
								title: node.text,
								closable:true,
								content:content
							});
						}
					}
				}
	});
	//打开首页窗口,设置不能关闭
	$('#tabs').tabs({
	    border:false,
	 	fit:true
	});
});