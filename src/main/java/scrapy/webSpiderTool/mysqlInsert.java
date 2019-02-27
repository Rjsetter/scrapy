package scrapy.webSpiderTool;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class mysqlInsert {
    final static Logger Log = Logger.getLogger(createThread.class);

    public static void insert(String sql) {
        Connection conn = BaseConnection.getConnection();
        PreparedStatement ps = null;
        try {
//            ps = conn.prepareStatement(sql);//把写好的sql语句传递到数据库，让数据库知道我们要干什么
//            ps.executeUpdate();//这个方法用于改变数据库数据，a代表改变数据库的条数
            Statement state = conn.createStatement();   //容器
            state.executeUpdate(sql);
//            if (a > 0) {
//                System.out.println("添加成功");
//            } else {
//                System.out.println("添加失败");
//            }
        } catch (SQLException e3) {
            Log.error(e3);
        } catch (Exception e) {
            Log.error(e);
//            e.printStackTrace();
        }
        try {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e2) {
            Log.error(e2);
//            e2.printStackTrace();
        }
    }
}
