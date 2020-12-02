package com.simongarton.advent_of_code.days;

import lombok.Data;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day5 {

    List<Integer> memory;
    String result;

    public void run() {
        try {
            System.out.println("Day 5: Sunny with a Chance of Asteroids\n");
            File file = Paths.get("data", "day5.txt").toFile();
            result = Files.readAllLines(Paths.get(file.getAbsolutePath())).get(0);
            loadProgram();
            System.out.println(memory.size() + " instructions.");
            System.out.println("");
            runProgram();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.out.println("");
        }
    }

    private void loadProgram() {
        memory = Arrays.stream(result.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    private void debugProgram() {
        String line = memory.stream().map(Objects::toString).collect(Collectors.joining(","));
        line = line.substring(0, 100) + "...";
        System.out.println(line);
    }

    private void runProgram() {
        int pointer = 0;
        while (true) {
            String instruction = String.valueOf(memory.get(pointer));
            Operation operation = new Operation(pointer, instruction);
            if (operation.type == OperationType.HALT) {
                break;
            }
            int result, first, second, third;
            switch (operation.type) {
                case ADDITION:
                    first = operation.get(memory, 1);
                    second = operation.get(memory, 2);
                    result = first + second;
                    memory.set(memory.get(pointer + 3), result);
                    pointer += 4;
                    break;
                case MULTIPLICATION:
                    first = operation.get(memory, 1);
                    second = operation.get(memory, 2);
                    result = first * second;
                    memory.set(memory.get(pointer + 3), result);
                    pointer += 4;
                    break;
                case INPUT:
                    int input = getInput();
                    memory.set(memory.get(pointer + 1), input);
                    pointer += 2;
                    break;
                case OUTPUT:
                    output(operation.get(memory, 1));
                    pointer += 2;
                    break;
                case JUMP_IF_TRUE:
                    first = operation.get(memory, 1);
                    second = operation.get(memory, 2);
                    if (first != 0) {
                        pointer = second;
                    } else {
                        pointer += 3;
                    }
                    break;
                case JUMP_IF_FALSE:
                    first = operation.get(memory, 1);
                    second = operation.get(memory, 2);
                    if (first == 0) {
                        pointer = second;
                    } else {
                        pointer += 3;
                    }
                    break;
                case LESS_THAN:
                    first = operation.get(memory, 1);
                    second = operation.get(memory, 2);
                    third = memory.get(pointer + 3);
                    if (first < second) {
                        memory.set(third, 1);
                    } else {
                        memory.set(third, 0);
                    }
                    pointer += 4;
                    break;
                case EQUALS:
                    first = operation.get(memory, 1);
                    second = operation.get(memory, 2);
                    third = memory.get(pointer + 3);
                    if (first == second) {
                        memory.set(third, 1);
                    } else {
                        memory.set(third, 0);
                    }
                    pointer += 4;
                    break;
            }
        }
    }

    private String getMemory(Operation operation, List<Integer> memory) {
        String line = "instruction " + operation.pointer + " ";
        line = line + "operation " + operation.type + " ";
        line = line + memory.get(operation.pointer + 1);
        if (operation.type == OperationType.ADDITION || operation.type == OperationType.MULTIPLICATION) {
            line = line + ", " + memory.get(operation.pointer + 2);
            line = line + ", " + memory.get(operation.pointer + 3);
        }
        return line;
    }

    private void output(Integer value) {
        System.out.println(String.valueOf(value));
    }

    private int getInput() {
        if (true) {
            return 5;
        }
        System.out.println("Enter a number, then press return.");
        Scanner in = new Scanner(System.in);
        return in.nextInt();
    }

    private void setInitial(int one, int two) {
        memory.set(1, one);
        memory.set(2, two);
    }

    @Getter
    public enum OperationType {
        ADDITION(1),
        MULTIPLICATION(2),
        INPUT(3),
        OUTPUT(4),
        JUMP_IF_TRUE(5),
        JUMP_IF_FALSE(6),
        LESS_THAN(7),
        EQUALS(8),
        HALT(99);

        private final int operationCode;

        OperationType(int operationCode) {
            this.operationCode = operationCode;
        }

        static OperationType fromCode(int operationCode) {
            for (OperationType operationType : values()) {
                if (operationType.getOperationCode() == operationCode) {
                    return operationType;
                }
            }
            return null;
        }
    }

    @Data
    private static class Operation {

        private int pointer;
        private OperationType type;
        private List<Integer> modes = new ArrayList<>();

        public Operation(int pointer, String instruction) {
            this.pointer = pointer;
            int length = instruction.length();
            int operationCode = length > 1 ? Integer.parseInt(instruction.substring(length - 2, length)) : Integer.parseInt(instruction);
            type = OperationType.fromCode(operationCode);
            if (length > 0) {
                for (int i = length - 3; i >= 0; i--) {
                    modes.add(Integer.parseInt(instruction.substring(i, i + 1)));
                }
            }
        }

        @Override
        public String toString() {
            return pointer + ":" + type + " " + modes.stream().map(Object::toString).collect(Collectors.joining(","));
        }

        public int get(List<Integer> memory, int i) {
            int val = memory.get(pointer + i);
            if (modes.isEmpty() || modes.size() < i || modes.get(i - 1) == 0) {
                return memory.get(val);
            }
            return val;
        }
    }
}
