package model.impl;

import entity.Ticket;
import model.ITicketDAO;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 采取单例，节省资源.
 * */
public class TicketDAOImpl implements ITicketDAO{
    private volatile static TicketDAOImpl instance = null;

    private TicketDAOImpl(){}

    public static TicketDAOImpl getInstance(){
        if(instance == null){
            synchronized (TicketDAOImpl.class){
                if(instance == null){
                    instance = new TicketDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public int save(int foreignerId, double price, String type, String buyTime, int times,boolean back) {
        int backing = back ? 1 : 0;
        String sql = "insert into ticket(foreignerId,price,type,buyTime,times,backing) values(?,?,?,?,?,?)";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql,foreignerId,price,type,buyTime,times,backing);
    }

    @Override
    public int delete(int foreignerId) {
        String sql = "delete from ticket where foreignerId = ?";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql,foreignerId);
    }

    @Override
    public int update(int ticketId, int times) {
        String sql = "update ticket set times = ? where ticketId = ?";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql,times,ticketId);
    }

    @Override
    public int update(int ticketId,boolean back) {
        int backing = back ? 1 : 0;
        String sql = "update ticket set backing = ? where ticketId = ?";
        return DBUtils.executeUpdate(DBUtils.getConnection(),sql,backing,ticketId);
    }

    @Override
    public Ticket get(int foreignerId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from ticket where foreignerId = ?";
            if(conn != null){
                ps = conn.prepareStatement(sql);
                ps.setInt(1,foreignerId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    boolean backing = rs.getInt("backing")==1;
                    return new Ticket(rs.getInt("ticketId"),rs.getInt("foreignerId"),rs.getFloat("price"),
                            rs.getString("type"),rs.getString("buyTime"),rs.getInt("times"),backing);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }

    @Override
    public List<Ticket> getListByBacking(boolean back) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Ticket> tickets = new ArrayList<>();
        int backing = back ? 1 : 0;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from ticket where backing = ?";
            if(conn != null){
                ps = conn.prepareStatement(sql);
                ps.setInt(1,backing);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Ticket ticket = new Ticket(rs.getInt("ticketId"),rs.getInt("foreignerId"),rs.getFloat("price"),
                            rs.getString("type"),rs.getString("buyTime"),rs.getInt("times"),back);
                    tickets.add(ticket);
                }
            }
            return tickets;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn,ps, rs);
        }
        return null;
    }
}
