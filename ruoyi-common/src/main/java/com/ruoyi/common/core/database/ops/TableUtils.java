package com.ruoyi.common.core.database.ops;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.PatternUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @description:
 * @author: zhangxiulin
 * @time: 2020/12/15 23:54
 */
public class TableUtils {

    private static final Logger log = LoggerFactory.getLogger(TableUtils.class);

    public static final String SQL_PATTERN = "\\#\\{[a-zA-Z0-9_]+\\}";

    public static final int findInt(String[] colArray, String key) throws RuntimeException {
        for(int i = 0; i < colArray.length; ++i) {
            if (colArray[i].equals(key)) {
                return i;
            }
        }

        throw new RuntimeException("没有这个key");
    }

    public static final Object[][] mapValues(ResultSet rs, int colNums) throws SQLException {
        if (rs == null) {
            log.debug("空表不予处理");
            return null;
        } else {
            int rows = 0;
            if (rs.last()) {
                rows = rs.getRow();
            }

            if (rows <= 0) {
                log.debug("空表不予处理");
                return null;
            } else {
                Object[][] tempResult = new Object[rows][colNums];
                log.debug("一阶段赋值已创建储值空间：" + rows + "," + colNums);
                rs.beforeFirst();
                long s1 = System.currentTimeMillis();

                for(int c = 0; rs.next(); ++c) {
                    for(int k = 0; k < colNums; ++k) {
                        new Object();

                        Object v;
                        try {
                            v = rs.getObject(k + 1);
                            if (v == null) {
                                v = "";
                            }
                        } catch (Exception var10) {
                            v = "";
                        }

                        tempResult[c][k] = v;
                    }
                }

                long s2 = System.currentTimeMillis();
                log.debug("数据底层映射耗时：" + (s2 - s1) + "ms");
                return tempResult;
            }
        }
    }

    public static final Object getTempValue(Object[][] tempResult, int rowNum, int colNum) throws Exception {
        return tempResult[rowNum][colNum];
    }

    public static final Object getTempValue(String[] colArray, Object[][] tempResult, int rowNum, String colName) throws Exception {
        int i = 0;
        try {
            i = findInt(colArray, colName);
            return tempResult[rowNum][i];
        } catch (Exception var6) {
            log.error("数据取值映射时发生错误" + rowNum + "，" + colName + "，" + i, var6);
            return null;
        }
    }

    public static final Map<String, Integer> mapColumns(ResultSet rs, Map<String, Integer> colMap) throws SQLException {
        if (!rs.next()) {
            return colMap;
        } else {
            colMap.clear();
            ResultSetMetaData rsmd = rs.getMetaData();
            int cc = rsmd.getColumnCount();

            for(int i = 0; i < cc; ++i) {
                //colMap.put(rsmd.getColumnName(i + 1), i);
                colMap.put(rsmd.getColumnLabel(i + 1), i);
            }

            rsmd = null;
            return colMap;
        }
    }

    public static JSONArray table2Json(Table table){
        JSONArray jsonArray = new JSONArray();
        if (table != null) {
            Map<String, Integer> colMap = table.getColMap();
            Object[][] tempResult = table.getTempResult();
            if (tempResult != null) {
                for (int g = 0, leng = tempResult.length; g < leng; g++) {
                    JSONObject jsonObject = new JSONObject();
                    Set<String> colKeySet = colMap.keySet();
                    Iterator<String> iterator = colKeySet.iterator();
                    while (iterator.hasNext()) {
                        String colName = iterator.next();
                        jsonObject.put(colName, tempResult[g][colMap.get(colName)]);
                    }
                    jsonArray.add(jsonObject);
                }
            }
        }
        return jsonArray;
    }

    public static List prepareParamsList(String sql, Object sendData){
        // 预编译STMT动态参数，格式：#{}
        // 规则：{ 参数名 : 参数值 }
        List<Object[]> list = new ArrayList<>(1);
        List<String> matchedStrs = PatternUtils.getMatchedStrs(SQL_PATTERN, sql);
        // 判断单笔批量
        if (sendData != null) {
            if (sendData instanceof List) {
                ((List) sendData).forEach(d -> {  // 批量
                    Object[] params = null;
                    // 动态参数数组
                    params = new Object[matchedStrs.size()];
                    for (int j = 0, lenj = matchedStrs.size(); j < lenj; j++) {
                        params[j] = ((Map) d).get(matchedStrs.get(j).substring(2, matchedStrs.get(j).length() - 1));
                    }
                    list.add(params);
                });
            } else {
                Object[] params = null;
                // 动态参数数组
                params = new Object[matchedStrs.size()];
                for (int j = 0, lenj = matchedStrs.size(); j < lenj; j++) {
                    params[j] = ((Map) sendData).get(matchedStrs.get(j).substring(2, matchedStrs.get(j).length() - 1));
                }
                list.add(params);
            }
        }
        return list;
    }

    public static Object[] prepareParams(String sql, Object sendData){
        // 预编译STMT动态参数，格式：#{}
        // 规则：{ 参数名 : 参数值 }
        List<String> matchedStrs = PatternUtils.getMatchedStrs(SQL_PATTERN, sql);

        Object[] params = null;
        if (sendData != null) {
            // 动态参数数组
            params = new Object[matchedStrs.size()];
            for (int j = 0, lenj = matchedStrs.size(); j < lenj; j++) {
                params[j] = ((Map) sendData).get(matchedStrs.get(j).substring(2, matchedStrs.get(j).length() - 1));
            }
        }
        return params;
    }

    public static String prepareSql(String sql){
        // 预编译STMT动态参数，格式：#{}
        // 规则：{ 参数名 : 参数值 }
        List<String> matchedStrs = PatternUtils.getMatchedStrs(SQL_PATTERN, sql);
        // 类MyBatis的语法 -> “?”占位的预编译SQL格式
        for (int j = 0, lenj = matchedStrs.size(); j < lenj; j++) {
            sql = sql.replaceAll("\\#\\{" + matchedStrs.get(j).substring(2, matchedStrs.get(j).length() - 1) + "\\}", "?");
        }
        return sql;
    }

    public static Table resultSet2Table(ResultSet rs) throws SQLException{
        Table table = new Table();
        if (rs != null) {
            table = table.addColumns(TableUtils.mapColumns(rs, new HashMap()));
            Object[][] r = TableUtils.mapValues(rs, table.getColMap().size());
            if (r != null && r.length > 0) {
                table = table.addData(r);
            }
        }
        return table;
    }

}
