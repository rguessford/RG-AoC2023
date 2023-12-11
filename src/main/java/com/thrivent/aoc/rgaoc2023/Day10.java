package com.thrivent.aoc.rgaoc2023;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Slf4j
public class Day10 {

    enum Exit {
        UP(0, -1),
        RIGHT(1, 0),
        DOWN(0, 1),
        LEFT(-1, 0);
        public int x, y;

        Exit(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    record Point(int x, int y) {
    }

    record Pipe(Exit one, Exit two) {
    }

    static Set<Point> loop = new HashSet<>(13572);
    static int xPos = 0;
    static int yPos = 0;

    static Map<Character, Pipe> pipes = new HashMap<>();

    static {
        pipes.put('|', new Pipe(Exit.UP, Exit.DOWN));
        pipes.put('-', new Pipe(Exit.LEFT, Exit.RIGHT));
        pipes.put('L', new Pipe(Exit.UP, Exit.RIGHT));
        pipes.put('J', new Pipe(Exit.UP, Exit.LEFT));
        pipes.put('7', new Pipe(Exit.DOWN, Exit.LEFT));
        pipes.put('F', new Pipe(Exit.DOWN, Exit.RIGHT));
        pipes.put('S', new Pipe(Exit.UP, Exit.DOWN));
    }

    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd10.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);

        for (int i = 0; i < stringList.size(); i++) {
            int startX = stringList.get(i).indexOf('S');
            if (startX == -1) {
                continue;
            } else {
                xPos = startX;
                yPos = i;
                break;
            }
        }
        log.debug("{},{}, Pipe: {}", xPos, yPos, stringList.get(yPos).charAt(xPos));
        int nextDirection = 1;
        int stepCounter = 0;

        do {
            loop.add(new Point(xPos, yPos));
            nextDirection = followPipe(stringList, nextDirection);
            stepCounter++;
            log.debug("{},{}, Pipe: {}, steps: {}", xPos, yPos, stringList.get(yPos).charAt(xPos), stepCounter);
        } while (stringList.get(yPos).charAt(xPos) != 'S');
        log.info("loop steps for part 1: {} divide by two for answer", stepCounter);

        int interriorcount = 0;
        for (int i = 0; i < stringList.size(); i++) {
            boolean seenUp = false;
            boolean seenDown = false;
            for (int j = 0; j < stringList.get(i).length(); j++) {
                if (loop.contains(new Point(j, i))) {
                    Pipe thispipe = pipes.get(stringList.get(i).charAt(j));
                    if (thispipe.one.equals(Exit.UP)) {
                        seenUp = !seenUp;
                    }
                    if (thispipe.one.equals(Exit.DOWN)) {
                        seenDown = !seenDown;
                    }
                    if (thispipe.two.equals(Exit.UP)) {
                        seenUp = !seenUp;
                    }
                    if (thispipe.two.equals(Exit.DOWN)) {
                        seenDown = !seenDown;
                    }
                } else {
                    if(seenUp && seenDown){
                        interriorcount++;
                    }
                }
            }
        }
        log.info("part2: {}",interriorcount);
    }

    static int followPipe(List<String> map, int direction) {
        Pipe currentPipe = pipes.get(map.get(yPos).charAt(xPos));
        Integer fromX = xPos;
        Integer fromY = yPos;
        if (direction == 1) {
            xPos += currentPipe.one.x;
            yPos += currentPipe.one.y;
        } else if (direction == 2) {
            xPos += currentPipe.two.x;
            yPos += currentPipe.two.y;
        }

        Pipe nextPipe = pipes.get(map.get(yPos).charAt(xPos));
        if (xPos + nextPipe.one.x == fromX && yPos + nextPipe.one.y == fromY) {
            return 2;
        } else return 1;
    }

}
