package edu.dartmouth.cs.jgualtieri.amina.ContentActivity;

/**
 * Created by azharhussain on 3/7/17.
 */

public class CardObject {
    String title;
    String description;
    String imagePath;
    String videoPath;
    public CardObject(){

    }

    public CardObject(String content_title, String content_description, String content_imagePath, String content_videoPath){
        title = content_title;
        description = content_description;
        imagePath = content_imagePath;
        videoPath = content_videoPath;
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

    public String getVideoPath() { return videoPath; }

}
