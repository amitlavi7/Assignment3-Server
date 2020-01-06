package bgu.spl.net.impl.stomp;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class JoinGenreReadingClubCommand implements Command<String> {

    private String Genere;

    public JoinGenreReadingClubCommand(String genere) {
        Genere = genere;
    }

    public String getGenere() {
        return Genere;
    }

    @Override
    public Serializable execute(String arg) {
        return null;
    }
}
