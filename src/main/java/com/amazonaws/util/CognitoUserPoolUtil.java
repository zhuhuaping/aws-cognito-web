package com.amazonaws.util;

import static com.amazonaws.util.CommonUtil.getProperty;
import static com.amazonaws.util.CommonUtil.getSecretHash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.common.CheckUtils;
import com.amazonaws.model.User;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthRequest;
import com.amazonaws.services.cognitoidp.model.AdminInitiateAuthResult;
import com.amazonaws.services.cognitoidp.model.AttributeType;
import com.amazonaws.services.cognitoidp.model.AuthFlowType;
import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.amazonaws.services.cognitoidp.model.ChangePasswordRequest;
import com.amazonaws.services.cognitoidp.model.ChangePasswordResult;
import com.amazonaws.services.cognitoidp.model.CodeDeliveryDetailsType;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpRequest;
import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import com.amazonaws.services.cognitoidp.model.DeleteUserRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordRequest;
import com.amazonaws.services.cognitoidp.model.ForgotPasswordResult;
import com.amazonaws.services.cognitoidp.model.GetUserRequest;
import com.amazonaws.services.cognitoidp.model.GetUserResult;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutRequest;
import com.amazonaws.services.cognitoidp.model.GlobalSignOutResult;
import com.amazonaws.services.cognitoidp.model.SignUpRequest;
import com.amazonaws.services.cognitoidp.model.SignUpResult;
import com.amazonaws.services.cognitoidp.model.UpdateUserAttributesRequest;

public class CognitoUserPoolUtil {

	static final AWSCredentials awsCredentials = new BasicAWSCredentials(getProperty("access_key"),
			getProperty("secret_key"));
	static final AWSCognitoIdentityProviderClient cognitoClient = new AWSCognitoIdentityProviderClient(awsCredentials);

	static final String USER_POOL_ID = getProperty("userpool_id");

	static final String APP_NAME = getProperty("app_name");

	static final String APP_CLIENT_ID = getProperty("app_client_id");
	static final String APP_CLIENT_SECRET = getProperty("app_client_secret");

	static {
		cognitoClient.setRegion(Region.getRegion(Regions.AP_NORTHEAST_1));
	}

	public static void signUp(User user) {

		SignUpRequest signUpRequest = new SignUpRequest();

		signUpRequest.setClientId(APP_CLIENT_ID);
		signUpRequest.setSecretHash(getSecretHash(user.getUserName(), APP_CLIENT_ID, APP_CLIENT_SECRET));

		signUpRequest.setUsername(user.getUserName());
		signUpRequest.setPassword(user.getPassword());

		List<AttributeType> attributeTypeList = new ArrayList<>();
		attributeTypeList.add(new AttributeType().withName("email").withValue(user.getEmail()));
		attributeTypeList.add(new AttributeType().withName("custom:role").withValue(user.getRole()));
		signUpRequest.setUserAttributes(attributeTypeList);

		SignUpResult signUpResult = cognitoClient.signUp(signUpRequest);
		System.out.println(signUpResult.toString());
	}

	public static void verifyEmail(String userId) {

		ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest();
		confirmSignUpRequest.setConfirmationCode("538625");
		confirmSignUpRequest.setClientId(APP_CLIENT_ID);
		confirmSignUpRequest.setSecretHash(getSecretHash(userId, APP_CLIENT_ID, APP_CLIENT_SECRET));
		confirmSignUpRequest.setUsername(userId);
		ConfirmSignUpResult confirmSignUpResult = cognitoClient.confirmSignUp(confirmSignUpRequest);

		System.out.println(confirmSignUpResult);
	}

	public static void login(User user) throws Exception {

		AdminInitiateAuthRequest adminInitiateAuthRequest = new AdminInitiateAuthRequest();
		adminInitiateAuthRequest.setAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH);
		adminInitiateAuthRequest.setClientId(APP_CLIENT_ID);
		adminInitiateAuthRequest.setUserPoolId(USER_POOL_ID);

		Map<String, String> authParamMap = new HashMap<>();
		authParamMap.put("USERNAME", user.getUserName());
		authParamMap.put("PASSWORD", user.getPassword());
		authParamMap.put("SECRET_HASH", getSecretHash(user.getUserName(), APP_CLIENT_ID, APP_CLIENT_SECRET));

		adminInitiateAuthRequest.setAuthParameters(authParamMap);

		AdminInitiateAuthResult adminInitiateAuthResult = cognitoClient.adminInitiateAuth(adminInitiateAuthRequest);
		if (!CheckUtils.isNull(adminInitiateAuthResult.getChallengeName())) {
			user.setChallengeName(adminInitiateAuthResult.getChallengeName());
			throw new Exception("The user status is " + adminInitiateAuthResult.getChallengeName()
					+ "!Please do something to change the user status or contact the administrator.");
		}

		AuthenticationResultType authenticationResultType = adminInitiateAuthResult.getAuthenticationResult();
		if (!CheckUtils.isNull(authenticationResultType)) {
			user.setIdToken(authenticationResultType.getIdToken());
			user.setAccessToken(authenticationResultType.getAccessToken());
			user.setRefreshToken(authenticationResultType.getRefreshToken());
			user.setTokenType(authenticationResultType.getTokenType());
			user.setExpiresIn(authenticationResultType.getExpiresIn());

			System.out.println("Id_Token=" + authenticationResultType.getIdToken());
			System.out.println("Access_Token=" + authenticationResultType.getAccessToken());
			System.out.println("Refresh_Token=" + authenticationResultType.getRefreshToken());
			System.out.println("Token_Type=" + authenticationResultType.getTokenType());
			System.out.println("Expires_In=" + authenticationResultType.getExpiresIn());

			GetUserResult userResult = getUser(authenticationResultType.getAccessToken());
			for (AttributeType attrType : userResult.getUserAttributes()) {
				String name = attrType.getName();
				String value = attrType.getValue();

				if (name.equals("email")) {
					user.setEmail(value);
				} else if (name.equals("custom:role")) {
					user.setRole(value);
				} else {
					continue;
				}
			}
		}
	}

	public static void signOut(String accessToken) {
		GlobalSignOutRequest globalSignOutRequest = new GlobalSignOutRequest();
		globalSignOutRequest.setAccessToken(accessToken);
		GlobalSignOutResult globalSignOutResult = cognitoClient.globalSignOut(globalSignOutRequest);
		System.out.println(globalSignOutResult.toString());
	}

	public static GetUserResult getUser(String accessToken) {

		GetUserRequest getUserRequest = new GetUserRequest();
		getUserRequest.setAccessToken(accessToken);

		GetUserResult userResult = cognitoClient.getUser(getUserRequest);

		System.out.println("UserName " + userResult.getUsername());

		userResult.getUserAttributes().stream()
				.forEach(a -> System.out.println("Name: " + a.getName() + " ,Value : " + a.getValue()));

		return userResult;

	}

	public static void updateUserAttributes(String accessToken) {
		UpdateUserAttributesRequest updateUserAttributesRequest = new UpdateUserAttributesRequest();
		updateUserAttributesRequest.setAccessToken(accessToken);

		List<AttributeType> userAttributes = new ArrayList<>();
		userAttributes.add(new AttributeType().withName("gender").withValue("Male"));
		userAttributes.add(new AttributeType().withName("custom:role").withValue("admin"));
		userAttributes.add(new AttributeType().withName("custom:region").withValue("North Region"));
		userAttributes.add(new AttributeType().withName("custom:customer").withValue("Bob Medical Center"));
		userAttributes.add(new AttributeType().withName("custom:facility").withValue("Cardiac Facility"));
		userAttributes.add(new AttributeType().withName("custom:ipaddress").withValue("127.0.0.1"));

		updateUserAttributesRequest.setUserAttributes(userAttributes);
		cognitoClient.updateUserAttributes(updateUserAttributesRequest);

		/*
		 * AdminUpdateUserAttributesRequest adminUpdateUserAttributesRequest =
		 * new AdminUpdateUserAttributesRequest();
		 * adminUpdateUserAttributesRequest.setUsername("demoUser");
		 * adminUpdateUserAttributesRequest.setUserPoolId(USER_POOL_ID);
		 * adminUpdateUserAttributesRequest.setUserAttributes(userAttributes);
		 * cognitoClient.adminUpdateUserAttributes(
		 * adminUpdateUserAttributesRequest);
		 */
	}

	public static void forgotPassword(String userId) {
		ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest();
		forgotPasswordRequest.setClientId(APP_CLIENT_ID);
		forgotPasswordRequest.setSecretHash(getSecretHash(userId, APP_CLIENT_ID, APP_CLIENT_SECRET));
		forgotPasswordRequest.setUsername(userId);
		ForgotPasswordResult forgotPasswordResult = cognitoClient.forgotPassword(forgotPasswordRequest);

		CodeDeliveryDetailsType codeDeliveryDetails = forgotPasswordResult.getCodeDeliveryDetails();

		System.out.println(" " + codeDeliveryDetails.getAttributeName());
		System.out.println(" " + codeDeliveryDetails.getDeliveryMedium());
		System.out.println(" " + codeDeliveryDetails.getDestination());
	}

	public static void confirmForgotPassword(String userId, String password) {
		ConfirmForgotPasswordRequest confirmForgotPasswordRequest = new ConfirmForgotPasswordRequest();
		confirmForgotPasswordRequest.setClientId(APP_CLIENT_ID);
		confirmForgotPasswordRequest.setSecretHash(getSecretHash(userId, APP_CLIENT_ID, APP_CLIENT_SECRET));
		confirmForgotPasswordRequest.setConfirmationCode("856797");
		confirmForgotPasswordRequest.setUsername(userId);
		confirmForgotPasswordRequest.setPassword(password);
		ConfirmForgotPasswordResult confirmForgotPasswordResult = cognitoClient
				.confirmForgotPassword(confirmForgotPasswordRequest);
		System.out.println(confirmForgotPasswordResult.toString());
	}

	public static void changePassword(String accessToken, String oldPassword, String newPassword) {
		ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
		changePasswordRequest.setAccessToken(accessToken);
		changePasswordRequest.setPreviousPassword(oldPassword);
		changePasswordRequest.setProposedPassword(newPassword);
		ChangePasswordResult changePasswordResult = cognitoClient.changePassword(changePasswordRequest);
		System.out.println("" + changePasswordResult.toString());
	}

	public static void deleteUser(String accessToken) {
		DeleteUserRequest deleteUserRequest = new DeleteUserRequest();
		deleteUserRequest.setAccessToken(accessToken);
		cognitoClient.deleteUser(deleteUserRequest);
	}

	public static void renewAccessTokenUsingRefreshToken(String userId, String refreshToken) {
		AdminInitiateAuthRequest adminInitiateAuthRequest = new AdminInitiateAuthRequest();
		adminInitiateAuthRequest.setAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH);
		adminInitiateAuthRequest.setClientId(APP_CLIENT_ID);
		adminInitiateAuthRequest.setUserPoolId(USER_POOL_ID);

		HashMap<String, String> authParamMap = new HashMap<>();
		authParamMap.put("USERNAME", userId);
		// authParamMap.put("PASSWORD", "password");
		authParamMap.put("REFRESH_TOKEN", refreshToken);
		authParamMap.put("SECRET_HASH", getSecretHash(userId, APP_CLIENT_ID, APP_CLIENT_SECRET));

		adminInitiateAuthRequest.setAuthParameters(authParamMap);

		AdminInitiateAuthResult adminInitiateAuthResult = cognitoClient.adminInitiateAuth(adminInitiateAuthRequest);
		AuthenticationResultType authenticationResultType = adminInitiateAuthResult.getAuthenticationResult();
		System.out.println("id_token=" + authenticationResultType.getIdToken());
		System.out.println("access_token=" + authenticationResultType.getAccessToken());
		System.out.println("refresh_token=" + authenticationResultType.getRefreshToken());
		// System.out.println("Expires in " +
		// authenticationResultType.getExpiresIn());
	}

}
