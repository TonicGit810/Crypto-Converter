package major.model;

import java.util.ArrayList;
import java.util.HashMap;





/**
 *Twilio API Interface
 */
public interface TwilioAPI {



    /**
     * Add message.
     * @param id ID to convert from.
     * @param convert ID to convert to.
     * @param amount The amount of crypto converted.
     * @param result The result of the conversion.
     * @param selectedList the list of selected crypto to get the information of the currency.
     * @param translation the map of the translation data.
     */

    void addMessages(String id, String convert, String amount, String result, ArrayList<Currency> selectedList, HashMap<String,String> translation);


    /**
     * Send message.
     * @param message
     * @return if the message is sent or not.
     */
    boolean sendMessage(Message message);


    /**
     * Return the last message in the TwilioAPI to send.
     * @return Message object to send.
     */

    Message getLastMessages();

    /**
     * Inject http object
     * @param http the http object to inject.
     */

    void injectHttp(Http http);

    /**
     * Check if the message can be sent.
     * @retun if the message can be sent
     */
    boolean getSend();

    /**
     * Set the send boolean value.
     * @param  send set the boolean value of send
     */
    void setSend(boolean send);
}
