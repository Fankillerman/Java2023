package life;

public class JumpingOrganism extends Organism {
    private Board board;

    public JumpingOrganism(int energy, Board board) {
        super(energy);
        this.board = board;

    }

    @Override
    public void move() {
        int newX = getPosition().getX();
        int newY = getPosition().getY();

        boolean moveVertically = getRandom().nextBoolean();
        if (moveVertically) {
            newY += getRandom().nextBoolean() ? 2 : -2;
        } else {
            newX += getRandom().nextBoolean() ? 2 : -2;
        }

        newX = Math.max(0, Math.min(newX, board.getWidth() - 1));
        newY = Math.max(0, Math.min(newY, board.getHeight() - 1));

        if (board.isValidMove(newX, newY)) {
            board.moveOrganism(this, newX, newY);
        } else {
            System.out.println("JumpingOrganism: Invalid move!");
        }
    }


}
