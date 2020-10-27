package com.example.javaproject;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class javatest {
    public static void main(String[] args) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd/ HH:mm");
        String date_of_now = formatter.format(new Date(System.currentTimeMillis()));
        System.out.println("TIME IS ========> "+date_of_now);
    }
}