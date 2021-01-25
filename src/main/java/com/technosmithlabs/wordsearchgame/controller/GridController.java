package com.technosmithlabs.wordsearchgame.controller;

import com.technosmithlabs.wordsearchgame.model.DifficultyLevelEnum;
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
        DifficultyLevelEnum difficultyLevel = DifficultyLevelEnum.EXPERT;
        List<String> wordsList = Arrays.asList("ONE", "TWO", "THREE", "FOUR", "FIVE");
        gridGenerationService.generateGrid(gridSize, difficultyLevel, wordsList);
    }

}
