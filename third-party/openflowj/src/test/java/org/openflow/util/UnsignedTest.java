package org.openflow.util;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import junit.framework.TestCase;

public class UnsignedTest extends TestCase {
    public static String ULONG_MAX = "18446744073709551615";

  /**
   * Tests that we correctly extract an unsigned long into a BigInteger
   * @throws Exception
   */
  public void testGetUnsignedLong() throws Exception {
    ByteBuffer bb = ByteBuffer.allocate(8);
    bb.put((byte)0xff).put((byte)0xff).put((byte)0xff).put((byte)0xff);
    bb.put((byte)0xff).put((byte)0xff).put((byte)0xff).put((byte)0xff);
    bb.position(0);
    bb.limit(8);
    BigInteger bi = Unsigned.getUnsignedLong(bb);
    BigInteger uLongMax = new BigInteger(ULONG_MAX);
    for (int i = 0; i < uLongMax.bitCount(); ++i) {
        TestCase.assertTrue("Bit: " + i + " should be: " + uLongMax.testBit(i),
                uLongMax.testBit(i) == bi.testBit(i));
    }
    TestCase.assertEquals(ULONG_MAX, bi.toString());

    bb = ByteBuffer.allocate(10);
    bb.put((byte)0x00);
    bb.put((byte)0xff).put((byte)0xff).put((byte)0xff).put((byte)0xff);
    bb.put((byte)0xff).put((byte)0xff).put((byte)0xff).put((byte)0xff);
    bb.put((byte)0x00);
    bb.position(0);
    bb.limit(10);
    bi = Unsigned.getUnsignedLong(bb, 1);
    uLongMax = new BigInteger(ULONG_MAX);
    for (int i = 0; i < uLongMax.bitCount(); ++i) {
        TestCase.assertTrue("Bit: " + i + " should be: " + uLongMax.testBit(i),
                uLongMax.testBit(i) == bi.testBit(i));
    }
    TestCase.assertEquals(ULONG_MAX, bi.toString());
  }

  /**
   * Tests that we correctly put an unsigned long into a ByteBuffer
   * @throws Exception
   */
  public void testPutUnsignedLong() throws Exception {
    ByteBuffer bb = ByteBuffer.allocate(8);
    BigInteger uLongMax = new BigInteger(ULONG_MAX);
    Unsigned.putUnsignedLong(bb, uLongMax);
    for (int i = 0; i < 8; ++i) {
        TestCase.assertTrue("Byte: " + i + " should be 0xff, was: " + bb.get(i),
                (bb.get(i) & (short)0xff) == 0xff);
    }

    bb = ByteBuffer.allocate(10);
    Unsigned.putUnsignedLong(bb, uLongMax, 1);
    int offset = 1;
    for (int i = 0; i < 8; ++i) {
        TestCase.assertTrue("Byte: " + i + " should be 0xff, was: " +
                bb.get(offset+i), (bb.get(offset+i) & (short)0xff) == 0xff);
    }
  }
}
