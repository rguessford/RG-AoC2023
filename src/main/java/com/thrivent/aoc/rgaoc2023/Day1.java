package com.thrivent.aoc.rgaoc2023;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Day1 {
    public static void main(String[] args) throws IOException {
        Path filePath = new File("src/main/resources/inputs/inputd1.txt").toPath();
        Charset charset = Charset.defaultCharset();
        List<String> stringList = Files.readAllLines(filePath, charset);
        String[] stringArray = stringList.toArray(new String[]{});
        int result = Arrays.stream(stringArray)
                .map(Day1::digitize)
                .map((s)-> s.replaceAll( "\\D", "" ))
                .map((s)-> {
                    int len = s.length();
                    Integer res = Integer.valueOf(""+s.charAt(0)+s.charAt(len-1));
                    return res;
                })
                .reduce(0, Integer::sum);
        System.out.println(result);
    }

    public static String digitize(String inStr){
        String str = inStr;
        str = str.replaceAll("one","o1e");
        str = str.replaceAll("two","t2o");
        str = str.replaceAll("three","t3e");
        str = str.replaceAll("four","f4r");
        str = str.replaceAll("five","f5e");
        str = str.replaceAll("six","s6x");
        str = str.replaceAll("seven","s7n");
        str = str.replaceAll("eight","e8t");
        str = str.replaceAll("nine","n9e");
        return str;
    }

}
