package com.ruoyi.common.utils.digest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: Http Digest Request contains POST、GET、PUT
 * @author: zhangxiulin
 * @time: 2021/2/1 22:21
 */
public class HttpDigestRequestUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpDigestRequestUtils.class);

    public static int nc = 0;

    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";

    /**
     * 向指定URL发送DELETE方法的请求
     * @param url                                   发送请求的URL
     * @param param                                 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param username                              验证所需的用户名
     * @param password                              验证所需的密码
     * @param json                                  请求json字符串
     * @param type                                  返回xml和json格式数据，默认xml，传入json返回json数据
     * @return URL                                  所代表远程资源的响应结果
     */
    public static String sendDelete(String url, String param, String username, String password, String json, String type) {

        logger.info("sendDelete - url:{} param:{} username:{} password:{} json:{} type:{}", url, param, username, password, json, type);

        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String wwwAuth = sendGet(url, param);       //发起一次授权请求
            if (wwwAuth.startsWith("WWW-Authenticate:")) {
                wwwAuth = wwwAuth.replaceFirst("WWW-Authenticate:", "");
            } else {
                return wwwAuth;
            }
            nc ++;
            String urlNameString = url + (StringUtils.isNotEmpty(param) ? "?" + param : "");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();

            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            // Read from the connection. Defaultis true.
            connection.setDoInput(true);
            // 默认是 GET方式
            connection.setRequestMethod(DELETE);

            // 设置通用的请求属性
            setRequestProperty(connection, wwwAuth, realUrl, username, password, DELETE, type);

            if (!StringUtils.isEmpty(json)) {
                byte[] writebytes =json.getBytes();
                connection.setRequestProperty("Content-Length",String.valueOf(writebytes.length));
                OutputStream outwritestream = connection.getOutputStream();
                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }

            if (connection.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                String errResult = formatResultInfo(connection, type);
                logger.info(errResult);
                return errResult;
            }

            nc = 0;
        } catch (Exception e) {
            nc = 0;
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e2) {
                logger.error("关闭流异常", e2);
            }
        }
        return result.toString();
    }



    /**
     * 向指定URL发送PUT方法的请求
     * @param url                                      发送请求的URL
     * @param param                                    请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param username                                 验证所需的用户名
     * @param password                                 验证所需的密码
     * @param json                                     请求json字符串
     * @param type                                     返回xml和json格式数据，默认xml，传入json返回json数据
     * @return URL                                     所代表远程资源的响应结果
     */
    public static String sendPut(String url, String param, String username, String password, String json, String type) {

        logger.info("sendPut - url:{} param:{} username:{} password:{} json:{} type:{}", url, param, username, password, json, type);

        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String wwwAuth = sendGet(url, param);       //发起一次授权请求
            if (wwwAuth.startsWith("WWW-Authenticate:")) {
                wwwAuth = wwwAuth.replaceFirst("WWW-Authenticate:", "");
            } else {
                return wwwAuth;
            }
            nc ++;
            String urlNameString = url + (StringUtils.isNotEmpty(param) ? "?" + param : "");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();

            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            // Read from the connection. Defaultis true.
            connection.setDoInput(true);
            // 默认是 GET方式
            connection.setRequestMethod(PUT);
            // Post 请求不能使用缓存
            connection.setUseCaches(false);

            // 设置通用的请求属性
            setRequestProperty(connection, wwwAuth,realUrl, username, password, PUT, type);

            if (!StringUtils.isEmpty(json)) {
                byte[] writebytes =json.getBytes();
                connection.setRequestProperty("Content-Length",String.valueOf(writebytes.length));
                OutputStream outwritestream = connection.getOutputStream();
                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }

            if (connection.getResponseCode() == 200) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                String errResult = formatResultInfo(connection, type);
                logger.info(errResult);
                return errResult;
            }

            nc = 0;
        } catch (Exception e) {
            nc = 0;
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e2) {
                logger.error("关闭流异常", e2);
            }
        }
        return result.toString();
    }

    /**
     * 向指定URL发送POST方法的请求
     * @param url                                       发送请求的URL
     * @param param                                     请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param username                                  验证所需的用户名
     * @param password                                  验证所需的密码
     * @param json                                      请求json字符串
     * @param type                                      返回xml和json格式数据，默认xml，传入json返回json数据
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, String username, String password, String json, String type) {

        logger.info("sendPost - url:{} param:{} username:{} password:{} json:{} type:{}", url, param, username, password, json, type);

        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String wwwAuth = sendGet(url, param);       //发起一次授权请求
            if (wwwAuth.startsWith("WWW-Authenticate:")) {
                wwwAuth = wwwAuth.replaceFirst("WWW-Authenticate:", "");
            } else {
                return wwwAuth;
            }
            nc ++;
            String urlNameString = url + (StringUtils.isNotEmpty(param) ? "?" + param : "");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();

            // 设置是否向connection输出，因为这个是post请求，参数要放在
            // http正文内，因此需要设为true
            connection.setDoOutput(true);
            // Read from the connection. Defaultis true.
            connection.setDoInput(true);
            // 默认是 GET方式
            connection.setRequestMethod(POST);
            // Post 请求不能使用缓存
            connection.setUseCaches(false);

            // 设置通用的请求属性
            setRequestProperty(connection, wwwAuth,realUrl, username, password, POST, type);

            if (!StringUtils.isEmpty(json)) {
                byte[] writebytes =json.getBytes();
                connection.setRequestProperty("Content-Length",String.valueOf(writebytes.length));
                OutputStream outwritestream = connection.getOutputStream();
                outwritestream.write(json.getBytes());
                outwritestream.flush();
                outwritestream.close();
            }
            if (connection.getResponseCode() == 200 || connection.getResponseCode() == 201) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
            } else {
                String errResult = formatResultInfo(connection, type);
                logger.info(errResult);
                return errResult;
            }

            nc = 0;
        } catch (Exception e) {
            nc = 0;
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e2) {
                logger.error("关闭流异常", e2);
            }
        }
        return result.toString();
    }


    /**
     * 向指定URL发送GET方法的请求
     * @param url                                       发送请求的URL
     * @param param                                     请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param username                                  验证所需的用户名
     * @param password                                  验证所需的密码
     * @param type                                      返回xml和json格式数据，默认xml，传入json返回json数据
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String username, String password, String type) {

        logger.info("sendGet - url:{} param:{} username:{} password:{} type:{}", url, param, username, password, type);

        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String wwwAuth = sendGet(url, param);       //发起一次授权请求
            if (wwwAuth.startsWith("WWW-Authenticate:")) {
                wwwAuth = wwwAuth.replaceFirst("WWW-Authenticate:", "");
            } else {
                return wwwAuth;
            }
            nc ++;
            String urlNameString = url + (StringUtils.isNotEmpty(param) ? "?" + param : "");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection)realUrl.openConnection();
            // 设置通用的请求属性
            setRequestProperty(connection, wwwAuth,realUrl, username, password, GET, type);
            // 建立实际的连接
            // connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            nc = 0;
        } catch (Exception e) {
            nc = 0;
            throw new RuntimeException(e);
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e2) {
                logger.error("关闭流异常", e2);
            }
        }
        return result.toString();
    }

    /**
     * 生成授权信息
     * @param authorization                             上一次调用返回401的WWW-Authenticate数据
     * @param username                                  用户名
     * @param password                                  密码
     * @return                                          授权后的数据, 应放在http头的Authorization里
     * @throws IOException                              异常
     */
    private static String getAuthorization(String authorization, String uri, String username, String password, String method) throws IOException {

        uri = StringUtils.isEmpty(uri) ? "/" : uri;
        // String temp = authorization.replaceFirst("Digest", "").trim();
        String temp = authorization.replaceFirst("Digest", "").trim().replace("MD5","\"MD5\"");
        // String json = "{\"" + temp.replaceAll("=", "\":").replaceAll(",", ",\"") + "}";
        String json = withdrawJson(authorization);
        // String json = "{ \"realm\": \"Wowza\", \" domain\": \"/\", \" nonce\": \"MTU1NzgxMTU1NzQ4MDo2NzI3MWYxZTZkYjBiMjQ2ZGRjYTQ3ZjNiOTM2YjJjZA==\", \" algorithm\": \"MD5\", \" qop\": \"auth\" }";

        JSONObject jsonObject = JSON.parseObject(json);
        // String cnonce = new String(Hex.encodeHex(Digests.generateSalt(8)));    //客户端随机数
        String cnonce = Digests.generateSalt2(8);
        String ncstr = ("00000000" + nc).substring(Integer.toString(nc).length());     //认证的次数,第一次是1，第二次是2...
        // String algorithm = jsonObject.getString("algorithm");
        String algorithm = jsonObject.getString("algorithm");
        String qop = jsonObject.getString("qop");
        String nonce = jsonObject.getString("nonce");
        String realm = jsonObject.getString("realm");

        String response = Digests.http_da_calc_HA1(username, realm, password,
                nonce, ncstr, cnonce, qop,
                method, uri, algorithm);

        //组成响应authorization
        authorization = "Digest username=\"" + username + "\"," + temp;
        authorization += ",uri=\"" + uri
                + "\",nc=\"" + ncstr
                + "\",cnonce=\"" + cnonce
                + "\",response=\"" + response+"\"";
        return authorization;
    }

    /**
     * 将返回的Authrization信息转成json
     * @param authorization                                     authorization info
     * @return                                                  返回authrization json格式数据 如：String json = "{ \"realm\": \"Wowza\", \" domain\": \"/\", \" nonce\": \"MTU1NzgxMTU1NzQ4MDo2NzI3MWYxZTZkYjBiMjQ2ZGRjYTQ3ZjNiOTM2YjJjZA==\", \" algorithm\": \"MD5\", \" qop\": \"auth\" }";
     */
    private static String withdrawJson(String authorization) {
        String temp = authorization.replaceFirst("Digest", "").trim().replaceAll("\"","");
        // String noncetemp = temp.substring(temp.indexOf("nonce="), temp.indexOf("uri="));
        // String json = "{\"" + temp.replaceAll("=", "\":").replaceAll(",", ",\"") + "}";
        String[] split = temp.split(",");
        Map<String, String> map = new HashMap<>();
        Arrays.asList(split).forEach(c -> {
            String c1 = c.replaceFirst("=",":");
            String[] split1 = c1.split(":");
            map.put(split1[0].trim(), split1[1].trim());
        });
        return JSONObject.toJSONString(map);
    }

    /**
     * 向指定URL发送GET方法的请求
     * @param url                                                   发送请求的URL
     * @param param                                                 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL                                                  所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {

            String urlNameString = url + (StringUtils.isNotEmpty(param) ? "?" + param : "");
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

            connection.connect();

            //返回401时需再次用用户名和密码请求
            //此情况返回服务器的 WWW-Authenticate 信息
            if (((HttpURLConnection) connection).getResponseCode() == 401) {
                Map<String, List<String>> map = connection.getHeaderFields();
                return "WWW-Authenticate:" + map.get("WWW-Authenticate").get(0);
            }

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("get请求发送失败",e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) in.close();
            } catch (Exception e2) {
                logger.error("关闭流异常", e2);
            }
        }
        return result.toString();
    }

    /**
     * HTTP set request property
     *
     * @param connection                            HttpConnection
     * @param wwwAuth                               授权auth
     * @param realUrl                               实际url
     * @param username                              验证所需的用户名
     * @param password                              验证所需的密码
     * @param method                                请求方式
     * @param type                                  返回xml和json格式数据，默认xml，传入json返回json数据
     */
    private static void setRequestProperty(HttpURLConnection connection, String wwwAuth, URL realUrl, String username, String password, String method, String type)
            throws IOException {

        if (type != null && type.equals("json")) {
            // 返回json
            connection.setRequestProperty("accept", "application/json;charset=UTF-8");
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        } else {
            // 返回xml
            if (!method.equals(GET)) {
                connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            }
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            // connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        }
        //授权信息
        String authentication = getAuthorization(wwwAuth, realUrl.getPath(), username, password, method);
        connection.setRequestProperty("Authorization", authentication);
    }

    /**
     * 格式化请求返回信息，支持json和xml格式
     * @param connection                                HttpConnection
     * @param type                                      指定返回数据格式，json、xml，默认xml
     * @return                                          返回数据
     */
    private static String formatResultInfo(HttpURLConnection connection, String type) throws IOException {
        String result = "";
        if (type != null && type.equals("json")) {
            result = String.format("{\"code\":%s, \"msg\":%s}",connection.getResponseCode(),connection.getResponseMessage());
        } else {
            result = String.format(" <?xml version=\"1.0\" encoding=\"UTF-8\" ?> "
                    + " <wmsResponse>"
                    + " <code>%d</code>"
                    + " <msg>%s</msg>"
                    + " </wmsResponse>",connection.getResponseCode(),connection.getResponseMessage());
        }
        return result;
    }

}
