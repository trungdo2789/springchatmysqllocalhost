package com.dttr.tchat.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dttr.tchat.dao.UserinfoDAO;
import com.dttr.tchat.entities.Userinfo;
import com.dttr.tchat.service.JwtService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BaseController {

	@Autowired
	JwtService jwtService;

	@Autowired
	UserinfoDAO userService;

	@RequestMapping("/login")
	public String getLogin(@RequestParam(value = "error", required = false) final String error,
			@RequestParam(value = "logout", required = false) final String loguot, final Model model) {
		if (error != null) {
			model.addAttribute("message", "Login Failed!");
		}
		if (loguot != null) {
			model.addAttribute("message", "Logout succes!");
		}
		return "login";
	}

	@RequestMapping("/admin")
	public String getAdmin() {
		return "admin";
	}

	@GetMapping(value = "/register")
	public String getRegister() {
		return "register";
	}

	@PostMapping(value = "/register")
	public String register(HttpServletRequest request, Model model) {
		Userinfo user = new Userinfo();
		user.setUsername(request.getParameter("username"));
		user.setPassword(request.getParameter("password"));
		if (userService.add(user)) {
			model.addAttribute("message", "Created account. Please login!");
			return "login";
		} else
			model.addAttribute("message", "Username exist!");
		return "register";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}

	@RequestMapping("/")
	public String getHome(Principal principal) {
		if (principal == null)
			return "redirect:login";
		return "index";
	}

	@ResponseBody
	@RequestMapping(value = "/token", method = RequestMethod.GET)
	public ResponseEntity<String> getToken(Principal principal) {
		String result = "";
		String username = "";
		HttpStatus httpStatus = null;
		if (principal != null) {
			username = principal.getName();
			result = jwtService.generateTokenLogin(username);
			httpStatus = HttpStatus.OK;
		} else {
			result = "Unauthorized";
			httpStatus = HttpStatus.BAD_REQUEST;
		}

		return new ResponseEntity<String>(result, httpStatus);
	}

	
}
