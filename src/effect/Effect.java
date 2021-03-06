package effect;

/**
 * Effect type and function for semantic analysis
 **/

public class Effect {
    // init effect
    public static final int INIT = 0;
    public static final Effect INITIALIZED = new Effect(INIT);

    // read & write effect
    public static final int RW = 1;
    public static final Effect READWRITE = new Effect(RW);

    // delete effect
    public static final int DEL = 2;
    public static final Effect DELETED = new Effect(DEL);

    // error effect
    public static final int ERR = 3;
    public static final Effect ERROR = new Effect(ERR);


    // Actual status of a variable.
    private int status;

    private Effect(final int status) {
        this.status = status;
    }

    public Effect() {
        this(INIT);
    }

    public Effect(final Effect effect) {
        this(effect.status);
    }

    public void updateStatus(Effect effect) {
        this.status = effect.status;
    }
/**
 * =====================================================
 * Max Function
 * =====================================================
 **/

    public static Effect maxEffect(final Effect effect1, final Effect effect2) {
        int maxStatus = Math.max(effect1.status, effect2.status);
        return new Effect(maxStatus);
    }

    public static Effect maxEffectNoCopy(final Effect effect1, final Effect effect2) {
        if(effect1.status > effect2.status)
            return effect1;
        else
            return effect2;
    }

/**
 * =====================================================
 * End Max Function
 * =====================================================
 **/

/**
 * =====================================================
 * Sequence Function
 * =====================================================
 **/

    public static Effect sequenceEffect(final Effect effect1, final Effect effect2) {

        if (maxEffect(effect1, effect2).status <= RW)
            return new Effect(RW);

        if ((effect1.status <= RW && effect2.status == DEL) || (effect1.status == DEL && effect2.status == INIT))
            return new Effect(DEL);
        return new Effect(ERR);
    }

/**
 * =====================================================
 * End Sequence Function
 * =====================================================
 **/

/**
 * =====================================================
 * Parallel Function
 * =====================================================
 **/

    public static Effect parallelEffect(final Effect effect1, final Effect effect2) {
        Effect firstSecondEffect = sequenceEffect(effect1, effect2);
        Effect secondFirstEffect = sequenceEffect(effect2, effect1);

        return maxEffect(firstSecondEffect,secondFirstEffect);
    }

/**
 * =====================================================
 * End parallel Function
 * =====================================================
 **/

    @Override
    public String toString() {
        switch (status) {
            case INIT:
                return "IS_INIT";
            case RW:
                return "IS_RW";
            case DEL:
                return "IS_DEL";
            case ERR:
                return "IS_ERR";
            default:
                return "";
        }
    }

    public boolean equals(Effect e) {
        return status == e.status;
    }

}
