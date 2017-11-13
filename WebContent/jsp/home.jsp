<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Consumer Home</title>
</head>
<body>
	<div>
		<h1>Consumer Home</h1>
		<h3>
			<font color="red">${message}</font>
		</h3>
		<form action="/user/changePasswordDeal.do" method="post">
			<input type="hidden" name="accessToken" value="${user.accessToken}" />
			<table>
				<thead>
					<tr>
						<th>Change Your Password</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<th>Old Password:</th>
						<td><input type="text" name="oldPassword"
							placeholder="Old Password" required /></td>
					</tr>
					<tr>
						<th>New Password:</th>
						<td><input type="text" name="newPassword"
							placeholder="New Password" required /></td>
					</tr>
				</tbody>
				<tfoot>
					<tr>
						<td><button>Submit</button></td>
					</tr>
				</tfoot>
			</table>
		</form>
		<div>
			<a href="/user/signOutDeal.do?accessToken=${user.accessToken}">Sign
				Out</a>
		</div>
	</div>
</body>
</html>