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
public class Day3 {

    static String[] schematic;

    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd3.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        schematic = stringList.toArray(new String[]{});

        List<PartNumber> partNumberList = new ArrayList<>();
        List<Gear> gearList = new ArrayList<>();
        for(int l=0; l < schematic.length; l++){
            parsePartNumbers(partNumberList, schematic[l], l);
            findGears(gearList,schematic[l],l );

        }

        int resultpart1 = partNumberList.stream()
                .filter(Day3::examinePartNumber)
                .map(partNumber -> partNumber.value)
                .reduce(0, Integer::sum);
        log.info("part1: {}", resultpart1);

        log.info("part2: {}",findGearRatios(gearList,partNumberList).stream().reduce(0,Integer::sum));
    }

    record PartNumber(int row, int beginindex, int width, int value){}

    record Gear(int x, int y){}
    public static void parsePartNumbers(List<PartNumber> partNumberlist, String line, int lineIndex){
        Pattern number = Pattern.compile("\\d+");
        Matcher m = number.matcher(line);
        while (m.find()){
            PartNumber pn = new PartNumber(lineIndex,m.start(0),m.group(0).length(),Integer.parseInt(m.group(0)));
            log.debug("{}",pn);
            partNumberlist.add(pn);
        }
    }
    public static void findGears(List<Gear> gearList, String line, int lineIndex){
        Pattern number = Pattern.compile("\\*");
        Matcher m = number.matcher(line);
        while (m.find()){
            Gear gear = new Gear(m.start(0),lineIndex);
            log.debug("{}",gear);
            gearList.add(gear);
        }
    }


    public static boolean examinePartNumber(PartNumber partNumber){
        log.debug("{}",partNumber.value);
        int leftIndex = Math.max(partNumber.beginindex - 1, 0);
        int rightIndex = partNumber.beginindex + partNumber.width();
        if (rightIndex == schematic[0].length()) rightIndex = rightIndex-1;
        StringBuilder perimeter = new StringBuilder();
        //hat
        if(partNumber.row != 0){
            String hat = schematic[partNumber.row-1].substring(leftIndex,rightIndex+1);
            perimeter.append(hat);
        }
        //left
        if(leftIndex!=partNumber.beginindex+partNumber.width){
            char left = schematic[partNumber.row].charAt(leftIndex);
            perimeter.append(left);
        }
        //right
        if(rightIndex!=partNumber.beginindex){
            char right = schematic[partNumber.row].charAt(rightIndex);
            perimeter.append(right);
        }
        //pants
        if (partNumber.row != schematic.length-1){
            String pants = schematic[partNumber.row + 1].substring(leftIndex, rightIndex+1);
            perimeter.append(pants);
        }
        String scrubbedPerimeter = perimeter.toString().replaceAll("[\\d\\.]", "");
        log.debug("perimeter: {}, scrubbed:{}, scrublen:{}",perimeter, scrubbedPerimeter, scrubbedPerimeter.length());

        return scrubbedPerimeter.length() > 0;
    }

    public static List<Integer> findGearRatios(List<Gear> gearList, List<PartNumber> partNumberList){
        List<Integer> gearRatios = new ArrayList<>();
        for(Gear gear:gearList){
            log.debug("gear{}", gear);
            List<PartNumber> adjacentParts = new ArrayList<>();
            for(PartNumber pn:partNumberList){
                if(gear.y >= pn.row-1 && gear.y <= pn.row+1 && gear.x >= pn.beginindex-1 && gear.x <= pn.beginindex+pn.width){
                    adjacentParts.add(pn);
                }
            }
            if(adjacentParts.size() == 2){
                log.debug("GEAR:{}", gear);
                log.debug("PART1:{}",adjacentParts.get(0));
                log.debug("PART2:{}",adjacentParts.get(1));
                gearRatios.add(adjacentParts.get(0).value * adjacentParts.get(1).value);
            }
        }
        return gearRatios;
    }
}
