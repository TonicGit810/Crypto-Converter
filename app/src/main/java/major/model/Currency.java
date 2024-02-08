package major.model;


import com.google.gson.JsonObject;

/**
 *Currency class. Lazy Load pattern used.
 */

public class Currency {
    private String id;
    private String name;
    private String description = null;
    private String symbol;
    private String date = null;
    private String logo = null;
    private String website = null;
    private CMCAPI input;

    private boolean updated = false;




    /**
     * Constructor of currency object the API is for the lazy load design pattern.
     * @param input input API.
     * @param id id of the crypto.
     * @param name name of the crypto.
     * @param symbol symbol of the crypto.
     */

    public Currency(CMCAPI input, String id, String name, String symbol){
        this.input = input;
        this.id = id;
        this.name = name;
        this.symbol = symbol;

    }

    /**
     * Constructor of currency object to load the currency from database.
     * @param id id of the crypto.
     * @param name name of the crypto.
     * @param symbol symbol of the crypto.
     * @param description description of the crypto.
     * @param date launch of the crypto.
     * @param website website of the crypto.
     * @param logo logo of the crypto.
     */

    public Currency(String id, String name, String symbol, String description, String date,String website,String logo){
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.description = description;
        this.date = date;
        this.website = website;
        this.logo = logo;
    }


    /**
     * Get id
     */

    public String getId() {
        return id;
    }

    /**
     * Get name
     */

    public String getName(){
        return this.name;
    }


    /**
     * Get description
     */
    public String getDescription() {
        if(!updated){
            if(description == null){
                load();
            }
        }

        return description;
    }

    /**
     * Get symbol
     */

    public String getSymbol() {
        return symbol;
    }


    /**
     * Get launch date, mostly null.
     */
    public String getDate() {
        if(!updated){
            if(date == null){
                load();
            }
        }

        return date;
    }


    /**
     * Get logo
     */

    public String getLogo() {
        if(!updated){
            if(logo == null){
                load();
            }
        }
        return logo;
    }


    /**
     * Get website
     */


    public String getWebsite() {
        if(!updated){
            if(website == null){
                load();
            }
        }
        return website;
    }

    /**
     * Lazy load. If certain field of the currency is null, this method will call api to get the full data
     */


    private void load(){

        JsonObject js = input.loadCrypto(id);
        if(js==null){
            this.description = "X";
            this.date = "X";
            this.website ="X";
            this.logo = "error.png";
        }else{

            this.description = String.valueOf(js.get("description")).replaceAll("\"", "");
            this.date = String.valueOf(js.get("date")).replaceAll("\"", "");
            this.website = String.valueOf(js.get("url")).replaceAll("\"", "");
            this.logo = String.valueOf(js.get("logo")).replaceAll("\"", "");

        }



        updated = true;

    }







}
