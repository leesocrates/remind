package com.lee.library.view.tagwidget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lee on 2018/4/4.
 */

public class TagFactory {
    public static List<Tag> getTagList(String[] tagTexts) {
        List<Tag> tagList = new ArrayList<>();
        if (tagTexts != null) {
            for (String text : tagTexts) {
                Tag tag = new Tag();
                tag.setText(text);
                tagList.add(tag);
            }
        }
        return tagList;
    }


}
