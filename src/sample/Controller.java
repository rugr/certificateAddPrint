package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class Controller implements Runnable {
    public AnchorPane anchorPane;
    public Pane pane;
    public String pathDirectoryIn = Main.pathDirectoryIn;
    public File listDirectoryIn = new File(pathDirectoryIn);
    public Label textDirectoryIn;
    public Label textFilePrint;
    public int xPosition;
    public int yPosition;
    public TextField printWidth;
    public TextField printHeight;
    public ChoiceBox cbPrintWidth = new ChoiceBox(FXCollections.observableArrayList());
    public ChoiceBox cbPrintHeight = new ChoiceBox(FXCollections.observableArrayList());
    public Slider sliderX = new Slider();
    public Slider sliderY = new Slider();
    public Rectangle printRectangle;
    public Label textDirectoryOut;
    public String pathFilePrint = Main.pathFilePrint;
    public String pathDirectoryOut = Main.pathDirectoryOut;
    public Text textStart = new Text();
    public ProgressBar progressBar = new ProgressBar(0);
    public ProgressIndicator progressIndicator = new ProgressIndicator(0);
    public boolean stop = true;
    Double oldX, oldY;

//    private static Logger log = Logger.getLogger(Controller.class.getName());

    @FXML
    public void initialize() {
        printRectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                oldX = me.getX();
                oldY = me.getY();
            }
        });

        printRectangle.setOnMouseDragged(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                Number i;
                if(printRectangle.getLayoutX() == 0) {
                    printRectangle.setLayoutX(1);
                    sliderX.setValue(1);
                    i = (Number) ((int) (sliderX.getValue()));
                    Main.printX = i.toString();
                }else if ((printRectangle.getLayoutX() + printRectangle.getWidth()) > pane.getWidth()){
                    printRectangle.setLayoutX(sliderX.getMax() - printRectangle.getWidth() - 1);
                    sliderX.setValue(printRectangle.getLayoutX());
                    i = (Number) ((int) (sliderX.getValue()));
                    Main.printX = i.toString();
                }else {
                    printRectangle.setLayoutX(me.getSceneX() - anchorPane.getLayoutX() - pane.getLayoutX() - oldX);
                    sliderX.setValue(printRectangle.getLayoutX());
                    i = (Number) ((int) (sliderX.getValue()));
                    Main.printX = i.toString();
                }


                if(printRectangle.getLayoutY() == 0) {
                    printRectangle.setLayoutY(sliderY.getMax() - 1);
                    sliderY.setValue(sliderY.getMax() - 1);
                    i = (Number) ((int) (sliderY.getValue()));
                    Main.printY = i.toString();
                }else if ((printRectangle.getLayoutY() + printRectangle.getHeight()) > pane.getHeight()){
                    printRectangle.setLayoutY(sliderY.getMin() + printRectangle.getHeight() + 1);
                    sliderY.setValue(printRectangle.getLayoutY());
                    i = (Number) ((int) (sliderY.getValue()));
                    Main.printY = i.toString();
                }else {
                    printRectangle.setLayoutY(me.getSceneY() - anchorPane.getLayoutY() - pane.getLayoutY() - oldY);
                    sliderY.setValue(sliderY.getMax() - printRectangle.getLayoutY());
                    i = (Number) ((int) (sliderY.getMax() - sliderY.getValue()));
                    Main.printY = i.toString();
                }


            }
        });

        printRectangle.setOnMouseEntered(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                printRectangle.setFill(Color.RED);
                printRectangle.setCursor(Cursor.HAND);
            }
        });

        printRectangle.setOnMouseExited(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                System.out.println("Mouse entered");
                printRectangle.setFill(Color.DODGERBLUE);
                printRectangle.setCursor(Cursor.DEFAULT);
            }
        });

        cbPrintWidth.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//                System.out.println(cbPrintWidth.getItems().get((Integer) number2));
                printRectangle.setWidth(number2.doubleValue());
                Main.printWidth = number2.toString();
            }
        });

        cbPrintHeight.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
//                System.out.println(cbPrintHeight.getItems().get((Integer) number2));
                printRectangle.setHeight(number2.doubleValue());
                Main.printHeight = number2.toString();
            }
        });

        String[] printWidthArr = new String[211];
        for (int i = 0; i < printWidthArr.length; i++) {
            printWidthArr[i] = "" + (i);
        }
        cbPrintWidth.getItems().addAll(printWidthArr);

        if(!Main.printWidth.equals("")){
            cbPrintWidth.getSelectionModel().select(Integer.parseInt(Main.printWidth));
        }else {
            cbPrintWidth.getSelectionModel().select(50);
        }
        printRectangle.setWidth(Double.parseDouble(cbPrintWidth.getValue().toString()));

        String[] printHeightArr = new String[298];
        for (int i = 0; i < printHeightArr.length; i++) {
            printHeightArr[i] = "" + (i);
        }
        cbPrintHeight.getItems().addAll(printHeightArr);

        if(!Main.printHeight.equals("")){
            cbPrintHeight.getSelectionModel().select(Integer.parseInt(Main.printHeight));
        }else {
            cbPrintHeight.getSelectionModel().select(50);
        }
        printRectangle.setHeight(Double.parseDouble(cbPrintHeight.getValue().toString()));

        File testFile = new File(Main.pathDirectoryIn);
        if (testFile.isDirectory()) {
            textDirectoryIn.setText(Main.pathDirectoryIn);
        }else {
            textDirectoryIn.setText("");
            Main.pathDirectoryIn = "";
        }

        testFile = new File(Main.pathFilePrint);
        if (testFile.isFile()) {
            textFilePrint.setText(Main.pathFilePrint);
        }else {
            textFilePrint.setText("");
            Main.pathFilePrint = "";
        }

        testFile = new File(Main.pathDirectoryOut);
        if (testFile.isDirectory()) {
            textDirectoryOut.setText(Main.pathDirectoryOut);
        }else {
            textDirectoryOut.setText("");
            Main.pathDirectoryOut = "";
        }

        if(!Main.printX.equals("")) {
            sliderX.setValue(Double.parseDouble(Main.printX));
        }else {
            sliderX.setValue(75);
        }

        if(!Main.printY.equals("")) {
            sliderY.setValue(297 - Double.parseDouble(Main.printY));
        }else {
            sliderY.setValue(75);
        }

        printRectangle.setLayoutX(sliderX.getValue());
        printRectangle.setLayoutY(297 - sliderY.getValue());

        sliderX.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                printRectangle.setLayoutX(sliderX.getValue());
                Number i = (Number) ((int) (sliderX.getValue()));
                Main.printX = i.toString();
            }
        });
        sliderY.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                                Number old_val, Number new_val) {
                printRectangle.setLayoutY(297 - sliderY.getValue());
                Number i = (Number) ((int) (297 - sliderY.getValue()));
                Main.printY = i.toString();
            }
        });
    }

    public void setProgressBar(double progress) {
        this.progressBar.setProgress(progress);
    }

    public void setProgressIndicator(double progress) {
        this.progressIndicator.setProgress(progress);
    }

    public void alertInformation(String textAlert){
        Alert newAlert = new Alert(Alert.AlertType.INFORMATION, textAlert);
        newAlert.setTitle("Повідомлення");
        newAlert.setHeaderText("");
        newAlert.graphicProperty().setValue(null);
        Optional<ButtonType> result = newAlert.showAndWait();
    }

    public BufferedImage resizeImage(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return dimg;
    }

//==== set print width/height ====
    public void setPrintWidth(){
        printRectangle.setWidth(Double.parseDouble(printWidth.getText()));
//        printRectangle.setWidth(Double.parseDouble(cbPrintWidth.getValue().toString()));
    }

    public void setPrintHeight(){
        printRectangle.setHeight(Double.parseDouble(printHeight.getText()));
    }

//===== choose DirectoryIn ==
    public void addDirectoryIn(){
//        pathDirectoryIn = "";
//        textDirectoryIn.setText("");
//        textStart.setText("");

        DirectoryChooser chooserPathDirectoryIn = new DirectoryChooser();
        File testDirectoryIn = new File(Main.pathDirectoryIn);
        if (testDirectoryIn.isDirectory()) {
            chooserPathDirectoryIn.setInitialDirectory(new File(Main.pathDirectoryIn));
        }
        File selectedDirectoryIn = chooserPathDirectoryIn.showDialog(null);
        if (selectedDirectoryIn != null) {
            pathDirectoryIn = "" + selectedDirectoryIn;
            Main.pathDirectoryIn = pathDirectoryIn;
            textDirectoryIn.setText(pathDirectoryIn);
            listDirectoryIn = selectedDirectoryIn;
        }else{
            alertInformation("ОБЕРІТЬ ПАПКУ СЕРТИФІКАТІВ!!!");
        }
    }

//===== choose File print ==
    public void addFilePrint(){
//        pathFilePrint = "";
//        textFilePrint.setText("");
        textStart.setText("");
        FileChooser chooserPathFile = new FileChooser();
        File testFilePrint = new File(Main.pathFilePrint);
        if (testFilePrint.isFile()) {
            chooserPathFile.setInitialDirectory(new File(new File(Main.pathFilePrint).getParent()));
        }
        File selectedFile = chooserPathFile.showOpenDialog(null);
        if (selectedFile != null) {
            pathFilePrint = "" + selectedFile;
            Main.pathFilePrint = pathFilePrint;
            textFilePrint.setText(pathFilePrint);
        }else{
            alertInformation("ОБЕРІТЬ ФАЙЛ ПЕЧАТІ!!!");
        }
    }

//===== choose DirectoryOut ==
    public void addDirectoryOut(){
//        pathDirectoryOut = "";
//        textDirectoryOut.setText("");
        textStart.setText("");

        DirectoryChooser chooserPathDirectory = new DirectoryChooser();
        File testDirectoryOut = new File(Main.pathDirectoryOut);
        if (testDirectoryOut.isDirectory()) {
            chooserPathDirectory.setInitialDirectory(new File(Main.pathDirectoryOut));
        }
        File selectedFile = chooserPathDirectory.showDialog(null);
        if (selectedFile != null) {
            pathDirectoryOut = "" + selectedFile;
            Main.pathDirectoryOut = pathDirectoryOut;
            textDirectoryOut.setText(pathDirectoryOut);
        }else{
            alertInformation("ОБЕРІТЬ ПАПКУ ДЛЯ ЗБЕРЕЖЕННЯ!!!");
        }
    }

    public void start(){
        if (Main.pathDirectoryIn.equals("") || Main.pathFilePrint.equals("") || Main.pathDirectoryOut.equals("")){
            String alertStr = "";
            if (Main.pathDirectoryIn.equals("")){
                alertStr = alertStr + "Оберіть папку з файлами сертифікатів!";
            }

            if (Main.pathFilePrint.equals("")){
                alertStr = alertStr + "\nОберіть файл печаті!";
            }

            if (Main.pathDirectoryOut.equals("")){
                alertStr = alertStr + "\nОберіть папку для збереження!";
            }

            alertInformation(alertStr);
            return;
        }
        Thread threadAddPrintOnCertificates = new Thread(this);
        threadAddPrintOnCertificates.start();
//        addPrintOnCertificates();
    }

    public void stopAddPrint(){
        stop = true;
    }

    @Override
    public void run() {
        stop = false;
        String mainPrintX = Main.printX;
        String mainPrintY = Main.printY;
        String printWidth = cbPrintWidth.getValue().toString();
        String printHeight = cbPrintHeight.getValue().toString();

        BufferedImage bufferedCertificate = null;

        BufferedImage bufferedPrint = null;

        BufferedImage resultBufferedImage = null;

        File fileCertificate = null;
        File filePrint = null;
        File new1 = null;


        double i = 0;
        double progress = 0;
        setProgressBar(progress);
        setProgressIndicator(progress);
        outer: for (File file : listDirectoryIn.listFiles()) {
//                    System.out.println(file.getPath());
//                    System.out.println(file.getName());
            if (stop == false) {
                i++;
//                System.out.println(i);
                progress = i * 100 / listDirectoryIn.listFiles().length / 100;
//                System.out.println(progress);
                setProgressBar(progress);
                setProgressIndicator(progress);
//                xPosition = 45;
//                yPosition = 85;
                String typeFile = new MimetypesFileTypeMap().getContentType(file);
                if(!typeFile.equals("image/jpeg")){
                    System.out.println(file.getName().substring(file.getName().lastIndexOf(".")+1));
                    continue outer;
                }
                xPosition = Integer.parseInt(mainPrintX) * 100 / 210;
                yPosition = Integer.parseInt(mainPrintY) * 100 / 297;

                fileCertificate = null;
                filePrint = null;
                new1 = null;

                bufferedCertificate = null;
                bufferedPrint = null;

                //                tmp = null;
                //                dimg = null;

                resultBufferedImage = null;

                textStart.setText("");

                fileCertificate = new File(file.getPath());
//                System.out.println("" + fileCertificate);

                try {
                    bufferedCertificate = ImageIO.read(fileCertificate);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

                filePrint = new File(pathFilePrint);

                if(file.isFile()) {
                    try {
                        bufferedPrint = ImageIO.read(filePrint);
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }else {
                    return;
                }

                if(bufferedCertificate.getWidth() > 5000 || bufferedCertificate.getHeight() > 5000){
                    bufferedCertificate = resizeImage(bufferedCertificate, 2500, 3536);
//                    log.info("not print: " + fileCertificate);
//                    System.out.println("resizeImage: " + fileCertificate);
                }
                resultBufferedImage = new BufferedImage(bufferedCertificate.getWidth(), bufferedCertificate.getHeight(), BufferedImage.TYPE_INT_RGB);
                //                BufferedImage resultBufferedImage = new BufferedImage(bufferedCertificate.getWidth(), bufferedCertificate.getHeight(), bufferedCertificate.getType());

//                bufferedPrint = resizeImage(bufferedPrint, bufferedCertificate.getWidth() / 4, bufferedCertificate.getHeight() / 13);
//                bufferedPrint = resizeImage(bufferedPrint, bufferedCertificate.getWidth() / (210 / (Integer.parseInt(cbPrintWidth.getValue().toString()))), bufferedCertificate.getHeight() / (297/(Integer.parseInt(cbPrintHeight.getValue().toString()))));
                bufferedPrint = resizeImage(bufferedPrint, bufferedCertificate.getWidth() / (210 / Integer.parseInt(printWidth)), bufferedCertificate.getHeight() / (297 / (Integer.parseInt(printHeight))));

                xPosition = bufferedCertificate.getWidth() * xPosition / 100;
                yPosition = bufferedCertificate.getHeight() * yPosition / 100;
//                System.out.println("bufferedCertificate.getWidth()/hPosition = " + bufferedCertificate.getWidth() / hPosition + ".  bufferedCertificate.getHeight()/vPosition = " + bufferedCertificate.getHeight() / vPosition);
//                System.out.println("hPosition = " + hPosition + ".  vPosition = " + vPosition);

                resultBufferedImage.createGraphics().drawImage(bufferedCertificate, 0, 0, null);
                resultBufferedImage.createGraphics().drawImage(bufferedPrint, xPosition, yPosition, null);

                //                    new1 = new File("E:/1new/1.jpg");
                new1 = new File(pathDirectoryOut + "/" + file.getName());

                try {
                    //                    ImageIO.write(resultBufferedImage, "jpg", fileCertificate);
                    ImageIO.write(resultBufferedImage, "jpg", new1);
                } catch (IOException e1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "E:/1new/1.jpg ERROR");
                    Optional<ButtonType> result = alert.showAndWait();
                    e1.printStackTrace();
                }
            }else {
                return;
            }
        }

//        String textResult = "Печать: " + filePrint.getPath() +
//                "\nдодано на сертифікати в папці: " + pathDirectoryIn + "\\" +
//                "\nта збережено в папку: " + pathDirectoryOut + "\\";
//
//        textStart.setText(textResult);
    }
}
