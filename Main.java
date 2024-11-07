package islam;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.util.Base64;
import java.util.Scanner;

public class Main {



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Ask for the file path and key
        System.out.print("Enter the file path: ");
        String filePath = scanner.nextLine();
        System.out.print("Enter the key (16 characters for AES): ");
        String key = scanner.nextLine();

        File inputFile = new File(filePath);
        if (!inputFile.exists()) {
            System.out.println("File does not exist.");
            return;
        }


        System.out.print("Do you want to (E)ncrypt or (D)ecrypt? ");
        String choice = scanner.nextLine().toUpperCase();

        try {
            if (choice.equals("E")) {
                encrypt(key, inputFile);
//                System.out.println("File encrypted successfully.");
            } else if (choice.equals("D")) {
                decrypt(key, inputFile);
//                System.out.println("File decrypted successfully.");
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }

    public static void encrypt(String key, File inputFile) throws Exception {
        SecretKey secretKey = getKeyFromPassword(key);

        // Read file content
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line).append("\n");
        }
        reader.close();

        // Encrypt the file content
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(content.toString().getBytes());

        // Create the output file by appending ".txt" to the original file name
        File outputFile = new File(inputFile.getParent(), inputFile.getName()+".enc");

        // Write encrypted content to output file (Base64 encoded to make it readable)
        String encryptedContent = Base64.getEncoder().encodeToString(encryptedBytes);
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(encryptedContent);
        writer.close();

        System.out.println("File content encrypted successfully!");
        File inputFile1 = inputFile;
        // Delete the original input file
        if (inputFile1.delete()) {
            System.out.println("Original file deleted successfully!");
        } else {
            System.out.println("Failed to delete the original file.");
        }
    }


    public static void decrypt(String key, File inputFile) throws Exception {
        SecretKey secretKey;
        try {
            secretKey = getKeyFromPassword(key);
        } catch (Exception e) {
            System.out.println("Error retrieving secret key: " + e.getMessage());
            return; // Exit if key retrieval fails
        }
        // Read the encrypted file content
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        StringBuilder encryptedContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            encryptedContent.append(line);
        }
        reader.close();

        // Decrypt the content
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedContent.toString()));

        // Create the output file by removing ".enc" from the input file name
        String originalFileName = inputFile.getName().replace(".enc", "");
        File outputFile = new File(inputFile.getParent(),originalFileName );

        // Write decrypted content to the output file
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
        writer.write(new String(decryptedBytes));
        writer.close();

        System.out.println("File content decrypted successfully!");

        // Delete the encrypted input file
        File inputFile1 = inputFile;
        if (inputFile1.delete()) {
            System.out.println("Encrypted file deleted successfully!");
        } else {
            System.out.println("Failed to delete the encrypted file.");
        }
    }
    private static SecretKey getKeyFromPassword(String password) throws Exception {
        // Ensure the key is exactly 16 bytes (128 bits)
        byte[] key = password.getBytes("UTF-8");
        if (key.length != 16) {
            // Resize the key array to 16 bytes (if too long, truncate; if too short, pad with zeros)
            key = java.util.Arrays.copyOf(key, 16);
        }
        return new SecretKeySpec(key, "AES");
    }

}
