package com.fullsail.smith.speedydemo;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;

public class CustomSerial implements Serializable {

    protected static final long serialVersionUID = 1825114L;
    private static final String TAG = "CustomSerial.TAGS";

    private String name;
    private int age;
    private ArrayList<String> strings;
    private ArrayList<Integer> nums;

    public CustomSerial(String name, int age) {
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

    private void writeObject(java.io.ObjectOutputStream outputStream) throws IOException {
        Log.i(TAG, "writeObject: ");
         outputStream.writeUTF(this.name);
         outputStream.writeInt(this.age);
         outputStream.writeObject(this.strings);
         outputStream.writeObject(this.nums);
    }

    private void readObject(ObjectInputStream inputStream) throws ClassNotFoundException, IOException {

        Log.i(TAG, "readObject: ");
        this.name = inputStream.readUTF();
        this.age = inputStream.readInt();
        this.strings = (ArrayList<String>) inputStream.readObject();
        this.nums = (ArrayList<Integer>) inputStream.readObject();

    }

    private void readObjectNoData() throws ObjectStreamException {

        Log.i(TAG, "readObjectNoData: ");
    }
}
