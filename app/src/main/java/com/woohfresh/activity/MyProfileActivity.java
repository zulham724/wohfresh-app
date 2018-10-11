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
import com.mobsandgeeks.saripaar.Validator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.adapters.MyAdapterSpinnerRegion;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.models.api.RGlobal;
import com.woohfresh.models.api.region.wstate.GwState;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

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
//    @BindView(R.id.profileSpSubdistrict)
//    Spinner spSub;
    MyAdapterSpinnerRegion mySpinnerAdapterState;
    MyAdapterSpinnerRegion mySpinnerAdapterCity;
    MyAdapterSpinnerRegion mySpinnerAdapterSub;

    @BindView(R.id.ivProfile)
    ImageView ivPhotos;

    @OnClick(R.id.fab_profile)
    public void ocFab() {
        askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PICK_IMAGE);
    }

    public static final int PICK_IMAGE = 100;
    Bitmap bitmap;
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
//        App.TShort(selectedStateId+" "+selectedState);
    }

    @BindView(R.id.profileSpStateStatic)
    Spinner spStateStatic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mContext = this;
        ButterKnife.bind(this);
        pd = new App.LoadingPrimary(mContext);

        requestOptions
                .centerCrop()
                .placeholder(R.drawable.profile_blank)
                .error(R.drawable.err_img_port)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
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

        initSpinnerUI();
        initSpinnerDefault();
        initSpinnerState();

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
                if(null != spState.getSelectedView().findViewById(R.id.tvRegionId)) {
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
//                    if (listCity.size() > 0) {
//                        listCity.clear();
//                        listCityId.clear();
//                        initSpinnerSub(selectedCityId);
//                    } else {
//                        initSpinnerSub(selectedCityId);
//                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        spSub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                if (null != spSub.getSelectedView().findViewById(R.id.tvRegionId)) {
//                    selectedSubId = ((TextView) (spSub.getSelectedView().findViewById(R.id.tvRegionId))).getText().toString();
//                    selectedSub = ((TextView) (spSub.getSelectedView().findViewById(R.id.tvRegionName))).getText().toString();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void initSpinnerDefault() {

        listCity.add(0, getString(R.string.profile_prompt_city));
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, R.layout.my_spinner_simple, listCity);
        cityAdapter.setDropDownViewResource(R.layout.my_spinner_simple);
        spCity.setAdapter(cityAdapter);

        listSub.add(0, getString(R.string.profile_prompt_subdistrict));
        ArrayAdapter<String> subAdapter = new ArrayAdapter<String>(this, R.layout.my_spinner_simple, listSub);
        subAdapter.setDropDownViewResource(R.layout.my_spinner_simple);
//        spSub.setAdapter(subAdapter);
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

            Uri selectedImage = data.getData();

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
}
