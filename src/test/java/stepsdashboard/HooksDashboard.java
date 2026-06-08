package stepsdashboard;  // ← bukan "steps"

import io.cucumber.java.Before;
import io.cucumber.java.After;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class HooksDashboard {
    public static AndroidDriver driver;
    private static boolean isDriverReady = false;

    @Before(order = 1)
    public void setUp() throws MalformedURLException {
        if (isDriverReady && driver != null) {
            try {
                driver.currentActivity();
                System.out.println("LOG: Driver masih aktif, skip.");
                return;
            } catch (Exception e) {
                System.out.println("LOG: Driver crash, buat ulang: " + e.getMessage());
                isDriverReady = false;
                driver = null;
                // Jangan quit — langsung buat driver baru
            }
        }

        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android")
                .setAutomationName("UiAutomator2")
                .setDeviceName("b53f2c23")
                .setNoReset(true)
                .setUiautomator2ServerLaunchTimeout(Duration.ofSeconds(120))
                .setAdbExecTimeout(Duration.ofSeconds(90))
                .setAndroidInstallTimeout(Duration.ofSeconds(120));

        options.setCapability("autoLaunch", false);
        options.setCapability("dontStopAppOnReset", true);

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.activateApp("com.tentangdental.app");

        try { Thread.sleep(3000); } catch (Exception ignored) {}

        isDriverReady = true;
        System.out.println("LOG: Driver siap, aplikasi diaktifkan.");
    }

    @After(order = 1)
    public void tearDown() {
        System.out.println("LOG: Scenario selesai, driver tetap aktif.");
    }
}