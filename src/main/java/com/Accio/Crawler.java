package com.Accio;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;

public class Crawler {
    private HashSet<String> urllink;
    public Connection connection;

    public Crawler(){

        connection = DatabaseConnection.getConnection();



    urllink = new HashSet<String>();
}
    public void getPageTextAndLinks(String URL, int depth){
        if(!urllink.contains(URL)){
            try{
                if(urllink.add(URL)){
                    System.out.println(URL);
                }
                Document document = Jsoup.connect(URL).userAgent("Chrome").timeout(5000).get();
            String text = document.text().substring(0,500);
                System.out.println(text);

                depth++;
                if(depth==2){
                    return;
                }
                Elements availableLinksOnPage = document.select("a[href]");
                for(Element element:availableLinksOnPage){
                    getPageTextAndLinks(element.attr("abs:href"), depth);
                }



            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
