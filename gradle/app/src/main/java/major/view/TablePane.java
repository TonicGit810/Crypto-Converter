package major.view;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import major.model.Controller;
import major.model.Currency;

import java.util.ArrayList;


public class TablePane extends Application {

    private TableView tableView;

    private StackPane pane;



    private Controller model;



    private HBox Hbox;
    public TablePane(Controller modell){
        this.tableView = new TableView();

        this.pane = new StackPane(tableView);

        this.model = modell;
        setUp();



    }


    private void setUp(){
        TableColumn<Currency, String> logo = new TableColumn("Logo");
        TableColumn<Currency,String> name = new TableColumn("Name");
        TableColumn<Currency,String> symbol = new TableColumn("Symbol");
        TableColumn<Currency,String> description = new TableColumn("Description");
        TableColumn<Currency,String> date = new TableColumn("Date Launched");
        TableColumn<Currency,String> website = new TableColumn("Website");
        TableColumn<Currency,Void> convert = new TableColumn("Convert");
        TableColumn<Currency,Void> remove = new TableColumn("Remove");



        Callback<TableColumn<Currency, String>, TableCell<Currency, String>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Currency, String> call(final TableColumn<Currency, String> param) {
                final TableCell<Currency, String> cell = new TableCell<>() {
                    ImageView img = new ImageView();


                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item!= null) {
                            Image pic = new Image(item);
                            img.setImage(pic);
                            img.setFitWidth(50);
                            img.setFitHeight(50);
                            setGraphic(img);
                        }else{
                            setGraphic(null);
                        }
                    }
                };
                return cell;
            }
        };


        Callback<TableColumn<Currency, Void>, TableCell<Currency, Void>> cellFactoryconvert = new Callback<>() {
            @Override
            public TableCell<Currency, Void> call(final TableColumn<Currency, Void> param) {
                final TableCell<Currency, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Convert");

                    {

                        btn.setOnAction((ActionEvent event) -> {
                            Currency currency = getTableView().getItems().get(getIndex());
                            convertAction(currency.getName());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };



        Callback<TableColumn<Currency, Void>, TableCell<Currency, Void>> cellFactoryremove = new Callback<>() {
            @Override
            public TableCell<Currency, Void> call(final TableColumn<Currency, Void> param) {
                final TableCell<Currency, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Remove");

                    {

                        btn.setOnAction((ActionEvent event) -> {
                            Currency currency = getTableView().getItems().get(getIndex());
                            remove(currency);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };


        Callback<TableColumn<Currency, String>, TableCell<Currency, String>> cellFactoryWeb = new Callback<>() {
            @Override
            public TableCell<Currency, String> call(final TableColumn<Currency, String> param) {
                final TableCell<Currency, String> cell = new TableCell<>() {

                    Hyperlink link = new Hyperlink();


                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            link.setText(item);
                            link.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    getHostServices().showDocument(link.getText());
                                }
                            });
                            setGraphic(link);
                        }
                    }
                };
                return cell;
            }
        };














        logo.setCellValueFactory(new PropertyValueFactory<>("logo"));
        logo.setCellFactory(cellFactory);

        website.setCellValueFactory(new PropertyValueFactory<>("website"));
        website.setCellFactory(cellFactoryWeb);

        name.setCellValueFactory( new PropertyValueFactory<>("name"));
        symbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
        description.setCellValueFactory( new PropertyValueFactory<>("description"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        convert.setCellFactory(cellFactoryconvert);
        remove.setCellFactory(cellFactoryremove);
        description.setPrefWidth(230);


        tableView.getColumns().addAll(logo,name,symbol,description,date,website,convert,remove);


    }


    private void convertAction(String name){

        VBox box = new VBox();

        Text name1 = new Text(String.format("Convert From: %s",name));
        name1.setFont(Font.font(20));



        Text am = new Text("Amount: ");
        am.setFont((Font.font(20)));


        Text to = new Text("to");
        to.setFont(Font.font(20));


        Text rate = new Text("Conversion rate is: ");
        Text total = new Text("Conversion result is: ");

        rate.setFont((Font.font(20)));
        total.setFont((Font.font(20)));


        TextField amount = new TextField();


        amount.setFont(Font.font(20));


        ObservableList<String> new_list = FXCollections.observableArrayList(model.getSelectedNames());



        ComboBox comboBox = new ComboBox(new_list);
        Button button = new Button("Convert");


        button.setOnAction((ActionEvent event)->{

            if(comboBox.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Empty Box");
                alert.showAndWait();
                return;

            }
            String value = (String) comboBox.getValue();

            String convert_id = model.getIdByName(value);
            String id = model.getIdByName(name);



            System.out.println(id);
            System.out.println(convert_id);
            System.out.println(amount.getText());

            if(!amount.getText().isEmpty()){
                try{
                    double damount = Double.parseDouble(amount.getText());
                    String result = model.convert(id,convert_id,amount.getText());
                    double dresult = Double.parseDouble(result);

                    double drate = dresult/damount;
                    if(damount == 0){
                        drate = 0;
                    }

                    rate.setText(String.format("Conversion rate is: %f",drate));

                    this.model.addMessages(id,convert_id,amount.getText(),result);


                    total.setText(String.format("Conversion result is: %s",result));
                }catch(NumberFormatException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Wrong Amount");
                    alert.showAndWait();
                    return;


                }

            }



        });



        Button button1 = new Button("Send");

        button1.setOnAction((ActionEvent event)->{

           model.message(model.getLastMessages());

        });

        box.getChildren().addAll(name1,am, amount,to,comboBox,button,rate,total,button1);



        Stage stage = new Stage();
        Scene s1 = new Scene(box, 500, 500);
        stage.setTitle("Convert");
        stage.setScene(s1);
        stage.show();

    }


    public void update(){

        ObservableList<Currency> cur_list = FXCollections.observableArrayList( model.getSelected_list());




        tableView.setItems(cur_list);

    }

    public void add(Currency currency){
        model.add(currency);


    }

    public void remove(Currency currency){

        model.remove(currency);
        update();

    }


    public void clear(){

        model.clear();


    }


    public StackPane getPane(){
        return this.pane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
