package ru.itsphere.itmoney.dao;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private FileProperties fileProperties;

    @Override
    public LineNumberReader getLineNumberReader() throws Exception {
        FileInputStream fileInputStream = new FileInputStream(fileProperties.getFileName());
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, fileProperties.getCharset());
        return new LineNumberReader(inputStreamReader);
    }
}
