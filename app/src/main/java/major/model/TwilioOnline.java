package major.model;

import java.util.ArrayList;
import java.util.HashMap;



/**
 *TwiloAPI Online mode
 */
public class TwilioOnline implements TwilioAPI {
    private ArrayList<Message> messages;
    private Http http;

    private boolean send = false;




    public TwilioOnline(){
        messages = new ArrayList<>();
    }



    /**
     * Add message.
     * @param id ID to convert from.
     * @param convert ID to convert to.
     * @param amount The amount of crypto converted.
     * @param result The result of the conversion.
     * @param selectedList the list of selected crypto to get the information of the currency.
     * @param translation the map of the translation data.
     */

    @Override
    public void addMessages(String id, String convert, String amount, String result, ArrayList<Currency>selectedList, HashMap<String,String> translation) {
        StringBuilder sb = new StringBuilder();
        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");


        try{
            double damount = Double.parseDouble(amount);
            double dresult = Double.parseDouble(result);
            double rate = dresult/damount;


            String id_name = "";
            String id_symbol = "";

            String cv_name = "";
            String cv_symbol = "";

            for(int i = 0; i<selectedList.size(); i++){
                if(selectedList.get(i).getId().equals(id)){
                    id_name = selectedList.get(i).getName();
                    id_symbol = selectedList.get(i).getSymbol();
                }

                if(selectedList.get(i).getId().equals(convert)){
                    cv_name = selectedList.get(i).getName();
                    cv_symbol = selectedList.get(i).getSymbol();
                }
            }

            System.out.println(id_name);
            System.out.println(cv_name);



            sb.append(String.format(translation.get("Convert From: ")+"%s,"+translation.get("symbol")+": %s\n",id_name,id_symbol));
            sb.append(String.format(translation.get("to")+"%s,"+translation.get("symbol")+": %s\n",cv_name,cv_symbol));
            sb.append(String.format(translation.get("Amount: ")+"%,.2f\n"+translation.get("rate")+"%,.2f\n"+translation.get("result")+"%,.2f\n",damount,rate,dresult));
            this.messages.add(new Message(from,to, sb.toString()));


        }catch (Exception e){

            this.messages.add(new Message(from,to, "Error when processing data"));

        }









    }

    /**
     * Send message.
     * @param message
     * @return if the message is sent or not.
     */

    @Override
    public boolean sendMessage(Message message) {



        return http.sendMessage(message);



    }



    /**
     * Return the last message in the TwilioAPI to send.
     * @return Message object to send.
     */

    @Override
    public Message getLastMessages() {
        if(this.messages.isEmpty()){
            String from = System.getenv("TWILIO_API_FROM");
            String to = System.getenv("TWILIO_API_TO");
            return (new Message(from,to, "Something went wrong when converting"));
        }
        return this.messages.get(messages.size()-1);
    }

    /**
     * Inject http object
     * @param http the http object to inject.
     */
    @Override
    public void injectHttp(Http http) {
        this.http = http;
    }

    /**
     * Check if the message can be sent.
     * @retun if the message can be sent
     */
    @Override
    public boolean getSend() {
        return send;
    }

    /**
     * Set the send boolean value.
     * @param  send set the boolean value of send
     */
    @Override
    public void setSend(boolean send) {
        this.send = send;
    }
}
