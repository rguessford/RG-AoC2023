package com.thrivent.aoc.rgaoc2023;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.util.Map.entry;

@Slf4j
public class Day7 {
    static Map<String, Integer> suits;

    static {
        suits = Map.ofEntries(
                entry("2", 0),
                entry("3", 1),
                entry("4", 2),
                entry("5", 3),
                entry("6", 4),
                entry("7", 5),
                entry("8", 6),
                entry("9", 7),
                entry("T", 8),
                entry("J", 9),
                entry("Q", 10),
                entry("K", 11),
                entry("A", 12)
        );
    }
    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd7.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        List<Hand> handList = new ArrayList<>();
        for (int i = 0; i < stringList.size(); i++) {
            handList.add(parseHand(stringList.get(i)));
        }
        HandComparator comparator = new HandComparator();
        handList.sort(comparator);
        int accumulator = 0;
        for (int i = 0; i < handList.size(); i++) {
            log.info("rank:{}, winnings:{}, hand:{} ", i+1, handList.get(i).bid * (i+1), handList.get(i).toString());
            accumulator = accumulator + (handList.get(i).bid * (i+1));
            log.info("{}", accumulator);
        }
        log.info("part1:{}",accumulator);
    }
    private enum HandType{
        FIVE(7), FOUR(6), FH(5), THREE(4),TWOPAIR(3), TWO(2), ONE(1), HC(0);
        public final int power;
        HandType(int in){
            power = in;
        }
    }

    record Hand(String hand, int bid, HandType type){

    }
    public static class HandComparator implements Comparator<Hand>{

        @Override
        public int compare(Hand o1, Hand o2) {
            if(o1.type.power - o2.type.power == 0){
                for (int i = 0; i < o1.hand.length(); i++) {
                    Integer one = suits.get(Character.toString(o1.hand.charAt(i)));
                    Integer two = suits.get(Character.toString(o2.hand.charAt(i)));
                    if(one-two != 0){
                        return one-two;
                    }
                }
            } else {
                return o1.type.power - o2.type.power;
            }
            return 0;
        }
    }
    private static Hand parseHand(String input){
        String hand = input.split(" ")[0];
        int[] countHand = new int[13];
        for (int i = 0; i < hand.length(); i++) {
            Integer suitIndex = suits.get(Character.toString(hand.charAt(i)));
            countHand[suitIndex]++;
        }
        boolean hasthree = false;
        boolean hastwo = false;
        boolean hastwoagain = false;
        for (int i = 0; i < countHand.length; i++) {
            if(countHand[i] == 5){
                return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.FIVE );
            } else if (countHand[i] == 4) {
                return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.FOUR );
            } else if (countHand[i] == 3) {
                hasthree = true;
            } else if (countHand[i] == 2) {
                if(hastwo) hastwoagain = true;
                hastwo = true;
            }
        }
        if (hasthree && hastwo){
            return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.FH );
        } else if (hasthree) {
            return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.THREE );
        } else if (hastwoagain){
            return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.TWOPAIR );
        }else if (hastwo){
            return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.TWO );
        }
        return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.HC );
    }

}
