package hmo.flightroute.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class FileWriter implements Closeable {
    private final static Logger logger = LoggerFactory.getLogger(FileWriter.class.getName());

    private String filename;
    private BufferedWriter writer;

    public void open(String filename, String encoding, boolean append)
            throws FileNotFoundException, UnsupportedEncodingException {
        writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename, append), encoding));
        this.filename = filename;
    }

    public void writeLine(String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }

    @Override
    public void close() throws IOException {
        try {
            if (writer!=null)
                writer.close();
        } catch (IOException e) {
            logger.warn("Error closing file [{}].", filename, e);
        }
    }
}
