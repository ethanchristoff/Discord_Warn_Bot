package events;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.entities.User;

public class CSV_Manager_Content {
    private final String csvFile = "user_data.csv";

    public CSV_Manager_Content(){
        File file = new File(csvFile);

        // Check if the file exists
        if (file.exists()) {
            System.out.println("The file " + csvFile + " already exists.");
        } else {
            try {
                // Create a new file
                if (file.createNewFile()) {
                    System.out.println("The file " + csvFile + " was created successfully.");

                    // Optionally, write a header to the new CSV file
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                        writer.write("User,Warnings,Warning_Messages,total_timeouts"); // Example header
                        writer.newLine();
                        System.out.println("Header written to the new CSV file.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Failed to create the file " + csvFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String read_data(User user, String selected_column){
        String result = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            String[] header = null;
            int columnIndex = -1;

            // Step 1: Read the header row to find the index of the selected column
            if ((line = reader.readLine()) != null) {
                header = line.split(",");
                for (int i = 0; i < header.length; i++) {
                    if (header[i].equalsIgnoreCase(selected_column)) {
                        columnIndex = i;
                        break;
                    }
                }
            }

            if (columnIndex == -1) {
                System.out.println("Column '" + selected_column + "' not found.");
                return null;
            }

            // Step 2: Find the row corresponding to the user and extract the value in the selected column
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");

                if (columns[0].equals(user.toString())) { // Assuming user.toString() returns a unique identifier
                    result = columns[columnIndex];
                    break;
                }
            }

            if (result == null) {
                System.out.println("User not found or column data is missing.");
            } else {
                System.out.println("Data for " + user + " in column '" + selected_column + "': " + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public void write_line(User user, int warnings, String warning_message ,int total_timeouts){
        List<String[]> data = new ArrayList<>();
        boolean userExists = false;

        // Step 1: Read the CSV file into memory
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                if (row[0].equals(user.toString())) {
                    row[1] = String.valueOf(warnings);
                    row[2] = warning_message;
                    row[3] = String.valueOf(total_timeouts);
                    userExists = true;
                }
                data.add(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Step 2: In case the user does not exist, the users data will be added into a new row
        if (!userExists) {
            data.add(new String[]{user.toString(), String.valueOf(warnings), warning_message, String.valueOf(total_timeouts)});
        }

        // Step 3: Writes the updated data back to the CSV file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
            for (String[] row : data) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
            System.out.println("Data written to " + csvFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}