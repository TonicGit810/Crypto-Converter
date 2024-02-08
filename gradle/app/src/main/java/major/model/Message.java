package major.model;

public class Message {


    private String To;
    private String From;


    private String Body;



    public Message( String from, String to,String body){


        this.From = from;
        this.To = to;
        this.Body = body;
    }

    public String getTo(){return this.To;}

    public String getFrom(){return this.From;}

    public String getBody(){return this.Body;}
}
