package ru.itsphere.itmoney.dao;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FileProperties fileProperties;

    @Override
    public PrintWriter getPrintWriter(boolean append) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(fileProperties.getFileName(), append);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, fileProperties.getCharset());
        return new PrintWriter(outputStreamWriter);
    }
}
