package vistas;

public class ItemsComboBox {
    private int id;
    private String name;

    public ItemsComboBox(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}