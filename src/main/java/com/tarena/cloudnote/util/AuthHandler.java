package com.tarena.cloudnote.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class AuthHandler extends HandlerInterceptorAdapter {

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object arg2, Exception arg3)
			throws Exception {
	}

	/**
	 * 设置服务端token判断结果..
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object object, ModelAndView mv)
			throws Exception {
	}

	/**
	 * 验证用户请求的token值是否合法
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object object) throws Exception {
		// 获得用户请求头

		// "Basic DGFSRED%$E%%R^&TDGGFDRES"
		// "DGFSRED%$E%%R^&TDGGFDRES"
		// "username:token"
		String header = request.getHeader("Authorization");
		if (StringUtils.isNotEmpty(header)) {
			Pattern p = Pattern.compile("[0-9a-zA-Z=]+");
			Matcher m = p.matcher(header);
			List<String> list = new ArrayList<String>();
			while (m.find()) {
				list.add(m.group());
			}
			if (list.size() == 2) {
				String con[] = new String(Base64.decodeBase64(list.get(1)
						.getBytes("utf-8"))).split(":");

				if (con.length == 2) {
					String token = (String) request.getSession().getAttribute(
							"userToken");
					// 获取用户session中存放的token值
					if (StringUtils.isNotEmpty(token)) {
						// 两个值相等则表示认证通过
						if (con[1].equals(token)
								&& Integer.parseInt(con[2]) + 1800 < System
										.currentTimeMillis()) {
							return true;
						}
					}
				}
			}
		}

		response.setStatus(401);
		String result = "{'status':-1,'message':'用户token与服务端不一致！'}";
		response.getWriter().write(result);
		return false;
	}
}
