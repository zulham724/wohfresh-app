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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.mobsandgeeks.saripaar.Validator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.woohfresh.App;
import com.woohfresh.R;

import java.io.FileNotFoundException;
import java.util.Calendar;

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
    }

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
