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
        Boolean[] possibleReflections = new Boolean[map.get(0).length()-1];
        Arrays.fill(possibleReflections, true);

        for (int i = 0; i < map.size(); i++) {
            if(!Arrays.stream(possibleReflections).reduce(false , (a, b)-> a || b )){
                log.info("no possible horizontal reflections");
                return 0;
            }
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
        }
        for (int i = 0; i < possibleReflections.length; i++) {
            if(possibleReflections[i]){
                log.info("reflection between line {} and {}",i,i+1);
                return i+1;
            }
        }
        return 0;
    }

    public static Integer findVerticalReflection(List<String> map){
        Boolean[] possibleReflections = new Boolean[map.size()-1];
        Arrays.fill(possibleReflections, true);

        for (int i = 0; i < map.get(0).length(); i++) {
            if(!Arrays.stream(possibleReflections).reduce(false , (a, b)-> a || b )){
                log.info("no possible vertical reflections");
                return 0;
            }
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
        }
        for (int i = 0; i < possibleReflections.length; i++) {
            if(possibleReflections[i]){
                log.info("reflection between line {} and {}",i,i+1);
                return i+1;
            }
        }
        return 0;
    }
}
