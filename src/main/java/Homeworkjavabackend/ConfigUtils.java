package Homeworkjavabackend;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
@UtilityClass
public class {
        Properties prop = new Properties();
private static  InputStreamconfigFile;
static {
        try {
        configFile = new
        FileInputStream("src/main/resources/application.properties");
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        }
        }
@SneakyThrows
public String getBaseUrl() {
        prop.load(configFile);
        return prop.getProperty("url");
        }
        }