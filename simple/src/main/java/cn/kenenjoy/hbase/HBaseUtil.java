package cn.kenenjoy.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


public class HBaseUtil {
    public static Configuration configuration;
    public static Connection connection;
    public static Admin admin;

    /**
     * 建立连接
     */
    public static void init() {
        configuration = HBaseConfiguration.create();
        configuration.set("hbase.rootdir", "hdfs://localhost:9000/hbase");
        try {
            connection = ConnectionFactory.createConnection(configuration);
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接
     */
    public static void close() {
        try {
            if (null != admin) {
                admin.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建表
     *
     * @param myTableName 表名
     * @param colFamily   列族数组
     * @throws IOException
     */
    public static void createTable(String myTableName, String[] colFamily) throws IOException {
        init();

        TableName tableName = TableName.valueOf(myTableName);
        if (admin.tableExists(tableName)) {
            System.out.println("table exists!");
//            admin.disableTable(tableName);
//            admin.deleteTable(tableName);
        }else{
            HTableDescriptor hTableDescriptor = new HTableDescriptor(tableName);
            for (String str : colFamily) {
                HColumnDescriptor hColumnDescriptor = new HColumnDescriptor(str);
                hTableDescriptor.addFamily(hColumnDescriptor);
            }
            admin.createTable(hTableDescriptor);
        }

        close();
    }

    /**
     * 添加数据
     *
     * @param tableName 表名
     * @param rowkey    行键
     * @param colFamily 列族
     * @param col       列限定符
     * @param val       数据
     * @throws IOException
     */
    public static void insertData(String tableName, String rowkey, String colFamily, String col, String val) throws IOException {
        init();

        Table table = connection.getTable(TableName.valueOf(tableName));
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(col), Bytes.toBytes(val));
        table.put(put);
        table.close();

        close();
    }

    /**
     * 获取数据
     *
     * @param tableName 表名
     * @param rowkey 行键
     * @param colFamily 列族
     * @param col 列限定符
     * @throws IOException
     */
    public static void getData(String tableName, String rowkey, String colFamily, String col) throws IOException {
        init();

        Table table = connection.getTable(TableName.valueOf(tableName));
        Get get = new Get(Bytes.toBytes(rowkey));
        get.addColumn(Bytes.toBytes(colFamily), Bytes.toBytes(col));
        // 获取的result数据是结果集，还需要格式化输出想要的数据才行
        Result result = table.get(get);
        System.out.println(new String(result.getValue(colFamily.getBytes(), col == null ? null : col.getBytes())));
        table.close();

        close();
    }

    public static void main(String[] args) throws IOException {
        HBaseUtil.createTable("person", new String[]{"info"});

        HBaseUtil.insertData("person","row1","info","name","zhangsan");
        HBaseUtil.insertData("person","row1","info","age","18");
        HBaseUtil.insertData("person","row1","info","sex","M");

        HBaseUtil.getData("person","row1","info","name");
        HBaseUtil.getData("person","row1","info","age");
        HBaseUtil.getData("person","row1","info","sex");
    }
}
