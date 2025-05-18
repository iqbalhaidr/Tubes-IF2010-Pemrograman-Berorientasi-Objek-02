package com.labpro;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testAppInitialization() {
        App app = new App();
        assertNotNull(app);
    }
}