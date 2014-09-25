package com.tika.jobs;

import org.apache.tika.io.IOUtils;

import java.io.*;

public class FileHandler {
    private FileInputStream fileInputStream;
    private String theFileAsString;

    public void readFile() throws IOException {
        File file = new File(new File("").getAbsolutePath()+"/src/main/resources/TSVFiles/computrabajo-ar-20121106.tsv");
        fileInputStream = new FileInputStream(file);
    }

    public String outputStringFromFile() throws IOException {
        theFileAsString = IOUtils.toString(fileInputStream);
        return theFileAsString;
    }

    public void printStringFromFile() throws IOException {
        System.out.println(outputStringFromFile());
    }

}
