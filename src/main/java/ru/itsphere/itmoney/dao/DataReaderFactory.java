package ru.itsphere.itmoney.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * http://it-channel.ru/
 *
 * @author Budnikov Aleksandr
 */
@Component
public class DataReaderFactory implements ReaderFactory {

    public static final String CHARSET_NAME = "UTF-8";

    @Value("${data.access.file}")
    private String FILE_NAME;

    @Override
    public LineNumberReader getLineNumberReader() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(FILE_NAME);
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, CHARSET_NAME);
        return new LineNumberReader(inputStreamReader);
    }
}
