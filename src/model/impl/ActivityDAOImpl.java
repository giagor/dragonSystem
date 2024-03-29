package model.impl;

import entity.Activity;
import model.IActivityDAO;
import util.DBUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAOImpl implements IActivityDAO {
    private volatile static ActivityDAOImpl instance = null;

    private ActivityDAOImpl() {
    }

    public static ActivityDAOImpl getInstance() {
        if (instance == null) {
            synchronized (ActivityDAOImpl.class) {
                if (instance == null) {
                    instance = new ActivityDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public int save(int dragonGroupId, String name, String content, String startTime, String overTime) {
        String sql = "insert into activity(dragonGroupId,name,content,startTime,overTime) values(?,?,?,?,?)";
        return DBUtils.executeUpdate(DBUtils.getConnection(), sql, dragonGroupId, name, content, startTime, overTime);
    }

    @Override
    public Activity getById(int activityId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from activity where activityId = ?";
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, activityId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return new Activity(rs.getInt("activityId"), rs.getInt("dragonGroupId"), rs.getString("name"),
                            rs.getString("content"), rs.getString("startTime"), rs.getString("overTime"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public Activity getByTimeAndId(int activityId, LocalDate time) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from activity where activityId = ?";
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                ps.setInt(1, activityId);
                rs = ps.executeQuery();
                if (rs.next()) {
                    Activity activity = new Activity(rs.getInt("activityId"), rs.getInt("dragonGroupId"),
                            rs.getString("name"), rs.getString("content"), rs.getString("startTime"),
                            rs.getString("overTime"));

                    LocalDate startTime = LocalDate.parse(activity.getStartTime());
                    LocalDate overTime = LocalDate.parse(activity.getOverTime());

                    if (!((time.isBefore(startTime) || time.isAfter(overTime)))) {
                        return activity;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Activity> getValidList(LocalDate time) {
        List<Activity> activities = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from activity";
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    LocalDate startTime = LocalDate.parse(rs.getString("startTime"));
                    LocalDate overTime = LocalDate.parse(rs.getString("overTime"));
                    //传入的时间在活动的 开始时间-结束时间  之间
                    if (!(time.isBefore(startTime) || time.isAfter(overTime))) {
                        Activity activity = new Activity(rs.getInt("activityId"), rs.getInt("dragonGroupId"),
                                rs.getString("name"), rs.getString("content"), rs.getString("startTime"),
                                rs.getString("overTime"));
                        activities.add(activity);
                    }
                }
            }
            return activities;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        return null;
    }

    @Override
    public List<Activity> getList() {
        List<Activity> activities = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "select * from activity";
            if (conn != null) {
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Activity activity = new Activity(rs.getInt("activityId"), rs.getInt("dragonGroupId"),
                            rs.getString("name"), rs.getString("content"), rs.getString("startTime"),
                            rs.getString("overTime"));
                    activities.add(activity);
                }
            }
            return activities;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtils.close(conn, ps, rs);
        }
        return null;
    }
}
