package major.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;
/**
 *CMCAPI Offline mode.
 */
public class CMCInputOffline implements CMCAPI {


    private ArrayList<Currency> currencies;
    private ArrayList<Currency> selected_list;

    private ArrayList<Message> messages;

    private double threshold;





    /**
     * Constructor, add fake data since offline.
     */

    public CMCInputOffline(){
        this.currencies = new ArrayList<>();
        this.selected_list = new ArrayList<>();
        this.messages = new ArrayList<>();

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");
        currencies.add(bit1);
        currencies.add(bit2);


    }

    /**
     * Get all the cryptos that is active on the Coin Market Cap.
     * @return Arraylist contains all the currency object with only id,name,symbol.
     */
    @Override
    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    /**
     * Add selected crypto in to the selected list.
     * @param c selected currency
     */


    @Override
    public void add(Currency c) {
        selected_list.add(c);

    }

    /**
     * Remove selected crypto from the selected list.
     * @param c selected currency.
     */

    @Override
    public void remove(Currency c) {
        this.selected_list.remove(c);

    }

    /**
     * Remove all selected cryptos in to the selected list.
     */

    @Override
    public void clear() {
        this.selected_list.clear();

    }






    /**
     * Get the convert result of the two given currencies.
     * @param id selected currency id to convert from.
     * @param convertId selected currency id to convert to.
     * @param amount the amount of currencies to convert.
     * @return The result of the conversion.
     *
     */
    @Override
    public String convert(String id, String convertId, String amount) {
        try{
            double damount = Double.parseDouble(amount);

            long cap = 1000000000000L;
            if(damount < 0.00000001 || damount > cap){


                return "Out Range";
            }
        }catch(NumberFormatException e){
            return "Wrong Type";
        }

        return "0.5";
    }



    /**
     * Get the amount of selected cryptos.
     * @return the amount of the select cryptos.
     *
     */

    @Override
    public int getSelectListSize() {
        return selected_list.size();
    }


    /**
     * Get the ID of the crypto with its name.
     * @return the amount of the select cryptos.
     */

    @Override
    public String getIdByName(String name) {
        for (int i = 0; i<getSelectListSize();i++){
            if(name.equals(selected_list.get(i).getName())){
                return selected_list.get(i).getId();
            }


        }

        return null;
    }


    /**
     * Get the list of the selected crypto.
     * @return list of the selected crypto.
     */

    @Override
    public ArrayList<Currency> getSelectedList() {
        return selected_list;
    }


    /**
     * Do nothing
     * @param id selected currency id to load.
     * @return null.
     */

    @Override
    public JsonObject loadCrypto(String id) {
        return null;
    }


    /**
     * Get names of the selected list.
     * @return An arraylist contains the names
     *
     */


    @Override
    public ArrayList<String> getSelectedNames() {
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i<getSelectListSize(); i++){
            list.add(selected_list.get(i).getName());

        }

        return list;
    }


    /**
     * Do nothing since it is offline mode.
     */

    @Override
    public void getIdMap() {

    }


    /**
     * Do nothing since it is offline mode.
     * @return false
     */

    @Override
    public boolean checkCurrencyExist(String id) {
        return false;
    }


    /**
     * Do nothing since it is offline mode.
     *  @param currency
     */

    @Override
    public void updateDB(Currency currency) {

    }

    /**
     * Do nothing since it is offline mode
     *  @param currency
     */

    @Override
    public void addDB(Currency currency) {

    }


    /**
     * Do nothing since it is offline mode
     *  @param currency
     */

    @Override
    public Currency loadDB(Currency currency) {
        return null;
    }


    /**
     * Do nothing since it is offline mode.
     */

    @Override
    public void clearDB() {

    }


    /**
     * Do nothing since it is offline mode.
     *  @param http
     */

    @Override
    public void injectHttp(Http http) {

    }

    /**
     * Do nothing since it is offline mode.
     *  @param sql
     */

    @Override
    public void injectSQL(Sql sql) {

    }
    
    /**
     * Set the threshold.
     * @param threshold the threshold to set
     * @return whether the threshold is valid or not.
     */

    @Override
    public boolean setThreshold(String threshold) {

        try{
            double dthreshold = Double.parseDouble(threshold);
            if(dthreshold <0.1|| dthreshold >1){

                return false;
            }else{
                this.threshold = dthreshold;
                return true;
            }


        }catch(NumberFormatException e){
            return false;
        }

    }

    /**
     * See if the rate is between the threshold
     * @param rate the rate to check.
     * @return whether the value is in between the threshold.
     */

    @Override
    public boolean checkThreshold(double rate) {
        if (rate < threshold){
            return false;
        }else{
            return true;
        }
    }


}
