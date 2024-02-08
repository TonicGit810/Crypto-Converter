package major.model;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class SQL {


    private static final String dbName = "currency.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;


    public SQL(){
        createDB();
        setupDB();

    }


    public void createDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            System.out.println("Database already created");
            return;
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            // If we get here that means no exception raised from getConnection - meaning it worked
            System.out.println("A new database has been created.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }


    public void removeDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            boolean result = dbFile.delete();
            if (!result) {
                System.out.println("Couldn't delete existing db file");
                System.exit(-1);
            } else {
                System.out.println("Removed existing DB file.");
            }
        } else {
            System.out.println("No existing DB file.");
        }
    }




    public static void setupDB() {

        String createCurrencyTableSQL =
                """
                CREATE TABLE IF NOT EXISTS Currency (
                    id TEXT PRIMARY KEY,
                    name TEXT,
                    description TEXT,
                    symbol TEXT,
                    logo TEXT,
                    date TEXT,
                    website TEXT
                                        
                );
                """;



        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(createCurrencyTableSQL);


            System.out.println("Created tables");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }


    public void addCurrency(String id, String name, String description, String symbol, String logo, String date, String website) {
            String addUser =
                """
                INSERT INTO Currency VALUES
                    (?,?,?,?,?,?,?)
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addUser)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, symbol);
            preparedStatement.setString(5, logo);
            preparedStatement.setString(6, date);
            preparedStatement.setString(7, website);

            preparedStatement.executeUpdate();

            System.out.println("Added currency data");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void clearCache(){

        String deleteUser =
                """
                DELETE FROM Currency
                WHERE id in (SELECT id from Currency)
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(deleteUser)) {
            preparedStatement.executeUpdate();

            System.out.println("clear currency data");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

    }

    public void updateCurrency(String id, String name, String description, String symbol, String logo, String date, String website){
        String enrolmentSQL =
                """
                update Currency
                SET id = ?, name = ?, description = ?, symbol = ?, logo = ?, date = ?, website = ?
                WHERE id = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(enrolmentSQL)) {
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, symbol);
            preparedStatement.setString(5, logo);
            preparedStatement.setString(6, date);
            preparedStatement.setString(7, website);
            preparedStatement.setString(8, id);

            preparedStatement.executeUpdate();

            System.out.println("updated currency data");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }


    public Currency loadCurrency(String id) {
        String studentRangeSQL =
                """
                SELECT *
                FROM currency
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(studentRangeSQL)) {
            preparedStatement.setString(1, id);

            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {


                return new Currency(results.getString("id"),results.getString("name"),results.getString("symbol"),results.getString("description"),results.getString("date"),results.getString("website"),results.getString("logo"));
            }

            System.out.println("Finished simple query");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return null;
    }



    public boolean checkCurrency(String id){
        String enrolmentSQL =
                """
                SELECT id
                FROM currency
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(enrolmentSQL)) {
            preparedStatement.setString(1, id);
            ResultSet results = preparedStatement.executeQuery();


            return results.next();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

        return  false;



    }


}
