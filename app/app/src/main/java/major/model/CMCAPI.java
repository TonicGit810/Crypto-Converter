package major.model;

import com.google.gson.JsonObject;

import java.util.ArrayList;

public interface CMCAPI {


    private void initialise(){

    };
    ArrayList<Currency> getCurrencies();
    void add(Currency c);
    void remove(Currency c);
    void clear();
//    Message getLastMessages();
//    void addMessages(String id, String convert, String amount, String result);
    String convert(String id, String convert, String amount);
//    void message(Message message);
    int getSelectListSize();
    String getIdByName(String name);


    ArrayList<Currency> getSelected_list();



    JsonObject loadCrypto(String id);



    ArrayList<String> getSelectedNames();

    void getIdMap();


    boolean checkCurrencyExist(String id);

    void updateDB(Currency currency);

    void addDB(Currency currency);

    Currency loadDB(Currency currency);

    void clearDB();

    void injectHttp(HTTP http);

    void injectSQL(SQL sql);
}
