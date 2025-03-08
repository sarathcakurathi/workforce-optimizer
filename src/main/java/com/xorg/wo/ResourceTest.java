package com.xorg.wo;

import com.xorg.wo.utils.GlobalConstants;

import java.io.InputStream;


public class ResourceTest {
    public static void main(String[] args) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(GlobalConstants.CONFIG_FILE);
        if (inputStream != null) {
            System.out.println("Resource found!");
        } else {
            System.out.println("Resource not found!");
        }
    }
}
