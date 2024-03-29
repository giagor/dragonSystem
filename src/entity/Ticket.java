package entity;

/**
 * 信物.
 * 外邦人与信物是一对一的关系.
 * 这里使用进入龙之谷的有效次数来代替有效时间.
 */
public class Ticket {
    private int ticketId;
    private int foreignerId;
    private double price;
    private String type;
    private String buyTime;
    private int times;//表示票的有效次数
    private boolean backing;//是否在退票状态

    public Ticket() {
    }

    public Ticket(int ticketId, int foreignerId, double price, String type, String buyTime, int times, boolean backing) {
        this.ticketId = ticketId;
        this.foreignerId = foreignerId;
        this.price = price;
        this.type = type;
        this.buyTime = buyTime;
        this.times = times;
        this.backing = backing;
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public int getForeignerId() {
        return foreignerId;
    }

    public void setForeignerId(int foreignerId) {
        this.foreignerId = foreignerId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(String buyTime) {
        this.buyTime = buyTime;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public boolean isBacking() {
        return backing;
    }

    public void setBacking(boolean backing) {
        this.backing = backing;
    }
}
