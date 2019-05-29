package client;

import customedExceptions.UnknownFileFormatException;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import utils.strUtils;

public class Client
{

    private static Socket OpenSocket(String adIP, int port)
    {
        Socket sc = null;
        try
        {
            InetAddress server = InetAddress.getByName(adIP);
            sc = new Socket(server, port);
        }
        catch (UnknownHostException ex)
        {
            System.out.println("Erreur au niveau du port ");
            return sc;
        }
        catch (IOException ex)
        {
            System.out.println("Erreur au niveau de l'ouverture du Socket");
            return sc;
        }
        return sc;
    }

    /**
     *
     * @param ipServer
     * @param port
     * @param fileName
     * @param localFilePath
     * @return int :
     * 0 : Tout s'est bien passé.
     * -1 : Problème de flux
     * -2 : erreur de fermeture de socket
     * -3 : Erreur lors de l'ouverture du socket (Methode OpenSocket())
     * -5 : Impossible de lire/ouvrir le fichier
     * -6 : Impossible d'écrire le fichier spécifié dans un flux
     */
    public static int PUT(String ipServer, int port, String fileName, String localFilePath)
    {
        byte[] fileInBytes = null;
        String fileInString = null;
        boolean isImg = strUtils.isImg(fileName);

        if (isImg)
        {
            try
            {
                File file = new File(localFilePath);
                fileInBytes = readFileToByteArray(file);
            }
            catch (Exception e)
            {
                // impossible d'ouvrir / lire le fichier
                System.out.println("Exception : Impossible de lire/ouvrir le fichier.\n" + e);
                return -5;
            }
        }
        else
        {
            try
            {
                fileInString = strUtils.readFileAsString(localFilePath);
            }
            catch (IOException ex)
            {
                // impossible d'ouvrir / lire le fichier
                System.out.println("Exception : Impossible de lire/ouvrir le fichier.\n" + ex);
                return -5;
            }
        }

        Socket sock = OpenSocket(ipServer, port);
        try
        {
            if (sock == null)
            {
                // -3 en cas d'erreur d'ouverture
                System.out.println("Erreur lors de l'ouverture du socket (Methode OpenSocket())");
                return -3;
            }
        }
        catch (NullPointerException ex)
        {
            System.out.println("Exception : " + ex);
            return -3;
        }

        try
        {
            OutputStream outputStream = sock.getOutputStream();
            // Envoyer la requête vers le serveur : "écrire sur le document 'doc.html'
            // Le serveur répond. Sa répojnse contient le document ou la raison du refus
            String url = "http://" + ipServer + ":" + port;
            String httpPUTRequest = "PUT /" + fileName + " HTTP/1.1 \r\n";
            httpPUTRequest += "Host: " + ipServer + "\r\n";
            if (isImg && fileInBytes != null)
            {
                // (+1 car on compte le retour chariot après le header Content-length)
                int length = fileInBytes.length + 1;
                httpPUTRequest += "Content-length: " + length + "\r\n\r\n";
                for (int i = 0; i < fileInBytes.length; i++)
                {
                    httpPUTRequest += fileInBytes[i];
                }
            }
            else if (!isImg && fileInString != null)
            {
                int length = fileInString.length() + 1;
                httpPUTRequest += "Content-length: " + length + "\r\n\n";
                httpPUTRequest += fileInString;
            }
            else
            {
                System.out.println("Exception : Impossible d'écrire le fichier"
                        + "spécifié dans un flux.");
                sock.close();
                return -6;
            }

            // écriture et envoi
            System.out.println(httpPUTRequest);
            outputStream.write(httpPUTRequest.getBytes());
            outputStream.flush();
        }
        catch (IOException ex)
        {
            // -1 en cas de problème de flux
            System.out.println("Erreur lors d'utilisation d'un flux "
                    + "sortant.\nRapport d'exception : " + ex);
            try
            {
                sock.close();
            }
            catch (IOException exSock)
            {
                // -2 en cas d'erreur de fermeture de socket
                System.out.println("Erreur lors de la fermeture du Socket. "
                        + "\nIP serveur : " + ipServer + "\nPort serveur : "
                        + port + " \nRapport d'exception complet : " + exSock);
                return -2;
            }
            return -1;
        }
        // Fermeture de la connexion si tout s'est bien passé
        try
        {
            sock.close();
        }
        catch (IOException ex)
        {
            // -2 en cas d'erreur de fermeture de socket
            System.out.println("Erreur lors de la fermeture du Socket. "
                    + "\nIP serveur : " + ipServer + "\nPort serveur : "
                    + port + " \nRapport d'exception complet : " + ex);
            return -2;
        }

        // Le PUT s'est bien passé. Le fichier a été écrit sur le serveur.
        return 0;
    }

    /**
     * This method uses java.io.FileInputStream to read file content into a byte
     * array
     * https://netjs.blogspot.com/2015/11/how-to-convert-file-to-byte-array-java.html
     *
     * @param file
     * @return
     */
    private static byte[] readFileToByteArray(File file)
    {
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try
        {
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();
        }
        catch (IOException ioEx)
        {
            ioEx.printStackTrace();
            System.out.println(ioEx);
        }
        return bArray;
    }

    /**
     *
     * @param ipServer
     * @param port
     * @param fileName
     * @return int :
     * 0 : Tout s'est bien passé.
     * -1 : Problème de flux
     * -2 : erreur de fermeture de socket
     * -3 : Erreur lors de l'ouverture du socket (Methode OpenSocket())
     * -5 : Impossible de lire/ouvrir le fichier
     * -6 : Impossible d'écrire le fichier spécifié dans un flux
     * -7 : Impossible d'écrire le fichier local
     */
    public static int GET(String ipServer, int port, String fileName)
    {
        byte[] fileInBytes = null;
        String fileInString = null;
        boolean isImg = strUtils.isImg(fileName);

        Socket sock = OpenSocket(ipServer, port);
        try
        {
            if (sock == null)
            {
                // -3 en cas d'erreur d'ouverture
                System.out.println("Erreur lors de l'ouverture du socket (Methode OpenSocket())");
                return -3;
            }
        }
        catch (NullPointerException ex)
        {
            System.out.println("Exception : " + ex);
            return -3;
        }

        try
        {
            OutputStream outputStream = sock.getOutputStream();

            //indata représente l’objet InputStream et l’objet inputStream permet d’utiliser la zone de stockage  (buffer).
            InputStream indata = sock.getInputStream();

            BufferedInputStream inputStream = new BufferedInputStream(indata);
            // Envoyer la requête vers le serveur : "écrire sur le document 'doc.html'
            // Le serveur répond. Sa répojnse contient le document ou la raison du refus
            // String url = "http://" + ipServer + ":" + port;
            String httpGETRequest = "GET " + fileName + " HTTP/1.1\r\n";
            httpGETRequest += "Host: " + ipServer + "\r\n\r\n";
            //httpGETRequest += "Accept : */*\r\n\r\n";

            // écriture et envoi
            outputStream.write(httpGETRequest.getBytes());
            outputStream.flush();
            System.out.println(httpGETRequest);

            //On récupère les données
            FileOutputStream fileb;
            if (isImg)
            {
                String extension = strUtils.getFileExtension(fileName);
                fileb = new FileOutputStream("reception." + extension);
            }
            else
            {
                fileb = new FileOutputStream("reception.html");
            }
            int byteCourant;
            int count = 0;
            String responseCode = "";
            boolean headerEnded = false;
            try
            {
                //recevoir tous les bytes dans un tableau
                // quand ils ont été reçus, envoyer une demande de fermeture
                // de connexion au serveur
                // lmao
                while (inputStream.available() == 0)
                {
                }

                while ((inputStream.available()) != 0)
                {

                    byteCourant = inputStream.read();
                    if (count == 9 || count == 10 || count == 11)
                    {
                        responseCode += (char) byteCourant;
                        // System.out.print(responseCode);
                    }
                    if (!headerEnded)
                    {
                        System.out.print((char) byteCourant);
                        if (byteCourant == 13)
                        {
                            int byteSuivant0 = inputStream.read();
                            if (byteSuivant0 == 10)
                            {
                                int byteSuivant1 = inputStream.read();
                                if (byteSuivant1 == 13)
                                {
                                    int byteSuivant2 = inputStream.read();
                                    if (byteSuivant2 == 10)
                                    {
                                        headerEnded = true;
                                    }
                                }
                            }
                        }
                    }
                    else if (headerEnded)
                    {
                        //System.out.print((char) byteCourant);
                        fileb.write(byteCourant);
                    }
                    count++;
                }

                indata.close();
                fileb.close();
                int responseInt = Integer.parseInt(responseCode);
                System.out.println(responseCode);
                if (responseInt != 200)
                {
                    // code d'erreur
                    _errCode = responseInt;
                    return -8;
                }
            }
            catch (Exception e)
            {
                System.out.println(e);
            }
        }
        catch (IOException ex)
        {
            // -1 en cas de problème de flux
            System.out.println("Erreur lors de l'utilisation d'un flux "
                    + "sortant.\nRapport d'exception : " + ex);
            try
            {
                sock.close();
            }
            catch (IOException exSock)
            {
                // -2 en cas d'erreur de fermeture de socket
                System.out.println("Erreur lors de la fermeture du Socket. "
                        + "\nIP serveur : " + ipServer + "\nPort serveur : "
                        + port + " \nRapport d'exception complet : " + exSock);
                return -2;
            }
            return -1;
        }

        // Fermeture de la connexion si tout s'est bien passé
        try
        {
            sock.close();
        }
        catch (IOException ex)
        {
            // -2 en cas d'erreur de fermeture de socket
            System.out.println("Erreur lors de la fermeture du Socket. "
                    + "\nIP serveur : " + ipServer + "\nPort serveur : "
                    + port + " \nRapport d'exception complet : " + ex);
            return -2;
        }

        // Le GET s'est bien passé. .
        return 0;

    }

    private static int _errCode;

    public static int getErrorCode()
    {
        return _errCode;
    }
}
