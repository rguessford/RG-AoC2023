package com.thrivent.aoc.rgaoc2023;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class Day11 {

    record Galaxy(int x, int y) {
    }

    static List<Integer> xExpansions = new ArrayList<>();
    static List<Integer> yExpansions = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd11.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        List<String> universe = expandSpace(stringList);
        List<Galaxy> galaxies = new ArrayList<>();
        for (int i = 0; i < universe.size(); i++) {
            findGalaxies(galaxies, universe.get(i), i);
        }
        long accumulator = 0;
        int expansionFactor = 1000000;
        for (int i = 0; i < galaxies.size()-1; i++) {
            for (int j = i+1; j < galaxies.size(); j++) {
                Galaxy one = galaxies.get(i);
                Galaxy two = galaxies.get(j);
                Long xDistance = (long) Math.abs((two.x - one.x));
                for (Integer x :
                        xExpansions) {
                    if(x > one.x && x < two.x || x < one.x && x > two.x){
                        xDistance -=1;
                        xDistance +=expansionFactor;
                    }
                }
                Long yDistance = (long) Math.abs((two.y- one.y));
                for (Integer y :
                        yExpansions) {
                    if(y > one.y && y < two.y || y < one.y && y > two.y){
                        yDistance -= 1;
                        yDistance += expansionFactor;
                    }
                }
                accumulator += xDistance+yDistance;
            }
        }
        log.info("part1: {}", accumulator);
    }

    public static void findGalaxies(List<Galaxy> galaxyList, String line, int lineIndex) {
        Pattern number = Pattern.compile("#");
        Matcher m = number.matcher(line);
        while (m.find()) {
            Galaxy galaxy = new Galaxy(m.start(0), lineIndex);
            log.debug("{}", galaxy);
            galaxyList.add(galaxy);
        }
    }

    public static List<String> expandSpace(List<String> map) {
        for (int i = 0; i < map.size(); i++) {
            if(map.get(i).contains("#")){
                continue;
            } else {
                yExpansions.add(i);
            }
        }

        for (int i = 0; i < map.get(1).length(); i++) {
            boolean hasGalaxy = false;
            for (int j = 0; j < map.size(); j++) {
                if(map.get(j).charAt(i)=='#'){
                    hasGalaxy = true;
                    break;
                }
            }
            if(!hasGalaxy){
                xExpansions.add(i);
            }
        }
        return map;
    }
}
