package major.model;

import com.google.gson.JsonObject;
import java.util.ArrayList;


/**
 * CMCAPI interface
 */
public interface CMCAPI {


    /**
     * Get all the cryptos that is active on the Coin Market Cap.
     * @return Arraylist contains all the currency object with only id,name,symbol
     */
    ArrayList<Currency> getCurrencies();


    /**
     * Add selected crypto in to the selected list.
     * @param c selected currency
     */

    void add(Currency c);


    /**
     * Remove selected crypto from the selected list.
     * @param c selected currency
     */
    void remove(Currency c);


    /**
     * Remove all selected cryptos in to the selected list.
     *
     */
    void clear();

    /**
     * Get the convert result of the two given currencies.
     * @param id selected currency id to convert from.
     * @param convertId selected currency id to convert to.
     * @param amount the amount of currencies to convert.
     * @return The result of the conversion.
     *
     */

    String convert(String id, String convertId, String amount);
//    void message(Message message);


    /**
     * Get the amount of selected cryptos.
     * @return the amount of the select cryptos.
     *
     */
    int getSelectListSize();


    /**
     * Get the ID of the crypto with its name.
     * @return the amount of the select cryptos.
     */
    String getIdByName(String name);


    /**
     * Get the list of the selected crypto.
     * @return list of the selected crypto.
     */


    ArrayList<Currency> getSelectedList();



    /**
     * Load the crypto with API calls if online.
     * @param id selected currency id to load.
     * @return the jsonObject contains the data.
     *
     */
    JsonObject loadCrypto(String id);



    /**
     * Get names of the selected list.
     * @return An arraylist contains the names.
     *
     */



    ArrayList<String> getSelectedNames();

    /**
     * Make API calls to load all the available cryptos.
     */

    void getIdMap();


    /**
     * Check if crypto data exists in the database.
     * @param id the id to check.
     * @return If it exists.
     */


    boolean checkCurrencyExist(String id);


    /**
     * Update the database with the new crypto data.
     * @param currency crypto to update.
     */

    void updateDB(Currency currency);


    /**
     * Add the crypto data into the database.
     * @param currency crypto to add.
     */

    void addDB(Currency currency);


    /**
     * Load the crypto data from the database.
     * @param currency crypto to load.
     */


    Currency loadDB(Currency currency);

    /**
     * Clear the database.
     */

    void clearDB();

    /**
     * Inject http module.
     * @param http http object to inject.
     */

    void injectHttp(Http http);

    /**
     * Inject sql module.
     * @param sql the sql module to inject.
     */

    void injectSQL(Sql sql);

    /**
     * Set the threshold
     * @param threshold the threshold to use.
     */

    boolean setThreshold(String threshold);

    /**
     * See if the rate is between the threshold
     * @param rate the rate to check.
     * @return whether the value is in less than threshold.
     */

    boolean checkThreshold(double rate);
}
