package major.model;


import com.google.gson.JsonObject;

import java.util.ArrayList;

public class Currency {
    private String id;
    private String name;
    private String description = null;
    private String symbol;
    private String date = null;
    private String logo = null;
    private String website = null;
    private CMCAPI input;





    public Currency(CMCAPI input, String id, String name, String symbol){
        this.input = input;
        this.id = id;
        this.name = name;
        this.symbol = symbol;

    }

    public Currency(String id, String name, String symbol, String description, String date,String website,String logo){
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.description = description;
        this.date = date;
        this.website = website;
        this.logo = logo;
    }



    public String getId() {
        return id;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription() {
        if(description == null){
            load();
        }
        return description;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getDate() {
        if(date == null){
            load();
        }

        return date;
    }

    public String getLogo() {
        if(logo == null){
            load();
        }
        return logo;
    }

    public String getWebsite() {
        if(website== null){
            load();
        }
        return website;
    }

    private void load(){
        System.out.println("YESSSS");
        JsonObject js = input.loadCrypto(id);




        this.description = String.valueOf(js.get("description")).replaceAll("\"", "");
        System.out.println(description);
        this.symbol = String.valueOf(js.get("symbol")).replaceAll("\"", "");
        this.date = String.valueOf(js.get("date")).replaceAll("\"", "");
        this.website = String.valueOf(js.get("url")).replaceAll("\"", "");
        this.logo = String.valueOf(js.get("logo")).replaceAll("\"", "");

    }





}
