package com.smith.library.speedy

import android.util.Log

/**
 * * **Speedy** is an optimization tool and is meant to show you how long a method takes to complete.
 *
 * * After constructing your Speedy object and passing in the method name you can start the timer by
 * calling **startJob()**.
 *
 * * Call **checkDelta()** whenever you want to mark a portion of the code you want to get timing info for.
 *
 * * **Note**: Each portion you want to record needs a **startJob()** call with a matching **checkDelta()**.
 *     - For instance say you have a pullData() method that pulled info from an api, parsed it and
 *     then stored it locally you could call **startJob()** before the api pull section of code and after
 *     that section of code call **checkDelta**(passing in job name ex: "api pull").
 *
 * * Speedy will record how long each job took to complete in **milliseconds**.
 *
 * * When all jobs you want to check within a method have been recorded call **finishTime()**.
 *     - This will log out how long each job took to complete as well as how long the whole bit of code
 *     you checked took from the first call to **startJob()**.
 *
 * * Easily search for Speedy's logs by using '**TAGS**' as the log search criteria.
 *
 * * **Note**: Getting a **0** as a **delta**(change) doesn't necessarily mean the '**prevCurrent**' variable and the
 *     '**current**' variable match it could just be so minuscule that it doesn't register as a long or a float changing.
 *
 * @param methodName
 * @author Ryan Smith
 **/
class Speedy(private var methodName: String) {

    private var begin: Long = 0L // Value is set with each call to startJob().
    private var delta: Long = 0L // Value is calculated in checkDelta().
    private var initial: Long = -1L // Value is set with the first call to startJob().
    private var isInitialSet: Boolean = false
    private var prevCurrent: Long = -1L // Value is set after current is set in checkDelta to ensure no duplicates
    private val logs: MutableList<Logtastic> = arrayListOf() // With each call to checkDelta a new Logtastic object is added. logs are displayed when finishTime() is called.

    // State flags
    private var didStart = false
    private var didCheck = false

    /**
     * Call startJob with each location you want to start timing,
     * must end each job with 'checkDelta()' call
     *
     * @throws IllegalStateException
     */
    @Throws(IllegalStateException::class)
    fun startJob() {

        // Check state -> See if we called start before
        if (didStart) {

            if (didCheck) {// Has checked -> Starting another job set

                // Clear state flags
                didStart = false
                didCheck = false

            } else {// Trying to call start before ending the previous job with a check.

                throw IllegalStateException("Attempting to call startJob() when a previous call to " +
                        "startJob() had been made without a subsequent call to checkDelta().")
            }
        }

        if (!isInitialSet) {// First call to startJob()

            // Set initial to the current time in milliseconds
            initial = System.currentTimeMillis()

            // Set isInitialSet to true ** Will set to 'false' when finished so initial remains != -1,
            // as a result isInitialSet will never be set to true.
            isInitialSet = true

            // Set begin to initial's value
            begin = initial

        } else {// isInitialSet is true -- initial time already recorded

            // Set start time of job to current time
            begin = System.currentTimeMillis()
        }

        didStart = true
    }

    /**
     * Call checkDelta after you want to end the job started with startJob. Will add the timing
     * info as well as the jobName to logs arrayList to be displayed later
     *
     * @throws IllegalStateException
     * @param jobName
     */
    @Throws(IllegalStateException::class)
    fun checkDelta(jobName: String) {

        // Check state
        if (!didStart) throw IllegalStateException("Attempting to call checkDelta() before making a " +
                "call to startJob()")

        // Reset delta
        delta = 0

        // Grab current time
        val current = System.currentTimeMillis()

        if (prevCurrent == -1L) {// First check

            // Set prevCurrent to current
            prevCurrent = current
        }

        if (prevCurrent != current) {// Not first check

            // Calculate the change with the current time of the check minus the prevCurrent
            delta = current - prevCurrent

            // Set prevCurrent to current
            prevCurrent = current

        } else {// prevCurrent == current

            // Set delta to zero because the time has not changed
            delta = current - begin
        }

        // Add to the logs Arraylist
        logs.add(Logtastic(jobName, delta))
        didCheck = true
    }

    /**
     * Call finish time when finished with chunk of code you are timing to display the max time as
     * well as the job times.
     *
     * @throws IllegalStateException
     */
    @Throws(IllegalStateException::class)
    fun finishTime() {

        if (!isInitialSet) throw IllegalStateException("Attempting to call finishTime() before a call")

        // Reset values
        delta = 0
        prevCurrent = 0
        initial = -1L

        Log.d(TAG, logSeparatorStart)
        Log.d(TAG, " ")
        Log.d(TAG, methodName)

        // Loop through logs and display the info
        for (log in logs) {

            // Log out the job info
            log.logInfo()

            // Increment delta with the log object's delta value
            delta += log.delta
        }

        // Log out the total time
        Log.d(TAG, "${logs.size} tasks took total of $delta ms to complete")
        Log.d(TAG, " ")
        Log.d(TAG, logSeparator)

        // Check if the slowness is perceptible to the user
        when  {
            delta in SLOWNESS_PERCEPTIBLE_RANGE  -> Log.d(TAG, "Note: The slowness is " +
                    "perceptible to the user.")

            delta >= SLOWNESS_PERCEPTIBLE_MAX -> Log.d(TAG, "Note: The slowness is perceptible " +
                    "to the user and has exceeded the 100ms-200ms starting threshold.")

            delta <= CAN_USE_ASYNC_TASK_INTERVAL -> Log.d(TAG, "Tip: within $CAN_USE_ASYNC_TASK_INTERVAL ms. " +
                    "If applicable consider use async task for the background work")

            delta > SCREEN_DRAW_INTERVAL -> Log.d(TAG, "Tip: longer than $SCREEN_DRAW_INTERVAL ms. " +
                    "Consider threading some of the work")

            delta in CAN_USE_ASYNC_TASK_INTERVAL..SCREEN_DRAW_INTERVAL -> Log.d(TAG, "Tip: took longer than $CAN_USE_ASYNC_TASK_INTERVAL ms. Consider using " +
                    "handler thread or run a runnable on a created thread for the background work.")
        }

        Log.d(TAG, " ")
        Log.d(TAG, logSeparatorFinish)
    }

    fun updateMethodName(methodName: String) {

        this.methodName = methodName
    }

    /**
     * Wraps the startJob() & checkDelta() logic allowing you to pass the jobName for checkDelta()
     * call.
     *
     * Ex:
     *
     * clock("Filtering List") {
     *      list.filter()...
     * }
     *
     * @param jobName Name of the job. Used in the checkDelta() call to track the work.
     * @param block Function block you'd like to track.
     *
     * @return Optional Exception
     */
    inline fun<R> clock(jobName: String, block: () -> R): Exception? {

        return try {

            startJob()
            block()
            checkDelta(jobName)

            null

        } catch (e: Exception) {

            e
        }
    }

    /**
     * Wraps the startJob() & checkDelta() logic allowing you to pass the jobName for checkDelta()
     * call.
     *
     * Uses an 'Express<R>' parameter which wraps the function block and holds the block name provided
     * by the user.
     *
     * Ex:
     * clock(Express("Filtering List" { filterList() }))
     *
     * @param express 'Express<R>' Function block you'd like to track.
     *
     * @return Optional Exception
     */
    inline fun<R> clock(express: Express<R>): Exception? {

        return try {

            startJob()
            express()
            checkDelta(express.blockName)

            null

        } catch (e: Exception) {

            e
        }
    }

    /**
     * Wraps the startJob() & checkDelta() logic allowing you to pass the jobName for checkDelta()
     * call. Finally, finishTime() is called after the call to checkDelta().
     *
     * Best used for the last item of work you would like to track in a function.
     *
     * Ex:
     *
     * clockFinish("Filtering List") {
     *      list.filter()...
     * }
     *
     * @param jobName Name of the job. Used in the checkDelta() call to track the work.
     * @param block Function block you'd like to track.
     *
     * @return Optional Exception
     */
    inline fun<R> clockFinish(jobName: String, block: () -> R): Exception? {

        return try {

            startJob()
            block()
            checkDelta(jobName)
            finishTime()

            null

        } catch (e: Exception) {

            e
        }
    }

    /**
     * Wraps the startJob() & checkDelta() logic allowing you to pass the jobName for checkDelta()
     * call. Finally, finishTime() is called after the call to checkDelta().
     *
     * Best used for the last item of work you would like to track in a function.
     *
     * Uses an 'Express<R>' parameter which wraps the function block and holds the block name provided
     * by the user.
     *
     * Ex:
     * clockFinish(Express("Filtering List" { filterList() }))
     *
     * @param express 'Express<R>' Function block you'd like to track.
     *
     * @return Optional Exception
     */
    inline fun<R> clockFinish(express: Express<R>): Exception? {

        return try {

            startJob()
            express()
            checkDelta(express.blockName)
            finishTime()

            null

        } catch (e: Exception) {

            e
        }
    }

    fun<R> mapped(map: Map<String, () -> R>) {

        val lastKey = map.keys.last()
        map.forEach {
           // Log.d(TAG, "f: TESTING::: () -> R.value.javaClass.enclosingMethod $${it.value.javaClass.enclosingMethod.name}")
            if (!it.key.contentEquals(lastKey)) clock(it.key, it.value) else clockFinish(it.key, it.value)
        }
    }

    fun<R> expressMapped(map: Map<String, Express<R>>) {

        val lastKey = map.keys.last()
        map.forEach {
            Log.d(TAG, "express() blockName: ${it.value.blockName}")
            if (!it.key.contentEquals(lastKey)) clock(it.key, it.value) else clockFinish(it.key, it.value)
        }
    }

    fun<R> express(list: List<Express<R>>) {

        list.forEachIndexed { index, express ->
            if (index != list.lastIndex) clock(express) else clockFinish(express)
        }
    }

    private inner class Logtastic internal constructor(private val jobName: String, val delta: Long) {

        fun logInfo() {

            Log.d(TAG, "$jobName took $delta ms to complete")
        }
    }

    private companion object {

        private val TAG = "Speedy.TAGS"

        // User's can start to perceive slowness in an app in the 100-200ms range
        private const val SLOWNESS_PERCEPTIBLE_MIN = 100L
        private const val SLOWNESS_PERCEPTIBLE_MAX = 200L
        private val SLOWNESS_PERCEPTIBLE_RANGE = SLOWNESS_PERCEPTIBLE_MIN..SLOWNESS_PERCEPTIBLE_MAX

        // Screen draws every 16ms any work that takes longer blocks the thread and needs to be threaded
        private const val SCREEN_DRAW_INTERVAL: Long = 16L // 16ms

        // If work in an async task takes longer than 5ms try using another thread method
        private const val CAN_USE_ASYNC_TASK_INTERVAL: Long = 5L // 5ms

        private const val logSeparatorStart = "........................................Speedy START........................................"
        private const val logSeparator = "................................................................................"
        private const val logSeparatorFinish = "........................................Speedy FINISHED........................................"
    }
}