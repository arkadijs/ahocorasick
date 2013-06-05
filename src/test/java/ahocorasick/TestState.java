package ahocorasick;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TestState {

    @Test
    public void testSimpleExtension() {
        State s = new State(0);
        State s2 = s.extend("a".getBytes()[0]);
        assertTrue(s2 != s && s2 != null);
        assertEquals(2, s.size());
    }

    @Test
    public void testSimpleExtensionSparse() {
        State s = new State(50);
        State s2 = s.extend((byte) 3);
        assertTrue(s2 != s && s2 != null);
        assertEquals(2, s.size());
    }

    @Test
    public void testSingleState() {
        State s = new State(0);
        assertEquals(1, s.size());
    }

    @Test
    public void testSingleStateSparse() {
        State s = new State(50);
        assertEquals(1, s.size());
    }

    @Test
    public void testExtendAll() {
        State s = new State(0);
        State s2 = s.extendAll("hello world".getBytes());
        assertEquals(12, s.size());
    }

    @Test
    public void testExtendAllTwiceDoesntAddMoreStates() {
        State s = new State(0);
        State s2 = s.extendAll("hello world".getBytes());
        State s3 = s.extendAll("hello world".getBytes());
        assertEquals(12, s.size());
        assertTrue(s2 == s3);
    }

    @Test
    public void testExtendAllTwiceDoesntAddMoreStatesSparse() {
        State s = new State(50);
        State s2 = s.extendAll("hello world".getBytes());
        State s3 = s.extendAll("hello world".getBytes());
        assertEquals(12, s.size());
        assertTrue(s2 == s3);
    }

    @Test
    public void testAddingALotOfStatesIsOk() {
        State s = new State(0);
        for (int i = 0; i < 256; i++) {
            s.extend((byte) i);
        }
        assertEquals(257, s.size());
    }

    @Test
    public void testAddingALotOfStatesIsOkOnSparseRep() {
        State s = new State(50);
        for (int i = 0; i < 256; i++) {
            s.extend((byte) i);
        }
        assertEquals(257, s.size());
    }
}
