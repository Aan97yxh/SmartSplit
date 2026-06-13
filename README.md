# SmartSplit — Aplikasi Mobile Split Bill

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android Badge">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin Badge">
  <img src="https://img.shields.io/badge/UI--Framework-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose Badge">
  <img src="https://img.shields.io/badge/Architecture-MVVM-FF6F00?style=for-the-badge" alt="MVVM Badge">
</p>

**SmartSplit** adalah aplikasi Android yang membantu pengguna membagi tagihan secara adil dan praktis. Pengguna dapat menentukan item yang dipesan oleh masing-masing orang, menghitung pajak dan service charge secara otomatis, melacak status pembayaran anggota, serta mengelola riwayat transaksi secara offline menggunakan Room Database.

---

##  Fitur Utama

Aplikasi ini dilengkapi dengan fitur Utama serta fitur tambahan untuk meningkatkan pengalaman pengguna:

###  Fitur Utama (Core Features)
* **Autentikasi Pengguna** Sistem Login & Register multi-akun yang dilengkapi fitur Auto-Login untuk mempermudah akses pengguna.
* **Create & Manage Bill** Pengguna dapat membuat transaksi/tagihan baru.
* **Split Bill Berbasis Item (Item-Based Split)** Setiap item dapat dikaitkan langsung dengan peserta yang mengonsumsinya sehingga pembagian biaya lebih adil dan transparan.
* **Kalkulasi Otomatis** Sistem secara otomatis menghitung subtotal, pajak (Tax), dan biaya layanan (Service Charge) untuk setiap peserta.
* **Riwayat Transaksi** Menyimpan dan menampilkan daftar transaksi yang pernah dibuat sehingga dapat diakses kembali kapan saja.
**Manajemen Transaksi (BREAD)** Pengguna dapat melihat riwayat transaksi, melihat detail tagihan, menambah tagihan baru, mengubah item, menghapus item/tagihan. 
* **Pelacakan Pembayaran (Payment Tracking)**  Memantau status pembayaran setiap peserta dengan status Pending atau Settled.
* **Detail Pembagian Tagihan** – Menampilkan rincian biaya per individu secara lengkap dan mudah dipahami.
* **Penyimpanan Lokal (Room Database)** – Data pengguna dan transaksi disimpan secara persisten menggunakan Room Database sehingga tetap tersedia setelah aplikasi ditutup.

###  Fitur Tambahan (Extra Features)
* **Jastip Mode (Jasa Titip)** Mendukung konversi harga dari mata uang asing ke Rupiah secara otomatis menggunakan data kurs dari API. 
* **Share Bill Result** Membagikan hasil pembagian tagihan kepada peserta lain secara cepat dan praktis.
* **Notifikasi Pengingat (Reminder Notification)** Mengirimkan notifikasi otomatis untuk tagihan yang belum diselesaikan dalam 24 jam.
* **Multi-Language Support** Mendukung Bahasa Indonesia dan Bahasa Inggris yang dapat diubah langsung melalui pengaturan aplikasi.
* **Dark Mode** Menyediakan tampilan mode gelap untuk meningkatkan kenyamanan penggunaan.
* **Manajemen Profil** Pengguna dapat melihat dan mengubah informasi profil, termasuk foto profil.
* **Onboarding Screen** Memberikan pengenalan singkat mengenai fitur dan alur penggunaan aplikasi bagi pengguna baru.
---

##  Teknologi yang Digunakan

| Komponen | Teknologi & Library | Deskripsi |
| :--- | :--- | :--- |
| **Frontend** | Kotlin, Jetpack Compose, Material 3 | Membangun UI deklaratif yang modern, adaptif, dan responsif. |
| **Architecture** | MVVM, Repository Pattern, Clean Architecture | Menjaga kode tetap rapi, mudah diuji (*testable*), dan *scalable*. |
| **Local Database** | Room Database, SQLite | Mengelola penyimpanan data relasional lokal secara efisien. |
| **Networking** | Retrofit, OkHttp, HttpLoggingInterceptor | Menangani *request* ke API eksternal dengan performa tinggi. |
| **Serialization** | Kotlinx Serialization | Melakukan konversi data JSON ke objek Kotlin secara *type-safe*. |
| **Background Task**| WorkManager | Menjalankan *background processing* untuk memicu sistem pengingat tagihan. |

###  Integrasi API Eksternal
Fitur konversi mata uang pada **Jastip Mode** memanfaatkan **Frankfurter API** untuk mendapatkan nilai tukar secara *real-time*.
*   **Endpoint:** `GET /latest`
*   **Contoh Request:** `https://api.frankfurter.app/latest?from=USD&to=IDR`

---

##  Struktur Folder (Architecture & Package Structure)

Proyek ini menerapkan prinsip pemisahan kode berbasis Clean Architecture (`data`, `domain`, `presentation`):

```text
app/src/main/java/com/smartsplit/app
├── data/
│   ├── local/
│   │   ├── dao/          # Data Access Object untuk query Room
│   │   └── converter/    # Custom Type Converters untuk Room
│   ├── preferences/      # Menyimpan session & pengaturan (DataStore/SharedPreferences)
│   ├── remote/
│   │   ├── api/          # Definisikan interface Retrofit
│   │   └── model/        # Data Transfer Object (DTO) untuk respon JSON
│   └── repository/       # Implementasi konkret dari domain repository
│
├── domain/
│   ├── model/            # Objek bisnis inti (Core Entity)
│   ├── repository/       # Interface abstraksi untuk data layer
│   └── usecase/          # Aturan bisnis spesifik aplikasi
│       ├── auth/
│       ├── bill/
│       └── settings/
│
├── presentation/         # Layer UI (Screen & ViewModel)
│   ├── auth/             # Login & Register Screen
│   ├── bill/             # Split Bill & Detail Screen
│   ├── home/             # Dashboard utama
│   ├── onboarding/       # Panduan awal pengguna
│   ├── settings/         # Pengaturan & Profil
│   └── splash/           # Splash screen awal
│
├── navigation/           # Pengaturan rute Navigasi Compose
├── notification/         # Logika WorkManager dan pembuatan Notifikasi
├── ui/
│   ├── components/       # Komponen UI global (Button, TextField kustom)
│   └── theme/            # Konfigurasi Tema Warna (Material 3), Tipografi, & Bentuk
└── util/                 # Helper, Extension, dan kelas Utility umum
```
##  Skema Database (Room Entities)

SmartSplit menggunakan **Room Database** untuk memastikan semua data transaksi tersimpan dengan aman di penyimpanan lokal secara *persistent*, sehingga aplikasi tetap dapat berfungsi penuh meskipun perangkat sedang *offline*.

Berikut adalah struktur tabel (*Entity*) utama yang digunakan:

### 1. User Entity
Digunakan untuk mengelola data autentikasi dan profil pengguna.
| Field | Tipe Data | Deskripsi |
| :--- | :--- | :--- |
| `email` (PK) | String | Email unik pengguna sebagai ID akun |
| `nama` | String | Nama lengkap pengguna |
| `password` | String | Kredensial kata sandi akun |
| `photoUri` | String? | URI lokasi file foto profil asli pengguna |

### 2. Bill Entity
Menyimpan data utama dari setiap sesi pembagian tagihan kelompok.
| Field | Tipe Data | Deskripsi |
| :--- | :--- | :--- |
| `billId` (PK) | String | ID unik untuk setiap tagihan |
| `userEmail` | String | Email User |
| `restaurantName` | String | Nama tempat atau restoran |
| `date` | String | Tanggal transaksi |
| `taxPercent` | Int | Persentase pajak yang dikenakan (Tax) |
| `servicePercent`| Int | Persentase biaya layanan (Service Charge) |
| `persons` | List\<String\> | Daftar anggota yang ikut serta dalam tagihan |
| `items` | List\<BillItem\> | Daftar item/makanan yang dipesan |
| `settledPersons` | Set<String> | Status pemantauan (Lunas / Belum Lunas) |
| `paymentStatus` | Long | Waktu pembuatan tagihan dalam format timestamp.) |

### 3. Transaction History Entity
Mencatat rekapitulasi transaksi yang telah diselesaikan untuk kebutuhan laporan atau arsip offline.
| Field | Tipe Data | Deskripsi |
| :--- | :--- | :--- |
| `historyId` (PK) | Long (Auto-generate) | ID unik rekam riwayat |
| `billId` | Long | Foreign Key yang terhubung ke Bill Entity |
| `totalAmount` | Double | Total nominal akhir setelah pajak & servis |
| `timestamp` | Long | Waktu transaksi berhasil dikunci/diselesaikan |

---
## Notifikasi Pengingat (Background Task)

Untuk menjamin tidak ada tagihan kelompok yang terlupakan, SmartSplit memanfaatkan dependensi **WorkManager** untuk menjalankan tugas di latar belakang (*background task*):

* **Logika Sistem:** Setiap kali sebuah tagihan baru dibuat dengan status *Belum Lunas*, `WorkManager` akan menjadwalkan tugas pemeriksaan berkala.
* **Durasi Batas Waktu:** Jika dalam kurun waktu **24 jam** sejak pembuatan awal tagihan tersebut belum ditandai sebagai *Lunas*, sistem latar belakang akan secara otomatis memicu pembuatan **Notification** lokal.
* **Pengalaman Pengguna:** Notifikasi pengingat (*reminder*) akan muncul di *status bar* perangkat Android untuk memperingatkan pengguna agar segera menagih atau menyelesaikan pembayaran anggota kelompok.

---

##  Cara Menjalankan Aplikasi

### Prasyarat (Prerequisites)
Sebelum melakukan proses kompilasi kode, pastikan sudah memenuhi spesifikasi berikut:
* **Android Studio Narwhal** (atau versi di atasnya yang lebih baru).
* **Android SDK 24+** (Aplikasi berjalan minimum pada Android 7.0 Nougat).
* **JDK 17** yang sudah terpasang dan dipilih sebagai Gradle JDK pada pengaturan Android Studio.

### Langkah Instalasi
1.  **Clone Repository** Buka terminal atau Git Bash, lalu jalankan perintah berikut:
    ```bash
    git clone https://github.com/Aan97yxh/SmartSplit.git
    ```
2.  **Buka Proyek di Android Studio** Buka Android Studio, pilih menu **File > Open**, lalu arahkan ke folder hasil kloning `SmartSplit`.
3.  **Sinkronisasi Gradle (Gradle Sync)** Biarkan Android Studio mengunduh semua dependensi eksternal yang dibutuhkan (seperti Room, Compose, Retrofit, dan WorkManager). Tunggu hingga proses selesai dengan status *Build Successful*.
4.  **Jalankan Aplikasi** Sambungkan HP Android asli menggunakan kabel data (pastikan *USB Debugging* aktif) atau gunakan Emulator Android Studio. Klik tombol ikon **Run (Segitiga Hijau)** atau tekan kombinasi tombol `Shift + F10`.
5.  **Memulai Sesi** Dimulai dengan ditampilkannya *SplashScreen* terlebih dahulu sebagai identitas aplikasi dan proses pengecekan awal. uJika pengguna telah login sebelumnya, sistem akan langsung mengarahkan ke halaman Home tanpa perlu melakukan login ulang.

---

##  Dokumentasi / Screenshot Aplikasi

*Pratinjau tampilan antarmuka (User Interface) dari aplikasi SmartSplit:*

| SplashScreen (1) | SplashScreen (2) | OnBoarding (1) |
| :---: | :---: | :---: |
|<img width="427" height="947" alt="1  SplashScreen (1)" src="https://github.com/user-attachments/assets/5ac7f00a-702a-4682-8c96-bf67b4368124" />|<img width="427" height="947" alt="1  SplashScreen (2)" src="https://github.com/user-attachments/assets/819975fc-a99e-4797-9842-9a8ac0ff71dc" />|<img width="427" height="947" alt="1  OnBoarding (1)" src="https://github.com/user-attachments/assets/c77e8de7-a946-49d7-8cc4-012c8a4d16cb" />| 

| OnBoarding (2) | OnBoarding (3) | Login (Light) |
| :---: | :---: | :---: |
|<img width="420" height="942" alt="2  OnBoarding (2)" src="https://github.com/user-attachments/assets/7cb660f4-2757-4618-9da6-d9b8203be9f4" />|<img width="422" height="946" alt="2  OnBoarding (3)" src="https://github.com/user-attachments/assets/635b1b1f-149d-4feb-963b-b302d125e850" />|<img width="427" height="947" alt="1  Login (1)" src="https://github.com/user-attachments/assets/36d105ec-d508-40dd-bb5e-8036279e9e0b" />|

| Login (Dark) | Register (Light) |  Register (Dark) |
| :---: | :---: | :---: |
|<img width="420" height="918" alt="3  Login (2)" src="https://github.com/user-attachments/assets/fcec4feb-f7b7-4ffb-89b2-2a84527db56e" />|<img width="425" height="943" alt="4  Register (1)" src="https://github.com/user-attachments/assets/d9433af3-801b-47f4-9e91-c2edb90d4224" />|<img width="416" height="907" alt="4  Register (2)" src="https://github.com/user-attachments/assets/44657c9a-be7d-438c-81f2-b0aa128cead4" />|

| Home Empty (Light) | Home Empty (Dark) |  CreateBill (Light) |
| :---: | :---: | :---: |
|<img width="422" height="917" alt="5  Home (empty)" src="https://github.com/user-attachments/assets/fe6c348d-236c-47e1-84ce-dbfbfc3544ec" />|<img width="420" height="898" alt="5  Home (empty2)" src="https://github.com/user-attachments/assets/045000cb-4753-4e33-a97d-19ce98c63838" />|<img width="422" height="942" alt="6  CreateBill (1)" src="https://github.com/user-attachments/assets/ed8cec35-0326-4b2d-9e60-4eadbc6bc8e6" />|

| CreateBill (Dark) | AddItems 1 (Light) |  AddItems 1 (Dark) |
| :---: | :---: | :---: |
|<img width="422" height="918" alt="6  CreateBill (2)" src="https://github.com/user-attachments/assets/89f8e489-3dd1-44eb-8160-4a4eca7cac92" />|<img width="425" height="946" alt="7  AddItems (1)" src="https://github.com/user-attachments/assets/d253db7f-97b9-435e-b4c5-f7362e8572e4" />|<img width="421" height="918" alt="7  AddItems (1dark)" src="https://github.com/user-attachments/assets/c0e7d14e-5a37-4587-ace0-7a4869618edd" />|

| AddItems 2 (Light) | AddItems 2 (Dark) |  AddItems 3 (Light) |
| :---: | :---: | :---: |
|<img width="422" height="941" alt="7  AddItems (2)" src="https://github.com/user-attachments/assets/9f67578d-86f8-49b8-bdd4-09429b02e1f1" />|<img width="420" height="922" alt="7  AddItems (2dark)" src="https://github.com/user-attachments/assets/bc6601eb-17e9-483c-a17f-ef2d5383d5a8" />|<img width="425" height="945" alt="7  AddItems (3)" src="https://github.com/user-attachments/assets/f31942bf-ecda-49dd-9f1b-4a9cc24b0ab8" />|

| AddItems 3 (Dark) | DeleteItems |  EditItems (Light) |
| :---: | :---: | :---: |
|<img width="422" height="917" alt="7  AddItems (3dark)" src="https://github.com/user-attachments/assets/f4da635f-ad6a-440b-94a2-48a738490a6c" />|<img width="420" height="942" alt="8  DeleteItems" src="https://github.com/user-attachments/assets/637584f2-70a1-468a-a265-d78bd6f86ece" />|<img width="422" height="943" alt="8  EditItems (1)" src="https://github.com/user-attachments/assets/a02c9cbc-4850-405a-93ba-229c77cb0435" />|

| EditItems (Dark) | DetailScreen (Light) |  DetailScreen (Dark) |
| :---: | :---: | :---: |
|<img width="421" height="930" alt="8  EditItems (2)" src="https://github.com/user-attachments/assets/8a1116a3-9dc8-494a-861c-5891a4ccb79a" />|<img width="422" height="941" alt="9  DetailScreen (1)" src="https://github.com/user-attachments/assets/6ec7c665-67ec-4a79-bb5e-29afeb3ec2ef" />|<img width="420" height="922" alt="9  DetailScreen (2)" src="https://github.com/user-attachments/assets/cd10f7f0-db53-4666-8c80-81dd0526af59" />|

| Share | Home (Light) |  Home (Dark) |
| :---: | :---: | :---: |
|<img width="421" height="936" alt="10  Share" src="https://github.com/user-attachments/assets/c7330d87-0fe4-4182-9b89-4f2ad86a80e6" />|<img width="422" height="942" alt="11  Home (1filled)" src="https://github.com/user-attachments/assets/f1f9e13c-86a6-4349-8fd2-d51c340b73d3" />|<img width="421" height="900" alt="11  Home (2filled)" src="https://github.com/user-attachments/assets/0eaf6b72-1f7b-4fd4-b937-a9e8df5aa6f2" />|

| Settings (Light) | Settings (Dark) |  Profile (Light) |
| :---: | :---: | :---: |
|<img width="422" height="945" alt="12  Settings (1)" src="https://github.com/user-attachments/assets/639e55d7-6b70-4f22-9354-376796b8e4d8" />|<img width="422" height="917" alt="12  Settings (2)" src="https://github.com/user-attachments/assets/b06b3cca-92e4-4e66-a6ad-df5f42584d02" />|<img width="423" height="943" alt="13  Profile (1)" src="https://github.com/user-attachments/assets/933829d9-5a09-485c-9a81-7a09cceeb565" />|

| Profile (Dark) | EditProfile (Light) |  EditProfile (Dark) |
| :---: | :---: | :---: |
|<img width="422" height="917" alt="13  Profile (2)" src="https://github.com/user-attachments/assets/9ec01575-25f4-4a74-9828-908fdc6caf65" />|<img width="423" height="947" alt="14  EditProfil (1)" src="https://github.com/user-attachments/assets/77d9cfad-847d-4cda-b9da-84898ba7e697" />|<img width="422" height="917" alt="14  EditProfil (2)" src="https://github.com/user-attachments/assets/522bfd6a-f9a6-4dda-911a-0b798dce8666" />|

---

##  Anggota Kelompok

Aplikasi ini dikembangkan secara kolaboratif oleh:

* **M. Anshari** (2410817310008)
* **Rabiah Riska Amaliah** (2410817320010)

---

## Lisensi

Proyek ini dibangun, dikembangkan, dan dipublikasikan secara khusus untuk memenuhi **Ujian Akhir Semester (UAS) Pemrograman Mobile**. Hak cipta penuh dari seluruh kode sumber ini dipegang oleh tim pengembang internal kelompok.
