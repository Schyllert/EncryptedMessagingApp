package server.database;

import org.apache.commons.dbcp2.*;
import java.io.InputStream;
import java.util.Properties;

public class DataSource {

    private static String DRIVER_CLASS_NAME;
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;
    private static int CONN_POOL_SIZE;
    private final BasicDataSource bds = new BasicDataSource();

    static {

        try {
        Properties prop = new Properties();
        String propFileName = "/server/server-config.properties";
        InputStream inputStream = DataSource.class.getResourceAsStream(propFileName);
        prop.load(inputStream);

        DRIVER_CLASS_NAME = prop.getProperty("DB_DRIVER_CLASS_NAME");
        DB_URL = "jdbc:mysql://" + prop.getProperty("db_ip")+":"+prop.getProperty("db_port")+"/"+prop.getProperty("db_name");
        DB_USER = prop.getProperty("db_username");
        DB_PASSWORD= prop.getProperty("db_password");
        CONN_POOL_SIZE = 5;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DataSource() {

        //Set database driver name
        bds.setDriverClassName(DRIVER_CLASS_NAME);
        //Set database url
        bds.setUrl(DB_URL);
        //Set database user
        bds.setUsername(DB_USER);
        //Set database password
        bds.setPassword(DB_PASSWORD);
        //Set the connection pool size
        bds.setInitialSize(CONN_POOL_SIZE);
    }

    private static class DataSourceHolder {
        private static final DataSource INSTANCE = new DataSource();
    }

    public static DataSource getInstance() {
        return DataSourceHolder.INSTANCE;
    }

    public BasicDataSource getBds() {
        return bds;
    }
}
