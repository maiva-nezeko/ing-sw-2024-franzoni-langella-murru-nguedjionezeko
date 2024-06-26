package it.polimi.ingsw.ModelTesting;

import it.polimi.ingsw.ServerSide.Utility.ServerConstants;
import junit.framework.TestCase;

public class ConstantsTest extends TestCase {

    public void testConstants()
    {
        assertEquals(100, ServerConstants.getUPS_SET());

        assertEquals(60, ServerConstants.getTime_Per_Turn());

        boolean value = ServerConstants.getDebug();

        ServerConstants.setDebug(true);
        assertTrue(ServerConstants.getDebug());
        ServerConstants.setDebug(value);

    }


}
