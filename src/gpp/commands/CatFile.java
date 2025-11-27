package gpp.commands;

import gpp.objects.GitObject;
import gpp.objects.ObjectStore;
import java.nio.charset.StandardCharsets;

public class CatFile implements Command {
    @Override
    public void execute(String[] args) {
        if (args.length < 3 || !args[1].equals("-p")) {
            System.out.println("Usage: gitpp cat-file -p <hash>");
            return;
        }

        String hash = args[2];
        try {
            ObjectStore store = new ObjectStore();
            GitObject obj = store.read(hash);
            System.out.print(new String(obj.getData(), StandardCharsets.UTF_8)); // Assuming text [ WIll support other types later ]

        } catch (Exception e) {
            System.err.println("Error reading object: " + e.getMessage());
        }
    }
}