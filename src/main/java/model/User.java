package model;

public class User extends MenuItem {
    private Integer id;
    private String nick;
    private Long chatId;
    private Integer stateId;
    private Boolean notify = false;

    public User() {
    }

    public User(Long chatId, Integer stateId) {
        this.chatId = chatId;
        this.stateId = stateId;
    }

    public User(Long chatId, Integer stateId, Boolean notify) {
        this.chatId = chatId;
        this.stateId = stateId;
        this.notify = notify;
    }

    public User(String nick, Long chatId, Integer stateId, Boolean notify) {
        this.nick = nick;
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

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

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
}
