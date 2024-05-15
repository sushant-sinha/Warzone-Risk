package com.APP.Project.UserInterface;

import com.APP.Project.Main;
import com.APP.Project.UserInterface.mappers.UserCommandsMapper;
import com.APP.Project.UserInterface.models.UsersCommands;
import com.APP.Project.UserInterface.service.RequestsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.APP.Project.UserInterface.constants.states.UserInteractionState;
import com.APP.Project.UserInterface.exceptions.InvalidArgumentException;
import com.APP.Project.UserInterface.exceptions.InvalidCommandException;
import com.APP.Project.InterfaceCoreMiddleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class asks the user to enter the command, it is interpreted and redirected to its respective services for further processing.
 *
 * @author Jayati Thakkar
 * @version 1.0
 */
public class UserInterfaceClass implements Runnable, InterfaceCoreMiddleware {

    /**
     * used to convert user input to a form understandable by the engine
     */
    private static UserCommandsMapper d_UserCommandMapper;

    /**
     * it indicates the current state of the user
     */
    private UserInteractionState d_interactionState = UserInteractionState.WAIT;


    /**
     * returns the current user state
     *
     * @return Value of the state of user interaction
     */
    public UserInteractionState getInteractionState() {
        return d_interactionState;
    }

    /**
     * it sets the user's current state whether they are playing or waiting
     *
     * @param p_interactionState  indicates the state of the user
     */
    public void setInteractionState(UserInteractionState p_interactionState) {
        this.d_interactionState = p_interactionState;
    }

    public final Thread d_thread;

    private final Queue<UsersCommands> d_userCommandQueue = new LinkedList<>();

    private final RequestsService d_requestService;

    /**
     * It can lock the shared data
     */
    ReentrantLock d_reentrantLock = new ReentrantLock();

    /**
     * It is an instance of <code>Main</code>
     */
    private final Main d_application;

    /**
     * default constructor
     *
     * defines thread and assigns the mapper class
     */
    public UserInterfaceClass(Main p_application) {
        d_application = p_application;
        d_thread = new Thread(this);
        d_UserCommandMapper = new UserCommandsMapper();
        d_requestService = new RequestsService();
    }

    /**
     * Stream for the users to add the input
     * @param p_inputStream an object of input stream class for the user to enter the data from
     */
    public void setIn(InputStream p_inputStream) {
        System.setIn(p_inputStream);
    }

    /**
     *It waits for the user to input data
     * @return the string entered by the user
     * @throws IOException
     */
    private String waitForUserInput() throws IOException {
        // Enter data using BufferReader
        BufferedReader l_bufferedReader =
                new BufferedReader(new InputStreamReader(System.in));

        // Reading data using readLine and trim the input string
        return l_bufferedReader.readLine().trim();
    }

    /**
     * whenever thread is executed, this method is called
     */
    public void run() {
        while (d_application.isRunning()) {
            try {
                d_reentrantLock.lockInterruptibly();
                try {
                    if (this.getInteractionState() == UserInteractionState.WAIT) {
                        try {
                            String l_userInput = this.waitForUserInput();

                            // Takes user input and interprets it for further processing
                            UsersCommands l_userCommand = d_UserCommandMapper.toUserCommand(l_userInput);
                            //System.out.println(l_userCommand);

                            this.setInteractionState(UserInteractionState.IN_PROGRESS);
                            // Takes action according to command instructions.
                            d_requestService.takeAction(l_userCommand);

                            if (this.getInteractionState() == UserInteractionState.IN_PROGRESS)
                                this.setInteractionState(UserInteractionState.WAIT);
                        } catch (IOException p_e) {
                        }
                    }
                    if (!d_userCommandQueue.isEmpty()) {
                        UsersCommands l_userCommand = d_userCommandQueue.poll();
                        // Takes action according to command instructions.
                        d_requestService.takeAction(l_userCommand);
                    }
                } catch (InvalidArgumentException | InvalidCommandException p_exception) {
                    // Show exception message
                    // In Graphical User Interface, we can show different modals respective to the exception.
                    System.out.println(p_exception.getMessage());

                    if (this.getInteractionState() == UserInteractionState.IN_PROGRESS) {
                        this.setInteractionState(UserInteractionState.WAIT);
                    }
                } finally {
                    d_reentrantLock.unlock();
                }
            } catch (InterruptedException l_ignored) {
            }
        }
    }

    /**
     * it asks user to input the command in String format.
     * @param p_message message that asks user to input
     * @return
     */
    @Override
    public String askForUserInput(String p_message) {
        try {
            d_reentrantLock.lockInterruptibly();
            try {
                // Print the message if any.
                if (p_message != null && !p_message.isEmpty()) {
                    System.out.println(p_message);
                }
                ObjectMapper mapper = new ObjectMapper();
                UsersCommands l_userCommand = d_UserCommandMapper.toUserCommand(this.waitForUserInput());
                if (l_userCommand.getPredefinedUserCommand().isOrderCommand()) {
                    return mapper.writeValueAsString(l_userCommand);
                } else if (l_userCommand.getPredefinedUserCommand().isGameEngineCommand()) {
                    d_userCommandQueue.add(l_userCommand);
                } else {
                    this.stderr("Invalid command!");
                }
                return "";
            } catch (IOException p_ioException) {
                return "";
            } catch (InvalidCommandException | InvalidArgumentException p_exception) {
                this.stderr(p_exception.getMessage());
                return "";
            }
        } catch (InterruptedException p_e) {
            return "";
        } finally {
            d_reentrantLock.unlock();
        }
    }

    /**
     *
     * @param p_message this is the message to print
     */
    public void stdout(String p_message) {
        if (p_message.equals("GAME_ENGINE_STARTED")) {
            d_thread.interrupt();
            this.setInteractionState(UserInteractionState.GAME_ENGINE);
        } else if (p_message.equals("GAME_ENGINE_STOPPED")) {
            d_thread.interrupt();
            this.setInteractionState(UserInteractionState.WAIT);
        } else {
            System.out.println(p_message);
        }
    }

    /**
     *
     * @param p_message the message that is to be shown to the user
     */

    public void stderr(String p_message) {
        System.out.println(p_message);
    }
}
