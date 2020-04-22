package entity;

/**
 * 账目实体类.
 * */
public class Account {
    private int accountId;
    private int foreignerId;
    private double money;
    private String createTime;
    private String status;//表示状态，即购买/退货.

    public Account() {
    }

    public Account(int accountId, int foreignerId, double money, String createTime, String status) {
        this.accountId = accountId;
        this.foreignerId = foreignerId;
        this.money = money;
        this.createTime = createTime;
        this.status = status;
    }

    public int getAccountId() {
        return accountId;
    }

    public int getForeignerId() {
        return foreignerId;
    }

    public void setForeignerId(int foreignerId) {
        this.foreignerId = foreignerId;
    }

    public double getMoney() {
        return money;
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getStatus() {
        return status;
    }

}
