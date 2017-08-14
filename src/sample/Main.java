package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.prefs.Preferences;

public class Main extends Application {
    public static String pathDirectoryIn = "";
    public static String pathFilePrint = "";
    public static String pathDirectoryOut = "";
    public static String printX = "";
    public static String printY = "";
    public static String printWidth = "";
    public static String printHeight = "";

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println("==== START 'start' ====");
        pathDirectoryIn = getPersonData("pathDirectoryIn");
        System.out.println(" pathDirectoryIn: " + pathDirectoryIn);
        pathFilePrint = getPersonData("pathFilePrint");
        System.out.println("   pathFilePrint: " + pathFilePrint);
        pathDirectoryOut = getPersonData("pathDirectoryOut");
        System.out.println("pathDirectoryOut: " + pathDirectoryOut);
        printWidth = getPersonData("printWidth");
        System.out.println("      printWidth: " + printWidth);
        printHeight = getPersonData("printHeight");
        System.out.println("     printHeight: " + printHeight);
        printX = getPersonData("printX");
        System.out.println("          printX: " + printX);
        printY = getPersonData("printY");
        System.out.println("          printY: " + printY);

        Parent root = FXMLLoader.load(getClass().getResource("sample4.fxml"));
        primaryStage.setTitle("Додати печать на сертифікати");
        primaryStage.setScene(new Scene(root, 637, 380));
        primaryStage.getIcons().add(new Image("file:resources/images/header-logo-min.png"));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        setPersonData("pathDirectoryIn", pathDirectoryIn);
        setPersonData("pathFilePrint", pathFilePrint);
        setPersonData("pathDirectoryOut", pathDirectoryOut);
        setPersonData("printWidth", printWidth);
        setPersonData("printHeight", printHeight);
        setPersonData("printX", printX);
        setPersonData("printY", printY);
        System.out.println("=== STOP ===");
    }

    public String getPersonData(String dataName) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String data = prefs.get(dataName, null);
        if (data != null) {
            return data;
        } else {
            return "";
        }
    }

    public void setPersonData(String dataName, String data) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (data != null) {
            prefs.put(dataName,data);
        } else {
            prefs.remove(dataName);
        }
    }

    public static void main(String[] args) {
        System.out.println("==== START 'main' ====");
        launch(args);
    }
}
