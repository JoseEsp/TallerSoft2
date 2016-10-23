package com.jose.movilizateucn;

import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ChoferActivity extends AppCompatActivity {

    private ListView navMenu;
    private ArrayAdapter<String> navMenuAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chofer);
        configureNavMenu();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mActivityTitle = getTitle().toString();
    }

    private void configureNavMenu(){
        navMenu = (ListView) findViewById(R.id.navMenu);
        navMenuAdapter = new ArrayAdapter<String>(this,
                R.layout.support_simple_spinner_dropdown_item ,menuOptions());
        navMenu.setAdapter(navMenuAdapter);
    }

    private String[] menuOptions(){
        String[] menuOptions = {
                "Perfil",
                "Historial deh viajes",
                "Ser Pasajero"
        };
        return menuOptions;
    }

}
