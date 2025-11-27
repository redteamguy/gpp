package gpp.utils;
import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class Utils {
    public static final String ROOT_DIR = ".gitpp";

    public static void makeDir(Path path) throws IOException {
        if (!Files.exists(path)) Files.createDirectories(path);
    }

    public static void writeFile(Path path, String content) throws IOException {
        Files.writeString(path, content);
    }

    // Hashing...
    public static String sha256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // New Compression Tech...
    public static byte[] compress(byte[] data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (DeflaterOutputStream dos = new DeflaterOutputStream(baos)) {
            dos.write(data);
        }
        return baos.toByteArray();
    }

    public static byte[] decompress(byte[] data) throws IOException {
        try (InflaterInputStream iis = new InflaterInputStream(new ByteArrayInputStream(data));
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = iis.read(buffer)) > 0) {
                baos.write(buffer, 0, len);
            }
            return baos.toByteArray();
        }
    }
}