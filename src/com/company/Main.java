package com.company;

import java.util.Scanner;

public class Main {
    private static ReplyWaitingTimeDB db;

    public static void main(String[] args) {
        db = new ReplyWaitingTimeDB();
        testData();
        //readData();
    }

    private static void testData() {
        db.record("C 1.1 8.15.1 P 15.10.2012 83");
        db.record("C 1 10.1 P 01.12.2012 65");
        db.record("C 1.1 5.5.1 P 01.11.2012 117");
        System.out.println(db.query("D 1.1 8 P 01.01.2012-01.12.2012"));
        db.record("C 3 10.2 N 02.10.2012 100");
        System.out.println(db.query("D 1 * P 08.10.2012-20.11.2012"));
        System.out.println(db.query("D 3 10 P 01.12.2012"));
    }

    public static void readData() {
        Scanner scanner = new Scanner(System.in);

        int count = scanner.nextInt();

        if (count > 0 && count <= 100000) {
            String line;
            scanner.nextLine();

            for (int i = 0; i < count; i++) {
                line = scanner.nextLine();

                if (line.charAt(0) == 'C') {

                    db.record(line);

                } else if (line.charAt(0) == 'D') {

                    int result = db.query(line);
                    if (result == -1) {
                        System.out.println("-");
                    } else {
                        System.out.println(result);
                    }
                }
            }
        }
    }
}
