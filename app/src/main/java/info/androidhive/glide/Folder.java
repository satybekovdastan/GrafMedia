package info.androidhive.glide;

import java.io.Serializable;

/**
 * Created by Erlan on 23.06.2017.
 */

public class Folder implements Serializable{
    String name;
    int ID;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
