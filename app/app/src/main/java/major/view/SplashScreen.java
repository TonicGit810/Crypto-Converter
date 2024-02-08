package major.view;

import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Set;

public class SplashScreen {
    private Image splash;
    private ImageView view;
    private ProgressBar progressBar;

    private Scene scene;

    private Stage stage;
    private double progress = 0.0;

    public SplashScreen(){

        this.splash = new Image("splash.jpg");
        this.view = new ImageView(splash);
        this.progressBar = new ProgressBar(progress);
        this.stage = new Stage();
        Setup();



    }


    private void Setup(){
        view.setFitHeight(380);
        view.setFitWidth(400);

        progressBar.setPrefWidth(400);

        VBox box = new VBox();
        box.getChildren().addAll(view,progressBar);
        Scene scene = new Scene(box,400,400);
        stage.setScene(scene);

        stage.initStyle(StageStyle.TRANSPARENT);






    }


    public void increase(int a){
        progress += 0.066;

        if(progress>=0.99){
            progress = 1;
        }


        System.out.println(progress);
        progressBar.setProgress(progress);

    }

    public Stage getStage(){
        return this.stage;
    }
}
