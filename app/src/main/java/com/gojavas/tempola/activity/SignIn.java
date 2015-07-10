package com.gojavas.tempola.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.gojavas.tempola.R;
import com.gojavas.tempola.application.TempolaApplication;
import com.gojavas.tempola.constants.Constants;
import com.gojavas.tempola.database.UserHelper;
import com.gojavas.tempola.entity.UserEntity;
import com.gojavas.tempola.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by gjs331 on 7/3/2015.
 */
public class SignIn extends AppCompatActivity implements View.OnClickListener{

    Button btn_signIn,btn_Register;
    EditText et_email,et_password;
    String str_email,str_password;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        btn_signIn=(Button)findViewById(R.id.signin_submit);
        btn_Register=(Button)findViewById(R.id.signin_register);
        et_email=(EditText)findViewById(R.id.signin_email);
        et_password=(EditText)findViewById(R.id.signin_password);

        progressDialog=new ProgressDialog(SignIn.this);

        btn_signIn.setOnClickListener(this);
        btn_Register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.signin_submit:


                if (!Utility.isInternetConnected(SignIn.this)){
                    Utility.showToast(SignIn.this,"Please Check Your Internet Connection!");
                    return;
                }

                str_email=et_email.getText().toString().trim();
                str_password=et_password.getText().toString().trim();


                if (!emailValidator(str_email)){
                    Utility.showToast(SignIn.this,"Please Enter valid Email id");
                }else if (TextUtils.isEmpty(str_password)){
                    Utility.showToast(SignIn.this,"Please Enter Password ");
                }else {
                    showProgrss("Please Wait...");
                    validateUser();
                }


                break;

            case R.id.signin_register:
                Intent intentRegister=new Intent(SignIn.this,ProfileActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    public void showProgrss(String message){
        if (progressDialog!=null && !progressDialog.isShowing()){
            progressDialog.show();
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
        }
    }


    public void hideProgrss(){
        if (progressDialog!=null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }


    private boolean validateUser(){


        StringRequest jsonObjRequest = new StringRequest(Request.Method.POST,
                Constants.LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        System.out.print(response);
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            boolean response_code=jsonObject.getBoolean("success");



                            if (response_code==false){

                                String error_msg=jsonObject.getString("error");
                                Utility.showToast(SignIn.this,error_msg);

                            }else{
                                System.out.print(response_code);



                        //        {"success":true,"id":19,"first_name":"anshul","last_name":"goel","phone":"123456789",
                        // "email":"anshul.goel@gojavas.com","picture":"http:\/\/tempola.in\/api\/public\/uploads\/449e75ac68e4d13455672dc16d0eed3bc111f018.png",
                        // "bio":"asd","address":"asd","state":"haryana","country":"india","zipcode":"122001",
                        // "login_by":"manual","social_unique_id":"","device_token":"123456dsadasdaddad",
                        // "device_type":"android","token":"2y10Fh1l3YTiS9qB6Ku7c9yZxlWoEoDe6dbzdTrn1Ti3HsR5bICuddwG",
                        // "type":0}

                                UserEntity userEntity=new UserEntity();
//
//                                String id=jsonObject.getString("id");
//                                String first_name=jsonObject.getString("first_name");
//                                String last_name=jsonObject.getString("last_name");
//                                String phone=jsonObject.getString("phone");
//                                String email=jsonObject.getString("email");
//                                String picture=jsonObject.getString("picture");
//                                String bio=jsonObject.getString("bio");
//                                String address=jsonObject.getString("address");
//                                String state=jsonObject.getString("state");
//                                String country=jsonObject.getString("country");
//                                String zipcode=jsonObject.getString("zipcode");
                                String token=jsonObject.getString("token");
//                                String type=jsonObject.getString("type");


                                userEntity.setUserid(jsonObject.getString("id"));
                                userEntity.setFname(jsonObject.getString("first_name"));
                                userEntity.setLname(jsonObject.getString("last_name"));
                                userEntity.setPhoneno(jsonObject.getString("phone"));
                                userEntity.setEmail(jsonObject.getString("email"));
                                userEntity.setPicture(jsonObject.getString("picture"));
                                userEntity.setBio(jsonObject.getString("bio"));
                                userEntity.setAddress(jsonObject.getString("address"));
                                userEntity.setState(jsonObject.getString("state"));
                                userEntity.setCountry(jsonObject.getString("country"));
                                userEntity.setZipcode(jsonObject.getString("zipcode"));
                                userEntity.setToken(jsonObject.getString("token"));
                                userEntity.setType(jsonObject.getString("type"));

                                Utility.saveToSharedPrefs(SignIn.this,Constants.TOKEN,token);

                                UserHelper.getInstance().insertOrUpdate(userEntity);

                                Intent intentSubmit=new Intent(SignIn.this,MainActivity.class);
                                startActivity(intentSubmit);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        hideProgrss();



                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgrss();
                VolleyLog.d("volley", "Error: " + error.getMessage());
                error.printStackTrace();

                Utility.showToast(SignIn.this,"Server Error");

              }
        }) {

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
//                        params.put("username", etUname.getText().toString().trim());
//                        params.put("password", etPass.getText().toString().trim());

                params.put("email", str_email);
                params.put("password", str_password);
                params.put("device_type", "android");
                params.put("device_token", Utility.getDeviceId());
                params.put("login_by", "manual");

                return params;
            }

        };

        TempolaApplication.getInstance().addToRequestQueue(jsonObjRequest);


        return false;
    }


    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
