<%@page contentType="text/html;charset=utf-8" pageEncoding="Utf-8" %>
<!DOCTYPE html>
<html>
<head>
<script type="text/javascript" src="scripts/jquery.min.js">
</script>
<script type="text/javascript" src="scripts/base64.js">
</script>
<script type="text/javascript" src="scripts/cookie_util.js">
</script>
<script type="text/javascript" src="scripts/login/login.js">
</script>
<script type="text/javascript" src="scripts/jQuery.md5.js">
</script>
<script type="text/javascript" src="scripts/login/login_callback.js">
</script>

</head>
	<body>
		<div class="global">
			<div class="log log_in" tabindex='-1' id='dl'>
				<dl>
					<dt>
						<div class='header'>
							<h3>登&nbsp;录</h3>
						</div>
					</dt>
					<dt></dt>
					<dt>
						<div class='letter'>
							用户名:&nbsp;<input type="text" name="cnUserName" id="account" tabindex='1'/>
						</div>
					</dt>
					<dt>
						<div class='letter'>
							密&nbsp;&nbsp;&nbsp;码:&nbsp;<input type="password" name="cnUserPassword" id="password" tabindex='2'/>
						</div>
					</dt>
					<dt>
						<div>
							<input type="button" name="" id="login" value='&nbsp登&nbsp录&nbsp' tabindex='3'/>
							<input type="button" name="" id="sig_in" value='&nbsp注&nbsp册&nbsp' tabindex='4'/>
						</div>
					</dt>
				</dl>
			</div>
			
		</div>
	</body>
</html>