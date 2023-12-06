package com.thrivent.aoc.rgaoc2023;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class Day6 {

    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd6.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        List<Integer> times = Arrays.stream(
                        Arrays.copyOfRange(stringList.get(0).split("\\s+"), 1, 5))
                .map(Integer::parseInt)
                .toList();
        List<Integer> distances = Arrays.stream(
                        Arrays.copyOfRange(stringList.get(1).split(("\\s+")), 1, 5))
                .map(Integer::parseInt)
                .toList();

        Long time2 = Long.parseLong(Arrays.stream(
                        Arrays.copyOfRange(stringList.get(0).split("\\s+"), 1, 5))
                .reduce("", (a, b) -> String.join("", a, b)));

        Long distance2 = Long.parseLong(Arrays.stream(
                        Arrays.copyOfRange(stringList.get(1).split("\\s+"), 1, 5))
                .reduce("", (a, b) -> String.join("", a, b)));

        List<Integer> waysToWin = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            int counter = 0;
            for (int j = 0; j < times.get(i); j++) {
                if (j * (times.get(i) - j) > distances.get(i)) {
                    counter++;
                }
            }
            waysToWin.add(counter);
        }

        log.info("part1: {}", waysToWin.stream().reduce(1, (a, b) -> a * b));

        List<Long> waysToWin2 = new ArrayList<>();

        Long interval = 1000000L;
        //find start of winners
        int startWin;
        for (startWin = 0; ; startWin += interval) {
            if (startWin * (time2 - startWin) > distance2) {
                if (interval == 1) break;
                startWin -= interval;
                interval /= 10;
            }
        }
        log.info("{}", startWin);
        Long endWin;
        interval = 1000000L;
        for (endWin = time2; ; endWin -= interval) {
            if (endWin * (time2 - endWin) > distance2) {
                endWin += interval;
                if (interval == 1) break;
                interval /= 10;
            }
        }
        log.info("{}", endWin);
        log.info("part2:{}", endWin - startWin);
    }
}
