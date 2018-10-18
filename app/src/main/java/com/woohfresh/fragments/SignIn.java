package com.woohfresh.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.BuildConfig;
import com.woohfresh.R;
import com.woohfresh.activity.ForgotPasswordActivity;
import com.woohfresh.activity.MainActivity;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.GSecret;
import com.woohfresh.models.api.POauth;
import com.woohfresh.models.api.RGlobal;
import com.woohfresh.models.api.user.GUser;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

import static com.woohfresh.App.subMail;
import static com.woohfresh.data.local.Constants.IS_LANGUAGE;

//import com.crashlytics.android.Crashlytics;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignIn extends Fragment implements Validator.ValidationListener,
        GoogleApiClient.OnConnectionFailedListener {

    App.LoadingPrimary pd;

    Context mContext;
    Validator validator;

    private static final int RC_SIGN_IN = 007;
    public GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    public static String TAG = "";

    @BindView(R.id.tvNoAccount)
    TextView mAskSignup;

    @NotEmpty
    @Email
    @BindView(R.id.etLoginEmail)
    EditText mEmail;

    @NotEmpty
    @Password
    @BindView(R.id.etLoginPassword)
    EditText mPass;

    @OnClick(R.id.btnSosmedGoogle)
    public void ocGoogle() {
        signInGoogle();
    }

    @OnClick(R.id.btnLogin)
    public void ocLogin() {
        validator.validate();
    }

    @OnClick(R.id.tvForgotPassword)
    public void ocFP() {
        startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
//        Crashlytics.getInstance().crash(); // Force a crash
    }

    //    facebook
    @BindView(R.id.login_button)
    LoginButton mLoginButton;
    private CallbackManager mCallbackManager;

    @OnClick(R.id.btnSosmedFacebook)
    public void ocFB() {
        mLoginButton.performClick();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pd = new App.LoadingPrimary(getActivity());
        if (isAdded()) {
            TAG = getActivity().getClass().getSimpleName();
        }
        mContext = getActivity();
        validator = new Validator(this);
        validator.setValidationListener(this);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        if (Prefs.getString(IS_LANGUAGE, "").isEmpty()) {
            App.showDialogLang(getActivity());
        }

//        facebook init
        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_RAW_RESPONSES);
        mCallbackManager = CallbackManager.Factory.create();

        initSecret();
    }

    private void initSecret() {
        pd.show();
        App.mApiService.gSecret().enqueue(new Callback<GSecret>() {
            @Override
            public void onResponse(Call<GSecret> call, retrofit2.Response<GSecret> response) {
                pd.dismiss();
                if (response.isSuccessful()) {
                    Prefs.putString(Constants.APP_CLIENT_ID, String.valueOf(response.body().getId()));
                    Prefs.putString(Constants.APP_CLIENT_SECRET, response.body().getSecret());
                } else {
                    App.TShort(getString(R.string.err_server));
                    getActivity().finish();
                }

            }

            @Override
            public void onFailure(Call<GSecret> call, Throwable t) {
                pd.dismiss();
                App.TShort(t.getMessage());
            }
        });
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        if (BuildConfig.DEBUG) {
            return inflater.inflate(R.layout.fragment_signin_dev, parent, false);
        } else {
            return inflater.inflate(R.layout.fragment_signin, parent, false);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        if(!Prefs.getString(Constants.TEMP_EMAIL,"").equals("")) {
            mEmail.setText(Prefs.getString(Constants.TEMP_EMAIL, ""));
            mPass.setText(Prefs.getString(Constants.TEMP_PASS, ""));
        }
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        mAskSignup.setText(Html.fromHtml(getString(R.string.login_no_acc)));

//        facebook
        mLoginButton.setReadPermissions("email", "public_profile");
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
//                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                Toast.makeText(mContext, "Login facebook error", Toast.LENGTH_SHORT).show();
                // [END_EXCLUDE]
            }
        });

    }

    @Override
    public void onValidationSucceeded() {
        pd.show();
        AndroidNetworking.post(BuildConfig.SERVER_URL + "oauth/token")
                .addBodyParameter("grant_type", "password")
                .addBodyParameter("client_id", Prefs.getString(Constants.APP_CLIENT_ID, ""))
                .addBodyParameter("client_secret", Prefs.getString(Constants.APP_CLIENT_SECRET, ""))
                .addBodyParameter("username", mEmail.getText().toString())
                .addBodyParameter("password", mPass.getText().toString())
                .build()
                .getAsObject(POauth.class, new ParsedRequestListener<POauth>() {
                    @Override
                    public void onResponse(POauth user) {
                        pd.dismiss();
                        Prefs.putString(Constants.G_PASSWORD, mPass.getText().toString());
                        Prefs.putString(Constants.OAUTH_TOKEN_TYPE, user.getTokenType());
                        Prefs.putString(Constants.OAUTH_EXPIRES_IN, String.valueOf(user.getExpiresIn()));
                        Prefs.putString(Constants.OAUTH_ACCESS_TOKEN, user.getAccessToken());
                        Prefs.putString(Constants.OAUTH_REFRESH_TOKEN, user.getRefreshToken());
                        Prefs.putString(Constants.IS_LOGIN, "1");
                        App.TShort("success");
                        navSuccess();
//                        Prefs.putString(Constants.USER_NAME, subMail(mEmail.getText().toString()));
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        if (error.getErrorCode() != 0) {
                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                            // get parsed error object (If ApiError is your class)
                            RGlobal apiError = error.getErrorAsObject(RGlobal.class);
                            App.TShort(apiError.getMessage());
                        } else {
                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
                        }
                    }
                });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(mContext);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    //    Login Google
    private void signInGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        Log.d(TAG, "googleSign:" + result.getStatus().toString());
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            String idToken = account.getIdToken();
            String idSosmed = account.getId();
            Prefs.putString(Constants.APP_SOSMED_ID, idSosmed);
            Prefs.putString(Constants.APP_SOSMED_TOKEN, idToken);
            App.TShort("success :" + account.getDisplayName());
            navSuccess();
            Log.d("reqGoogle", idToken + "\n" + idSosmed);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(mContext, result.getStatus().getStatusMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

        // fb fragment
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    //login facebook
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        pd.show();
        // [END_EXCLUDE]
        if (token != null) {
            String idTokenFB = token.getToken();
            String idSosmedFB = token.getUserId();
            Prefs.putString(Constants.APP_SOSMED_ID,idSosmedFB);
            Prefs.putString(Constants.APP_SOSMED_TOKEN,idTokenFB);
            Log.e(TAG, "FB:token: " + token.getToken());
            Log.e(TAG, "FB:id: " + token.getUserId());
        }

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            App.TShort("Authentication failed");
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        pd.dismiss();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    private void updateUI(FirebaseUser user) {
        pd.dismiss();
        if (user != null) {
//            requestLoginSosmed(idSosmedFB, idTokenFB, "2");
            App.TShort("success");
            navSuccess();
        } else {
//            Toast.makeText(mContext, "Silahkan isi data anda untuk melanjutkan", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(), SignUpSosmed.class));
//            finish();
            App.TShort("success");
            navSuccess();
        }
    }

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        signOut();
    }

    public void navSuccess(){
        pd.show();
        AndroidNetworking.get(Apis.GET_USER)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(GUser.class, new ParsedRequestListener <GUser>() {
                    @Override
                    public void onResponse(GUser response) {
                        pd.dismiss();

                        Prefs.putString(Constants.G_USER_ID, String.valueOf(response.getId()));
                        Prefs.putString(Constants.G_ROLE_ID , String.valueOf(response.getRoleId()));
                        Prefs.putString(Constants.G_NAME, response.getName());
                        Prefs.putString(Constants.G_EMAIL , response.getEmail());
                        Prefs.putString(Constants.G_AVATAR , String.valueOf(response.getAvatar()));
                        Prefs.putString(Constants.G_FACEBOOK_ID , String.valueOf(response.getFacebookId()));
                        Prefs.putString(Constants.G_GOOGLE_ID , String.valueOf(response.getGoogleId()));
                        Prefs.putString(Constants.G_LAST_LOGIN , String.valueOf(response.getLastLogin()));
                        Prefs.putString(Constants.G_IS_ACTIVE , String.valueOf(response.getIsActive()));
                        Prefs.putString(Constants.G_CREATED_AT , String.valueOf(response.getCreatedAt()));
                        Prefs.putString(Constants.G_UPDATED_AT , String.valueOf(response.getUpdatedAt()));

                        Intent i = new Intent(getActivity(), MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        getActivity().finish();
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        // handle error
                        App.TShort(error.getErrorDetail());
                    }
                });
    }

}
