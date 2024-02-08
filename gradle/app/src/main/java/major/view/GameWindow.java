package major.view;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import major.model.Controller;


public class GameWindow {
    private Scene scene;
    private VBox sideButtons;

    private TablePane tablePane;

    private Controller model;
    private AllCurrencyPane all;

    private About ab;





    public GameWindow(Controller model){
        BorderPane pane = new BorderPane();
        this.scene = new Scene(pane);
        this.tablePane = new TablePane(model);
        buildSideButtons();
        pane.setRight(sideButtons);
        pane.setCenter(tablePane.getPane());
        this.model = model;
        this.all = new AllCurrencyPane(model,tablePane);
        this.ab = new About();



    }


    private void buildSideButtons(){
        Button add_currency = new Button("Add Currency");
        add_currency.setPrefWidth(100);
        add_currency.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addAction();
            }
        });
        Button clear = new Button("Clear List");
        clear.setPrefWidth(100);
        clear.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearAction();

            }
        });


        Button clearCache = new Button("Clear Cache");
        clearCache.setPrefWidth(100);
        clearCache.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearCacheActon();

            }
        });


        Button about = new Button("About");
        about.setPrefWidth(100);
        about.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                aboutAction();

            }
        });

        this.sideButtons = new VBox(add_currency,clear,clearCache,about);

    }


    private  void clearAction(){
        this.model.clear();
        this.tablePane.update();
    }


    private void clearCacheActon(){
        this.model.clearCache();
    }

    private void addAction(){


        Stage add = all.getStage();
        add.show();

    }

    private void aboutAction(){
        Stage about = ab.getStage();
        about.show();
    }

    public Scene getScene(){return this.scene;}

}
