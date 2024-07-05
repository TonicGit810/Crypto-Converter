package major;

import major.model.Currency;
import major.model.Http;
import major.model.Message;
import major.model.TwilioOnline;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TwilioOnlineTest {
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
}
