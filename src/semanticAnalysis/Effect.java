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

    // actual value of the status for a variable
    private final int value;

    /**
     * Constructor
     * @param value = can be {@code _INITIALIZED}, {@code _READ_WRITE},
     * {@code _DELETE} or {@code _ERROR}
     */
    public Effect(int value) {
        this.value = value;
    }

    /**
     * Constructor
     * Defeault value {@code _INITIALIZED}
     */
    public Effect(){
        this.value = _INITIALIZED;
    }

    /**
     * Constructor
     * @param e Effect
     */
    public Effect(Effect e){
        this(e.value);
    }


    // Defines operation with Effects
    /**
     * Returns a new {@code Effect} representing the max between the
     * two effects {@code e1} and {@code e2}
     *
     * @param e1 = first effect
     * @param e2 = second effect
     * @return {@code Effect} max between the two effect
     */
    public static Effect max(Effect e1, Effect e2){
        return new Effect(Math.max(e1.value, e2.value));
    }

    /**
     * Returns a new {@code Effect} representing the sequence of the
     * two effects {@code e1} and {@code e2}
     *
     * @param e1 = first effect
     * @param e2 = second effect
     * @return {@code Effect} sequence of the two effect
     */
    public static Effect seq(Effect e1, Effect e2){
        if(e1.value <= _READ_WRITE && e2.value <= _READ_WRITE)
            return Effect.max(e1, e2);

        if ((e1.value <= _READ_WRITE && e2.value == _DELETE)||(e1.value == _DELETE && e2.value == _ERROR))
            return new Effect(_DELETE);

        return new Effect(_ERROR);
    }

    /**
     * Returns a new {@code Effect} representing the par of the
     * two effects {@code e1} and {@code e2} define as par = max(seq(e1,e2),seq(e2,e1))
     *
     * @param e1 = first effect
     * @param e2 = second effect
     * @return {@code Effect} par of the two effect
     */
    public static Effect par(Effect e1, Effect e2){
        return Effect.max(Effect.seq(e1, e2), Effect.seq(e2, e1));
    }

    // Utils
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
