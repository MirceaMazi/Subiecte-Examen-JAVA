public enum Tip {
    intrare(1), iesire(-1);
    int semn;

    Tip(int semn) {
        this.semn = semn;
    }

    public int getSemn() {
        return semn;
    }
}
