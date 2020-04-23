package model.database;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.ResourceBundle;

/**
 * 连接池.
 * */
public class MyDataSource extends DataSourceAdapter{
    private static final int initConnections = 5;
    private static final int poolMaxIdleConnections = 10;//空闲池允许的最大的连接数量
    private static final int poolMaxActiveConnections = 10;
    private static LinkedList<Connection> idleConnections = new LinkedList<>();//空闲的连接池
    private static LinkedList<Connection> activeConnections = new LinkedList<>();//激活的连接池

    private static ResourceBundle bundle = ResourceBundle.getBundle("resource/jdbc");
    private static String driver = bundle.getString("driver");
    private static String url = bundle.getString("url");
    private static String username = bundle.getString("username");
    private static String password = bundle.getString("password");

    private final Object monitor = new Object();//用于同步

    static {
        try {
            Class.forName(driver);
            //初始化连接池的数量
            for (int i = 0; i < initConnections; i++) {
                idleConnections.addFirst(DriverManager.getConnection(url, username, password));
            }
//            System.out.println("初始化----空闲:"+idleConnections.size()+"  激活:"+activeConnections.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        Connection conn = null;

        while(conn == null){
            //同步
            synchronized (monitor){
                //如果空闲连接池不为空，直接获取连接
                if(!idleConnections.isEmpty()){
                    conn = idleConnections.removeLast();
                }else{
                    //没有空闲连接可以使用，那么我们需要获取新的连接(也就是需要传建一个连接)
                    if(activeConnections.size() < poolMaxActiveConnections){
                        //如果当前激活的连接数 小于 我们允许的最大连接数，那么此时可以创建一个新的连接，否则还不能创建
                        conn = DriverManager.getConnection(url,username,password);
                    }
                    //否则是不能创建新连接的，需要等待，wait
                }

                if(conn == null){
                    try {
//                        System.out.println(Thread.currentThread().getName()+"等待"+"  空闲:"+idleConnections.size()
//                                +"  激活:"+activeConnections.size());
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //线程给打断则跳出循环
                        break;
                    }
                }else{
                    //对象不为空，说明已经拿到了该连接
                    activeConnections.add(conn);
//                    System.out.println(Thread.currentThread().getName() + "拿到----"+conn+"  空闲:"+idleConnections.size()
//                            +"  激活:"+activeConnections.size());
                }
            }//synchronizedEnd

        }

        //返回连接代理对象
        Connection finalConn = conn;
        return (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(), new Class[]{Connection.class},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
                        if ("close".equals(method.getName())) {
                            synchronized (monitor){
                                activeConnections.remove(finalConn);
                                if(idleConnections.size() < poolMaxIdleConnections){
                                    idleConnections.addFirst(finalConn);
                                }
                                monitor.notifyAll();

//                                System.out.println(Thread.currentThread().getName()+"归还   "+finalConn+
//                                        "   空闲:"+idleConnections.size()+"  激活:"+activeConnections.size());
                            }
                        } else {
                            return method.invoke(finalConn, args);
                        }
                        return null;
                    }
                });

    }

}
