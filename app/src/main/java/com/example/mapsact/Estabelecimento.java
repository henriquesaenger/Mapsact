package com.example.mapsact;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.fragment.app.FragmentActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Estabelecimento extends FragmentActivity {

    FirebaseFirestore banco = FirebaseFirestore.getInstance();          //inicia uma instância do Firestore

    private CollectionReference collref= banco.collection("Restaurantes");

    static ArrayList<Pratos> pratos= new ArrayList<Pratos>();                                         //pega o menu do restaurante clicado

    ArrayList<String> nome_pratos= new ArrayList<String>();                                         //lista que pega o nome dos pratos do menu dos restaurantes

    ArrayList<String> ingred= new ArrayList<String>();
    HashMap<String, String> pratos_menu= new HashMap<String, String>();
    String aux;

    @Override
    public void onBackPressed()
    {
        pratos.clear();
        nome_pratos.clear();
        ingred.clear();
        pratos_menu.clear();
        super.onBackPressed();  // optional depending on your needs
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.estabelecimento);
        ListView menu= (ListView)findViewById(R.id.menu);
        Log.d("TAG", "Entrou no Estabelecimento");


        for(Pratos p: pratos) {
            nome_pratos.add(p.prato);

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

        List<HashMap<String, String>> itens=new ArrayList<HashMap<String, String>>();
        SimpleAdapter simpleAdapter= new SimpleAdapter(this, itens, R.layout.item_lista,new String[]{"First Line", "Second Line"},new int[]{R.id.Text1, R.id.Text2});

        Iterator iterator= pratos_menu.entrySet().iterator();

        while(iterator.hasNext()){
            HashMap<String, String> resultado= new HashMap<>();
            Map.Entry pair= (Map.Entry) iterator.next();
            resultado.put("First Line", pair.getKey().toString());
            resultado.put("Second Line", pair.getValue().toString());
            itens.add(resultado);
        }

        menu.setAdapter(simpleAdapter);


        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });

    }



}
