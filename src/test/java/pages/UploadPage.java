package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class UploadPage {

    private AndroidDriver driver;
    private WebDriverWait wait;

    private static final String APP_PACKAGE    = "com.tentangdental.app";
    private static final int    TIMEOUT_NORMAL  = 15;
    private static final int    TIMEOUT_GALLERY = 8;

    public UploadPage(AndroidDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_NORMAL));
    }

    // ==================== LOCATORS ====================

    private final By btnGalleryApp = By.xpath(
            "//*[contains(@text, 'Tambah') or contains(@text, 'TAMBAH') or contains(@content-desc, 'Tambah')]" +
                    " | //*[contains(@text, 'Gallery') or contains(@content-desc, 'Gallery')]" +
                    " | (//android.view.ViewGroup[contains(@content-desc,'Gallery')])[1]" +
                    " | //*[contains(@text, '+')]"
    );

    private final By fotoGaleri = By.xpath(
            "//*[contains(@resource-id,'photopicker') and contains(@resource-id,'icon_thumbnail')] | " +
                    "//*[contains(@resource-id,'com.google.android.photopicker:id/icon_thumbnail')]        | " +
                    "//*[contains(@resource-id,'media_item')]                                              | " +
                    "//android.widget.GridView//android.widget.ImageView                                   | " +
                    "//*[contains(@content-desc,'Photo') or contains(@content-desc,'Image')]               | " +
                    "(//android.widget.ImageView)[1]"
    );

    private final By mediaInvalidGif = By.xpath(
            "//*[contains(@content-desc,'GIF') or contains(@content-desc,'gif')]                  | " +
                    "(//android.widget.GridView//android.widget.ImageView)[2]                              | " +
                    "(//android.widget.ImageView)[2]"
    );

    private final By btnDonePhotoPicker = By.xpath(
            "//*[@text='Add']     | //*[@text='ADD']     | " +
                    "//*[@text='Done']    | //*[@text='DONE']    | " +
                    "//*[@text='Selesai'] | //*[@text='SELESAI'] | " +
                    "//*[contains(@resource-id,'button_add')]    | " +
                    "//*[contains(@resource-id,'button_done')]   | " +
                    "//*[contains(@resource-id,'action_done')]"
    );

    private final By txtErrorMsg = By.xpath(
            "//*[contains(@text,'gagal')]         | " +
                    "//*[contains(@text,'ukuran')]        | " +
                    "//*[contains(@text,'format')]        | " +
                    "//*[contains(@text,'tidak didukung')]| " +
                    "//*[contains(@text,'Maksimal')]      | " +
                    "//*[contains(@text,'Error')]"
    );

    // ==================== PUBLIC API ====================

    public void openGallery() {
        prosesPilihMedia(fotoGaleri, 1);
    }

    // FUNGSI BARU: Dipanggil saat upload batch/massal agar foto yang dipilih bervariasi (urut berdasarkan indeks loop)
    public void openGalleryDenganIndeks(int indeks) {
        try {
            System.out.println("Membuka Galeri Aplikasi untuk indeks ke-" + indeks + "...");
            wait.until(ExpectedConditions.elementToBeClickable(btnGalleryApp)).click();
            Thread.sleep(3000);

            System.out.println("Memilih media secara variatif berdasarkan urutan list...");
            boolean targetTerpilih = false;

            try {
                WebDriverWait galleryWait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_GALLERY));
                galleryWait.until(ExpectedConditions.presenceOfElementLocated(fotoGaleri));
                List<WebElement> daftarFoto = driver.findElements(fotoGaleri);

                if (!daftarFoto.isEmpty()) {
                    // Gunakan modulo % agar jika putaran loop melebihi total foto di HP, tidak terjadi crash/error index out of bounds
                    int indeksAman = indeks % daftarFoto.size();
                    WebElement mediaDipilih = daftarFoto.get(indeksAman);

                    System.out.println("[INFO] Berhasil mengambil foto urutan list ke: " + indeksAman);
                    mediaDipilih.click();
                    targetTerpilih = true;
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                System.out.println("[WARNING] Gagal ambil elemen berindeks, beralih ke fallback koordinat dinamis.");
            }

            if (!targetTerpilih) {
                Dimension size = driver.manage().window().getSize();
                // Mengubah titik kolom klik X secara dinamis berdasarkan sisa bagi indeks agar koordinatnya bergeser-geser
                int kolomDinamis = (indeks % 3) + 1;
                int x = (size.width / 4) * kolomDinamis;
                int y = (int)(size.height * 0.30);
                tapCoordinate(x, y);
                System.out.println("Tap koordinat fallback dinamis: X=" + x + ", Y=" + y);
                Thread.sleep(2000);
            }

            prosesKonfirmasiSelesaiPicker();

        } catch (Exception e) {
            System.out.println("[ERROR] Gangguan di picker berindeks: " + e.getMessage());
        }
    }

    public void openGalleryForInvalidFile() {
        prosesPilihMedia(mediaInvalidGif, 2);
    }

    private void prosesPilihMedia(By targetLocator, int kolomFallback) {
        try {
            System.out.println("Membuka Galeri Aplikasi...");
            wait.until(ExpectedConditions.elementToBeClickable(btnGalleryApp)).click();
            Thread.sleep(3000);

            System.out.println("Memilih media dari Gallery Picker...");
            boolean targetTerpilih = false;
            try {
                WebDriverWait galleryWait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_GALLERY));
                WebElement media = galleryWait.until(ExpectedConditions.elementToBeClickable(targetLocator));
                media.click();
                System.out.println("[INFO] Klik elemen media berhasil.");
                targetTerpilih = true;
                Thread.sleep(2000);
            } catch (Exception e) {
                System.out.println("[WARNING] Klik elemen gagal, pakai koordinat kolom ke-" + kolomFallback);
            }

            if (!targetTerpilih) {
                Dimension size = driver.manage().window().getSize();
                int x = (size.width / 4) * kolomFallback;
                int y = (int)(size.height * 0.30);
                tapCoordinate(x, y);
                System.out.println("Tap koordinat fallback: X=" + x + ", Y=" + y);
                Thread.sleep(2000);
            }

            prosesKonfirmasiSelesaiPicker();

        } catch (Exception e) {
            System.out.println("[ERROR] Gangguan di picker: " + e.getMessage());
        }
    }

    // Helper internal untuk mengisolasi penekanan tombol Done/Selesai bawaan asli kamu
    private void prosesKonfirmasiSelesaiPicker() throws InterruptedException {
        System.out.println("Mencari tombol konfirmasi Photo Picker (Add/Done)...");
        boolean doneClicked = false;
        for (int retry = 0; retry < 3; retry++) {
            try {
                WebElement doneBtn = driver.findElement(btnDonePhotoPicker);
                if (doneBtn.isDisplayed()) {
                    doneBtn.click();
                    System.out.println("[INFO] Tombol konfirmasi berhasil ditekan pada pencobaan ke-" + (retry + 1));
                    doneClicked = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("[INFO] Mengulang pencarian tombol Done karena DOM refresh...");
                Thread.sleep(800);
            }
        }

        if (!doneClicked) {
            System.out.println("[WARNING] Gagal klik via elemen, mencoba tap koordinat area kanan bawah...");
            Dimension size = driver.manage().window().getSize();
            int x = (int)(size.width * 0.85);
            int y = (int)(size.height * 0.94);
            tapCoordinate(x, y);
            Thread.sleep(3000);
        }

        System.out.println("Menunggu aplikasi kembali ke " + APP_PACKAGE + "...");
        try {
            WebDriverWait pkgWait = new WebDriverWait(driver, Duration.ofSeconds(15));
            pkgWait.until(d -> APP_PACKAGE.equals(((AndroidDriver) d).getCurrentPackage()));
            System.out.println("[✅] Aplikasi berhasil kembali ke package utama.");
        } catch (Exception e) {
            System.out.println("[WARNING] Package saat ini: " + driver.getCurrentPackage());
        }
    }

    private void tapCoordinate(int x, int y) {
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(tap));
    }

    public boolean isThumbnailDisplayed() {
        try {
            By imgPreview = By.xpath(
                    "//android.widget.ImageView[contains(@content-desc, 'preview') or contains(@content-desc, 'Rontgen')]" +
                            " | //android.view.ViewGroup/android.widget.ImageView"
            );
            List<WebElement> elements = driver.findElements(imgPreview);
            for (WebElement el : elements) {
                String resId = el.getAttribute("resource-id");
                String desc = el.getAttribute("content-desc");

                if (resId != null && (resId.contains("back") || resId.contains("close") || resId.contains("left"))) {
                    continue;
                }
                if (desc != null && desc.contains("")) {
                    continue;
                }
                if (el.isDisplayed() && el.getSize().getWidth() > 60) return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessage() {
        try {
            WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            WebElement msg = quickWait.until(ExpectedConditions.visibilityOfElementLocated(txtErrorMsg));
            return msg.getText();
        } catch (Exception e) {
            return "Pesan error tidak ditemukan.";
        }
    }

    public void deletePhoto() {
        try {
            System.out.println("[DEBUG] Mencari kandidat tombol × merah (elemen kecil < 60x60)...");
            List<WebElement> semuaElemen = driver.findElements(
                    By.xpath("//android.widget.ImageView | //android.view.View | //android.view.ViewGroup")
            );
            for (WebElement el : semuaElemen) {
                try {
                    int w = el.getSize().getWidth();
                    int h = el.getSize().getHeight();
                    if (w > 0 && w < 60 && h > 0 && h < 60) {
                        String rid  = el.getAttribute("resource-id");
                        String desc = el.getAttribute("content-desc");
                        int locX = el.getLocation().getX();
                        int locY = el.getLocation().getY();
                        System.out.println("[DEBUG × kandidat] resource-id='" + rid
                                + "' content-desc='" + desc
                                + "' size=" + w + "x" + h
                                + " loc=" + locX + "," + locY);
                    }
                } catch (Exception ignored) {}
            }

            boolean hapusBerhasil = false;

            try {
                WebElement btnIconFont = driver.findElement(By.xpath("//*[@content-desc='']"));
                if (btnIconFont.isDisplayed()) {
                    System.out.println("[INFO] Menemukan tombol × via deskripsi spesifik ''. Mengklik...");
                    btnIconFont.click();
                    hapusBerhasil = true;
                    Thread.sleep(1500);
                }
            } catch (Exception e) {
                System.out.println("[INFO] Mencari dengan locator regex standard...");
            }

            if (!hapusBerhasil) {
                By btnSilangMerah = By.xpath(
                        "//*[contains(@resource-id,'delete') or contains(@resource-id,'remove')" +
                                "    or contains(@resource-id,'btn_close') or contains(@resource-id,'ic_close')" +
                                "    or contains(@resource-id,'icon_close') or contains(@resource-id,'btn_delete')" +
                                "    or contains(@resource-id,'icon_delete')]" +
                                "   [not(contains(@resource-id,'back'))] | " +
                                "//*[contains(@content-desc,'Hapus') or contains(@content-desc,'Remove')" +
                                "    or contains(@content-desc,'Delete') or @content-desc='×'" +
                                "    or @content-desc='X' or @content-desc='x']" +
                                "   [not(contains(@content-desc,'back')) and not(contains(@content-desc,'Back'))]"
                );

                try {
                    WebDriverWait quickWait = new WebDriverWait(driver, Duration.ofSeconds(4));
                    WebElement btnHapus = quickWait.until(ExpectedConditions.elementToBeClickable(btnSilangMerah));
                    System.out.println("[INFO] Tombol × ditemukan — resource-id='"
                            + btnHapus.getAttribute("resource-id")
                            + "' content-desc='" + btnHapus.getAttribute("content-desc") + "'");
                    btnHapus.click();
                    System.out.println("[INFO] Tombol × berhasil diklik.");
                    hapusBerhasil = true;
                    Thread.sleep(1500);
                } catch (Exception e) {
                    System.out.println("[WARNING] Locator tombol × standard tidak cocok.");
                }
            }

            if (!hapusBerhasil) {
                System.out.println("[INFO] Fallback: Membidik area kanan atas dari struktur box foto...");
                By imgThumbnail = By.xpath(
                        "//android.widget.ImageView" +
                                "[not(contains(@resource-id,'back')) and not(contains(@resource-id,'arrow'))" +
                                " and not(contains(@resource-id,'header'))]"
                );
                List<WebElement> fotos = driver.findElements(imgThumbnail);
                WebElement targetFoto = null;
                for (WebElement f : fotos) {
                    try {
                        String rid = f.getAttribute("resource-id");
                        String desc = f.getAttribute("content-desc");
                        if (rid != null && (rid.contains("back") || rid.contains("arrow") || rid.contains("header"))) {
                            continue;
                        }
                        if (desc != null && desc.contains("")) {
                            continue;
                        }
                        if (f.isDisplayed() && f.getSize().getWidth() > 60) {
                            targetFoto = f;
                            break;
                        }
                    } catch (Exception ignored) {}
                }

                if (targetFoto != null) {
                    int thumbX = targetFoto.getLocation().getX();
                    int thumbY = targetFoto.getLocation().getY();
                    int width  = targetFoto.getSize().getWidth();

                    int tapX   = thumbX + width - 15;
                    int tapY   = thumbY + 15;
                    tapCoordinate(tapX, tapY);
                    System.out.println("[INFO] Tap koordinat kanan atas thumbnail X=" + tapX + " Y=" + tapY);
                    hapusBerhasil = true;
                    Thread.sleep(1500);
                } else {
                    System.out.println("[WARNING] Box foto tidak terbaca, mengeksekusi koordinat absolut logcat (343, 694)...");
                    tapCoordinate(343, 694);
                    hapusBerhasil = true;
                    Thread.sleep(1500);
                }
            }

            if (hapusBerhasil) {
                By btnKonfirmasiDialog = By.xpath(
                        "//*[@text='Hapus'] | //*[@text='HAPUS'] | " +
                                "//*[@text='Ya']    | //*[@text='YA']    | " +
                                "//*[@text='OK']    | //*[@text='ok']"
                );
                try {
                    WebDriverWait dialogWait = new WebDriverWait(driver, Duration.ofSeconds(3));
                    WebElement btnDialog = dialogWait.until(ExpectedConditions.elementToBeClickable(btnKonfirmasiDialog));
                    btnDialog.click();
                    System.out.println("[INFO] Dialog konfirmasi hapus berhasil diklik.");
                    Thread.sleep(1500);
                } catch (Exception ignored) {
                    System.out.println("[INFO] Tidak ada dialog konfirmasi muncul, lanjut.");
                }
            }

        } catch (Exception e) {
            System.out.println("[ERROR] Gagal menghapus foto: " + e.getMessage());
        }
    }
}