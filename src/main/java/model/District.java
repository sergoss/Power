package model;

public class District extends MenuItem {
    private Integer id;
    private String name;
    private Integer cityId;

    public District(Integer id, String name, Integer cityId) {
        this.id = id;
        this.name = name;
        this.cityId = cityId;
    }
    @Override
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
