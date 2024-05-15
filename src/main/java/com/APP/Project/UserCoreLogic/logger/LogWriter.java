package com.APP.Project.UserCoreLogic.logger;

import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;
import com.APP.Project.UserCoreLogic.exceptions.ResourceNotFoundException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;


/**
 * LogWriter class writes log messages to a log file.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 1.0
 */
public class LogWriter extends Observer {
    /**
     * The target log file to write log messages.
     */
    private final File d_targetFile;

    /**
     * Constructs a LogWriter object.
     *
     * @param p_observable The observable object to observe for changes.
     * @throws ResourceNotFoundException if the log file path cannot be resolved.
     */
    public LogWriter(Observable p_observable) throws ResourceNotFoundException {
        super(p_observable);
        String l_timestamp = String.valueOf(new Date().getTime());
        String l_FileName = l_timestamp.concat("_log_file.log");
        String l_pathToFile = FindFilePathUtil.resolveLogPath(l_FileName);
        d_targetFile = FileValidationUtil.createFileIfNotExists(l_pathToFile);
    }

    /**
     * Updates the log file with the latest log message.
     *
     * @param p_observable The observable object that triggered the update.
     */
    @Override
    public void update(Observable p_observable) {
        if (d_targetFile == null) {
            return;
        }
        String l_message = ((LogEntryBuffer) p_observable).getMessage();
        try (Writer l_writer = new FileWriter(d_targetFile, true)) {
            l_writer.append(l_message);
        } catch (IOException p_ioException) {
        }
    }
}