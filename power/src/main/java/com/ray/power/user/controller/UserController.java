package com.ray.power.user.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ray.power.base.DateEditor;
import com.ray.power.role.model.Role;
import com.ray.power.user.form.UserForm;
import com.ray.power.user.model.UserGridModelVO;
import com.ray.power.user.service.UserService;
import com.ray.power.util.GridDataModel;
import com.ray.power.util.ModelAndViewUtil;

@Controller
@RequestMapping("/user") // 注：系统唯一请求标示
public class UserController {

	private final String jspPath = "user";// JSP 包名称
	private final String tabCode = "user";// 表名标示

	private static Logger logger = LogManager.getLogger(UserController.class);

	@Resource(name = "userService")
	private UserService userService;

	/**
	 * 绑定controller日期转换处理
	 * 
	 * @param binder
	 * @throws Exception
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) throws Exception {
		binder.registerCustomEditor(Date.class, new DateEditor());
	}

	@RequestMapping("")
	public ModelAndView welcome(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Model model) {
		return index(request, response, session, model);
	}

	@RequestMapping("index")
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			Model model) {
		logger.info("...用户管理");
		model.addAttribute("tabCode", tabCode);
		return ModelAndViewUtil.Jsp(jspPath + "/index");
	}

	@RequestMapping(value = "queryforlist", method = RequestMethod.POST)
	public ModelAndView queryforlist(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			UserForm queryForm) {
		logger.info("queryforlist");
		logger.info(queryForm.getRoleid() +"#"+queryForm.getState()+"#"+queryForm.getLoginname());
		GridDataModel<UserGridModelVO> gdm = userService.query(queryForm);
		return ModelAndViewUtil.Json_ok(gdm, "userForm");
	}

	/**
	 * 跳转到添加或编辑页面
	 * @return
	 */
	@RequestMapping("toSaveOrEdit")
	public ModelAndView toSaveOrEdit (
			HttpSession session,
			HttpServletRequest request, 
			HttpServletResponse response, Model model,
			@RequestParam Integer id,
			@RequestParam String new_or_edit){
		User role = null;
		model.addAttribute("tabCode", tabCode);
		if("create".equalsIgnoreCase(new_or_edit)){
			model.addAttribute("new_or_edit", "create");
			model.addAttribute("action", "role/save");
		} else {
			role = userService.findById(id);
			model.addAttribute("new_or_edit", "edit");
			model.addAttribute("role", role);
			model.addAttribute("action","role/edit/"+role.getId());
		}
		return ModelAndViewUtil.Jsp( "role/saveOrEdit" );
	}
}