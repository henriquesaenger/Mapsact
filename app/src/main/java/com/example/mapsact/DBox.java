package com.example.mapsact;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DBox extends DialogFragment {

    FirebaseFirestore banco = FirebaseFirestore.getInstance();          //inicia uma instância do Firestore
    FirebaseUser atual= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder alert= new AlertDialog.Builder(getActivity());
        View mview= getLayoutInflater().inflate(R.layout.activity_d_box, null);
        final CheckBox checkgostei= (CheckBox) mview.findViewById(R.id.gostei);
        final CheckBox checknaogostei=(CheckBox) mview.findViewById(R.id.naogostei);
        Button avaliar=(Button) mview.findViewById(R.id.avaliar);
        alert.setView(mview);

        Dialog dialog= alert.create();
        dialog.setCanceledOnTouchOutside(true);

        avaliar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(checkgostei.isChecked()){
                    if(checknaogostei.isChecked()){
                        Toast.makeText(getActivity(), "Por favor escolha apenas um!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        DocumentReference docIdRef = banco.collection("Usuários").document(atual.getUid()).collection("Avaliações").document(Estabelecimento.pratos.get(Estabelecimento.position).getID());
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d("TAG", "Document exists!");
                                        updateCheck(true);
                                    } else {
                                        Log.d("TAG", "Document does not exist!");
                                        createAvaliation(true);
                                    }
                                } else {
                                    Log.d("TAG", "Failed with: ", task.getException());
                                }
                            }
                        });
                    }
                }
                else{
                    if(checknaogostei.isChecked()){
                        DocumentReference docIdRef = banco.collection("Usuários").document(atual.getUid()).collection("Avaliações").document(Estabelecimento.pratos.get(Estabelecimento.position).getID());
                        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d("TAG", "Document exists!");
                                        updateCheck(false);
                                    } else {
                                        Log.d("TAG", "Document does not exist!");
                                        createAvaliation(false);
                                    }
                                } else {
                                    Log.d("TAG", "Failed with: ", task.getException());
                                }
                            }
                        });
                    }
                }
            }
        });
        return dialog;

    }

    public void updateCheck(Boolean av){
        banco.collection("Usuários").document(atual.getUid()).collection("Avaliações").document(Estabelecimento.pratos.get(Estabelecimento.position).getID()).update("Gostei", av).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "Document successfully updated");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error updating document", e);
            }
        });
    }

    public void createAvaliation(Boolean av){
        Map<String, Boolean> map= new HashMap<>();
        map.put("Gostei", av);
        banco.collection("Usuários").document(atual.getUid()).collection("Avaliações").document(Estabelecimento.pratos.get(Estabelecimento.position).getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("TAG", "Document successfully written");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "Error writing document", e);
            }
        });
    }
}
