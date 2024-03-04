package lambeer;

import java.io.Serializable;

public class Beer implements Serializable {

    private String name;
    private  String style;
    private double strength;

    public  Beer(String n, String sty, double str){
        name = n;
        style = sty;
        strength = str;
    }

    public double getStrength() {
        return strength;
    }

    public String getName() {
        return name;
    }

    public String getStyle() {
        return style;
    }

    @Override
    public String toString() {
        return "Sör neve: " + name + "\n" +
                "Sör jellege: " + style + "\n" +
                "Alkoholfok: " + strength + "%";
    }
}
