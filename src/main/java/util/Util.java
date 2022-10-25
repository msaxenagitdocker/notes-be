package util;

import domain.BaseResponse;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 */
public class Util {
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 100;
    private static final int CODE_ERROR = 100;

    public static BaseResponse getSuccessResponse() {
        BaseResponse response = new BaseResponse();
        response.setStatus(SUCCESS_STATUS);
        response.setCode(CODE_SUCCESS);
        return response;
    }

    public static BaseResponse getErrorResponse() {
        BaseResponse response = new BaseResponse();
        response.setStatus(ERROR_STATUS);
        response.setCode(CODE_ERROR);
        return response;
    }

    /**
     *
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void closeAll(ResultSet resultSet, Statement statement, Connection connection) {
        try {
            if(resultSet != null) resultSet.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(statement != null) statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if(connection != null) connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
