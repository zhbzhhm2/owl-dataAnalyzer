import java.io.File;

/**
 * Created by root on 3/22/16.
 */
public class FileUtils {

    public static File getFileFromResources(String name) {
        return new FileUtils().getFileFromResourcesNonStatic(name);
    }


    public File getFileFromResourcesNonStatic(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        return new File(classLoader.getResource(name).getFile());
    }
}
