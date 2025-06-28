package model;

import java.util.Random;

public class Monster extends Field {


    public Monster(Position position){
        super(position);

    }

    public void setPositionForTest(Position position){
        this.position = position;
    }

    public Position randomMove() {
        Random random = new Random();
        int direction = random.nextInt(4); // 0 to 3 for four possible moves
        int x = 0, y = 0;
        switch (direction) {
            case 0: // Move up
                x = -1;
                break;
            case 1: // Move down
                x = 1;
                break;
            case 2: // Move left
                y = -1;
                break;
            case 3: // Move right
                y = 1;
                break;
        }

        return new Position(this.position.getI() + x, this.position.getJ() + y);
    }



    public void action(Map map) {
        Position randomMove = randomMove();
        // Keep generating new moves until one is valid
        while (!map.validateMove(this.position, randomMove)) {
            randomMove = randomMove();
        }


        map.getFields()[this.position.getI()][this.position.getJ()] = new Empty(this.position); // Clear the old position
        this.position = randomMove;
        map.getFields()[this.position.getI()][this.position.getJ()] = this; // Place the monster at the new position
    }




}
