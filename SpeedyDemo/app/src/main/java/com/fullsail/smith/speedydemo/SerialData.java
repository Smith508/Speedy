package com.fullsail.smith.speedydemo;

import java.io.Serializable;
import java.util.ArrayList;

/**

 SerialData will represent our custom object that implements serializable.
 - This will be passed as an extra in an intent when the user taps the corresponding button.

 **/

public class SerialData implements Serializable {

    private String name;
    private int age;
    private ArrayList<String> strings;
    private ArrayList<Integer> nums;

    public SerialData(String name, int age) {
        this.name = name;
        this.age = age;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ArrayList<String> getStrings() {
        return strings;
    }

    public void setStrings(ArrayList<String> strings) {
        this.strings = strings;
    }

    public ArrayList<Integer> getNums() {
        return nums;
    }

    public void setNums(ArrayList<Integer> nums) {
        this.nums = nums;
    }

    @Override
    public String toString() {
        return "SerialData: name = " + getName() + " age: " + getAge() + " strings: " + getStrings().size() + " nums: " + getNums().size();
    }

    public String toStringWithoutGetters(){

        return "SerialData: name = " + name + " age: " + age + " strings: " + strings.size() + " nums: " + nums.size();


    }
}
