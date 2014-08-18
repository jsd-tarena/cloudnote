package com.tarena.cloudnote.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tarena.cloudnote.entity.Response;
import com.tarena.cloudnote.entity.User;
import com.tarena.cloudnote.service.UserService;
import com.tarena.cloudnote.util.UUIDUtil;

@Controller
@RequestMapping("/user")
public class UserController {

	@Resource(name = "userService")
	private UserService userService;

	/**
	 * 用户登陆验证
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public Response login(HttpServletRequest request,
			HttpServletResponse response) {
		Response message = new Response();
		message.setStatus(-2);
		message.setMessage("用户名或密码错误");
		String header = request.getHeader("Authorization");
		User user = new User();
		try {
			if (StringUtils.isNotEmpty(header)) {
				Pattern p = Pattern.compile("[0-9a-zA-Z=]+");
				Matcher m = p.matcher(header);
				List<String> list = new ArrayList<String>();
				while (m.find()) {
					list.add(m.group());
				}
				if (list.size() == 2) {
					String[] decode = new String(Base64.decodeBase64(list
							.get(1).getBytes("utf-8"))).split(":");
					if (decode.length == 2) {
						user = userService.validateUserAndPwd(decode[0],
								decode[1]);
						if (user != null) {
							message.setStatus(1);
							message.setResource(user);
							message.setMessage("用户验证成功！");
						}
					}
					String tk = UUIDUtil.getUId();
					request.getSession().setAttribute("userToken", tk);
					response.setHeader("Authorization", "token " + tk + ":"
							+ System.currentTimeMillis());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("用户登录失败！");
			message.setStatus(-1);
		}
		return message;
	}

	@RequestMapping(value = "/regist", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public Response regist(HttpServletRequest request,
			HttpServletResponse response) {
		Response message = new Response();
		String header = request.getHeader("Authorization");
		User rigistUser = null;
		try {
			Pattern p = Pattern.compile("[0-9a-zA-Z]+");
			Matcher m = p.matcher(header);
			List<String> list = new ArrayList<String>();
			if (m.find()) {
				list.add(m.group());
			}
			String[] decodes = new String(Base64.decodeBase64(list.get(1)
					.getBytes("utf-8"))).split(":");
			rigistUser = userService.validateUserAndPwd(decodes[0], decodes[1]);
			if (rigistUser != null) {
				rigistUser.setCnUserName(decodes[0]);
				rigistUser.setCnUserPassword(decodes[1]);
			}
			if (rigistUser == null) {
				userService.createUser(rigistUser);
				message.setStatus(1);
				message.setResource(rigistUser);
				message.setMessage("用户注册成功！");
			} else {
				message.setMessage("用户注册失败！");
				message.setStatus(-3);
			}
		} catch (Exception e) {
			e.printStackTrace();
			message.setMessage("用户注册失败！");
			message.setStatus(-3);
		}
		return message;
	}
}
