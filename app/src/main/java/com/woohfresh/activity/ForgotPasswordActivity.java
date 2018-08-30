package com.woohfresh.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.BuildConfig;
import com.woohfresh.R;
import com.woohfresh.data.local.Datas;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.POauth;
import com.woohfresh.models.api.RGlobal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends AppCompatActivity implements Validator.ValidationListener {

    App.LoadingPrimary pd;

    Context mContext;

    public static String TAG = "";

    Validator validator;

    @NotEmpty
    @Email
    @BindView(R.id.etForgotEmail)
    EditText mEmail;

    @OnClick(R.id.btnForgot)
    public void ocForgot(){
        validator.validate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        validator = new Validator(this);
        validator.setValidationListener(this);
        ButterKnife.bind(this);
        pd = new App.LoadingPrimary(mContext);
    }

    @Override
    public void onValidationSucceeded() {
        pd.show();
        AndroidNetworking.post(Apis.BASE_URL_API+"forgot/password")
                .addBodyParameter("email", mEmail.getText().toString())
                .build()
                .getAsObject(POauth.class, new ParsedRequestListener<POauth>() {
                    @Override
                    public void onResponse(POauth user) {
                        pd.dismiss();
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
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
