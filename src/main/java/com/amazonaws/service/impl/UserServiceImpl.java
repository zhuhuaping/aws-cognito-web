package com.amazonaws.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.common.Consts;
import com.amazonaws.common.Enum.SignOut_Rst;
import com.amazonaws.common.Enum.SignUp_Rst;
import com.amazonaws.model.User;
import com.amazonaws.service.UserService;
import com.amazonaws.util.CognitoUserPoolUtil;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public Map<Object, Object> login(User user) {

		Map<Object, Object> checkRst = new HashMap<Object, Object>();

		try {
			CognitoUserPoolUtil.login(user);

			checkRst.put(Consts.MSG, "Login Successed!");
		} catch (AmazonServiceException e) {
			checkRst.put(Consts.MSG, e.getErrorMessage());
		} catch (Exception e) {
			checkRst.put(Consts.MSG, e.getMessage());
		}

		return checkRst;
	}

	@Override
	public Map<Object, Object> signUp(User user) {

		Map<Object, Object> checkRst = new HashMap<Object, Object>();

		try {
			CognitoUserPoolUtil.signUp(user);

			checkRst.put(Consts.REG_RST, SignUp_Rst.SUCCESS);
			checkRst.put(Consts.MSG, "Sign Up Successed!");
		} catch (AmazonServiceException e) {
			checkRst.put(Consts.REG_RST, SignUp_Rst.FAIL);
			checkRst.put(Consts.MSG, e.getErrorMessage());
		}

		return checkRst;
	}

	@Override
	public Map<Object, Object> signOut(String accessToken) {

		Map<Object, Object> checkRst = new HashMap<Object, Object>();

		try {
			CognitoUserPoolUtil.signOut(accessToken);

			checkRst.put(Consts.REG_RST, SignOut_Rst.SUCCESS);
			checkRst.put(Consts.MSG, "Sign Out Successed!");
		} catch (AmazonServiceException e) {
			checkRst.put(Consts.REG_RST, SignOut_Rst.FAIL);
			checkRst.put(Consts.MSG, e.getErrorMessage());
		}

		return checkRst;
	}

	@Override
	public Map<Object, Object> changePassword(String accessToken, String oldPassword, String newPassword) {

		Map<Object, Object> checkRst = new HashMap<Object, Object>();

		try {
			CognitoUserPoolUtil.changePassword(accessToken, oldPassword, newPassword);

			checkRst.put(Consts.REG_RST, SignOut_Rst.SUCCESS);
			checkRst.put(Consts.MSG, "Change Password Successed!");
		} catch (AmazonServiceException e) {
			checkRst.put(Consts.REG_RST, SignOut_Rst.FAIL);
			checkRst.put(Consts.MSG, e.getErrorMessage());
		}

		return checkRst;
	}

}
