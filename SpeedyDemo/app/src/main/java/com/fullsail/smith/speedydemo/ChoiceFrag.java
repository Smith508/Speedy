package com.fullsail.smith.speedydemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.speedy.Speedy;

import java.util.ArrayList;

/**

 This is where you can see Speedy in action. *** Pay ATTENTION to the LOGS - You can search .TAG to bring up all
 relevant logs ***

 - The Serializable button sends a Serializable object as an extra through an intent to a BroadcastReceiver.
 -- Speedy is checking throughout the process.

 - The Parcelable button sends a Parcelable object as an extra through an intent to a BroadcastReceiver.
 -- Speedy is checking throughout the process.
 *** A majority of the time you will notice that Parcelable completes faster than Serializable when being
 passed through intents ***
 .........................................................................................................

 *** Sometimes the loops don't finish so the finishTime() method in Speedy is never called. Simply tap the
 button again ***

 - The For Loop button starts a loop that runs 1000 times and finishes.

 - The For Each button starts a loop that iterates through an ArrayList that holds 1000 items.

 - The While Loop button starts a loop that iterates 1000 times and finishes.

 -- Speedy will show how long each loop took to complete

 **/

public class ChoiceFrag extends Fragment {

    public static final String TAG = "ChoiceFrag.TAGS";
    public static final String ACTION_SERIAL = "com.fullsail.smith.speedysample.ACTION_SERIAL";
    public static final String ACTION_PARCELABLE = "com.fullsail.smith.speedysample.ACTION_PARCELABLE";
    public static final String EXTRA_SERIAL = "com.fullsail.smith.speedysample.EXTRA_SERIAL";
    public static final String EXTRA_PARCELABLE = "com.fullsail.smith.speedysample.EXTRA_PARCELABLE";

    public static final String EXTRA_SPEEDY = "com.fullsail.smith.speedysample.EXTRA_SPEEDY";

    // Global Speedy object for the sake of this example, best declared on a method to method basis.
    private Speedy speedy;

    public static ChoiceFrag newInstance(){

        return new ChoiceFrag();
    }//END newInstance()

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Create Speedy object passing in the method name
        speedy = new Speedy("Sample Example: ");
    }//END onActivityCreated

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_choice_layout, container, false);

        final ArrayList<String> tempStrings = new ArrayList<>();
        final ArrayList<Integer> tempNums = new ArrayList<>();

        Button serial = (Button)view.findViewById(R.id.main_serializable_button);
        serial.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Serializable Intent to BroadcastReceiver", Toast.LENGTH_SHORT).show();

                // Create SerialData object
                SerialData data = new SerialData("Ryan Smith",22);

                // Populate arraylist of strings and nums 750 each
                for (int i = 0; i < 750; i++){

                    tempStrings.add(String.valueOf(i));
                    tempNums.add(i);
                }//END for int i = 0; i < 750; i++

                data.setNums(tempNums);
                data.setStrings(tempStrings);

                // Create intent to go to broadcast
                Intent serialBroadcast = new Intent(ACTION_SERIAL);
                serialBroadcast.putExtra(EXTRA_SERIAL,data);

                // Start Job before broadcast send
                speedy.startJob();

                // Update the method name with 'Serial Broadcast'
                speedy.updateMethodName("Serial Broadcast:");

                // Send regular broadcast
                getActivity().sendBroadcast(serialBroadcast);
            }//END onClick
        });//END serial.setOnClickListener

        final Button parcelable = (Button)view.findViewById(R.id.main_parcelable_button);
        parcelable.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Parcelable Intent to BroadcastReceiver", Toast.LENGTH_SHORT).show();

                // Create ParcelableData object
                ParcelableData data = new ParcelableData("Ryan Smith",23);

                // Populate arraylist of stirngs and nums 750 each
                for (int i = 0; i < 750; i++){

                    tempStrings.add(String.valueOf(i));
                    tempNums.add(i);
                }//END for int i = 0; i < 750; i++

                // TODO: Uncomment to show other finish method results
                //Speedy checkSpeedy = new Speedy("Just Checking");
                //checkSpeedy.startJob();
                data.setNums(tempNums);
                data.setStrings(tempStrings);
                // TODO: Uncomment to show other finish method results
                //checkSpeedy.checkDelta("setting nums and strings on data object");
                //checkSpeedy.finishTime();

                // Create intent to go to broadcast
                Intent parcelableBroadcast = new Intent(ACTION_PARCELABLE);
                parcelableBroadcast.putExtra(EXTRA_PARCELABLE,data);

                // Start job before broadcast send
                speedy.startJob();

                // Update the method name with 'Parcelable Broadcast'
                speedy.updateMethodName("Parcelable Broadcast:");

                // Send regular broadcast
                getActivity().sendBroadcast(parcelableBroadcast);

            }//END onClick
        });//END parcelable.setOnClickListener

        Button localBroadcastSerializbleButton = (Button)view.findViewById(R.id.main_local_broadcast_serializable_button);
        localBroadcastSerializbleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Local Broadcast Serializable Tapped", Toast.LENGTH_SHORT).show();

                // Create SerialData object
                SerialData data = new SerialData("Local Broadcast Serializable Example",101010);

                // Populate arraylist of strings and nums 750 each
                for (int i = 0; i < 750; i++){

                    tempStrings.add(String.valueOf(i));
                    tempNums.add(i);
                }//END for int i = 0; i < 750; i++

                // Set data
                data.setNums(tempNums);
                data.setStrings(tempStrings);

                // Create intent to pass to the local broadcast
                Intent serialBroadcast = new Intent(ACTION_SERIAL);
                serialBroadcast.putExtra(EXTRA_SERIAL,data);

                // Start job before local broadcast send
                speedy.startJob();

                // Update the method name with 'Serial Broadcast'
                speedy.updateMethodName("Local Serial Broadcast:");

                // Send local broadcast
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(serialBroadcast);
            }//END onClick
        });//END localSerialBroadcastButton.setOnClickListener

        Button localBroadcastParcelableButton = (Button)view.findViewById(R.id.main_local_broadcast_parcelable_button);
        localBroadcastParcelableButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "Local Broadcast Parcelable Tapped", Toast.LENGTH_SHORT).show();

                // Create ParcelableData object
                ParcelableData data = new ParcelableData("Local Broadcast Example",101010);

                // Populate arraylist of strings and nums 750 each
                for (int i = 0; i < 750; i++){

                    tempStrings.add(String.valueOf(i));
                    tempNums.add(i);
                }//END for int i = 0; i < 750; i++

                data.setNums(tempNums);
                data.setStrings(tempStrings);

                // Create intent to pass to the broadcast
                Intent parcelableBroadcast = new Intent(ACTION_PARCELABLE);
                parcelableBroadcast.putExtra(EXTRA_PARCELABLE,data);

                // Start job before local broadcast send
                speedy.startJob();

                // Update the method name with 'Parcelable Broadcast'
                speedy.updateMethodName("Local Parcelable Broadcast:");

                // Send local parcelable broadcast
                LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(parcelableBroadcast);
            }//END onClick

        });//END localBroadcastParcelableButton

        Button forLoopButton = (Button)view.findViewById(R.id.main_for_loop_button);
        forLoopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "For Loop button pressed", Toast.LENGTH_SHORT).show();

                // Creating Speedy object
                Speedy speedLogger = new Speedy("forLoopButton:onClick");

                // Start the job
                speedLogger.startJob();

                // loop through 750 times
                for (int i = 0; i < 750; i++){

                    Log.i(TAG, "onClick: i: " + i);
                }//END for int i = 0; i < 750; i++

                // Check how long it took to loop and print
                speedLogger.checkDelta("for loop finished looping 750 times");

                // Finish speedy
                speedLogger.finishTime();
            }//END onClick
        });// END forLoopButton.setOnClickListener

        Button forEachButton = (Button)view.findViewById(R.id.main_for_each_button);
        forEachButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "For Each button pressed", Toast.LENGTH_SHORT).show();

                // Create empty arraylist
                ArrayList<Integer> nums = new ArrayList<Integer>();

                // loop through 750 times
                for (int i = 0; i < 750; i++){

                    nums.add(i);
                }//END for int i = 0; i < 750; i++

                // Creating Speedy object
                Speedy speedLogger = new Speedy("forEachButton:onClick");

                // Start the job
                speedLogger.startJob();

                // loop through 750 times
                for (int num: nums) {

                    Log.i(TAG, "onClick: num: " + num);
                }//END for num : nums

                // Check how long it took to loop and print
                speedLogger.checkDelta("for each finished looping 750 times");

                // Finish speedy
                speedLogger.finishTime();
            }//END onClick
        });//END forEachButton.setOnClickListener

        Button whileButton = (Button)view.findViewById(R.id.main_while_loop_button);
        whileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getActivity(), "While Loop button pressed", Toast.LENGTH_SHORT).show();

                int i = 0;

                // Creating Speedy object
                Speedy speedLogger = new Speedy("whileLoopButton:onClick");

                // Start the job
                speedLogger.startJob();

                while (i < 750){

                    Log.i(TAG, "onClick: i: " + i);
                    i++;
                }//END while i < 750

                // Check how long it took to loop and print
                speedLogger.checkDelta("while loop finished looping 750 times");

                // Finish speedy
                speedLogger.finishTime();
            }//END onClick


        });//END whileButton.setOnClickListener

        // Return the view to be displayed
        return view;
    }//END onCreateView

    @Override
    public void onResume() {
        super.onResume();

        // Create intent filter
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SERIAL);
        filter.addAction(ACTION_PARCELABLE);

        // Register receivers
        getActivity().registerReceiver(mReceiver, filter);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver,filter);
    }//END onResume

    @Override
    public void onPause() {
        super.onPause();

        // Unregister Receivers
        getActivity().unregisterReceiver(mReceiver);

        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
    }//END onPause

    @Override
    public void onDestroy() {
        super.onDestroy();
    }//END onDestroy

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            speedy.checkDelta("Time check from broadcast send to onReceive hit");

            // Call start job because this is where I am beginning the check.
            speedy.startJob();
            if (intent.getAction().contentEquals(ChoiceFrag.ACTION_SERIAL) && intent.hasExtra(ChoiceFrag.EXTRA_SERIAL)){// Serial

                // Call checkDelta to demonstrate how long this if check took.
                speedy.checkDelta("BroadcastReceiver: Intent check for Serializable");

                // Start job for the reading of
                speedy.startJob();

                // Pull SerialData from intent
                SerialData data = (SerialData) intent.getSerializableExtra(ChoiceFrag.EXTRA_SERIAL);

                // Call checkDelta for the reading of the SerialData object from intent
                speedy.checkDelta("BroadcastReceiver: Time check for reading of Serializable object");

            } else if (intent.getAction().contentEquals(ChoiceFrag.ACTION_PARCELABLE) && intent.hasExtra(ChoiceFrag.EXTRA_PARCELABLE)){// Parcelable

                // Call checkDelta to demonstrate how long this if check took.
                speedy.checkDelta("BroadcastReceiver: Intent check for Parcelable");

                // Start job for the reading of
                speedy.startJob();

                // Pull ParcelableData from intent
                ParcelableData data = intent.getParcelableExtra(EXTRA_PARCELABLE);

                // Call checkDelta for the reading of the ParcelableData object from intent
                speedy.checkDelta("BroadcastReceiver: Time check for reading of Parcelable object");
            }//END intent.getAction().contentEquals(ChoiceFrag.ACTION_SERIAL) && intent.hasExtra(ChoiceFrag.EXTRA_SERIAL)

            // Finish the time
            speedy.finishTime();

            // This line is also for the sake of this example. This resets the global if need be, but as stated earlier
                // this is best used on a method to method basis
            speedy = new Speedy("Sample Example: ");
        }//END onReceive
    };//END mReceiver
}
