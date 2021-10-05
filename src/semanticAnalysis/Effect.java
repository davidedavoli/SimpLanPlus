package semanticAnalysis;

public class Effect {
    // ⊥
    private static final int _INITIALIZED = 0;
    // rw
    private static final int _READ_WRITE = 1;
    // d
    private static final int _DELETE = 2;
    // ⊤
    private static final int _ERROR = 3;

    // Actual value representing the status of a variable.
    private final int value;

    /**
     * Constructor of {@code Effect}.
     *
     * @param value can be {@code _INITIALIZED}, {@code _READ_WRITE},
     *              {@code _DELETE}, {@code _ERROR}.
     */
    private Effect(final int value) {
        this.value = value;
    }

    /**
     * Construct of {@code Effect}. Defaults to {@code Effect.INITIALIZED}.
     */
    public Effect() {
        this(_INITIALIZED);
    }

    /**
     * Copy constructor of {@code Effect}.
     *
     * @param e effect
     */
    public Effect(final Effect e) {
        this(e.value);
    }

    // Effect operations (max,seq,par)
    /**
     * Returns a new {@code Effect} representing the max between the two
     * effects {@code e1} and {@code e2}.
     *
     * @param e1 the first effect
     * @param e2 the second effect
     * @return new {@code Effect} max of the two effects {@code e1} and {@code e2}
     */
    public static Effect max(final Effect e1, final Effect e2) {
        return new Effect(Math.max(e1.value, e2.value));
    }

    /**
     * Returns a new {@code Effect} instance representing the sequence of the two
     * effects {@code e1} and {@code e2}.
     *
     * @param e1 the first effect
     * @param e2 the second effect
     * @return new {@code Effect} sequence of the two effects {@code e1} and {@code e2}
     */
    public static Effect seq(final Effect e1, final Effect e2) {
        if (max(e1, e2).value <= _READ_WRITE) {
            return Effect.max(e1, e2);
        }

        if ((e1.value <= _READ_WRITE && e2.value == _DELETE) || (e1.value == _DELETE && e2.value == _INITIALIZED)) {
            return new Effect(_DELETE);
        }

        return new Effect(_ERROR);
    }

    /**
     * Returns a new {@code Effect} instance representing the par of the two effects
     * {@code e1} and {@code e2} define as par = max(seq(e1,e2),seq(e2,e1)).
     *
     * @param e1 the first effect
     * @param e2 the second effect
     * @return new {@code Effect} instance representing the sequence of the two
     * effects {@code e1} and {@code e2}
     */
    public static Effect par(final Effect e1, final Effect e2) {
        return max(seq(e1, e2), seq(e2, e1));
    }


    // utils
    @Override
    public String toString() {
        switch (value) {
            case _INITIALIZED:
                return "INITIALIZED";
            case _READ_WRITE:
                return "READ_WRITE";
            case _DELETE:
                return "DELETE";
            case _ERROR:
                return "ERROR";
            default:
                return "";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Effect e = (Effect) obj;
        return value == e.value;
    }
}
