package com.simmya.controller;



import java.io.File;
import java.math.BigDecimal;
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
import com.simmya.pojo.Box;
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
			@RequestParam(value="rows")int rows,
			@RequestParam(value="name",required = false)String name) {
		Box box = new Box();
		if (StringUtils.isNoneBlank(name)) {
			box.setName(name);
		}
		return boxService.getDataGrid(page, rows, box);
	}
	
	@RequestMapping(value= "/info", method = RequestMethod.POST)
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
	@RequestMapping(value= "/info/delete.do", method = RequestMethod.POST)
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
			ajaxResult = new AjaxResult(400, "有box正在使用某条info，删除失败。");
		}
		return ajaxResult;
	}
	
	@RequestMapping(value= "/box/add.do", method = RequestMethod.POST)
	public String boxAdd(HttpServletRequest request,
			@RequestParam("file") MultipartFile multipartFile) {
		try {
			String originalFilename = multipartFile.getOriginalFilename();
			String suffix = StringUtils.substringAfter(originalFilename, ".");
			String uuid = UUID.randomUUID().toString().replace("-", "");
			String realPath = request.getSession().getServletContext().getRealPath("/pic/box");
			String dirPath = realPath + FilePathUtil.createPath();
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String name = dirPath + File.separator + uuid + "." + suffix;
			File file = new File(name);
			multipartFile.transferTo(file);
			Box box = new Box();
			box.setName(request.getParameter("name"));
			box.setTitle(request.getParameter("title"));
			String boxPrice = request.getParameter("boxPrice");
			box.setBoxPrice(new BigDecimal(boxPrice));
			box.setImageAddress("pic" + File.separator + "box" + FilePathUtil.createPath() + File.separator + uuid + "." + suffix);
			box.setDetail(request.getParameter("detail"));
			boxService.save(box);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "box";
	}
	
	@RequestMapping(value= "/box/delete.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult boxDelete(@RequestParam("ids") String ids, HttpServletRequest request) {
		int count;
		AjaxResult ajaxResult;
		String realPath = request.getSession().getServletContext().getRealPath("/");
		try {
			String[] idsArr = ids.split(",");
			count = boxService.deleteByIds(idsArr, realPath);
			ajaxResult = new AjaxResult(200, "成功删除" + count + "条资讯。");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResult = new AjaxResult(400, "有order正在使用某条box，删除失败。");
		}
		return ajaxResult;
	}
	
}
