package htmlparserjar;

import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
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

    
    public static void main(String[] args) {

//        String url = "http://www.expatriates.com/classifieds/bhr";
//        String url = "http://www.expatriates.com/classifieds/bhr/forsale/";
        String url = "http://www.expatriates.com/classifieds/bhr/housingavailable/";
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
                            System.out.println("\nlink ::: " + counter++ + " " + url);
                            //   System.out.println("text :::          " + link.text());
                            data.setImagesList(getImages(data.getUrl()));
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
            writer.append(',');
            writer.append("Images");
            writer.append('\n');
            for (Data data : urlList) {

                writer.append(data.getUrl());
                writer.append(',');
                writer.append(data.getTitle());
                writer.append(',');
                writer.append(((String) data.getImagesList()));
                writer.append('\n');
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String removeSpecialCharacter(String albumName) {

        albumName = Normalizer.normalize(albumName, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
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

    public static String getImages(String url) {

        StringBuilder stringBuilder = new StringBuilder();
        try {
            Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();
            Elements media = doc.select("[src]");
            //  print("\nMedia: (%d)", media.size());
            for (Element src : media) {
                if (src.tagName().equals("img")) {
                    // System.out.println(src.attr("abs:src"));
                    stringBuilder.append(src.attr("abs:src"));
                    stringBuilder.append("=");
//                    print(" * %s: <%s> %sx%s (%s)",
//                            src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
//                            trim(src.attr("alt"), 20));
                } else {
                    //    print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
                }
            }

//            String masthead = doc.select("div.container > div.main-content > div.clearfix > div.post-body").text();
//            System.out.println("Details ::: " + masthead);
//
//            Element addDetails = doc.select("div.container > div.main-content > div.clearfix > div.col_7.post-info > ul.no-bullet").first();
//            Elements divChildren = addDetails.children();
//            for (Element elem : divChildren) {
//                System.out.println(elem.text());
//            }
//
//            Elements contactNumber = doc.select("a[href*=tel:]");
//            for (Element src : contactNumber) {
//                System.out.println("Contact Number ::: " + src.attr("href").replace("tel:", "tel:+973"));
//            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width) {
            return s.substring(0, width - 1) + ".";
        } else {
            return s;
        }
    }

}
