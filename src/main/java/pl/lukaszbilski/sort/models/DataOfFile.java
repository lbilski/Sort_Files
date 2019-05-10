package pl.lukaszbilski.sort.models;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.TimeZone;

public class DataOfFile {
    private String nameOfFile;
    private File pathToFile;
    private File finalFile;
    private Optional<Date> creationDate;

    final String [] MONTHS = {"Styczen", "Luty", "Marzec", "Kwiecien", "Maj", "Czerwiec", "Lipiec", "Sierpien", "Wrzesien", "Pazdziernik", "Listopad", "Grudzien"};


    public DataOfFile (File fileToCheck, File pathToFile){
        this.pathToFile = pathToFile;
        this.creationDate = getDateOriginal(fileToCheck);
        this.nameOfFile = createFileName(fileToCheck);
        System.out.println(nameOfFile);
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
        if(creationDate.isPresent()){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
            StringBuilder fileName = new StringBuilder();

            fileName.append(dateFormat.format(creationDate.get()));
            fileName.append(getExtension(fileToRename));

            return String.valueOf(fileName);
        }else {
            return fileToRename.getName();
        }
    }

    private String getExtension(File input) {
        String name = input.getName();
        int extension = name.lastIndexOf(".");

        return name.substring(extension);
    }
}
