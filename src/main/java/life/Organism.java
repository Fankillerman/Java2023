package life;

import java.util.Random;

public abstract class Organism {

    private int energy;
    private Position position;
    private Random random = new Random();

    public Organism(int energy) {
        this.energy = energy;
    }

    public abstract void move();

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Random getRandom() {
        return random;
    }

    public void consume(Organism other) {
        System.out.println(this.getClass().getSimpleName() + " ate " + other.getClass().getSimpleName() +
                " on position (" + this.getPosition().getX() + ", " + this.getPosition().getY() + ")");
        this.energy += other.energy;
        other.energy = 0;
    }

}
