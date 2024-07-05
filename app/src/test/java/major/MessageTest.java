package major;

import major.model.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {
    /**
     * Test getters.
     */

    @Test
    public void messageGetter(){
        Message message = new Message("111111","111111","body");
        assertEquals(message.getFrom(),"111111");
        assertEquals(message.getTo(),"111111");
        assertEquals(message.getBody(),"body");
    }

}
