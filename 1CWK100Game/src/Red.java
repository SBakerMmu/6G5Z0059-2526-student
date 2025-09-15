class Red {
    private final static int START_OF_TAIL_INDEX = 18;
    private final static int END_OF_TAIL_INDEX = 20;
    private final static int[] POSITIONS = new int[]{
            1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 1, 2, 3
    };

    private int currentIndex = 0;

    public boolean isInBody() {
        return !isInTail();
    }

    public boolean isHome() {
        return currentIndex == 0;
    }

    public boolean isInTail() {
        return currentIndex >= START_OF_TAIL_INDEX;
    }

    public boolean isAtEnd() {
        return currentIndex == END_OF_TAIL_INDEX;
    }

    public int getPosition() {
        return POSITIONS[currentIndex];
    }

    public String getPositionAsString() {
        if (isHome())
            return String.format("HOME (Position %d)", getPosition());
        if (isAtEnd())
            return String.format("END (Tail Position %d)", getPosition());
        if (isInTail())
            return String.format("TAIL (Tail Position %d)", getPosition());
        else
            return String.format("Position %d", getPosition());

    }

    public void advance(int positions) {
        currentIndex = currentIndex + positions;
        if (currentIndex > END_OF_TAIL_INDEX) {
            currentIndex = END_OF_TAIL_INDEX;
        }
    }

}
