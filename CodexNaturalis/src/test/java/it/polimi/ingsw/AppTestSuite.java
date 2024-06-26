package it.polimi.ingsw;

import it.polimi.ingsw.ModelTesting.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({CardTest.class, TableTest.class, UtilityTest.class,
        UpdateTest.class, UpdatePackageTest.class, GameTest.class, MultipleGameTest.class,
        ServerIOTest.class, TAbleManagerTest.class, ClientHandleTest.class, ConstantsTest.class})
public class AppTestSuite
{



}
