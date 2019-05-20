import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ping {
    public static void main(String[] args) {
        String osName = System.getProperty("os.name");
        System.out.println(osName);
        boolean ping = ping("39.105.135.247", 0, 0);
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
        // 将要执行的ping命令,此命令是windows格式的命令
//        String pingCommand = "ping " + ipAddress + " -n " + pingTimes    + " -w " + timeOut;
        String pingCommand = "ping " + ipAddress;
        // Linux命令如下
        // String pingCommand = "ping" -c " + pingTimes + " -w " + timeOut + ipAddress;
        try {
            System.out.println(pingCommand);
            // 执行命令并获取输出
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            in = new BufferedReader(new InputStreamReader(p.getInputStream(),"gbk"));
            int connectedCount = 0;
            String line = null;
            StringBuffer sb = new StringBuffer();
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            System.out.println("返回值为:" + sb);
            // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            while ((line = in.readLine()) != null) {
                System.out.println(line);
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
