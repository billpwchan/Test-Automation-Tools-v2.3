package main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import DB.User;
import DBcontroller.sessionFactorySingleton;
import controller.TATFrameController;
import controller.util.CommonFunctions;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.ImageCursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Classe;
import model.HMI;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author tmartinez
 */
public class Main extends Application {

    /**
     *
     */
    public static DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    /**
     *
     */
    public static Scene scene;

    /**
     *
     */
    public static Stage primaryStage;

    /**
     *
     */
    public TATFrameController tatFrameController;

    /**
     *
     */
    public static String pathForFunctional;

    /**
     *
     */
    public static boolean isSet = false;

    /**
     *
     */
    public static User currentUser = null;

    /**
     *
     */
    public boolean tryAgain = true;

    /**
     *
     */
    public static ObservableList<Classe> classFound = FXCollections.observableArrayList();

    /**
     *
     */
    public static ArrayList<HMI> HMIs = new ArrayList<>();

    ;//=  
    

    @Override
    public void start(Stage primaryStage) {
        boolean askUser = false;
        if (!askUser) {
            User tom = new User();
            tom.setName("AdminAcc");
            tom.setEmail("AdminAcc@email.com");
            tom.setRight("Admin");
            currentUser = tom;
        }

//        UserDB userHandler = new UserDB();
//        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
//        LoginDialog dlg = new LoginDialog(null, null);
//        while (currentUser == null && tryAgain && askUser) {
//            dlg.showAndWait().ifPresent(result -> {
//                currentUser = userHandler.getUser(result.getKey(), result.getValue());
//            });
//            if (currentUser == null) {
//                Alert dlg2 = new Alert(AlertType.CONFIRMATION, "");
//                dlg2.setTitle("Wrong username or password");
//                dlg2.getDialogPane().setContentText("Your username of password is wrong, do you want to try again or quit");
//                dlg2.showAndWait().ifPresent(result -> {
//                    if (result.getText().equals("Cancel")) {
//                        tryAgain = false;
//                    }
//                });
//            }
//        }

        if (currentUser != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = fxmlLoader.load(getClass().getResource("/assets/view/TATFrame.fxml").openStream());
                tatFrameController = fxmlLoader.getController();
                scene = new Scene(root);
                primaryStage.setScene(scene);
                getMainString(scene);
                primaryStage.show();
                primaryStage.setTitle("Test Automation Tool");
                primaryStage.getIcons().add(new Image("/assets/images/images.png"));

                Main.primaryStage = primaryStage;
                tatFrameController.setPrimaryStage(Main.primaryStage);
                primaryStage.setOnCloseRequest((WindowEvent event) -> {
                    SessionFactory factory = sessionFactorySingleton.getInstance();

                    factory.close();
                    //System.exit(0);
                    //executor.shutdown();
                });
                //SessionFactory fac = sessionFactorySingleton.getInstance();

                //fac.close();
//            Main.primaryStage.setX(primaryScreenBounds.getMinX());
//            Main.primaryStage.setY(primaryScreenBounds.getMinY());
//            Main.primaryStage.setWidth(primaryScreenBounds.getWidth());
//            Main.primaryStage.setHeight(primaryScreenBounds.getHeight());
            } catch (IOException ex) {
                CommonFunctions.debugLog.error("Exception Caught: ", ex);
            }
        }
        //set Stage boundaries to visible bounds of the main screen
        //displayFunctionalPath();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     *
     * @return
     */
    public Stage getPrimaryStage() {
        return Main.primaryStage;
    }

    /**
     *
     * @param scene
     */
    public void getMainString(Scene scene) {
        Image image = new Image("/assets/images/valid.png");  //pass in the image path
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_ANY, KeyCombination.CONTROL_ANY), (Runnable) () -> {
            //condition here of you that want you want to achive.
            scene.setCursor(new ImageCursor(image));
            isSet = true;
        });
    }

}
