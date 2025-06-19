package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        showLoginView();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void showLoginView() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/view/Login.fxml"));
        setScene(root, "Login - UMM Library Access");
    }

    public static void showAdminView() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/view/admin/MainLayoutAdmin.fxml"));
        setScene(root, "Admin Dashboard - UMM Library");
    }

    public static void showAdminLoginView() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/view/admin/LoginAdmin.fxml"));
        setScene(root, "Admin Login - UMM Library Access");
    }

    public static void showRegistrationView() throws Exception {
        Parent root = FXMLLoader.load(Main.class.getResource("/view/mahasiswa/Register.fxml"));
        setScene(root, "Registrasi Akun");
    }

    private static void setScene(Parent root, String title) {
        primaryStage.setTitle(title);
        primaryStage.setScene(new Scene(root, 1200, 800));

        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}