package major.model;



/**
 *Message Class
 */

public class Message {


    private String To;
    private String From;


    private String Body;



    public Message( String from, String to,String body){


        this.From = from;
        this.To = to;
        this.Body = body;
    }

    /**
     * Get to number
     */

    public String getTo(){return this.To;}

    /**
     * Get from number
     */


    public String getFrom(){return this.From;}


    /**
     * Get body of the message
     */

    public String getBody(){return this.Body;}
}
