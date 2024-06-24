package test.java.it.polimi.ingsw;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import test.java.it.polimi.ingsw.ServerTesting.CardTest;
import test.java.it.polimi.ingsw.ServerTesting.TableTest;
import test.java.it.polimi.ingsw.ServerTesting.UpdateTest;
import test.java.it.polimi.ingsw.ServerTesting.UtilityTest;


@RunWith(Suite.class)
@Suite.SuiteClasses({CardTest.class, TableTest.class, UtilityTest.class, UpdateTest.class})
public class AppTestSuite
{



}
