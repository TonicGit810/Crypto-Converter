
package major;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import major.model.*;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModelTest {




    /**
     * Test getters.
     */

    @Test
    public void messageGetter(){
        Message message = new Message("111111","111111","body");
        assertEquals(message.getFrom(),"111111");
        assertEquals(message.getTo(),"111111");
        assertEquals(message.getBody(),"body");
    }



    /**
     * Test getters.
     */

    @Test
    public void currencyGetter(){
        Currency currency = new Currency("1","name","symbol","description","date","website","logo");
        assertEquals(currency.getId(),"1");
        assertEquals(currency.getDate(),"date");
        assertEquals(currency.getName(),"name");
        assertEquals(currency.getSymbol(),"symbol");
        assertEquals(currency.getDescription(),"description");
        assertEquals(currency.getWebsite(),"website");
        assertEquals(currency.getLogo(),"logo");

    }


    /**
     * Test lazy load.
     */

    @Test
    public void currencyLazyLoad(){
        JsonObject ret = new JsonObject();
        ret.addProperty("id","1");
        ret.addProperty("name","name");
        ret.addProperty("description","description");
        ret.addProperty("symbol","symbol");
        ret.addProperty("logo","logo");
        ret.addProperty("date","date");
        ret.addProperty("url","website");


        Http http = mock(Http.class);


        Mockito.when(http.getCrypto(anyString())).thenReturn(ret);
        CMCInputOnline input = new CMCInputOnline();


        input.injectHttp(http);



        Currency currency = new Currency(input,"1","name","symbol");
        assertEquals(currency.getId(),"1");
        assertEquals(currency.getDate(),"date");
        assertEquals(currency.getName(),"name");
        assertEquals(currency.getSymbol(),"symbol");
        assertEquals(currency.getDescription(),"description");
        assertEquals(currency.getWebsite(),"website");
        assertEquals(currency.getLogo(),"logo");

        verify(http,times(1)).getCrypto(anyString());

    }

    /**
     * Test lazy load.
     */
    @Test
    public void currencyLazyLoad2(){
        JsonObject ret = new JsonObject();
        ret.addProperty("id","1");
        ret.addProperty("name","name");
        ret.addProperty("description","description");
        ret.addProperty("symbol","symbol");
        ret.addProperty("logo","logo");
//        ret.addProperty("date","null");
        ret.addProperty("url","website");


        Http http = mock(Http.class);


        Mockito.when(http.getCrypto(anyString())).thenReturn(ret);
        CMCInputOnline input = new CMCInputOnline();


        input.injectHttp(http);



        Currency currency = new Currency(input,"1","name","symbol");
        assertEquals(currency.getId(),"1");
        assertEquals(currency.getDate(),"null");
        assertEquals(currency.getName(),"name");
        assertEquals(currency.getSymbol(),"symbol");
        assertEquals(currency.getDescription(),"description");
        assertEquals(currency.getWebsite(),"website");
        assertEquals(currency.getLogo(),"logo");

        verify(http,times(1)).getCrypto(anyString());

    }

    /**
     * Test lazy load when http calls have an error.
     */

    @Test
    public void currencyLazyLoadError(){
        JsonObject ret = new JsonObject();
        ret.addProperty("id","1");
        ret.addProperty("name","name");
        ret.addProperty("description","description");
        ret.addProperty("symbol","symbol");
        ret.addProperty("logo","logo");
        ret.addProperty("date","date");
        ret.addProperty("url","website");


        Http http = mock(Http.class);


        Mockito.when(http.getCrypto(anyString())).thenReturn(null);
        CMCInputOnline input = new CMCInputOnline();


        input.injectHttp(http);



        Currency currency = new Currency(input,"1","name","symbol");

        assertEquals(currency.getId(),"1");
        assertEquals(currency.getDate(),"X");
        assertEquals(currency.getName(),"name");
        assertEquals(currency.getSymbol(),"symbol");
        assertEquals(currency.getDescription(),"X");
        assertEquals(currency.getWebsite(),"X");
        assertEquals(currency.getLogo(),"error.png");

        verify(http,times(1)).getCrypto(anyString());

    }


    /**
     * Test get currencies for online mode CMCAPI.
     */


    @Test
    public void CMCOnlineTestGetCurrencies(){

        Http http = mock(Http.class);

        String js = "{\"status\":{\"timestamp\":\"2022-05-30T13:58:36.782Z\",\"error_code\":0,\"error_message\":null,\"elapsed\":1042,\"credit_count\":1,\"notice\":null},\"data\":[{\"id\":1,\"name\":\"Bitcoin\",\"symbol\":\"BTC\",\"slug\":\"bitcoin\",\"rank\":1,\"is_active\":1,\"first_historical_data\":\"2013-04-28T18:47:21.000Z\",\"last_historical_data\":\"2022-05-30T13:49:00.000Z\",\"platform\":null}]}";
        JsonObject jo = new JsonParser().parse(js).getAsJsonObject();
        JsonArray data =  jo.getAsJsonArray("data");
        ArrayList<LinkedTreeMap<String,Object>> listdata = new Gson().fromJson(data,ArrayList.class);

        Mockito.when(http.getIdMap()).thenReturn(listdata);

        CMCInputOnline input = new CMCInputOnline();
        input.injectHttp(http);

        ArrayList<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(input,"1","Bitcoin","BTC"));

        assertEquals(currencies.get(0).getId(),input.getCurrencies().get(0).getId());
        assertEquals(currencies.get(0).getName(),input.getCurrencies().get(0).getName());
        assertEquals(currencies.get(0).getSymbol(),input.getCurrencies().get(0).getSymbol());


        verify(http,times(3)).getIdMap();




    }


    /**
     * Test conversion of online mode CMCAPI.
     */


    @Test
    public void CMCOnlineTestGetConvert(){

        Http http = mock(Http.class);

        Mockito.when(http.getConvert(anyString(),anyString(),anyString())).thenReturn("1");

        CMCInputOnline input = new CMCInputOnline();
        input.injectHttp(http);

        assertEquals(input.convert("1","1","1"),"1");
        verify(http,times(1)).getConvert("1","1","1");


    }


    /**
     * Test convert with wrong input type.
     */


    @Test
    public void CMCOnlineTestGetConvertInputType(){


        CMCInputOnline input = new CMCInputOnline();


        assertEquals(input.convert("1","1","one"),"Wrong Type");


    }

    /**
     * Test convert with input out of range.
     */

    @Test
    public void CMCOnlineTestGetConvertBadInputRange(){


        CMCInputOnline input = new CMCInputOnline();


        assertEquals(input.convert("1","1","0"),"Out Range");


    }


    /**
     * Test convert with input out of range.
     */

    @Test
    public void CMCOnlineTestGetConvertBadInputRange2(){


        CMCInputOnline input = new CMCInputOnline();


        assertEquals(input.convert("1","1","0.000000001"),"Out Range");


    }

    /**
     * Test convert with input out of range.
     */

    @Test
    public void CMCOnlineTestGetConvertBadInputRange3(){


        CMCInputOnline input = new CMCInputOnline();


        assertEquals(input.convert("1","1","1000000000001"),"Out Range");


    }
    /**
     * Test convert with http request error
     */


    @Test
    public void CMCOnlineTestGetConvertHttpNull(){

        Http http = mock(Http.class);

        Mockito.when(http.getConvert(anyString(),anyString(),anyString())).thenReturn(null);

        CMCInputOnline input = new CMCInputOnline();
        input.injectHttp(http);

        assertNull(input.convert("1","1","1"));
        verify(http,times(1)).getConvert("1","1","1");


    }

    /**
     * Test add crypto to CMCAPI
     */

    @Test
    public void CMCOnlineTestAdd(){
        CMCInputOnline input = new CMCInputOnline();
        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        input.add(bit1);
        input.add(bit2);
        assertEquals(input.getSelectListSize(),2);
        assertEquals(input.getSelectedList().get(0),bit1);
        assertEquals(input.getSelectedList().get(1),bit2);

    }

    /**
     * Test remove crypto from CMCAPI
     */

    @Test
    public void CMCOnlineTestRemove(){
        CMCInputOnline input = new CMCInputOnline();
        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        input.add(bit1);
        input.add(bit2);



        input.remove(bit1);
        assertEquals(input.getSelectListSize(),1);
        assertEquals(input.getSelectedList().get(0),bit2);

    }

    /**
     * Test remove all crypto from CMCAPI
     */
    @Test
    public void CMCOnlineTestClear(){
        CMCInputOnline input = new CMCInputOnline();
        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        input.add(bit1);
        input.add(bit2);



        input.clear();
        assertEquals(input.getSelectedList().size(),0);


    }

    /**
     * Test get id by name con CMCAPI
     */

    @Test
    public void CMCOnlineTestGetIdByName(){
        CMCInputOnline input = new CMCInputOnline();
        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        input.add(bit1);
        input.add(bit2);



        assertEquals(input.getIdByName("Bitcoin"),"1");


    }


    /**
     * Test the database for CMCOnline
     */


    @Test
    public void CMCOnlineDBTest(){
        Sql sql = mock(Sql.class);

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Mockito.when(sql.loadCurrency(anyString())).thenReturn(bit1);

        CMCInputOnline input = new CMCInputOnline();
        input.injectSQL(sql);

        assertEquals(input.loadDB(bit1),bit1);
        verify(sql,times(1)).loadCurrency("1");
    }

    /**
     * Test the database void method for CMCOnline
     */

    @Test
    public void CMCOnlineDBTestVoid(){
        Sql sql = mock(Sql.class);

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");


        CMCInputOnline input = new CMCInputOnline();
        input.injectSQL(sql);

        input.addDB(bit1);
        input.updateDB(bit1);
        input.clearDB();

        verify(sql,times(1)).addCurrency("1","Bitcoin","It is a crypto","BTC","splash.jpg","week1","www");
        verify(sql,times(1)).updateCurrency("1","Bitcoin","It is a crypto","BTC","splash.jpg","week1","www");
        verify(sql,times(1)).clearCache();
    }


    /**
     * Test the database check crypto method for CMCOnline
     */

    @Test
    public void CMCOnlineDBTestCheck(){
        Sql sql = mock(Sql.class);

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");


        Mockito.when(sql.checkCurrency(anyString())).thenReturn(Boolean.TRUE);


        CMCInputOnline input = new CMCInputOnline();
        input.injectSQL(sql);

        input.addDB(bit1);
        assertTrue(input.checkCurrencyExist("1"));
        verify(sql,times(1)).checkCurrency("1");
    }

    /**
     * CMCOffline tests all in one
     */

    @Test
    public void CMCOfflineTest(){
        CMCInputOffline input = new CMCInputOffline();


        assertEquals(input.getCurrencies().size(),2);

        assertEquals(input.convert("1","1","1"),"0.5");


        assertNull(input.loadCrypto("1"));
        assertFalse(input.checkCurrencyExist("1"));
        assertNull(input.getIdByName("Bitcoin"));


        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        assertNull(input.loadDB(bit1));

        input.add(bit1);
        input.add(bit2);



        assertEquals(input.getSelectListSize(),2);
        assertEquals(input.getSelectedList().get(0),bit1);
        assertEquals(input.getSelectedList().get(1),bit2);


        input.remove(bit1);
        assertEquals(input.getSelectListSize(),1);
        assertEquals(input.getSelectedList().get(0),bit2);


        input.clear();
        assertEquals(input.getSelectedList().size(),0);

    }

    /**
     *Test when there is no message to send.
     */

    @Test
    public void TwilioOnlineTestNoMessage(){
        TwilioOnline output = new TwilioOnline();
        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");
        Message message = new Message(from,to, "Something went wrong when converting");
        assertEquals(output.getLastMessages().getBody(),message.getBody());
        assertEquals(output.getLastMessages().getFrom(),message.getFrom());
        assertEquals(output.getLastMessages().getTo(),message.getTo());
    }


    /**
     * Test TwilioOnline sending message
     */

    @Test
    public void TwilioOnlineTestSendMessage(){

        Http http = mock(Http.class);

        TwilioOnline output = new TwilioOnline();

        output.injectHttp(http);
        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");
        Message message = new Message(from,to, "Something went wrong when converting");


        http.sendMessage(message);

        verify(http,times(1)).sendMessage(message);

    }



    /**
     * Test Twilio add message
     */
    @Test
    public void TwilioOnlineTestAddMessage(){
        HashMap<String,String> translation = new HashMap<String,String>();



        try{

            BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/english.csv"));
            String translate = "";
            while((translate = reader.readLine())!=null){

                String[] split = translate.split(",");



                System.out.println(split[1]);

                translation.put(split[0],split[1]);

            }
        }catch (Exception e){
            System.out.println(e);
        }



        TwilioOnline output = new TwilioOnline();

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        ArrayList<Currency> selected_list = new ArrayList<>();
        selected_list.add(bit1);
        selected_list.add(bit2);

        output.addMessages("1","2","1","1",selected_list,translation);



        StringBuilder sb = new StringBuilder();


        sb.append("Convert From:Bitcoin,Symbol: BTC\n");
        sb.append("Convert To:Bitcoins,Symbol: BTCS\n");
        sb.append("Amount:1.00\nConversion rate is:1.00\nConversion result is:1.00\n");

        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");
        Message message = new Message(from,to, sb.toString());

        assertEquals(output.getLastMessages().getBody(),message.getBody());
        assertEquals(output.getLastMessages().getFrom(),message.getFrom());
        assertEquals(output.getLastMessages().getTo(),message.getTo());




    }
    /**
     *TwilioOfflineTest all in one.
     */


    @Test
    public void TwilioOfflineTest(){

        HashMap<String,String> translation = new HashMap<String,String>();



        try{

            BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/english.csv"));
            String translate = "";
            while((translate = reader.readLine())!=null){

                String[] split = translate.split(",");



                System.out.println(split[1]);

                translation.put(split[0],split[1]);

            }
        }catch (Exception e){
            System.out.println(e);
        }
        TwilioOffline output = new TwilioOffline();


        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");
        Message message = new Message(from,to, "Something went wrong when converting");
        assertEquals(output.getLastMessages().getBody(),message.getBody());
        assertEquals(output.getLastMessages().getFrom(),message.getFrom());
        assertEquals(output.getLastMessages().getTo(),message.getTo());

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        ArrayList<Currency> selected_list = new ArrayList<>();
        selected_list.add(bit1);
        selected_list.add(bit2);

        output.addMessages("1","2","1","1",selected_list,translation);



        StringBuilder sb = new StringBuilder();


        sb.append("Convert From:Bitcoin,Symbol: BTC\n");
        sb.append("Convert To:Bitcoins,Symbol: BTCS\n");
        sb.append("Amount:1.00\nConversion rate is:1.00\nConversion result is:1.00\n");

        Message message2 = new Message(from,to, sb.toString());

        assertEquals(output.getLastMessages().getBody(),message2.getBody());
        assertEquals(output.getLastMessages().getFrom(),message2.getFrom());
        assertEquals(output.getLastMessages().getTo(),message2.getTo());


    }

    /**
     * Controller test online input.
     */

    @Test
    public void controllerTestGetCurrenciesOnline(){

        Http http = mock(Http.class);

        String js = "{\"status\":{\"timestamp\":\"2022-05-30T13:58:36.782Z\",\"error_code\":0,\"error_message\":null,\"elapsed\":1042,\"credit_count\":1,\"notice\":null},\"data\":[{\"id\":1,\"name\":\"Bitcoin\",\"symbol\":\"BTC\",\"slug\":\"bitcoin\",\"rank\":1,\"is_active\":1,\"first_historical_data\":\"2013-04-28T18:47:21.000Z\",\"last_historical_data\":\"2022-05-30T13:49:00.000Z\",\"platform\":null}]}";
        JsonObject jo = new JsonParser().parse(js).getAsJsonObject();
        JsonArray data =  jo.getAsJsonArray("data");
        ArrayList<LinkedTreeMap<String,Object>> listdata = new Gson().fromJson(data,ArrayList.class);

        Mockito.when(http.getIdMap()).thenReturn(listdata);

        CMCInputOnline input = new CMCInputOnline();
        input.injectHttp(http);

        ArrayList<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(input,"1","Bitcoin","BTC"));

        assertEquals(currencies.get(0).getId(),input.getCurrencies().get(0).getId());
        assertEquals(currencies.get(0).getName(),input.getCurrencies().get(0).getName());
        assertEquals(currencies.get(0).getSymbol(),input.getCurrencies().get(0).getSymbol());


        verify(http,times(3)).getIdMap();




        input.injectHttp(http);

        TwilioAPI output = new TwilioOnline();
        output.injectHttp(http);


        Facade facade = new Facade(input, output);


        assertEquals(currencies.get(0).getId(), facade.getCurrencies().get(0).getId());
        assertEquals(currencies.get(0).getName(), facade.getCurrencies().get(0).getName());
        assertEquals(currencies.get(0).getSymbol(), facade.getCurrencies().get(0).getSymbol());



    }


    /**
     * Controller add crypto test. Whether the APIs are offline or online make no difference when the method does not involve the http and database calls.
     */
    @Test
    public void controllerTestAddCurrencies(){





        CMCInputOnline input = new CMCInputOnline();


        ArrayList<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(input,"1","Bitcoin","BTC"));




        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");







        TwilioAPI output = new TwilioOnline();


        Facade facade = new Facade(input, output);

        facade.add(bit1);
        facade.add(bit2);
        assertEquals(facade.getSelectedListSize(),2);
        assertEquals(facade.getSelectedList().get(0),bit1);
        assertEquals(facade.getSelectedList().get(1),bit2);



    }

    /**
     * Controller clear cryptos test
     */

    @Test
    public void controllerTestClearCurrencies(){





        CMCInputOnline input = new CMCInputOnline();


        ArrayList<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(input,"1","Bitcoin","BTC"));




        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");







        TwilioAPI output = new TwilioOnline();


        Facade facade = new Facade(input, output);

        facade.add(bit1);
        facade.add(bit2);
        assertEquals(facade.getSelectedListSize(),2);
        facade.clear();
        assertEquals(facade.getSelectedListSize(),0);



    }

    /**
     * Controller remove certain cryptos test
     */

    @Test
    public void controllerTestRemoveCurrencies(){





        CMCInputOnline input = new CMCInputOnline();


        ArrayList<Currency> currencies = new ArrayList<>();
        currencies.add(new Currency(input,"1","Bitcoin","BTC"));




        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");







        TwilioAPI output = new TwilioOnline();


        Facade facade = new Facade(input, output);

        facade.add(bit1);
        facade.add(bit2);
        assertEquals(facade.getSelectedListSize(),2);
        facade.remove(bit1);
        assertEquals(facade.getSelectedListSize(),1);
        assertEquals(facade.getSelectedList().get(0),bit2);



    }

    /**
     * Controller get last message test.
     */

    @Test
    public void controllerTestGetLastMessage(){




        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");


        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");






        StringBuilder sb = new StringBuilder();
        sb.append("Convert From:Bitcoin,Symbol: BTC\n");
        sb.append("Convert To:Bitcoins,Symbol: BTCS\n");
        sb.append("Amount:1.00\nConversion rate is:1.00\nConversion result is:1.00\n");

        Message message = new Message(from,to, sb.toString());



        CMCInputOnline input = new CMCInputOnline();
        TwilioOnline output = new TwilioOnline();

        input.add(bit1);
        input.add(bit2);




        Facade facade = new Facade(input, output);
        facade.setLanguage("english");

        facade.addMessages("1","2","1","1");


        assertEquals(facade.getLastMessages().getBody(),message.getBody());
        assertEquals(facade.getLastMessages().getFrom(),message.getFrom());
        assertEquals(facade.getLastMessages().getTo(),message.getTo());



    }

    /**
     * Controller send message test.
     */
    @Test
    public void controllerTestMessage(){




        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");





        StringBuilder sb = new StringBuilder();
        sb.append("Convert From:Bitcoin,Symbol: BTC\n");
        sb.append("Convert To:Bitcoins,Symbol: BTCS\n");
        sb.append("Amount:1.00\nConversion rate is:1.00\nConversion result is:1.00\n");

        Message message = new Message(from,to, sb.toString());



        CMCInputOnline input = new CMCInputOnline();
        TwilioOffline output = new TwilioOffline();



        Facade facade = new Facade(input, output);
        facade.setLanguage("english");

        facade.addMessages("1","2","1","1");

        assertTrue(facade.message(message));





    }
    /**
     * Controller get selected name tests.
     */

    @Test
    public void controllerGetSelectedNames(){




        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");


        CMCInputOffline input = new CMCInputOffline();
        TwilioOffline output = new TwilioOffline();



        Facade facade = new Facade(input, output);

        facade.add(bit1);
        facade.add(bit2);
        ArrayList<String> names = new ArrayList<>();
        names.add("Bitcoin");
        names.add("Bitcoins");


        assertEquals(facade.getSelectedNames(),names);





    }

    /**
     * Controller check crypto exists test.
     */

    @Test
    public void controllerCheckCurrencyExist(){



        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");


        CMCInputOffline input = new CMCInputOffline();
        TwilioOffline output = new TwilioOffline();



        Facade facade = new Facade(input, output);
        assertFalse(facade.checkCurrencyExist(bit1));





    }


    /**
     * Controller get boolean for sending message test.
     */
    @Test
    public void controllerCheckSend(){






        CMCInputOffline input = new CMCInputOffline();
        TwilioOffline output = new TwilioOffline();



        Facade facade = new Facade(input, output);
        assertFalse(facade.getSend());

        facade.setSend(true);

        assertTrue(facade.getSend());





    }

    /**
     * controller check language translation test. User will be given limited option so no need for defensive test here.
     */

    @Test
    public void controllerCheckLanguage(){



        CMCInputOffline input = new CMCInputOffline();
        TwilioOffline output = new TwilioOffline();



        Facade facade = new Facade(input, output);
        assertFalse(facade.getSend());

        facade.setLanguage("english");

        assertEquals(facade.getTranslation("name"),"Name");





    }


    /**
     * New feature test CMCAPI with valid input.
     */

    @Test
    public void newFeatureTestValid(){



        CMCInputOffline input = new CMCInputOffline();
        assertTrue(input.setThreshold("0.5"));

        assertFalse(input.checkThreshold(0.01));

        assertTrue(input.checkThreshold(0.6));

        assertTrue(input.checkThreshold(0.5));






    }



    /**
     * New feature test CMCAPI with invalid input.
     */

    @Test
    public void newFeatureTestInvalid(){



        CMCInputOffline input = new CMCInputOffline();
        assertFalse(input.setThreshold("one"));
        assertFalse(input.setThreshold("1.1"));
        assertFalse(input.setThreshold("0.09"));
        assertTrue(input.setThreshold("0.5"));





    }


    /**
     * New feature test Facade with valid input.
     */

    @Test
    public void newFeatureTestValidFacade(){

        TwilioAPI output = new TwilioOffline();



        CMCInputOffline input = new CMCInputOffline();

        Facade facade = new Facade(input,output);
        assertTrue(facade.setThreshold("0.5"));
        assertTrue(facade.checkThreshold(0.6));
        assertTrue(facade.checkThreshold(0.5));
        assertFalse(facade.checkThreshold(0.4));






    }

    /**
     * New feature test Facade with valid input.
     */

    @Test
    public void newFeatureTestInvalidFacade(){

        TwilioAPI output = new TwilioOffline();



        CMCInputOffline input = new CMCInputOffline();

        Facade facade = new Facade(input,output);
        assertFalse(facade.setThreshold("one"));
        assertFalse(input.setThreshold("1.1"));
        assertFalse(input.setThreshold("0.09"));
        assertTrue(input.setThreshold("0.5"));








    }













    



}

