package org.jed2k.protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import org.jed2k.exception.JED2KException;

import static org.jed2k.protocol.Unsigned.uint8;

public class ServerList extends ContainerHolder<UInt8, NetworkIdentifier> implements Dispatchable {

    public ServerList() {
        super(uint8(), new ArrayList<NetworkIdentifier>(), NetworkIdentifier.class);
    }

    @Override
    public ByteBuffer get(ByteBuffer src) throws JED2KException {
        return src;
    }

    @Override
    public ByteBuffer put(ByteBuffer dst) throws JED2KException {
        return dst;
    }

    @Override
    public int bytesCount() {        
        return 0;
    }

    @Override
    public void dispatch(Dispatcher dispatcher) throws JED2KException {
        dispatcher.onServerList(this);
    }
}
