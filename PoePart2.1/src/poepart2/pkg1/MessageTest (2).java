package poepart2.pkg1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MessageTest {

    private final Message m = new Message();

    @Test
    public void messageLengthSuccess() {
        assertEquals("Message ready to send.",
                m.checkMessageLength("Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void messageLengthFailure() {
        String longMsg = "a".repeat(300);
        assertEquals("Message exceeds 250 characters by 50, please reduce the size.",
                m.checkMessageLength(longMsg));
    }

    @Test
    public void recipientSuccess() {
        assertEquals("Cell phone number successfully captured.",
                m.checkRecipientCell("+27718693002"));
    }

    @Test
    public void recipientFailure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. "
                + "Please correct the number and try again.",
                m.checkRecipientCell("08575975889"));
    }

    @Test
    public void messageHashTestData1() {
        assertEquals("23:0:HITONIGHT",
                m.createMessageHash("2345678901", 0, "Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void messageHashExample() {
        assertEquals("00:0:HITHANKS", m.createMessageHash("0012345678", 0, "Hi, thanks"));
    }

    @Test
    public void messageIdCreated() {
        String id = m.generateMessageID();
        assertEquals(10, id.length());
        assertTrue(m.checkMessageID(id));
    }

    @Test
    public void sentMessageOptions() {
        assertEquals("Message successfully sent.", m.sentMessage(1));
        assertEquals("Press 0 to delete the message.", m.sentMessage(2));
        assertEquals("Message successfully stored.", m.sentMessage(3));
    }

    @Test
    public void totalMessagesSent() {
        m.addSentMessage("1234567890", "12:0:HITONIGHT", "+27718693002", "Hi Mike, tonight");
        assertEquals(1, m.returnTotalMessages());
        // Message 2 was discarded, so the total stays at 1
        assertEquals(1, m.returnTotalMessages());
    }
}
