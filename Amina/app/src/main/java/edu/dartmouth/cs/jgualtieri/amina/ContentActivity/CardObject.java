package edu.dartmouth.cs.jgualtieri.amina.ContentActivity;

/**
 * Created by azharhussain on 3/7/17.
 */

public class CardObject {
    String title;
    String description;
    String imagePath;
    public CardObject(){

    }

    public CardObject(String content_title, String content_description, String content_imagePath){
        title = content_title;
        description = content_description;
        imagePath = content_imagePath;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getImagePath(){
        return imagePath;
    }

}
