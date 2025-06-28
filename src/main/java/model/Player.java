package model;

public class Player extends Field {

    private String name;
    private Integer moves;

    public Player(String name , Position position){
        super(position);
        this.name  = name;
        this.moves = 0;
    }

    public Integer getMoves() {
        return moves;
    }

    public String getName() {
        return name;
    }

    public void action(Position newPosition, Map map) {

        map.getFields()[this.position.getI()][this.position.getJ()] = new Empty(this.position); // Clear the old position
        this.position = newPosition;
        map.getFields()[newPosition.getI()][newPosition.getJ()] = this;
        moves++;

    }



}
