package controller.admin;

import Data.Book;
import SQL_DATA.BookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageBooksController {

    @FXML private TableView<Book> tableView;
    @FXML private TableColumn<Book, Integer> colNo;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colAuthor;
    @FXML private TableColumn<Book, String> colCategory;
    @FXML private TableColumn<Book, String> colISBN;
    @FXML private TableColumn<Book, Integer> colQuantity;
    @FXML private TableColumn<Book, String> colStatus;
    @FXML private TableColumn<Book, Void> colActions;
    @FXML private TextField searchField;
    @FXML private Button btnAddBook;

    private final BookDAO bookDAO = new BookDAO();
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadBooks();
        setupSearchFilter();
        btnAddBook.setOnAction(e -> openBookForm(null));
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupTableColumns() {
        colNo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(bookList.indexOf(cellData.getValue()) + 1).asObject());
        setColumnAlignment(colNo, Pos.CENTER);

        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        setColumnAlignment(colTitle, Pos.CENTER_LEFT);

        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        setColumnAlignment(colAuthor, Pos.CENTER_LEFT);

        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        setColumnAlignment(colCategory, Pos.CENTER);

        colISBN.setCellValueFactory(new PropertyValueFactory<>("code"));
        setColumnAlignment(colISBN, Pos.CENTER);

        colQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        setColumnAlignment(colQuantity, Pos.CENTER);

        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        setColumnAlignment(colStatus, Pos.CENTER);

        colActions.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("✏️ Edit");
            private final Button deleteBtn = new Button("🗑️ Hapus");
            private final HBox pane = new HBox(5, editBtn, deleteBtn);

            {
                pane.setAlignment(Pos.CENTER);
                editBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");

                editBtn.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    openBookForm(book);
                });
                deleteBtn.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Anda yakin ingin menghapus buku '" + book.getTitle() + "'?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.YES) {
                            if (bookDAO.deleteBook(book.getCode())) {
                                loadBooks();
                            }
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private <T> void setColumnAlignment(TableColumn<Book, T> column, Pos alignment) {
        column.setCellFactory(col -> new TableCell<Book, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                    setAlignment(alignment);
                }
            }
        });
    }

    private void loadBooks() {
        bookList.setAll(bookDAO.getAllBooks());
        tableView.setItems(bookList);
    }

    private void setupSearchFilter() {
        FilteredList<Book> filteredData = new FilteredList<>(bookList, b -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(book -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (book.getCode().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

        SortedList<Book> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedData);
    }

    private void openBookForm(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/admin/AddBook.fxml"));
            Parent root = loader.load();

            AddBookController controller = loader.getController();
            if (book != null) {
                controller.setBookToEdit(book);
            }

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(book != null ? "Edit Buku" : "Tambah Buku Baru");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadBooks();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}