package entity;
/**
 * 信物.
 * 外邦人与信物是一对一的关系.
 * 这里使用进入龙之谷的有效次数来代替有效时间.
 * */
public class Ticket {
    private int ticketId;
    private int foreignerId;
    private double price;
    private String type;
    private String buyTime;
    private int times;//表示票的有效次数
    private boolean backing;//是否在退票状态
    private boolean checked = false;//处理退票的时候用，标记checked是否被checkbox打勾

    //票的类型
    public static final String TYPE1 = "一等票";
    public static final String TYPE2 = "二等票";
    public static final String TYPE3 = "三等票";

    //票的价格
    public static final double PRICE1 = 20.0;
    public static final double PRICE2 = 10.0;
    public static final double PRICE3 = 5.0;

    //退票的价格。一次有效次数==一个Back_Price
    public static final double Back_Price = 1.0;

    //票的有效次数
    public static final int TIMES1 = 15;
    public static final int TIMES2 = 5;
    public static final int TIMES3 = 1;

    public Ticket(int ticketId, int foreignerId, double price, String type, String buyTime, int times, boolean backing) {
        this.ticketId = ticketId;
        this.foreignerId = foreignerId;
        this.price = price;
        this.type = type;
        this.buyTime = buyTime;
        this.times = times;
        this.backing = backing;
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

    public int getTicketId() {
        return ticketId;
    }

    public int getForeignerId() {
        return foreignerId;
    }

    public void setForeignerId(int foreignerId) {
        this.foreignerId = foreignerId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

}
