package cn.kenenjoy.presto;


import java.sql.*;

/**
 * Created by hefa on 2019-02-27.
 */
public class PrestoUtil {
    static {
        try {
            Class.forName("com.facebook.presto.jdbc.PrestoDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Connection connection;

    private static void getConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:presto://hefadeMacBook-Pro:8080/hive/default","hefa",null);
    }

    private static void closeConnection(){
        if (connection!=null){
            try {
                if (!connection.isClosed()){
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void select(String sql) {
        try {
            long start = System.currentTimeMillis();
            getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                System.out.print(resultSet.getString("id")+"\t");
                System.out.print(resultSet.getString("name1")+"\t");
                System.out.print(resultSet.getString("age")+"\t");
                System.out.print(resultSet.getString("salary")+"\t");
                System.out.println();
            }
            resultSet.close();
            long end = System.currentTimeMillis();
            System.out.println("Presto查询耗时:"+ (end - start)+"毫秒");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    public static void main(String[] args){
//        String sql = "select count(*) as counts from customer";
        String sql = "SELECT c.id,c.name AS name1,n.name AS name2, c.age, c.salary,  n.address, n.phone from customer c INNER JOIN newcustomer n on c.id = n.id WHERE c.salary < 100000 ORDER BY c.salary LIMIT 10";
//        String sql = "SELECT c.id,c.name AS name1, c.age, c.salary from customer c WHERE c.salary < 80000 ORDER BY c.salary LIMIT 10";
//        String sql = "SELECT c.id,c.name AS name1, c.age, c.salary from customer c WHERE c.salary < 80000 LIMIT 10";

        PrestoUtil.select(sql);
    }


}
