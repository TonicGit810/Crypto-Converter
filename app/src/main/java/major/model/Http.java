package major.model;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.*;
import java.net.http.HttpClient;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



/**
 *Http class makes all the http requests.
 */
public class Http {
    /**
     * Constructor of currency object the API is for the lazy load design pattern.
     */

    public Http(){


    }





    /**
     * Get arraylist of a linked tree map if string and object that contains the information of the available cryptos.
     * @return arraylist of a linked tree map if string and object that contains the information of the available cryptos. Null if any error happens during or after getting the data.
     */



    public ArrayList<LinkedTreeMap<String,Object>> getIdMap(){
        ArrayList<LinkedTreeMap<String,Object>> IdMap = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder(new URI("https://pro-api.coinmarketcap.com/v1/cryptocurrency/map?&CMC_PRO_API_KEY=2b9e04e5-3315-4597-892e-e21d31083e32"))
                    .GET()
                    .build();


            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject)parser.parse(response.body());
            JsonArray data =  json.getAsJsonArray("data");

            Type listType = new TypeToken<ArrayList<LinkedTreeMap<String,Object>>>(){}.getType();

            ArrayList<LinkedTreeMap<String,Object>> listdata = new Gson().fromJson(data,listType);
            for (int i = 0; i<listdata.size(); i++){
                if(listdata.get(i).get("is_active").equals(1.0)){
                    IdMap.add(listdata.get(i));
                }
            }


        } catch (Exception e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
            return null;



        }

        return IdMap;


    }


    /**
     * Get full information about certain crypto based on the id of that crypto
     * @param id the id of the crypto
     * @return JsonObject contains the data. Null if any error happens during or after getting the data.
     */
    public JsonObject getCrypto(String id){

        JsonObject ret = new JsonObject();


        try {

            JsonParser parser = new JsonParser();



            String link = String.format("https://pro-api.coinmarketcap.com/v1/cryptocurrency/info?id=%s&aux=urls,logo,description,date_added&CMC_PRO_API_KEY=2b9e04e5-3315-4597-892e-e21d31083e32",id);
            HttpRequest req = HttpRequest.newBuilder(new URI(link))
                    .GET()
                    .build();


            HttpClient client1 = HttpClient.newBuilder().build();
            HttpResponse<String> response1 = client1.send(req, HttpResponse.BodyHandlers.ofString());


            JsonObject js = (JsonObject)parser.parse(response1.body());

            JsonObject dt =  js.getAsJsonObject("data");

            JsonObject dd = dt.getAsJsonObject(id);

            String cid = String.valueOf(dd.get("id")).replaceAll("\"", "");
            String name = String.valueOf(dd.get("name")).replaceAll("\"", "");
            String description = String.valueOf(dd.get("description")).replaceAll("\"", "");
            String symbol = String.valueOf(dd.get("symbol")).replaceAll("\"", "");
            String logo = String.valueOf(dd.get("logo")).replaceAll("\"", "");
            String date= String.valueOf(dd.get("date_launched")).replaceAll("\"", "");
            JsonObject urls = (JsonObject)parser.parse(String.valueOf(dd.get("urls")));
            JsonArray url = urls.getAsJsonArray("website");

            Type listType = new TypeToken<ArrayList<String>>(){}.getType();
            ArrayList<String> ul = new Gson().fromJson(url,listType);


            ret.addProperty("id",cid);
            ret.addProperty("name",name);
            ret.addProperty("description",description);
            ret.addProperty("symbol",symbol);
            ret.addProperty("logo",logo);
            ret.addProperty("date",date);
            ret.addProperty("url",ul.get(0));





        } catch (Exception e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
            return null;
        }

        return ret;

    }













    /**
     * Get the convert result of the 2 given cryptos
     * @param id the id of the crypto to convert from
     * @param convertId the id of the crypto to convert to.
     * @param  amount the amount of the crypto to convert.
     * @return The result of the conversion as a string
     */

    public  String getConvert(String id, String convertId, String amount){
        String price = "";

        try {
            String url = String.format("https://pro-api.coinmarketcap.com/v1/tools/price-conversion?amount=%s&id=%s&convert_id=%s&CMC_PRO_API_KEY=2b9e04e5-3315-4597-892e-e21d31083e32",amount,id,convertId);
            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .GET()
                    .build();


            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject)parser.parse(response.body());
            JsonObject data = (JsonObject)parser.parse(json.get("data").toString());
            JsonObject quote = (JsonObject)parser.parse(data.get("quote").toString());
            JsonObject actual = (JsonObject)parser.parse(quote.get(convertId).toString());
            price = actual.get("price").toString();




        } catch (Exception e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
            return  null;
        }

        return price;


    }


    /**
     * Send message
     * @param message the message to send
     * @return  if the message is successfully sent.
     */


    public boolean sendMessage(Message message){
        String token = System.getenv("TWILIO_API_KEY");
        String SID = System.getenv("TWILIO_API_SID");



        try {


            Map<String,String> sms = new HashMap<>();

            sms.put("To", message.getTo());
            sms.put("From",message.getFrom());
            sms.put("Body", message.getBody());



            HttpRequest request = HttpRequest.newBuilder(new URI(String.format("https://api.twilio.com/2010-04-01/Accounts/%s/Messages",SID)))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(buildFormDataFromMap(sms))
                    .build();
            HttpClient client = HttpClient.newBuilder().authenticator((new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SID, token.toCharArray());

                }
            })).build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return true;

        } catch (Exception e) {
            return false;
        }

    }

    /**
     * Code get from https://mkyong.com/java/how-to-send-http-request-getpost-in-java. To encrypt the message to send.
     * @param data data of the message: from number, to number, body of the message
     * @return  HttpRequest.BodyPublisher that contians the encrypted message.
     */

    private  HttpRequest.BodyPublisher buildFormDataFromMap(Map<String, String> data) {
        var builder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }





}
