package model;

import Data.Book;


public final class SharedBookData {

    private static Book selectedBook = null;

    private SharedBookData() {
    }


    public static void setSelectedBook(Book book) {
        selectedBook = book;
    }


    public static Book getSelectedBook() {
        return selectedBook;
    }
}