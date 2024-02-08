package major.model;

import java.util.ArrayList;

public class Controller {

    private CMCAPI input;
    private TwilioAPI output;


    public Controller(CMCAPI input, TwilioAPI output){
        this.input = input;
        this.output = output;
    }


    public ArrayList<Currency> getCurrencies(){
        return input.getCurrencies();
    };
    public void add(Currency c){
        input.add(c);
    };

    public void remove(Currency c){
        input.remove(c);
    };
    public void clear(){
        input.clear();
    };

    public Message getLastMessages(){
        return output.getLastMessages();
    };
    public void addMessages(String id, String convert, String amount, String result){
        output.addMessages(id,convert,amount,result, input.getSelected_list());
    };
    public String convert(String id, String convert, String amount){
        return input.convert(id,convert,amount);
    }
    public void message(Message message){
        output.SendMessage(message);
    };
    public int getSelectListSize(){
        return input.getSelectListSize();
    }
    public String getIdByName(String name){
        return input.getIdByName(name);
    }


    public ArrayList<Currency> getSelected_list(){
        return input.getSelected_list();
    }


    public ArrayList<String> getSelectedNames(){
        return input.getSelectedNames();
    }

    public boolean checkCurrencyExist(Currency currency){
        String id = currency.getId();
        return input.checkCurrencyExist(id);
    }

    public void clearCache(){
        input.clearDB();
    }

    public void updateDB(Currency currency){
        input.updateDB(currency);
    }

    public void addDB(Currency currency){
        input.addDB(currency);
    }

    public Currency loadDB(Currency currency){

        return input.loadDB(currency);
    }




}
