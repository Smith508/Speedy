package com.fullsail.smith.speedydemo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**

 ParcelableData will represent our custom object that implements Parcelable.

 - This will be passed as an extra in an intent when the user taps the corresponding button.
 - Parcelable objects are supposed to be more efficient at being passed through intents than objects
 that implement Serializable.


 **/

public class ParcelableData implements Parcelable {


    private String name;
    private int age;
    private ArrayList<String> strings;
    private ArrayList<Integer> nums;

    public ParcelableData(String name, int age) {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.age);
        dest.writeStringList(this.strings);
        dest.writeList(this.nums);
    }

    protected ParcelableData(Parcel in) {
        this.name = in.readString();
        this.age = in.readInt();
        this.strings = in.createStringArrayList();
        this.nums = new ArrayList<Integer>();
        in.readList(this.nums, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<ParcelableData> CREATOR = new Parcelable.Creator<ParcelableData>() {
        @Override
        public ParcelableData createFromParcel(Parcel source) {
            return new ParcelableData(source);
        }

        @Override
        public ParcelableData[] newArray(int size) {
            return new ParcelableData[size];
        }
    };
}
