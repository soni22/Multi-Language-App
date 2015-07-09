package com.gojavas.tempola.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.gojavas.tempola.R;

/**
 * Created by gjs331 on 7/3/2015.
 */
public class SignIn extends AppCompatActivity implements View.OnClickListener{

    Button btn_signIn,btn_Register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        btn_signIn=(Button)findViewById(R.id.signin_submit);
        btn_Register=(Button)findViewById(R.id.signin_register);


        btn_signIn.setOnClickListener(this);
        btn_Register.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.signin_submit:
                Intent intentSubmit=new Intent(SignIn.this,MainActivity.class);
                startActivity(intentSubmit);
                break;

            case R.id.signin_register:
                Intent intentRegister=new Intent(SignIn.this,ProfileActivity.class);
                startActivity(intentRegister);
                break;
        }
    }
}
