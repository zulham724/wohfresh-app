package com.woohfresh.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
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
import com.woohfresh.activity.MyProfileActivity;
import com.woohfresh.data.local.Datas;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.POauth;
import com.woohfresh.models.api.RGlobal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignIn extends Fragment implements Validator.ValidationListener,
        GoogleApiClient.OnConnectionFailedListener{

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
    public void ocGoogle(){
        signInGoogle();
    }

    @OnClick(R.id.btnLogin)
    public void ocLogin(){
        validator.validate();
    }

    @OnClick(R.id.tvForgotPassword)
    public void ocFP(){
        startActivity(new Intent(getActivity(), ForgotPasswordActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isAdded()) {
            TAG = getActivity().getClass().getSimpleName();
        }
        mContext = getActivity();
        validator = new Validator(this);
        validator.setValidationListener(this);
        pd = new App.LoadingPrimary(getActivity());
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
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        if(BuildConfig.DEBUG){
            return inflater.inflate(R.layout.fragment_signin_dev, parent, false);
        }else {
            return inflater.inflate(R.layout.fragment_signin, parent, false);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        mAskSignup.setText(Html.fromHtml(getString(R.string.login_no_acc)));
    }

    @Override
    public void onValidationSucceeded() {
        pd.show();
        AndroidNetworking.post(BuildConfig.SERVER_URL+"oauth/token")
                .addBodyParameter("grant_type", "password")
                .addBodyParameter("client_id", Prefs.getString(Datas.APP_CLIENT_ID,""))
                .addBodyParameter("client_secret", Prefs.getString(Datas.APP_CLIENT_SECRET,""))
                .addBodyParameter("username", mEmail.getText().toString())
                .addBodyParameter("password", mPass.getText().toString())
                .build()
                .getAsObject(POauth.class, new ParsedRequestListener<POauth>() {
                    @Override
                    public void onResponse(POauth user) {
                        pd.dismiss();
                        Prefs.putString(Datas.OAUTH_TOKEN_TYPE,user.getTokenType());
                        Prefs.putString(Datas.OAUTH_EXPIRES_IN, String.valueOf(user.getExpiresIn()));
                        Prefs.putString(Datas.OAUTH_ACCESS_TOKEN,user.getAccessToken());
                        Prefs.putString(Datas.OAUTH_REFRESH_TOKEN,user.getRefreshToken());
                        Prefs.putString(Datas.IS_LOGIN,"1");
                        App.TShort("success");
                        startActivity(new Intent(getActivity(), MyProfileActivity.class));
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
                            App.TShort(apiError.getError());
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
            Prefs.putString(Datas.APP_SOSMED_ID, idSosmed);
            Prefs.putString(Datas.APP_SOSMED_TOKEN, idToken);
            App.TShort("success :"+account.getDisplayName());
            startActivity(new Intent(getActivity(), MyProfileActivity.class));
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
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

}
