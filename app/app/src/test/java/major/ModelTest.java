
package major;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import major.model.*;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModelTest {


    @Test
    public void messageGetter(){
        Message message = new Message("111111","111111","body");
        assertEquals(message.getFrom(),"111111");
        assertEquals(message.getTo(),"111111");
        assertEquals(message.getBody(),"body");
    }




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


        HTTP http = mock(HTTP.class);


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


    @Test
    public void CMCOnlineTestGetCurrencies(){

        HTTP http = mock(HTTP.class);

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


    @Test
    public void CMCOnlineTestGetConvert(){

        HTTP http = mock(HTTP.class);

        Mockito.when(http.getConvert(anyString(),anyString(),anyString())).thenReturn("1");

        CMCInputOnline input = new CMCInputOnline();
        input.injectHttp(http);

        assertEquals(input.convert("1","1","1"),"1");
        verify(http,times(1)).getConvert("1","1","1");


    }


    @Test
    public void CMCOnlineTestAdd(){
        CMCInputOnline input = new CMCInputOnline();
        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        input.add(bit1);
        input.add(bit2);
        assertEquals(input.getSelectListSize(),2);
        assertEquals(input.getSelected_list().get(0),bit1);
        assertEquals(input.getSelected_list().get(1),bit2);

    }

    @Test
    public void CMCOnlineTestRemove(){
        CMCInputOnline input = new CMCInputOnline();
        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        input.add(bit1);
        input.add(bit2);



        input.remove(bit1);
        assertEquals(input.getSelectListSize(),1);
        assertEquals(input.getSelected_list().get(0),bit2);

    }

    @Test
    public void CMCOnlineTestClear(){
        CMCInputOnline input = new CMCInputOnline();
        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        input.add(bit1);
        input.add(bit2);



        input.clear();
        assertEquals(input.getSelected_list().size(),0);


    }

    @Test
    public void CMCOnlineTestGetIdByName(){
        CMCInputOnline input = new CMCInputOnline();
        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        input.add(bit1);
        input.add(bit2);



        assertEquals(input.getIdByName("Bitcoin"),"1");


    }

    @Test
    public void CMCOnlineDBTest(){
        SQL sql = mock(SQL.class);

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Mockito.when(sql.loadCurrency(anyString())).thenReturn(bit1);

        CMCInputOnline input = new CMCInputOnline();
        input.injectSQL(sql);

        assertEquals(input.loadDB(bit1),bit1);
        verify(sql,times(1)).loadCurrency("1");
    }


    @Test
    public void CMCOnlineDBTestVoid(){
        SQL sql = mock(SQL.class);

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


    @Test
    public void CMCOnlineDBTestCheck(){
        SQL sql = mock(SQL.class);

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");


        Mockito.when(sql.checkCurrency(anyString())).thenReturn(Boolean.TRUE);


        CMCInputOnline input = new CMCInputOnline();
        input.injectSQL(sql);

        input.addDB(bit1);
        assertTrue(input.checkCurrencyExist("1"));
        verify(sql,times(1)).checkCurrency("1");
    }


    @Test
    public void CMCOfflineTest(){
        CMCInputOffline input = new CMCInputOffline();


        assertEquals(input.getCurrencies().size(),2);

        assertEquals(input.convert("1","1","1"),"1");


        assertNull(input.loadCrypto("1"));
        assertFalse(input.checkCurrencyExist("1"));
        assertNull(input.getIdByName("Bitcoin"));


        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        assertNull(input.loadDB(bit1));

        input.add(bit1);
        input.add(bit2);



        assertEquals(input.getSelectListSize(),2);
        assertEquals(input.getSelected_list().get(0),bit1);
        assertEquals(input.getSelected_list().get(1),bit2);


        input.remove(bit1);
        assertEquals(input.getSelectListSize(),1);
        assertEquals(input.getSelected_list().get(0),bit2);


        input.clear();
        assertEquals(input.getSelected_list().size(),0);

    }



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


    @Test
    public void TwilioOnlineTestSendMessage(){

        HTTP http = mock(HTTP.class);

        TwilioOnline output = new TwilioOnline();

        output.injectHttp(http);
        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");
        Message message = new Message(from,to, "Something went wrong when converting");


        http.sendMessage(message);

        verify(http,times(1)).sendMessage(message);

    }



    @Test
    public void TwilioOnlineTestAddMessage(){



        TwilioOnline output = new TwilioOnline();

        Currency bit1 = new Currency("1","Bitcoin","BTC","It is a crypto","week1","www","splash.jpg");
        Currency bit2 = new Currency("2","Bitcoins","BTCS","It is a crypto","week2","www","splash.jpg");

        ArrayList<Currency> selected_list = new ArrayList<>();
        selected_list.add(bit1);
        selected_list.add(bit2);

        output.addMessages("1","2","1","1",selected_list);



        StringBuilder sb = new StringBuilder();


        sb.append("Convert from: Bitcoin, Symbol: BTC\n");
        sb.append("Convert to: Bitcoins, Symbol: BTCS\n");
        sb.append("Amount: 1.00 , Rate: 1.00, Result: 1.00\n");

        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");
        Message message = new Message(from,to, sb.toString());

        assertEquals(output.getLastMessages().getBody(),message.getBody());
        assertEquals(output.getLastMessages().getFrom(),message.getFrom());
        assertEquals(output.getLastMessages().getTo(),message.getTo());




    }



    @Test
    public void TwilioOfflineTest(){
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

        output.addMessages("1","2","1","1",selected_list);



        StringBuilder sb = new StringBuilder();


        sb.append("Convert from: Bitcoin, Symbol: BTC\n");
        sb.append("Convert to: Bitcoins, Symbol: BTCS\n");
        sb.append("Amount: 1.00 , Rate: 1.00, Result: 1.00\n");


        Message message2 = new Message(from,to, sb.toString());

        assertEquals(output.getLastMessages().getBody(),message2.getBody());
        assertEquals(output.getLastMessages().getFrom(),message2.getFrom());
        assertEquals(output.getLastMessages().getTo(),message2.getTo());


    }











    



}

