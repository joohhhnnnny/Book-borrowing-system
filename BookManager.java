/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package joption;
import java.util.ArrayList;

/**
 *
 * @author My PC
 */
public class BookManager {
    private static BookManager instance;
    private ArrayList<String> availableBooks;

    private BookManager() {
        availableBooks = new ArrayList<>();
    }

    public static BookManager getInstance() {
        if (instance == null) {
            instance = new BookManager();
        }
        return instance;
    }

    public void addBook(String book) {
        availableBooks.add(book);
    }

    public ArrayList<String> getAvailableBooks() {
        return availableBooks;
    }
}
