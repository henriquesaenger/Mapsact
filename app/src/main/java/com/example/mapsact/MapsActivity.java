package com.example.mapsact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.parse.Parse;

import java.util.ArrayList;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    DrawerLayout drawer;

    ArrayList<Restaurante> restaurantes= new ArrayList<Restaurante>();
    static ArrayList<Pratos> pratos= new ArrayList<Pratos>();
    FirebaseFirestore banco = FirebaseFirestore.getInstance();          //inicia uma instância do Firestore

    CollectionReference collref= banco.collection("Restaurantes");





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }





    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here
        Parse.initialize(new Parse.Configuration.Builder(getApplicationContext())
                .applicationId("67ccd4f784bad8a497b81c6937a9ac4043d5b583")
                .clientKey("b3dadb3ff33c924083c9ac5fbe7ab021d32d3a45")
                .server("http://52.14.151.81:80/parse/")
                .build()
        );


        Toolbar toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView =(NavigationView) findViewById(R.id.nav_view);
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){

                    case R.id.preferencias:
                        Intent pref = new Intent(MapsActivity.this, Preferences.class);
                        startActivity(pref);
                        break;
                    case R.id.recomendacao:
                        Intent rec= new Intent(MapsActivity.this, Recommendation.class);
                        startActivity(rec);
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,toolbar , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



    }



    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        mMap.setOnInfoWindowClickListener(this);
        loadData();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng user = new LatLng(location.getLatitude(), location.getLongitude());          //pega Latitude e longitude do usuário
                mMap.clear();                                                                       //limpa o mapa de marcadores para atualizar o marcador de usuário
                mMap.setMyLocationEnabled(true);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 15));                     //move a câmera para onde o usuário está



                for(Restaurante s: restaurantes){
                    LatLng x= new LatLng(s.latitude, s.longitude);
                    if(s.tipo.equals("Restaurante")){
                       mMap.addMarker(new MarkerOptions().position(x).title(s.estabelecimento).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    }
                    else{
                        if(s.tipo.equals("Café")){
                            mMap.addMarker(new MarkerOptions().position(new LatLng(s.latitude, s.longitude)).title(s.estabelecimento).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                        }

                    }
                }


                //restaurantes.clear();




            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT < 23){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else{
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5, locationListener);
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //pedir permissão
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            //já temos permissão
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 5, locationListener);
        }
    }



    @Override
    public void onInfoWindowClick(Marker marker) {
        // When touch InfoWindow on the market, display another screen.
        Intent intent = new Intent(this, Estabelecimento.class);
        loadPratos(marker);
        Log.d("TAG", "passou do loadPratos(marker)");
        /*final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Write whatever to want to do after delay specified (1 sec)
                Log.d("Handler", "Running Handler");
            }
        }, 1000);*/

        startActivity(intent);


    }



    public void loadData(){
        Query query= banco.collection("Restaurantes");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        /*final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Write whatever to want to do after delay specified (1 sec)
                                Log.d("Handler", "Running Handler");
                            }
                        }, 1000);*/
                        Restaurante x= new Restaurante();
                        x.estabelecimento= document.getString("Estabelecimento");
                        x.haveglutenopt=document.getBoolean("GlutenFO");
                        x.havelactoseopt=document.getBoolean("LactoseFO");
                        x.ID= document.getDouble("ID");
                        x.tipo=document.getString("Tipo");
                        x.longitude=document.getGeoPoint("Localizacao").getLongitude();
                        x.latitude=document.getGeoPoint("Localizacao").getLatitude();
                        restaurantes.add(x);
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void loadPratos(Marker marker){
        Log.d("TAG", "restaurantes"+this.restaurantes.toString());
        Log.d("TAG", "pratos"+Estabelecimento.pratos.toString());
        for(Restaurante r: restaurantes){
            Log.d("TAG", "Entrou no for de load pratos");
            if(marker.getTitle().equals(r.estabelecimento)){
                Log.d("TAG", "entrou no if de load pratos"+"Nome:"+r.estabelecimento);
                banco.collectionGroup("Pratos").whereEqualTo("IDRest", r.ID).get(Source.CACHE)
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "entrou no if do On complete");
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        /*final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                //Write whatever to want to do after delay specified (1 sec)
                                                Log.d("Handler", "Running Handler");
                                            }
                                        }, 1000);*/
                                        Log.d("TAG", "Pratos:"+document.getData());
                                        Map<String, Object> map = document.getData();
                                        //Log.d("TAG", document.toString());
                                        Pratos p= new Pratos();
                                        p.ID=document.getId();
                                        p.havegluten=document.getBoolean("Gluten");
                                        p.havelactose=document.getBoolean("Lactose");
                                        p.IDR=document.getDouble("IDRest");
                                        p.prato=document.getString("Nome");
                                        p.ingredientes=(ArrayList<String>)map.get("Ingredientes");
                                        pratos.add(p);
                                        Log.d("TAG", "Verificação Mapsact do Pratos dentro do Loop"+p.toString());
                                        Log.d("TAG", "Verificação Mapsact do Pratos dentro do Loop"+pratos.toString());

                                        //if(p.ID==3.0|| p.ID==6.0){
                                        //    Log.d("TAG", "Prato:"+p.prato+"\nGluten"+p.havegluten+"\nLactose"+p.havelactose+"\nIDRest:"+p.ID+"\nIngredientes"+p.ingredientes);
                                        //}

                                    }
                                } else {
                                    Log.d("TAG", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                Log.d("TAG", "Verificação Mapsact do Pratos Mapsact"+this.pratos.toString());
                Estabelecimento.pratos.clear();
                Estabelecimento.pratos.addAll(this.pratos);
                Log.d("TAG", "Verificação MapsAct do Pratos estabelecimento"+Estabelecimento.pratos.toString());
                //pratos.clear();
                Log.d("TAG", "Pratos:"+pratos.toString());
                //pratos.clear();  // aqui pegava todos os restaurantes menos 2

            }
            else{
                Log.d("TAG", "não deu");
            }

        }

    }








}



