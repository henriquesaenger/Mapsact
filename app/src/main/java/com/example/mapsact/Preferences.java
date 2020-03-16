package com.example.mapsact;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public class Preferences extends FragmentActivity {

    private Button save;
    private Switch lactose;
    private Switch gluten;

    public static final String SHARED_PREFS= "sharedPrefs";
    public static final String LACT="Lactose";
    public static final String GLUT="Gluten";

    private Boolean switchlactose;
    private Boolean switchgluten;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_preferences);


        lactose = (Switch) findViewById(R.id.lactoseSwitch);
        gluten = (Switch) findViewById(R.id.glutenSwitch);
        save=(Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        loadData();
        update();

    }

    public void saveData(){
        SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.putBoolean(LACT, lactose.isChecked());
        editor.putBoolean(GLUT, gluten.isChecked());
        editor.apply();

        Toast.makeText(this, "Preferências Salvas", Toast.LENGTH_SHORT).show();
    }

    public void loadData(){
        SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchlactose= sharedPreferences.getBoolean(LACT, false);
        switchgluten= sharedPreferences.getBoolean(GLUT, false);
    }


    public void update(){
        lactose.setChecked(switchlactose);
        gluten.setChecked(switchgluten);
    }
}
