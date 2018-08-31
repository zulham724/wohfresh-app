package com.woohfresh.fragments;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.BuildConfig;
import com.woohfresh.R;
import com.woohfresh.data.local.Datas;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.POauth;
import com.woohfresh.models.api.PSignUp;
import com.woohfresh.models.api.RGlobal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUp extends Fragment implements Validator.ValidationListener{

    App.LoadingPrimary pd;

    Context mContext;

    Validator validator;

    public static String TAG = "";

    @BindView(R.id.tvSignupTermPrivacy)
    TextView mTermPrivacy;

    @NotEmpty
    @Email
    @BindView(R.id.etSignupEmail)
    EditText mEmail;

    @NotEmpty
    @Password
    @BindView(R.id.etSignupPassword)
    EditText mPass;

    @NotEmpty
    @Password
    @BindView(R.id.etSignupPasswordRe)
    EditText mPassRe;

    @OnClick(R.id.btnSignup)
    public void ocSignUp(){
        validator.validate();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isAdded()) {
            TAG = getActivity().getClass().getSimpleName();
        }
        pd = new App.LoadingPrimary(getActivity());
        mContext = getActivity();
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_signup, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this,view);
        mTermPrivacy.setText(Html.fromHtml(getString(R.string.signup_term)));
        mTermPrivacy.setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
    }

    public void dialogSuccess(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.my_dialog_success_register);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(android.R.drawable.alert_dark_frame));

        Button mOk = (Button) dialog.findViewById(R.id.btnOk);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    @Override
    public void onValidationSucceeded() {
        pd.show();
        String email = mEmail.getText().toString();
        int index = email.indexOf('@');
        String name = email.substring(0,index);
        AndroidNetworking.post(Apis.BASE_URL_API+"register")
                .addBodyParameter("name", name)
                .addBodyParameter("email", mEmail.getText().toString())
                .addBodyParameter("password", mPassRe.getText().toString())
                .build()
                .getAsObject(PSignUp.class, new ParsedRequestListener<PSignUp>() {
                    @Override
                    public void onResponse(PSignUp user) {
                        pd.dismiss();
                        Prefs.putString(Datas.USER_ROLE_ID, String.valueOf(user.getRoleId()));
                        Prefs.putString(Datas.USER_NAME, String.valueOf(user.getName()));
                        Prefs.putString(Datas.USER_EMAIL,user.getEmail());
                        Prefs.putString(Datas.USER_ID, String.valueOf(user.getId()));
                        Prefs.putString(Datas.IS_LOGIN,"1");
                        dialogSuccess();
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

}
