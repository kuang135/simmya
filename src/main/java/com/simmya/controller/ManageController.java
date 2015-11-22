package com.simmya.controller;



import java.io.File;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.simmya.easyui.AjaxResult;
import com.simmya.easyui.DataGrid;
import com.simmya.pojo.Info;
import com.simmya.service.impl.BoxService;
import com.simmya.service.impl.InfoService;
import com.simmya.util.FilePathUtil;


@Controller
@RequestMapping(value= "/manage")
public class ManageController {
	
	@Autowired
	private BoxService boxService;
	@Autowired
	private InfoService infoService;

	@RequestMapping(value= "/nav/{jspName}", method = RequestMethod.GET)
	public String home(@PathVariable("jspName") String jspName) {
		return jspName;
	}
	
	@RequestMapping(value= "/box", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid box(@RequestParam(value="page")int page,
			@RequestParam(value="rows")int rows) {
		return boxService.getDataGrid(page, rows);
	}
	
	@RequestMapping(value= "/info", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid info(@RequestParam(value="page")int page,
			@RequestParam(value="rows")int rows) {
		return infoService.getDataGrid(page, rows);
	}
	
	@RequestMapping(value= "/info/add.do", method = RequestMethod.POST)
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
			info.setImageAddress("pic" + File.separator + "head" + FilePathUtil.createPath() + File.separator + uuid + "." + suffix);
			info.setDetail(request.getParameter("detail"));
			infoService.save(info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "info";
	}
	
	@RequestMapping(value= "/info/delete.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult infoDelete(@RequestParam("ids") String ids) {
		System.out.println(ids);
		AjaxResult ajaxResult = new AjaxResult(200, "删除资讯成功");
		return ajaxResult;
	}
}
