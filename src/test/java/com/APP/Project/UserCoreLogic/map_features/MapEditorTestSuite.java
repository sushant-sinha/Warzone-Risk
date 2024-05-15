package com.APP.Project.UserCoreLogic.map_features;

import com.APP.Project.UserCoreLogic.map_features.adapters.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * This class represents a test suite for testing map editor functionalities.
 *
 * @author Rikin Dipakkumar Chauhan
 * @version 3.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        LoadMapAdapterTest.class,
        ShowMapAdapterTest.class,
        ContinentAdapterTest.class,
        ValidateMapAdapterTest.class,
        CountryAdapterTest.class,
        CountryNeighborAdapterTest.class,
        SaveMapAdapterTest.class,
        EditMapAdapterTest.class
})
public class MapEditorTestSuite {
    
}