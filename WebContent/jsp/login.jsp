<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
</head>
<body>
	<div>
		<h1>Login</h1>
		<h3>
			<font color="red">${message}</font>
		</h3>
		<form action="/user/loginDeal.do" method="post">
			<table>
				<tbody>
					<tr>
						<th><label>User name:</label></th>
						<td><input type="text" name="userName"
							placeholder="User name" required /></td>
					</tr>
					<tr>
						<th><label>Password:</label></th>
						<td><input type="text" name="Password"
							placeholder="Password" required /></td>
					</tr>
					<tr>
						<td><button>Login</button></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>