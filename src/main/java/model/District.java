package model;

public class District extends MenuItem {
    private Integer id;
    private String name;
    private Integer cityId;

    public District(String name, Integer cityId) {
        this.name = name;
        this.cityId = cityId;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Integer getCityId() {
        return cityId;
    }


}
