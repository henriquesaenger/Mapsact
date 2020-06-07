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
import com.google.android.material.navigation.NavigationView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    DrawerLayout drawer;
    ArrayList<Restaurante> restaurantes;
    static ArrayList<String> menu_rest;
    FirebaseFirestore banco = FirebaseFirestore.getInstance();





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
        restaurantes=locais(restaurantes);


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng user = new LatLng(location.getLatitude(), location.getLongitude());          //pega Latitude e longitude do usuário

                mMap.clear();                                                                       //limpa o mapa de marcadores para atualizar o marcador de usuário
                mMap.setMyLocationEnabled(true);
                for(Restaurante s: restaurantes){
                    if(s.Tipo=="Restaurante"){
                       mMap.addMarker(new MarkerOptions().position(new LatLng(s.latitude,s.longitude)).title(s.nome).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                    }
                    if(s.Tipo=="Café"){
                       mMap.addMarker(new MarkerOptions().position(new LatLng(s.latitude, s.longitude)).title(s.nome).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                    }
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(user, 15));                     //move a câmera para onde o usuário está

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

    public ArrayList<Restaurante> locais(ArrayList<Restaurante> restaurante){
        restaurante= new ArrayList<Restaurante>();
        Restaurante subway_bento=new Restaurante("Subway Bento", -31.7628906,-52.3345544, "Restaurante");
        subway_bento.addPrato("Salada de Frango", new String[]{"Tomate", "Azeitona", "Cebola", "Rúcula", "Pimentão", "Pepino", "Frango", "Alface"}, false, false);
        subway_bento.addPrato("Salada BMT", new String[]{"Tomate", "Azeitona", "Pepino","Pimentão", "Rúcula", "Presunto", "Salame", "Pepperoni", "Alface"}, false, false);
        restaurante.add(subway_bento);


        Restaurante padaria_Anchieta= new Restaurante("Padaria Anchieta", -31.7565992,-52.337372, "Café");
        padaria_Anchieta.addPrato("Suflê de Cenoura", new String[]{"Cenoura", "Cebola", "Leite de arroz", "Amido de Milho", "Margarina sem lactose"}, false, false);
        padaria_Anchieta.addPrato("Coxinha", new String[]{"Margarina sem lactose", "Batata", "Farinha de arroz", "Frango", "Cebola", "Tomate","Caldo de frango sem glúten"}, false, false);
        padaria_Anchieta.addPrato("Pão de queijo", new String[]{"Polvilho azedo", "Polvilho doce", "Batata", "Ovo", "Água", "Fermento"},false, true);
        restaurante.add(padaria_Anchieta);

        Restaurante griffos = new Restaurante("Griffos", -31.7549066,-52.336653, "Restaurante");
        griffos.addPrato("Massa a Carbonara", new String[]{"Massa sem glúten", "Bacon", "Cebola", "Alho", "Ovo", "Azeite de oliva", "Azeitona"}, false, false);
        griffos.addPrato("Mac & Cheese", new String[]{"Castanha de caju", "Alho", "Cebola", "Batata", "limão", "Macarrão" }, false, true);
        restaurante.add(griffos);

        Restaurante rodeio_pastelaria= new Restaurante("Pastelaria Rodeio", -31.7290896,-52.3438763, "Restaurante");
        rodeio_pastelaria.addPrato("Pastel Sem Graça", new String[]{"Massa de pastel", "Ovo", "Carne moída"}, false, true);
        rodeio_pastelaria.addPrato("Pastel de Vento", new String[]{"Massa de Pastel"}, false, true);
        restaurante.add(rodeio_pastelaria);

        Restaurante cachorro_do_jao= new Restaurante("Cachorro do Jão", -31.7320576,-52.3449867, "Restaurante");
        cachorro_do_jao.addPrato("Pancho Tradicional", new String[]{"Pão", "Salsicha Defumada", "Mostarda Agridoce"}, false, true);
        cachorro_do_jao.addPrato("Porção de Fritas", new String[]{"Batata"}, false, false);
        restaurante.add(cachorro_do_jao);

        Restaurante colosso_cohab= new Restaurante("Colosso Cohabpel", -31.7521214,-52.3368854, "Café");
        colosso_cohab.addPrato("Pão de soja", new String[]{"Ovo", "Polvilho doce", "Farinha de arroz", "Óleo de Soja", "Água", "Açúcar", "sal", "Fermento"}, false, false);
        colosso_cohab.addPrato("Salgadinho de Queijo", new String[]{"Batata", "Queijo", "Fécula de batata", "Frango"}, true, false);
        colosso_cohab.addPrato("Empadinha", new String[]{"Farinha de Arroz", "Polvilho doce", "Farinha de linhaça", "sal", "Manteiga ghee", "Ovo", "Água", "Cenoura", "Tomate", "Cebola", "Vagem", "Tofu"}, false, false);
        restaurante.add(colosso_cohab);

        Restaurante aveiro_padaria= new Restaurante("Padaria Aveiro", -31.757199,-52.3454862, "Café");
        aveiro_padaria.addPrato("Bolo de banana e cenoura", new String[]{"Banana", "Ovo", "Óleo de côco", "Fécula de batata", "Farinha de amêndoas", "Cenoura", "Vinagre de maçã", "Bicarbonato de sódio", "Mel", "Canela", "Gengibre"}, false, false);
        restaurante.add(aveiro_padaria);



        return restaurante;

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // When touch InfoWindow on the market, display another screen.
        Intent intent = new Intent(this, Estabelecimento.class);
        for(Restaurante r: restaurantes){
            if(marker.getTitle().equals(r.nome)){
                Estabelecimento.pratos=r.menurestaur;
            }
        }
        startActivity(intent);
    }


}



