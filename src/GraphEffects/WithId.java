package GraphEffects;

public class WithId {
    private int id;
    private static int count = 0;

    public WithId(){
        this.id=makeId();
    }

    public static int makeId(){
        return count++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
