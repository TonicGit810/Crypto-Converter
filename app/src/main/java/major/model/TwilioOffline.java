package major.model;

import java.util.ArrayList;
import java.util.HashMap;







/**
 *TwilioAPI Offline mode
 */
public class TwilioOffline implements TwilioAPI {
    private ArrayList<Message> messages;

    private boolean send = false;


    public TwilioOffline(){
        this.messages = new ArrayList<>();

    }



    /**
     * Add message.
     * @param id ID to convert from.
     * @param convert ID to convert to.
     * @param amount The amount of crypto converted.
     * @param result Thre result of the conversion.
     */
    @Override
    public void addMessages(String id, String convert, String amount, String result, ArrayList<Currency> selected_list, HashMap<String,String> translation) {
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

            for(int i = 0; i<selected_list.size(); i++){
                if(selected_list.get(i).getId().equals(id)){
                    id_name = selected_list.get(i).getName();
                    id_symbol = selected_list.get(i).getSymbol();
                }

                if(selected_list.get(i).getId().equals(convert)){
                    cv_name = selected_list.get(i).getName();
                    cv_symbol = selected_list.get(i).getSymbol();
                }
            }

            System.out.println(id_name);
            System.out.println(cv_name);


//            sb.append(String.format("Convert from: %s, Symbol: %s\n",id_name,id_symbol));
//            sb.append(String.format("Convert to: %s, Symbol: %s\n",cv_name,cv_symbol));
//            sb.append(String.format("Amount: %,.2f , Rate: %,.2f, Result: %,.2f\n",damount,rate,dresult));

            sb.append(String.format(translation.get("Convert From: ")+"%s,"+translation.get("symbol")+": %s\n",id_name,id_symbol));
            sb.append(String.format(translation.get("to")+"%s,"+translation.get("symbol")+": %s\n",cv_name,cv_symbol));
            sb.append(String.format(translation.get("Amount: ")+"%,.2f\n"+translation.get("rate")+"%,.2f\n"+translation.get("result")+"%,.2f\n",damount,rate,dresult));
            this.messages.add(new Message(from,to, sb.toString()));


        }catch (Exception e){

            this.messages.add(new Message(from,to, "Error when processing data"));

        }









    }

    /**
     * Send message. This is offline so it will only print the message
     * @param message
     * @return true
     */

    @Override
    public boolean sendMessage(Message message) {
        System.out.println(message.getFrom());
        System.out.println(message.getTo());
        System.out.println(message.getBody());

        return true;

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
     * Do nothing since offline.
     */
    @Override
    public void injectHttp(Http http) {

    }

    /**
     * Check if the message can be sent.
     * @retun if the message can be sent
     */

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
