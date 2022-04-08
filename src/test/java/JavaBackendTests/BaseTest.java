package JavaBackendTests;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class BaseTest

        public String getResourceAsString( Stringname)  throwsIOException {
            return new String(
                    getClass().getResourceAsStream(name)
                            .readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }