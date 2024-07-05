package major;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import major.model.CMCInputOnline;
import major.model.Currency;
import major.model.Http;
import major.model.Sql;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class CMCOnlineTest {
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
}
