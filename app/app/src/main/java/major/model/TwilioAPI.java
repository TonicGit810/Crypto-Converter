package major.model;

import java.net.http.HttpClient;
import java.util.ArrayList;

public interface TwilioAPI {




    void addMessages(String id, String convert, String amount, String result, ArrayList<Currency> selected_list);

    void SendMessage(Message message);
    Message getLastMessages();

    void injectHttp(HTTP http);
}
