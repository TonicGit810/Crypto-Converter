package major.model;
import java.io.File;
import java.sql.*;




/**
 *Sql class makes all the sql calls. Including create class.
 */
public class Sql {


    private static final String dbName = "currency.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;

    /**
     * Constructor of currency object the API is for the lazy load design pattern.
     */
    public Sql(){
        createDB();
        setupDB();

    }

    /**
     * Create database
     */
    public static void createDB() {
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

    /**
     * remove database
     */


    public static void removeDB() {
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


    /**
     * Create table
     */

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


    /**
     * Add currency into the database
     * @param id id of the crypto
     * @param name name of the crypto
     * @param description description of the crypto
     * @param symbol symbol of the crypto
     * @param logo logo of the crypto
     * @param date date of the crypto
     * @param website website of the crypto
     */


    public void addCurrency(String id, String name, String description, String symbol, String logo, String date, String website) {
            String addCurrency =
                """
                INSERT INTO Currency VALUES
                    (?,?,?,?,?,?,?)
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addCurrency)) {
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

    /**
     * clear the table
     */

    public void clearCache(){

        String clear =
                """
                DELETE FROM Currency
                WHERE id in (SELECT id from Currency)
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(clear)) {
            preparedStatement.executeUpdate();

            System.out.println("clear currency data");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }

    }

    /**
     * update the exists crypto
     * @param id id of the crypto
     * @param name name of the crypto
     * @param description description of the crypto
     * @param symbol symbol of the crypto
     * @param logo logo of the crypto
     * @param date date of the crypto
     * @param website website of the crypto
     */

    public void updateCurrency(String id, String name, String description, String symbol, String logo, String date, String website){
        String updateSQl =
                """
                update Currency
                SET id = ?, name = ?, description = ?, symbol = ?, logo = ?, date = ?, website = ?
                WHERE id = ?
                """;
        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(updateSQl)) {
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


    /**
     * Load currency with given id
     * @param id the id of the crypto.
     * @return the loaded crypto.
     */


    public Currency loadCurrency(String id) {
        String loadSQL =
                """
                SELECT *
                FROM currency
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(loadSQL)) {
            preparedStatement.setString(1, id);

            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {


                return new Currency(results.getString("id"),results.getString("name"),results.getString("symbol"),results.getString("description"),results.getString("date"),results.getString("website"),results.getString("logo"));
            }

            System.out.println("load currency data");
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return null;
    }


    /**
     * Load currency with given id
     * @param id the id of the crypto.
     * @return if crypto exists
     */

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

        }

        return  false;



    }


}
