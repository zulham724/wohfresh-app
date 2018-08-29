package com.woohfresh.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.BuildConfig;
import com.woohfresh.R;
import com.woohfresh.data.local.Datas;
import com.woohfresh.models.api.POauth;
import com.woohfresh.models.api.RGlobal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignIn extends Fragment {

    App.LoadingPrimary pd;

    public static String TAG = "";

    @BindView(R.id.tvNoAccount)
    TextView mAskSignup;

    @BindView(R.id.etLoginEmail)
    EditText mEmail;

    @BindView(R.id.etLoginPassword)
    EditText mPass;

    @OnClick(R.id.btnLogin)
    public void ocLogin(){
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isAdded()) {
            TAG = getActivity().getClass().getSimpleName();
        }
        pd = new App.LoadingPrimary(getActivity());
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

}
