package cn.kenenjoy.util;

import java.io.File;

/**
 * Created by hefa on 2018/1/11.
 */
public class FileComparator implements Comparable<FileComparator> {
    public FileComparator(File file) {
        this.file = file;
    }

    private File file;

    public File getFile() {
        return file;
    }

    /**
     * 按最后修改时间排序，时间越早越前
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(FileComparator o) {
//        if (file.lastModified() < o.file.lastModified()) {
//            return -1;
//        } else if (file.lastModified() == o.file.lastModified()) {
//            return 0;
//        } else {
//            return 1;
//        }

        return file.getName().compareTo(o.file.getName());


    }

}
