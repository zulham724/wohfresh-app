package com.woohfresh.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.pixplicity.easyprefs.library.Prefs;
import com.woohfresh.App;
import com.woohfresh.R;
import com.woohfresh.data.local.Constants;
import com.woohfresh.data.sources.remote.api.Apis;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipesAddActivity extends AppCompatActivity implements Validator.ValidationListener {

    @BindView(R.id.llAddRecipe)
    LinearLayout llAdd;
    @BindView(R.id.svAdd)
    ScrollView mAdd;
    @NotEmpty
    @BindView(R.id.etRecipesName)
    EditText etName;
    @NotEmpty
    @BindView(R.id.etRecipesDesc)
    EditText etDesc;
    @NotEmpty
    @BindView(R.id.etRecipesTutorial)
    EditText etTutorial;
    @BindView(R.id.rtbRecipes)
    RatingBar rbRecipes;
    @BindView(R.id.sbRecipes)
    SeekBar sbRecipes;
    @BindView(R.id.tvSBrecipes)
    TextView tvTagSb;
    @NotEmpty
    @BindView(R.id.etRecipesPortion)
    EditText etPortion;
    @OnClick(R.id.btnRecipesAdd)
    public void ocAdd(){
        validator.validate();
    }

    @BindView(R.id.svEdit)
    ScrollView mEdit;
    @NotEmpty
    @BindView(R.id.etEditRecipesName)
    EditText etEditName;
    @NotEmpty
    @BindView(R.id.etEditRecipesDesc)
    EditText etEditDesc;
    @NotEmpty
    @BindView(R.id.etEditRecipesTutorial)
    EditText etEditTutorial;
    @BindView(R.id.rtbEditRecipes)
    RatingBar rbEditRecipes;
    @BindView(R.id.sbEditRecipes)
    SeekBar sbEditRecipes;
    @BindView(R.id.tvEditSBrecipes)
    TextView tvEditTagSb;
    @NotEmpty
    @BindView(R.id.etEditRecipesPortion)
    EditText etEditPortion;
    @BindView(R.id.btnRecipesEdit)
    Button btnEdit;
    @OnClick(R.id.btnRecipesEdit)
    public void ocEdit(){
        validator.validate();
    }
    @BindView(R.id.btnRecipesDelete)
    Button btnDelete;
    @OnClick(R.id.btnRecipesDelete)
    public void ocDelete(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle(getString(R.string.set_exit_t))
                .setMessage(getString(R.string.info_msg_del_recipes))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.act_ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                pd.show();
                                AndroidNetworking.delete(Apis.GET_RECIPES+"/"+getIntent().getStringExtra(Constants.RECIPES_ID))
                                        .addHeaders("Accept", "application/json")
                                        .addHeaders("Authorization", Constants.VAL_AUTH)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                pd.dismiss();
                                                App.TShort(c,"Resep berhasil dihapus");
                                                App.intentFinish(RecipesAddActivity.this, MainActivity.class);
                                            }

                                            @Override
                                            public void onError(ANError error) {
                                                pd.dismiss();
                                                App.TShort(c,"Resep berhasil dihapus");
                                                App.intentFinish(RecipesAddActivity.this, MainActivity.class);
                                            }
                                        });
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

    Validator validator;
    App.LoadingPrimary pd;
    Context c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Resep");
        ButterKnife.bind(this);
        c = this;
        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RecipesAddActivity.class);
                Bundle b = new Bundle();
                b.putString(Constants.RECIPES_STATUS, "add");
                i.putExtras(b);
                startActivity(i);
                finish();
            }
        });
        sbRecipes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvTagSb.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pd = new App.LoadingPrimary(this);
        validator = new Validator(this);
        validator.setValidationListener(this);

        Intent i = getIntent();
        String id = i.getStringExtra(Constants.RECIPES_ID);
        String user_id = i.getStringExtra(Constants.RECIPES_USER_ID);
        String name = i.getStringExtra(Constants.RECIPES_NAME);
        String description = i.getStringExtra(Constants.RECIPES_DESCRIPTION);
        String tutorial = i.getStringExtra(Constants.RECIPES_TUTORIAL);
        String difficulty_level = i.getStringExtra(Constants.RECIPES_DIFFICULTY_LEVEL);
        String preparation_time = i.getStringExtra(Constants.RECIPES_PREPARATION_TIME);
        String portion_per_serve = i.getStringExtra(Constants.RECIPES_PORTION_PER_SERVE);

        if("edit".equals(i.getStringExtra(Constants.RECIPES_STATUS))){
            mAdd.setVisibility(View.GONE);
            mEdit.setVisibility(View.VISIBLE);
            etEditName.setText(name);
            etEditDesc.setText(description);
            etEditTutorial.setText(tutorial);
            rbEditRecipes.setRating(Float.parseFloat(difficulty_level));
            tvEditTagSb.setText(preparation_time);
            etEditPortion.setText(portion_per_serve);
        }else if("show".equals(i.getStringExtra(Constants.RECIPES_STATUS))){
            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );
            mAdd.setVisibility(View.GONE);
            mEdit.setVisibility(View.VISIBLE);
            etEditName.setText(name);
            etEditName.setEnabled(false);
            etEditDesc.setText(description);
            etEditDesc.setEnabled(false);
            etEditTutorial.setText(tutorial);
            etEditTutorial.setEnabled(false);
            rbEditRecipes.setRating(Float.parseFloat(difficulty_level));
            tvEditTagSb.setText(preparation_time);
            etEditPortion.setText(portion_per_serve);
            etEditPortion.setEnabled(false);
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }else if("add".equals(i.getStringExtra(Constants.RECIPES_STATUS))){
            llAdd.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValidationSucceeded() {
        if(mAdd.getVisibility() == View.VISIBLE) {
            pd.show();
            String user_id = Prefs.getString(Constants.G_USER_ID, "");
            String name = etName.getText().toString();
            String description = etDesc.getText().toString();
            String tutorial = etTutorial.getText().toString();
            String difficulty_level = String.valueOf(rbRecipes.getRating());
            String preparation_time = tvTagSb.getText().toString();
            String portion_per_serve = etPortion.getText().toString();

//        App.TShort(c,user_id + "\n" +name + "\n" +description + "\n" +tutorial + "\n" +difficulty_level + "\n" +preparation_time + "\n" +portion_per_serve);

            AndroidNetworking.post(Apis.GET_RECIPES)
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Authorization", Constants.VAL_AUTH)
                    .addBodyParameter("user_id", user_id)
                    .addBodyParameter("name", name)
                    .addBodyParameter("description", description)
                    .addBodyParameter("tutorial", tutorial)
                    .addBodyParameter("difficulty_level", difficulty_level)
                    .addBodyParameter("preparation_time", preparation_time)
                    .addBodyParameter("portion_per_serve", portion_per_serve)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            pd.dismiss();
                            App.TShort(c,"Resep berhasil ditambahkan");
                            App.intentFinish(RecipesAddActivity.this, MainActivity.class);
                        }

                        @Override
                        public void onError(ANError error) {
                            pd.dismiss();
                            App.TShort(c,error.getErrorDetail());
                        }
                    });
        }else if(mEdit.getVisibility() == View.VISIBLE){
            pd.show();
            String user_id = Prefs.getString(Constants.G_USER_ID, "");
            String name = etEditName.getText().toString();
            String description = etEditDesc.getText().toString();
            String tutorial = etEditTutorial.getText().toString();
            String difficulty_level = String.valueOf(rbEditRecipes.getRating());
            String preparation_time = tvEditTagSb.getText().toString();
            String portion_per_serve = etEditPortion.getText().toString();

            AndroidNetworking.put(Apis.GET_RECIPES+"/"+getIntent().getStringExtra(Constants.RECIPES_ID))
                    .addHeaders("Accept", "application/json")
                    .addHeaders("Authorization", Constants.VAL_AUTH)
                    .addBodyParameter("user_id", user_id)
                    .addBodyParameter("name", name)
                    .addBodyParameter("description", description)
                    .addBodyParameter("tutorial", tutorial)
                    .addBodyParameter("difficulty_level", difficulty_level)
                    .addBodyParameter("preparation_time", preparation_time)
                    .addBodyParameter("portion_per_serve", portion_per_serve)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            pd.dismiss();
                            App.TShort(c,"Resep berhasil diperbaharui");
                            App.intentFinish(RecipesAddActivity.this, MainActivity.class);
                        }

                        @Override
                        public void onError(ANError error) {
                            pd.dismiss();
                            App.TShort(c,error.getErrorDetail());
                        }
                    });
        }
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
