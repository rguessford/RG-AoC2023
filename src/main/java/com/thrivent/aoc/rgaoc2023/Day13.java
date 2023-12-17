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
public class Day13 {
    static List<List<String>> maps = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd13.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        List<String> map = new ArrayList<>();
        for (String string :
                stringList) {
            if(string.isEmpty()){
                maps.add(map);
                map = new ArrayList<>();
            } else {
                map.add(string);
            }
        }
        maps.add(map);
        Integer hTotal = 0;
        Integer vTotal = 0;

        for(List<String> imap:maps){
            log.info("{}", imap);
            hTotal += findHorizontalReflection(imap);
            vTotal += findVerticalReflection(imap);
            log.info("{}:{}",vTotal,hTotal);
        }
    }

    public static Integer findHorizontalReflection(List<String> map){
        Boolean[][] reflectionMap = new Boolean[map.size()][map.get(0).length()-1];

        Boolean[] possibleReflections = new Boolean[map.get(0).length()-1];

        for (int i = 0; i < map.size(); i++) {
            Arrays.fill(possibleReflections, true);
//            if(!Arrays.stream(possibleReflections).reduce(false , (a, b)-> a || b )){
//                log.info("no possible horizontal reflections");
//                return 0;
//            }
            String line = map.get(i);
            for (int j = 0; j < possibleReflections.length; j++) {
                if(!possibleReflections[j])continue;
                String left=line.substring(0,1+j);
                String right=line.substring(1+j);
                log.debug("left: {} : {} : right", left, right);
                for (int k = 0; k < left.length() && k < right.length(); k++) {
                    log.debug("{} != {} == ", left.charAt(left.length()-(1+k)), right.charAt(k));
                    if(left.charAt(left.length()-(1+k)) != right.charAt(k)){
                        possibleReflections[j] = false;
                        break;
                    }
                }
            }
            reflectionMap[i] = Arrays.copyOf(possibleReflections, possibleReflections.length);
        }

//        for (int i = 0; i < reflectionMap.length; i++) {
//            log.info(Arrays.toString(reflectionMap[i]));
//        }
        for (int i = 0; i < reflectionMap[0].length; i++) {

            Integer falseCounter= 0;
            for (int j = 0; j < reflectionMap.length; j++) {
                if (!reflectionMap[j][i]) falseCounter++;
            }
            if(falseCounter == 0){
                log.info("reflection between columns {} and {}", i,i+1);
                //part 1:
                //return i+1;
            } else if (falseCounter == 1) {
                log.info("possible smudge for reflection between columns {} and {}", i, i+1);
                return i+1;
            }
        }

//        for (int i = 0; i < possibleReflections.length; i++) {
//            if(possibleReflections[i]){
//                log.info("reflection between line {} and {}",i,i+1);
//                return i+1;
//            }
//        }
        return 0;
    }

    public static Integer findVerticalReflection(List<String> map){
        Boolean[][] reflectionMap = new Boolean[map.get(0).length()][map.size()-1];

        Boolean[] possibleReflections = new Boolean[map.size()-1];


        for (int i = 0; i < map.get(0).length(); i++) {
            Arrays.fill(possibleReflections, true);

//            if(!Arrays.stream(possibleReflections).reduce(false , (a, b)-> a || b )){
//                log.info("no possible vertical reflections");
//                return 0;
//            }
            StringBuilder currentColumnBuilder = new StringBuilder();
            for (int j = 0; j < map.size(); j++) {
                currentColumnBuilder.append(map.get(j).charAt(i));
            }
            String line = currentColumnBuilder.toString();
            for (int j = 0; j < possibleReflections.length; j++) {
                if(!possibleReflections[j])continue;
                String left=line.substring(0,1+j);
                String right=line.substring(1+j);
                log.debug("left: {} : {} : right", left, right);
                for (int k = 0; k < left.length() && k < right.length(); k++) {
                    log.debug("{} != {} == ", left.charAt(left.length()-(1+k)), right.charAt(k));
                    if(left.charAt(left.length()-(1+k)) != right.charAt(k)){
                        possibleReflections[j] = false;
                        break;
                    }
                }
            }

            reflectionMap[i] = Arrays.copyOf(possibleReflections, possibleReflections.length);
        }

//        for (int i = 0; i < reflectionMap.length; i++) {
//            log.info(Arrays.toString(reflectionMap[i]));
//        }
        for (int i = 0; i < reflectionMap[0].length; i++) {

            Integer falseCounter= 0;
            for (int j = 0; j < reflectionMap.length; j++) {
                if (!reflectionMap[j][i]) falseCounter++;
            }
            if(falseCounter == 0){
                log.info("reflection between rows {} and {}", i,i+1);
                //part 1:
                //return i+1;
            } else if (falseCounter == 1) {
                log.info("possible smudge for reflection between rows {} and {}", i, i+1);
                //part 2:
                return i+1;
            }
        }

//        for (int i = 0; i < possibleReflections.length; i++) {
//            if(possibleReflections[i]){
//                log.info("reflection between line {} and {}",i,i+1);
//                return i+1;
//            }
//        }
        return 0;
    }
}
