import java.awt.Desktop; // Importing the Desktop class
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class VirtualClassroom {
    private static final String[] classroomCodes = {"CLASS101", "CLASS102", "CLASS103", "CLASS104"};
    private static final String UPLOAD_DIR = "uploads"; // Directory to store uploaded files

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Welcome to the Virtual Classroom");
        
        // Ask if the user is a teacher or a student
        System.out.print("Are you a Teacher (T) or Student (S)? ");
        String userType = scanner.nextLine().trim().toUpperCase();

        if (userType.equals("T")) {
            teacherMenu(scanner);
        } else if (userType.equals("S")) {
            studentMenu(scanner);
        } else {
            System.out.println("Invalid input. Please restart the program.");
        }
        
        scanner.close();
    }

    private static void teacherMenu(Scanner scanner) {
        System.out.println("\n--- Teacher Menu ---");
        System.out.println("1. Upload Content");
        System.out.println("2. Start Video Call");
        System.out.println("3. Upload Content and Start Video Call");
        System.out.println("4. Exit");

        System.out.print("Select an option: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (option) {
            case 1:
                System.out.print("Enter the path of the file to upload: ");
                String uploadPath = scanner.nextLine().trim();
                uploadContent(uploadPath);
                break;
            case 2:
                System.out.print("Enter Classroom Code to start video call: ");
                String classCode = scanner.nextLine().trim();
                startVideoCall(classCode);
                break;
            case 3:
                System.out.print("Enter Classroom Code to start video call: ");
                classCode = scanner.nextLine().trim();
                System.out.print("Enter the path of the file to upload: ");
                uploadPath = scanner.nextLine().trim();
                uploadContent(uploadPath);
                startVideoCall(classCode);
                break;
            case 4:
                System.out.println("Exiting the application.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    private static void studentMenu(Scanner scanner) {
        System.out.println("\n--- Student Menu ---");
        System.out.println("1. View Uploaded Content");
        System.out.println("2. Join a Video Call");
        System.out.println("3. View Content and Join Video Call");
        System.out.println("4. Exit");

        System.out.print("Select an option: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (option) {
            case 1:
                System.out.println("\nFiles uploaded:");
                listUploadedFiles(scanner);
                break;
            case 2:
                System.out.print("Enter Classroom Code to join: ");
                String selectedCode = scanner.nextLine().trim();
                if (isValidCode(selectedCode)) {
                    startVideoCall(selectedCode);
                } else {
                    System.out.println("Invalid classroom code. Please try again.");
                }
                break;
            case 3:
                System.out.println("\nFiles uploaded:");
                listUploadedFiles(scanner);
                System.out.print("Enter Classroom Code to join: ");
                String code = scanner.nextLine().trim();
                if (isValidCode(code)) {
                    startVideoCall(code);
                } else {
                    System.out.println("Invalid classroom code. Please try again.");
                }
                break;
            case 4:
                System.out.println("Exiting the application.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    // Validate if the classroom code is valid
    private static boolean isValidCode(String code) {
        for (String validCode : classroomCodes) {
            if (validCode.equalsIgnoreCase(code)) {
                return true;
            }
        }
        return false;
    }

    // Method to start the video call by calling the Python script
    private static void startVideoCall(String classCode) {
        try {
            ProcessBuilder pb = new ProcessBuilder("python", "video_call.py", classCode);
            pb.inheritIO(); // Show Python script output in the console
            pb.start();
        } catch (Exception ex) {
            System.out.println("Error starting video call: " + ex.getMessage());
        }
    }

    // Method to upload content
    private static void uploadContent(String filePath) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory()) {
            // Move the file to the uploads directory
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdir(); // Create uploads directory if it doesn't exist
            }
            File uploadedFile = new File(uploadDir, file.getName());
            if (file.renameTo(uploadedFile)) {
                System.out.println("Uploaded: " + uploadedFile.getAbsolutePath());
            } else {
                System.out.println("Failed to upload the file.");
            }
        } else {
            System.out.println("File not found. Please check the path and try again.");
        }
    }

    // Method to list uploaded files
    private static void listUploadedFiles(Scanner scanner) {
        File uploadDir = new File(UPLOAD_DIR);
        File[] files = uploadDir.listFiles();
        if (files != null && files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }
            System.out.print("Enter the file number to open or 0 to return: ");
            int fileChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (fileChoice > 0 && fileChoice <= files.length) {
                openFile(files[fileChoice - 1]);
            } else {
                System.out.println("Returning to the student menu.");
            }
        } else {
            System.out.println("No files uploaded yet.");
        }
    }

    // Method to open a file
    private static void openFile(File file) {
        try {
            // Use the default program to open the file
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(file);
                System.out.println("Opening file: " + file.getName());
            } else {
                System.out.println("Desktop is not supported. Unable to open the file.");
            }
        } catch (IOException e) {
            System.out.println("Error opening the file: " + e.getMessage());
        }
    }
}
