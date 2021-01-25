package com.technosmithlabs.wordsearchgame.service;

import com.technosmithlabs.wordsearchgame.model.Coordinate;
import com.technosmithlabs.wordsearchgame.model.DifficultyLevelEnum;
import com.technosmithlabs.wordsearchgame.model.Directions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class GridGenerationServiceImpl implements GridGenerationService {

    @Override
    public void generateGrid(int gridSize, DifficultyLevelEnum difficultyLevel, List<String> wordsList) {
        final char[][] gridContent = new char[gridSize][gridSize];
        final List<Coordinate> coordinatesList = new ArrayList<>();
        // initializing empty grid and list of coordinates.
        initializeGrid(gridContent, coordinatesList);
        // get possible directions to work on, with respect to difficulty level.
        final List<Directions> directions =
                getDirectionListByDifficultyLevel(difficultyLevel);
        // filling grid with provided words in random order.
        fillGridWithWords(wordsList, directions, gridContent, coordinatesList);
        // filling empty spaces in the grid with random alphabets.
        fillRemainingGridWithRandomLetters(gridContent);
        printGeneratedGrid(gridContent);
    }

    private List<Directions> getDirectionListByDifficultyLevel(DifficultyLevelEnum difficultyLevel) {
        final List<Directions> directions = new ArrayList<>();
        switch (difficultyLevel) {
            case BEGINNER:
                directions.addAll(Arrays.asList(Directions.HORIZONTAL,
                        Directions.VERTICAL));
                break;
            case MEDIUM:
                directions.addAll(Arrays.asList(Directions.DIAGONAL,
                        Directions.VERTICAL, Directions.HORIZONTAL));
                break;
            case EXPERT:
                directions.addAll(Arrays.asList(Directions.HORIZONTAL,
                        Directions.VERTICAL, Directions.DIAGONAL,
                        Directions.REVERSE_DIAGONAL,
                        Directions.REVERSE_HORIZONTAL,
                        Directions.REVERSE_VERTICAL));
                break;
        }
        return directions;
    }

    private void fillGridWithWords(List<String> wordsList, List<Directions> directionsList,
                                   char[][] gridContent, List<Coordinate> coordinatesList) {
        for (String word : wordsList) {
            // shuffling the grid coordinates so that every time
            // a unique order is obtained when we start processing the grid.
            Collections.shuffle(coordinatesList);
            for (Coordinate coordinate : coordinatesList) {
                final Directions directionValue = getDirection(coordinate,
                        directionsList, word, gridContent);
                if (directionValue != null) {
                    int x = coordinate.getX();
                    int y = coordinate.getY();
                    switch (directionValue) {
                        case HORIZONTAL:
                            for (char c : word.toCharArray()) {
                                gridContent[x][y++] = c;
                            }
                            break;
                        case VERTICAL:
                            for (char c : word.toCharArray()) {
                                gridContent[x++][y] = c;
                            }
                            break;
                        case DIAGONAL:
                            for (char c : word.toCharArray()) {
                                gridContent[x++][y++] = c;
                            }
                            break;
                        case REVERSE_HORIZONTAL:
                            for (char c : word.toCharArray()) {
                                gridContent[x][y--] = c;
                            }
                            break;
                        case REVERSE_VERTICAL:
                            for (char c : word.toCharArray()) {
                                gridContent[x--][y] = c;
                            }
                            break;
                        case REVERSE_DIAGONAL:
                            for (char c : word.toCharArray()) {
                                gridContent[x--][y--] = c;
                            }
                            break;
                    }
                    break;
                }
            }
        }
    }

    private Directions getDirection(Coordinate coordinate, List<Directions> directions,
                                    String word, char[][] gridContent) {
        // shuffling the directions for the selected difficulty level.
        Collections.shuffle(directions);
        for (Directions direction : directions) {
            if (checkIfWordFits(direction, word, gridContent, coordinate)) {
                return direction;
            }
        }
        return null;
    }

    private boolean checkIfWordFits(Directions direction, String word,
                                    char[][] gridContent,
                                    Coordinate coordinate) {
        final int gridSize = gridContent[0].length;
        final int wordLength = word.length();
        switch (direction) {
            case HORIZONTAL:
                if (coordinate.getY() + wordLength > gridSize) {
                    return false;
                }
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter =
                            gridContent[coordinate.getX()][coordinate.getY() + i];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) {
                        return false;
                    }
                }
                break;
            case VERTICAL:
                if (coordinate.getX() + wordLength > gridSize) {
                    return false;
                }
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter =
                            gridContent[coordinate.getX() + i][coordinate.getY()];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) {
                        return false;
                    }
                }
                break;
            case DIAGONAL:
                if ((coordinate.getX() + wordLength > gridSize) || (coordinate.getY() + wordLength > gridSize)) {
                    return false;
                }
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter =
                            gridContent[coordinate.getX() + i][coordinate.getY() + i];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) {
                        return false;
                    }
                }
                break;
            case REVERSE_HORIZONTAL:
                if (coordinate.getY() + 1 - wordLength < 0) {
                    return false;
                }
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter =
                            gridContent[coordinate.getX()][coordinate.getY() - i];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) {
                        return false;
                    }
                }
                break;
            case REVERSE_VERTICAL:
                if (coordinate.getX() + 1 - wordLength < 0) {
                    return false;
                }
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter =
                            gridContent[coordinate.getX() - i][coordinate.getY()];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) {
                        return false;
                    }
                }
                break;
            case REVERSE_DIAGONAL:
                if ((coordinate.getX() + 1 - wordLength < 0) || (coordinate.getY() + 1 - wordLength < 0)) {
                    return false;
                }
                for (int i = 0; i < wordLength; i++) {
                    char gridLetter =
                            gridContent[coordinate.getX() - i][coordinate.getY() - i];
                    if (gridLetter != '_' && gridLetter != word.charAt(i)) {
                        return false;
                    }
                }
                break;
        }
        return true;
    }

    private void setGridCoordinate(int x, int y, List<Coordinate> coordinatesList) {
        final Coordinate coordinate = new Coordinate();
        coordinate.setX(x);
        coordinate.setY(y);
        coordinatesList.add(coordinate);
    }

    private void printGeneratedGrid(char[][] gridContent) {
        final int gridSize = gridContent[0].length;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(gridContent[i][j] + " ");
            }
            System.out.println(" ");
        }
    }

    private void initializeGrid(char[][] gridContent, List<Coordinate> coordinateList) {
        final int gridSize = gridContent[0].length;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridContent[i][j] = '_';
                // Adding this coordinate to Coordinate List.
                setGridCoordinate(i, j, coordinateList);
            }
        }
    }

    private void fillRemainingGridWithRandomLetters(char[][] gridContent) {
        final int gridSize = gridContent[0].length;
        final String alphabetsString = IntStream.rangeClosed('A', 'Z')
                .mapToObj(c -> "" + (char) c).collect(Collectors.joining());
        System.out.println(alphabetsString);
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (gridContent[i][j] == '_') {
                    int randomIndex = ThreadLocalRandom.current().nextInt(0,
                            alphabetsString.length());
                    gridContent[i][j] = alphabetsString.charAt(randomIndex);
                }
            }
        }
    }
}
