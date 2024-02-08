package major.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import major.model.Controller;

import java.awt.*;

public class About {
    private Stage stage;


    public About(){

        this.stage = new Stage();
        Setup();

    }



    private void Setup(){


        stage.setTitle("About");

        Font font = Font.font("Arial", FontWeight.BOLD,20);
        Label appName = new Label("App name");
        appName.setFont(font);
        Text name = new Text("Crypto Converter");
        Label about = new Label("About");
        about.setFont(new Font("Arial",30));
        Label author = new Label("Author");
        author.setFont(font);
        Text authorName = new Text("Tony Wang");
        Label reference = new Label("Reference");
        reference.setFont(font);
        VBox refBox = new VBox(20);

        VBox nameBox = new VBox(20);

        Text input = new Text("Input API:\nCoin Market Cap API V1: https://coinmarketcap.com/api/documentation/v1/");

        Text output = new Text("Output API:\nTwilio API: https://www.twilio.com/");


        nameBox.getChildren().addAll(about,appName,name,author,authorName,reference);
        refBox.getChildren().addAll(input,output);

        nameBox.setAlignment(Pos.CENTER);

        nameBox.setSpacing(10);

        BorderPane pane = new BorderPane();
        pane.setTop(nameBox);
        pane.setCenter(refBox);


        Scene scene = new Scene(pane,400,400);

        stage.setScene(scene);





    }





    public Stage getStage(){
        return this.stage;
    }
}
