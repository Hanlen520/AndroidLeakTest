package android.example.xinxi.leaktest;


import android.util.Log;
import com.squareup.leakcanary.AnalysisResult;
import com.squareup.leakcanary.DisplayLeakService;
import com.squareup.leakcanary.HeapDump;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by xinxi
 * 修改reqLeakInfo方法中post请求的body改成json拼接方式
 * reqLeakInfo方法删除createtime参数，增加uid参数
 *
 */

public class LeakUploadService extends DisplayLeakService {

    private String class_name = "";
    private String pkg_name = "";
    private String pkg_ver = "";
    private String leak_detail = "";

    @Override
    protected void afterDefaultHandling(HeapDump heapDump, AnalysisResult result, String leakInfo) {

        Log.i("LeakUploadService", "解析内存泄漏");

        // 当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String create_time = format.format(cal.getTime()).substring(0,19);

        //if (!result.leakFound || result.excludedLeak) {
        //    return;
        //}

        Log.i("LeakUploadService", "开始上报内存泄漏");
        Log.i("LeakUploadService","泄漏信息"+leakInfo.toString());
        try {
            // 处理内存泄漏数据
            class_name = result.className.toString();
            pkg_name = leakInfo.trim().split(":")[0].split(" ")[1];
            pkg_ver = leakInfo.trim().split(":")[1];
            leak_detail = leakInfo.split("\n\n")[0] + "\n\n" + leakInfo.split("\n\n")[1];
        }catch (Exception e){
            e.printStackTrace();
            Log.e("LeakUploadService",  e.toString());
        }


        // 系统版本
        String os_version = "7.0";
        // 系统机型
        String device_name = "小米";

        int uid = 0;
        // 获取用户uid

        // 保存到数据库
        try {
            reqLeakInfo(class_name, pkg_name, pkg_ver, leak_detail, os_version, device_name, uid);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("LeakUploadService",  e.toString());
        }

    }

    public static StringBuffer reqLeakInfo(String class_name, String pkg_name,
                                           String pkg_ver, String leak_detail, String os_version, String device_name, int uid) throws JSONException {
        HttpURLConnection connection = null;

        JSONObject body = new JSONObject();
        body.put("class_name",class_name);
        body.put("pkg_name",pkg_name);
        body.put("pkg_ver",pkg_ver);
        body.put("leak_detail",leak_detail);
        body.put("os_version",os_version);
        body.put("device_name",device_name);
        body.put("uid",String.valueOf(uid));

        try {
            String urlString = "https://backend.luojilab.com/leak/leaksave";

            URL url = new URL(urlString);
            // 打开http连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求方式
            connection.setRequestMethod("POST");
            // 设置编码格式
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("cache-control", "no-cache");
            connection.setRequestProperty("Postman-Token", "c0abe493-174f-4e66-b0da-fc328fb60b9f");
            connection.setRequestProperty("User-Agent", "PostmanRuntime/6.4.1");
            connection.setRequestProperty("Accept", "*/*");
            connection.setRequestProperty("Host", "backend.luojilab.com");
            connection.setRequestProperty("accept-encoding", "gzip, deflate");
            //connection.setRequestProperty("content-length", "258");
            // 设置这个连接是否可以输出数据
            connection.setDoOutput(true);
            // 是否可以写入数据
            connection.setDoInput(true);
            //设置消息的类型
            //connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            // 连接，从上述至此的配置必须要在connect之前完成，实际上它只是建立了一个与服务器的TCP连接
            connection.connect();

            // 输出流，用来发送请求，http请求实际上直到这个函数里面才正式发送出去
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            //用此方法向服务器端发送数据
            dataOutputStream.write(body.toString().getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();

            Log.i("LeakUploadService", "code = " + connection.getResponseCode());

            StringBuffer buffer = new StringBuffer();
            // 得到服务端的返回码是否连接成功
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String str = null;
                // BufferedReader特有功能，一次读取一行数据
                while ((str = bufferedReader.readLine()) != null) {
                    buffer.append(str);
                }
                inputStream.close();
                bufferedReader.close();
            } else {
                Log.i("LeakUploadService", "连接失败, code = " + connection.getResponseCode());
            }
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 使用完关闭TCP连接，释放资源
            connection.disconnect();
        }

        return null;
    }

}
