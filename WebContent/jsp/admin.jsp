<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Administrator Home</title>
</head>
<body>
	<div>
		<h1>Administrator Home</h1>
		<h3>
			<font color="red">${message}</font>
		</h3>
		<table>
			<tbody>
				<tr>
					<td><a href="/user/userlist.do">Users List</a></td>
				</tr>
				<tr>
					<td><a
						href="/user/signOutDeal.do?accessToken=${user.accessToken}">Sign
							Out</a></td>
				</tr>
			</tbody>
		</table>
	</div>
</body>
</html>