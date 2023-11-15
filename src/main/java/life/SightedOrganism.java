package life;

public class SightedOrganism extends Organism {

    private final int sightRange;
    private Board board;

    public SightedOrganism(int energy, int sightRange, Board board) {
        super(energy);
        this.sightRange = sightRange;
        this.board = board;
    }

    @Override
    public void move() {
        Position currentPosition = getPosition();
        Position nearestOrganismPosition = findNearestOrganism(currentPosition.getX(), currentPosition.getY());

        if (nearestOrganismPosition != null) {
            int deltaX = Integer.compare(nearestOrganismPosition.getX(), currentPosition.getX());
            int deltaY = Integer.compare(nearestOrganismPosition.getY(), currentPosition.getY());

            int newX = currentPosition.getX() + deltaX;
            int newY = currentPosition.getY() + deltaY;

            newX = Math.max(0, Math.min(newX, board.getWidth() - 1));
            newY = Math.max(0, Math.min(newY, board.getHeight() - 1));

            if (board.isValidMove(newX, newY)) {
                board.moveOrganism(this, newX, newY);
            } else {
                System.out.println("SightedOrganism: Invalid move!");
            }
        } else {
            int newX = currentPosition.getX() + getRandom().nextInt(3) - 1; // -1, 0, или 1
            int newY = currentPosition.getY() + getRandom().nextInt(3) - 1; // -1, 0, или 1

            newX = Math.max(0, Math.min(newX, board.getWidth() - 1));
            newY = Math.max(0, Math.min(newY, board.getHeight() - 1));

            if (board.isValidMove(newX, newY)) {
                board.moveOrganism(this, newX, newY);
            } else {
                System.out.println("SightedOrganism: Invalid move!");
            }
        }
    }

    private Position findNearestOrganism(int x, int y) {
        Position nearest = null;
        double nearestDistance = Double.MAX_VALUE;

        for (int i = -sightRange; i <= sightRange; i++) {
            for (int j = -sightRange; j <= sightRange; j++) {
                int searchX = x + i;
                int searchY = y + j;

                if (searchX >= 0 && searchX < board.getWidth() && searchY >= 0 && searchY < board.getHeight()) {
                    Organism organism = board.getOrganismAt(searchX, searchY);
                    if (organism != null && organism != this) {
                        double distance = Math.sqrt(i * i + j * j);
                        if (distance < nearestDistance) {
                            nearestDistance = distance;
                            nearest = new Position(searchX, searchY);
                        }
                    }
                }
            }
        }

        return nearest;
    }
}
