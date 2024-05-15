package com.APP.Project.UserCoreLogic.map_features.adapters;

import com.APP.Project.UserCoreLogic.UserCoreLogic;
import com.APP.Project.UserCoreLogic.constants.enums.FileType;
import com.APP.Project.UserCoreLogic.constants.enums.MapModelTypes;
import com.APP.Project.UserCoreLogic.constants.interfaces.StandaloneCommand;
import com.APP.Project.UserCoreLogic.game_entities.Country;
import com.APP.Project.UserCoreLogic.game_entities.Continent;
import com.APP.Project.UserCoreLogic.exceptions.*;
import com.APP.Project.UserCoreLogic.logger.LogEntryBuffer;
import com.APP.Project.UserCoreLogic.map_features.MapEditorEngine;
import com.APP.Project.UserCoreLogic.Container.ContinentContainer;
import com.APP.Project.UserCoreLogic.Container.CountryContainer;
import com.APP.Project.UserCoreLogic.Utility.FileValidationUtil;
import com.APP.Project.UserCoreLogic.Utility.FindFilePathUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EditConquestMapService class provides functionality to load and edit Conquest maps.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
public class EditConquestMapService implements StandaloneCommand {

    private final HashMap<String, String> d_MapDetails;
    
    private final MapEditorEngine d_mapEditorEngine;
    private final CountryContainer d_countryRepository;
    private final ContinentContainer d_continentRepository;
    private final ContinentAdapter d_continentService;
    private final CountryAdapter d_countryService;
    private final LogEntryBuffer d_logEntryBuffer;

    /**
     * Constructor for EditConquestMapService class.
     */
    public EditConquestMapService() {
        d_MapDetails = new HashMap<>();
        d_mapEditorEngine = UserCoreLogic.getGameEngine().getMapEditorEngine();
        d_countryRepository = new CountryContainer();
        d_continentService = new ContinentAdapter();
        d_countryService = new CountryAdapter();
        d_logEntryBuffer = LogEntryBuffer.getLogger();
        d_continentRepository = new ContinentContainer();
    }

    /**
     * Loads a Conquest map from the specified file path.
     *
     * @param p_filePath       Path to the Conquest map file.
     * @param shouldCreateNew  Flag indicating whether to create a new file if not found.
     * @return                 Confirmation message indicating the successful load or creation of the file.
     * @throws ResourceNotFoundException  If the file is not found.
     * @throws InvalidInputException      If the input is invalid.
     * @throws InvalidMapException        If the map is invalid.
     * @throws AbsentTagException         If required tags are absent.
     * @throws EntityNotFoundException   If an entity is not found.
     */
    public String loadConquestMap(String p_filePath, boolean shouldCreateNew)
            throws ResourceNotFoundException,
            InvalidInputException,
            InvalidMapException,
            AbsentTagException,
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
                        
                        if (this.doLineHasModelData(l_currentLine, MapModelTypes.MAP)) {
                            readMapDetails(l_reader);
                        }
                        
                        else if (this.doLineHasModelData(l_currentLine, MapModelTypes.CONTINENT)) {
                            readContinents(l_reader);
                        }
                        
                        else if (this.doLineHasModelData(l_currentLine, MapModelTypes.TERRITORY)) {

                            readTerritories(l_reader);
                        }
                    }
                }
                return "File(Conquest map) successfully loaded";
            } catch (IOException e) {
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
     * Reads the map details from the given reader.
     *
     * @param p_reader         Reader for reading the map details.
     * @throws InvalidMapException  If the map is invalid.
     */
    private void readMapDetails(BufferedReader p_reader) throws InvalidMapException {
        String l_currentLine;
        try {
            while ((l_currentLine = p_reader.readLine()) != null && !l_currentLine.startsWith("[")) {
                if (!l_currentLine.isEmpty()) {
                    String[] maps_entry = l_currentLine.split("=");
                    d_MapDetails.put(maps_entry[0], maps_entry[1]);
                    p_reader.mark(0);
                }
            }
            p_reader.reset();
            d_mapEditorEngine.setMapDetails(d_MapDetails);
        } catch (IOException e) {
            throw new InvalidMapException("Invalid map file");
        }
    }

    /**
     * Reads the continents from the given reader.
     *
     * @param p_reader         Reader for reading the continents.
     * @throws InvalidInputException  If the input is invalid.
     * @throws InvalidMapException   If the map is invalid.
     * @throws AbsentTagException    If required tags are absent.
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
        } catch (IOException e) {
            throw new InvalidMapException("Error while processing!");
        }
    }

    /**
     * Reads the territories from the given reader.
     *
     * @param p_reader         Reader for reading the territories.
     * @throws InvalidMapException    If the map is invalid.
     * @throws EntityNotFoundException  If an entity is not found.
     */
    public void readTerritories(BufferedReader p_reader) throws InvalidMapException, EntityNotFoundException {
        String l_territories;
        try {
            while ((l_territories = p_reader.readLine()) != null && !l_territories.startsWith("[")) {
                if (!l_territories.isEmpty()) {
                    String l_countryName, l_continentName;
                    String l_xCoordinate;
                    String l_yCoordinate;
                    List<Country> l_neighbourNodes = new ArrayList<>();
                    String[] l_terrProperties = l_territories.split(",");
                    l_countryName = l_terrProperties[0];
                    l_xCoordinate = l_terrProperties[1];
                    l_yCoordinate = l_terrProperties[2];
                    l_continentName = l_terrProperties[3];
                    Continent l_continent = d_continentRepository.findFirstByContinentName(l_continentName);

                    for (int i = 4; i <= l_terrProperties.length - 1; i++) {
                        String l_neighbourCountryName = l_terrProperties[i];
                        Country l_neighbour;
                        try {
                            l_neighbour = d_countryRepository.findFirstByCountryName(l_neighbourCountryName);
                            l_neighbour.setContinent(l_continent);
                            l_neighbourNodes.add(l_neighbour);
                        } catch (EntityNotFoundException e) {
                            l_neighbour = new Country(l_neighbourCountryName);
                            l_neighbour.setContinent(l_continent);
                            l_neighbourNodes.add(l_neighbour);
                        }
                    }
                    d_countryService.add(l_countryName, l_continentName, l_neighbourNodes, l_xCoordinate, l_yCoordinate);
                }
            }
        } catch (IOException e) {
            throw new InvalidMapException("Error while processing!");
        }
    }

    /**
     * Retrieves model components from the input line.
     *
     * @param p_line the input line
     * @return a list of model components
     */
    public List<String> getModelComponents(String p_line) {
        try {
            if (!p_line.isEmpty()) {
                List<String> l_continentComponentList = Arrays.asList(p_line.split("="));
                if (!l_continentComponentList.isEmpty()) {
                    l_continentComponentList = l_continentComponentList.stream().map(String::trim)
                            .collect(Collectors.toList());
                    if (!(l_continentComponentList.contains(null) || l_continentComponentList.contains(""))) {
                        return l_continentComponentList;
                    }
                }
            }
        } catch (Exception e) {
            // Exception handling
        }
        return new ArrayList<>();
    }

    /**
     * Loads a conquest map from the specified file path.
     *
     * @param p_filePath the file path of the conquest map
     * @return a string indicating the status of the loading process
     * @throws AbsentTagException     if a required tag is absent
     * @throws InvalidMapException    if the map is invalid
     * @throws ResourceNotFoundException if a resource is not found
     * @throws InvalidInputException  if the input is invalid
     * @throws EntityNotFoundException if an entity is not found
     */
    public String loadConquestMap(String p_filePath) throws AbsentTagException, InvalidMapException, ResourceNotFoundException, InvalidInputException, EntityNotFoundException {
        return this.loadConquestMap(p_filePath, true);
    }

    /**
     * Checks if the current line has model data of the specified type.
     *
     * @param p_currentLine the current line
     * @param p_mapModelType the map model type
     * @return true if the line has model data of the specified type, false otherwise
     */    
    private boolean doLineHasModelData(String p_currentLine, MapModelTypes p_mapModelType) {
        return p_currentLine.substring(p_currentLine.indexOf("[") + 1, p_currentLine.indexOf("]"))
                .equalsIgnoreCase(p_mapModelType.getJsonValue());
    }

    /**
     * Executes the editing command with the given command values.
     *
     * @param p_commandValues the command values
     * @return a string indicating the result of the command execution
     * @throws UserCoreLogicException if an exception occurs during command execution
     */
    @Override
    public String execute(List<String> p_commandValues) throws UserCoreLogicException {
        String l_response = "";
        if (!p_commandValues.isEmpty()) {
            String l_resolvedPathToFile = FindFilePathUtil.resolveFilePath(p_commandValues.get(0));
            int l_index = l_resolvedPathToFile.lastIndexOf('\\');
            l_response = this.loadConquestMap(l_resolvedPathToFile);
            d_logEntryBuffer.dataChanged("editmap", l_resolvedPathToFile.substring(l_index + 1) + " " + l_response);
            d_mapEditorEngine.setLoadingMap(false);
            return l_response;
        } else {
            throw new InvalidInputException("File name is empty!");
        }
    }
}
