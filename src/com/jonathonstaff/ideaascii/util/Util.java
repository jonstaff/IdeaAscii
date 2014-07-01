package com.jonathonstaff.ideaascii.util;

//  Created by jonstaff on 6/11/14.
//  Edited by kjarrio on 2014-07-01

import com.intellij.ide.util.PropertiesComponent;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Util {

    public static final String KEY_FONT = "ascii_font";

    public static String convertTextToAscii(String text, int indentLength) {
        PropertiesComponent prop = PropertiesComponent.getInstance();
        if (!prop.getValue(KEY_FONT, "ivrit").equals("ivrit")) {
            return convertTextToAsciiCommented(text, prop.getValue(KEY_FONT), indentLength);
        } else {
            return convertTextToAsciiCommented(new StringBuilder(text).reverse().toString(), "ivrit", indentLength);
        }
    }

    public static String convertTextToAscii(String text, String font) {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod("http://artii.herokuapp.com/make?text="
                + urlEncodedString(text) + "&font=" + font);

        String response = "Something went wrong...\nThis plugin requires an Internet connection " +
                "- please ensure you have one.";
        try {
            client.executeMethod(get);
            response = get.getResponseBodyAsString();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            get.releaseConnection();
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

    public static String convertTextToAsciiCommented(String text, String font, int indentLength) {
        StringBuilder ascii = new StringBuilder(convertTextToAscii(text, font));
        ascii.insert(0, "// ");

        StringBuilder indent = new StringBuilder();
        for (int a = 0; a < indentLength; a++) indent.append(" ");

        return ascii.toString().replace("\n", "\n" + indent + "// ");
    }
}
