package major.model;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;


import java.util.ArrayList;

/**
 *CMCAPI Online mode
 */

public class CMCInputOnline implements CMCAPI {

    private ArrayList<Currency> currencies;
    private ArrayList<Currency> selected_list;




    ArrayList<LinkedTreeMap<String,Object>> IdMap;

    private Http http;

    private Sql sql;

    private double threshold;


    /**
     * Constructor
     */
    public CMCInputOnline(){
        this.currencies = new ArrayList<>();
        this.selected_list = new ArrayList<>();




    }



    /**
     * Get all the cryptos that is active on the Coin Market Cap.
     * @return Arraylist contains all the currency object with only id,name,symbol
     */
    public ArrayList<Currency> getCurrencies(){

        currencies.clear();
        getIdMap();

        if(IdMap == null){
            return null;
        }
        for (int i = 0; i<IdMap.size(); i++){

            double d = (double) IdMap.get(i).get("id");
            int id = (int)d;
            String name = (String) IdMap.get(i).get("name");
            String symbol = (String) IdMap.get(i).get("symbol");



            currencies.add(new Currency(this,Integer.toString(id),name,symbol));

        }

        return this.currencies;
    }




    /**
     * Get the amount of selected cryptos.
     * @return the amount of the select cryptos.
     *
     */

    public ArrayList<Currency> getSelectedList(){return this.selected_list;}

    /**
     * Load the crypto with API calls
     * @param id selected currency id to load.
     * @return the jsonObject contains the data
     *
     */

    @Override
    public JsonObject loadCrypto(String id) {


        try{
            return http.getCrypto(id);

        }catch (Exception e){
            throw new IllegalArgumentException();
        }

    }

    /**
     * add selected crypto in to the selected list.
     * @param c selected currency
     */

    public void add(Currency c){

        this.selected_list.add(c);

    }

    /**
     * remove selected crypto from the selected list.
     * @param c selected currency
     */
    public void remove(Currency c){
        this.selected_list.remove(c);
    }



    /**
     * remove all selected cryptos in to the selected list.
     *
     */
    public void clear(){
        this.selected_list.clear();
    }

    /**
     * Clear the database
     */
    public void clearDB(){

        try{
            sql.clearCache();
        }catch(Exception e){

            throw new IllegalArgumentException();

        }


    }


    /**
     * Get the convert result of the two given currencies.
     * @param id selected currency id to convert from.
     * @param convertId selected currency id to convert to.
     * @param amount the amount of currencies to convert.
     * @return The result of the conversion.
     *
     */



    public String convert(String id, String convertId, String amount){

        try{
            double damount = Double.parseDouble(amount);

            long cap = 1000000000000L;
            if(damount < 0.00000001 || damount > cap){


                return "Out Range";
            }
        }catch(NumberFormatException e){
            return "Wrong Type";
        }




            return http.getConvert(id,convertId,amount);




    }



    /**
     * Get the amount of selected cryptos.
     * @return the amount of the select cryptos.
     *
     */

    public int getSelectListSize(){
        return this.selected_list.size();
    }


    /**
     * Get names of the selected list.
     * @return An arraylist contains the names
     *
     */
    public ArrayList<String> getSelectedNames(){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i<getSelectListSize(); i++){
            list.add(selected_list.get(i).getName());

        }

        return list;
    }



    /**
     * Make API calls to load all the available cryptos.
     */
    @Override
    public void getIdMap() {



        try{
            IdMap = http.getIdMap();

        }catch(NullPointerException e){
            throw new IllegalArgumentException();
        }


    }


    /**
     * Update the database with the new crypto data.
     * @param currency crypto to update.
     */


    public void updateDB(Currency currency){

        try{
            sql.updateCurrency(currency.getId(),currency.getName(),currency.getDescription(),currency.getSymbol(),currency.getLogo(),currency.getDate(),currency.getWebsite());

        }catch (NullPointerException e){
            throw new IllegalArgumentException();
        }


    }

    /**
     * Add the crypto data into the database.
     * @param currency crypto to add.
     */

    @Override
    public void addDB(Currency currency) {

        try{

            sql.addCurrency(currency.getId(),currency.getName(),currency.getDescription(),currency.getSymbol(),currency.getLogo(),currency.getDate(),currency.getWebsite());

        }catch (NullPointerException e){
            throw new IllegalArgumentException();
        }


    }

    /**
     * Load the crypto data from the database.
     * @param currency crypto to load.
     */
    @Override
    public Currency loadDB(Currency currency) {

        try{

            return sql.loadCurrency(currency.getId());

        }catch (NullPointerException e){
            throw new IllegalArgumentException();
        }
    }

    /**
     * Check if crypto data exists in the database.
     * @param id the id to check
     * @return If it exists.
     */
    @Override
    public boolean checkCurrencyExist(String id) {
        try{

            return sql.checkCurrency(id);

        }catch (NullPointerException e){
            throw new IllegalArgumentException();
        }


    }


    /**
     * Get the ID of the crypto with its name
     * @return the amount of the select cryptos.
     */
    public String getIdByName(String name){


        for (int i = 0; i<getSelectListSize();i++){
            if(name.equals(selected_list.get(i).getName())){
                return selected_list.get(i).getId();
            }


        }

        return null;


    }


    /**
     * inject http module.
     * @param http http object to inject.
     */

    public void injectHttp(Http http){
        this.http = http;
    }


    /**
     * inject sql module.
     * @param sql the sql module to inject.
     */
    public void injectSQL(Sql sql){
        this.sql = sql;
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
            System.out.println(dthreshold);
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
     * @return return false if rate is below threshold
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
