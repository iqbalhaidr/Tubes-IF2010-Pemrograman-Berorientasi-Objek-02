# IF2010 - Pemrograman Berorientasi Objek 2025

> **Tugas Besar 2 - IF2010 Pemrograman Berorientasi Objek 2025**
## Deskripsi Singkat

Aplikasi ini merupakan sistem manajemen logistik berbasis Java yang dirancang untuk mengelola data pengiriman, parsel, dan kurir secara modular dan extensible. Dengan menggunakan JavaFX sebagai antarmuka pengguna, serta dukungan arsitektur berbasis desain pola seperti Factory, Repository, Proxy, Adapter, dan Observer, aplikasi ini memungkinkan pengembangan fitur yang mudah dan terstruktur. Sistem juga mendukung plugin eksternal yang dapat dimuat secara dinamis menggunakan custom class loader, memungkinkan integrasi fitur tambahan tanpa perlu memodifikasi kode utama..

---
## Prasyarat Sistem

* Java Development Kit (JDK) 8 or higher
* JavaFX SDK
* Apache Maven
* **Sistem Operasi:** Linux/Unix (Ubuntu 20.04 LTS atau lebih baru disarankan)
* **Dukungan untuk Windows:** Gunakan WSL (Windows Subsystem for Linux) jika menggunakan Windows

### Instalasi Dependensi

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

---

## Cara Penggunaan

1. **Clone Repository**
   ```bash
   https://github.com/Labpro-21/if2010-2425-tubes-1-lah
   ```

2. **Compile Aplikasi**

   Untuk membuat runnable JAR file, gunakan command berikut.

   ```bash
   mvn package
   ```

   Jar file akan berada di folder `target/`. Jalankan jar file dengan command berikut.

   ```bash
   java --module-path $PATH_TO_FX --add-modules javafx.controls,javafx.fxml -jar target/courier-app-1.0-SNAPSHOT.jar
   ```


---

## Pembagian Tugas

<table border="1">
  <tr>
    <th>Nama Anggota</th>
    <th>NIM</th>
    <th>Tugas Utama</th>
  </tr>
  <tr>
    <td>Muhammad Aufa Farabi</td>
    <td>13523023</td>
    <td>Kerja</td>
  </tr>
  <tr>
    <td>Joel Hotlan Haris Siahaan</td>
    <td>13523025</td>
    <td>Kerja</td>
  </tr>
  <tr>
    <td>Julius Arthur</td>
    <td>13523030</td>
    <td>Kerja</td>
  </tr>
  <tr>
    <td>Ferdinand Gabe Tua Sinaga</td>
    <td>13523051</td>
    <td>Kerja</td>
  </tr>
  <tr>
    <td>Muhammad Iqbal Haidar</td>
    <td>13523111</td>
    <td>Kerja</td>
  </tr>
</table>

---

## Daftar Fitur


<table border="1">
  <tr>
    <th>Nama Fitur</th>
    <th>Status</th>
    <th>Nama Fitur</th>
    <th>Status</th>
  </tr>
  <tr>
    <td>Manajemen Pengiriman</td>
    <td align="center">✓</td>
    <td>Tracking Pengiriman</td>
    <td align="center">✓</td>
  </tr>
  <tr>
    <td>Manajemen Kurir</td>
    <td align="center">✓</td>
    <td>Manajemen Parsel</td>
    <td align="center">✓</td>
  </tr>
  <tr>
    <td>Ganti Akun</td>
    <td align="center">✓</td>
    <td>Halaman Kurir</td>
    <td align="center">✓</td>
  </tr>
  <tr>
    <td>Halaman Setting</td>
    <td align="center">✓ [?]</td>
    <td>Threading</td>
    <td align="center">✓</td>
  </tr>
  <tr>
    <td>Exception</td>
    <td align="center">✓</td>
    <td>Plugin Analytics</td>
    <td align="center">✓ [?]</td>
  </tr>
  <tr>
    <td>Data Store</td>
    <td align="center">✓</td>
    <td></td>
    <td></td>
  </tr>
</table>

---


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
