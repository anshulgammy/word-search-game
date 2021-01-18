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
    private DifficultyLevelEnum difficultyLevel;
    private List<DirectionsEnum> directions = new ArrayList<>();
    private List<String> wordsList;
    private List<Coordinate> coordinatesList = new ArrayList<>();

    @Override
    public void generateGrid(int gridSize, DifficultyLevelEnum difficultyLevel, List<String> wordsList) {
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
                directions.addAll(Arrays.asList(DirectionsEnum.HORIZONTAL,
                        DirectionsEnum.VERTICAL));
                break;
            case MEDIUM:
                directions.addAll(Arrays.asList(DirectionsEnum.DIAGONAL,
                        DirectionsEnum.VERTICAL, DirectionsEnum.HORIZONTAL));
                break;
            /*case EXPERT:
                directions = Arrays.asList(DirectionsEnum.HORIZONTAL,
                        DirectionsEnum.VERTICAL, DirectionsEnum.DIAGONAL,
                        DirectionsEnum.REVERSE_DIAGONAL,
                        DirectionsEnum.REVERSE_HORIZONTAL, DirectionsEnum.REVERSE_VERTICAL);
                break;*/
        }
    }

    private void fillGridWithWords() {
        for (String word : wordsList) {
            for (DirectionsEnum direction : directions) {
                boolean isWordFilled = false;
                for (Coordinate coordinate : coordinatesList) {
                    int x = coordinate.getX();
                    int y = coordinate.getY();
                    switch (direction) {
                        case HORIZONTAL:
                            if(!checkIfWordFits(x, y, direction, word)) continue;
                            else {
                                for(int i = 0; i<word.length(); i++) {
                                    y++;
                                    gridContent[x][y] = word.charAt(i);
                                }
                                isWordFilled = true;
                            }
                            break;
                        case VERTICAL:
                            if(!checkIfWordFits(x, y, direction, word)) continue;
                            else {
                                for(int i = 0; i<word.length(); i++) {
                                    x++;
                                    gridContent[x][y] = word.charAt(i);
                                }
                                isWordFilled = true;
                            }
                            break;
                        case DIAGONAL:
                            if(!checkIfWordFits(x, y, direction, word)) continue;
                            else {
                                for(int i = 0; i<word.length(); i++) {
                                    x++;
                                    y++;
                                    gridContent[x][y] = word.charAt(i);
                                }
                                isWordFilled = true;
                            }
                            break;
                        /*case REVERSE_HORIZONTAL:
                            break;
                        case REVERSE_VERTICAL:
                            break;
                        case REVERSE_DIAGONAL:
                            break;*/
                    }
                    if(isWordFilled) {
                        break;
                    }
                }
            }
        }
    }

    private boolean checkIfWordFits(int x, int y, DirectionsEnum direction, String word) {
        switch (direction) {
            case HORIZONTAL:
                if (y + word.length() >= gridSize) {
                    return false;
                }
                for (int i = y + 1; i < word.length(); i++) {
                    if (gridContent[x][i] != '_') {
                        return false;
                    }
                }
                break;
            case VERTICAL:
                if (x + word.length() >= gridSize) {
                    return false;
                }
                for (int i = x + 1; i < word.length(); i++) {
                    if (gridContent[i][y] != '_') {
                        return false;
                    }
                }
                break;
            case DIAGONAL:
                if (x + word.length() >= gridSize) {
                    return false;
                } else if (y + word.length() >= gridSize) {
                    return false;
                }
                for (int i = 0; i < word.length(); i++) {
                    x++;
                    y++;
                    if (gridContent[x][y] != '_') {
                        return false;
                    }
                }
                break;
            /*case REVERSE_HORIZONTAL:
                break;
            case REVERSE_VERTICAL:
                break;
            case REVERSE_DIAGONAL:
                break;*/
        }
        return true;
    }

    private void initializeGridCoordinates(List<Coordinate> coordinatesList) {
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
