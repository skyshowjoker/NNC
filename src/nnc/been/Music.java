package nnc.been;

public class Music {
    private int id;
    private String name;
    private String maxlength;
    private String motion;
    private String beat;
    private String basicbeat;
    private String mode;

    public String getMaxlength() {
        return maxlength;
    }

    public void setMaxlength(String maxlength) {
        this.maxlength = maxlength;
    }

    public String getMotion() {
        return motion;
    }

    public void setMotion(String motion) {
        this.motion = motion;
    }

    public String getBasicbeat() {
        return basicbeat;
    }

    public void setBasicbeat(String basicbeat) {
        this.basicbeat = basicbeat;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public int getId(){ return id;}

    public void setId(int id){this.id=id;}

    public String getName(){return name;}

    public void setName(String name){this.name=name;}

    public String getBeat() {
        return beat;
    }

    public void setBeat(String beat) {
        this.beat = beat;
    }
}
