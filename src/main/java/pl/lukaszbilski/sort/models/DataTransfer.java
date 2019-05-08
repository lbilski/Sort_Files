package pl.lukaszbilski.sort.models;

import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DataTransfer extends Task{
    private File sourceDirectory;
    private File finalDirectory;
    private double sizeOfDirectory;
    private final Map<Integer, String> MONTHS = new LinkedHashMap<>();
    private List<File> listOfPhotos = new ArrayList<>();
    double currentSize;

    /**
     * Class constructor.
     */
    public DataTransfer(String sourcePath, String finalPath) {
        sourceDirectory = new File(sourcePath);
        finalDirectory = new File(finalPath);
        sizeOfDirectory = convertBytesToMegaBytes(FileUtils.sizeOfDirectory(sourceDirectory));
        setMonths();
    }

    @Override
    protected Object call(){
        createListOfFiles(sourceDirectory);

        for (File child: listOfPhotos){
            currentSize += convertBytesToMegaBytes(FileUtils.sizeOf(child));
            System.out.println(child.getName());
            updateMessage("Pozostało: " + remaingSize(sizeOfDirectory,currentSize) + " MB");
            updateProgress(currentSize,sizeOfDirectory);

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    private void createListOfFiles(File file){
        File[] mainDirectory = file.listFiles();

        for(File candidate: mainDirectory){
            if (candidate.isFile()){
                listOfPhotos.add(candidate);
            }else if (candidate.isDirectory()){
                createListOfFiles(candidate);
            }
        }
    }

    /**
     * Convert Bytes to Megabytes.
     * @param bytes is size of source directory.
     * @return size of source directory in MB.
     */
    private double convertBytesToMegaBytes (long bytes){
        return (double) bytes / (1024L * 1024L);
    }

    private String remaingSize(double currentSize, double totalSize){
        return String.format("%.2f", currentSize - totalSize);
    }

    /**
     * This method set months in Map.
     */
    private void setMonths() {
        MONTHS.put(1, "Styczeń");
        MONTHS.put(2, "Luty");
        MONTHS.put(3, "Marzec");
        MONTHS.put(4, "Kwiecień");
        MONTHS.put(5, "Maj");
        MONTHS.put(6, "Czerwiec");
        MONTHS.put(7, "Lipiec");
        MONTHS.put(8, "Sierpień");
        MONTHS.put(9, "Wrzesień");
        MONTHS.put(10, "Październik");
        MONTHS.put(11, "Listopad");
        MONTHS.put(12, "Grudzień");
    }
}
