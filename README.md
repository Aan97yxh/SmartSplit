# SmartSplit — Aplikasi Mobile Split Bill

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android Badge">
  <img src="https://img.shields.io/badge/Language-Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin Badge">
  <img src="https://img.shields.io/badge/UI--Framework-Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Compose Badge">
  <img src="https://img.shields.io/badge/Architecture-MVVM-FF6F00?style=for-the-badge" alt="MVVM Badge">
</p>

**SmartSplit** adalah aplikasi Android yang membantu pengguna membagi tagihan secara adil dan praktis. Pengguna dapat menentukan item yang dipesan oleh masing-masing orang, menghitung pajak dan service charge secara otomatis, melacak status pembayaran anggota, serta mengelola riwayat transaksi secara offline menggunakan Room Database.

---

## 🚀 Fitur Utama

Aplikasi ini dilengkapi dengan fitur wajib serta fitur tambahan untuk meningkatkan pengalaman pengguna:

### 📌 Fitur Wajib (Core Features)
*   **Autentikasi Pengguna:** Sistem *Login* & *Register* multi-akun dilengkapi fitur *Auto-Login*.
*   **Split Bill Berbasis Item (*Item-Based Split*):** Menentukan pesanan secara spesifik untuk tiap-tiap orang agar pembagian adil.
*   **Kalkulator Otomatis:** Perhitungan *Tax* (Pajak) dan *Service Charge* langsung dihitung secara presisi.
*   **Riwayat Transaksi:** Melihat transaksi lama.
*   **Edit & Delete Item:** Dapat mengedit dan menghapus item sebelum melakukan validasi.
*   **Pelacakan Pembayaran (*Payment Tracking*):** Memantau siapa saja anggota yang sudah membayar tagihan.
*   **Sinkronisasi Lokal:** Penyimpanan data persisten yang aman menggunakan Room Database.

### 🌟 Fitur Tambahan (Extra Features)
*   **Jastip Mode (Jasa Titip):** Fitur untuk mengonversi mata uang asing secara otomatis menggunakan data kurs dari API.
*   **Notifikasi Pengingat (*Reminder*):** Mengirimkan peringatan otomatis untuk tagihan yang belum diselesaikan.
*   **Manajemen Profil:** Pengaturan informasi akun pengguna termasuk penyimpanan foto profil.

---

## 🛠️ Teknologi yang Digunakan

| Komponen | Teknologi & Library | Deskripsi |
| :--- | :--- | :--- |
| **Frontend** | Kotlin, Jetpack Compose, Material 3 | Membangun UI deklaratif yang modern, adaptif, dan responsif. |
| **Architecture** | MVVM, Repository Pattern, Clean Architecture | Menjaga kode tetap rapi, mudah diuji (*testable*), dan *scalable*. |
| **Local Database** | Room Database, SQLite | Mengelola penyimpanan data relasional lokal secara efisien. |
| **Networking** | Retrofit, OkHttp, HttpLoggingInterceptor | Menangani *request* ke API eksternal dengan performa tinggi. |
| **Serialization** | Kotlinx Serialization | Melakukan konversi data JSON ke objek Kotlin secara *type-safe*. |
| **Background Task**| WorkManager | Menjalankan *background processing* untuk memicu sistem pengingat tagihan. |

### 🌐 Integrasi API Eksternal
Fitur konversi mata uang pada **Jastip Mode** memanfaatkan **Frankfurter API** untuk mendapatkan nilai tukar secara *real-time*.
*   **Endpoint:** `GET /latest`
*   **Contoh Request:** `https://api.frankfurter.app/latest?from=USD&to=IDR`

---

## 📂 Struktur Folder (Architecture & Package Structure)

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
## 🗄️ Skema Database (Room Entities)

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

> 💡 *Catatan:* Karena Room tidak mendukung penyimpanan tipe data objek kompleks secara langsung, kelas `Converter` digunakan di dalam paket `data/local/converter` untuk mengubah struktur data `List` menjadi format teks (`String` JSON) secara otomatis menggunakan *Kotlinx Serialization*.

---

## ⏰ Notifikasi Pengingat (Background Task)

Untuk menjamin tidak ada tagihan kelompok yang terlupakan, SmartSplit memanfaatkan dependensi **WorkManager** untuk menjalankan tugas di latar belakang (*background task*):

* **Logika Sistem:** Setiap kali sebuah tagihan baru dibuat dengan status *Belum Lunas*, `WorkManager` akan menjadwalkan tugas pemeriksaan berkala.
* **Durasi Batas Waktu:** Jika dalam kurun waktu **24 jam** sejak pembuatan awal tagihan tersebut belum ditandai sebagai *Lunas*, sistem latar belakang akan secara otomatis memicu pembuatan **Notification** lokal.
* **Pengalaman Pengguna:** Notifikasi pengingat (*reminder*) akan muncul di *status bar* perangkat Android untuk memperingatkan pengguna agar segera menagih atau menyelesaikan pembayaran anggota kelompok.

---

## 💻 Cara Menjalankan Aplikasi

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
5.  **Memulai Sesi** Pada peluncuran pertama, masuklah ke halaman *Register* terlebih dahulu untuk membuat akun baru, lalu masuk via *Login Screen* untuk mulai menikmati fitur pelacakan tagihan.

---

## 📸 Dokumentasi / Screenshot Aplikasi

*Pratinjau tampilan antarmuka (User Interface) dari aplikasi SmartSplit:*

| Login Screen | Dashboard Screen | Create Bill Screen |
| :---: | :---: | :---: |
| <img src="[https://placehold.co/300x600/png?text=Login+Screen](https://placehold.co/300x600/png?text=Login+Screen)" width="180"> | <img src="[https://placehold.co/300x600/png?text=Dashboard+Screen](https://placehold.co/300x600/png?text=Dashboard+Screen)" width="180"> | <img src="[https://placehold.co/300x600/png?text=Create+Bill+Screen](https://placehold.co/300x600/png?text=Create+Bill+Screen)" width="180"> |

| Bill Detail Screen | History Screen | Profile Screen |
| :---: | :---: | :---: |
| <img src="[https://placehold.co/300x600/png?text=Bill+Detail+Screen](https://placehold.co/300x600/png?text=Bill+Detail+Screen)" width="180"> | <img src="[https://placehold.co/300x600/png?text=History+Screen](https://placehold.co/300x600/png?text=History+Screen)" width="180"> | <img src="[https://placehold.co/300x600/png?text=Profile+Screen](https://placehold.co/300x600/png?text=Profile+Screen)" width="180"> |

> 💡 *Tips Pengembangan:* Kamu bisa mengganti atribut `src` pada tag gambar (`<img>`) di atas menggunakan tautan gambar screenshot asli proyekmu setelah diunggah ke GitHub Asset.

---

## 👥 Anggota Kelompok

Aplikasi ini dikembangkan secara kolaboratif oleh:

* **M. Anshari** — 2410817310008.
* **Rabiah Riska Amaliah** — 2410817320010.

---

## 📄 Lisensi

Proyek ini dibangun, dikembangkan, dan dipublikasikan secara khusus untuk memenuhi tugas **Ujian Akhir Semester (UAS) Pemrograman Mobile**. Hak cipta penuh dari seluruh kode sumber ini dipegang oleh tim pengembang internal kelompok.
