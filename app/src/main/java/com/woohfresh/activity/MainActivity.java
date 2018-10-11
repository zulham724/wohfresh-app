package com.woohfresh.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;
import com.woohfresh.fragments.FruitsFragment;
import com.woohfresh.fragments.HomeFragment;
import com.woohfresh.fragments.RecipesFragment;
import com.woohfresh.fragments.SettingsFragment;
import com.woohfresh.models.api.search.state.GStateId;

import java.util.List;
import java.util.Locale;

import static com.woohfresh.data.local.Constants.USER_NAME;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtedit;
    private Toolbar toolbar;

    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "https://png.pngtree.com/thumb_back/fw800/back_pic/03/59/79/0557a49d0f32599.jpg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_FRUITS = "fruits";
    private static final String TAG_RECIPES = "recipes";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    //getCurrentLoc
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String lattitude, longitude;
    App.LoadingPrimary pd;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();
        pd = new App.LoadingPrimary(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtedit = (TextView) navHeader.findViewById(R.id.edit);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }

//        ActivityCompat.requestPermissions(this,
//                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
//
//        initLocation();

        if (CheckPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // you have permission go ahead
            initLocation();
        } else {
            // you do not have permission go request runtime permissions
            RequestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        switch (permsRequestCode) {

            case REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // you have permission go ahead
                    initLocation();
                } else {
                    // you do not have permission show toast.
                    App.TShort("Unable to get product by location");
                }
                return;
            }
        }
    }

    public void RequestPermission(Activity thisActivity, String Permission, int Code) {
        if (ContextCompat.checkSelfPermission(thisActivity,
                Permission)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(thisActivity,
                    Permission)) {
            } else {
                ActivityCompat.requestPermissions(thisActivity,
                        new String[]{Permission},
                        Code);
            }
        }
    }

    public boolean CheckPermission(Context context, String Permission) {
        if (ContextCompat.checkSelfPermission(context,
                Permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void initLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latti, longi, 1);
                    getStateId(addresses.get(0).getAdminArea());
//                    App.TShort(addresses.get(0).getAdminArea());
                } catch (Exception e) {

                }

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latti, longi, 1);
                    getStateId(addresses.get(0).getAdminArea());
//                    App.TShort(addresses.get(0).getAdminArea());
                } catch (Exception e) {

                }


            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);

//                textView.setText("Your current location is"+ "\n" + "Lattitude = " + lattitude
//                        + "\n" + "Longitude = " + longitude);
                try {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(latti, longi, 1);
                    getStateId(addresses.get(0).getAdminArea());
//                    App.TShort(addresses.get(0).getAdminArea());
                } catch (Exception e) {

                }

            }
            else {
                Toast.makeText(this, "Unable to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Turn ON your location to get product by location")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, edit, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, edit
        txtName.setText(Prefs.getString(USER_NAME,""));
        txtedit.setText("edit");
        txtedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MyProfileActivity.class));
                drawer.closeDrawers();
            }
        });

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.bg_gradient_primary);
        requestOptions.error(R.drawable.err_img_land);

        // loading header background image
        Glide.with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(R.drawable.my_bg_nav_header).into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this)
                .load(R.drawable.profile_blank)
                .apply(RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.profile_blank)
                        .error(R.drawable.err_img_port))
                .into(imgProfile);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                Bundle data = new Bundle();
                data.putString("url", CURRENT_TAG);
                fragment.setArguments(data);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                FruitsFragment fruitsFragment = new FruitsFragment();
                return fruitsFragment;
            case 2:
                RecipesFragment recipesFragment = new RecipesFragment();
                return recipesFragment;
            case 3:
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_fruits:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_FRUITS;
                        break;
                    case R.id.nav_recipes:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_RECIPES;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, .class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(MainActivity.this, .class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder
                    .setTitle(getString(R.string.set_exit_t))
                    .setMessage(getString(R.string.info_msg_exit))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.act_ok),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        Process.killProcess(Process.myPid());
                                        System.exit(0);
                                }
                            })
                    .setNegativeButton(getString(R.string.setting_cancel), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

//        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.menu_home, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.navFind) {
            startActivity(new Intent(this,SearchActivity.class));
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
//        if (id == R.id.action_mark_all_read) {
//            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
//        }
//
//        // user is in notifications fragment
//        // and selected 'Clear All'
//        if (id == R.id.action_clear_notifications) {
//            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
//        }

        return super.onOptionsItemSelected(item);
    }

    public void getStateId(String stateId){
//        App.TShort(stateId);
        pd.show();
        AndroidNetworking.get(Apis.URL_SEARCH_STATE+"{stateId}")
                .addPathParameter("stateId", stateId)
                .addHeaders("Accept", "application/json")
                .addHeaders("Authorization", Constants.VAL_AUTH)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .build()
                .getAsObjectList(GStateId.class, new ParsedRequestListener<List<GStateId>>() {
                    @Override
                    public void onResponse(List<GStateId> rets) {
                        // do anything with response
                        for (GStateId ret : rets) {
                            Prefs.putString(Constants.loc_state_id, String.valueOf(ret.getId()));
                            pd.dismiss();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        // handle error
                        pd.dismiss();
                        App.TShort(anError.getErrorDetail());
                    }
                });
    }
}
