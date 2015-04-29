package htmlparserjar;


import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.HashSet;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author CSDVZAK
 */
public class HtmlParser {

    public static void main1(String[] args) {
        try {
            Document doc;
            // need http protocol
            doc = Jsoup.connect("http://google.com").get();

            // get page title
            String title = doc.title();
            System.out.println("title : " + title);

            // get all links
            Elements links = doc.select("a[href]");
            for (Element link : links) {

                // get the value from href attribute
                System.out.println("\nlink : " + link.attr("href"));
                System.out.println("text : " + link.text());

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {

        String url = "http://www.expatriates.com/classifieds/bhr";
//        String url = "http://www.expatriates.com/classifieds/bhr/forsale/";
        int counter = 1;
        //  List<Data> urlList = new ArrayList<Data>();
        HashSet<Data> urlList = new HashSet<Data>();
        urlList = getLinks(url, counter, urlList);

        String fileName = "C:\\Zeeshan_Akhter_Data\\allLinks2015.csv";
        generateCsvFile(fileName, urlList);
    }

    public static HashSet<Data> getLinks(String url, int counter, HashSet<Data> urlList) {
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();

            Elements links = doc.select("a[href]");

            for (Element link : links) {

                // get the value from href attribute
                if (link.attr("href").contains("classifieds/bhr/")) {
                    url = "http://www.expatriates.com" + link.attr("href");

                    if (url.lastIndexOf("/") > 0) {
                        url = url.substring(0, url.lastIndexOf("/"));
                    }
                    if (!url.equalsIgnoreCase("http://www.expatriates.com")) {
                        String text = removeSpecialCharacter((link.text().trim()));
                        Data data = new Data((url.trim()), text);
                        if (!urlList.contains(data)) {
//                            System.out.println("url =" + url.length());
//                            System.out.println("link.text() =" + link.text().length());
//
//                            System.out.println("\nlink : " + counter++ + " " + url);
//                            System.out.println("text :          " + link.text());
                            urlList.add(data);
                            urlList = getLinks(url, counter, urlList);
                        }
                    } else {
                        continue;
                    }
                } else {
                    url = "http://www.expatriates.com" + link.attr("href");

                    if (url.contains("/cls/")) {
                        String text = removeSpecialCharacter((link.text().trim()));
                        Data data = new Data((url.trim()), text);
                        url = " http://www.expatriates.com" + link.attr("href");
                        if (!urlList.contains(data)) {
//                            System.out.println("url =" + url.length());
//                            System.out.println("link.text() =" + link.text().length());
//                            System.out.println("\nlink ::: " + counter++ + " " + url);
//                            System.out.println("text :::          " + link.text());
                            urlList.add(data);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return urlList;
    }

    public static void generateCsvFile(String sFileName, HashSet<Data> urlList) {
        try {
            FileWriter writer = new FileWriter(sFileName);

            writer.append("URL");
            writer.append(',');
            writer.append("Title");
            writer.append('\n');
            for (Data data : urlList) {

                writer.append(data.getUrl());
                writer.append(',');
                writer.append(data.getTitle());
                writer.append('\n');
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String removeSpecialCharacter(String albumName) {
//        if (albumName.contains(" & ")) {
//            albumName = albumName.replace(" & ", " and ");
//        }
//       
//        if (albumName.contains("'")) {
//            albumName = albumName.replace("'", "");
//        }
//        if (albumName.contains(",")) {
//            albumName = albumName.replace(",", " ");
//        }
//        if (albumName.contains(":")) {
//            albumName = albumName.replace(":", " ");
//        }
//        if (albumName.contains("?")) {
//            albumName = albumName.replace("?", " ");
//        }
//        
//        if (albumName.contains("-")) {
//            albumName = albumName.replace("-", " ");
//        }
//        if (albumName.contains("&")) {
//            albumName = albumName.replace("&", " and ");
//        }
//        if (albumName.contains("__")) {
//            albumName = albumName.replace("__", " ");
//        }
//        if (albumName.contains("___")) {
//            albumName = albumName.replace("___", " ");
//        }
//        if (albumName.contains("____")) {
//            albumName = albumName.replace("____", " ");
//        }
//        if (albumName.contains("!")) {
//            albumName = albumName.replace("!", " ");
//        }
//
//        if (albumName.contains("\"")) {
//            albumName = albumName.replace("\"", " ");
//        }
//        if (albumName.contains("'")) {
//            albumName = albumName.replace("'", "");
//        }
//        if (albumName.contains("_/_")) {
//            albumName = albumName.replace("_/_", " ");
//        }
//        if (albumName.contains("/")) {
//            albumName = albumName.replace("/", " ");
//        }
//        if (albumName.contains("%")) {
//            albumName = albumName.replace("%", "");
//        }

      

        albumName = Normalizer.normalize(albumName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        //alphaOnly         
        //String alphaOnly = albumName.replaceAll("[^a-zA-Z]+","");
        // alphaAndDigits 
        //albumName = albumName.replaceAll("[^a-zA-Z0-9]+", "");
        albumName = albumName.replace("_", " ");
        albumName = albumName.replace("BHD ", "Bahraini Dinar ");
        albumName = albumName.replace(" BHD ", " Bahraini Dinar ");
        albumName = albumName.replace("BD ", "Bahraini Dinar ");
        albumName = albumName.replace(" BD ", " Bahraini Dinar ");
        albumName = albumName.replace("BR ", " Bed Rooms ");
        albumName = albumName.replace(" BR ", " Bed Rooms ");
        albumName = albumName.replace(" br ", " Bed Rooms ");
        return albumName;
    }
}
