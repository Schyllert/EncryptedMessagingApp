package server.application;

import server.security.BCrypt;
import server.models.User;
import server.models.users.UserFactory;
import java.sql.*;

public class Authenticator {

    /**
     * Returns an User object that contains the information about a stored user if username/password
     * match a stored user in the database.
     *
     * @param username the username registered by the user
     * @param password the secret plain-text password chosen by the user
     * @param conn
     * @return User if properly verified with the database or null if verification failed.
     * @throws SQLException
     */
    public static User login(String username, String password, Connection conn) throws SQLException {


    //  A simple call to the close() method will do the job. If you close the Connection object first,
    //  it will close the Statement object as well. However, you should always explicitly close the Statement object to ensure proper cleanup.

    //  A simple call to the close() method will do the job. If you close the Connection object first, it will close the CallableStatement
    //  object as well. However, you should always explicitly close the CallableStatement object to ensure proper cleanup.

        if (username == null || password == null) {
            if (conn != null) {
                conn.close();
                return null;
            }
            return null;
        }

        try (conn) {
           String salt = getSalt(username, conn);
            if (salt != null) {
                if (checkHash(password, username, salt, conn)) {
                    return UserFactory.createUser(username, getRole(username, conn));
                }
            }
            return null;
        }
    }

    /**
     *
     * @param password
     * @param username
     * @param salt
     * @param conn
     * @return
     * @throws SQLException
     */
    private static Boolean checkHash(String password, String username, String salt, Connection conn) throws SQLException {

        if (password == null || username == null || conn == null) {
            return false;
        }

        String hashedPassword = BCrypt.hashpw(password, salt);
        CallableStatement cStmt = null;
        ResultSet rs = null;

        try {

            boolean hadResults;
            String stored_hash;
            cStmt = conn.prepareCall("{call getPassword(?)}");
            cStmt.setString(1, username);
            hadResults = cStmt.execute();

            if (hadResults) {
                rs = cStmt.getResultSet();
                if (rs.next()) {
                    stored_hash = rs.getString("passwordh");
                    //Todo: add lockout time for that account
                    if (stored_hash != null) {
                        return stored_hash.compareTo(hashedPassword) == 0;
                    }
                    return false;
                }
            }
        }

        finally {
            if (cStmt != null) {
                cStmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return false;
    }

    /**
     *
     * @param username
     * @param conn
     * @return
     * @throws SQLException
     */
    private static String getSalt(String username, Connection conn) throws SQLException {

        if (conn == null || username == null) {
            return null;
        }

        String salt = null;
        boolean hadResults;
        ResultSet rs = null;
        CallableStatement cStmt = null;

        try {
            /*
            Connection.prepareCall() is an expensive method, due to the metadata retrieval that the driver performs
            to support output parameters. For performance reasons, minimize unnecessary calls to Connection.prepareCall()
            by reusing CallableStatement instances in your code.
            https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-usagenotes-statements-callable.html

            Parameter	            Default	Description
            poolPreparedStatements	false	Enable prepared statement pooling for this pool.
            ^ is this relevant to CallableStatement?
             */

            cStmt = conn.prepareCall("{call getSalt(?)}");
            cStmt.setString(1, username);
            hadResults = cStmt.execute();

            if (hadResults) {
                rs = cStmt.getResultSet();
                while (rs.next()) {
                    salt = rs.getString("salth");
                }
            }

            return salt;
        }
        finally {
            if (rs != null) {
                rs.close();
            }
            if (cStmt != null) {
                cStmt.close();
            }
        }
    }

    /**
     *
     * @param username
     * @param conn
     * @return
     * @throws SQLException
     */
    private static String getRole(String username, Connection conn) throws SQLException {

        CallableStatement cStmt = null;
        ResultSet rs = null;
        boolean hadResults = false;
        String role = null;

        if (username == null || conn == null) {
            return null;
        }

        try {
            cStmt = conn.prepareCall("{call getRoleh(?)}");
            cStmt.setString(1, username);
            hadResults = cStmt.execute();

            if (hadResults) {
                rs = cStmt.getResultSet();
                while (rs.next()) {
                   role = rs.getString("roleh");
                }
            }
            return role;
        }

        finally {
            if (cStmt != null) {
                cStmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }
}
