package gpp.objects;

import java.nio.charset.StandardCharsets;

public class GitObject {
    public enum Type { blob, tree, commit }

    private final Type type;
    private final byte[] data;

    public GitObject(Type type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public Type getType() { return type; }
    public byte[] getData() { return data; }

    // THis will format the object as "type size\0content"
    public byte[] serialize() {
        String header = type.name() + " " + data.length + "\0";
        byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);
        
        byte[] result = new byte[headerBytes.length + data.length];
        System.arraycopy(headerBytes, 0, result, 0, headerBytes.length);
        System.arraycopy(data, 0, result, headerBytes.length, data.length);
        
        return result;
    }
}