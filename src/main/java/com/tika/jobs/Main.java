package com.tika.jobs;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileHandler fileHandler = new FileHandler();
        fileHandler.readFile();
        fileHandler.printStringFromFile();
    }
}
