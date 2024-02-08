package major.model;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;


import java.util.ArrayList;



public class CMCInputOnline implements CMCAPI {

    private ArrayList<Currency> currencies;
    private ArrayList<Currency> selected_list;




    ArrayList<LinkedTreeMap<String,Object>> IdMap;

    private HTTP http;

    private SQL sql;



    public CMCInputOnline(){
        this.currencies = new ArrayList<>();
        this.selected_list = new ArrayList<>();




    }

//    private void initialise(){
//        JsonParser parser = new JsonParser();
//        for(int i = 0; i<data.size();i++){
//            for(Map.Entry<String, JsonElement> entry : data.get(i).entrySet()) {
//                JsonObject j = entry.getValue().getAsJsonObject();
//                String id = String.valueOf(j.get("id")).replaceAll("\"", "");
//                String name = String.valueOf(j.get("name")).replaceAll("\"", "");
//                String description = String.valueOf(j.get("description")).replaceAll("\"", "");
//                String symbol = String.valueOf(j.get("symbol")).replaceAll("\"", "");
//                String logo = String.valueOf(j.get("logo")).replaceAll("\"", "");
//                String date= String.valueOf(j.get("date_launched")).replaceAll("\"", "");
//                JsonObject urls = (JsonObject)parser.parse(String.valueOf(j.get("urls")));
//                JsonArray url = urls.getAsJsonArray("website");
//                ArrayList<String> ul = new Gson().fromJson(url,ArrayList.class);
//
//
//
//
//                Currency cur = new Currency(id,name,description,symbol,logo,date,ul);
//                currencies.add(cur);
//
//
//
//            }
//
//        }

//    }


    public ArrayList<Currency> getCurrencies(){

        currencies.clear();
        getIdMap();
        for (int i = 0; i<IdMap.size(); i++){

            double d = (double) IdMap.get(i).get("id");
            int id = (int)d;
            String name = (String) IdMap.get(i).get("name");
            String symbol = (String) IdMap.get(i).get("symbol");



            currencies.add(new Currency(this,Integer.toString(id),name,symbol));

        }

        return this.currencies;
    }


    public ArrayList<Currency> getSelected_list(){return this.selected_list;}



    @Override
    public JsonObject loadCrypto(String id) {
        return http.getCrypto(id);
    }




    public void add(Currency c){

        this.selected_list.add(c);

    }

    public void remove(Currency c){
        this.selected_list.remove(c);
    }

    public void clear(){
        this.selected_list.clear();
    }

    public void clearDB(){
        sql.clearCache();
    }




    public String convert(String id, String convert, String amount){
        return http.getConvert(id,convert,amount);

    }


    public int getSelectListSize(){
        return this.selected_list.size();
    }

    public ArrayList<String> getSelectedNames(){
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0; i<getSelectListSize(); i++){
            list.add(selected_list.get(i).getName());

        }

        return list;
    }

    @Override
    public void getIdMap() {

        IdMap = http.getIdMap();


    }

    public void updateDB(Currency currency){
        sql.updateCurrency(currency.getId(),currency.getName(),currency.getDescription(),currency.getSymbol(),currency.getLogo(),currency.getDate(),currency.getWebsite());

    }

    @Override
    public void addDB(Currency currency) {
        sql.addCurrency(currency.getId(),currency.getName(),currency.getDescription(),currency.getSymbol(),currency.getLogo(),currency.getDate(),currency.getWebsite());

    }

    @Override
    public Currency loadDB(Currency currency) {

        return sql.loadCurrency(currency.getId());
    }

    @Override
    public boolean checkCurrencyExist(String id) {
        return sql.checkCurrency(id);
    }


    public String getIdByName(String name){


        for (int i = 0; i<getSelectListSize();i++){
            if(name.equals(selected_list.get(i).getName())){
                return selected_list.get(i).getId();
            }


        }

        return null;


    }

    public void injectHttp(HTTP http){
        this.http = http;
    }

    public void injectSQL(SQL sql){
        this.sql = sql;
    }






}
