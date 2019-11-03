package itba.client;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class Writer {

    private PrintWriter writer;

    public Writer(final String path) {
        try {
            this.writer = new PrintWriter(path, "UTF-8");
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void writeString(final String string) {
        writer.print(string);
        writer.flush();
    }
}

