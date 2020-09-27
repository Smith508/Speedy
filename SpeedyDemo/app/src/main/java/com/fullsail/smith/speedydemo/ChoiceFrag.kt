package com.fullsail.smith.speedydemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.smith.library.speedy.Speedy
import java.util.ArrayList

class ChoiceFrag : Fragment(), View.OnClickListener {

    val TAG = "ChoiceFrag.TAGS"

    val ACTION_SERIAL = "com.fullsail.smith.speedysample.ACTION_SERIAL"
    val ACTION_SERIAL_CUSTOM = "com.fullsail.smith.speedysample.ACTION_SERIAL_CUSTOM"
    val ACTION_PARCELABLE = "com.fullsail.smith.speedysample.ACTION_PARCELABLE"
    val EXTRA_SERIAL = "com.fullsail.smith.speedysample.EXTRA_SERIAL"
    val EXTRA_SERIAL_CUSTOM = "com.fullsail.smith.speedysample.EXTRA_SERIAL_CUSTOM"
    val EXTRA_PARCELABLE = "com.fullsail.smith.speedysample.EXTRA_PARCELABLE"

    val EXTRA_SPEEDY = "com.fullsail.smith.speedysample.EXTRA_SPEEDY"

    // Global SpeedyKt object for the sake of this example, best declared on a method to method basis.
    private var speedy: Speedy? = null

    fun newInstance(): ChoiceFrag {

        return ChoiceFrag()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_choice_layout, container, false)

        val tempStrings = ArrayList<String>()
        val tempNums = ArrayList<Int>()

        val serial = view.findViewById<Button>(R.id.main_serializable_button)
        val parcelable = view.findViewById<Button>(R.id.main_parcelable_button)
        val localBroadcastSerializbleButton = view.findViewById<Button>(R.id.main_local_broadcast_serializable_button)
        val localBroadcastParcelableButton = view.findViewById<Button>(R.id.main_local_broadcast_parcelable_button)
        val forLoopButton = view.findViewById<Button>(R.id.main_for_loop_button)
        val forEachButton = view.findViewById<Button>(R.id.main_for_each_button)
        val whileButton = view.findViewById<Button>(R.id.main_while_loop_button)
        val customSerializable = view.findViewById<Button>(R.id.main_custom_serialzable)
        val customLocalSerializable = view.findViewById<Button>(R.id.main_local_custom_serializable)

        serial.setOnClickListener(this)
        parcelable.setOnClickListener(this)
        localBroadcastSerializbleButton.setOnClickListener(this)
        localBroadcastParcelableButton.setOnClickListener(this)
        forLoopButton.setOnClickListener(this)
        forEachButton.setOnClickListener(this)
        whileButton.setOnClickListener(this)
        customSerializable.setOnClickListener(this)
        customLocalSerializable.setOnClickListener(this)

        return view
    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter()
        filter.addAction(ACTION_SERIAL)
        filter.addAction(ACTION_PARCELABLE)
        filter.addAction(ACTION_SERIAL_CUSTOM)

        // Register receivers
        activity?.let {

            it.registerReceiver(mReceiver, filter)
            LocalBroadcastManager.getInstance(it).registerReceiver(mReceiver, filter)
        }

        test()
        final()
        a()
    }

    override fun onPause() {
        super.onPause()

        activity?.let {
            // Unregister Receivers
            it.unregisterReceiver(mReceiver)

            LocalBroadcastManager.getInstance(it).unregisterReceiver(mReceiver)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private var mReceiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {

            speedy!!.checkDelta("Time check from broadcast send to onReceive hit")

            // Call start job because this is where I am beginning the check.
            speedy!!.startJob()
            if (intent.action!!.contentEquals(ACTION_SERIAL)
                    && intent.hasExtra(EXTRA_SERIAL)) {// Serial

                // Call checkDelta to demonstrate how long this if check took.
                speedy!!.checkDelta("BroadcastReceiver: Intent check for Serializable")

                // Start job for the reading of
                speedy!!.startJob()

                // Pull SerialData from intent
                val data = intent.getSerializableExtra(EXTRA_SERIAL) as SerialData

                // Call checkDelta for the reading of the SerialData object from intent
                speedy!!.checkDelta("BroadcastReceiver: Time check for reading of Serializable object")

            } else if (intent.action!!.contentEquals(ACTION_PARCELABLE)
                    && intent.hasExtra(EXTRA_PARCELABLE)) {// Parcelable

                // Call checkDelta to demonstrate how long this if check took.
                speedy!!.checkDelta("BroadcastReceiver: Intent check for Parcelable")

                // Start job for the reading of
                speedy!!.startJob()

                // Pull ParcelableData from intent
                val data = intent.getParcelableExtra<ParcelableData>(EXTRA_PARCELABLE)

                // Call checkDelta for the reading of the ParcelableData object from intent
                speedy!!.checkDelta("BroadcastReceiver: Time check for reading of Parcelable object")

            } else if (intent.action!!.contentEquals(ACTION_SERIAL_CUSTOM)
                    && intent.hasExtra(EXTRA_SERIAL_CUSTOM)) {// Custom Serializable

                // Call checkDelta to demonstrate how long this if check took.
                speedy!!.checkDelta("BroadcastReceiver: Intent check for custom serialization")

                // Start job for the reading of
                speedy!!.startJob()

                // Pull CustomSerial from intent
                val data = intent.getSerializableExtra(EXTRA_SERIAL_CUSTOM) as CustomSerial

                // Call checkDelta for the reading of the ParcelableData object from intent
                speedy!!.checkDelta("BroadcastReceiver: Time check for reading of Custom Serializable object")
            }

            // Finish the time
            speedy!!.finishTime()

            // This line is also for the sake of this example. This resets the global if need be, but as stated earlier
            // this is best used on a method to method basis
            speedy = Speedy("Sample Example: ")
        }
    }
    fun readData() {
        Log.d(TAG, "readData()")
    }
    fun filterList() {
        Log.d(TAG, "filterList()")
    }
    fun saveData() {
        Log.d(TAG, "saveData()")
    }
    fun loadView() {
        Log.d(TAG, "loadView()")
    }



    fun test() {

        Speedy("test").apply {

            startJob()
            readData()
            checkDelta("read data")

            startJob()
            filterList()
            checkDelta("filter list")

            startJob()
            saveData()
            checkDelta("save data")

            startJob()
            loadView()
            checkDelta("load view")
        }
    }


    fun final() {

        Speedy("final").apply {

            clock("read data") {

                readData()
            }

            clock("filter list") {

                filterList()
            }

            clock("save data") {

                saveData()
            }

            clockFinish("load view") {

                loadView()
            }
        }
    }

    fun a() {
        Speedy("final").apply {

            mapped(mapOf("read data" to { readData() }
                    , "filter list" to { filterList() }
                    , "save data" to { saveData() }
                    , "load view" to { loadView() }
            ))
        }
    }

    override fun onClick(v: View?) {

        when(v!!.id) {

            R.id.main_serializable_button -> {

                val tempStrings = ArrayList<String>()
                val tempNums = ArrayList<Int>()

                Toast.makeText(activity, "Serializable Intent to BroadcastReceiver", Toast.LENGTH_SHORT).show()

                // Create SerialData object
                val data = SerialData("Ryan Smith", 22)

                // Populate arraylist of strings and nums 750 each
                for (i in 0..749) {

                    tempStrings.add(i.toString())
                    tempNums.add(i)
                }

                data.nums = tempNums
                data.strings = tempStrings

                // Create intent to go to broadcast
                val serialBroadcast = Intent(ACTION_SERIAL)
                serialBroadcast.putExtra(EXTRA_SERIAL, data)

                // Start Job before broadcast send
                speedy!!.startJob()

                // Update the method name with 'Serial Broadcast'
                speedy!!.updateMethodName("Serial Broadcast:")

                // Send regular broadcast
                activity?.let {
                    it.sendBroadcast(serialBroadcast)
                }
            }

            R.id.main_parcelable_button -> {

                val tempStrings = ArrayList<String>()
                val tempNums = ArrayList<Int>()

                Toast.makeText(activity, "Parcelable Intent to BroadcastReceiver", Toast.LENGTH_SHORT).show()

                // Create ParcelableData object
                val data = ParcelableData("Ryan Smith", 23)

                // Populate arraylist of stirngs and nums 750 each
                for (i in 0..750) {

                    tempStrings.add(i.toString())
                    tempNums.add(i)
                }//END for int i = 0; i < 750; i++

                // TODO: Uncomment to show other finish method results
                //SpeedyKt checkSpeedy = new SpeedyKt("Just Checking");
                //checkSpeedy.startJob();
                data.nums = tempNums
                data.strings = tempStrings
                // TODO: Uncomment to show other finish method results
                //checkSpeedy.checkDelta("setting nums and strings on data object");
                //checkSpeedy.finishTime();

                // Create intent to go to broadcast
                val parcelableBroadcast = Intent(ACTION_PARCELABLE)
                parcelableBroadcast.putExtra(EXTRA_PARCELABLE, data)

                // Start job before broadcast send
                speedy!!.startJob()

                // Update the method name with 'Parcelable Broadcast'
                speedy!!.updateMethodName("Parcelable Broadcast:")

                // Send regular broadcast
                activity?.let {
                    it.sendBroadcast(parcelableBroadcast)
                }
            }
        }

        when (v.id) {

            /*
            R.id.main_serializable_button.run {


            }

            R.id.main_parcelable_button run {

            }

            R.id.main_local_broadcast_serializable_button run {

                val tempStrings = ArrayList<String>()
                val tempNums = ArrayList<Int>()

                Toast.makeText(activity, "Local Broadcast Serializable Tapped", Toast.LENGTH_SHORT).show()

                // Create SerialData object
                val data = SerialData("Local Broadcast Serializable Example", 101010)

                // Populate arraylist of strings and nums 750 each
                for (i in 0..749) {

                    tempStrings.add(i.toString())
                    tempNums.add(i)
                }//END for int i = 0; i < 750; i++

                // Set data
                data.nums = tempNums
                data.strings = tempStrings

                // Create intent to pass to the local broadcast
                val serialBroadcast = Intent(ACTION_SERIAL)
                serialBroadcast.putExtra(EXTRA_SERIAL, data)

                // Start job before local broadcast send
                speedy.startJob()

                // Update the method name with 'Serial Broadcast'
                speedy.updateMethodName("Local Serial Broadcast:")

                // Send local broadcast
                LocalBroadcastManager.getInstance(activity!!).sendBroadcast(serialBroadcast)
                break
            }

            R.id.main_local_broadcast_parcelable_button run {

                val tempStrings = ArrayList<String>()
                val tempNums = ArrayList<Int>()

                Toast.makeText(activity, "Local Broadcast Parcelable Tapped", Toast.LENGTH_SHORT).show()

                // Create ParcelableData object
                val data = ParcelableData("Local Broadcast Example", 101010)

                // Populate arraylist of strings and nums 750 each
                for (i in 0..749) {

                    tempStrings.add(i.toString())
                    tempNums.add(i)
                }//END for int i = 0; i < 750; i++

                data.nums = tempNums
                data.strings = tempStrings

                // Create intent to pass to the broadcast
                val parcelableBroadcast = Intent(ACTION_PARCELABLE)
                parcelableBroadcast.putExtra(EXTRA_PARCELABLE, data)

                // Start job before local broadcast send
                speedy.startJob()

                // Update the method name with 'Parcelable Broadcast'
                speedy.updateMethodName("Local Parcelable Broadcast:")

                // Send local parcelable broadcast
                LocalBroadcastManager.getInstance(activity!!).sendBroadcast(parcelableBroadcast)
                break
            }

            R.id.main_while_loop_button run {

                Toast.makeText(activity, "While Loop button pressed", Toast.LENGTH_SHORT).show()

                var i = 0

                // Creating SpeedyKt object
                val speedLogger = Speedy("whileLoopButton:onClick")

                // Start the job
                speedLogger.startJob()

                while (i < 750) {

                    Log.i(TAG, "onClick: i: $i")
                    i++
                }//END while i < 750

                // Check how long it took to loop and print
                speedLogger.checkDelta("while loop finished looping 750 times")

                // Finish speedy
                speedLogger.finishTime()
                break
            }

            R.id.main_for_each_button run {

                val speedyKt = SpeedyKt("ForEachButton:onClick KOTLIN")

                Toast.makeText(activity, "For Each button pressed", Toast.LENGTH_SHORT).show()

                // Create empty arraylist
                val nums = ArrayList<Int>()

                // loop through 750 times
                for (i in 0..749) {

                    nums.add(i)
                }//END for int i = 0; i < 750; i++

                // Creating Speedy object
                val speedLogger = Speedy("forEachButton:onClick")

                // Start the job
                speedLogger.startJob()
                speedyKt.startJob()

                // loop through 750 times
                for (num in nums) {

                    Log.i(TAG, "onClick: num: $num")
                }//END for num : nums

                // Check how long it took to loop and print
                speedLogger.checkDelta("for each finished looping 750 times")
                speedyKt.checkDelta("for each finished looping 750 times")

                // Finish speedy
                speedLogger.finishTime()
                speedyKt.finishTime()
                break
            }

            R.id.main_for_loop_button run {

                Toast.makeText(activity, "For Loop button pressed", Toast.LENGTH_SHORT).show()

                // Creating SpeedyKt object
                val speedLogger = Speedy("forLoopButton:onClick")

                // Start the job
                speedLogger.startJob()

                // loop through 750 times
                for (i in 0..149) {

                    Log.i(TAG, "onClick: i: $i")
                }//END for int i = 0; i < 750; i++

                // Check how long it took to loop and print
                speedLogger.checkDelta("for loop finished looping 750 times")

                // Finish speedy
                speedLogger.finishTime()
                break
            }

            //case R.id.mainser
            else
            break
             */
        }
    }
}