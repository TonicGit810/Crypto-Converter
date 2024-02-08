package major.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import major.model.Facade;
/**
 *Class to setup about page
 */
public class About {
    private Stage stage;

    private Facade model;


    public About(Facade model){

        this.stage = new Stage();
        this.model = model;
        setup();

    }



    /**
     * Setup the page
     */
    private void setup(){


        stage.setTitle(model.getTranslation("about"));
        Font font = Font.font("Arial", FontWeight.BOLD,20);

        Label appName = new Label(model.getTranslation("appName"));
        appName.setFont(font);

        Text name = new Text("Crypto Converter");
        Label about = new Label(model.getTranslation("about"));
        about.setFont(new Font("Arial",30));
        Label author = new Label(model.getTranslation("author"));
        author.setFont(font);
        Text authorName = new Text("Tony Wang");
        Label reference = new Label(model.getTranslation("reference"));
        reference.setFont(font);
        VBox refBox = new VBox(20);

        VBox nameBox = new VBox(20);

        Text input = new Text(model.getTranslation("input")+"\nCoin Market Cap API V1: https://coinmarketcap.com/api/documentation/v1/");

        Text output = new Text(model.getTranslation("output")+"\nTwilio API: https://www.twilio.com/");

        Text splash = new Text(model.getTranslation("splash")+"\nhttps://www.instagram.com/artrachen/");

        Text error = new Text(model.getTranslation("error")+"\nhttps://pixabay.com/vectors/false-error-is-missing-absent-x-2061132/");

        Text request = new Text(model.getTranslation("twiliobuild")+"\nhttps://mkyong.com/java/how-to-send-http-request-getpost-in-java");

        Text sql = new Text(model.getTranslation("sql")+"\n SOFT3202 WK7 tutorial");
        Text http = new Text(model.getTranslation("http")+"\n SOFT3202 WK6 tutorial");


        nameBox.getChildren().addAll(about,appName,name,author,authorName,reference);
        refBox.getChildren().addAll(input,output,splash,error,request,sql,http);

        nameBox.setAlignment(Pos.CENTER);

        nameBox.setSpacing(10);

        BorderPane pane = new BorderPane();
        pane.setTop(nameBox);
        pane.setCenter(refBox);


        Scene scene = new Scene(pane,400,600);

        stage.setScene(scene);





    }



    /**
     *Get Stage
     * @return stage
     */

    public Stage getStage(){
        return this.stage;
    }
}
