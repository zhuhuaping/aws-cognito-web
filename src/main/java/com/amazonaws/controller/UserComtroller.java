package com.amazonaws.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.amazonaws.common.CheckUtils;
import com.amazonaws.common.Consts;
import com.amazonaws.common.Enum.Role;
import com.amazonaws.common.Enum.SignOut_Rst;
import com.amazonaws.common.Enum.SignUp_Rst;
import com.amazonaws.model.User;
import com.amazonaws.service.UserService;

@Controller
public class UserComtroller {

	@Autowired
	private UserService userService;

	@RequestMapping("/user/login")
	public String login() {
		return "login";
	}

	@RequestMapping("/user/loginDeal")
	public ModelAndView loginDeal(HttpSession session, User user) {

		Map<Object, Object> loginRst = userService.login(user);
		String msg = (String) loginRst.get(Consts.MSG);
		session.setAttribute(Consts.MSG, msg);

		ModelAndView mav = new ModelAndView();

		if (!CheckUtils.isNull(user.getAccessToken())) {

			if (Role.ADMINISTRATOR.name().equals(user.getRole())) {
				mav = new ModelAndView("redirect:/admin.do");
			} else if (Role.CONSUMER.name().equals(user.getRole())) {
				mav = new ModelAndView("redirect:/home.do");
			}

			session.setAttribute(Consts.USER, user);
		} else {
			mav = new ModelAndView("redirect:/user/login.do");
		}

		return mav;
	}

	@RequestMapping("/user/signUp")
	public String signUp() {
		return "signUp";
	}

	@RequestMapping("/user/signUpDeal")
	public ModelAndView signUpDeal(HttpSession session, User user) {
		Map<Object, Object> checkRst = userService.signUp(user);
		Object signUpRst = checkRst.get(Consts.REG_RST);
		String msg = (String) checkRst.get(Consts.MSG);
		session.setAttribute(Consts.MSG, msg);

		ModelAndView mav = new ModelAndView();

		if (signUpRst.equals(SignUp_Rst.SUCCESS)) {
			mav = new ModelAndView("redirect:/user/login.do");
		} else if (signUpRst.equals(SignUp_Rst.FAIL)) {
			mav = new ModelAndView("redirect:/user/signUp.do");
		}

		return mav;
	}

	@RequestMapping("/user/signOutDeal")
	public ModelAndView signOutDeal(HttpSession session, String accessToken) {
		Map<Object, Object> checkRst = userService.signOut(accessToken);
		Object signOutRst = checkRst.get(Consts.REG_RST);
		String msg = (String) checkRst.get(Consts.MSG);
		session.setAttribute(Consts.MSG, msg);

		ModelAndView mav = new ModelAndView();

		if (signOutRst.equals(SignOut_Rst.SUCCESS)) {
			mav = new ModelAndView("redirect:/user/login.do");
		} else if (signOutRst.equals(SignOut_Rst.FAIL)) {
			mav = new ModelAndView("error");
		}

		return mav;
	}

	@RequestMapping("/user/changePasswordDeal")
	public ModelAndView changePasswordDeal(HttpSession session, String accessToken, String oldPassword,
			String newPassword) {
		Map<Object, Object> checkRst = userService.changePassword(accessToken, oldPassword, newPassword);
		Object signOutRst = checkRst.get(Consts.REG_RST);
		String msg = (String) checkRst.get(Consts.MSG);
		session.setAttribute(Consts.MSG, msg);

		ModelAndView mav = new ModelAndView();

		if (signOutRst.equals(SignOut_Rst.SUCCESS)) {
			mav = new ModelAndView("redirect:/user/login.do");
		} else if (signOutRst.equals(SignOut_Rst.FAIL)) {
			mav = new ModelAndView("error");
		}

		return mav;
	}

}
