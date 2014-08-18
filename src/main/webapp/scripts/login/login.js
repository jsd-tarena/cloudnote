$(function() {
	// 登录
	$("#login").click(
			function() {
				var loginUserName = $("#account").val();
				var loginPassword = $.md5($("#password").val());
				var token = "";
				var userId = "";
				// 请求   
				jQuery.support.cors = true;
				$.ajax({
					type : "post",
					url : "http://localhost:8080/cloudnote/user/login",
					dataType : "json",
					beforeSend : function(xhr) {
						xhr
								.setRequestHeader('Content-Type',
										'application/json');
						// xhr.setRequestHeader('Authorization',
						// loginUserName+":"+loginPassword);
						xhr.setRequestHeader('Authorization', "Basic "
								+ Base64.encode(loginUserName + ":"
										+ loginPassword));
					},
					success : function(result) {
						// token 存cookie，2小时有效时间
						if (result.resource != null) {
							userId = result.resource.cnUserId;
						} else {
							alert(result.message);
							return;
						}

						if ($.isFunction(loginSuccess)) {
							loginSuccess(result);
						}
					},
					error : loginError,
					complete : function(xhr, TS) {
//						alert("complete:" + JSON.stringify(TS));
						// "Token "+UUID
						// base64(userName:UUID) --> cookie
						token = xhr.getResponseHeader("Authorization");
						var uuid = token.split(" ");
						if (uuid[1] != null) {
							token = "Basic "
									+ Base64.encode(loginUserName + ":"
											+ uuid[1]);
						}
						addCookie(userId + "_token", token, 2);
					}
				});
			});

	// 注册
	$("#regist_button").click(function() {
		// 允许跨域访问
		jQuery.support.cors = true;
		var username = $("#regist_username").val();
		var password = $("#regist_password").val();

		var user = {
			"cnUserName" : username,
			"cnUserPassword" : password
		};

		$.ajax({
			type : "post",
			url : "http://localhost:8080/cloudnote_login/" + "user/createUser",
			data : JSON.stringify(user),
			dataType : "json",
			contentType : "application/json",
			beforeSend : function(xhr) {
				xhr.setRequestHeader('Content-Type', 'application/json');
				// xhr.setRequestHeader('Authorization', token);
				return checkUserName(username);
			},
			success : function(result) {
				if ($.isFunction(registSuccess)) {
					registSuccess(result);
				}
			},
			error : registError
		});

	});

	// 登出
	$("#logout").click(function() {
		logout();
	});

});

/*******************************************************************************
 * 判断用户名是否重复
 * 
 * @param username
 * @returns {Boolean}
 */
function checkUserName(username) {
	var b = false;
	$.ajax({// 提交之前验证用户名是否重复
		type : "post",
		async : false,
		url : "http://localhost:8080/cloudnote_login/" + "user/checkUserName/"
				+ username,
		dataType : "json",
		contentType : "application/json",
		beforeSend : function(xhr) {
			xhr.setRequestHeader('Content-Type', 'application/json');
		},
		success : function(result) {
			if (result.status == -2) {
				get('warning_1').style.display = 'block';
				b = false;
			} else {
				b = true;
			}
		},
		error : function() {
			b = false;
		}
	});
	return b;
};

/**
 * 退出登录
 */
function logout() {
	var userId = getCookie("tarena_cloud_note_user");
	delCookie("tarena_cloud_note_user");// 删除用户cookie
	delCookie(userId + "_token");// 删除token
	location.href = "log_in.html";
}

/**
 * 修改密码时验证老密码是否正确
 * 
 * @param oldPwd
 * @returns {Boolean}
 */
function validOldPwd(oldPwd) {
	var flog = false;
	var loginUserName = getCookie(UserName);

	jQuery.support.cors = true;
	$.ajax({
		type : "post",
		async : false,
		url : "http://localhost:8080/cloudnote_login/"
				+ "user/checkUserOldpwd/" + loginUserName + "/" + oldPwd,
		dataType : "json",
		beforeSend : function(xhr) {
			xhr.setRequestHeader('Content-Type', 'application/json');
		},
		success : function(result) {
			if (result.status == 1) {
				flog = true;
			}
		}
	});
	return flog;
}

/**
 * 修改密码
 * 
 * @param changepwdSuccess
 * @param changepwdError
 */
function changepwd(changepwdSuccess, changepwdError) {
	var loginUserId = getCookie(cookie_key);
	var pwd = $("#new_password").val();
	$.ajax({
		type : "post",
		url : "http://localhost:8080/cloudnote_login/" + "user/resetPwd/"
				+ loginUserId + "/" + pwd,
		dataType : "json",
		contentType : "application/json",
		beforeSend : function(xhr) {
			var token = getCookie(loginUserId + "_token");
			xhr.setRequestHeader('Content-Type', 'application/json');
			xhr.setRequestHeader('Authorization', token);
		},
		success : function(result) {
			changepwdSuccess(result);
		},
		error : function() {
			changepwdError();
		}
	});
}
