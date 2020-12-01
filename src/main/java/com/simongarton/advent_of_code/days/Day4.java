package com.simongarton.advent_of_code.days;

import java.util.HashMap;
import java.util.Map;

public class Day4 {

    public void run() {

        System.out.println("Day 4: Secure Container\n");

        int low = 347312;
        int high = 805915;

        int valid = 0;
        for (int i = low; i <= high; i++) {
            if (validPassword(i)) {
                valid++;
                //System.out.println(i);
            }
        }

        System.out.println("valid passwords " + valid);

        valid = 0;
        for (int i = low; i <= high; i++) {
            if (validPassword2(i)) {
                valid++;
                //System.out.println(i);
            }
        }

        System.out.println("valid password2s " + valid);
        System.out.println("\n" + valid);
        System.out.println("");
    }

    private boolean validPassword(int attempt) {
        String password = String.valueOf(attempt);
        boolean dbl = false;
        boolean decrease = false;
        for (int i = 1; i < 6; i++) {
            int a = Integer.parseInt(password.substring(i - 1, i));
            int b = Integer.parseInt(password.substring(i, i + 1));
            if (a == b) {
                dbl = true;
            }
            if (b < a) {
                decrease = true;
                break;
            }
        }
        return dbl && !decrease;
    }

    private boolean validPassword2(int attempt) {
        if (!validPassword(attempt)) {
            return false;
        }
        String password = String.valueOf(attempt);
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 6; i++) {
            int val = Integer.parseInt(password.substring(i, i + 1));
            if (map.containsKey(val)) {
                map.put(val, map.get(val) + 1);
            } else {
                map.put(val, 1);
            }
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 2) {
                return true;
            }
        }
        return false;
    }
}
