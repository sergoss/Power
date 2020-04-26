package model;

public class Product extends MenuItem {
    private Integer id;
    private String name;
    private Integer price;
    private Double weight;
    private Integer cityId;
    private Integer districtId;

    public Product(Integer id, String name, Integer price, Double weight, Integer cityId, Integer districtId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.cityId = cityId;
        this.districtId = districtId;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Double getWeight() {
        return weight;
    }

    public Integer getCityId() {
        return cityId;
    }

    public Integer getDistrictId() {
        return districtId;
    }
}
