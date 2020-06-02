package com.example.mapsact.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mapsact.MapsActivity;
import com.example.mapsact.R;

import io.realm.Realm;
import io.realm.RealmObject;

public class Login extends AppCompatActivity {

    private Button btnLogin;
    private Button btnSignUp;
    Realm inst= Realm.getDefaultInstance();            //cria uma instancia do realm
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        Realm.init(this);           //inicializa o Realm





        btnLogin = (Button) findViewById(R.id.Login);
        btnSignUp = (Button) findViewById(R.id.Signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }


        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //insert database
    public void insert(String usuario, String senha){
        inst.beginTransaction();

        RealmLogin user= inst.createObject(RealmLogin.class);
        user.setUser(usuario);
        user.setPass(senha);

        inst.commitTransaction();

    }


}
