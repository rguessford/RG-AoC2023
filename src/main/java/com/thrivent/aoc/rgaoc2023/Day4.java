package com.thrivent.aoc.rgaoc2023;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class Day4 {

    static Integer[] cardQuantities;

    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd4.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        String[] stringArray = stringList.toArray(new String[]{});

        cardQuantities = new Integer[stringArray.length];
        Arrays.fill(cardQuantities, 1);

        Integer part1 = Arrays.stream(stringArray).map(Day4::parseGame)
                .map(Day4::calculateGameValue)
                .reduce(0, Integer::sum);

        log.info("{}", part1);

        List<Game> part2Games = Arrays.stream(stringArray)
                .map(Day4::parseGame)
                .map(Day4::countWinningNums)
                .toList();

        for (int i = 0; i < cardQuantities.length; i++) {
            Game game = part2Games.get(i);
            for (int j = 1; j < game.numWinners + 1; j++) {
                int cardsWon = cardQuantities[i];

                try {
                    cardQuantities[i + j] += cardsWon;
                } catch (ArrayIndexOutOfBoundsException e) {
                    //continue
                }
            }
        }

        Integer part2 = Arrays.stream(cardQuantities).reduce(0, Integer::sum);

        log.info("{}", part2);
    }

    @AllArgsConstructor
    static class Game {
        public Integer id;
        public List<Integer> winningnums;
        public List<Integer> playednums;
        public Integer score;
        public Integer numWinners;
    }

    static Integer calculateGameValue(Game game) {
        for (Integer i : game.playednums) {
            if (game.winningnums.contains(i)) {
                if (game.score == 0) {
                    game.score = 1;
                } else {
                    game.score *= 2;
                }
            }
        }
        return game.score;
    }

    static Game countWinningNums(Game game) {
        for (Integer i : game.playednums) {
            if (game.winningnums.contains(i)) {
                game.numWinners++;
            }
        }
        return game;
    }

    static Game parseGame(String game) {
        Pattern findId = Pattern.compile("Card\\s*(\\d*):(.*)");
        Matcher m = findId.matcher(game);
        m.find();
        game = m.group(2);
        String[] gamenums = game.split("\\|");
        String[] winningnums = gamenums[0].trim().split("\\s+");
        String[] playednums = gamenums[1].trim().split("\\s+");
        return new Game(Integer.parseInt(m.group(1)),
                Arrays.stream(winningnums).sequential().map(Integer::parseInt).collect(Collectors.toList()),
                Arrays.stream(playednums).sequential().map(Integer::parseInt).collect(Collectors.toList()),
                0,
                0);
    }

}
