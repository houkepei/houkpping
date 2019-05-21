import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping {

    private static Logger logger = LoggerFactory.getLogger(Ping.class);


    public static void main(String[] args) {
        boolean ping = ping("121.69.49.235", 1, 5000);
        System.out.println(ping);
    }

    public static boolean ping(String ipAddress) throws Exception {
        int timeOut = 3000;
        boolean status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        return status;
    }

    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        BufferedReader in = null;
        Runtime r = Runtime.getRuntime();
        String osName = System.getProperty("os.name");
        String pingCommand = null;
        if (osName.toLowerCase().contains("windows")) {
            // 将要执行的ping命令,此命令是windows格式的命令  -n多少次  -w 多少毫秒
            pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;
        } else {
            int second = timeOut / 1000;
            // Linux命令如下  -c多少次 -w多少秒
            pingCommand = "ping - c " + pingTimes + " - w " + second + ipAddress;
        }


        try {
            logger.debug("pingCommand:{}", pingCommand);
            // 执行命令并获取输出
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }

            in = new BufferedReader(new InputStreamReader(p.getInputStream(), "gbk"));
            int connectedCount = 0;
            String line = null;
            // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            while ((line = in.readLine()) != null) {
                logger.debug("line:{}", line);
                connectedCount += getCheckResult(line);
            }
            // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            return connectedCount == pingTimes;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    //若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
    private static int getCheckResult(String line) {  // System.out.println("控制台输出的结果为:"+line);
        Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            return 1;
        }
        return 0;
    }

}
