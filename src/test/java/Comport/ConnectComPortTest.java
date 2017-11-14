/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Comport;

import jssc.SerialPortEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jan.Adamczyk
 */
public class ConnectComPortTest {
    
    /**
     *
     */
    public ConnectComPortTest() {
    }
    
    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {
    }
    
    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     *
     */
    @Before
    public void setUp() {
        
    }
    
    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class ComportHandler.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        ComportHandler instance = null;
        instance.run();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of serialEvent method, of class ComportHandler.
     */
    @Test
    public void testSerialEvent() {
        System.out.println("serialEvent");
        SerialPortEvent event = null;
        ComportHandler instance = null;
        instance.serialEvent(event);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of sendCommand method, of class ComportHandler.
     */
    @Test
    public void testSendCommand() {
        System.out.println("sendCommand");
        ComportHandler instance = null;
        instance.sendCommand();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getEmbeddedLatency method, of class ComportHandler.
     */
    @Test
    public void testGetEmbeddedLatency() {
        System.out.println("getEmbeddedLatency");
        ComportHandler instance = null;
        int expResult = 0;
        int result = instance.getEmbeddedLatency();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
