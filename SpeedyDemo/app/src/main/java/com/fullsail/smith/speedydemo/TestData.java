package com.fullsail.smith.speedydemo;

public class TestData extends CustomSerial {

    public TestData(String name, int age) {
        super(name, age);
    }

    public TestData(CustomSerial data) {

        super(data.getName(), data.getAge());
    }
}
