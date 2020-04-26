package model;

public class City extends MenuItem {
    private int id;
    private String name;

    public City(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
}
