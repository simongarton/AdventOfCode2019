package com.simongarton.advent_of_code.days;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day2 {

    List<Integer> memory;
    String result;

    public void run() {
        try {
            System.out.println("Day 2: 1202 Program Alarm\n");
            File file = Paths.get("data", "day2.txt").toFile();
            result = Files.readAllLines(Paths.get(file.getAbsolutePath())).get(0);
            loadProgram();
            System.out.println(memory.size() + " instructions.");
            run1202();
            run19690720();
            System.out.println("");
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.out.println("");
        }
    }

    private void run19690720() {
        for (int noun = 0; noun <= 99; noun++) {
            for (int verb = 0; verb <= 99; verb++) {
                loadProgram();
                setInitial(noun, verb);
                runProgram();
                if (memory.get(0) == 19690720) {
                    debugProgram();
                    System.out.println("Result: " + noun + " " + verb + " = " + ((100 * noun) + verb));
                }
            }
        }
    }

    private void run1202() {
        loadProgram();
        setInitial(12, 2);
        runProgram();
        debugProgram();
        System.out.println("Result: " + memory.get(0));
    }

    private void loadProgram() {
        memory = Arrays.stream(result.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    private void debugProgram() {
        System.out.println(memory.stream().map(i -> i + "").collect(Collectors.joining(",")));
    }

    private void runProgram() {
        int pointer = 0;
        int opCode = memory.get(pointer);
        int ref1, ref2, ref3;
        int val1, val2, result;
        while (opCode != 99) {
            if ((opCode != 1) && (opCode != 2)) {
                throw new RuntimeException("Bad opCode " + opCode);
            }
            ref1 = memory.get(pointer + 1);
            ref2 = memory.get(pointer + 2);
            ref3 = memory.get(pointer + 3);
            val1 = memory.get(ref1);
            val2 = memory.get(ref2);
            if (opCode == 1) {
                result = val1 + val2;
            } else {
                result = val1 * val2;
            }
            memory.set(ref3, result);
            pointer += 4;
            opCode = memory.get(pointer);
        }
    }

    private void setInitial(int one, int two) {
        memory.set(1, one);
        memory.set(2, two);
    }
}
