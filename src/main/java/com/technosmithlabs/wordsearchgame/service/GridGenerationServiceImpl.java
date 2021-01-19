package com.technosmithlabs.wordsearchgame.service;

import com.technosmithlabs.wordsearchgame.model.Coordinate;
import com.technosmithlabs.wordsearchgame.model.DifficultyLevelEnum;
import com.technosmithlabs.wordsearchgame.model.DirectionsEnum;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GridGenerationServiceImpl implements GridGenerationService {

    private char[][] gridContent;
    private int gridSize;
    private DifficultyLevelEnum difficultyLevel;
    private static final List<DirectionsEnum> directions = new ArrayList<>();
    private List<String> wordsList;
    private static final List<Coordinate> coordinatesList = new ArrayList<>();
    private static final String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public void generateGrid(int gridSize, DifficultyLevelEnum difficultyLevel, List<String> wordsList) {
        this.gridContent = new char[gridSize][gridSize];
        this.gridSize = gridSize;
        this.difficultyLevel = difficultyLevel;
        this.wordsList = wordsList;
        initializeEmptyGrid(this.gridContent);
        // initializing directions with respect to difficulty level.
        initializeDirectionsWrtDifficulty();
        // initializing all the possible grid coordinates in the list.
        initializeGridCoordinates();
        // filling grid with provided words in random order.
        fillGridWithWords();
        // filling empty spaces with random albhapets.
        fillGridWithRandomLetters();
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
            case EXPERT:
                directions.addAll(Arrays.asList(DirectionsEnum.HORIZONTAL,
                        DirectionsEnum.VERTICAL, DirectionsEnum.DIAGONAL,
                        DirectionsEnum.REVERSE_DIAGONAL,
                        DirectionsEnum.REVERSE_HORIZONTAL,
                        DirectionsEnum.REVERSE_VERTICAL));
                break;
        }
    }

    private void fillGridWithWords() {
        for (String word : wordsList) {
            boolean isWordFilled = false;
            // shuffling the grid coordinates so that every time a unique order is obtained when we start processing the grid.
            Collections.shuffle(coordinatesList);
            for (Coordinate coordinate : coordinatesList) {
                int x = coordinate.getX();
                int y = coordinate.getY();
                final DirectionsEnum correctDirection = getDirectionToFitWord(x, y,
                        directions, word);
                if (correctDirection != null) {
                    switch (correctDirection) {
                        case HORIZONTAL:
                            for (int i = 0; i < word.length(); i++) {
                                y++;
                                gridContent[x][y] = word.charAt(i);
                            }
                            isWordFilled = true;
                            break;
                        case VERTICAL:
                            for (int i = 0; i < word.length(); i++) {
                                x++;
                                gridContent[x][y] = word.charAt(i);
                            }
                            isWordFilled = true;
                            break;
                        case DIAGONAL:
                            for (int i = 0; i < word.length(); i++) {
                                x++;
                                y++;
                                gridContent[x][y] = word.charAt(i);
                            }
                            isWordFilled = true;
                            break;
                        case REVERSE_HORIZONTAL:
                            for (int i = 0; i < word.length(); i++) {
                                y--;
                                gridContent[x][y] = word.charAt(i);
                            }
                            isWordFilled = true;
                            break;
                        case REVERSE_VERTICAL:
                            for (int i = 0; i < word.length(); i++) {
                                x--;
                                gridContent[x][y] = word.charAt(i);
                            }
                            isWordFilled = true;
                            break;
                        case REVERSE_DIAGONAL:
                            for (int i = 0; i < word.length(); i++) {
                                x--;
                                y--;
                                gridContent[x][y] = word.charAt(i);
                            }
                            isWordFilled = true;
                            break;
                    }
                } else {
                    continue;
                }
                if (isWordFilled) {
                    break;
                }
            }
        }
    }

    private DirectionsEnum getDirectionToFitWord(int x, int y, List<DirectionsEnum> directions,
                                                 String word) {
        // shuffling the directions for the selected difficulty level.
        Collections.shuffle(directions);
        DirectionsEnum correctDirection = null;
        for (DirectionsEnum direction : directions) {
            boolean wasCurrentIterationValid = true;
            int limitY = y + 1 + word.length();
            int limitX = x + 1 + word.length();
            int reverseLimitY = y - 1 - word.length();
            int reverseLimitX = x - 1 - word.length();
            switch (direction) {
                case HORIZONTAL:
                    if (limitY >= gridSize) {
                        wasCurrentIterationValid = false;
                        break;
                    }
                    for (int i = y + 1; i < limitY; i++) {
                        if (gridContent[x][i] != '_') {
                            wasCurrentIterationValid = false;
                            break;
                        }
                    }
                    if (wasCurrentIterationValid) {
                        correctDirection = DirectionsEnum.HORIZONTAL;
                    }
                    break;
                case VERTICAL:
                    if (limitX >= gridSize) {
                        wasCurrentIterationValid = false;
                        break;
                    }
                    for (int i = x + 1; i < limitX; i++) {
                        if (gridContent[i][y] != '_') {
                            wasCurrentIterationValid = false;
                            break;
                        }
                    }
                    if (wasCurrentIterationValid) {
                        correctDirection = DirectionsEnum.VERTICAL;
                    }
                    break;
                case DIAGONAL:
                    if (limitX >= gridSize) {
                        wasCurrentIterationValid = false;
                        break;
                    } else if (limitY >= gridSize) {
                        wasCurrentIterationValid = false;
                        break;
                    }
                    for (int i = 0; i < word.length(); i++) {
                        x++;
                        y++;
                        if (gridContent[x][y] != '_') {
                            wasCurrentIterationValid = false;
                            break;
                        }
                    }
                    if (wasCurrentIterationValid) {
                        correctDirection = DirectionsEnum.DIAGONAL;
                    }
                    break;
                case REVERSE_HORIZONTAL:
                    if (reverseLimitY < 0) {
                        wasCurrentIterationValid = false;
                        break;
                    }
                    for (int i = y - 1; i >= reverseLimitY; i--) {
                        if (gridContent[x][i] != '_') {
                            wasCurrentIterationValid = false;
                            break;
                        }
                    }
                    if (wasCurrentIterationValid) {
                        correctDirection = DirectionsEnum.REVERSE_HORIZONTAL;
                    }
                    break;
                case REVERSE_VERTICAL:
                    if (reverseLimitX < 0) {
                        wasCurrentIterationValid = false;
                        break;
                    }
                    for (int i = x - 1; i >= reverseLimitX; i--) {
                        if (gridContent[i][y] != '_') {
                            wasCurrentIterationValid = false;
                            break;
                        }
                    }
                    if (wasCurrentIterationValid) {
                        correctDirection = DirectionsEnum.REVERSE_VERTICAL;
                    }
                    break;
                case REVERSE_DIAGONAL:
                    if (reverseLimitX < 0) {
                        wasCurrentIterationValid = false;
                        break;
                    } else if (reverseLimitY < 0) {
                        wasCurrentIterationValid = false;
                        break;
                    }
                    for (int i = 0; i < word.length(); i++) {
                        x--;
                        y--;
                        if (gridContent[x][y] != '_') {
                            wasCurrentIterationValid = false;
                            break;
                        }
                    }
                    if (wasCurrentIterationValid) {
                        correctDirection = DirectionsEnum.REVERSE_DIAGONAL;
                    }
                    break;
            }
            if (correctDirection != null) {
                break;
            } else {
                continue;
            }
        }
        return correctDirection;
    }

    private void initializeGridCoordinates() {
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

    private void fillGridWithRandomLetters() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (gridContent[i][j] == '_') {
                    int randomIndex = ThreadLocalRandom.current().nextInt(0,
                            ALPHABETS.length());
                    gridContent[i][j] = ALPHABETS.charAt(randomIndex);
                }
            }
        }
    }
}
