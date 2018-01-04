package com.example.tms.sapihir;

/**
 * Created by TMS on 03.01.2018.
 */

public class Blog {

    private  String cim, img,leiras, tel;
    public Blog(){}

    public Blog(String cim, String img, String leiras, String tel) {
        this.cim = cim;
        this.img = img;
        this.leiras = leiras;
        this.tel = tel;
    }

    public String getCim() {
        return cim;
    }

    public void setCim(String cim) {
        this.cim = cim;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLeiras() {
        return leiras;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
