package com.technosmithlabs.wordsearchgame.service;

import com.technosmithlabs.wordsearchgame.model.DifficultyLevelEnum;

import java.util.List;

public interface GridGenerationService {
    public void generateGrid(int gridSize, DifficultyLevelEnum difficultyLevel, List<String> wordsList);
}
