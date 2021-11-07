package com.example.a0411_0127122_t2_v3usafe;

public class MainMenuItem {
    private int imgId;
    private String menuName;

    public MainMenuItem(){
        imgId = 0;
        menuName = "";
    }

    public MainMenuItem(int imgId, String menuName){
        this.imgId = imgId;
        this.menuName = menuName;
    }

    public int getImgId(){
        return imgId;
    }

    public void setImgId(int imgId){
        this.imgId = imgId;
    }

    public String getMenuName(){
        return menuName;
    }

    public void setMenuName(String menuName){
        this.menuName = menuName;
    }
}
