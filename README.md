# Speedy

#### Speedy is a class designed to help with optimization and is meant to show you how long a method or task takes to complete.

## Overview
Speedy consists of three key methods: **.startJob()**, **.checkDelta(String jobDescription)** & **.finishTime()**

### Example
Here is a basic method to launch a fragment.           
![screen shot 2017-07-26 at 12 28 38 pm](https://user-images.githubusercontent.com/6344435/28632218-ce2e4812-71fd-11e7-92fb-218a72d19da1.png)

Now lets see what happens when we use Speedy.                                                                               
#### Steps:

**1)** Create a new Speedy object passing in a String for a method name or overall task.   
**2)** Decide where you want to begin initially and call **speedy.startJob()**   
**3)** After a job is completed call **speedy.checkDelta()** and pass in a String to describe the job that was completed ex: "Reading from Storage".    
     - **Each startJob call must be followed by a checkDelta() call**   
**4)** Continue steps 2 & 3 for each other job you wish to check within the block.   
**5)** Finally call **speedy.finishTime()** to end the check. Logs will display how long each job took to complete as well as the whole block from    the first call to **startJob()** to **finishTime()**. 
![screen shot 2017-07-26 at 12 05 23 pm](https://user-images.githubusercontent.com/6344435/28631860-b43e16cc-71fc-11e7-8a6b-449f99a15275.png)

- Search for the logs easily by searching for **TAGS** in the logcat.    
The logs for this would look like:                                                                                        
![screen shot 2017-07-26 at 12 05 58 pm](https://user-images.githubusercontent.com/6344435/28632692-5ff7d1ae-71ff-11e7-9c24-de5b47c8b9f9.png)

After a little optimization the method might look like this:                                                                    
![screen shot 2017-07-26 at 12 10 40 pm](https://user-images.githubusercontent.com/6344435/28632914-06c80648-7200-11e7-9357-1396ef3d2aaf.png)

Now lets see how the logs look after the optimization:
![screen shot 2017-07-26 at 12 10 55 pm](https://user-images.githubusercontent.com/6344435/28633036-68de71be-7200-11e7-888a-01d45d1e93b8.png)

The **loadInitialLogin()** method went from taking **46ms** to complete to only **2ms**. Speedy is now ready to be removed from this method. 

### Things to Note
 - Any block of code within the **100-200ms** range on the main thread causes the slowness to begin becoming noticeable to the user. 
 - The screen draws **every 16ms**, in order to maintain **60fps**, and any work that takes longer will block the main thread and should be put on    a backgorund thread.
 - Each thread takes up **64kb** of memory.
 - **AsyncTasks** are great for small quick operations that need to work with UI elements, but should only be used with work that takes **no        longer than 5 ms** to complete. AsyncTasks are threaded **serially** and can block with long running work.  
 - Getting a **0** as a **delta(change)** doesn't necessarily mean the **'prevCurrent'** variable and the **'current'** variable match. It could just be      so minuscule that it doesn't register as a long or a float changing.
 
 ### How Do I Optimize?   
 Depending on your particular situation you may want to use **AsyncTask**, **Runnable**, **HandlerThread** or **ThreadPoolExecutor**.
 - **AsyncTask** is good for short operations like loading and decoding a bitmap. Move work to the doInBackground() method.          
      - **NOTE**: Use **WeakReference** for UI elements to help with garbage collection.   
 - Use a **Runnable** passed to a created thread, like in the example above. You should set the thread priority to                                **Process.THREAD_PRIORITY_BACKGROUND**. Move the work to the Runnable's **run()** method. Be sure to call the created thread's **.start()** method.    If you need to communicate with the UI thread you should use a **Handler** to post the work.
 - **HandlerThread** is good for long running background work that will need to interact with UI at some point. This class allows you to          manage some of the workflow. This comes in handy when dealing with timing and callbacks. Documentation gives an example of grabbing the    preview frame from the camera. If this callback is invoked on the main thread it now has large arrays of pixels to handle while also      rendering. 
      - **NOTE**: Need to specify thread priority.
 - **ThreadPoolExecutor** is meant for work that requires several operations running in **parallel**. This class also helps with the load            balancing by spinning up and destroying threads based on the need. **ThreadPoolExecutor** creates a group of threads, sets their        priorities and handles the work load. ThreadPoolExecutor has a **min** and **max** number of threads specified and based on the workload and these min max values it determines how many threads to keep alive. 
