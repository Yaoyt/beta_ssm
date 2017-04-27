package com.beta.basic.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by yaoyt on 17/4/27.
 *
 * @author yaoyt
 */

@Component("impalaCommonDao")
public class ImpalaCommonDao {

    @Autowired()
    @Qualifier("impalaDataSource")
    private DataSource impalaDataSource;


    public void impalaTest() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = impalaDataSource.getConnection();
            preparedStatement = connection.prepareStatement("select * from testdril.recipient_egroup_24 order by create_date desc limit ?;");
            preparedStatement.setInt(1,Integer.valueOf("10")); // 这里将问号赋值
            resultSet = preparedStatement.executeQuery();
            System.out.println("\n== Begin Query Results ======================");
            while (resultSet.next()) {
                // the example query returns one String column
                System.out.println(resultSet.getString(1).concat("  ").
                        concat(resultSet.getString(2) == null ? "" : resultSet.getString(2)).concat("  ").
                        concat(resultSet.getString(3) == null ? "" : resultSet.getString(3)).concat("  ").
                        concat(resultSet.getString(4) == null ? "" : resultSet.getString(4)));
            }
            System.out.println("== End Query Results =======================\n\n");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally{
            try {
                connection.close();
            } catch (Exception e) {
                // swallow
            }
        }
    }
}
