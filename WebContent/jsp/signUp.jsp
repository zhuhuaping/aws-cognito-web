<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sign Up</title>
</head>
<body>
	<div>
		<h1>Sign Up</h1>
		<h3>
			<font color="red">${message}</font>
		</h3>
		<form action="/user/signUpDeal.do" method="post">
			<table>
				<tbody>
					<tr>
						<th><label>User name:</label></th>
						<td><input type="text" name="userName"
							placeholder="User name" required /></td>
					</tr>
					<tr>
						<th><label>Email:</label></th>
						<td><input type="email" name="email" placeholder="Email"
							required /></td>
					</tr>
					<tr>
						<th><label>Role:</label></th>
						<td><input type="radio" name="role" value="ADMINISTRATOR"
							checked />Administrator</td>
						<td><input type="radio" name="role" value="CONSUMER" />Consumer</td>
					</tr>
					<tr>
						<th><label>Password:</label></th>
						<td><input type="password" name="password"
							placeholder="Password" required /></td>
					</tr>
					<tr>
						<th><label>Confirm your password:</label></th>
						<td><input type="password"
							placeholder="Confirm your password" required /></td>
					</tr>
					<tr>
						<td><button>Submit</button></td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
</body>
</html>