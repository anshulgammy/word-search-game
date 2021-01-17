package com.technosmithlabs.wordsearchgame;

import com.technosmithlabs.wordsearchgame.controller.GridController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WordSearchGameApplication {

    private static GridController gridController;

    @Autowired
    public void setGridController(GridController gridController) {
        synchronized (WordSearchGameApplication.class) {
            if (WordSearchGameApplication.gridController == null) {
                WordSearchGameApplication.gridController = gridController;
            }
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(WordSearchGameApplication.class, args);
        gridController.generateGrid();
    }

}
