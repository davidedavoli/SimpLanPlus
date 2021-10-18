package ast;

public class Label {

    private static int label_count = 0;

    /**
     * Generates a unique label with label_count from:
     *
     * label_name (name that needs etiquette)
     *
     * and return a unique label
     *
     */
    public String freshLabel(String label_name) {
        label_count += 1;
        return "LABEL"+label_name + label_count;
    }



}
