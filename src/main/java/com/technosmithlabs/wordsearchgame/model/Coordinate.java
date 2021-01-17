package com.technosmithlabs.wordsearchgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
public class Coordinate {
    private int x;
    private int y;
}
