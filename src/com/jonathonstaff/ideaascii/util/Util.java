package com.jonathonstaff.ideaascii.util;
//  Created by jonstaff on 6/11/14.


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

public class Util {

    public static String convertTextToAscii(String text) {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod("http://artii.herokuapp.com/make?text=hello+my+name+is+jon&font=ivrit");

        try {
            client.executeMethod(get);
            System.out.println("get request body:");
            System.out.println(get.getResponseBodyAsString());
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "hello";
    }

}
