package com.example.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.Entity.Users;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class Dao {

    public static String questionMarkGeneration(int length) {
        String questionMarks = "";
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                questionMarks = questionMarks + '?';
                break;
            }
            questionMarks = questionMarks + "?,";
        }
        questionMarks = questionMarks + ')';
        return questionMarks;
    }

    public static LinkedHashMap<String, Object[]> paramsConversion(LinkedHashMap<String, String[]> queryParams) {
        LinkedHashMap<String, Object[]> convertedQueryParams = new LinkedHashMap<>();
        Users u = new Users();
        java.lang.reflect.Field[] field = u.getClass().getDeclaredFields();
        for (Entry<String, String[]> m : queryParams.entrySet()) {
            for (java.lang.reflect.Field fieldvalue : field) {
                if (m.getKey().equals(fieldvalue.getName())) {
                    if (fieldvalue.getType().toString().equals("int")) {
                        String[] values = m.getValue();
                        Object[] convertedvalue = new Object[values.length];
                        for (int i = 0; i < values.length; i++) {
                            int value = Integer.parseInt(values[i]);
                            convertedvalue[i] = value;
                        }
                        convertedQueryParams.put(m.getKey(), convertedvalue);
                    }

                }
            }
        }
        return convertedQueryParams;
    }

    public static List<String> getColumnName(LinkedHashMap<String, String[]> queryParams) {
        List<String> columnName = new ArrayList<>();
        return columnName;

    }

    public static LinkedHashMap<String, String[]> getQueryParameters(HttpServletRequest request)
            throws UnsupportedEncodingException {
        LinkedHashMap<String, String[]> queryParameters = new LinkedHashMap<>();
        String queryString = request.getQueryString();
        if (queryString.length() != 0) {
            queryString = URLDecoder.decode(queryString, StandardCharsets.UTF_8.toString());
            String[] parameters = queryString.split("&");
            for (String parameter : parameters) {
                String[] keyValuePair = parameter.split("=");
                String[] values = queryParameters.get(keyValuePair[0]);
                values = keyValuePair.length == 1 ? ArrayUtils.add(values, "")
                        : ArrayUtils.addAll(values, keyValuePair[1].split(","));
                queryParameters.put(keyValuePair[0], values);
            }
        }
        return queryParameters;
    }

    /**
     * @param Tablename
     * @param tablevalues
     * @throws Exception
     */

    public static int insertToTable(String Tablename, LinkedHashMap<String, Object> tablevalues) throws Exception {
        int lengthofthecolumn = tablevalues.size();
        int rowsAffected = 0;
        String questionMark = "(";
        questionMark = questionMark + Dao.questionMarkGeneration(lengthofthecolumn);
        StringBuilder query = new StringBuilder("INSERT INTO \"" + Tablename + "\"(");
        for (Map.Entry<String, Object> m : tablevalues.entrySet()) {
            query.append(m.getKey());
            query.append(',');
        }
        query.deleteCharAt(query.length() - 1);
        query.append(')' + " VALUES" + '(' + questionMarkGeneration(lengthofthecolumn));
        query.deleteCharAt(query.length() - 1);
        query.append(')' + ";");
        Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = DbConnection.connect_to_db();
            stmt = con.prepareStatement(query.toString());
            int i = 1;
            for (Map.Entry<String, Object> m : tablevalues.entrySet()) {
                stmt.setObject(i, m.getValue());
                i++;
            }
            rowsAffected = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stmt.close();
            con.close();
        }
        return rowsAffected;
    }

    public static JSONArray selectFromTable(String string, List<String> columnName,
            HashMap<String, String[]> queryParams) throws Exception {
        StringBuilder query = new StringBuilder("SELECT ");
        int lengthofthecolumn = columnName.size();
        if (lengthofthecolumn != 0) {
            for (int i = 0; i < lengthofthecolumn; i++) {
                query.append(columnName.get(i));
                query.append(',');
            }
        } else
            query.append(" * ");
        query.deleteCharAt(query.length() - 1);
        query.append(" FROM " + "\"" + string + "\"");
        if (!(queryParams.isEmpty())) {
            query.append(" WHERE ");
            int length = queryParams.size();
            for (Entry<String, String[]> m : queryParams.entrySet()) {
                query.append(m.getKey());
                int len=m.getValue().length;
                if(len>1){
                    query.append(" IN (");
                }
                else{
                    query.append(" = ");
                }
                for (String foo : m.getValue()) {
                    if (foo instanceof String) {
                        query.append("'");
                        query.append(foo.toString());
                        query.append("'");
                        query.append(",");
                    } else {
                        query.append(foo.toString());
                        query.append(",");
                    }
                }
                query.deleteCharAt(query.length()-1);
                if(len>1) query.append(") ");
                if (length != 1)
                    query.append(" AND ");
                length--;
            }
            query.append(";");
        }
        try (Connection con = DbConnection.connect_to_db();
                PreparedStatement stmt = con.prepareStatement(query.toString());
                ResultSet res = stmt.executeQuery()) {
            List<JSONObject> resList = new ArrayList<JSONObject>();
            try {
                ResultSetMetaData rsMeta = res.getMetaData();
                int columnCnt = rsMeta.getColumnCount();
                List<String> columnNames = new ArrayList<String>();
                for (int i = 1; i <= columnCnt; i++) {
                    columnNames.add(rsMeta.getColumnName(i));
                }
                while (res.next()) {
                    JSONObject obj = new JSONObject();
                    for (int i = 1; i <= columnCnt; i++) {
                        String key = columnNames.get(i - 1);
                        Object value = res.getObject(i);
                        obj.put(key, value);
                    }
                    resList.add(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    res.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            JSONArray jsonArray = new JSONArray(resList);
            return jsonArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteFromTable(String tableName, List<String> columnNames, HashMap<String, String> queryParams)
            throws Exception {
        StringBuilder query = new StringBuilder("DELETE ");
        int lengthOfTheColumn = columnNames.size();
        if (lengthOfTheColumn > 0) {
            for (int i = 0; i < lengthOfTheColumn; i++) {
                query.append(columnNames.get(lengthOfTheColumn));
                query.append(',');
            }
        } else {
            query.append("*");
            query.append(',');
        }
        query.deleteCharAt(query.length() - 1);
        query.append(" FROM ");
        query.append(tableName);
        try {
            if (!queryParams.isEmpty()) {
                query.append(" WHERE ");
                int length = queryParams.size();
                for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                    query.append(entry.getKey()).append(" = ? ");
                    if (length != 1)
                        query.append(" AND ");
                    length--;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (Connection con = DbConnection.connect_to_db();
                PreparedStatement stmt = con.prepareStatement(query.toString())) {
            int index = 1;
            for (String value : queryParams.values()) {
                stmt.setString(index++, value);
            }
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int updateTheTable(String tableName, LinkedHashMap<String, Object> setParams,
            HashMap<String, Object> conditionParams) throws Exception {
        StringBuilder query = new StringBuilder("UPDATE \"");
        int rowsAffected = 0;
        query.append(tableName);
        query.append("\" SET ");
        int setParamsSize = setParams.size();
        int index = 1;
        for (Map.Entry<String, Object> entry : setParams.entrySet()) {
            query.append(entry.getKey()).append(" = ?");
            if (index < setParamsSize) {
                query.append(", ");
            }
            index++;
        }
        query.append(" WHERE ");
        int conditionParamsSize = conditionParams.size();
        index = 1;
        for (Map.Entry<String, Object> entry : conditionParams.entrySet()) {
            query.append(entry.getKey()).append(" = ?");
            if (index < conditionParamsSize) {
                query.append(" AND ");
            }
            index++;
        }
        try (Connection con = DbConnection.connect_to_db();
                PreparedStatement stmt = con.prepareStatement(query.toString())) {
            index = 1;
            for (Object value : setParams.values()) {
                stmt.setObject(index++, value);
            }
            for (Object value : conditionParams.values()) {
                stmt.setObject(index++, value);
            }
            rowsAffected = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    public static JSONArray selectwithJoins(String[] Tablenames, String[] primarykey,
    List<String> columnName,
    HashMap<String, String[]> queryParams) throws Exception {
    StringBuilder query = new StringBuilder("SELECT ");
    int lengthofthecolumn = columnName.size();
    if (lengthofthecolumn != 0) {
        for (int i = 0; i < lengthofthecolumn; i++) {
            query.append(columnName.get(i));
            query.append(',');
        }
    } else
        query.append(" * ");
    query.deleteCharAt(query.length() - 1);
    query.append(" FROM ");
    query.append("\"").append(Tablenames[0]).append("\" ");
    for (int i = 1; i < Tablenames.length; i++) {
        query.append(" LEFT JOIN ").append(Tablenames[i]).append(" ON ")
                .append(Tablenames[i - 1]).append(".").append(primarykey[i - 1]).append(" =");
        query.append(Tablenames[i]).append(".").append(primarykey[i - 1]);
    }
    if (!(queryParams.isEmpty())) {
        query.append(" WHERE ");
        int length = queryParams.size();
        for (Entry<String, String[]> m : queryParams.entrySet()) {
            query.append(m.getKey()).append(" = ?");
            if (length != 1)
                query.append(" AND ");
            length--;
        }
    }
    query.append(";");
    try (Connection con = DbConnection.connect_to_db();
            PreparedStatement stmt = con.prepareStatement(query.toString())) {
        int parameterIndex = 1; 
        for (Map.Entry<String, String[]> entry : queryParams.entrySet()) {
            String[] values = entry.getValue();
            if (values != null) {
                for (String value : values) {
                    stmt.setObject(parameterIndex, Integer.valueOf(value)); 
                }
            }
            parameterIndex++;
        }
        ResultSet res = stmt.executeQuery();
            List<JSONObject> resList = new ArrayList<JSONObject>();
            try {
                ResultSetMetaData rsMeta = res.getMetaData();
                int columnCnt = rsMeta.getColumnCount();
                List<String> columnNames = new ArrayList<String>();
                for (int i = 1; i <= columnCnt; i++) {
                    columnNames.add(rsMeta.getColumnName(i));
                }
                while (res.next()) {
                    JSONObject obj = new JSONObject();
                    for (int i = 1; i <= columnCnt; i++) {
                        String key = columnNames.get(i - 1);
                        Object value = res.getObject(i);
                        obj.put(key, value);
                    }
                    resList.add(obj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                res.close();
            }
            JSONArray jsonArray = new JSONArray(resList);
            return jsonArray;
        }
    }
}
