package com.example.recuerdate.FamiliarsV2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class SubItem {
    private String subItemImageBase64; // Guarda la imagen como una cadena Base64
    private String subItemTitle;
    private String subItemDesc;

    public SubItem(String subItemTitle, String subItemDesc, ImageView subItemImage, Context context) {
        this.subItemTitle = subItemTitle;
        this.subItemDesc = subItemDesc;
        this.subItemImageBase64 = imageViewToBase64(subItemImage, context);
    }

    // Convertir ImageView a Base64
    private String imageViewToBase64(ImageView imageView, Context context) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // Convertir Base64 a Bitmap
    public Bitmap getSubItemImage(Context context) {
        byte[] decodedString = Base64.decode(subItemImageBase64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
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

