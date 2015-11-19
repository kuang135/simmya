package com.simmya.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.simmya.service.impl.DiscussService;


@Controller
public class DiscussController {

	@Autowired
	private DiscussService discussService;
	
	@RequestMapping(value= "/infos/discuss/list", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listDiscussByInfoid(@RequestParam(value = "infoid", required = true)String infoid) throws SQLException {
		return discussService.listDiscussByInfoid(infoid);
	}
	
	
}
