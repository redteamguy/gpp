package gpp.objects;

import gpp.utils.Utils;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public class ObjectStore {
    
    private final Path objectsDir;

    public ObjectStore() {
        this.objectsDir = Paths.get(Utils.ROOT_DIR).resolve("objects");
    }

    // Writing the object to storage
    public String write(GitObject obj) throws IOException {
        // 1. Formatting the object as "type size\0content"
        byte[] content = obj.serialize();
        
        // Hashing the object 
        String hash = Utils.sha256(content);
        
        // Compressing 
        byte[] compressed = Utils.compress(content);

        // Path Determination [ Sharding ]
        String dirName = hash.substring(0, 2);
        String fileName = hash.substring(2);
        Path dirPath = objectsDir.resolve(dirName);
        Path filePath = dirPath.resolve(fileName);

        // Writing the changes 
        Utils.makeDir(dirPath);
        Files.write(filePath, compressed);

        return hash;
    }

    // Reading the object from storage 
    public GitObject read(String hash) throws IOException {
        String dirName = hash.substring(0, 2);
        String fileName = hash.substring(2);
        Path filePath = objectsDir.resolve(dirName).resolve(fileName);

        if (!Files.exists(filePath)) {
            throw new IOException("Object not found: " + hash);
        }

        // Decompressing the object 
        byte[] fileContent = Files.readAllBytes(filePath);
        byte[] decompressed = Utils.decompress(fileContent);

        // Parsing the header [Finding the null byte]
        int nullIndex = -1;
        for (int i = 0; i < decompressed.length; i++) {
            if (decompressed[i] == 0) {
                nullIndex = i;
                break;
            }
        }

        if (nullIndex == -1) throw new IOException("Invalid object format");

        // Extraction 
        String header = new String(Arrays.copyOfRange(decompressed, 0, nullIndex));
        byte[] data = Arrays.copyOfRange(decompressed, nullIndex + 1, decompressed.length);

        String typeStr = header.split(" ")[0];
        return new GitObject(GitObject.Type.valueOf(typeStr), data);
    }
}