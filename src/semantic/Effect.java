package semantic;

/**
 * Effect type and function for semantic analysis
 **/

//public static final Effect IS_INIT = new Effect(INIT);
//public static final Effect IS_RW = new Effect(RW);
//public static final Effect IS_DEL = new Effect(DEL);
//public static final Effect IS_ERR = new Effect(ERR);

public class Effect {
    // init effect
    public static final int INIT = 0;
    // read & write effect
    public static final int RW = 1;
    // delete effect
    public static final int DEL = 2;
    // error effect
    public static final int ERR = 3;

    // Actual status of a variable.
    private final int status;

    public Effect(int status) {
        this.status = status;
    }

    public Effect() {
        this(INIT);
    }

    public Effect(final Effect effect) {
        this(effect.status);
    }

    public static Effect maxEffect(final Effect effect1, final Effect effect2) {
        int maxStatus = Math.max(effect1.status, effect2.status);
        return new Effect(maxStatus);
    }

    public static Effect sequenceEffect(final Effect effect1, final Effect effect2) {

        if (maxEffect(effect1, effect2).status <= RW)
            return new Effect(RW);

        if ((effect1.status <= RW && effect2.status == DEL) || (effect1.status == DEL && effect2.status == INIT))
            return new Effect(DEL);
        return new Effect(ERR);
    }

    public static Effect parallelEffect(final Effect effect1, final Effect effect2) {
        Effect firstSecondEffect = sequenceEffect(effect1, effect2);
        Effect secondFirstEffect = sequenceEffect(effect2, effect1);
        Effect parEffect = maxEffect(firstSecondEffect,secondFirstEffect);

        return parEffect;
    }

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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Effect e = (Effect) obj;
        return status == e.status;
    }

}
