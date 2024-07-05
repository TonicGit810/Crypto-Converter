package major;

import major.model.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurrencyTest {

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
}
