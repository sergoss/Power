package model;

public class Payment extends MenuItem {
    private Integer id;
    private String name;
    private String paymentDetails;

    public Payment(Integer id, String name, String paymentDetails) {
        this.id = id;
        this.name = name;
        this.paymentDetails = paymentDetails;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }
}
