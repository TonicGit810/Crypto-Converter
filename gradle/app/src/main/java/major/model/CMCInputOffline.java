package major.model;

import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

public class CMCInputOffline implements CMCAPI {


    private ArrayList<Currency> currencies;
    private ArrayList<Currency> selected_list;

    private ArrayList<Message> messages;







    public CMCInputOffline(){
        this.currencies = new ArrayList<>();
        this.selected_list = new ArrayList<>();
        this.messages = new ArrayList<>();

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");
        currencies.add(bit1);
        currencies.add(bit2);


    }
    @Override
    public ArrayList<Currency> getCurrencies() {
        return currencies;
    }

    @Override
    public void add(Currency c) {
        selected_list.add(c);

    }

    @Override
    public void remove(Currency c) {
        this.selected_list.remove(c);

    }

    @Override
    public void clear() {
        this.selected_list.clear();

    }





    @Override
    public String convert(String id, String convert, String amount) {
        return "1";
    }



    @Override
    public int getSelectListSize() {
        return selected_list.size();
    }

    @Override
    public String getIdByName(String name) {
        for (int i = 0; i<getSelectListSize();i++){
            if(name.equals(selected_list.get(i).getName())){
                return selected_list.get(i).getId();
            }


        }

        return null;
    }

    @Override
    public ArrayList<Currency> getSelected_list() {
        return selected_list;
    }



    @Override
    public JsonObject loadCrypto(String id) {
        return null;
    }


    @Override
    public ArrayList<String> getSelectedNames() {
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i<getSelectListSize(); i++){
            list.add(selected_list.get(i).getName());

        }

        return list;
    }

    @Override
    public void getIdMap() {

    }

    @Override
    public boolean checkCurrencyExist(String id) {
        return false;
    }

    @Override
    public void updateDB(Currency currency) {

    }

    @Override
    public void addDB(Currency currency) {

    }

    @Override
    public Currency loadDB(Currency currency) {
        return null;
    }

    @Override
    public void clearDB() {

    }

    @Override
    public void injectHttp(HTTP http) {

    }

    @Override
    public void injectSQL(SQL sql) {

    }
}
