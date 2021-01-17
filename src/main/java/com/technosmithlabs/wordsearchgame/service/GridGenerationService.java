package com.technosmithlabs.wordsearchgame.service;

import com.technosmithlabs.wordsearchgame.model.GameDifficultyLevel;

import java.util.List;

public interface GridGenerationService {
    public void generateGrid(int gridSize, GameDifficultyLevel difficultyLevel, List<String> wordsList);
}
