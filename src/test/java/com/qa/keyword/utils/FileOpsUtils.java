package com.qa.keyword.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FileOpsUtils {

    public FileInputStream getStreamObjectForFile(String fileName)
            throws FileNotFoundException  {
        ClassLoader clsLoader = getClass().getClassLoader();
        File file = new File(clsLoader.getResource(fileName).getFile());
        String path = file.getAbsolutePath();
        path = URLDecoder.decode(path, StandardCharsets.UTF_8); // remove encoders added to the file path
        FileInputStream fis =new FileInputStream(path);
        return fis;
    }
}
