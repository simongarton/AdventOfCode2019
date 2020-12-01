package com.simongarton.advent_of_code;

import com.simongarton.advent_of_code.days.Day0;
import com.simongarton.advent_of_code.days.Day1;

public class Main {

    public static void main(String[] args) {
        //day0();
        day1();
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
