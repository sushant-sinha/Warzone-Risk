package com.APP.Project;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserInterface.UserInterfaceClass;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The execution of the game will start from this class
 *
 * @author Jayati Thakkar
 * @version 1.0
 */
public class Main {
    /**
     * If the user is playing, it will return false; true otherwise
     */
    private volatile boolean d_IsRunning = true;

    /**
     * an instance of the user interface class
     */
    private UserInterfaceClass d_CommandLineInterface;

    /**
     * an instance of the user core logic class
     */
    private UserCoreLogic d_VirtualMachine;

    /**
     * Default constructor
     */
    public Main() {
        // Creates interface for user interaction.
        // Just a local variable as the instance is not being used/shared with any other class.
        d_CommandLineInterface = new UserInterfaceClass(this);

        // Starts the runtime engine for the game.
        // Virtual Machine will have the UI middleware.
        d_VirtualMachine = UserCoreLogic.newInstance();

        // Attaches the UserInterface (stub) to UserCoreLogic.
        d_VirtualMachine.attachUIMiddleware(d_CommandLineInterface);
    }

    /**
     * main method
     * @param args arguments
     * @throws InterruptedException thrown if it is interrupted by another thread.
     */
    public static void main(String[] args) throws InterruptedException {
        Main l_application = new Main();

        // Sets the environment for game.
        l_application.handleApplicationStartup();

        // Starts the UserInterface
        l_application.handleCLIStartUp();
    }

    /**
     * It returns the instance of UserCoreLogic
     * @return returns UserCoreLogic instance
     */
    public UserCoreLogic VIRTUAL_MACHINE() {
        return d_VirtualMachine;
    }
    /**
     * For testing purposes.
     * Map files are restored to the user data directory location.
     * Saves the files to the user's location.
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    public void restoreMapFiles() throws IOException, URISyntaxException {
        // Download the files at user data directory.
        Path l_sourceMapFiles = Paths.get(Objects.requireNonNull(Main.class.getClassLoader().getResource("map_files")).toURI());
        Path l_userDataDirectory = FindFilePathUtil.getUserDataDirectoryPath();
        Files.walk(l_sourceMapFiles)
                .forEach(source -> FileValidationUtil.copy(source, l_userDataDirectory.resolve(l_sourceMapFiles.relativize(source))));
    }

    /**
     * sets d_isRunning False if user is playing; true otherwise
     *
     * @param p_isRunning false if the user is interacting; true otherwise
     */
    public void setIsRunning(boolean p_isRunning) {
        d_IsRunning = p_isRunning;
    }

    /**
     * it handles the startup of the game engine
     */
    public void handleApplicationStartup() {
        setIsRunning(true);
    }

    /**
     * it handles the startup for the User Interface
     * @throws InterruptedException
     */
    public void handleCLIStartUp() throws InterruptedException {
        d_CommandLineInterface.d_thread.start();
        // Wait till the game is over.
        d_CommandLineInterface.d_thread.join();
    }

    /**
     * sets the value of the variable if the game is running
     * @return returns false if the user is interacting; true otherwise
     */
    public boolean isRunning() {
        return d_IsRunning;
    }


}
