package major.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import major.model.Controller;
import major.model.Currency;

import java.util.Optional;

public class AllCurrencyPane {

    private Controller model;
    private Stage stage;

    private TablePane table;
    public AllCurrencyPane(Controller model, TablePane table){
        this.model = model;
        this.stage = new Stage();
        this.table = table;
        setUp();

    }



    private void setUp(){
        //        ArrayList<Currency> observe = new ArrayList<>();
//        ArrayList<String> od = new ArrayList<>();
//        for(int i = 0;i<10000;i++){
//            observe.add(new Currency("1","bit","dsd","ddd","https://s2.coinmarketcap.com/static/img/coins/64x64/19985.png","dddd",od) );
//
//        }
//
//        ObservableList<Currency> oblist = FXCollections.observableList(observe);


        ObservableList<Currency> oblist = FXCollections.observableList(model.getCurrencies());
        TableView tv = new TableView();
        tv.setItems(oblist);



        stage.setTitle("Add Currency");


        TableColumn<Currency,String> name = new TableColumn("Name");
        TableColumn<Currency,String> symbol = new TableColumn("Symbol");
        TableColumn<Currency,Void> action = new TableColumn();
        Callback<TableColumn<Currency, Void>, TableCell<Currency, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Currency, Void> call(final TableColumn<Currency, Void> param) {
                final TableCell<Currency, Void> cell = new TableCell<>() {

                    private final Button btn = new Button("Add");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Currency currency = getTableView().getItems().get(getIndex());
                            if(model.checkCurrencyExist(currency)){

                                ButtonType yes = new ButtonType("yes", ButtonBar.ButtonData.OK_DONE);
                                ButtonType no = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);
                                Alert alert = new Alert(Alert.AlertType.WARNING,"The currency is already existed in the database. Do you want to request new data or not?",yes,no);
                                alert.setTitle("Load");
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.orElse(no)==yes){
                                    currency.getWebsite();
                                    model.add(currency);
                                    model.updateDB(currency);


                                }else{
                                    Currency loaded = model.loadDB(currency);
                                    model.add(loaded);


                                }

                            }else{
                                model.add(currency);
                                model.addDB(currency);

                            }
                            table.update();
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

        action.setCellFactory(cellFactory);

        name.setCellValueFactory( new PropertyValueFactory<>("name"));
        symbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));

        name.prefWidthProperty().bind(tv.widthProperty().multiply(0.3));
        symbol.prefWidthProperty().bind(tv.widthProperty().multiply(0.3));
        action.prefWidthProperty().bind(tv.widthProperty().multiply(0.3));

        name.setResizable(false);
        symbol.setResizable(false);
        action.setResizable(false);
        tv.getColumns().addAll(name,symbol,action);
        Scene s1 = new Scene(tv, 500, 800);
        stage.setScene(s1);

    }






    public Stage getStage(){

        return this.stage;

    }

}
