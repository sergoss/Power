package model;

public class User extends MenuItem {

    private Integer id;
//    private String nick;
    private Long chatId;
    private Integer stateId;
    private Boolean notify = false;
    private Order order;

    public User() {
    }

    public User(Integer id, Long chatId, Integer stateId, Boolean notify) {
        this.id = id;
        this.chatId = chatId;
        this.stateId = stateId;
        this.notify = notify;
    }

    public User(Long chatId, Integer stateId) {
        this.chatId = chatId;
        this.stateId = stateId;
    }

    public User(Integer id, Long chatId) {
        this.id = id;
        this.chatId = chatId;
    }

    public User(Long chatId, Integer stateId, Boolean notify) {
        this.chatId = chatId;
        this.stateId = stateId;
        this.notify = notify;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public String getNick() {
//        return nick;
//    }
//
//    public void setNick(String nick) {
//        this.nick = nick;
//    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public Boolean getNotify() {
        return notify;
    }

    public void setNotify(Boolean notify) {
        this.notify = notify;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
