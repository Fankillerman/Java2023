package life;

public class LifeSimulator {

    public static void main(String[] args) {
        Board board = new Board(10, 10);

        Organism jumpingOrganism = new JumpingOrganism(100, board);
        board.addOrganism(jumpingOrganism, 5, 4);

        Organism sightedOrganism = new SightedOrganism(100, 2, board);
        board.addOrganism(sightedOrganism, 5, 5);

        for (int i = 0; i < 1000; i++) {
            jumpingOrganism.move();
            sightedOrganism.move();

        }
    }
}
