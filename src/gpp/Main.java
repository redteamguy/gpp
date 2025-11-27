package gpp;

import gpp.commands.*;
import java.util.HashMap;
import java.util.Map;

public class Main {
    
    // Known / Available commands
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("init", new Init());
        commands.put("hash-object", new HashObject());
        commands.put("cat-file", new CatFile());
        // Future: commands.put("add", new Add());
        // Future: commands.put("commit", new Commit());
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: gitpp <command>");
            return;
        }

        String commandName = args[0];
        Command command = commands.get(commandName);

        if (command != null) {
            command.execute(args);
        } else {
            System.out.println("Unknown command: " + commandName);
        }
    }
}