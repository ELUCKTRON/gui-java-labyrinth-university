package model;

public abstract class Field {

    protected Position position;
    protected boolean visibility;

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(boolean visibility) {
        this.visibility = visibility;
    }

    public Position getPosition() {
        return position;
    }

    public Field(Position position){
        this.visibility = false;
        this.position = position;
    }


}
