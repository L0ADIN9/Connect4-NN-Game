package org.example.data_processing;

import java.io.*;

public class SolverC4 {
    private Process process;
    private BufferedReader reader;
    private BufferedWriter writer;

    public SolverC4() {
        try {
            ProcessBuilder pb = new ProcessBuilder("./c4solver");
            pb.directory(new File("src/main/resources/saved_models/optimalConnectFourData.txt"));
            pb.redirectErrorStream(true);
            process = pb.start();
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to start c4solver. Have you built it in tools/connect4 ?");
        }
    }

    public int getScore(String sequence) {
        if (sequence.isEmpty()) return 0;
        try {
            writer.write(sequence + "\n");
            writer.flush();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) return -1000;
                if (line.startsWith("Line ") && line.contains("Invalid move")) {
                    return -1000;
                }
                String expectedPrefix = sequence + " ";
                if (line.startsWith(expectedPrefix)) {
                    return Integer.parseInt(line.substring(expectedPrefix.length()).trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void close() {
        try {
            if (process != null) {
                process.destroy();
            }
        } catch (Exception e) {}
    }
}
