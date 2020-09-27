package com.fullsail.smith.speedydemo

import android.util.Log
import com.smith.library.speedy.Express
import com.smith.library.speedy.Speedy

object TestUtil {

    private const val TAG = "TestUtil"

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
            finishTime()
        }
    }


    fun clocked() {

        Speedy("clocked()").apply {

            // Clock each task
            clock("read data") {

                readData()
            }

            clock("filter list") {

                filterList()
            }

            clock("save data") {

                saveData()
            }

            // Clock & Finish
            clockFinish("load view") {

                loadView()
            }
        }
    }

    fun mapped() {

        Speedy("mapped()").apply {

            mapped(mapOf("read data" to { readData() }
                    , "filter list" to { filterList() }
                    , "save data" to { saveData() }
                    , "load view" to { loadView() }
            ))
        }
    }

    fun express() {

        Speedy("express()").apply {

            expressMapped(mapOf("read data" to Express("readData") { readData() }
                    , "filter data" to Express("filterList") { filterList() }
                    , "save data" to Express("saveData") { saveData() }
                    , "load view" to Express("loadView") { loadView() }))
        }
    }

    fun expressSimple() {

        Speedy("expressSimple()").apply {

            express(listOf(Express("readData") { readData() }
                    , Express("filterList") { filterList() }
                    , Express("saveData") { saveData() }
                    , Express("loadView") { loadView() }))
        }
    }

    fun testSealed() {

        Speedy("testSealed()").apply {

            clockFinish("testSealed") {
                SealedState.toList().forEachIndexed { ind, va ->
                    Log.d(TAG, "testSealed() index: $ind | val: $va")
                }
            }
        }
    }

    fun testIntStates() {

        Speedy("testIntStates()").apply {
            clockFinish("testIntStates") {
                IntStates.toList().forEachIndexed  { ind, va ->
                    Log.d(TAG, "testIntStates() index: $ind | val: $va")
                }
            }
        }
    }
}