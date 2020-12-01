package com.simongarton.advent_of_code;

import com.simongarton.advent_of_code.days.*;

public class Main {

    public static void main(String[] args) {
        //day0();
        day1();
        day2();
        day3();
        day4();
    }

    private static void day4() {
        Day4 day4 = new Day4();
        day4.run();
    }

    private static void day3() {
        Day3 day3 = new Day3();
        day3.run();
    }

    private static void day2() {
        Day2 day2 = new Day2();
        day2.run();
    }

    private static void day1() {
        Day1 day1 = new Day1();
        day1.run();
    }

    private static void day0() {
        Day0 day0 = new Day0();
        boolean result1 = day0.run();
        System.out.println("day 0 : " + result1);
    }
}
