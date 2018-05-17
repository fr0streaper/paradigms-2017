import java.security.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CalcSHA256 {
  public static String getHashOfFile(String filename) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("SHA-256");       
    } catch (NoSuchAlgorithmException ex) {
      System.out.println("SHA256 algorithm not found");
      return "ERROR";
    }

    byte[] fileBytes;
    try {
      fileBytes = Files.readAllBytes(Paths.get(filename));
    } catch (IOException ex) {
      System.out.println("Unable to read file \"" + filename + "\"");
      return "ERROR";
    }

    byte[] bytes = md.digest(fileBytes);

    Formatter formatter = new Formatter();
    for (byte b : bytes) {
    	formatter.format("%02x", b);
    }

    return formatter.toString(); 
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Invalid arguments. Usage: java CalcSHA256 <filename>");
      return;
    }
    
    BufferedReader in;         
    try {                                     
      in = new BufferedReader(new FileReader(args[0]));
    } catch (FileNotFoundException ex) {
      System.out.println("There is no file called \"" + args[0] + "\"");
      return;
    }

    while (true) {
      String filename;
      try {
        filename = in.readLine();
      } catch (IOException ex) {
        return;
      }

      if (filename == null) {
        break;
      }      
                                      
      System.out.println(getHashOfFile(filename));
    }
    
    try {
      in.close();
    } catch (IOException ex) {
      return;
    } 
  }
}