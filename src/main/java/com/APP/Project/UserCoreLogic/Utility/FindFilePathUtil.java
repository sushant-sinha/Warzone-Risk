package com.APP.Project.UserCoreLogic.Utility;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class resolves the path using the user data directory path.
 *
 * @author Bhoomiben Bhatt
 * @version 1.0
 */
public class FindFilePathUtil {
    /**
     * Singleton instance of the class.
     */
    private static FindFilePathUtil d_Instance;

    /**
     * Represents the path to the user data directory.
     */
    private final Path USER_DATA_DIRECTORY_PATH;

    /**
     * Folder from or to save/load user files.
     */
    private final String USER_DATA_DIRECTORY = "";

    /**
     * Name of the directory to save log files.
     */
    private final String USER_LOG_DIRECTORY = "War_Zone_Logs";

    /**
     * Path to the directory of logs.
     */
    private final Path USER_LOG_DIRECTORY_PATH;

    /**
     * Instance can not be created outside the class. (private)
     */
    private FindFilePathUtil() {
        USER_DATA_DIRECTORY_PATH = Paths.get(System.getProperty("user.dir"), "src","main", "resources", USER_DATA_DIRECTORY);
        USER_LOG_DIRECTORY_PATH = Paths.get(System.getProperty("user.home"), "Downloads", USER_LOG_DIRECTORY);

        // Create user directories if it doesn't exist.
        this.createDirectory(USER_DATA_DIRECTORY_PATH.toString());
        this.createDirectory(USER_LOG_DIRECTORY_PATH.toString());
    }

    /**
     * Creates the directory.
     *
     * @param p_filePath Path to the directory.
     */
    public void createDirectory(String p_filePath) {
        File l_file = new File(p_filePath);
        if (!l_file.exists()) {
            l_file.mkdir();
        }
    }

    /**
     * Gets the single instance of the class.
     *
     * @return Value of the instance.
     */
    public static FindFilePathUtil getInstance() {
        if (d_Instance == null) {
            d_Instance = new FindFilePathUtil();
        }
        return d_Instance;
    }

    /**
     * Retrieves the path to the user data directory as a string. This method provides access to the directory
     * used for storing user-specific data, ensuring applications can consistently locate and use this path for
     * data storage and retrieval.
     *
     * @return The string representation of the user data directory path.
     */
    public static Path getUserDataDirectoryPath() {
        return FindFilePathUtil.getInstance().USER_DATA_DIRECTORY_PATH;
    }

    /**
     * Gets the string value of the log directory.
     *
     * @return Value of the path.
     */
    public static Path getLogDirectoryPath() {
        return FindFilePathUtil.getInstance().USER_LOG_DIRECTORY_PATH;
    }

    /**
     * Uses the user data directory path to resolve absolute the path to the file.
     *
     * @param p_filePath The name or relative path of the file from the user data directory.
     * @return Value of the absolute path to the file.
     */
    public static String resolveFilePath(String p_filePath) {
        return Paths.get(getUserDataDirectoryPath().toString(), p_filePath).toString();
    }

    /**
     * Uses the user data directory and log folder paths to resolve absolute the path to the file.
     *
     * @param p_filePath The name or relative path of the file from the user data directory.
     * @return Value of the absolute path to the file.
     */
    public static String resolveLogPath(String p_filePath) {
        return Paths.get(getLogDirectoryPath().toString(), p_filePath).toString();
    }
}
