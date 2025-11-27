
// Initialization of .gpp
package gpp.commands;

import gpp.utils.Utils;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Init implements Command {
    @Override
    public void execute(String[] args) {
        try {
            Path root = Paths.get(".").normalize().toAbsolutePath();
            Path gitDir = root.resolve(Utils.ROOT_DIR);
            Path objectsDir = gitDir.resolve("objects");
            Path refsDir = gitDir.resolve("refs").resolve("heads");
            Path headFile = gitDir.resolve("HEAD");

            if (gitDir.toFile().exists()) {
                System.out.println("Gitpp is already initialized.");
                return;
            }

            // Creating the neccessary directories
            Utils.makeDir(gitDir);
            Utils.makeDir(objectsDir);
            Utils.makeDir(refsDir);

            // Creating HEAD
            Utils.writeFile(headFile, "ref: refs/heads/main\n");

            System.out.println("Initialized empty Gitpp repository in " + gitDir);

        } catch (Exception e) {
            System.err.println("Error initializing: " + e.getMessage());
        }
    }
}