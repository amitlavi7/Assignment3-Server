package bgu.spl.net.impl.stomp.commadsToDel;

import bgu.spl.net.impl.rci.Command;

import java.io.Serializable;

public class ExitGenreReadingClubCommand implements Command<String> {

    private String Genere;

    public ExitGenreReadingClubCommand(String genere) {
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
