package com.example.mapsact.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mapsact.MapsActivity;
import com.example.mapsact.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    private Button btnLogin;
    private Button btnSignUp;
    private TextView txtUs;
    private TextView txtPs;
    FirebaseAuth logAuth;
    FirebaseFirestore banco= FirebaseFirestore.getInstance();
    CollectionReference collref= banco.collection("Usuários");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


        btnLogin = (Button) findViewById(R.id.Login);
        btnSignUp = (Button) findViewById(R.id.Signup);
        txtUs = (TextView) findViewById(R.id.usuario);
        txtPs = (TextView) findViewById(R.id.password);
        logAuth = FirebaseAuth.getInstance();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser(txtUs.getText().toString(), txtPs.getText().toString());
            }


        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               CriarUser(txtUs.getText().toString(), txtPs.getText().toString());
            }
        });
    }

    public void LoginUser(String email, String password){
        logAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithEmail:success");
                    FirebaseUser user = logAuth.getCurrentUser();
                    Intent intent = new Intent(Login.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String resposta= task.getException().toString();
                    tratamentoErro(resposta);
                }

                // ...
            }
        });
    }

    public void CriarUser(String email, String password){
        logAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = logAuth.getCurrentUser();
                            adicionaUser(user.getUid());//adiciona o usuário ao banco de dados de usuários
                            Intent intent = new Intent(Login.this, MapsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String resposta= task.getException().toString();
                            tratamentoErro(resposta);
                        }

                        // ...
                    }
                });

    }

    private void tratamentoErro(String resposta){
        if(resposta.contains("least 6 characters")){
            Toast.makeText(getApplicationContext(), "A senha deve conter um mínimo de 6 caracteres",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            if(resposta.contains("address is badly")){
                Toast.makeText(getApplicationContext(), "Email de formato incorreto, tente digitar um email válido",
                        Toast.LENGTH_SHORT).show();
            }
            else{
                if(resposta.contains("address is already")){
                    Toast.makeText(getApplicationContext(), "Este usuário já existe!",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    if(resposta.contains("interrupted connection")){
                        Toast.makeText(getApplicationContext(), "Sem conexão com a internet no momento!",
                                Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), resposta, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void adicionaUser(String id){
        Map<String, Object> usuario= new HashMap<>();
        usuario.put("Lactose", true);
        usuario.put("Gluten", true);
        banco.collection("Usuários").document(id)
                .set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "DocumentSnapshot successfully written!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error writing document", e);
                    }
                });
    }


}
