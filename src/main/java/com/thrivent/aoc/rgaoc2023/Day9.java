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
public class Day9 {


    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd9.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        Long accumulator = 0L;
        for (int i = 0; i < stringList.size(); i++) {
            accumulator+= nextNumber(Arrays.stream(stringList.get(i).split( " ")).map(Integer::parseInt).toList());
        }
        log.info("{}",accumulator);

        Long accumulator2 = 0L;
        for (int i = 0; i < stringList.size(); i++) {
            accumulator2+= prevNumber(Arrays.stream(stringList.get(i).split( " ")).map(Integer::parseInt).toList());
        }
        log.info("{}",accumulator2);
    }

    public static int nextNumber(List<Integer> thisStep){
        List<Integer> nextStep = new ArrayList<>();
        int zeroCount = 0;
        for (int i = 0; i < thisStep.size()-1; i++) {
            Integer diff = thisStep.get(i+1)-thisStep.get(i);
            if(diff ==0) zeroCount++;
            nextStep.add(diff);
        }
        if (zeroCount == thisStep.size()-1) {
            log.debug("next number for zero set {} is {}", nextStep, 0);
            return thisStep.get(thisStep.size()-1);
        } else {
            int nextNum = nextNumber(nextStep);
            log.debug("next number for set {} is {}", nextStep, nextNum);
            nextStep.add(nextNum);
        }
        int result = thisStep.get(thisStep.size()-1) + nextStep.get(nextStep.size()-1);

        return result;
    }

    public static int prevNumber(List<Integer> thisStep){
        List<Integer> nextStep = new ArrayList<>();
        int zeroCount = 0;
        for (int i = 0; i < thisStep.size()-1; i++) {
            Integer diff = thisStep.get(i+1)-thisStep.get(i);
            if(diff ==0) zeroCount++;
            nextStep.add(diff);
        }
        if (zeroCount == thisStep.size()-1) {
            log.debug("prev number for zero set {} is {}", nextStep, 0);
            return thisStep.get(0);
        } else {
            int nextNum = prevNumber(nextStep);
            log.debug("prev number for set {} is {}", nextStep, nextNum);
            nextStep.add(0,nextNum);
        }
        int result = thisStep.get(0) - nextStep.get(0);

        return result;
    }
}
