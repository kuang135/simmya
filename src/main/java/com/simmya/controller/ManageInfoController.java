package com.simmya.controller;

import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.simmya.easyui.AjaxResult;
import com.simmya.easyui.DataGrid;
import com.simmya.pojo.Info;
import com.simmya.service.impl.InfoService;
import com.simmya.util.FilePathUtil;

@Controller
@RequestMapping(value= "/manage/info")
public class ManageInfoController {
	
	
	@Autowired
	private InfoService infoService;
	
	@RequestMapping(value= "/list", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid info(@RequestParam(value="page")int page,
			@RequestParam(value="rows")int rows,
			@RequestParam(value="name",required = false)String name) {
		Info info = new Info();
		if (StringUtils.isNotBlank(name)) {
			info.setName(name);
		}
		return infoService.getDataGrid(page, rows, info);
	}
	
	@RequestMapping(value= "/add.do", method = RequestMethod.POST)
	public String infoAdd(HttpServletRequest request,
			@RequestParam("file") MultipartFile multipartFile) {
		try {
			String originalFilename = multipartFile.getOriginalFilename();
			String suffix = StringUtils.substringAfter(originalFilename, ".");
			String uuid = UUID.randomUUID().toString().replace("-", "");
			String realPath = request.getSession().getServletContext().getRealPath("/pic/info");
			String dirPath = realPath + FilePathUtil.createPath();
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String name = dirPath + File.separator + uuid + "." + suffix;
			File file = new File(name);
			multipartFile.transferTo(file);
			Info info = new Info();
			info.setName(request.getParameter("name"));
			info.setTitle(request.getParameter("title"));
			info.setSource(request.getParameter("source"));
			info.setImageAddress("pic" + File.separator + "info" + FilePathUtil.createPath() + File.separator + uuid + "." + suffix);
			info.setDetail(request.getParameter("detail"));
			infoService.save(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "info";
	}
	
	/*
	 * 判断哪些info不能删除
	 * 图片文件要删除
	 */
	@RequestMapping(value= "/delete.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult infoDelete(@RequestParam("ids") String ids, HttpServletRequest request) {
		int count;
		AjaxResult ajaxResult;
		String realPath = request.getSession().getServletContext().getRealPath("/");
		try {
			String[] idsArr = ids.split(",");
			count = infoService.deleteByIds(idsArr, realPath);
			ajaxResult = new AjaxResult(200, "成功删除" + count + "条资讯。");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResult = new AjaxResult(400, "有box正在使用某条info，删除失败。");
		}
		return ajaxResult;
	}

}
