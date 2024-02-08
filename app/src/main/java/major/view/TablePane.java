package major.view;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import major.model.Facade;
import major.model.Currency;

/**
 *Class to setup page that display all the selected crypto
 */
public class TablePane extends Application {

    private TableView tableView;

    private StackPane pane;



    private Facade model;





    public TablePane(Facade model){
        this.tableView = new TableView();

        this.pane = new StackPane(tableView);

        this.model = model;


        setUp();



    }
    /**
     *Set up page
     */

    private void setUp(){




        TableColumn<Currency, String> logo = new TableColumn<Currency,String>(model.getTranslation("logo"));
        TableColumn<Currency,String> name = new TableColumn<Currency,String>(model.getTranslation("name"));
        TableColumn<Currency,String> symbol = new TableColumn<Currency,String>(model.getTranslation("symbol"));
        TableColumn<Currency,String> description = new TableColumn<Currency,String>(model.getTranslation("description"));
        TableColumn<Currency,String> date = new TableColumn<Currency,String>(model.getTranslation("date"));
        TableColumn<Currency,String> website = new TableColumn<Currency,String>(model.getTranslation("website"));
        TableColumn<Currency,Void> convert = new TableColumn<Currency,Void>(model.getTranslation("convert"));
        TableColumn<Currency,Void> remove = new TableColumn<Currency,Void>(model.getTranslation("remove"));



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

//                    private final Button btn = new Button("Convert");
                    private final Button btn = new Button(model.getTranslation("convert"));


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

                    private final Button btn = new Button(model.getTranslation("remove"));

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

                    Hyperlink link = new Hyperlink(); // Create the link.


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


    /**
     * Opend the convert page.
     */


    private void convertAction(String name){
         ConvertPane convertPane = new ConvertPane(model,name);

         Stage convert = convertPane.getStage();
         convert.show();


    }

    /**
     * update the selected list.
     */

    public void update(){

        ObservableList<Currency> cur_list = FXCollections.observableArrayList( model.getSelectedList());



        tableView.setItems(cur_list);

    }

    /**
     * Add crypto to the list.
     */


    public void add(Currency currency){
        model.add(currency);


    }

    /**
     * remove crypto from the list.
     */


    public void remove(Currency currency){

        model.remove(currency);
        update();

    }


    /**
     *return the pane to display
     * @return pant to display.
     */

    public StackPane getPane(){
        return this.pane;
    }

    /**
     * run as app for create HyperLink.
     */

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
