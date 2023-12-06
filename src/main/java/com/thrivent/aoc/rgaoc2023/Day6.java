package com.thrivent.aoc.rgaoc2023;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Day6 {

    static String[] schematic;

    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd6.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        List<Integer> times = Arrays.stream(
                        Arrays.copyOfRange(stringList.get(0).split("\\s+"), 1, 5))
                .map(Integer::parseInt)
                .toList();
        List<Integer> distances = Arrays.stream(
                        Arrays.copyOfRange(stringList.get(1).split(("\\s")), 1, 5))
                .map(Integer::parseInt)
                .toList();


        for (int i = 0; i < times.size(); i++) {
            int counter = 0;
            for (int j = 0; j < times.get(i); j++) {
                if (j * (times.get(i) - j) > distances.get(i)) {

                }
            }


        }
    }
}
