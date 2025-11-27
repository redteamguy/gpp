package gpp.commands;
import gpp.objects.GitObject;
import gpp.objects.ObjectStore;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HashObject implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: gitpp hash-object <file>");
            return;
        }
        String fileName = args[1];
        try {
            // read then create blob object and then save it to object store 
            byte[] data = Files.readAllBytes(Paths.get(fileName));
            GitObject blob = new GitObject(GitObject.Type.blob, data);

            ObjectStore store = new ObjectStore();
            String hash = store.write(blob);
            
            System.out.println(hash);

        } catch (Exception e) {
            System.err.println("Error hashing object: " + e.getMessage());
        }
    }
}