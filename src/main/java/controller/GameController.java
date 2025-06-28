package controller;

import model.Map;
import model.Position;
import view.GameView;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameController {
    private Map map;
    private GameView view;
    private KeyAdapter keyAdapter;

    public GameController(Map map, GameView view) {
        this.map = map;
        this.view = view;

        setupListeners();
    }

    private void setupListeners() {
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleInput(e);
            }
        };
        view.addKeyListener(keyAdapter);
    }



    private void handleInput(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                movePlayer(-1, 0);
                break;
            case KeyEvent.VK_DOWN:
                movePlayer(1, 0);
                break;
            case KeyEvent.VK_LEFT:
                movePlayer(0, -1);
                break;
            case KeyEvent.VK_RIGHT:
                movePlayer(0, 1);
                break;
        }
    }

    public void removeListeners() {
        view.removeKeyListener(keyAdapter);
    }

    private void movePlayer(int newI, int newJ) {
        Position oldPosition = map.getPlayer().getPosition();
        Position newPosition = new Position(
                oldPosition.getI() + newI,
                oldPosition.getJ() + newJ
        );

        if (map.validateMove(oldPosition, newPosition)) {
            boolean isEnd = map.playRound(newPosition);
            map.changeVisibility ();
            view.updateSteps(map.getPlayer().getMoves());

            if(isEnd){
                if(map.getResult().equals("WIN")){
                    view.gameWon = true;
                } else if (map.getResult().equals("LOSE")) {
                    view.gameLost = true;
                }

                removeListeners();
                map.letThereBeLight();

            }
            view.repaint();
        }
    }
}
