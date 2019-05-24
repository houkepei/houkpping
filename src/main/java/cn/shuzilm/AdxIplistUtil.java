package cn.shuzilm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Description: ip名单
 * @Author: houkp
 * @CreateDate: 2018/12/12 11:48
 * @UpdateUser: houkp
 * @UpdateDate: 2018/12/12 11:48
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class AdxIplistUtil {

    private static AdxIplistUtil adxIplistUtil = null;

    private static List adxIpList = null;

    private static final Logger LOG = LoggerFactory.getLogger(AdxIplistUtil.class);


    public static AdxIplistUtil getInstance() {
        if (adxIplistUtil == null) {
            adxIplistUtil = new AdxIplistUtil();
        }
        return adxIplistUtil;
    }

    public AdxIplistUtil() {
//        String fileTest = "C:\\Users\\houkp\\Desktop\\adxip.txt";
        String fileTest = "/home/srvadmin/adxip.txt";
        File file = new File(fileTest);
        adxIpList = getIpBlacklist(file);

    }

    /**
     * 加载ip名单
     *
     * @param file
     * @return
     */
    private static List getIpBlacklist(File file) {
        System.out.println("ip_list:" + file.isFile());
        BufferedReader bufferedReader = null;
        List ipList = new ArrayList();
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            int i = 1;
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(i++ + "行：" + line);
                ipList.add(line);
            }

            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipList;
    }

    public static List getIplist() {
        return adxIpList;
    }


    public static void main(String[] args) {
        AdxIplistUtil.getInstance();
    }
}
