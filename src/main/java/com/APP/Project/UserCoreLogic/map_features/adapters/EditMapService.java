package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.constants.enums.FileType;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.Container.ContinentContainer;
import com.APP.Project.UserCoreLogic.Container.CountryContainer;
import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.MapModelTypes;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This file loads map file in the user console. This file acts as a Target class in adapter pattern. This service handles `editmap` user command.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class EditMapService implements StandaloneCommand {
    
    private final MapEditorEngine d_mapEditorEngine;

    private final ContinentContainer d_continentRepository;
    private final CountryContainer d_countryRepository;
    private final ContinentAdapter d_continentService;
    private final CountryAdapter d_countryService;
    private final CountryNeighborAdapter d_countryNeighborService;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructs an EditMapService object.
     */
    public EditMapService() {
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_continentRepository = new ContinentContainer();
        d_countryRepository = new CountryContainer();
        d_continentService = new ContinentAdapter();
        d_countryService = new CountryAdapter();
        d_countryNeighborService = new CountryNeighborAdapter();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
    }

    /**
     * Handles loading of a map from a file path.
     *
     * @param p_filePath       The file path of the map.
     * @param shouldCreateNew  Boolean value indicating whether to create a new file if not found.
     * @return                 A message indicating the status of the operation.
     * @throws InvalidMapException         If the map is invalid.
     * @throws AbsentTagException          If a required tag is absent.
     * @throws ResourceNotFoundException  If the resource is not found.
     * @throws InvalidInputException      If the input is invalid.
     * @throws EntityNotFoundException    If the entity is not found.
     */
    public String handleLoadMap(String p_filePath, boolean shouldCreateNew)
            throws InvalidMapException,
            AbsentTagException,
            ResourceNotFoundException,
            InvalidInputException,
            EntityNotFoundException {
        
        d_mapEditorEngine.initialise();
        d_mapEditorEngine.setLoadingMap(true);
        if (new File(p_filePath).exists()) {
            try {
                
                FileValidationUtil.retrieveMapFile(p_filePath);
                
                BufferedReader l_reader = new BufferedReader(new FileReader(p_filePath));

                
                String l_currentLine;
                while ((l_currentLine = l_reader.readLine()) != null) {
                    if (l_currentLine.startsWith("[")) {
                        
                        if (this.doLineHasModelData(l_currentLine, MapModelTypes.CONTINENT)) {
                            readContinents(l_reader);
                        } else if (this.doLineHasModelData(l_currentLine, MapModelTypes.COUNTRY)) {
                            
                            readCountries(l_reader);
                        } else if (this.doLineHasModelData(l_currentLine, MapModelTypes.BORDER)) {
                            
                            readNeighbours(l_reader);
                        }
                    }
                }
                return "File loaded successfully!";
            } catch (IOException p_ioException) {
                throw new ResourceNotFoundException("File not found!");
            }
        } else if (shouldCreateNew) {
            
            FileValidationUtil.checksIfFileHasRequiredExtension(p_filePath, FileType.MAP);

            FileValidationUtil.createFileIfNotExists(p_filePath);
            return "New file created!";
        } else {
            throw new InvalidMapException("Please check if file exists. This may happen due to error while processing.");
        }
    }

    /**
     * Handles loading of a map from a file path.
     *
     * @param p_filePath       The file path of the map.
     * @return                 A message indicating the status of the operation.
     * @throws AbsentTagException          If a required tag is absent.
     * @throws InvalidMapException         If the map is invalid.
     * @throws ResourceNotFoundException  If the resource is not found.
     * @throws InvalidInputException      If the input is invalid.
     * @throws EntityNotFoundException    If the entity is not found.
     */
    public String handleLoadMap(String p_filePath) throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException {
        return this.handleLoadMap(p_filePath, true);
    }

    /**
     * Reads continents from the input reader.
     *
     * @param p_reader       The input reader.
     * @throws InvalidInputException  If the input is invalid.
     * @throws InvalidMapException    If the map is invalid.
     * @throws AbsentTagException     If a required tag is absent.
     */
    private void readContinents(BufferedReader p_reader) throws InvalidInputException, InvalidMapException, AbsentTagException {
        String l_currentLine;
        try {
            while ((l_currentLine = p_reader.readLine()) != null && !l_currentLine.startsWith("[")) {
                if (l_currentLine.trim().isEmpty()) {
                    
                    continue;
                }
                List<String> l_continentComponentList = this.getModelComponents(l_currentLine);
                if (l_continentComponentList.size() >= 2) {
                    d_continentService.add(l_continentComponentList.get(0), l_continentComponentList.get(1));
                } else {
                    throw new AbsentTagException("Missing continent value!");
                }
                p_reader.mark(0);
            }
            p_reader.reset();
        } catch (IOException p_ioException) {
            throw new InvalidMapException("Error while processing!");
        }
    }

    /**
     * Reads countries from the input reader.
     *
     * @param p_reader       The input reader.
     * @throws EntityNotFoundException    If the entity is not found.
     * @throws InvalidMapException        If the map is invalid.
     * @throws AbsentTagException         If a required tag is absent.
     */
    private void readCountries(BufferedReader p_reader) throws EntityNotFoundException, InvalidMapException, AbsentTagException {
        String l_currentLine;
        try {
            while ((l_currentLine = p_reader.readLine()) != null && !l_currentLine.startsWith("[")) {
                if (l_currentLine.trim().isEmpty()) {
                    
                    continue;
                }
                List<String> l_countryComponentList = this.getModelComponents(l_currentLine);
                if (l_countryComponentList.size() >= 3) {
                    d_countryService.add(Integer.parseInt(l_countryComponentList.get(0)), l_countryComponentList.get(1), Integer.parseInt(l_countryComponentList.get(2)));
                } else {
                    throw new AbsentTagException("Missing country value!");
                }
                p_reader.mark(0);
            }
            p_reader.reset();
        } catch (IOException p_ioException) {
            throw new InvalidMapException("Error while processing!");
        }

    }

    /**
     * Reads neighbors from the input reader.
     *
     * @param p_reader       The input reader.
     * @throws AbsentTagException     If a required tag is absent.
     * @throws InvalidMapException    If the map is invalid.
     */
    private void readNeighbours(BufferedReader p_reader) throws AbsentTagException, InvalidMapException {
        String l_currentLine;
        try {
            while ((l_currentLine = p_reader.readLine()) != null && !l_currentLine.startsWith("[")) {
                if (l_currentLine.trim().isEmpty()) {
                    
                    continue;
                }
                List<String> l_borderComponentList = this.getModelComponents(l_currentLine);
                if (l_borderComponentList.size() > 1) {
                    Country l_country = d_countryRepository.findByCountryId(Integer.parseInt(l_borderComponentList.get(0)));
                    if (l_country != null) {
                        for (int i = 1; i < l_borderComponentList.size(); i++) {
                            Country l_neighbourCountry = d_countryRepository.findByCountryId(Integer.parseInt(l_borderComponentList.get(i)));
                            if (l_neighbourCountry != null) {
                                d_countryNeighborService.add(l_country, l_neighbourCountry);
                            }
                        }
                    }
                } else {
                    throw new AbsentTagException("Missing border value!");
                }
            }
            p_reader.mark(0);
        } catch (IOException e) {
            throw new InvalidMapException("Error while processing!");
        }
    }

    /**
     * Checks if the line contains model data.
     *
     * @param p_currentLine     The current line.
     * @param p_mapModelType    The map model type.
     * @return                  True if the line contains model data, false otherwise.
     */
    private boolean doLineHasModelData(String p_currentLine, MapModelTypes p_mapModelType) {
        return p_currentLine.substring(p_currentLine.indexOf("[") + 1, p_currentLine.indexOf("]"))
                .equalsIgnoreCase(p_mapModelType.getJsonValue());
    }

    /**
     * Retrieves model components from the input line.
     *
     * @param p_line       The input line.
     * @return             List of model components.
     */
    public List<String> getModelComponents(String p_line) {
        try {
            if (!p_line.isEmpty() && p_line.contains(" ")) {
                List<String> l_continentComponentList = Arrays.asList(p_line.split("\\s"));
                if (!l_continentComponentList.isEmpty()) {
                    l_continentComponentList = l_continentComponentList.stream().map(String::trim)
                            .collect(Collectors.toList());
                    if (!(l_continentComponentList.contains(null) || l_continentComponentList.contains(""))) {
                        return l_continentComponentList;
                    }
                }
            }
        } catch (Exception e) {
            
        }
        return new ArrayList<>();
    }

    /**
     * Executes the command.
     *
     * @param p_commandValues        List of command values.
     * @return                       The response after execution.
     * @throws UserCoreLogicException   If there is an exception in UserCoreLogic.
     */
    @Override
    public String execute(List<String> p_commandValues)
            throws UserCoreLogicException {
        String l_response = "";
        if (!p_commandValues.isEmpty()) {
            // Resolve file path using absolute path of user data directory.
            String l_resolvedPathToFile = FindFilePathUtil.resolveFilePath(p_commandValues.get(0));
            int l_index = l_resolvedPathToFile.lastIndexOf('\\');
            l_response = this.handleLoadMap(l_resolvedPathToFile);
            d_logEntryBuffer.dataChanged("editmap", l_resolvedPathToFile.substring(l_index + 1) + " " + l_response);
            d_mapEditorEngine.setLoadingMap(false);
            return l_response;
        } else {
            throw new InvalidInputException("File name is empty!");
        }
    }
}
