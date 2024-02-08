package major.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import major.model.Facade;
import major.model.Currency;

import java.util.ArrayList;
import java.util.Optional;
/**
 *Class to create page that display all the crypto.
 */
public class AllCurrencyPane {

    private Facade model;
    private Stage stage;

    private TablePane table;
    public AllCurrencyPane(Facade model, TablePane table){
        this.model = model;
        this.stage = new Stage();
        this.table = table;
        setUp();

    }


    /**
     * Setup the page
     */

    private void setUp(){

        TableView<Currency> tv = new TableView<Currency>();


        if(model.getCurrencies()==null){
            ObservableList<Currency> oblist = FXCollections.observableList(new ArrayList<Currency>());
            tv.setItems(oblist);

        }else{
            ObservableList<Currency> oblist = FXCollections.observableList(model.getCurrencies());
            tv.setItems(oblist);

        }





        tv.setPlaceholder(new Label(model.getTranslation("apiError")));





        stage.setTitle(model.getTranslation("addCurrency"));




        TableColumn<Currency,String> name = new TableColumn<Currency,String>(model.getTranslation("name"));
        TableColumn<Currency,String> symbol = new TableColumn<Currency,String>(model.getTranslation("symbol"));

        TableColumn<Currency,Void> action = new TableColumn<Currency,Void>();
        Callback<TableColumn<Currency, Void>, TableCell<Currency, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Currency, Void> call(final TableColumn<Currency, Void> param) {
                final TableCell<Currency, Void> cell = new TableCell<Currency,Void>() {

                    private final Button btn = new Button(model.getTranslation("add"));

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Currency currency = getTableView().getItems().get(getIndex());
                            if(model.checkCurrencyExist(currency)){



                                ButtonType yes = new ButtonType(model.getTranslation("yes"), ButtonBar.ButtonData.OK_DONE); // ask if user want to use existed data.
                                ButtonType no = new ButtonType(model.getTranslation("no"), ButtonBar.ButtonData.CANCEL_CLOSE);
                                Alert alert = new Alert(Alert.AlertType.WARNING,model.getTranslation("exist"),yes,no);

//                                alert.setTitle("Load");

                                alert.setTitle("Load");
                                Optional<ButtonType> result = alert.showAndWait();
                                if (result.orElse(no)==yes){
                                    currency.getWebsite();
                                    model.add(currency);
                                    model.updateDB(currency);


                                }else{
                                    Currency loaded = model.loadDB(currency);
                                    if(loaded == null){
                                        Alert sqlError = new Alert(Alert.AlertType.ERROR);
//                                        sqlError.setTitle("Error when Loading from Cache");
                                        sqlError.setTitle(model.getTranslation("dbError"));
                                        sqlError.showAndWait();
                                        return;
                                    }else{
                                        model.add(loaded);
                                    }


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




    /**
     * return the Stage
     * @return stage to display
     */

    public Stage getStage(){

        return this.stage;

    }

}
