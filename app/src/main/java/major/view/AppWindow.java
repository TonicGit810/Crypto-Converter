package major.view;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import major.model.Facade;

import java.util.Optional;


/**
 *Class to generate main window
 */
public class AppWindow {
    private Scene scene;
    private VBox sideButtons;

    private TablePane tablePane;

    private Facade model;
    private AllCurrencyPane all;

    private About ab;

    private boolean thresh = false;








    public AppWindow(Facade model){


        BorderPane pane = new BorderPane();


        this.scene = new Scene(pane);
        this.model = model;




        this.tablePane = new TablePane(this.model);
        this.all = new AllCurrencyPane(this.model,tablePane);
        this.ab = new About(this.model);



        buildSideButtons();
        pane.setRight(sideButtons);
        pane.setCenter(tablePane.getPane());

        while(!thresh){
            thresholdAction();
        }



    }

    /**
     * Build the side buttons
     */


    private void buildSideButtons(){

        Button add_currency = new Button(model.getTranslation("addCurrency"));
        add_currency.setPrefWidth(100);
        add_currency.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addAction();
            }
        });

        Button clear = new Button(model.getTranslation("clearlist"));
        clear.setPrefWidth(100);
        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearAction();

            }
        });



        Button clearCache = new Button(model.getTranslation("clearcache"));
        clearCache.setPrefWidth(100);
        clearCache.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearCacheActon();

            }
        });



        Button about = new Button(model.getTranslation("about"));
        about.setPrefWidth(100);
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aboutAction();

            }
        });

        this.sideButtons = new VBox(add_currency,clear,clearCache,about);

    }


    /**
     * clear the selected list.
     */
    private  void clearAction(){
        this.model.clear();
        this.tablePane.update();
    }




    /**
     * clear the cache.
     */

    private void clearCacheActon(){
        this.model.clearCache();
    }

    /**
     * add currency to main window
     */

    private void addAction(){


        Stage add = all.getStage();
        add.show();

    }

    /**
     * Show About Page
     */

    private void aboutAction(){
        Stage about = ab.getStage();
        about.show();
    }

    /**
     * Ask user for threshold
     */

    private void thresholdAction(){

        TextInputDialog textInput = new TextInputDialog("");
        textInput.setTitle("Set Threshold");
        textInput.setHeaderText("Enter threshold");

        Optional<String> input = textInput.showAndWait();
        if (input.isPresent()) {
            String threshold = input.get();

            if(model.setThreshold(threshold)){

                thresh = true;
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle("Alert"); // if the type of data input is wrong the alert will show.
                alert.setContentText("Invalid threshold! Must between 0.1 and 1!");
                alert.showAndWait();
                thresh = false;
            }


        }

    }

    /**
     * return scene
     * @return the Scene.
     */

    public Scene getScene(){return this.scene;}

}
