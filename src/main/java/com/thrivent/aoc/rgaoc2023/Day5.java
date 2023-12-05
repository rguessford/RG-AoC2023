package com.thrivent.aoc.rgaoc2023;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//for part 2, this produces the correct answer +1. i'm not quite sure why yet.
@Slf4j
public class Day5 {
    
    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd5.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        String[] stringArray = stringList.toArray(new String[]{});

        Long[] seeds = parseSeeds(stringArray[0]);
        List<SeedRange> seedsP2 = parseSeeds2(stringArray[0]);
        List<Mapping> mappings = parseMappings(Arrays.copyOfRange(stringArray, 2,stringArray.length));

        for (Mapping mapping : mappings) {
            for (int j = 0; j < seeds.length; j++) {
                seeds[j] = mapping.mapValue(seeds[j]);
            }
        }
        //88151870
        log.info("part1: {}",Arrays.stream(seeds).min(Long::compare).get());


        log.info("part2: {}",Arrays.stream(seeds).min(Long::compare).get());
        long p2start = System.currentTimeMillis();
        List<SeedRange> workingSet = new ArrayList<>();
        List<SeedRange> nextSet = List.copyOf(seedsP2);
        for (int i = 0; i < mappings.size(); i++) {
            log.info("mapper {}", i);
            workingSet = nextSet;
            nextSet = new ArrayList<>();
            for (SeedRange r : workingSet) {
                nextSet.addAll(mappings.get(i).mapRange(r));
            }
            log.info("{}",workingSet);
            log.info("{}",nextSet);
        }
        long p2end=System.currentTimeMillis();
        log.info("{}", p2end-p2start);
        log.info("part2:{}", nextSet.stream().map(seedRange -> seedRange.start).min(Long::compareTo).get());
    }

    @Data
    static class Mapping{

        List<Long> src = new ArrayList<>();
        List<Long> range = new ArrayList<>();
        List<Long> delta = new ArrayList<>();

        public void addMapping(Long d, Long s, Long r){
            src.add(s);
            range.add(r);
            delta.add(d-s);
        }
        public Long getEnd(int i){
            return src.get(i)+range.get(i);
        }

        Long mapValue(Long in){
            for(int i = 0; i < src.size(); i++){
                if(in > src.get(i) && in < getEnd(i)){
                    return in + delta.get(i);
                }
            }
            return in;
        }
        List<SeedRange> mapRange( SeedRange in){
            return mapRange(new ArrayList<>(), in);
        }

        List<SeedRange> mapRange(List<SeedRange> result, SeedRange in){
            boolean mapped = false;
            for (int i = 0; i < src.size(); i++) {
                //if the start of the seedrange is in the range of the mapping
                if(in.start > src.get(i) && in.start < getEnd(i)){
                    mapped = true;
                    //if the end of the seedrange extends beyond this range
                    if(in.end() > getEnd(i)) {
                        result.add(new SeedRange(in.start+delta.get(i), getEnd(i)-in.start));
                        result.addAll(mapRange(result, new SeedRange(getEnd(i)+1, in.range-(getEnd(i)-in.start))));
                        break;
                    } else {
                        result.add(new SeedRange(in.start+delta.get(i), in.range));
                        break;
                    }
                }

                //if the end of the seedRange is in the range of the mapping
                if(in.end() > src.get(i) && in.end() < getEnd(i)){
                    mapped = true;
                    //if the start of the seedrange begins before this range
                    if(in.start < src.get(i)) {
                        result.add(new SeedRange(src.get(i)+delta.get(i),in.end()-src.get(i)));
                        result.addAll(mapRange(result, new SeedRange(in.start, in.range-(in.end()-src.get(i)))));
                        break;
                    } else {
                        result.add(new SeedRange(in.start+delta.get(i), in.range));
                        break;
                    }
                }
            }
            if(!mapped) return List.of(in);
            return result;
        }
    }

    record SeedRange(Long start, Long range){
        public long end(){
            return start+range;
        }
    }

    static Long[] parseSeeds(String seedList) {
        String[] str = seedList.split(" ");
        return Arrays.stream(Arrays.copyOfRange(str, 1, str.length))
                .map(Long::valueOf)
                .toArray(Long[]::new);
    }

    static List<SeedRange> parseSeeds2(String seedList) {
        String[] str = seedList.split(" ");
        Long[] longs = Arrays.stream(Arrays.copyOfRange(str, 1, str.length))
                .map(Long::valueOf)
                .toArray(Long[]::new);
        List<SeedRange> seedRanges = new ArrayList<>();
        for (int i = 0; i < longs.length; i+=2) {
            seedRanges.add(new SeedRange(longs[i], longs[i+1]));
        }
        return seedRanges;
    }

    static List<Mapping> parseMappings(String[] input){
        List<Mapping> mappings = new ArrayList<>();
        Mapping currentMapper = null;
        for(int i = 0; i < input.length; i++){
            if(input[i].contains(":")){
                currentMapper = new Mapping();
                mappings.add(currentMapper);
                continue;
            }
            if (input[i].length() == 0){
                continue;
            }
            String[] maprow = input[i].split(" ");
            currentMapper.addMapping(Long.parseLong(maprow[0]), Long.parseLong(maprow[1]), Long.parseLong(maprow[2]));
        }
        return mappings;
    }
}
