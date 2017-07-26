package com.fullsail.smith.speedy;

/* Created by Ryan Smith */

import android.util.Log;

import java.util.ArrayList;

/**
 -  Speedy is a file to help with optimization and is meant to show you how long a method takes
 to complete.

 -- After constructing your speedy object and passing in the method name if there is one.

 -- You can start the timer by calling startJob().

 -- You can call checkDelta() whenever you want to mark a portion of the code you want to get timing info for.

 -  Each portion you want to record needs a startJob() call with a matching checkDelta.

 -- For instance say you have a pullData() method that pulled info from an api, parsed it and then stored
 it locally you could call startJob() before the api pull section of code and after that section of code call
 checkDelta(passing in job name ex: "api pull").

 -- You could then use this for the parsing and storing sections of code.

 - Speedy will record how long each job took to complete in milliseconds.

 - When all jobs you want to check within a method have been recorded call finishTime().

 -- This will log out how long each job took to complete as well as how long the whole bit
 of code you checked took from the first call to startJob()

 *** NOTE: Getting a 0 as a delta(change) doesn't necessarily mean the 'prevCurrent' variable and the 'current' variable match -
 -- it could just be so minuscule that it doesn't register as a long or a float changing.

 **/

class Speedy {

    private static final String TAG = "Speedy.TAG";
    private long begin; // Value is set with each call to startJob -- (beginning of individual job)
    private long delta; // Value is calculated in checkDelta()
    private long initial = -1; // Value is set with thte first call to startJob()
    private Boolean isInitialSet = false;
    private long prevCurrent = -1; // Value is set after current is set in checkDelta to ensure no duplicates
    private String methodName;
    private ArrayList<logtastic> logs; // With each call to checkDelta a new logtastic object is added. logs is displayed when finishTime() is called.

    // Screen draws every 16ms any work that takes longer blocks the thread and needs to be threaded
    private static final long SCREEN_DRAW_INTERVAL = 16;// 16ms

    // If work in an async task takes longer than 5ms try using another thread method
    private static final long CAN_USE_ASYNC_TASK_INTERVAL = 5;// 5ms

    // Pass in method name to be used when logging out times
    Speedy(String methodName) {

        this.methodName = methodName;
        logs = new ArrayList<>();

    }//END constructor

    ///*** Call startJob with each location you want to start timing, must end each job with checkDelta ***\\\
    void startJob(){

        if (!isInitialSet){// First call to startJob()

            // Set initial to the current time in milliseconds
            initial = System.currentTimeMillis();

            // Set isInitialSet to true ** Will set false when finished so initial remains != -1, as a result isInitialSet will never be set to true
            isInitialSet = true;

            // Set begin to initial's value
            begin = initial;


        } else {// isInitialSet is true -- initial time already recorded

            // Set start time of job to current time
            begin = System.currentTimeMillis();


        }//END if !isInitialSet


    }// END startJob


    ///*** Call checkDelta after you want to end the job started with startJob. Will add the timing info as well as the jobName to logs arraylist to be dipslayed later.
    void checkDelta(String jobName){

        // Reset delta
        delta = 0;

        // Grab current time
        long current = System.currentTimeMillis();

        if (prevCurrent == -1){// First check

            // Set prevCurrent to current
            prevCurrent = current;


        }//END prevCurrent == -1

        if (prevCurrent != current){// Not first check

            // Calculate the change with the current time of the check minus the prevCurrent
            delta = current - prevCurrent;

            // Set prevCurrent to current
            prevCurrent = current;


        } else {// prevCurrent == current

            // Set delta to zero because the time has not changed
            delta = current - begin;


        }//END if prevCurrent != current

        // Add to the logs Arraylist
        logs.add(new logtastic(jobName,delta));


    }//END checkDelta


    ///*** Call finish time when finished with chunk of code you are timing to display the max time as well as the job times ***\\\
    void finishTime(){

        // Reset values
        delta = 0;
        prevCurrent = 0;
        initial = -1;

        Log.i(TAG, ".............................................................................");

        Log.i(TAG, methodName);

        // Loop through logs and display the info
        for (logtastic log : logs) {

            // Log out the job info
            log.logInfo();

            // Increment delta with the log object's delta value
            delta = delta + log.getDelta();


        }//END for logtastic log : logs

        // Log out the total time
        Log.i(TAG, logs.size() + " tasks took total of " + delta + " ms to complete");

        Log.i(TAG, ".............................................................................");

        // Check async task interval
        if (delta > CAN_USE_ASYNC_TASK_INTERVAL){// More than 5ms to complete

            // Check screen draw interval
            if (delta > SCREEN_DRAW_INTERVAL){// Longer than 16ms to complete

                Log.i(TAG,"Tip: longer than " + SCREEN_DRAW_INTERVAL + "ms. Consider threading some of the work"  );


            } else {// Shorter than 16ms to complete

                Log.i(TAG, "Tip: took longer than " + CAN_USE_ASYNC_TASK_INTERVAL + "ms. Consider using handler thread or run a runnable\n     on a created thread for the background work" );


            }//END if delta > SCREEN_DRAW_INTERVAL


        } else { // delta <= CAN_USE_ASYNC_TASK INTERVAL

            Log.i(TAG,  "Tip: within " + CAN_USE_ASYNC_TASK_INTERVAL + "ms. If applicable consider use async task for the background work" );


        }//END if delta > CAN_USE_ASYNC_TASK_INTERVAL

        Log.i(TAG, ".............................................................................\n.");


    }//END finishTime()



    void updateMethodName(String methodName){

        this.methodName = methodName;

    }//END updateMethodName()



    private class logtastic {

        private final String jobName;
        private final long delta;

        logtastic(String jobName, long delta) {

            this.jobName = jobName;
            this.delta = delta;


        }//END constructor


        void logInfo(){

            Log.i(TAG, jobName + " took " + delta + " ms to complete");


        }//END logInfo


        long getDelta(){

            return this.delta;


        }//END getDelta


    }//END logstastic class


}
