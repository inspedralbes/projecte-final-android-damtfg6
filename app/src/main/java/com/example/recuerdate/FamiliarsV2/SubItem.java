package com.example.recuerdate.FamiliarsV2;

import android.widget.ImageView;

public class SubItem {
    private ImageView subItemImage;
    private String subItemTitle;
    private String subItemDesc;

    public SubItem(String subItemTitle, String subItemDesc, ImageView subItemImage) {
        this.subItemTitle = subItemTitle;
        this.subItemDesc = subItemDesc;
        this.subItemImage = subItemImage;
    }

    public ImageView getSubItemImage() {
        return subItemImage;
    }

    public void setSubItemImage(ImageView subItemImage) {
        this.subItemImage = subItemImage;
    }

    public String getSubItemTitle() {
        return subItemTitle;
    }

    public void setSubItemTitle(String subItemTitle) {
        this.subItemTitle = subItemTitle;
    }

    public String getSubItemDesc() {
        return subItemDesc;
    }

    public void setSubItemDesc(String subItemDesc) {
        this.subItemDesc = subItemDesc;
    }
}
