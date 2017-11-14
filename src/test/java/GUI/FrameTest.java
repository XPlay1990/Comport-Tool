/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package GUI;

import java.util.Observable;
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
public class FrameTest {
    
    /**
     *
     */
    public FrameTest() {
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
     * Test of allChannelOn method, of class Frame.
     */
    @Test
    public void testAllChannelOn() {
        System.out.println("allChannelOn");
        Frame instance = new Frame();
        instance.allChannelOn();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of allChannelOff method, of class Frame.
     */
    @Test
    public void testAllChannelOff() {
        System.out.println("allChannelOff");
        Frame instance = new Frame();
        instance.allChannelOff();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setErrorCount method, of class Frame.
     */
    @Test
    public void testSetErrorCount() {
        System.out.println("setErrorCount");
        int errorCount = 0;
        Frame instance = new Frame();
        instance.setErrorCount(errorCount);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setToolLatency method, of class Frame.
     */
    @Test
    public void testSetToolLatency() {
        System.out.println("setToolLatency");
        Long latency = null;
        Frame instance = new Frame();
        instance.setToolLatency(latency);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setEmbeddedLatency method, of class Frame.
     */
    @Test
    public void testSetEmbeddedLatency() {
        System.out.println("setEmbeddedLatency");
        long latency = 0L;
        Frame instance = new Frame();
        instance.setEmbeddedLatency(latency);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class Frame.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        Observable o = null;
        Object arg = null;
        Frame instance = new Frame();
        instance.update(o, arg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
