package major.model;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HTTP {
    private static String apikey = "2b9e04e5-3315-4597-892e-e21d31083e32";
    private static ArrayList<String> ids;




    public HTTP(){
        ids = new ArrayList<>();


    }






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

            ArrayList<LinkedTreeMap<String,Object>> listdata = new Gson().fromJson(data,ArrayList.class);
            for (int i = 0; i<listdata.size(); i++){
                if(listdata.get(i).get("is_active").equals(1.0)){
                    IdMap.add(listdata.get(i));
                }
            }


        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {

        }

        return IdMap;


    }

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
            ArrayList<String> ul = new Gson().fromJson(url,ArrayList.class);


            ret.addProperty("id",cid);
            ret.addProperty("name",name);
            ret.addProperty("description",description);
            ret.addProperty("symbol",symbol);
            ret.addProperty("logo",logo);
            ret.addProperty("date",date);
            ret.addProperty("url",ul.get(0));












        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {
            // This would mean our URI is incorrect - this is here because often the URI you use will not be (fully)
            // hard-coded and so needs a way to be checked for correctness at runtime.
        }

        return ret;

    }













    public  String getConvert(String id, String convert, String amount){
        String price = "";

        try {
            String url = String.format("https://pro-api.coinmarketcap.com/v1/tools/price-conversion?amount=%s&id=%s&convert_id=%s&CMC_PRO_API_KEY=2b9e04e5-3315-4597-892e-e21d31083e32",amount,id,convert);
            HttpRequest request = HttpRequest.newBuilder(new URI(url))
                    .GET()
                    .build();


            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonParser parser = new JsonParser();
            JsonObject json = (JsonObject)parser.parse(response.body());
            JsonObject data = (JsonObject)parser.parse(json.get("data").toString());
            JsonObject quote = (JsonObject)parser.parse(data.get("quote").toString());
            JsonObject actual = (JsonObject)parser.parse(quote.get(convert).toString());
            price = actual.get("price").toString();




        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {

        }

        return price;


    }


    public void sendMessage(Message message){
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

        } catch (IOException | InterruptedException e) {
            System.out.println("Something went wrong with our request!");
            System.out.println(e.getMessage());
        } catch (URISyntaxException ignored) {

        }

    }

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
