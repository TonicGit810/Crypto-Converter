package major;

import major.model.CMCInputOffline;
import major.model.Currency;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CMCOfflineTest {
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
}
