package com.thrivent.aoc.rgaoc2023;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class Day12 {

    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd12.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);

        Long accumulator = 0L;
        for (String s :
                stringList) {
            s = s.trim();
            String[] problem = s.split(" ");
            String layout = problem[0];
            List<Integer> nums = Arrays.stream(problem[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
            log.debug("{}, {}", layout, nums);
            accumulator += calc(layout, nums, 0);
//            log.info("result: {}", calc2(layout, nums, 0));
        }
        log.info("part1: {}", accumulator);

        accumulator = 0L;
        for (String s :
                stringList) {
            s = s.trim();
            String[] problem = s.split(" ");
            String layout = problem[0];
            layout = String.join("?",layout,layout,layout,layout,layout);

            List<Integer> nums = Arrays.stream(problem[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
            List<Integer> newNums = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                newNums.addAll(nums);
            }

            log.info("{}, {}", layout, newNums);
            accumulator += calc2(layout, newNums, 0);
            cache.clear();
        }
        log.info("part2: {}", accumulator);
    }

    static HashMap<Entry, Long> cache = new HashMap<>();

    private record Entry(String map, Integer num, Integer index){ }

    public static Integer calc(String map, List<Integer> nums, Integer numsIndex) {
        Integer result = 0;
        if(numsIndex == nums.size()){
            if (map.contains("#")){
                return 0;
            } else {
                return 1;
            }
        }
        if(map.length()==0){
            return 0;
        }
        Integer currentNum = nums.get(numsIndex);
        Character next = map.charAt(0);
        switch (next) {
            case '.':
                log.debug("skipping map {}", map);
                result += calc(map.substring(1), nums, numsIndex);
                break;
            case '#':
                log.debug("checking if {} fits in map {}", currentNum, map);
                if (currentNum > map.length()){
                    log.debug("it doesn't!");
                    return 0;
                }
                if (map.substring(0, currentNum).contains(".")) {
                    log.debug("it doesn't!");
                    return 0;
                } else {
                    log.debug("it does!");
                    if (currentNum >= map.length()){
                        if (numsIndex+1 == nums.size()){
                            log.debug("found possible arrangement! last num fits perf");
                            return 1;
                        }
                    } else {
                        if(map.charAt(currentNum)=='#'){
                            log.debug("nope! map pipe section too long");
                            return 0;
                        }
                        result += calc(map.substring(currentNum+1), nums, numsIndex + 1);
                    }
                }
                break;
            case '?':
                log.debug("examining branch . {}", map);
                result += calc("." + map.substring(1), nums, numsIndex);
                log.debug("examining branch # {}", map);
                result += calc("#" + map.substring(1), nums, numsIndex);
                break;
        }
        return result;
    }
    public static long calc2(String map, List<Integer> nums, Integer numsIndex) {
        long result = 0;
        log.debug(map);
        if(numsIndex == nums.size()){
            if (map.contains("#")){
                return 0;
            } else {
                return 1;
            }
        }
        if(map.length()==0){
            return 0;
        }
        Integer currentNum = nums.get(numsIndex);
        Entry entry = new Entry(map, currentNum, numsIndex);
        if(cache.containsKey(entry)){
            return cache.get(entry);
        }
        Character next = map.charAt(0);
        switch (next) {
            case '.':
                log.debug("skipping map {}", map);
                result += calc2(map.substring(1), nums, numsIndex);
                break;
            case '#':
                log.debug("checking if {} fits in map {}", currentNum, map);
                if (currentNum > map.length()){
                    log.debug("it doesn't!");
                    return 0;
                }
                if (map.substring(0, currentNum).contains(".")) {
                    log.debug("it doesn't!");
                    return 0;
                } else {
                    log.debug("it does!");
                    if (currentNum >= map.length()){
                        if (numsIndex+1 == nums.size()){
                            log.debug("found possible arrangement! last num fits perf");
                            return 1;
                        }
                    } else {
                        if(map.charAt(currentNum)=='#'){
                            log.debug("nope! map pipe section too long");
                            return 0;
                        }
                        result += calc2(map.substring(currentNum+1), nums, numsIndex + 1);
                    }
                }
                break;
            case '?':
                log.debug("examining branch . {}", map);
                result += calc2("." + map.substring(1), nums, numsIndex);
                log.debug("examining branch # {}", map);
                result += calc2("#" + map.substring(1), nums, numsIndex);
                break;
        }

        if(!cache.containsKey(entry)){
            cache.put(entry, result);
        }
        return result;
    }
}
