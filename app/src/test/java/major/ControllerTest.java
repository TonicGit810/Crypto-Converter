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

public class ControllerTest {
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

}
