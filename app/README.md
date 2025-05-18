# Template Tugas Besar 2

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- JavaFX SDK
- Apache Maven

## Installation

1. **Download JavaFX SDK:**

   Pakai curl, download JavaFX SDK. Untuk memudahkan, pastikan lakukan command ini pada folder `app/` bukan pada root directory.

   ```bash
   curl -L "https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2_linux-x64_bin-sdk.zip" -o javafx-sdk.zip
   ```

2. **Unzip JavaFX SDK**

   Setelah download berhasil, unzip sdk.

   ```bash
   unzip javafx-sdk.zip
   ```

3. **Set Path Variable untuk JavaFX SDK:**

   Set `PATH_TO_FX` env variable

   ```bash
   export PATH_TO_FX="$PWD/javafx-sdk-21.0.2/lib"
   ```

4. **Compile Aplikasi**

   Untuk membuat runnable JAR file, gunakan command berikut.

   ```bash
   mvn package
   ```

   Jar file akan berada di folder `target/`. Jalankan jar file dengan command berikut.

   ```bash
   java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -jar target/courier-app-1.0-SNAPSHOT.jar
   ```

5. **Menjalankan Unit Testing**

   Untuk menjalankan unit testing, gunakan command berikut.

   ```bash
   mvn test -Djava.awt.headless=true -Dtestfx.robot=glass -Dglass.platform=Monocle -Dmonocle.platform=Headless -Dprism.order=sw
   ```

## Common Problems

1. Rendering Issue untuk MacOS Apple Silicon (Development)

   Seharusnya ini tidak terjadi karena seharusnya menggunakan lingkungan Linux, tetapi jika kalian ada yang memakai MacOS untuk development (tidak disarankan), ini beberapa fix yang bisa dilakukan, download JavaFX SDK dengan `arch64`.

   **Cek arsitektur sistem:**
   ```bash
   <!-- Cek apakah sistem menggunakan ARM architecture (Apple Silicon) -->
   uname -m
   ```

   ```bash
   <!-- Download JavaFX SDK untuk Apple Silicon (M1/M2/M3) -->
   curl -L "https://download2.gluonhq.com/openjfx/21.0.2/openjfx-21.0.2_osx-aarch64_bin-sdk.zip" -o javafx-sdk.zip

   <!-- Unzip file -->
   unzip javafx-sdk.zip

   <!-- Set path -->
   export PATH_TO_FX="$PWD/javafx-sdk-21.0.2/lib"

   <!-- Jalankan aplikasi dengan tambahan flag berikut -->
   java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -Dprism.order=sw -jar target/courier-app-1.0-SNAPSHOT.jar
   ```