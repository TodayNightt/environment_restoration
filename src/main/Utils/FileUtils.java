package Utils;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtils {
        public static Path loadFromResources(String fileName) throws URISyntaxException {
            return Paths.get(Objects.requireNonNull(FileUtils.class.getClassLoader().getResource(fileName)).toURI());
        }
}
