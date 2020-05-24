import com.qa.keyword.base.WebDriverManager;
import com.qa.keyword.utils.FileOpsUtils;
import org.openqa.selenium.WebDriver;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static com.qa.keyword.base.WebDriverManager.initializeDriver;

public class WebDriverTest {

    public static void main(String args[]) throws IOException, InterruptedException, ClassNotFoundException {
        WebDriver wd = initializeDriver(null);
        wd.get("https://www.google.com");
        Thread.sleep(3000);
        wd.close();
    }
}
