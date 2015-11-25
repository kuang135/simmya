package com.simmya.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
import com.simmya.pojo.Box;
import com.simmya.pojo.BoxInfoRef;
import com.simmya.pojo.Info;
import com.simmya.service.impl.BoxInfoRefService;
import com.simmya.service.impl.BoxService;
import com.simmya.service.impl.InfoService;
import com.simmya.util.FilePathUtil;

@Controller
@RequestMapping(value= "/manage/box")
public class ManageBoxController {

	@Autowired
	private InfoService infoService;
	@Autowired
	private BoxService boxService;
	@Autowired
	private BoxInfoRefService boxInfoService;
	
	@RequestMapping(value= "/list", method = RequestMethod.POST)
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
	
	@RequestMapping(value= "/add.do", method = RequestMethod.POST)
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
	
	@RequestMapping(value= "/delete.do", method = RequestMethod.POST)
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
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value= "/info", method = RequestMethod.POST)
	@ResponseBody
	public DataGrid boxInfos(@RequestParam(value="boxid")String boxid) {
		DataGrid dataGrid = infoService.getAllBoxDataGrid();
		List<Info> infos = (List<Info>) dataGrid.getRows();
		BoxInfoRef boxInfo = new BoxInfoRef();
		boxInfo.setBoxId(boxid);
		List<BoxInfoRef> boxInfos = boxInfoService.selectListByWhere(boxInfo);
		if (boxInfos != null && boxInfos.size() > 0 && infos != null && infos.size() > 0) {
			for (BoxInfoRef binf : boxInfos) {
				for (Info inf : infos) {
					if (inf.getId().equals(binf.getInfoId())) {
						inf.setSelected(true);
						break;
					} 
				}
			}
		}
		Collections.sort(infos, new Comparator<Info>(){
			@Override
			public int compare(Info o1, Info o2) {
				if (!o1.isSelected() && o2.isSelected()) {
					return 1;
				}
				if (o1.isSelected() && !o2.isSelected()) {
					return -1;
				}
				return 0;
			}});
		return dataGrid;
	}
	
	@RequestMapping(value= "/editInfo.do", method = RequestMethod.POST)
	@ResponseBody
	public AjaxResult editInfo(
			@RequestParam("boxid") String boxid,
			@RequestParam("infoids") String infoids) {
		AjaxResult ajaxResult;
		try {
			String[] infoIdsArr = infoids.split(",");
			boxInfoService.update(boxid, infoIdsArr);
			ajaxResult = new AjaxResult(200, "咨询修改成功。");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxResult = new AjaxResult(400, "咨询修改失败。");
		}
		return ajaxResult;
	}
}
