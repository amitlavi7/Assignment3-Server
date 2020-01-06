package bgu.spl.net.impl.stomp.commadsToDel;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class ReturnBookCommand implements Command<String> {

    private String genre;
    private String bookName;

    public ReturnBookCommand(String genre, String bookName) {
        this.genre = genre;
        this.bookName = bookName;
    }

    public String getGenre() {
        return genre;
    }

    public String getBookName() {
        return bookName;
    }

    @Override
    public Serializable execute(String arg) {
        return null;
    }
}
