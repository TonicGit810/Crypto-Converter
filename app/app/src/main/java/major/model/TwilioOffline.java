package major.model;

import java.util.ArrayList;

public class TwilioOffline implements TwilioAPI {
    private ArrayList<Message> messages;
    public TwilioOffline(){
        this.messages = new ArrayList<>();

    }


    @Override
    public void addMessages(String id, String convert, String amount, String result, ArrayList<Currency> selected_list) {
        StringBuilder sb = new StringBuilder();
        String from = System.getenv("TWILIO_API_FROM");
        String to = System.getenv("TWILIO_API_TO");



        if(id.equals(null) || convert.equals(null) || amount.equals(null) || result.equals(null)){

            this.messages.add(new Message(from,to, "Something went wrong when converting"));

        }else{
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


            sb.append(String.format("Convert from: %s, Symbol: %s\n",id_name,id_symbol));
            sb.append(String.format("Convert to: %s, Symbol: %s\n",cv_name,cv_symbol));
            sb.append(String.format("Amount: %,.2f , Rate: %,.2f, Result: %,.2f\n",damount,rate,dresult));

            this.messages.add(new Message(from,to, sb.toString()));

        }

    }

    @Override
    public void SendMessage(Message message) {
        System.out.println(message.getFrom());
        System.out.println(message.getTo());
        System.out.println(message.getBody());

    }

    @Override
    public Message getLastMessages() {
        if(this.messages.isEmpty()){
            String from = System.getenv("TWILIO_API_FROM");
            String to = System.getenv("TWILIO_API_TO");
            return (new Message(from,to, "Something went wrong when converting"));
        }
        return this.messages.get(messages.size()-1);
    }

    @Override
    public void injectHttp(HTTP http) {

    }


}
