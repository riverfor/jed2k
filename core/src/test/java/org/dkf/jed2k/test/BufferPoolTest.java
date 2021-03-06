package org.dkf.jed2k.test;

import org.dkf.jed2k.pool.BufferPool;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by inkpot on 08.07.2016.
 */
public class BufferPoolTest {
    @Test
    public void testBufferPool() {
        BufferPool bp = new BufferPool(4);
        assertEquals(0, bp.totalAllocatedBuffers());
        assertEquals(0, bp.cachedBuffers());
        LinkedList<ByteBuffer> allocated = new LinkedList<ByteBuffer>();
        for(int i = 0; i < 4; ++i) {
            allocated.add(bp.allocate());
            assertTrue(allocated.getLast() != null);
            assertEquals(i+1, bp.totalAllocatedBuffers());
            assertEquals(0, bp.cachedBuffers());
        }

        assertTrue(bp.allocate() == null);
        for(int i = 0; i < 2; ++i) {
            bp.deallocate(allocated.poll(), i);
            assertEquals(i+1, bp.cachedBuffers());
            assertEquals(4-i-1, bp.totalAllocatedBuffers());
        }

        assertTrue(bp.allocate() != null);
        assertEquals(1, bp.cachedBuffers());
        assertEquals(3, bp.totalAllocatedBuffers());
    }

    @Test
    public void testBufferPoolReduce() {
        BufferPool bp = new BufferPool(10);
        LinkedList<ByteBuffer> allocated = new LinkedList<ByteBuffer>();
        for(int i = 0; i < 6; ++i) {
            allocated.add(bp.allocate());
            assertTrue(allocated.getLast() != null);
        }

        bp.setMaxBuffersCount(4);
        assertTrue(bp.allocate() == null);
        while(allocated.size() > 2) {
            bp.deallocate(allocated.poll(), 10L);
        }

        assertEquals(2, bp.cachedBuffers());
        for(int i = 0; i < 2; ++i) {
            allocated.add(bp.allocate());
            assertTrue(allocated.getLast() != null);
        }

        assertTrue(bp.allocate() == null);
        assertEquals(0, bp.cachedBuffers());
    }

    @Test
    public void testBufferPoolReduceFull() {
        BufferPool bp = new BufferPool(6);
        LinkedList<ByteBuffer> allocated = new LinkedList<ByteBuffer>();
        for(int i = 0; i < 6; ++i) {
            allocated.add(bp.allocate());
            assertTrue(allocated.getLast() != null);
        }

        while(!allocated.isEmpty()) {
            bp.deallocate(allocated.poll(), 10L);
        }

        assertEquals(6, bp.cachedBuffers());
        bp.setMaxBuffersCount(2);
        assertEquals(2, bp.cachedBuffers());
        assertEquals(0, bp.totalAllocatedBuffers());
        bp.setMaxBuffersCount(8);

        for(int i = 0; i < 6; ++i) {
            allocated.add(bp.allocate());
            assertTrue(allocated.getLast() != null);
        }

        assertEquals(6, bp.totalAllocatedBuffers());
        assertEquals(0, bp.cachedBuffers());

        while(allocated.size() > 2) {
            bp.deallocate(allocated.poll(), 10L);
        }

        assertEquals(4, bp.cachedBuffers());
        bp.setMaxBuffersCount(2);
        assertEquals(0, bp.cachedBuffers());
        assertEquals(2, bp.totalAllocatedBuffers());
        assertTrue(bp.allocate() == null);
    }

    @Test
    public void testMemoryAllocation() {
        BufferPool pool = new BufferPool(100);
        LinkedList<ByteBuffer> buffers = new LinkedList<>();
        for(int i = 0; i < 100; ++i) {
            buffers.add(pool.allocate());
        }

        for(final ByteBuffer buffer: buffers) {
            pool.deallocate(buffer, 100);
        }

        buffers.clear();
    }
}
