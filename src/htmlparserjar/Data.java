package htmlparserjar;

import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author CSDVZAK
 */
public class Data {

    String url;
    String title;
    String imagesList;

    public Data() {
    }

    public Data(String url, String title) {
        this.url = url;
        this.title = title;
    }

    public String getImagesList() {
        return imagesList;
    }

    public void setImagesList(String imagesList) {
        this.imagesList = imagesList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object object) {
        boolean isEqual = false;

        if (object != null && object instanceof Data) {
            isEqual = (this.url.equalsIgnoreCase(((Data) object).url));
        }

        return isEqual;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
