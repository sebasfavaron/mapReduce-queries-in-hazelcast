package itba.server;

import itba.model.Movement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class CsvLoader {

    public void loadMovement(List<Movement> movements, String filePath){
        File csvFile = new File(filePath);
        System.out.println(csvFile.getAbsolutePath());

        if (csvFile.isFile()) {
            String row;

            try {

                BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
                csvReader.readLine(); // waste first line that only gives column names

                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(";");

                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = df.parse(data[0]);
                    Integer time = Integer.valueOf(data[1]);
                    String flightType = data[2], classification = data[3], moveType = data[4], OACIOrigin = data[5], OACIDestiny = data[6], airline = data[7], plane = data[8];

                    movements.add(new Movement(date, time, flightType, classification, moveType, OACIOrigin, OACIDestiny, airline, plane));
                }
                csvReader.close();
            } catch (IOException e) {
                System.out.println("Error with the file");
            } catch (ParseException e) {
                System.out.println("Could not parse date");;
            }

        } else {
            System.out.println("Invalid file name");
        }
    }
}
