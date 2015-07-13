package com.gojavas.tempola.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.gojavas.tempola.R;
import com.gojavas.tempola.application.TempolaApplication;
import com.gojavas.tempola.network.MultipartRequest;
import com.gojavas.tempola.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by GJS280 on 2/7/2015.
 */
public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mProfileImageView;
    private Spinner spinner_countryList,spinner_vehicletype;
    public static final int REQUEST_CAMERA =1 ;
    public static final int SELECT_FILE =2 ;
    private static Bitmap image=null;
    Button btn_submit;
    EditText et_firstName,et_LastName,et_Password,et_emailid,et_phoneNumber,et_address,et_zipcode,et_bio;
    ArrayList<String> arrayList_vehicles=new ArrayList<>();
    String str_vehicletype,str_countrycode;
    String firstName,lastName,password,emailid,phoneNumber,address,zipcode,bio, mProfileImagePath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        registerViews();
        setUpClickListners();
        loadProfileImage();

        if(image!=null){
            mProfileImageView.setImageBitmap(image);
        }



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(image!=null){
            mProfileImageView.setImageBitmap(image);
        }

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.profile_imageview:
                selectImage();
                break;

            case R.id.submitregistration:
                sendRequest();
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                image=null;
//                image=thumbnail;



                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                mProfileImagePath = destination.getPath();

                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();

                    Bitmap bm;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(destination.getAbsolutePath(), options);
                    final int REQUIRED_SIZE = 200;
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                            && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    image = BitmapFactory.decodeFile(destination.getAbsolutePath(), options);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mProfileImageView.setImageBitmap(thumbnail);

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                mProfileImagePath = cursor.getString(column_index);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mProfileImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(mProfileImagePath, options);

                mProfileImageView.setImageBitmap(bm);
            }
        }
    }
    private void registerViews() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.profile_collapsing_toolbar);

        mProfileImageView = (ImageView) findViewById(R.id.profile_imageview);
        et_firstName=(EditText)findViewById(R.id.profile_firstname);
        et_LastName=(EditText)findViewById(R.id.profile_lastname);
        et_Password=(EditText)findViewById(R.id.profile_password);
        et_emailid=(EditText)findViewById(R.id.profile_email);
        et_phoneNumber=(EditText)findViewById(R.id.profile_phoneno);
        et_address=(EditText)findViewById(R.id.profile_address);
        et_zipcode=(EditText)findViewById(R.id.profile_zipcode);
        et_bio=(EditText)findViewById(R.id.profile_bio);
        spinner_countryList=(Spinner)findViewById(R.id.profile_countrycode);
        spinner_vehicletype=(Spinner)findViewById(R.id.spinner_vehicletype);
        btn_submit=(Button)findViewById(R.id.submitregistration);

        ArrayAdapter<String> adapter_vehicle = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_single_choice,arrayList_vehicles);
        adapter_vehicle.add("Car");
        adapter_vehicle.add("Truck");
        spinner_vehicletype.setAdapter(adapter_vehicle);

        StringRequest strReq = new StringRequest(Request.Method.GET,
                "http://tempola.in/api/public/application/types", new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("RESPONSE", response.toString());

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    JSONArray jsonArray=jsonObject.getJSONArray("types");
                    int len=jsonArray.length();

                    for (int i=0;i<len;i++){
                        JSONObject jsonObject1=jsonArray.getJSONObject(i);
                        arrayList_vehicles.add(jsonObject1.getString("name"));
                    }


                    ArrayAdapter<String> adapter_vehicle = new ArrayAdapter<String>(ProfileActivity.this,
                            android.R.layout.simple_list_item_1,arrayList_vehicles);
                    adapter_vehicle
                            .setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinner_vehicletype.setAdapter(adapter_vehicle);

                    spinner_vehicletype
                            .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                                           @Override
                                                           public void onItemSelected(AdapterView<?> parent,
                                                                                      View view, int position, long id) {
                                                               // TODO Auto-generated method stub

                                                               str_vehicletype=(position+1)+"";
//                                                   Toast.makeText(ProfileActivity.this, position + "", Toast.LENGTH_LONG).show();
                                                           }

                                                           @Override
                                                           public void onNothingSelected(AdapterView<?> parent) {
                                                               // TODO Auto-generated method stub

                                                           }
                                                       }


                            );






                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("tag", "Error: " + error.getMessage());

            }
        });

        TempolaApplication.getInstance().addToRequestQueue(strReq);
   }

    private void setUpClickListners() {

        mProfileImageView.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

        final ArrayList<String> countryList=parseCountryCodes();

        ArrayAdapter<String> adapter_state = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,countryList);
        adapter_state
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_countryList.setAdapter(adapter_state);
        spinner_countryList.setSelection(79);

        spinner_countryList
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                               @Override
                                               public void onItemSelected(AdapterView<?> parent,
                                                                          View view, int position, long id) {
                                                   // TODO Auto-generated method stub

                                                   str_countrycode=countryList.get(position).substring(0,countryList.get(position).indexOf(" "));

//                                                   Toast.makeText(ProfileActivity.this, position + "", Toast.LENGTH_LONG).show();
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {
                                                   // TODO Auto-generated method stub

                                               }
                                           }
                );

    }


    private void loadProfileImage() {

    }

    /**
     * Get Country codes from countrycodes.txt in assests
     * @return
     */

    public ArrayList<String> parseCountryCodes() {
        String response = "";
        ArrayList<String> list = new ArrayList<String>();
        try {
            StringBuffer sb = new StringBuffer();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(getAssets().open(
                        "countrycodes.txt")));
                String temp;
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                    //System.out.println("br::" + br);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    br.close(); // stop reading
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            response = sb.toString();


            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                list.add(object.getString("phone-code") + " "
                        + object.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Photo!");
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                }
        );
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } /*else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }*/
            }
        });
        builder.show();
    }


    private void sendRequest() {

        firstName=et_firstName.getText().toString().trim();
        lastName=et_LastName.getText().toString().trim();
        password=et_Password.getText().toString().trim();
        emailid=et_emailid.getText().toString().trim();
        phoneNumber=et_phoneNumber.getText().toString().trim();
        address=et_address.getText().toString().trim();
        zipcode=et_zipcode.getText().toString().trim();
        bio=et_bio.getText().toString().trim();

        if(mProfileImagePath == null || TextUtils.isEmpty(mProfileImagePath) || !new File(mProfileImagePath).exists()) {
            Utility.showToast(ProfileActivity.this,"Please choose a photo");
        } else if (TextUtils.isEmpty(firstName)){

            Utility.showToast(ProfileActivity.this,"Please Enter First Name");
        }else if(TextUtils.isEmpty(lastName)){

            Utility.showToast(ProfileActivity.this,"Please Enter Last Name");
        }else if(TextUtils.isEmpty(password)){

            Utility.showToast(ProfileActivity.this,"Please Enter Password");

        }else if(TextUtils.isEmpty(emailid)){
            Utility.showToast(ProfileActivity.this,"Please Enter EmailId ");

        }else if(TextUtils.isEmpty(phoneNumber)){
            Utility.showToast(ProfileActivity.this,"Please Enter PhoneNumber ");
        }else if(TextUtils.isEmpty(address)){
            Utility.showToast(ProfileActivity.this,"Please Enter Address");

        }else if(TextUtils.isEmpty(zipcode)){

            Utility.showToast(ProfileActivity.this,"Please Enter ZipCode ");
        }else if(TextUtils.isEmpty(bio)){

            Utility.showToast(ProfileActivity.this,"Please Enter Bio");
        }else {

            btn_submit.setText("Please wait...");
            btn_submit.setEnabled(false);

            String vehicleType = spinner_vehicletype.getSelectedItem().toString();
            String countryFull = spinner_countryList.getSelectedItem().toString();
            String country = countryFull.substring(countryFull.indexOf(" "));
            JSONObject loginRequest = new JSONObject();

            try {

                loginRequest.put("first_name", firstName);
                loginRequest.put("last_name", lastName);
                loginRequest.put("email", emailid);
                loginRequest.put("phone", phoneNumber);
                loginRequest.put("password", password);
                loginRequest.put("type", vehicleType);
                loginRequest.put("device_token", Utility.getDeviceId());
                loginRequest.put("device_type", "android");
                loginRequest.put("bio", bio);
                loginRequest.put("address", address);
                loginRequest.put("state", "");
                loginRequest.put("country", country);
                loginRequest.put("zipcode", zipcode);
                loginRequest.put("login_by", "manual");

                MultipartRequest loginMultipartRequest = new MultipartRequest("http://tempola.in/api/public/provider/register", new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        btn_submit.setText("Submit");
                        btn_submit.setEnabled(true);
                    }
                }, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        btn_submit.setText("Submit");
                        btn_submit.setEnabled(true);
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            String success = String.valueOf(responseObject.getBoolean("success"));
                            if(success.contains("false")) {
//                                JSONArray errorArray = responseObject.getJSONArray("error_messages");
//                                int size = errorArray.length();
//                                String errorMessage = "";
//                                for(int i=0; i<size; i++) {
//                                    errorMessage += errorArray.getString(i) + " ";
//                                }
                                String errorMessage = responseObject.getString("error");
                                Utility.showToast(ProfileActivity.this, errorMessage);
                            } else if(success.contains("true")) {
                                goToSignInScreen();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new File(mProfileImagePath), loginRequest);
                TempolaApplication.getInstance().addToRequestQueue(loginMultipartRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Go to sign in screen
     */
    private void goToSignInScreen() {
        Intent signInIntent = new Intent(ProfileActivity.this, SignIn.class);
        startActivity(signInIntent);
    }
}
