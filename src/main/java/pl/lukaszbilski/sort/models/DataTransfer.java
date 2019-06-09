package pl.lukaszbilski.sort.models;

import javafx.concurrent.Task;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DataTransfer extends Task{
    private File sourceDirectory;
    static File finalDirectoryByUser;
    private double sizeOfDirectory;
    private double currentSize;
    private List<File> listOfPhotos = new ArrayList<>();

    /**
     * Class constructor.
     */
    public DataTransfer(String sourcePath, String finalPath) {
        sourceDirectory = new File(sourcePath);
        finalDirectoryByUser = new File(finalPath);
        sizeOfDirectory = convertBytesToMegaBytes(FileUtils.sizeOfDirectory(sourceDirectory));
    }

    @Override
    protected Object call(){
        updateMessage("Tworzenie listy plików");
        createListOfFiles(sourceDirectory);

        for (File fileToMove: listOfPhotos){
            currentSize += convertBytesToMegaBytes(FileUtils.sizeOf(fileToMove));
            DataOfFile dataOfFile = new DataOfFile(fileToMove, finalDirectoryByUser);

            moveFile(fileToMove.getAbsolutePath(), dataOfFile.getNewFile().getAbsolutePath());

            updateMessage("Pozostało: " + remaingSize(sizeOfDirectory,currentSize) + " MB");
            updateProgress(currentSize,sizeOfDirectory);
        }
        return null;
    }
    /**
     * Create list of files to check.
     * @param file is source directory
     */
    private void createListOfFiles(File file) {
        File[] mainDirectory = file.listFiles();

        System.out.println(mainDirectory);

        if (mainDirectory != null) {
            for(File candidate: mainDirectory){
                if (candidate.isFile()){
                    listOfPhotos.add(candidate);
                }else if (candidate.isDirectory()){
                    createListOfFiles(candidate);
                }
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

    /**
     * Method calculates difference between processed and total size of files.
     * @param currentSize is a size of processed files.
     * @param totalSize is a size of source directory.
     * @return size in MB remaining files in String format
     */
    private String remaingSize(double currentSize, double totalSize){
        return String.format("%.2f", currentSize - totalSize);
    }

    private void moveFile(String pathFrom, String pathTo){
        try {
            Files.move(Paths.get(pathFrom), Paths.get(pathTo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
