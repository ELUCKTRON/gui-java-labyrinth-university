package model;

public class HighScore {

    private final String name;
    private final int score;

    private final String difficulty;

    public HighScore(String name, int score , String difficulty) {
        this.name = name;
        this.score = score;
        this.difficulty = difficulty;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }


    public String getDifficulty() {
        return difficulty;
    }

    @Override
    public String toString() {
        return "HighScore{" + "name=" + name + ", score=" + score + ", Difficulty=" + difficulty + '}';
    }


}
