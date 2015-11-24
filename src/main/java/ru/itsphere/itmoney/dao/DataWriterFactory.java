package ru.itsphere.itmoney.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
@Component
public class DataWriterFactory implements WriterFactory {

    public static final String CHARSET_NAME = "UTF-8";

    @Value("${data.access.file}")
    private String FILE_NAME;

    @Override
    public PrintWriter getPrintWriter(boolean append) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME, append);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, CHARSET_NAME);
        return new PrintWriter(outputStreamWriter);
    }
}
