package it.polimi.ingsw;

import it.polimi.ingsw.ServerTesting.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


@RunWith(Suite.class)
@Suite.SuiteClasses({CardTest.class, TableTest.class, UtilityTest.class,
        UpdateTest.class, UpdatePackageTest.class, GameTest.class, MultipleGameTest.class,
        ServerIOTest.class})
public class AppTestSuite
{



}
