package com.fullsail.smith.speedydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){

            ChoiceFrag frag = ChoiceFrag.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.activity_main,frag,ChoiceFrag.TAG).commit();


        }//END if savedInstanceState == null


    }//END onCreate


}
