package com.technosmithlabs.wordsearchgame.controller;

import com.technosmithlabs.wordsearchgame.model.GameDifficultyLevel;
import com.technosmithlabs.wordsearchgame.service.GridGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;

@Controller
public class GridController {

    @Autowired
    private GridGenerationService gridGenerationService;

    public void generateGrid() {
        int gridSize = 10;
        GameDifficultyLevel difficultyLevel = GameDifficultyLevel.MEDIUM;
        List<String> wordsList = Arrays.asList("ONE", "TWO", "THREE", "FOUR");
        gridGenerationService.generateGrid(gridSize, difficultyLevel, wordsList);
    }

}
