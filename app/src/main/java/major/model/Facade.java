package major.model;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.BufferedReader;

/**
 * Facade for the input and output API
 */

public class Facade {

    private CMCAPI input;
    private TwilioAPI output;
    private String language;
    private HashMap<String,String> translation = new HashMap<String,String>();


    public Facade(CMCAPI input, TwilioAPI output){
        this.input = input;
        this.output = output;

    }

    /**
     * Call CMCAPI to get the available crypto
     *@return Arraylist contains all the currency object with only id,name,symbol
     */


    public ArrayList<Currency> getCurrencies(){
        return input.getCurrencies();
    };



    /**
     * add selected crypto in to the selected list in CMCAPI.
     * @param c selected currency
     */

    public void add(Currency c){
        input.add(c);
    };


    /**
     * remove selected crypto from the selected list in CMCAPI.
     * @param c selected currency
     */

    public void remove(Currency c){
        input.remove(c);
    };

    /**
     * remove all selected cryptos in to the selected list in CMCAPI.
     *
     */
    public void clear(){
        input.clear();
    };

    /**
     * Return the last message in the TwilioAPI to send.
     * @return Message object to send.
     */

    public Message getLastMessages(){
        return output.getLastMessages();
    };



    /**
     * Add message to twilio API.
     * @param id ID to convert from.
     * @param convert ID to convert to.
     * @param amount The amount of crypto converted.
     * @param result Thre result of the conversion.
     */

    public void addMessages(String id, String convert, String amount, String result){
        output.addMessages(id,convert,amount,result, input.getSelectedList(),translation);
    };


    /**
     * Get the convert result of the two given currencies from API.
     * @param id selected currency id to convert from.
     * @param convertId selected currency id to convert to.
     * @param amount the amount of currencies to convert.
     * @return The result of the conversion.
     *
     */

    public String convert(String id, String convertId, String amount){
        return input.convert(id,convertId,amount);
    }


    /**
     * Send message.
     * @param message
     * @return if the message is sent or not.
     */
    public boolean message(Message message){

        return output.sendMessage(message);
    }

    /**
     * Get the ID of the crypto with its name
     * @return the amount of the select cryptos.
     */

    public String getIdByName(String name){
        return input.getIdByName(name);
    }

    /**
     * Get the list of the selected crypto
     * @return list of the selected crypto
     */


    public ArrayList<Currency> getSelectedList(){
        return input.getSelectedList();
    }


    /**
     * Get the amount of selected cryptos.
     * @return the amount of the select cryptos.
     *
     */

    public int getSelectedListSize(){return input.getSelectListSize();}



    /**
     * Get names of the selected list.
     * @return An arraylist contains the names
     *
     */

    public ArrayList<String> getSelectedNames(){
        return input.getSelectedNames();
    }

    /**
     * Check if crypto data exists in the database.
     * @param currency the currency to check
     * @return If it exists.
     */

    public boolean checkCurrencyExist(Currency currency){
        String id = currency.getId();
        return input.checkCurrencyExist(id);
    }


    /**
     * Clear the database
     */

    public void clearCache(){
        input.clearDB();
    }


    /**
     * Update the database with the new crypto data.
     * @param currency crypto to update.
     */

    public void updateDB(Currency currency){
        input.updateDB(currency);
    }


    /**
     * Add the crypto data into the database.
     * @param currency crypto to add.
     */

    public void addDB(Currency currency){
        input.addDB(currency);
    }


    /**
     * Load the crypto data from the database.
     * @param currency crypto to load.
     */
    public Currency loadDB(Currency currency){

        return input.loadDB(currency);
    }


    /**
     * Check if the message can be sent.
     * @retun if the message can be sent
     */

    public boolean getSend(){
        return output.getSend();
    }


    /**
     * Set the send boolean value of Twilio API.
     * @param  send set the boolean value of send
     */

    public void setSend(boolean send){
        output.setSend(send);
    }


    /**
     * Set system language.
     * @param  language the language of the system
     */
    public void setLanguage(String language){
        this.language = language;
        CSVRead();
    }

    /**
     * read the csv file to load the translated language in to the hashmap
     */


    private void CSVRead(){
        try{

            BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/"+language+".csv"));
            String translate = "";
            while((translate = reader.readLine())!=null){

                String[] split = translate.split(",");



//                System.out.println(split[1]);

                translation.put(split[0],split[1]);

            }
        }catch (Exception e){
            System.out.println(e);

        }


    }
    /**
     * read the csv file to load the translated language in to the hashmap
     * @param key the key to retrieve the translated language
     * @return the translated language
     */



    public String getTranslation(String key){
        return translation.get(key);
    }

    public boolean setThreshold(String threshold){
       return input.setThreshold(threshold);
    }


    public boolean checkThreshold(double rate){
        return input.checkThreshold(rate);
    }




}
