package it.polimi.ingsw;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import it.polimi.ingsw.ServerTesting.CardTest;
import it.polimi.ingsw.ServerTesting.TableTest;
import it.polimi.ingsw.ServerTesting.UpdateTest;
import it.polimi.ingsw.ServerTesting.UtilityTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({CardTest.class, TableTest.class, UtilityTest.class, UpdateTest.class})
public class AppTestSuite
{



}
