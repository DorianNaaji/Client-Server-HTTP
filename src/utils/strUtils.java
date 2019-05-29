/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import customedExceptions.UnknownFileFormatException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author Dorian
 */
public class strUtils
{

    /**
     * Returns a file extension
     *
     * @param fileName a filename
     * @return
     */
    public static String getFileExtension(String fileName)
    {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * Checks if a file is an image or note
     *
     * @param nomFichier the filename
     * @return true if the file is .png or .jpg. False if it is .txt. Exception
     * otherwise
     */
    public static boolean isImg(String nomFichier)
    {
        String extension = nomFichier.substring(nomFichier.lastIndexOf(".") + 1);

        return ("png".equals(extension) || "jpg".equals(extension));

    }

    public static String readFileAsString(String stringPath) throws IOException
    {
        return new String(Files.readAllBytes(Paths.get(stringPath)));
    }
}
