package com.woohfresh.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.pixplicity.easyprefs.library.Prefs;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.adapters.MyAdapterSpinnerRegion;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.RGlobal;
import com.woohfresh.models.api.region.cityShow.GCityShow;
import com.woohfresh.models.api.region.wstate.GwState;
import com.woohfresh.models.api.user.GUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.woohfresh.data.sources.remote.api.Apis.URL_STORAGE;

public class MyProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener,
        Validator.ValidationListener {

    App.LoadingPrimary pd;

    Context mContext;
    Validator validator;

    public static String TAG = MyProfileActivity.class.getClass().getSimpleName();

    private DatePickerDialog dpd;
    @BindView(R.id.etProfileDob)
    EditText etDob;

    @BindView(R.id.profileSpState)
    Spinner spState;
    @BindView(R.id.profileSpCity)
    Spinner spCity;
    @BindView(R.id.profileSpSubdistrict)
    Spinner spSub;
    MyAdapterSpinnerRegion mySpinnerAdapterState;
    MyAdapterSpinnerRegion mySpinnerAdapterCity;
    MyAdapterSpinnerRegion mySpinnerAdapterSub;

    @BindView(R.id.ivProfile)
    ImageView ivPhotos;
    @NotEmpty
    @BindView(R.id.tvProfileName)
    EditText tvProfileName;
    @NotEmpty
    @Email
    @BindView(R.id.tvProfileEmail)
    EditText tvProfileEmail;
    @NotEmpty
    @Password(scheme = Password.Scheme.ANY)
    @BindView(R.id.etProfilePassword)
    EditText mPass;
    @NotEmpty
    @ConfirmPassword(messageResId = R.string.err_signin_password)
    @BindView(R.id.etProfileRePassword)
    EditText mPassRe;
    @NotEmpty
    @BindView(R.id.etProfilePhone)
    EditText etProfilePhone;
    @NotEmpty
    @BindView(R.id.etProfileAddress)
    EditText etProfileAddress;

    @BindView(R.id.etStateId)
    EditText etStateId;
    @BindView(R.id.etCityId)
    EditText etCityId;
    @BindView(R.id.etSubId)
    EditText etSubId;

    @OnClick(R.id.fab_profile)
    public void ocFab() {
        askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PICK_IMAGE);
    }

    public static final int PICK_IMAGE = 100;
    Bitmap bitmap;
    Uri selectedImage;
    String pathAvatar = "";
    RequestOptions requestOptions = new RequestOptions();
    String selectedState, selectedStateId, selectedCity, selectedCityId, selectedSub, selectedSubId;
    ArrayList<String> listState;
    ArrayList<String> listStateId;
    ArrayList<String> listCity;
    ArrayList<String> listCityId;
    ArrayList<String> listSub;
    ArrayList<String> listSubId;

    @OnClick(R.id.btnSave)
    public void ocSave() {
        if (etStateId.getText().toString().equals("")) {
            App.TShort("Choose a state");
        } else if (etCityId.getText().toString().equals("")) {
            App.TShort("Choose a city");
        } else if (etSubId.getText().toString().equals("")) {
            App.TShort("Choose a subdistrict");
        } else {
            validator.validate();
        }
    }

    @BindView(R.id.profileSpStateStatic)
    Spinner spStateStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        validator = new Validator(this);
        validator.setValidationListener(this);
        ButterKnife.bind(this);
        pd = new App.LoadingPrimary(mContext);
        requestOptions
                .centerCrop()
                .placeholder(R.drawable.profile_blank)
                .error(R.drawable.err_img_port)
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        if (!Prefs.getString(Constants.G_PASSWORD, "").equals("")) {
            mPass.setText(Prefs.getString(Constants.G_PASSWORD, ""));
            mPassRe.setText(Prefs.getString(Constants.G_PASSWORD, ""));
        }

        initSpinnerUI();
        initSpinnerDefault();
        initSpinnerState();

        pd.show();
        AndroidNetworking.get(Apis.GET_USER)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObject(GUser.class, new ParsedRequestListener<GUser>() {
                    @Override
                    public void onResponse(GUser response) {
                        pd.dismiss();

                        Prefs.putString(Constants.G_USER_ID, String.valueOf(response.getId()));
                        Prefs.putString(Constants.G_BIO_ID, String.valueOf(response.getBiodata().getId()));
                        Prefs.putString(Constants.G_ROLE_ID, String.valueOf(response.getRoleId()));
                        Prefs.putString(Constants.G_NAME, response.getName());
                        tvProfileName.setText(response.getName());
                        Prefs.putString(Constants.G_EMAIL, response.getEmail());
                        tvProfileEmail.setText(response.getEmail());
                        Prefs.putString(Constants.G_AVATAR, String.valueOf(response.getAvatar()));
                        if (null != response.getAvatar()) {
                            Glide.with(getApplicationContext())
                                    .load(URL_STORAGE + response.getAvatar())
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(ivPhotos);
                        }
                        Prefs.putString(Constants.G_FACEBOOK_ID, String.valueOf(response.getFacebookId()));
                        Prefs.putString(Constants.G_GOOGLE_ID, String.valueOf(response.getGoogleId()));
                        Prefs.putString(Constants.G_LAST_LOGIN, String.valueOf(response.getLastLogin()));
                        Prefs.putString(Constants.G_IS_ACTIVE, String.valueOf(response.getIsActive()));
                        Prefs.putString(Constants.G_CREATED_AT, String.valueOf(response.getCreatedAt()));
                        Prefs.putString(Constants.G_UPDATED_AT, String.valueOf(response.getUpdatedAt()));
                        if (null != response.getBiodata()) {
                            if (null != response.getBiodata().getPhoneNumber()) {
                                etProfilePhone.setText(String.valueOf(response.getBiodata().getPhoneNumber()));
                            }
                            if (null != response.getBiodata().getAddress()) {
                                etProfileAddress.setText(response.getBiodata().getAddress());
                            }
                            if (null != String.valueOf(response.getBiodata().getStateId())) {
                                etStateId.setText(String.valueOf(response.getBiodata().getStateId()));
                            }
                            if (null != String.valueOf(response.getBiodata().getCityId())) {
                                etCityId.setText(String.valueOf(response.getBiodata().getCityId()));
                            }
                            if (null != String.valueOf(response.getBiodata().getSubdistrictId())) {
                                etSubId.setText(String.valueOf(response.getBiodata().getSubdistrictId()));
                            }
//                            if(null != String.valueOf(response.getBiodata().getStateId())){
//                                Log.wtf("stateId",String.valueOf(response.getBiodata().getStateId()));
//                                Log.wtf("cityId",String.valueOf(response.getBiodata().getCityId()));
//                                Log.wtf("subId",String.valueOf(response.getBiodata().getSubdistrictId()));
//                            spStateStatic.setVisibility(View.GONE);
//                            spState.setVisibility(View.VISIBLE);
//
//                            }
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        // handle error
                        App.TShort(error.getErrorDetail());
                    }
                });

        Glide.with(App.getAppContext())
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.profile_blank)
                .apply(RequestOptions.circleCropTransform()).into(ivPhotos);

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                /*
                It is recommended to always create a new instance whenever you need to show a Dialog.
                The sample app is reusing them because it is useful when looking for regressions
                during testing
                 */
                if (dpd == null) {
                    dpd = DatePickerDialog.newInstance(
                            MyProfileActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                } else {
                    dpd.initialize(
                            MyProfileActivity.this,
                            now.get(Calendar.YEAR),
                            now.get(Calendar.MONTH),
                            now.get(Calendar.DAY_OF_MONTH)
                    );
                }
                dpd.setTitle(getString(R.string.profile_dob));
                dpd.dismissOnPause(true);
                dpd.vibrate(true);
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        ivPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PICK_IMAGE);
            }
        });

        spStateStatic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                spStateStatic.setVisibility(View.GONE);
                spState.setVisibility(View.VISIBLE);
                return false;
            }
        });

        spState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != spState.getSelectedView().findViewById(R.id.tvRegionId)) {
                    selectedStateId = ((TextView) (spState.getSelectedView().findViewById(R.id.tvRegionId))).getText().toString();
                    selectedState = ((TextView) (spState.getSelectedView().findViewById(R.id.tvRegionName))).getText().toString();
                    if (listCity.size() > 0) {
                        listCity.clear();
                        listCityId.clear();
                        initSpinnerCity(selectedStateId);
                    } else {
                        initSpinnerCity(selectedStateId);
                    }
                }

                etStateId.setText(selectedStateId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != spCity.getSelectedView().findViewById(R.id.tvRegionId)) {
                    selectedCityId = ((TextView) (spCity.getSelectedView().findViewById(R.id.tvRegionId))).getText().toString();
                    selectedCity = ((TextView) (spCity.getSelectedView().findViewById(R.id.tvRegionName))).getText().toString();
                    if (listSub.size() > 0) {
                        listSub.clear();
                        listSubId.clear();
                        initSpinnerSub(selectedCityId);
                    } else {
                        initSpinnerSub(selectedCityId);
                    }
                }
                etCityId.setText(selectedCityId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (null != spSub.getSelectedView().findViewById(R.id.tvRegionId)) {
                    selectedSubId = ((TextView) (spSub.getSelectedView().findViewById(R.id.tvRegionId))).getText().toString();
                    selectedSub = ((TextView) (spSub.getSelectedView().findViewById(R.id.tvRegionName))).getText().toString();
                }
                etSubId.setText(selectedSubId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initSpinnerDefault() {

        listCity.add(0, getString(R.string.profile_prompt_city));
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.my_spinner_simple, listCity);
        cityAdapter.setDropDownViewResource(R.layout.my_spinner_simple);
        spCity.setAdapter(cityAdapter);

        listSub.add(0, getString(R.string.profile_prompt_subdistrict));
        ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(this, R.layout.my_spinner_simple, listSub);
        subAdapter.setDropDownViewResource(R.layout.my_spinner_simple);
        spSub.setAdapter(subAdapter);
    }

    private void initSpinnerUI() {
        listState = new ArrayList<String>();
        listStateId = new ArrayList<String>();
        listCity = new ArrayList<String>();
        listCityId = new ArrayList<String>();
        listSub = new ArrayList<String>();
        listSubId = new ArrayList<String>();
    }

//    private void initSpinnerState() {
//        AndroidNetworking.get(Apis.URL_REGION_STATE)
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsObject(GState.class, new ParsedRequestListener<GState>() {
//                    @Override
//                    public void onResponse(GState response) {
//                        List<SemuaprovinsiItem> provinsi = response.getSemuaprovinsi();
//
//                        for (int i = 0; i < provinsi.size(); i++) {
//                            listState.add(provinsi.get(i).getNama());
//                            listStateId.add(provinsi.get(i).getId());
//                        }
//
//                        String[] saName = new String[listState.size()];
//                        saName = listState.toArray(saName);
//
//                        String[] saId = new String[listStateId.size()];
//                        saId = listStateId.toArray(saId);
//
//                        mySpinnerAdapterState = new MyAdapterSpinnerRegion
//                                (getApplicationContext(), saId, saName);
//                        spState.setAdapter(mySpinnerAdapterState);
//                        mySpinnerAdapterState.setNotifyOnChange(true);
//
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                        if (error.getErrorCode() != 0) {
//                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
//                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                            // get parsed error object (If ApiError is your class)
//                            RGlobal apiError = error.getErrorAsObject(RGlobal.class);
//                            App.TShort(apiError.getError());
//                        } else {
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//    }

    private void initSpinnerState() {
        AndroidNetworking.get(Apis.URL_STATE)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GwState.class, new ParsedRequestListener<List<GwState>>() {
                    @Override
                    public void onResponse(List<GwState> provinsi) {

                        for (int i = 0; i < provinsi.size(); i++) {
                            listState.add(provinsi.get(i).getName());
                            listStateId.add(String.valueOf(provinsi.get(i).getId()));
                        }

                        String[] saName = new String[listState.size()];
                        saName = listState.toArray(saName);

                        String[] saId = new String[listStateId.size()];
                        saId = listStateId.toArray(saId);

                        mySpinnerAdapterState = new MyAdapterSpinnerRegion
                                (getApplicationContext(), saId, saName);
                        spState.setAdapter(mySpinnerAdapterState);
                        mySpinnerAdapterState.setNotifyOnChange(true);

                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
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

    private void initSpinnerCity(String idProvinsi) {
        pd.show();
        AndroidNetworking.get(Apis.URL_CITY + idProvinsi)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GwState.class, new ParsedRequestListener<List<GwState>>() {
                    @Override
                    public void onResponse(List<GwState> city) {

                        pd.dismiss();
                        listCity.clear();
                        listCityId.clear();
                        for (int i = 0; i < city.size(); i++) {
                            listCity.add(city.get(i).getName());
                            listCityId.add(String.valueOf(city.get(i).getId()));
                        }

                        String[] saName = new String[listCity.size()];
                        saName = listCity.toArray(saName);

                        String[] saId = new String[listCityId.size()];
                        saId = listCityId.toArray(saId);

                        mySpinnerAdapterCity = new MyAdapterSpinnerRegion
                                (getApplicationContext(), saId, saName);
                        spCity.setAdapter(mySpinnerAdapterCity);
                        mySpinnerAdapterCity.setNotifyOnChange(true);
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        // handle error
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

    private void initSpinnerSub(String idCity) {
        pd.show();
        AndroidNetworking.get(Apis.REGION_CITY_SHOW + idCity)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(GCityShow.class, new ParsedRequestListener<GCityShow>() {
                    @Override
                    public void onResponse(GCityShow sub) {

                        pd.dismiss();
                        listSub.clear();
                        listSubId.clear();
                        for (int i = 0; i < sub.getSubdistricts().size(); i++) {
                            listSub.add(String.valueOf(sub.getSubdistricts().get(i).getName()));
                            listSubId.add(String.valueOf(sub.getSubdistricts().get(i).getId()));
                        }

                        String[] saName = new String[listSub.size()];
                        saName = listSub.toArray(saName);

                        String[] saId = new String[listSubId.size()];
                        saId = listSubId.toArray(saId);

                        mySpinnerAdapterSub = new MyAdapterSpinnerRegion
                                (getApplicationContext(), saId, saName);
                        spSub.setAdapter(mySpinnerAdapterSub);
                        mySpinnerAdapterSub.setNotifyOnChange(true);
                    }

                    @Override
                    public void onError(ANError error) {
                        pd.dismiss();
                        // handle error
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

//    private void initSpinnerCity(String idProvinsi) {
//        pd.show();
//        AndroidNetworking.get(Apis.URL_REGION_STATE + "/" + idProvinsi + "/kabupaten")
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsObject(GCity.class, new ParsedRequestListener<GCity>() {
//                    @Override
//                    public void onResponse(GCity response) {
//                        pd.dismiss();
//                        List<DaftarKecamatanItem> kecamatan = response.getDaftarKecamatan();
//                        if (null != kecamatan) {
//                            listCity.clear();
//                            listCityId.clear();
//                            for (int i = 0; i < kecamatan.size(); i++) {
//                                listCity.add(kecamatan.get(i).getNama());
//                                listCityId.add(kecamatan.get(i).getId());
//                            }
//
//                            String[] saName = new String[listCity.size()];
//                            saName = listCity.toArray(saName);
//
//                            String[] saId = new String[listCityId.size()];
//                            saId = listCityId.toArray(saId);
//
//                            mySpinnerAdapterCity = new MyAdapterSpinnerRegion
//                                    (getApplicationContext(), saId, saName);
//                            spCity.setAdapter(mySpinnerAdapterCity);
//                            mySpinnerAdapterCity.setNotifyOnChange(true);
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        pd.dismiss();
//                        // handle error
//                        if (error.getErrorCode() != 0) {
//                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
//                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                            // get parsed error object (If ApiError is your class)
//                            RGlobal apiError = error.getErrorAsObject(RGlobal.class);
//                            App.TShort(apiError.getError());
//                        } else {
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//    }

//    private void initSpinnerSub(String idKab) {
//        pd.show();
//        AndroidNetworking.get(Apis.URL_REGION_STATE + "/kabupaten/" + idKab + "/kecamatan")
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsObject(GSubdistrict.class, new ParsedRequestListener<GSubdistrict>() {
//                    @Override
//                    public void onResponse(GSubdistrict response) {
//                        pd.dismiss();
//                        List<com.woohfresh.models.api.region.subdistrict.DaftarKecamatanItem> kecamatan = response.getDaftarKecamatan();
//
//                        listSub.clear();
//                        listSubId.clear();
//                        for (int i = 0; i < kecamatan.size(); i++) {
//                            listSub.add(kecamatan.get(i).getNama());
//                            listSubId.add(kecamatan.get(i).getId());
//                        }
//
//                        String[] saName = new String[listSub.size()];
//                        saName = listSub.toArray(saName);
//
//                        String[] saId = new String[listSubId.size()];
//                        saId = listSubId.toArray(saId);
//
//                        mySpinnerAdapterSub = new MyAdapterSpinnerRegion
//                                (getApplicationContext(), saId, saName);
//                        spSub.setAdapter(mySpinnerAdapterSub);
//                        mySpinnerAdapterSub.setNotifyOnChange(true);
//                    }
//
//                    @Override
//                    public void onError(ANError error) {
//                        pd.dismiss();
//                        // handle error
//                        if (error.getErrorCode() != 0) {
//                            Log.d(TAG, "onError errorCode : " + error.getErrorCode());
//                            Log.d(TAG, "onError errorBody : " + error.getErrorBody());
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                            // get parsed error object (If ApiError is your class)
//                            RGlobal apiError = error.getErrorAsObject(RGlobal.class);
//                            App.TShort(apiError.getError());
//                        } else {
//                            Log.d(TAG, "onError errorDetail : " + error.getErrorDetail());
//                        }
//                    }
//                });
//    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        etDob.setText(String.valueOf(dayOfMonth + "/" + monthOfYear + "/" + year));
    }

    //    img handler
    private void openFM() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        );
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Pilih Foto"), 1345);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
            openFM();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                //Read External Storage
                case PICK_IMAGE:
                    openFM();
                    break;
            }
        } else {
            Toast.makeText(this, "Ijinkan aplikasi mengakses galeri untuk merubah foto anda", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1345 && resultCode == Activity.RESULT_OK) {

            selectedImage = data.getData();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null)
                return;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String path = cursor.getString(columnIndex);
            cursor.close();

            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap = null;
            }

            pathAvatar = App.getPath(mContext, selectedImage);

            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
//                ivPhotos.setImageBitmap(bitmap);
                Glide.with(App.getAppContext())
                        .setDefaultRequestOptions(requestOptions)
                        .load(bitmap)
                        .apply(RequestOptions.circleCropTransform()).into(ivPhotos);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("Status:", "Action Not Completed");
        }
    }

    @Override
    public void onValidationSucceeded() {
        if (!pathAvatar.equals("")) {
            AndroidNetworking.upload(Apis.GET_USER_BY_ID + Prefs.getString(Constants.G_USER_ID, ""))
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Authorization", Constants.VAL_AUTH)
//                .addBodyParameter("role_id", Prefs.getString(Constants.G_ROLE_ID, ""))
//                .addBodyParameter("name", tvProfileName.getText().toString())
//                .addBodyParameter("email", tvProfileEmail.getText().toString())
//                .addBodyParameter("password", mPass.getText().toString())
//                .addBodyParameter("avatar", pathAvatar)
//                .addBodyParameter("_method", "put")
                    .addMultipartParameter("role_id", Prefs.getString(Constants.G_ROLE_ID, ""))
                    .addMultipartParameter("name", tvProfileName.getText().toString())
                    .addMultipartParameter("email", tvProfileEmail.getText().toString())
                    .addMultipartParameter("password", mPass.getText().toString())
                    .addMultipartFile("avatar", new File(pathAvatar))
                    .addMultipartParameter("_method", "put")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsObject(GUser.class, new ParsedRequestListener<GUser>() {
                        @Override
                        public void onResponse(GUser user) {
                            pd.dismiss();
//                        App.TShort("Profile Updated");
//                        App.intentFinish(MyProfileActivity.this,MainActivity.class);
                            pBiodata();
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
        } else {
            AndroidNetworking.post(Apis.GET_USER_BY_ID + Prefs.getString(Constants.G_USER_ID, ""))
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Authorization", Constants.VAL_AUTH)
                    .addBodyParameter("role_id", Prefs.getString(Constants.G_ROLE_ID, ""))
                    .addBodyParameter("name", tvProfileName.getText().toString())
                    .addBodyParameter("email", tvProfileEmail.getText().toString())
                    .addBodyParameter("password", mPass.getText().toString())
                    .addBodyParameter("_method", "put")
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsObject(GUser.class, new ParsedRequestListener<GUser>() {
                        @Override
                        public void onResponse(GUser user) {
                            pd.dismiss();
                            pBiodata();
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
    }

    private void pBiodata() {
        pd.show();
        AndroidNetworking.post(Apis.BIODATA_POST + "/" + Prefs.getString(Constants.G_BIO_ID, ""))
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .addBodyParameter("user_id", Prefs.getString(Constants.G_USER_ID, ""))
                .addBodyParameter("state_id", etStateId.getText().toString())
                .addBodyParameter("city_id", etCityId.getText().toString())
                .addBodyParameter("subdistrict_id", etSubId.getText().toString())
//                .addBodyParameter("first_name", "")
//                .addBodyParameter("last_name", "")
                .addBodyParameter("address", etProfileAddress.getText().toString())
                .addBodyParameter("phone_number", etProfilePhone.getText().toString())
                .addBodyParameter("phone_number", etProfilePhone.getText().toString())
                .addBodyParameter("_method", "put")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(GUser.class, new ParsedRequestListener<GUser>() {
                    @Override
                    public void onResponse(GUser user) {
                        pd.dismiss();
                        App.TShort("Profile Updated");
                        App.intentFinish(MyProfileActivity.this, MainActivity.class);
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
}
