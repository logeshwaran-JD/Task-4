import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class NotesApp {
    private static final String NOTES_DIRECTORY = "notes/";
    private static final String NOTES_INDEX_FILE = "notes/notes_index.txt";
    private Scanner scanner;
    private SimpleDateFormat dateFormat;

    public NotesApp() {
        scanner = new Scanner(System.in);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        createNotesDirectory();
    }

    
    private void createNotesDirectory() {
        File directory = new File(NOTES_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Created notes directory.");
        }
    }

  
    public void createNote() {
        System.out.print("Enter note title: ");
        String title = scanner.nextLine().trim();
        
        if (title.isEmpty()) {
            System.out.println("Title cannot be empty!");
            return;
        }

       
        String fileName = createSafeFileName(title);
        String filePath = NOTES_DIRECTORY + fileName + ".txt";

        System.out.println("Enter your note content (type 'END' on a new line to finish):");
        StringBuilder content = new StringBuilder();
        String line;
        
        while (!(line = scanner.nextLine()).equals("END")) {
            content.append(line).append("\n");
        }

        try {
          
            FileWriter writer = new FileWriter(filePath);
            writer.write("Title: " + title + "\n");
            writer.write("Created: " + dateFormat.format(new Date()) + "\n");
            writer.write("----------------------------------------\n");
            writer.write(content.toString());
            writer.close();

            // Update index file
            updateNotesIndex(title, fileName);
            
            System.out.println("Note saved successfully as: " + fileName + ".txt");
            
        } catch (IOException e) {
            System.out.println("Error saving note: " + e.getMessage());
        }
    }

 
    public void readNote() {
        List<String> notesList = getAllNotes();
        
        if (notesList.isEmpty()) {
            System.out.println("No notes found!");
            return;
        }

        System.out.println("\n=== AVAILABLE NOTES ===");
        for (int i = 0; i < notesList.size(); i++) {
            System.out.println((i + 1) + ". " + notesList.get(i));
        }

        System.out.print("Select note to read (enter number): ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice < 1 || choice > notesList.size()) {
                System.out.println("Invalid selection!");
                return;
            }

            String selectedNote = notesList.get(choice - 1);
            String[] parts = selectedNote.split(" \\| ");
            String fileName = parts[1];
            
            readNoteFromFile(NOTES_DIRECTORY + fileName + ".txt");
            
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number!");
            scanner.nextLine(); 
        }
    }

    private void readNoteFromFile(String filePath) {
        try {
            FileReader fileReader = new FileReader(filePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            System.out.println("\n" + "=".repeat(50));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            System.out.println("=".repeat(50));
            
            bufferedReader.close();
            fileReader.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Note file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading note: " + e.getMessage());
        }
    }

    public void listAllNotes() {
        List<String> notesList = getAllNotes();
        
        if (notesList.isEmpty()) {
            System.out.println("No notes found!");
            return;
        }

        System.out.println("\n=== ALL NOTES ===");
        for (int i = 0; i < notesList.size(); i++) {
            System.out.println((i + 1) + ". " + notesList.get(i));
        }
    }

    private List<String> getAllNotes() {
        List<String> notes = new ArrayList<>();
        
        try {
            File indexFile = new File(NOTES_INDEX_FILE);
            if (!indexFile.exists()) {
                return notes; 
            }

            FileReader fileReader = new FileReader(NOTES_INDEX_FILE);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    notes.add(line);
                }
            }
            
            bufferedReader.close();
            fileReader.close();
            
        } catch (IOException e) {
            System.out.println("Error reading notes index: " + e.getMessage());
        }
        
        return notes;
    }

    public void deleteNote() {
        List<String> notesList = getAllNotes();
        
        if (notesList.isEmpty()) {
            System.out.println("No notes to delete!");
            return;
        }

        System.out.println("\n=== SELECT NOTE TO DELETE ===");
        for (int i = 0; i < notesList.size(); i++) {
            System.out.println((i + 1) + ". " + notesList.get(i));
        }

        System.out.print("Select note to delete (enter number): ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice < 1 || choice > notesList.size()) {
                System.out.println("Invalid selection!");
                return;
            }

            String selectedNote = notesList.get(choice - 1);
            String[] parts = selectedNote.split(" \\| ");
            String fileName = parts[1];
            
            File noteFile = new File(NOTES_DIRECTORY + fileName + ".txt");
            if (noteFile.delete()) {
                notesList.remove(choice - 1);
                updateIndexFile(notesList);
                System.out.println("Note deleted successfully!");
            } else {
                System.out.println("Error deleting note file!");
            }
            
        } catch (InputMismatchException e) {
            System.out.println("Please enter a valid number!");
            scanner.nextLine(); 
        }
    }

    public void searchNotes() {
        System.out.print("Enter search term: ");
        String searchTerm = scanner.nextLine().toLowerCase().trim();
        
        if (searchTerm.isEmpty()) {
            System.out.println("Search term cannot be empty!");
            return;
        }

        List<String> allNotes = getAllNotes();
        List<String> matchingNotes = new ArrayList<>();
        
        for (String note : allNotes) {
            if (note.toLowerCase().contains(searchTerm)) {
                matchingNotes.add(note);
            }
        }
        
        if (matchingNotes.isEmpty()) {
            System.out.println("No notes found matching: " + searchTerm);
        } else {
            System.out.println("\n=== SEARCH RESULTS ===");
            for (int i = 0; i < matchingNotes.size(); i++) {
                System.out.println((i + 1) + ". " + matchingNotes.get(i));
            }
        }
    }

    // Update notes index file
    private void updateNotesIndex(String title, String fileName) {
        try {
            FileWriter writer = new FileWriter(NOTES_INDEX_FILE, true); // Append mode
            writer.write(title + " | " + fileName + " | " + dateFormat.format(new Date()) + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Error updating notes index: " + e.getMessage());
        }
    }

    private void updateIndexFile(List<String> notes) {
        try {
            FileWriter writer = new FileWriter(NOTES_INDEX_FILE); // Overwrite mode
            for (String note : notes) {
                writer.write(note + "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error updating index file: " + e.getMessage());
        }
    }

    private String createSafeFileName(String title) {
        String safe = title.replaceAll("[^a-zA-Z0-9\\s]", "");
        safe = safe.replaceAll("\\s+", "_");
     
        if (safe.length() > 50) {
            safe = safe.substring(0, 50);
        }
      
        long timestamp = System.currentTimeMillis();
        return safe + "_" + timestamp;
    }

    public void exportAllNotes() {
        List<String> notesList = getAllNotes();
        
        if (notesList.isEmpty()) {
            System.out.println("No notes to export!");
            return;
        }

        String exportFileName = "notes_export_" + System.currentTimeMillis() + ".txt";
        
        try {
            FileWriter writer = new FileWriter(exportFileName);
            writer.write("NOTES EXPORT - " + dateFormat.format(new Date()) + "\n");
            writer.write("=".repeat(60) + "\n\n");
            
            for (String noteInfo : notesList) {
                String[] parts = noteInfo.split(" \\| ");
                String fileName = parts[1];
                
              
                try {
                    FileReader fileReader = new FileReader(NOTES_DIRECTORY + fileName + ".txt");
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        writer.write(line + "\n");
                    }
                    writer.write("\n" + "-".repeat(40) + "\n\n");
                    
                    bufferedReader.close();
                    fileReader.close();
                    
                } catch (IOException e) {
                    writer.write("Error reading note: " + fileName + "\n\n");
                }
            }
            
            writer.close();
            System.out.println("All notes exported to: " + exportFileName);
            
        } catch (IOException e) {
            System.out.println("Error exporting notes: " + e.getMessage());
        }
    }

    public void showMenu() {
        System.out.println("\n=== NOTES APP ===");
        System.out.println("1. Create New Note");
        System.out.println("2. Read Note");
        System.out.println("3. List All Notes");
        System.out.println("4. Search Notes");
        System.out.println("5. Delete Note");
        System.out.println("6. Export All Notes");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");
    }

    public void run() {
        System.out.println("Welcome to the Notes App!");
        System.out.println("Your notes will be saved in the 'notes' directory.");
        
        while (true) {
            showMenu();
            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        createNote();
                        break;
                    case 2:
                        readNote();
                        break;
                    case 3:
                        listAllNotes();
                        break;
                    case 4:
                        searchNotes();
                        break;
                    case 5:
                        deleteNote();
                        break;
                    case 6:
                        exportAllNotes();
                        break;
                    case 7:
                        System.out.println("Thank you for using Notes App!");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
                
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                
            } catch (InputMismatchException e) {
                System.out.println("Please enter a valid number!");
                scanner.nextLine(); 
            }
        }
    }

    public static void main(String[] args) {
        NotesApp app = new NotesApp();
        app.run();
    }
}
