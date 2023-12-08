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
                entry("J", 0),
                entry("2", 1),
                entry("3", 2),
                entry("4", 3),
                entry("5", 4),
                entry("6", 5),
                entry("7", 6),
                entry("8", 7),
                entry("9", 8),
                entry("T", 9),
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
        for (String s : stringList) {
            handList.add(parseHand(s));
        }
        HandComparator comparator = new HandComparator();
        handList.sort(comparator);
        int accumulator = 0;
        for (int i = 0; i < handList.size(); i++) {
            log.debug("rank:{}, winnings:{}, hand:{} ", i+1, handList.get(i).bid * (i+1), handList.get(i).toString());
            accumulator = accumulator + (handList.get(i).bid * (i+1));
            log.debug("{}", accumulator);
        }
        log.info("part2:{}",accumulator);
    }
    private enum HandType{
        FIVE(7), FOUR(6), FH(5), THREE(4), TWO_PAIR(3), TWO(2), ONE(1), HC(0);
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
        int modeIndex = -1;
        int maxCount=0;
        for (int i = 1; i < countHand.length; i++) {
            if(countHand[i] > maxCount){
                maxCount=countHand[i];
                modeIndex=i;
            }
        }
        if(modeIndex == -1) {
            //all we found was jokers
            countHand[12] = 5;
        } else {
            countHand[modeIndex] += countHand[0];
        }
        boolean hasThree = false;
        boolean hasTwo = false;
        boolean hasTwoAgain = false;
        for (int i = 1; i < countHand.length; i++) {
            if(countHand[i] == 5){
                return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.FIVE );
            } else if (countHand[i] == 4) {
                return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.FOUR );
            } else if (countHand[i] == 3) {
                hasThree = true;
            } else if (countHand[i] == 2) {
                if(hasTwo) hasTwoAgain = true;
                hasTwo = true;
            }
        }
        if (hasThree && hasTwo){
            return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.FH );
        } else if (hasThree) {
            return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.THREE );
        } else if (hasTwoAgain){
            return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.TWO_PAIR);
        }else if (hasTwo){
            return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.TWO );
        }
        return new Hand(input.split(" ")[0], Integer.parseInt(input.split(" ")[1]), HandType.HC );
    }

}
