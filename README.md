# Speedy

Speedy is a class designed to help with optimization and is meant to show you how long a method or task takes to complete.

# Overview
Speedy consists of three key methods: .startJob(), .checkDelta(String jobDescription) and .finishTime()

# Example
Here is a basic method to launch a fragment.           
![screen shot 2017-07-26 at 12 28 38 pm](https://user-images.githubusercontent.com/6344435/28632218-ce2e4812-71fd-11e7-92fb-218a72d19da1.png)

Now lets see what happens when we use Speedy.                                                                               
Steps:
1) Create a new Speedy object passing in a String for a method name or overall task.
2) Decide where you want to begin initially and call speedy.startJob()
3) After a job is completed call speedy.checkDelta() and pass in a String to describe the job that was completed ex: "Reading from Storage". 
4) Continue steps 2 & 3 for each other job you wish to check
5) Finally call speedy.finishTime() to end the check. Logs will display how long each job took to complete as well as the whole block from    the first call to startJob to finishTime. 
![screen shot 2017-07-26 at 12 05 23 pm](https://user-images.githubusercontent.com/6344435/28631860-b43e16cc-71fc-11e7-8a6b-449f99a15275.png)

The logs for this would look like:                                                                                        
![screen shot 2017-07-26 at 12 05 58 pm](https://user-images.githubusercontent.com/6344435/28632692-5ff7d1ae-71ff-11e7-9c24-de5b47c8b9f9.png)

After a little optimization the method might look like this:                                                                    
