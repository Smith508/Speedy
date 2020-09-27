package com.fullsail.smith.speedydemo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.smith.library.speedy.Speedy;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){

            //ChoiceFrag frag = ChoiceFrag.newInstance();
            //getSupportFragmentManager().beginTransaction().replace(R.id.activity_main,frag,ChoiceFrag.TAG).commit();
        }

        TestUtil.INSTANCE.expressSimple();
        TestUtil.INSTANCE.testSealed();
        TestUtil.INSTANCE.testIntStates();

    }//END onCreate


    private void calc() {

        float loan = 110000.0f;
        //float loan = 30000.0f;
        float rate = 0.07f;
        //float rate = 0.05f;
        float daysInYear = 365.0f;
        float daysInMonth = 30.0f;
        float payment = 1147.86f + 343.0f + 343.0f;
        //float payment = 343.0f * 2.0f;

        int numYears = 10;
        int numMonths = 12;

        float dailyInterest = rate / daysInYear;
        float monthlyInterest = dailyInterest * daysInMonth;
        float paymentMinutesInterest = payment - (loan * monthlyInterest);

        Log.i("CALCS", "calc: payment minus interest " + paymentMinutesInterest + "\n");

        for (int i = 0; i < numYears; i++) {

            Log.i("CALCS", "\ncalc: year: " + i);

            for (int x = 0; x < numMonths; x++) {

                Log.i("CALCS", "calc: month: " + x + "\n");

                float minusInterest = loan - monthlyInterest;
                Log.i("CALCS", "calc: loan minus interest: " + minusInterest);

                loan = minusInterest - paymentMinutesInterest;
                Log.i("CALCS", "calc: Loan at END OF MONTH: " + loan);

            }
        }

    }
}
