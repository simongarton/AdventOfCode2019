package com.simongarton.advent_of_code.days;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Day1 {

    public long run() {

        try {
            System.out.println("Day 1: The Tyranny of the Rocket Equation\n");
            String filename = "day1.txt";
            File file = Paths.get("data", "day1.txt").toFile();
            List<String> result = Files.readAllLines(Paths.get(file.getAbsolutePath()));
            long fuelNeeded = calculateFuelForModule(result);
            System.out.println("Fuel for elves: " + fuelNeeded);
            long totalFuelNeeded = calculateTotalFuel(result);
            System.out.println("Total fuel needed: " + totalFuelNeeded);
            System.out.println("");
            return totalFuelNeeded;
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
            System.out.println("");
            return 0;
        }
    }

    private long calculateTotalFuel(List<String> masses) {
        long total = 0;
        for (String mass : masses) {
            long actualMass = Long.parseLong(mass);
            long fuelNeeded = calculateFuelForMass(actualMass);
            total += fuelNeeded;
        }
        return total;
    }

    private long calculateFuelForModule(List<String> masses) {
        long total = 0;
        for (String mass : masses) {
            long actualMass = Long.parseLong(mass);
            long fuelNeeded = (actualMass / 3) - 2;
            total += fuelNeeded;
        }
        return total;
    }

    private long calculateFuelForMass(Long actualMass) {
        long total = 0;
        long fuelTotal;
        long mass = actualMass;
        do {
            fuelTotal = (mass / 3) - 2;
            if (fuelTotal < 0) {
                fuelTotal = 0;
            }
            total += fuelTotal;
            mass = fuelTotal;
        } while (fuelTotal > 0);
        return total;
    }
}
