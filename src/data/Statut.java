package data;

/**
 * Enumération des statuts possibles d'un message
 */
public enum Statut {
    CREE(0),
    LU(1),
    SUPPRIME(2);

    private int value;

    /**
     * Constructeur d'un statut
     * @param value la valeur du statut
     */
    private Statut(int value) {
        this.value = value;
    }

    /**
     * Getter de la valeur du statut
     * @return la valeur du statut
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Récupère le statut correspondant à une valeur
     * @param value la valeur du statut
     * @return le statut correspondant à la valeur
     */
    public static Statut fromValue(int value) {
        for (Statut statut : Statut.values()) {
            if (statut.getValue() == value) {
                return statut;
            }
        }
        return null;
    }
}
