package major.view;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import major.model.Facade;


/**
 *Class to setup convert page
 */
public class ConvertPane {

    private Stage stage;
    private Facade model;

    private String currencyName;

    public ConvertPane(Facade model, String currencyName){

        this.currencyName = currencyName;
        this.model = model;
        this.stage = new Stage();
        setup();
    }

    /**
     * Setup the page
     */
    private void setup(){
        VBox box = new VBox();



        Text name1 = new Text(String.format(model.getTranslation("Convert From: ")+"%s",currencyName));
        name1.setFont(Font.font(20));




        Text am = new Text(model.getTranslation("Amount: "));
        am.setFont((Font.font(20)));



        Text to = new Text(model.getTranslation("to"));
        to.setFont(Font.font(20));




        Text rate = new Text(model.getTranslation("rate"));
        Text total = new Text(model.getTranslation("result"));

        rate.setFont((Font.font(20)));
        total.setFont((Font.font(20)));


        TextField amount = new TextField();


        amount.setFont(Font.font(20));


        ObservableList<String> new_list = FXCollections.observableArrayList(model.getSelectedNames());



        ComboBox<String> comboBox = new ComboBox<>(new_list);

        Button button = new Button(model.getTranslation("convert"));
        button.setOnAction((ActionEvent event)->{
            convertAction(comboBox,amount,rate,total);
        });




        Button button1 = new Button(model.getTranslation("send"));


        button1.setOnAction((ActionEvent event)->{
            if (model.getSend()){
                if(model.message(model.getLastMessages())){
                    model.setSend(false);

                }else{
                    Alert alert = new Alert(Alert.AlertType.ERROR);

                    alert.setTitle(model.getTranslation("alert"));
                    alert.setContentText(model.getTranslation("messageE"));
                    alert.showAndWait();

                }
                model.setSend(false);

            }else{

                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle(model.getTranslation("alert"));
                alert.setContentText(model.getTranslation("lackData"));           // if not enough data to send the message the alert will show
                alert.showAndWait();


            }

        });


        box.getChildren().addAll(name1,am, amount,to,comboBox,button,rate,total,button1);


        Scene s1 = new Scene(box, 500, 500);

        stage.setTitle(model.getTranslation("convert"));
        stage.setScene(s1);

    }

    /**
     *return the stage
     * @return stage to display
     */
    public Stage getStage(){
        return this.stage;

    }

    /**
     *convert currency
     * @param comboBox the box of the crypto to convert to.
     * @param amount the amount of the converted crypto.
     * @param rate the conversion rate between crypto.
     * @param total total result of the conversion.
     */
    private void convertAction(ComboBox<String> comboBox, TextField amount, Text rate, Text total){


            if(comboBox.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);

                alert.setTitle(model.getTranslation("alert"));
                alert.setContentText(model.getTranslation("empty"));

                alert.showAndWait();
                return;

            }
            String value = (String) comboBox.getValue();



            String convert_id = model.getIdByName(value);
            String id = model.getIdByName(currencyName);



            if(!amount.getText().isEmpty()){

                    String result = model.convert(id,convert_id,amount.getText());



                    if (result == null){



                        rate.setText(model.getTranslation("request"));
                        total.setText(model.getTranslation("null"));


                        this.model.addMessages(id,convert_id,amount.getText(),null);
                        model.setSend(true);


                    }else if(result.equals("Out Range")){


                        Alert alert = new Alert(Alert.AlertType.ERROR);

                        alert.setTitle(model.getTranslation("alert"));// if the number is out range the alert will show.
                        alert.setContentText(model.getTranslation("range"));
                        alert.showAndWait();
                        model.setSend(false);

                        return;

                    }else if(result.equals("Wrong Type")){

                        Alert alert = new Alert(Alert.AlertType.ERROR);

                        alert.setTitle(model.getTranslation("alert")); // if the type of data input is wrong the alert will show.
                        alert.setContentText(model.getTranslation("type"));
                        alert.showAndWait();
                        model.setSend(false);

                    }else{
                        double damount = Double.parseDouble(amount.getText());
                        double dresult = Double.parseDouble(result);
                        double drate = dresult/damount;

                        rate.setText(String.format(model.getTranslation("rate")+" %f",drate));
                        total.setText(String.format(model.getTranslation("result")+" %s",result));
                        this.model.addMessages(id,convert_id,amount.getText(),result);
                        model.setSend(true);


                        this.model.addMessages(id,convert_id,amount.getText(),result);
                        if(!model.checkThreshold(drate)){

                            Alert alert = new Alert(Alert.AlertType.ERROR);

                            alert.setTitle("Alert"); //  if below threshold this alert will show
                            alert.setContentText("The conversion rate is lower than threshold!");
                            alert.showAndWait();
                            rate.setText(String.format(model.getTranslation("rate")+" %f",drate));
                            total.setText(String.format(model.getTranslation("result")+" %s",result));

                        }




                    }



            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(model.getTranslation("alert"));
                alert.setContentText(model.getTranslation("aempty")); //Shows when not amount is provided.
                alert.showAndWait();
                model.setSend(false);


            }



        };







}
