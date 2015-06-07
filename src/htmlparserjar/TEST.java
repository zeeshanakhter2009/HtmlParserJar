/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package htmlparserjar;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import org.jsoup.parser.Tag;

/**
 *
 * @author CSDVZAK
 */
public class TEST {

    public static void main(String[] args) throws IOException {
        //Validate.isTrue(args.length == 1, "usage: supply url to fetch");
//        String url = "http://www.expatriates.com/cls/27172846.html";
        String url = "http://www.expatriates.com/cls/27040513.html";
        print("Fetching %s...", url);

        Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36").get();

        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");
        String text = doc.body().text();
        System.out.println("text ::::::: " + text);

        String masthead = doc.select("div.container > div.main-content > div.clearfix > div.post-body").text();
        System.out.println("Details ::: " + masthead);
        String postInfo = doc.select("div.container > div.main-content > div.clearfix > div.col_7.post-info > ul.no-bullet > li > stron").text();
        System.out.println("postInfo ::: " + postInfo);

        Element div = doc.select("div.container > div.main-content > div.clearfix > div.col_7.post-info > ul.no-bullet").first();
        Elements divChildren = div.children();
        for (Element elem : divChildren) {
            
                System.out.println(elem.text());
            
        }
        Elements detachedDivChildren = new Elements();
        for (Element elem : divChildren) {
            Element detachedChild = new Element(Tag.valueOf(elem.tagName()),
                    elem.baseUri(), elem.attributes().clone());

            detachedDivChildren.add(detachedChild);
        }

        System.out.println("\ndivChildren content: \n" + divChildren);

        System.out.println("\ndetachedDivChildren content: \n"
                + detachedDivChildren);

        Elements contactNumber = doc.select("a[href*=tel:]");
        for (Element src : contactNumber) {
            System.out.println("Contact Number ::: " + src.attr("href").replace("tel:", "tel:+973"));
        }

//        print("\nMedia: (%d)", media.size());
//        for (Element src : media) {
//            if (src.tagName().equals("img"))
//                print(" * %s: <%s> %sx%s (%s)",
//                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
//                        trim(src.attr("alt"), 20));
//            else
//                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
//        }
//
//        print("\nImports: (%d)", imports.size());
//        for (Element link : imports) {
//            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
//        }
//
//        print("\nLinks: (%d)", links.size());
//        for (Element link : links) {
//            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
//        }
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
