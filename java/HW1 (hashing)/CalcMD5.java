import java.security.*;
import java.io.*;
import java.nio.file.*;
import java.util.List;

public class CalcMD5 {
  public static String getHashOfFile(String filename) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");       
    } catch (NoSuchAlgorithmException ex) {
      System.out.println("MD5 algorithm not found");
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
      
    StringBuilder hash = new StringBuilder();

    for (int i = 0; i < bytes.length; i++) {
      String hex = Integer.toHexString(0xFF & bytes[i]);
      if (hex.length() == 1) {
        hash.append('0');
      }
      hash.append(hex);
    }

    return hash.toString().toUpperCase(); 
  }

  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Invalid arguments. Usage: java CalcMD5 <filename>");
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