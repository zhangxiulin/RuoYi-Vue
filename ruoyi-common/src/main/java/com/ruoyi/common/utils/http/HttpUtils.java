package com.ruoyi.common.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.security.cert.X509Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.enums.HttpMethod;
import com.ruoyi.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ruoyi.common.constant.Constants;

/**
 * 通用http发送方法
 * 
 * @author ruoyi
 */
public class HttpUtils
{
    private static final Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param)
    {
        return sendGet(url, param, Constants.UTF8);
    }

    public static String sendGetWithBearerToken(String url, String param, String token)
    {
        return sendGetWithBearerToken(url, param, Constants.UTF8, token);
    }

    /**
     * 向指定 URL 发送GET方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @param contentType 编码类型
     * @return 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, String contentType)
    {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try
        {
            String urlNameString = url + "?" + param;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpUtil.sendGet Exception, url=" + url + ",param=" + param, e);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (Exception ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    public static String sendGetWithBearerToken(String url, String param, String contentType, String token)
    {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try
        {
            String urlNameString = url + "?" + param;
            log.info("sendGetWithBearerToken - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setRequestProperty("authorization", "Bearer " + token);
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendGetWithBearerToken ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendGetWithBearerToken SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendGetWithBearerToken IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpUtil.sendGetWithBearerToken Exception, url=" + url + ",param=" + param, e);
        }
        finally
        {
            try
            {
                if (in != null)
                {
                    in.close();
                }
            }
            catch (Exception ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            String urlNameString = url;
            log.info("sendPost - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line;
            while ((line = in.readLine()) != null)
            {
                result.append(line);
            }
            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendPost ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendPost IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtils.sendPost Exception, url=" + url + ",param=" + param, e);
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }

    public static String sendSSLPost(String url, String param)
    {
        StringBuilder result = new StringBuilder();
        String urlNameString = url + "?" + param;
        try
        {
            log.info("sendSSLPost - {}", urlNameString);
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
            URL console = new URL(urlNameString);
            HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("contentType", "utf-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setSSLSocketFactory(sc.getSocketFactory());
            conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String ret = "";
            while ((ret = br.readLine()) != null)
            {
                if (ret != null && !"".equals(ret.trim()))
                {
                    result.append(new String(ret.getBytes("ISO-8859-1"), "utf-8"));
                }
            }
            log.info("recv - {}", result);
            conn.disconnect();
            br.close();
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendSSLPost ConnectException, url=" + url + ",param=" + param, e);
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendSSLPost SocketTimeoutException, url=" + url + ",param=" + param, e);
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendSSLPost IOException, url=" + url + ",param=" + param, e);
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtils.sendSSLPost Exception, url=" + url + ",param=" + param, e);
        }
        return result.toString();
    }

    /**
     * GET方式提交
     */
    public static String sendGet( String url, Map<String, Object> paraMap) throws Exception{
        String queryString = "";
        if (paraMap != null){
            StringBuilder psb = new StringBuilder();
            paraMap.forEach((k, v) -> {
                psb.append(k).append("=").append(String.valueOf(v)).append("&");
            });
            if (psb.length() > 0){
                queryString = psb.substring(0, psb.length() - 1);
            }
        }
        return sendGet(url, queryString);
    }

    public static String sendGetWithBearerToken( String url, Map<String, Object> paraMap, String token) throws Exception{
        String queryString = "";
        if (paraMap != null){
            StringBuilder psb = new StringBuilder();
            paraMap.forEach((k, v) -> {
                psb.append(k).append("=").append(String.valueOf(v)).append("&");
            });
            if (psb.length() > 0){
                queryString = psb.substring(0, psb.length() - 1);
            }
        }
        return sendGetWithBearerToken(url, queryString, token);
    }

    public static String sendJsonPost(String url, Map<String, Object> paraMap) throws Exception{
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            String urlNameString = url;
            log.info("sendJsonPost - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("accept","application/json");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            out = new PrintWriter(conn.getOutputStream());

            String json = "";
            try {
                json = new JSONObject(paraMap).toJSONString();
                log.debug("数据处理完成: " + json);
            } catch (RuntimeException e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            } catch (Exception e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            }
            if (StringUtils.isNotEmpty(json)){
                out.print(json);
                out.flush();
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                while ((line = in.readLine()) != null)
                {
                    result.append(line);
                }
            }

            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendJsonPost ConnectException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendJsonPost SocketTimeoutException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendJsonPost IOException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("调用HttpUtils.sendJsonPost Exception, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + paraMap, ex);
            }
        }
        return result.toString();
    }

    public static String sendJsonPostWithBearerToken(String url, Map<String, Object> paraMap, String token) throws Exception{
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            String urlNameString = url;
            log.info("sendJsonPostWithBearerToken - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("accept","application/json");
            conn.setRequestProperty("authorization", "Bearer " + token);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            out = new PrintWriter(conn.getOutputStream());

            String json = "";
            try {
                json = new JSONObject(paraMap).toJSONString();
                log.debug("数据处理完成: " + json);
            } catch (RuntimeException e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            } catch (Exception e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            }
            if (StringUtils.isNotEmpty(json)){
                out.print(json);
                out.flush();
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                while ((line = in.readLine()) != null)
                {
                    result.append(line);
                }
            }

            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendJsonPostWithBearerToken ConnectException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendJsonPostWithBearerToken SocketTimeoutException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendJsonPostWithBearerToken IOException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("调用HttpUtils.sendJsonPostWithBearerToken Exception, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + paraMap, ex);
            }
        }
        return result.toString();
    }

    public static String sendJsonPut(String url, Map<String, Object> paraMap) throws Exception{
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            String urlNameString = url;
            log.info("sendJsonPut - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("accept","application/json");
            conn.setRequestMethod(HttpMethod.PUT.name());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            out = new PrintWriter(conn.getOutputStream());

            String json = "";
            try {
                json = new JSONObject(paraMap).toJSONString();
                log.debug("数据处理完成: " + json);
            } catch (RuntimeException e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            } catch (Exception e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            }
            if (StringUtils.isNotEmpty(json)){
                out.print(json);
                out.flush();
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                while ((line = in.readLine()) != null)
                {
                    result.append(line);
                }
            }

            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendJsonPut ConnectException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendJsonPut SocketTimeoutException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendJsonPut IOException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtils.sendJsonPut Exception, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + paraMap, ex);
            }
        }
        return result.toString();
    }

    public static String sendJsonPutWithBearerToken(String url, Map<String, Object> paraMap, String token) throws Exception{
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            String urlNameString = url;
            log.info("sendJsonPutWithBearerToken - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("accept","application/json");
            conn.setRequestProperty("authorization", "Bearer " + token);
            conn.setRequestMethod(HttpMethod.PUT.name());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            out = new PrintWriter(conn.getOutputStream());

            String json = "";
            try {
                json = new JSONObject(paraMap).toJSONString();
                log.debug("数据处理完成: " + json);
            } catch (RuntimeException e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            } catch (Exception e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            }
            if (StringUtils.isNotEmpty(json)){
                out.print(json);
                out.flush();
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                while ((line = in.readLine()) != null)
                {
                    result.append(line);
                }
            }

            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendJsonPutWithBearerToken ConnectException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendJsonPutWithBearerToken SocketTimeoutException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendJsonPutWithBearerToken IOException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtils.sendJsonPutWithBearerToken Exception, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + paraMap, ex);
            }
        }
        return result.toString();
    }

    public static String sendJsonDelete(String url, Map<String, Object> paraMap) throws Exception{
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            String urlNameString = url;
            log.info("sendJsonDelete - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("accept","application/json");
            conn.setRequestMethod(HttpMethod.DELETE.name());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            out = new PrintWriter(conn.getOutputStream());

            String json = "";
            try {
                json = new JSONObject(paraMap).toJSONString();
                log.debug("数据处理完成: " + json);
            } catch (RuntimeException e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            } catch (Exception e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            }
            if (StringUtils.isNotEmpty(json)){
                out.print(json);
                out.flush();
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                while ((line = in.readLine()) != null)
                {
                    result.append(line);
                }
            }

            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendJsonDelete ConnectException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendJsonDelete SocketTimeoutException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendJsonDelete IOException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtils.sendJsonDelete Exception, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + paraMap, ex);
            }
        }
        return result.toString();
    }

    public static String sendJsonDeleteWithBearerToken(String url, Map<String, Object> paraMap, String token) throws Exception{
        PrintWriter out = null;
        BufferedReader in = null;
        StringBuilder result = new StringBuilder();
        try
        {
            String urlNameString = url;
            log.info("sendJsonDeleteWithBearerToken - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestProperty("accept","application/json");
            conn.setRequestProperty("authorization", "Bearer " + token);
            conn.setRequestMethod(HttpMethod.DELETE.name());
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(20000);
            conn.setReadTimeout(20000);
            out = new PrintWriter(conn.getOutputStream());

            String json = "";
            try {
                json = new JSONObject(paraMap).toJSONString();
                log.debug("数据处理完成: " + json);
            } catch (RuntimeException e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            } catch (Exception e) {
                log.error("数据转换失败", e);
                throw new Exception("数据转换失败", e);
            }
            if (StringUtils.isNotEmpty(json)){
                out.print(json);
                out.flush();
                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                String line;
                while ((line = in.readLine()) != null)
                {
                    result.append(line);
                }
            }

            log.info("recv - {}", result);
        }
        catch (ConnectException e)
        {
            log.error("调用HttpUtils.sendJsonDeleteWithBearerToken ConnectException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (SocketTimeoutException e)
        {
            log.error("调用HttpUtils.sendJsonDeleteWithBearerToken SocketTimeoutException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (IOException e)
        {
            log.error("调用HttpUtils.sendJsonDeleteWithBearerToken IOException, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        catch (Exception e)
        {
            log.error("调用HttpsUtils.sendJsonDeleteWithBearerToken Exception, url=" + url + ",param=" + paraMap, e);
            throw e;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
                if (in != null)
                {
                    in.close();
                }
            }
            catch (IOException ex)
            {
                log.error("调用in.close Exception, url=" + url + ",param=" + paraMap, ex);
            }
        }
        return result.toString();
    }

    private static class TrustAnyTrustManager implements X509TrustManager
    {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
        {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)
        {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier
    {
        @Override
        public boolean verify(String hostname, SSLSession session)
        {
            return true;
        }
    }
}