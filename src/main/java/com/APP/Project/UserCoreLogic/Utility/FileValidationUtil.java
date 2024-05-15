package com.APP.Project.UserCoreLogic.Utility;

import com.APP.Project.UserCoreLogic.constants.enums.FileType;
import com.APP.Project.UserCoreLogic.exceptions.InvalidInputException;
import com.APP.Project.UserCoreLogic.exceptions.ResourceNotFoundException;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * This class facilitates common file operations(copying file,creating new file if does not exists, retrieving existing file etc..), offering a suite of services for managing file I/O tasks.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public class FileValidationUtil {
    private static final String MAP_FILE_EXTENSION = "map";

    private static final String GAME_EXTENSION = "warzone";

    /**
     * Gets the value of the valid available file extension.
     *
     * @return Value of the file extension.
     */
    public static String getFileExtension() {
        return MAP_FILE_EXTENSION;
    }

    /**
     * Validate whether the given map file name is valid or not and correspondingly retrieve it. Uses overloading method
     *
     * @param p_filePath Value of the path to game file.
     * @return Value of File object for the file given with path.
     * @throws InvalidInputException     Throws if the file does not exist or path invalid.
     * @throws ResourceNotFoundException Throws if file can not be created or accessed.
     */
    public static File retrieveMapFile(String p_filePath) throws ResourceNotFoundException, InvalidInputException {
        return retrieveFile(p_filePath, FileType.MAP);
    }

    /**
     * Checks whether the given game file name is valid or not. Uses overloading method
     * <code>FileUtil#retrieveFile(String, FileType)</code>.
     *
     * @param p_filePath Value of the path to to the game file being verified and retrieved.
     * @return Value of File object for the file given with path.
     * @throws InvalidInputException     Throws if the file does not exist, indicating invalid path or filename.
     * @throws ResourceNotFoundException Throws if file can not be created or accessed, indicating potential issue with file permission or the existence of the file.
     */
    public static File retrieveGameFile(String p_filePath) throws ResourceNotFoundException, InvalidInputException {
        return retrieveFile(p_filePath, FileType.GAME);
    }

    /**
     * Retrieves the file using file type and its name.
     *
     * @param p_filePath Value of the path to the game file to be retrieved.
     * @param p_fileType Type of the file.
     * @return Value of File object for the file given with path, if available and accessible.
     * @throws InvalidInputException     Throws if the file does not exist, indicating invalid path or filename.
     * @throws ResourceNotFoundException Throws if file can not be created or accessed.
     */
    public static File retrieveFile(String p_filePath, FileType p_fileType) throws ResourceNotFoundException, InvalidInputException {
        File l_file = new File(p_filePath);
        String l_fileName = l_file.getName();
        try {
            l_file.createNewFile();
        } catch (Exception p_exception) {
            throw new ResourceNotFoundException("Can not create a file due to file permission!");
        }

        if (checksIfFileHasRequiredExtension(l_fileName, p_fileType)) {
            return l_file;
        }

        throw new InvalidInputException("Invalid file!");
    }

    /**
     * Determines if the specified file matches the required extension, utilizing <code>FileType</code>
     * for identifying the file's category. This method checks the file's extension against the expected type
     * defined by <code>FileType</code>.
     *
     * @param p_fileName Name of the file being checked.
     * @param p_fileType The expected type of the file, used to verify the file's extension.
     * @return True the file's extension aligns with the required FileType; otherwise false.
     * @throws InvalidInputException Throws If the provided filename is deemed invalid.
     */
    public static boolean checksIfFileHasRequiredExtension(String p_fileName, FileType p_fileType) throws InvalidInputException {
        int l_index = p_fileName.lastIndexOf('.');
        if (l_index > 0) {
            char l_prevChar = p_fileName.charAt(l_index - 1);
            if (l_prevChar != '.') {
                String l_extension = p_fileName.substring(l_index + 1);
                if (p_fileType == FileType.MAP &&
                        l_extension.equalsIgnoreCase(FileValidationUtil.getFileExtension())) {
                    return true;
                } else if (p_fileType == FileType.GAME &&
                        l_extension.equalsIgnoreCase(FileValidationUtil.getGameExtension())) {
                    return true;
                }
                throw new InvalidInputException("File doesn't exist!");
            }
        }
        throw new InvalidInputException("File must have an extension!");
    }

    /**
     * Ensures the existence of a file at the specified path by creating it if it does not already exist.
     * This method attempts to establish a new file based on the provided path and returns a File object.
     *
     * @param p_filePath The path where the file is to be created
     * @return A File object corresponding to the newly created or existing file at the specified path.
     * @throws ResourceNotFoundException If the file cannot be found or created, likely due to issues with the path or file system permissions.
     */
    public static File createFileIfNotExists(String p_filePath) throws ResourceNotFoundException {
        File l_file = new File(p_filePath);
        try {
            l_file.createNewFile();
        } catch (Exception p_exception) {
            throw new ResourceNotFoundException("File can not be created!");
        }
        return l_file;
    }

    /**
     * Verifies the existence of a file represented by the given file object.
     *
     * @param p_fileObject The file object to check for existence.
     * @return True if the file exists, indicating a successful check.
     * @throws ResourceNotFoundException If the file does not exist, indicating the file object points to an invalid or inaccessible file.
     */
    private static boolean checkIfFileExists(File p_fileObject) throws ResourceNotFoundException {
        if (!p_fileObject.exists()) {
            throw new ResourceNotFoundException("File doesn't exist!");
        }
        return true;
    }

    /**
     * Copies a file from a source path to a specified destination path. If a file already exists at the destination path,
     * it will be overwritten by the file from the source path. This method ensures that the exact content of the source file
     * is replicated at the destination, making it suitable for file backup or duplication tasks.
     *
     * @param p_source The path of the file to be copied.
     * @param p_dest   The path where the file should be copied to.
     */
    public static void copy(Path p_source, Path p_dest) {
        try {
            // Ignore if the file already exists.
            if (!(new File(p_dest.toUri().getPath()).exists())) {
                Files.copy(p_source, p_dest, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (Exception l_ignored) {
            // Ignore the exception while copying.
        }
    }

    /**
     * Gets the game extension.
     *
     * @return Value of the game extension.
     */
    public static String getGameExtension() {
        return GAME_EXTENSION;
    }
}
