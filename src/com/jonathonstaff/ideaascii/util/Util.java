package com.jonathonstaff.ideaascii.util;
//  Created by jonstaff on 6/11/14.


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Util {

    public static String convertTextToAscii(String text) {
        return convertTextToAscii(text, "ivrit");
    }

    public static String convertTextToAscii(String text, String font) {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod("http://artii.herokuapp.com/make?text=" + urlEncodedString(text) + "&font=" + font);

        String response = "Something went wrong...";
        try {
            client.executeMethod(get);
            response = get.getResponseBodyAsString();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return response;
    }

    public static String urlEncodedString(String str) {
        String encodedStr = "";
        try {
            encodedStr = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedStr;
    }
}
