package major;

import com.google.gson.JsonObject;
import major.model.CMCInputOnline;
import major.model.Currency;
import major.model.Http;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class LazyLoadTest {

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
}
