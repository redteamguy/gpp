# GitPlusPlus 🚀

> A lightweight Java implementation of Git's core object storage system

**GitPlusPlus** (or `gitpp`) is an educational Java project that reimplements essential Git plumbing commands. It demonstrates how Git stores objects, computes hashes, compresses data, and manages its object database—all in pure Java without external dependencies.

## 📋 Table of Contents

- [Features](#-features)
- [Prerequisites](#-prerequisites)
- [Installation](#-installation)
- [Usage](#-usage)
- [How It Works](#-how-it-works)
- [Project Structure](#-project-structure)
- [Architecture](#-architecture)
- [Examples](#-examples)
- [Roadmap](#-roadmap)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

| Command | Description |
|---------|-------------|
| `gitpp init` | Initializes a new `.gitpp` repository with the necessary directory structure |
| `gitpp hash-object <file>` | Computes the SHA-256 hash of a file, stores it as a blob object, and returns the object ID |
| `gitpp cat-file -p <hash>` | Retrieves and displays the content of a stored object by its hash |

### What Makes This Special?

- 🎓 **Educational**: Clean, readable code that teaches Git internals
- 🔧 **Pure Java**: No external dependencies—just standard Java libraries
- 📦 **Object Storage**: Implements Git's object serialization, compression, and storage format
- 🔐 **SHA-256 Hashing**: Uses SHA-256 for object identification (can be swapped to SHA-1)
- 🗜️ **Zlib Compression**: Compresses objects using Java's built-in deflate/inflate streams

## 📦 Prerequisites

- **Java 11 or higher** (JDK required for compilation)
- **Unix-like shell** (bash, zsh) or Windows PowerShell
- Basic familiarity with command-line tools

## 🚀 Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/gitPlusPlus.git
   cd gitPlusPlus
   ```

2. **Compile the project**
   ```bash
   javac -d out $(find src -name "*.java")
   ```

   On Windows (PowerShell):
   ```powershell
   Get-ChildItem -Path src -Recurse -Filter *.java | ForEach-Object { javac -d out $_.FullName }
   ```

3. **Verify installation**
   ```bash
   java -cp out gpp.Main
   ```
   You should see: `Usage: gitpp <command>`

## 💻 Usage

### Basic Workflow

```bash
# 1. Initialize a repository
java -cp out gpp.Main init
# Output: Initialized empty Gitpp repository in /path/to/.gitpp

# 2. Store a file and get its hash
java -cp out gpp.Main hash-object test.txt
# Output: a1b2c3d4e5f6... (SHA-256 hash)

# 3. Retrieve and display the file content
java -cp out gpp.Main cat-file -p a1b2c3d4e5f6...
# Output: (file contents)
```

### Complete Example

```bash
# Create a test file
echo "Hello, GitPlusPlus!" > hello.txt

# Initialize repository
java -cp out gpp.Main init

# Store the file
HASH=$(java -cp out gpp.Main hash-object hello.txt)
echo "Stored with hash: $HASH"

# Retrieve the file
java -cp out gpp.Main cat-file -p $HASH
# Output: Hello, GitPlusPlus!
```

## 🔍 How It Works

### Object Storage Lifecycle

```
┌─────────────┐
│  File Data  │
└──────┬──────┘
       │
       ▼
┌─────────────────────┐
│  Serialize          │  Format: "blob <size>\0<data>"
│  GitObject          │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│  Hash (SHA-256)     │  Generate unique object ID
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│  Compress (Zlib)    │  Reduce storage size
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│  Shard & Store      │  .gitpp/objects/ab/cdef...
└─────────────────────┘
```

### Key Concepts

1. **Serialization**: Objects are formatted as `type size\0content` (e.g., `blob 18\0Hello, GitPlusPlus!`)
2. **Hashing**: SHA-256 hash of the serialized bytes becomes the object ID
3. **Compression**: Objects are compressed using zlib (deflate algorithm) before storage
4. **Sharding**: Object IDs are split into directories (first 2 chars) to prevent filesystem issues
5. **Retrieval**: Objects are located by hash, decompressed, and parsed back into `GitObject` instances

### Repository Structure

After running `gitpp init`, your directory will look like:

```
.gitpp/
├── HEAD              # Points to refs/heads/main
├── objects/          # Object storage
│   ├── ab/
│   │   └── cdef...   # Compressed object files
│   └── ...
└── refs/
    └── heads/        # Branch references (for future use)
```

## 📁 Project Structure

```
gitPlusPlus/
├── src/
│   └── gpp/
│       ├── Main.java                 # Entry point & command dispatcher
│       ├── commands/
│       │   ├── Command.java          # Command interface
│       │   ├── Init.java             # Repository initialization
│       │   ├── HashObject.java       # Object hashing & storage
│       │   └── CatFile.java          # Object retrieval & display
│       ├── objects/
│       │   ├── GitObject.java        # Object representation & serialization
│       │   └── ObjectStore.java      # Object storage & retrieval logic
│       └── utils/
│           └── Utils.java            # File I/O, hashing, compression utilities
├── out/                              # Compiled classes (generated)
└── README.md
```

## 🏗️ Architecture

### Layer Breakdown

| Layer | Components | Responsibility |
|-------|-----------|----------------|
| **CLI** | `Main.java` | Command registration, argument parsing, dispatch |
| **Commands** | `Init`, `HashObject`, `CatFile` | Command-specific logic and validation |
| **Objects** | `GitObject`, `ObjectStore` | Object model, serialization, storage operations |
| **Utils** | `Utils.java` | Cross-cutting concerns (I/O, crypto, compression) |

### Design Patterns

- **Command Pattern**: Each Git command implements the `Command` interface
- **Strategy Pattern**: Different object types (blob, tree, commit) handled uniformly
- **Repository Pattern**: `ObjectStore` abstracts storage operations

## 📚 Examples

### Example 1: Storing Multiple Files

```bash
# Initialize
java -cp out gpp.Main init

# Store multiple files
HASH1=$(java -cp out gpp.Main hash-object file1.txt)
HASH2=$(java -cp out gpp.Main hash-object file2.txt)

echo "File 1 hash: $HASH1"
echo "File 2 hash: $HASH2"
```

### Example 2: Verifying Object Integrity

```bash
# Store a file
HASH=$(java -cp out gpp.Main hash-object document.txt)

# Retrieve it
java -cp out gpp.Main cat-file -p $HASH > retrieved.txt

# Compare (should be identical)
diff document.txt retrieved.txt
```

### Example 3: Inspecting Repository Contents

```bash
# List all stored objects
find .gitpp/objects -type f | wc -l  # Count objects

# View object structure
ls -R .gitpp/objects/
```

## 🗺️ Roadmap

- [ ] **Add Command**: Implement `gitpp add` for staging files
- [ ] **Commit Command**: Implement `gitpp commit` with tree objects
- [ ] **Log Command**: Implement `gitpp log` to view commit history
- [ ] **Branch Support**: Add branch creation and switching
- [ ] **SHA-1 Option**: Add option to use SHA-1 (Git's default) instead of SHA-256
- [ ] **Tree Objects**: Implement tree object serialization for directories
- [ ] **Build System**: Add Gradle or Maven build configuration
- [ ] **Unit Tests**: Add comprehensive test suite
- [ ] **JAR Packaging**: Create executable JAR file
- [ ] **Documentation**: Add Javadoc comments

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

1. **Fork the repository**
2. **Create a feature branch** (`git checkout -b feature/amazing-feature`)
3. **Make your changes** (follow existing code style)
4. **Test thoroughly**
5. **Commit your changes** (`git commit -m 'Add amazing feature'`)
6. **Push to the branch** (`git push origin feature/amazing-feature`)
7. **Open a Pull Request**

### Code Style Guidelines

- Follow Java naming conventions
- Add comments for complex logic
- Keep methods focused and small
- Handle exceptions appropriately

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Inspired by Git's elegant object model
- Built for educational purposes to understand version control internals
- Thanks to the Git community for excellent documentation

---

**Note**: This is an educational project. For production use, please use the official Git implementation.
