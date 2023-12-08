package com.thrivent.aoc.rgaoc2023;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Day8 {

    static Map<String, Exits> maps = new HashMap<>();
    static ArrayList<String> locations = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd8.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);

        String path = stringList.get(0).trim();
        parseMaps(stringList);

        log.info(maps.toString());

        String location = "AAA";
        int steps = 0;
        while (!location.equals("ZZZ")) {
            char direction = path.charAt(steps % path.length());
            location = takeStep(location, direction);
            steps++;
        }
        log.info("part1: {}", steps);

        long stepsP2 = 0;

        for (int i = 0; i < locations.size(); i++) {
            steps = 0;
            location = locations.get(i);
            while (location.charAt(2) != 'Z') {
                char direction = path.charAt(steps % path.length());
                location = takeStep(location, direction);
                steps++;
            }
            log.info("a path: {}", steps);

            int startLoop2 = steps;

            char direction = path.charAt(steps % path.length());
            location = takeStep(location, direction);
            steps++;
            while (location.charAt(2) != 'Z') {
                direction = path.charAt(steps % path.length());
                location = takeStep(location, direction);
                steps++;
            }
            log.info("again : {}", steps-startLoop2);
        }
    }

    private static String takeStep(String location, char direction) {
        switch (direction) {
            case 'L' -> {
                location = maps.get(location).left;
            }
            case 'R' -> {
                location = maps.get(location).right;
            }
        }
        return location;
    }
    

    record Exits(String left, String right) {
    }

    static void parseMaps(List<String> input) {
        for (int i = 2; i < input.size(); i++) {
            String node = input.get(i).split(" = ")[0];
            if (node.charAt(2) == 'A') {
                locations.add(node);
            }
            String[] exits = input.get(i).split("\\(")[1].split("\\)")[0].split(", ");
            maps.put(node, new Exits(exits[0], exits[1]));
        }

    }


}
