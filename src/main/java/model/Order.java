package model;

public class Order {
    private Integer id;
    private Integer orderDetails;
    private City orderCity;
    private District orderDistrict;
    private Product orderProduct;
    private Payment orderPayment;
    private User orderUser;

    private int a = 10000;
    private int b = 99999;

    public Order() {
        this.orderDetails = a + (int) (Math.random() * b);
    }

    public Order(User orderUser) {
        this.orderUser = orderUser;
        this.orderDetails = a + (int) (Math.random() * b);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(Integer orderDetails) {
        this.orderDetails = orderDetails;
    }

    public City getOrderCity() {
        return orderCity;
    }

    public void setOrderCity(City orderCity) {
        this.orderCity = orderCity;
    }

    public District getOrderDistrict() {
        return orderDistrict;
    }

    public void setOrderDistrict(District orderDistrict) {
        this.orderDistrict = orderDistrict;
    }

    public Product getOrderProduct() {
        return orderProduct;
    }

    public void setOrderProduct(Product orderProduct) {
        this.orderProduct = orderProduct;
    }

    public Payment getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(Payment orderPayment) {
        this.orderPayment = orderPayment;
    }

    public User getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(User orderUser) {
        this.orderUser = orderUser;
    }
}
