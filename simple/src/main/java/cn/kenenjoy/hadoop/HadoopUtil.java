package cn.kenenjoy.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by hefa on 2017/8/24.
 */
public class HadoopUtil {
    public static void main(String[] args) throws IOException, URISyntaxException {
        URI uri = new URI("hdfs://localhost:9000");
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(uri, conf);

        Path filename = new Path("/user/hefa/README.md");

        FSDataOutputStream outputStream =  fs.append(filename);
        outputStream.writeUTF("hello, Hadoop!");
        outputStream.close();

        FSDataInputStream is = fs.open(filename);//
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));



        String content;
        while ((content = bufferedReader.readLine()) != null) {
            System.out.println(content);
        }
        bufferedReader.close();
        fs.close();
    }

}
