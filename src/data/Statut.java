package src.data;

public enum Statut {
    CREE(0),
    LU(1),
    SUPPRIME(2);

    private int value;

    private Statut(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static Statut fromValue(int value) {
        for (Statut statut : Statut.values()) {
            if (statut.getValue() == value) {
                return statut;
            }
        }
        return null;
    }
}
