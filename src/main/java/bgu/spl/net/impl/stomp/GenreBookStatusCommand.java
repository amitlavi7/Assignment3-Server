package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class GenreBookStatusCommand implements Command<String> {

    private String genre;

    public String getGenre() {
        return genre;
    }

    @Override
    public Serializable execute(String arg) {
        return null;
    }
}
