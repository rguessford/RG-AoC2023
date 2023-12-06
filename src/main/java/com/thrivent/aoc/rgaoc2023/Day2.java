package com.thrivent.aoc.rgaoc2023;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day2 {
    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd2.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        String[] stringArray = stringList.toArray(new String[]{});

        int part1 = Arrays.stream(stringArray)
                .map(Day2::parseGame)
                .filter(Game::isPossible)
                .map(game -> game.id)
                .reduce(0, Integer::sum);

        System.out.println(part1);

        int part2 = Arrays.stream(stringArray)
                .map(Day2::parseGame)
                .map(Game::gamePower)
                .reduce(0, Integer::sum);

        System.out.println(part2);
    }


    static class Game {
        public int id;
        public int maxR = 0;
        public int maxG = 0;
        public int maxB = 0;

        public void trySetMaxB(int maxB) {
            if (maxB > this.maxB) this.maxB = maxB;
        }

        public void trySetMaxG(int maxG) {
            if (maxG > this.maxG) this.maxG = maxG;
        }

        public void trySetMaxR(int maxR) {
            if (maxR > this.maxR) this.maxR = maxR;
        }

        public boolean isPossible() {
            return maxR <= 12 && maxG <= 13 && maxB <= 14;
        }

        public int gamePower() {
            return maxR * maxG * maxB;
        }
    }

    static Game parseGame(String game) {
        Game resultGame = new Game();
        Pattern findId = Pattern.compile("Game (\\d*):(.*)");
        Matcher m = findId.matcher(game);
        m.find();
        resultGame.id = Integer.parseInt(m.group(1));
        game = m.group(2);
        Pattern findBlockCount = Pattern.compile("\\s(\\d*)\\s(red|blue|green)(,|;|\\n|$)");

        m = findBlockCount.matcher(game);
        while (m.find()) {
            switch (m.group(2)) {
                case "red" -> resultGame.trySetMaxR(Integer.parseInt(m.group(1)));
                case "green" -> resultGame.trySetMaxG(Integer.parseInt(m.group(1)));
                case "blue" -> resultGame.trySetMaxB(Integer.parseInt(m.group(1)));
            }
        }
        return resultGame;
    }

}
