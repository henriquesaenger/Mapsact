package com.example.mapsact;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.FragmentActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Estabelecimento extends FragmentActivity {

    FirebaseFirestore banco = FirebaseFirestore.getInstance();          //inicia uma instância do Firestore

    FirebaseAuth logAuth;

    private CollectionReference collref= banco.collection("Restaurantes");

    static ArrayList<Pratos> pratos= new ArrayList<Pratos>();                                         //pega o menu do restaurante clicado

    ArrayList<String> ingred= new ArrayList<String>();
    HashMap<String, String> pratos_menu= new HashMap<String, String>();
    String aux;
    static int position;
    List<HashMap<String, String>> listinha=new ArrayList<HashMap<String, String>>();                            //vai ser usado no SimpleAdapter

    @Override
    public void onBackPressed()
    {
        ingred.clear();
        super.onBackPressed();  // optional depending on your needs
    }

    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.estabelecimento);
        ListView menu= (ListView)findViewById(R.id.menu);

        listinha.clear();
        pratos.clear();
        pratos.addAll(MapsActivity.pratos);

        Log.d("TAG", "Verificação Estabelecimento do Pratos Estabelecimento"+pratos.toString());
        pratos_menu.clear();
        for(Pratos p: pratos) {

            aux="";
            for(String ingrediente: p.ingredientes){
                aux+=" - "+ingrediente;
            }
            ingred.add(aux);
            if(p.prato!=null && aux!=null){
                pratos_menu.put(p.prato,aux);
            }
            aux="";
        }

        Log.d("TAG", "Verificação Estabelecimento do pratos_menu"+ pratos_menu.toString());
        SimpleAdapter simpleAdapter= new SimpleAdapter(this, listinha, R.layout.item_lista,new String[]{"First Line", "Second Line"},new int[]{R.id.Text1, R.id.Text2});

        Iterator iterator= pratos_menu.entrySet().iterator();

        while(iterator.hasNext()){
            HashMap<String, String> resultado= new HashMap<>();
            Map.Entry pair= (Map.Entry) iterator.next();
            resultado.put("First Line", pair.getKey().toString());
            resultado.put("Second Line", pair.getValue().toString());
            listinha.add(resultado);
        }

        menu.setAdapter(simpleAdapter);


        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDialog();
            }
        });

    }

    public void openDialog(){
        DBox dialog= new DBox();
        dialog.show(getSupportFragmentManager(), "Avaliação");
    }

    /*public void opendialog(int pos){                                                               //chama a dialog box pra avaliação
        final AlertDialog.Builder alert= new AlertDialog.Builder(this);
        View mview= getLayoutInflater().inflate(R.layout.dialog_box_item, null);
        final CheckBox checkgostei= (CheckBox) mview.findViewById(R.id.gostei);
        final CheckBox checknaogostei=(CheckBox) mview.findViewById(R.id.naogostei);
        Button avaliar=(Button) mview.findViewById(R.id.avaliar);
        alert.setView(mview);
        position=pos;

        final AlertDialog alertDialog= alert.create();
        alertDialog.setCanceledOnTouchOutside(true);

        avaliar.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view){
               if(checkgostei.isChecked()){
                   if(checknaogostei.isChecked()){
                       Toast.makeText(Estabelecimento.this, "Por favor escolha apenas um!", Toast.LENGTH_SHORT).show();
                   }
                   else{
                       DocumentReference docIdRef = banco.collection("Usuário").document(logAuth.getCurrentUser().getUid()).collection("Avaliações").document(pratos.get(position).getID());
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
                       DocumentReference docIdRef = banco.collection("Usuário").document(logAuth.getCurrentUser().getUid()).collection("Avaliações").document(pratos.get(position).getID());
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
    }


    public void updateCheck(Boolean av){
        banco.collection("Usuário").document(logAuth.getCurrentUser().getUid()).collection("Avaliações").document(pratos.get(position).getID()).update("Gostei", av).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        banco.collection("Usuário").document(logAuth.getCurrentUser().getUid()).collection("Avaliações").document(pratos.get(position).getID()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
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
    */


}
