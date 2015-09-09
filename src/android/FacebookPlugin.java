package com.telerik.wazzurb;
 
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
 
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
 
import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import java.util.Arrays;
 
public class InvitePlugin extends CordovaPlugin {
 
    private String previewImagePath = "http://wzzrb.nl/appinvite/1.png"; // 1-2-3-4.png
    private String AppDownloadUrl = "https://fb.me/400072263520020";
    private Activity activity;
 
    public static CallbackManager callbackmanager;
 
    // init plugin
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        activity = cordova.getActivity();
        FacebookSdk.sdkInitialize(activity.getApplicationContext());
    }
 
    // execute actions
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
 
        // Invite action
        if (action.equals("invite")) {
            Log.d("Action:", "INVITE TRY");
            invite();
            return true;
        }
 
        // Login action
        if (action.equals("login")) {
            if (isLoggedIn()) {
                Log.d("Action:", "LOGOUT TRY");
                //logOut();
                return true;
            } else {
                Log.d("Action:", "LOGIN TRY");
                login();
                return true;
            }
        }
        return false;
    }
 
    // Simple active session check
    private boolean isLoggedIn() {
        Log.d("USER:","is logged in");
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }
 
    private void invite() {
        if (AppInviteDialog.canShow()) {
            System.out.println("invite launched");
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(AppDownloadUrl)
                    .setPreviewImageUrl(previewImagePath)
                    .build();
            AppInviteDialog.show(activity, content);
        }
    }
 
    private void login() {
        callbackmanager = CallbackManager.Factory.create();
 
        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(cordova.getActivity(), Arrays.asList("email", "public_profile"));
 
        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
 
                        System.out.println("Success");
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                            // handle error
                                            System.out.println("ERROR");
                                        } else {
                                            System.out.println("Success");
                                            try {
 
                                                String jsonResult = String.valueOf(json);
                                                System.out.println("JSON Result" + jsonResult);
 
                                                // strings for usage in app
                                                //String str_email = json.getString("email");
                                                String str_id = json.getString("id");
                                                String str_firstname = json.getString("first_name");
                                                String str_lastname = json.getString("last_name");
 
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
 
                                }).executeAsync();
 
                    }
 
                    // basic error/cancel handling
                    @Override
                    public void onCancel() {
                        Log.d("TAG_CANCEL", "On cancel");
                    }
 
                    @Override
                    public void onError(FacebookException error) {
                        Log.d("TAG_ERROR", error.toString());
                    }
                });
    }
 
    // TODO: Logout function
    public void logOut(){};
 
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackmanager.onActivityResult(requestCode, resultCode, data);
    }
 
}