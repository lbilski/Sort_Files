package pl.lukaszbilski.sort.models;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

public class DataOfFile {
    private String fileName;
    private String pathToFile;
    private File newFile;
    private Optional<Date> creationDate;

    final String [] MONTHS = {"Styczen", "Luty", "Marzec", "Kwiecien", "Maj", "Czerwiec", "Lipiec", "Sierpien", "Wrzesien", "Pazdziernik", "Listopad", "Grudzien"};


    DataOfFile (File fileToCheck, File pathToFile){
        this.pathToFile = pathToFile.getAbsolutePath();
        this.creationDate = getDateOriginal(fileToCheck);
        this.fileName = createFileName(fileToCheck);

        editPathToFile();

        createNewSubdirectory();
        setNewFile(this.pathToFile, fileName);

        while(newFile.exists()){
            fileName = addNextNumberToName(fileName);
            setNewFile(this.pathToFile, this.fileName);
        }
    }

    private Optional<Date> getDateOriginal(File input) {
        try {
            Metadata readMetadata = ImageMetadataReader.readMetadata(input);
            Date dateFromMetadata = readMetadata
                    .getFirstDirectoryOfType(ExifSubIFDDirectory.class)
                    .getDateOriginal(TimeZone.getTimeZone(ZoneId.systemDefault()));

            return Optional.of(dateFromMetadata);

        } catch (ImageProcessingException | IOException | NullPointerException e) {
            return Optional.empty();
        }
    }

    private String createFileName(File fileToRename) {
        StringBuilder fileName = new StringBuilder();
        try{
            if(creationDate.isPresent()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");

                fileName.append(dateFormat.format(creationDate.get()));
                fileName.append("_0");
                fileName.append(getExtension(fileToRename.getName()));

            }else {
                fileName.append(FilenameUtils.removeExtension(fileToRename.getName()));
                fileName.append("_0");
                fileName.append(getExtension(fileToRename.getName()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return fileName.toString();
    }

    private void editPathToFile(){
        if(creationDate.isPresent()){
          StringBuilder newPath = new StringBuilder();

          newPath.append(pathToFile);
          newPath.append("\\");
          newPath.append(MONTHS[creationDate.get().getMonth()]);
          newPath.append("\\");
          newPath.append(creationDate.get().getDate());

          pathToFile = newPath.toString();
        } else {
          pathToFile += "\\Brak daty";
        }
    }

    private String addNextNumberToName(String input) {
        int indexOfNumber = input.indexOf(".")-1;
        int numberOfFile = Character.getNumericValue(input.charAt(indexOfNumber));

        StringBuilder newName = new StringBuilder();

        newName.append(input, 0, indexOfNumber);
        newName.append(++numberOfFile);
        newName.append(getExtension(input));

        System.out.println(newName);
        return String.valueOf(newName);
    }

    private void createNewSubdirectory (){
        File newDirectory = new File(pathToFile);

        if(!newDirectory.exists()){
            newDirectory.mkdirs();
        }
    }

    private void setNewFile(String path, String name) {
        newFile = new File(path + "\\" + name);
    }

    private String getExtension(String input) {
        int extension = input.lastIndexOf(".");

        if(extension < 0){
            return "";
        }else {
            return input.substring(extension);
        }
    }

    File getNewFile() {
        return newFile;
    }
}
