package com.woohfresh;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.data.sources.remote.api.ApiService;
import com.woohfresh.data.sources.remote.api.Apis;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.woohfresh.data.local.Datas.APP_LANG;
import static com.woohfresh.data.local.Datas.IS_LANGUAGE;

public class App extends Application {

    public static String appVersion = BuildConfig.VERSION_NAME;
    private static Context appContext;
    public static ApiService mApiService;
    private static App instance;

    public App() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        appContext = this;
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName("spWoohFresh")
                .setUseDefaultSharedPreference(true)
                .build();

        mApiService = Apis.getAPIService();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        AndroidNetworking.initialize(getApplicationContext(), client);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static class LoadingPrimary extends Dialog {

        private ImageView iv;
        private TextView tv;

        public LoadingPrimary(Context context) {
            super(context, R.style.TransparentProgressDialog);
            WindowManager.LayoutParams wlmp = getWindow().getAttributes();
            wlmp.gravity = Gravity.CENTER_HORIZONTAL;
            getWindow().setAttributes(wlmp);
            setTitle(null);
            setCancelable(false);
            setOnCancelListener(null);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            iv = new ImageView(context);
            iv.setImageResource(R.drawable.ic_toys);
            layout.addView(iv, params);

            TextView tv = new TextView(context);
            tv.setText("Please Wait");
            tv.setTypeface(Typeface.SANS_SERIF);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextColor(context.getResources().getColor(android.R.color.white));
            layout.addView(tv, params);

            addContentView(layout, params);
        }

        @Override
        public void show() {
            super.show();
            YoYo.with(Techniques.Shake)
                    .duration(3000)
                    .repeat(-1)
                    .playOn(iv);
        }
    }

    //toast
    public static void TShort(String title) {
        if (title.toLowerCase().contains("failed")) {
            Toast.makeText(getAppContext(), getAppContext().getString(R.string.err_server), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getAppContext(), title, Toast.LENGTH_SHORT).show();
        }
    }

    public static void TLong(String title) {
        if (title.toLowerCase().contains("failed")) {
            Toast.makeText(getAppContext(), getAppContext().getString(R.string.err_server), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getAppContext(), title, Toast.LENGTH_LONG).show();
        }
    }

    public static void showToastContext(Context context, String title) {
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    //date format
    public static String getBulan(String i) {
        String hasil = "";
        if (i.equalsIgnoreCase("01")) {
            hasil = "Januari";
        } else if (i.equalsIgnoreCase("02")) {
            hasil = "Februari";
        } else if (i.equalsIgnoreCase("03")) {
            hasil = "Maret";
        } else if (i.equalsIgnoreCase("04")) {
            hasil = "April";
        } else if (i.equalsIgnoreCase("05")) {
            hasil = "Mei";
        } else if (i.equalsIgnoreCase("06")) {
            hasil = "Juni";
        } else if (i.equalsIgnoreCase("07")) {
            hasil = "Juli";
        } else if (i.equalsIgnoreCase("08")) {
            hasil = "Agustus";
        } else if (i.equalsIgnoreCase("09")) {
            hasil = "September";
        } else if (i.equalsIgnoreCase("10")) {
            hasil = "Oktober";
        } else if (i.equalsIgnoreCase("11")) {
            hasil = "November";
        } else if (i.equalsIgnoreCase("12")) {
            hasil = "Desember";
        }

        return hasil;
    }

    public static String tglJamSqlToInd(String tgl) {
        try {
            String x[] = tgl.split(" ");
            String x1[] = x[0].split("-");
            String jam[] = x[1].split(":");


            return x1[2] + " " + getBulan(x1[1]) + " " + x1[0] + " | " + jam[0] + ":" + jam[1];
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static void copyClipboard(String label) {
        ClipboardManager clipboard = (ClipboardManager) appContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", label);
        clipboard.setPrimaryClip(clip);
    }

    public static Double d(String transPokok) {
        Double x = 0.0;
        try {
            x = Double.parseDouble(transPokok);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return x;
    }

    public static String toRupiah(String nominal) {

        DecimalFormat df = (DecimalFormat) DecimalFormat.getCurrencyInstance();

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(dfs);
        df.setMaximumFractionDigits(0);
        String rupiah = df.format(d(nominal));

        return rupiah;
    }

    public static void callFullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    String id = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        id = DocumentsContract.getDocumentId(uri);
                    }
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    String docId = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        docId = DocumentsContract.getDocumentId(uri);
                    }
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    //whatsapp
    public static void wa(final LinearLayout ll, final String s, final Activity mActivity) {
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse(
                                "https://api.whatsapp.com/send?phone=" + s + "&text=Assalamualaikum,%20Saya%20memerlukan%20bantuan%20Aljihad-Smart,%20"
                        )));
            }
        });
    }

    //    language
    public static void showDialogLang(final Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.my_dialog_language, null);
        dialogBuilder.setView(dialogView);

        final Spinner spinner1 = (Spinner) dialogView.findViewById(R.id.spinner1);
        dialogBuilder.setTitle(activity.getString(R.string.setting_lang));
        dialogBuilder.setPositiveButton(activity.getString(R.string.setting_lang_change), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int position = spinner1.getSelectedItemPosition();
                changeLang(activity, position);
            }
        });
        dialogBuilder.setNegativeButton(activity.getString(R.string.setting_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
        Prefs.putString(IS_LANGUAGE,"1");
    }

    public static void changeLang(Activity mActivity, int position) {
        switch (position) {
            case 0: //English
                Prefs.putString(APP_LANG, "en");
                setLangRecreate(mActivity, "en");
                return;
            case 1: //Indonesia
                Prefs.putString(APP_LANG, "in");
                setLangRecreate(mActivity, "in");
                return;
            default: //By default set to english
                Prefs.putString(APP_LANG, "en");
                setLangRecreate(mActivity, "en");
                return;
        }
    }

    public static void setLangRecreate(Activity mActivity, String lang) {
        Configuration config = mActivity.getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        config.locale = locale;
        mActivity.getBaseContext().getResources().updateConfiguration(config, mActivity.getBaseContext().getResources().getDisplayMetrics());
        mActivity.recreate();
    }

}
