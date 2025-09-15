public class Main {
    public static void main(String[] args) {

        Red red = new Red();
        Blue blue = new Blue();
        final int[] shakes = new int[]{12, 12, 7, 8};
        int turns = 0;
        int redTurns = 0;
        int blueTurns = 0;

        System.out.format("Red:%s Blue:%s %n", red.getPositionAsString(), blue.getPositionAsString());
        while (!red.isAtEnd() & !blue.isAtEnd()) {
            int shake = shakes[turns++];
            if (turns % 2 == 1) {
                redTurns++;
                System.out.format("Red play %d rolls %d%n", redTurns, shake);
                String start = red.getPositionAsString();
                red.advance(shake);
                System.out.format("Red moves from %s to %s%n", start, red.getPositionAsString());
            } else {
                blueTurns++;
                System.out.format("Blue play %d rolls %d%n", blueTurns, shake);
                String start = blue.getPositionAsString();
                blue.advance(shake);
                System.out.format("Blue moves from %s to %s%n", start, blue.getPositionAsString());
            }
        }
        if (red.isAtEnd()) {
            System.out.format("Red wins in %d moves%n", redTurns);
        } else {
            System.out.format("BLue wins in %d moves%n", blueTurns);
        }
        System.out.format("Total plays %d%n", turns);

    }
}
