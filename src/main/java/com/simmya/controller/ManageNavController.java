package com.simmya.controller;





import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value= "/manage/nav")
public class ManageNavController {
	

	@RequestMapping(value= "/{jspName}", method = RequestMethod.GET)
	public String home(@PathVariable("jspName") String jspName) {
		return jspName;
	}
}
