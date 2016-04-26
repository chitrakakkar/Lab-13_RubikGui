package com.Chitra;

import java.sql.*;

public class Main {
    private static String DB_CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static String DB_NAME = "rubikGui";
    private static final String USER = "root";
    private static final String PASS = "password";

    static Statement statement = null;
    static Connection conn = null;
    static ResultSet rs = null;

    public final static String Rubik_Table_Name = "Rubik_Solver";
    public final static String Solver_Name = "Solver_Name";
    public final static String Time_Taken = "Time_Taken";


    //TODO change to your own login and password!!


    private static RubikModel rubikModel;

    public static void main(String[] args)
    {

      if(!setup())
      {
          System.exit(-1);
          System.out.println("I am here");
      }
        if(!loadAllRowAndColumns())
        {
            System.exit(-1);
            System.out.println("Hello");

        }

       RubikForm rubikForm = new RubikForm(rubikModel);
    }

    public static boolean loadAllRowAndColumns() {
        try {
            if (rs != null) {
                rs.close();
            }
            String getAllData = "SELECT * FROM " + Rubik_Table_Name;
            rs = statement.executeQuery(getAllData);

            if (rubikModel == null)
            {
                rubikModel = new RubikModel(rs);
            } else {
                rubikModel.updateResultSet(rs);
            }
            return true;

        }
        catch (Exception e)
        {
            System.out.println("Error loading or reloading rows");
            System.out.println(e);
            e.printStackTrace();
            return false;

        }
    }

    public static boolean setup()
    {
        try {
            try {
                String Driver = "com.mysql.jdbc.Driver";
                Class.forName(Driver); // ?

            } catch (ClassNotFoundException cnfe)

            {
                System.out.println("No database drivers found. Quitting");
                return false;
            }
            conn = DriverManager.getConnection(DB_CONNECTION_URL + DB_NAME, USER, PASS);
            statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            if (!rubikTableExists()) {
                String createTableSQL = "CREATE TABLE " + Rubik_Table_Name + " (  " + Solver_Name + " varchar(100), " + Time_Taken + " Double )";
                statement.executeUpdate(createTableSQL);
                System.out.println("Created rubik table");

                //Add some data
                String addDataSQL = "INSERT INTO " + Rubik_Table_Name + "(" + Solver_Name + "," + Time_Taken + "," + " )" + " VALUES ('CubeStormer II robot', 5.270)";
                statement.executeUpdate(addDataSQL);

                addDataSQL = "INSERT INTO " + Rubik_Table_Name + "(" + Solver_Name + "," + Time_Taken + "," + " )" + "  VALUES ('Fakhri Raihaan', 27.93)";
                statement.executeUpdate(addDataSQL);

                addDataSQL = "INSERT INTO " + Rubik_Table_Name + "(" + Solver_Name + "," + Time_Taken + "," + " )" + " VALUES ('Ruxin Liu', 99.33)";
                statement.executeUpdate(addDataSQL);
                addDataSQL = "INSERT INTO " + Rubik_Table_Name + "(" + Solver_Name + "," + Time_Taken + "," + " )" + " VALUES ('Mats Valk ', 6.27)";
                statement.executeUpdate(addDataSQL);
                System.out.println("Added four rows of data");

            }
            return true;
        }
        catch(SQLException sqle)
        {
            System.out.println("The Rubik table (probably) already exists, verify with following error message.");
            System.out.println(sqle);
            return false;
        }
    }

    private static boolean rubikTableExists() throws SQLException
    {
        String checkTablePresentQuery = " SHOW TABLES LIKE '" + Rubik_Table_Name + " '";
        ResultSet tablesRS = statement.executeQuery(checkTablePresentQuery);
        if (tablesRS.next())
        {    //If ResultSet has a next row, it has at least one row... that must be our table
            return true;
        }
        return false;

    }
    public static void shutdown()
    {
        //A finally block runs whether an exception is thrown or not. Close resources and tidy up whether this code worked or not.
        try {
            if (statement != null) {
                statement.close();
                System.out.println("Statement closed");
            }
        } catch (SQLException se) {
            //Closing the connection could throw an exception too
            se.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();  //Close connection to database
                System.out.println("Database connection closed");
            }
        } catch (SQLException se) {
            se.printStackTrace();
        }

        System.exit(0);
    }
}
