package pl.lukaszbilski.sort.models;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import org.apache.commons.io.FileUtils;

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


    public DataOfFile (File fileToCheck, File pathToFile){
        this.pathToFile = pathToFile.getAbsolutePath();
        this.creationDate = getDateOriginal(fileToCheck);
        this.fileName = createFileName(fileToCheck);

        editPathToFile();
        createNewSubdirectory();
        setNewFile(this.pathToFile, fileName);

//        if(isFileExist(newFile)){
//            fileName = editFileName(fileName);
//            setNewFile(this.pathToFile, fileName);
//        }
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
        try{
            if(creationDate.isPresent()){
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
                StringBuilder fileName = new StringBuilder();

                fileName.append(dateFormat.format(creationDate.get()));
                fileName.append(getExtension(fileToRename));

                return String.valueOf(fileName);
            }else {
                return fileToRename.getName();
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
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

    private void createNewSubdirectory (){
        File newDirectory = new File(pathToFile);

        if(!newDirectory.exists()){
            newDirectory.mkdirs();
        }
    }

    private void setNewFile(String path, String name) {
        newFile = new File(path + "\\" + name);
    }

    private String getExtension(File input) {
        String name = input.getName();
        int extension = name.lastIndexOf(".");

        return name.substring(extension);
    }

//    private boolean isFileExist(File file) {
//        return file.exists();
//    }
//
//    public File getNewFile () {
//        return newFile;
//    }
}
