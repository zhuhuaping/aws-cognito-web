package com.amazonaws.service;

import java.util.Map;

import com.amazonaws.model.User;

public interface UserService {

	public Map<Object, Object> login(User user);

	public Map<Object, Object> signUp(User user);

	public Map<Object, Object> signOut(String accessToken);

	public Map<Object, Object> changePassword(String accessToken, String oldPassword, String newPassword);

}
