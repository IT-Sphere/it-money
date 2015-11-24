package ru.itsphere.itmoney.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by andrey on 24.11.15.
 * In this class we get CHARSET and File Name for
 *DataFactory
 */
@Component
public class FileProperties {

    @Value("${CHARSET_NAME}")
    private String charset;

    @Value("${data.access.file}")
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public String getCharset() {
        return charset;
    }



}
