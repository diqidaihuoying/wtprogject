package mj.wt.wtapp.bean;

import java.io.Serializable;

/**
 * Created by wantao on 2017/2/24.
 */

public class PhotoInfo implements Serializable{
    private String name;
    private String number;

    public PhotoInfo() {
    }

    public PhotoInfo(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
