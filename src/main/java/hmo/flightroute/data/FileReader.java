package hmo.flightroute.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class FileReader implements Closeable {
    private final static Logger logger = LoggerFactory.getLogger(FileReader.class.getName());

    private String filename;
    private BufferedReader reader;

    public FileReader() {
    }

    public void open(String filename, String encoding) throws UnsupportedEncodingException,
            FileNotFoundException {
        this.filename = filename;
        reader = new BufferedReader( new InputStreamReader(
                new FileInputStream(filename), encoding) );
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public void close() {
        try {
            if (reader!=null)
                reader.close();
        } catch (IOException e) {
            logger.warn("Error closing file [{}].", filename, e);
        }
    }
}
