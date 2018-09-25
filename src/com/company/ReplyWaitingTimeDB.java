package com.company;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class to handle waiting time records and queries
 */
class ReplyWaitingTimeDB {
    private String[] db = new String[100000];
    private int length = 0;

    /**
     * @param line String to query
     * @return Average waiting time
     */
    int query(String line) {
        //sum of all waiting times
        int sum = 0;
        //counts number of lines used
        int count = 0;
        //array of strings between spaces
        String[] querySplit = line.split(" ");

        for (int i = 0; i < length; i++) {
            String[] tempSplit = db[i].split(" ");

            //compares service_id[.variation_id]
            if (querySplit[1].equals("*") || tempSplit[1].startsWith(querySplit[1])) {

                //compares question_type_id[.category_id.[sub-category_id]]
                if (querySplit[2].equals("*") || tempSplit[2].startsWith(querySplit[2])) {

                    //compares P/N
                    if (querySplit[3].equals(tempSplit[3])) {

                        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                        Date queryDate, tempDate;

                        try {
                            queryDate = format.parse(querySplit[4].substring(0,10));
                            tempDate = format.parse(tempSplit[4]);
                        } catch (ParseException e) {
                            return -1;
                        }

                        //compares if it is greater than date_from
                        if (tempDate.after(queryDate) || tempDate.equals(queryDate)) {
                            //if it is a time range
                            if (querySplit[4].indexOf("-") > 0) {

                                try {
                                    queryDate = format.parse(querySplit[4].substring(11,21));
                                } catch (ParseException e) {
                                    return -1;
                                }

                                //compares if it is less than date_to
                                //if at least one is greater, loop continues
                                if (tempDate.after(queryDate)) {
                                    continue;
                                }
                            }
                            //adds waiting time to sum var and increments count
                            sum += Integer.parseInt(tempSplit[5]);
                            count++;
                        }
                    }
                }
            }
        }

        return count > 0 ? sum / count : -1;
    }

    /**
     * @param line String to record
     * C service_id[.variation_id] question_type_id[.category_id.[sub-category_id]] P/N date time
     */
    void record(String line) {
        if (length < 100000 && line.matches(
                "^C\\s\\d\\d?(\\.\\d)?\\s" +
                "\\d\\d?(\\.\\d\\d?(\\.\\d)?)?\\s" +
                "[PN]\\s(\\d{2}\\.){2}\\d{4}\\s\\d+"
        )) {
            db[length++] = line;
        }
    }
}
