package org.jed2k.test;

import org.jed2k.Session;
import org.jed2k.SessionTrial;
import org.jed2k.Settings;
import org.jed2k.TransferHandle;
import org.jed2k.exception.JED2KException;
import org.jed2k.protocol.Hash;
import org.jed2k.protocol.NetworkIdentifier;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by inkpot on 24.08.2016.
 */
public class SessionTest {
    private Session session;
    private Settings settings;

    @Before
    public void setupSession() {
        settings = new Settings();
        settings.listenPort = -1;
        session = new SessionTrial(settings, new LinkedList<NetworkIdentifier>());
    }

    @Test
    public void testTransferHandle() throws JED2KException, InterruptedException {
        session.start();
        TransferHandle handle = session.addTransfer(Hash.EMULE, 1000L, "xxx");
        assertTrue(handle.isValid());
        TransferHandle handle2 = session.addTransfer(Hash.EMULE, 1002L, "yyy");
        assertEquals(1, session.getTransfers().size());
        assertEquals(handle, handle2);
        assertEquals(1000L, handle2.getSize());
        assertEquals("xxx", handle2.getFilepath());
        assertTrue(handle2.getPeersInfo().isEmpty());
        session.removeTransfer(handle.getHash(), true);
        session.interrupt();
        session.join();
        assertEquals(0, session.getTransfers().size());
    }
}