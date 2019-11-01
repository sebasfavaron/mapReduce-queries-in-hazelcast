package itba.client;

import com.hazelcast.core.IList;
import itba.model.Airport;
import itba.model.Movement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CsvLoader {

    public void loadMovement(final IList<Movement> movements, final String filePath) {
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
                    String flightType = data[2], classification = data[3], moveType = data[4], OACIOrigin = data[5],
                            OACIDestiny = data[6], airline = data[7], plane = data[8];

                    movements.add(new Movement(date, time, flightType, classification, moveType, OACIOrigin, OACIDestiny,
                            airline, plane));
                }
                csvReader.close();
            } catch (IOException e) {
                System.out.println("Error with the file");
            } catch (ParseException e) {
                System.out.println("Could not parse date");
            }

        } else {
            System.out.println("Invalid file name");
        }
    }

    public void loadAirport(final IList<Airport> airports, final String filePath) {
        File csvFile = new File(filePath);
        System.out.println(csvFile.getAbsolutePath());

        if (csvFile.isFile()) {
            String row;

            try {
                BufferedReader csvReader = new BufferedReader(new FileReader(filePath));
                csvReader.readLine(); // waste first line that only gives column names

                while ((row = csvReader.readLine()) != null) {
                    String[] data = row.split(";");

                    String local = data[0], OACI = data[1], IATA = data[2], type = data[3], denomination = data[4],
                            coordinates = data[5], latitude = data[6], longitude = data[7], uomElev = data[8],
                            ref = data[9], refDistance = data[10], refDirection = data[11], condition = data[12],
                            control = data[13], region = data[14], fir = data[15], use = data[16], traffic = data[17],
                            sna = data[18], concessioned = data[19], province = data[20], inhab = data[21];

                    airports.add(new Airport(local, OACI, IATA, type, denomination, coordinates, latitude, longitude,
                            uomElev, ref, refDistance, refDirection, condition, control, region, fir, use, traffic, sna,
                            concessioned, province, inhab));
                }
                csvReader.close();
            } catch (IOException e) {
                System.out.println("Error with the file");
            }

        } else {
            System.out.println("Invalid file name");
        }
    }
}
