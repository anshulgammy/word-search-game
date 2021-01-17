package com.technosmithlabs.wordsearchgame.service;

import com.technosmithlabs.wordsearchgame.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class GridGenerationServiceImpl implements GridGenerationService {

    private char[][] gridContent;
    private int gridSize;
    private GameDifficultyLevel difficultyLevel;
    private List directions;
    private List<String> wordsList;
    private List<Coordinate> coordinatesList;

    @Override
    public void generateGrid(int gridSize, GameDifficultyLevel difficultyLevel, List<String> wordsList) {
        this.gridContent = new char[gridSize][gridSize];
        this.gridSize = gridSize;
        this.difficultyLevel = difficultyLevel;
        this.wordsList = wordsList;
        initializeEmptyGrid(this.gridContent);
        // initializing directions with respect to difficulty level.
        initializeDirectionsWrtDifficulty();
        // shuffling the directions for the selected difficulty level.
        Collections.shuffle(directions);
        // initializing all the possible grid coordinates in the list.
        initializeGridCoordinates(coordinatesList);
        // shuffling the grid coordinates so that every time a unique order is obtained when we start processing the grid.
        Collections.shuffle(coordinatesList);
        // filling grid with provided words in random order.
        fillGridWithWords();
        System.out.println("Generating Grid...");
        printGeneratedGrid();
    }

    private void initializeDirectionsWrtDifficulty() {
        switch (difficultyLevel) {
            case BEGINNER:
                directions = Arrays.asList(DirectionsForBeginner.values());
                break;
            case MEDIUM:
                directions = Arrays.asList(DirectionsForMedium.values());
                break;
            case EXPERT:
                directions = Arrays.asList(DirectionsForExpert.values());
                break;
        }
    }

    private void fillGridWithWords() {
        for(String word : wordsList) {
            for(Enum enumTypes : directions) {
                switch (enumTypes) {
                    case DirectionsForMedium.HORIZONTAL:
                }
            }
            for(Coordinate coordinate : coordinatesList) {

            }
        }

    }

    private void initializeGridCoordinates(List<Coordinate> coordinatesList) {
        coordinatesList = new ArrayList<>();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                final Coordinate coordinate = new Coordinate();
                coordinate.setX(i);
                coordinate.setY(j);
                coordinatesList.add(coordinate);
            }
        }
    }

    private void printGeneratedGrid() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(gridContent[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    private void initializeEmptyGrid(char[][] gridContent) {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridContent[i][j] = '_';
            }
        }
    }
}
