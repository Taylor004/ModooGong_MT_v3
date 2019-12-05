package kr.co.bravecompany.modoogong.android.stdapp.data;

/**
 * Created by BraveCompany on 2016. 10. 13..
 */

public class MenuData {
    private int type;
    private int image;
    private String name;
    private boolean isNew = false;

    public int menuIndex=-1;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
