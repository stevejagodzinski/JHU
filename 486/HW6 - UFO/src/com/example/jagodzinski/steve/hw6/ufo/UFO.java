package com.example.jagodzinski.steve.hw6.ufo;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class UFO extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ufo);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ufo, menu);
        return true;
    }
    
}
