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
            runProgram();
            System.out.println(memory.size() + " instructions.");
            System.out.println("");
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
            debugProgram();
            String instruction = String.valueOf(memory.get(pointer));
//            System.out.println("Pointer " + pointer + " Instruction " + instruction);
            Operation operation = new Operation(pointer, instruction);
//            System.out.println("Operation " + operation);
            if (operation.type == OperationType.HALT) {
                break;
            }
//            System.out.println("Memory " + getMemory(operation, memory));
            int result;
            switch (operation.type) {
                case ADDITION:
                    result = operation.get(memory, 1) + operation.get(memory, 2);
                    memory.set(memory.get(pointer + 3), result);
                    System.out.println(pointer + " " + operation.type + " : first " + operation.get(memory, 1)
                            + " second " + operation.get(memory,2) + " result " + result + " write to " + memory.get(pointer + 3) + " move to " + (pointer + 4));
                    pointer += 4;
                    break;
                case MULTIPLICATION:
                    result = operation.get(memory, 1) * operation.get(memory, 2);
                    memory.set(memory.get(pointer + 3), operation.get(memory, 1) * operation.get(memory, 2));
                    System.out.println(pointer + " " + operation.type + " : first " + operation.get(memory, 1)
                            + " second " + operation.get(memory,2) + " result " + result + " write to " + memory.get(pointer + 3) + " move to " + (pointer + 4));
                    pointer += 4;
                    break;
                case INPUT:
                    int input = getInput();
                    memory.set(memory.get(pointer + 1), input);
                    System.out.println(pointer + " " + operation.type + " : got " + input + " write to " + memory.get(pointer + 1) + " move to " + (pointer + 2));
                    pointer += 2;
                    break;
                case OUTPUT:
                    output(operation.get(memory, 1));
                    System.out.println(pointer + " " + operation.type + " : output " + memory.get(pointer + 1) + " move to " + (pointer + 2));
                    pointer += 2;
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
            return 1;
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
    public static enum OperationType {
        ADDITION(1),
        MULTIPLICATION(2),
        INPUT(3),
        OUTPUT(4),
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
