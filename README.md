# Notes App

A simple text-based notes manager built with Java File I/O operations.

## Features

- **Create Notes**: Write multi-line text notes
- **Read Notes**: View saved notes from files
- **List All Notes**: Display all available notes with timestamps
- **Search Notes**: Find notes by title keywords
- **Delete Notes**: Remove unwanted notes
- **Export Notes**: Combine all notes into a single file

## File I/O Operations Used

- `FileWriter` - Writing notes to files
- `FileReader` - Reading note files
- `BufferedReader` - Efficient line-by-line reading

## Requirements

- Java 8 or higher
- VS Code (recommended)
- Terminal/Command Prompt

## How to Run

1. **Save the code as `NotesApp.java`**

2. **Compile the program:**
   ```bash
   javac NotesApp.java
   ```

3. **Run the program:**
   ```bash
   java NotesApp
   ```

## Usage

1. Choose option 1 to create a new note
2. Enter a title for your note
3. Type your content (type 'END' on a new line to finish)
4. Use other menu options to read, search, or manage your notes

## File Organization

The app automatically creates:
- `notes/` directory for storing all note files
- `notes_index.txt` to track all notes
- Individual `.txt` files for each note

## Example

```
=== NOTES APP ===
1. Create New Note
2. Read Note
3. List All Notes
4. Search Notes
5. Delete Note
6. Export All Notes
7. Exit
Choose an option: 1
```

## Author

Created as a demonstration of Java File I/O operations.
