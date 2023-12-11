package life;

public class Board {

    private int width;
    private int height;
    private Organism[][] organisms;

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.organisms = new Organism[width][height];
    }

    public Organism getOrganismAt(int x, int y) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            return organisms[x][y];
        }
        return null;
    }

    public void addOrganism(Organism organism, int x, int y) {
        if (organisms[x][y] == null) {
            organisms[x][y] = organism;
            organism.setPosition(new Position(x, y));
        } else {
            System.out.println("Position already occupied!");
        }
    }

    public void moveOrganism(Organism organism, int newX, int newY) {
        if (isValidMove(newX, newY)) {
            Position oldPosition = organism.getPosition();
            if (organisms[newX][newY] != null && organisms[newX][newY] != organism) {
                organism.consume(organisms[newX][newY]);
            }
            organisms[oldPosition.getX()][oldPosition.getY()] = null;
            organisms[newX][newY] = organism;
            organism.setPosition(new Position(newX, newY));
        } else {
            System.out.println("Invalid move!");
        }
    }


    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
