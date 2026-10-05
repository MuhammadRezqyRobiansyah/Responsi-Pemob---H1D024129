# CineExplore - Aplikasi Eksplorasi Film dan Serial Televisi

> **Tugas Responsi Praktikum Pemrograman Mobile (Shift A)**  
> **Nama:** Muhammad Rezqy Robiansyah  
> **NIM:** H1D024129  
> **Program Studi:** Informatika  
> **Link Video Presentasi:** [Tonton Video Demo & Penjelasan Kode](LINK_VIDEO_YOUTUBE_ATAU_DRIVE_DISINI)

---

## 1. Deskripsi Singkat Aplikasi

**CineExplore** adalah aplikasi mobile berbasis Android yang dikembangkan menggunakan **Kotlin**, **Jetpack Compose**, dan **Material Design 3**. Aplikasi ini memudahkan pengguna untuk mencari dan melihat informasi mengenai film maupun serial televisi secara cepat, dinamis, dan responsif. 

Aplikasi ini mengintegrasikan data publik dari **TVmaze REST API** untuk menampilkan katalog tayangan dengan detail lengkap seperti judul, tahun rilis, rating, daftar genre, hingga ringkasan/sinopsis yang diformat dengan bersih.

---

## 2. Demo Video & Screenshot Aplikasi

### 🎥 Video Demo & Code Walkthrough
▶️ **Link Video:** [Klik di sini untuk menonton Video Demo & Penjelasan Kode di YouTube / Google Drive](LINK_VIDEO_YOUTUBE_ATAU_DRIVE_DISINI)

*(Pastikan video diset ke **Unlisted / Public** di YouTube atau **Anyone with the link can view** di Google Drive)*

### 📱 Tangkapan Layar (Screenshots)

| Home Screen (Grid Film) | Detail Screen (Informasi & Sinopsis) | Pencarian & State Feedback |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/e5eb6bd8-52ad-41f5-9b20-179059c4bb33" width="260" alt="Home Screen" /> | <img src="https://github.com/user-attachments/assets/159d5585-cccf-4491-9c3e-f98a1cfa7ae6" width="260" alt="Detail Screen" /> | <img src="https://github.com/user-attachments/assets/272b5072-5c58-4629-8f05-c4e74cd5ecb5" width="260" alt="Search Screen" /> |

---

## 3. Fitur Utama

- **Pencarian Dinamis (Live Search API):** Mengambil data film dan serial televisi langsung dari endpoint TVmaze API saat pengguna memasukkan kata kunci.
- **Katalog Berbasis Grid (`LazyVerticalGrid`):** Menampilkan daftar kartu film dalam layout 2 kolom yang adaptif dan estetik.
- **Layar Detail Komprehensif:** Menampilkan poster film, judul, tahun rilis, skor rating (bintang), chip genre tayangan, dan sinopsis lengkap yang otomatis dibersihkan dari tag format HTML.
- **Navigasi Mulus & State Preservation:** Menggunakan Navigation Compose dengan pemindahan argumen dinamis (`detail/{showId}`) di mana hasil pencarian di Home Screen tetap terjaga saat pengguna kembali dari halaman detail.
- **State-Driven UI & Feedback Lengkap:** Menangani berbagai kondisi antarmuka secara elegan: *Loading Indicator*, *Empty State* (jika film tidak ditemukan), *Error State* (dengan tombol Coba Lagi / Retry), dan *Success State*.
- **Desain Khusus Dark Emerald Glassmorphism:** Menerapkan Material Design 3 dengan tema gelap elegan (*Obsidian Dark* `#0C1311`, *Emerald Surface* `#14221D`, glowing border halus, badge rating emas, dan chip kategori yang interaktif).

---

## 4. Penjelasan Struktur Arsitektur MVVM

Aplikasi ini secara disiplin menerapkan pola arsitektur **Model-View-ViewModel (MVVM)** dengan pemisahan tanggung jawab (*Separation of Concerns*) dan **Unidirectional Data Flow (UDF)**:

```
┌────────────────────────────────────────────────────────┐
│                   VIEW / UI LAYER                      │
│  - MainActivity (NavHost & Routing)                    │
│  - HomeScreen (Stateful & Stateless)                   │
│  - DetailScreen (Stateful & Stateless)                 │
│  - ShowItemCard & CineSearchBar (Reusable Components)  │
└───────────────────────────▲────────────────────────────┘
                            │ UI State (StateFlow)
                            ▼ Events / Intent
┌────────────────────────────────────────────────────────┐
│                   VIEWMODEL LAYER                      │
│  - TvShowViewModel                                     │
│  - SearchUiState (Idle, Loading, Success, Empty, Error)│
│  - DetailUiState (Loading, Success, Error)             │
└───────────────────────────▲────────────────────────────┘
                            │ Coroutines Result
                            ▼ Suspend Functions
┌────────────────────────────────────────────────────────┐
│                  REPOSITORY LAYER                      │
│  - TvShowRepository (Interface)                        │
│  - TvShowRepositoryImpl (Implementation)               │
└───────────────────────────▲────────────────────────────┘
                            │ API Calls
                            ▼ Data Extraction
┌────────────────────────────────────────────────────────┐
│               MODEL & NETWORK LAYER                    │
│  - Models: Show, Rating, ShowImage, SearchResultItem   │
│  - Network: TvMazeApiService, ApiClient (Retrofit)     │
└────────────────────────────────────────────────────────┘
```

### Komponen Arsitektur:
1. **Model (Data Class):**
   * Merepresentasikan struktur data yang diterima dari API.
   * Dilengkapi *helper properties* seperti `releaseYear` (ekstraksi tahun dari tanggal rilis), `ratingText` (format bintang), dan `cleanSummary` (pembersih tag HTML).
2. **Repository Layer:**
   * Berperan sebagai *Single Source of Truth* (SSOT) yang memisahkan akses API dari ViewModel.
   * `TvShowRepositoryImpl` memanggil `TvMazeApiService`, menangani pengecualian (exceptions), dan membungkus hasil dalam `Result<List<Show>>`.
3. **ViewModel Layer:**
   * `TvShowViewModel` bertugas mengelola state UI tanpa bergantung pada elemen Android UI.
   * Mengekspos `StateFlow<SearchUiState>` dan `StateFlow<DetailUiState>` yang diobservasi secara reaktif oleh Composable.
   * Menyediakan *cache-first strategy* agar pembukaan layar detail terasa instan tanpa jeda layar kosong.
4. **View / UI Layer (State Hoisting):**
   * Setiap Composable dibagi menjadi:
     * **Stateful:** Menerima `ViewModel` & `NavController`, memproses event, dan mengelola alur.
     * **Stateless:** Hanya menerima data murni dan *event lambda callback*, memastikan komponen mudah diuji (*testable*) dan dipratinjau (*previewable*).

---

## 5. Penjelasan Penggunaan API (TVmaze REST API)

Aplikasi ini menggunakan **TVmaze Public API** ([https://www.tvmaze.com/api](https://www.tvmaze.com/api)) yang tidak memerlukan API key autentikasi.

* **Base URL:** `https://api.tvmaze.com/`
* **Library HTTP Client:** Retrofit 2 (`2.11.0`) dengan `GsonConverterFactory`.

### Endpoint yang Digunakan:

#### 1. Pencarian Tayangan
* **HTTP Method:** `GET`
* **Endpoint:** `search/shows?q={query}`
* **Deskripsi:** Mengembalikan daftar film/serial yang relevan dengan kata kunci query.
* **Contoh Respon:**
  ```json
  [
    {
      "score": 0.9,
      "show": {
        "id": 139,
        "name": "Girls",
        "premiered": "2012-04-15",
        "genres": ["Drama", "Romance"],
        "rating": { "average": 6.6 },
        "image": {
          "medium": "https://static.tvmaze.com/uploads/images/medium_portrait/31/78286.jpg",
          "original": "https://static.tvmaze.com/uploads/images/original_untouched/31/78286.jpg"
        },
        "summary": "<p>This Emmy winning series follows four young women...</p>"
      }
    }
  ]
  ```

#### 2. Detail Tayangan
* **HTTP Method:** `GET`
* **Endpoint:** `shows/{id}`
* **Deskripsi:** Mengambil detail lengkap dari satu tayangan spesifik berdasarkan ID uniknya.

---

## 6. Teknologi & Pustaka Dependensi

| Kategori | Teknologi / Pustaka | Versi |
| :--- | :--- | :--- |
| **Bahasa Pemrograman** | Kotlin | `2.2.10` |
| **UI Framework** | Jetpack Compose (BOM) | `2026.02.01` |
| **Desain Komponen** | Material Design 3 | `1.4.0` |
| **Navigasi** | Jetpack Navigation Compose | `2.8.0` |
| **Networking** | Retrofit 2 + Gson Converter | `2.11.0` |
| **Image Loading** | Coil Compose | `2.7.0` |
| **Arsitektur & State** | Kotlin Coroutines & StateFlow | Lifecycle `2.8.6` |
| **Build System** | Android Gradle Plugin (AGP) | `9.4.1` |

---

## 7. Cara Menjalankan Proyek di Android Studio

1. **Clone Repositori:**
   ```bash
   git clone https://github.com/MuhammadRezqyRobiansyah/Responsi-Pemob---H1D024129.git
   ```
2. **Buka Proyek:**
   * Buka aplikasi **Android Studio**.
   * Pilih menu **File** > **Open**, lalu pilih folder repositori ini.
3. **Gradle Sync:**
   * Tunggu Android Studio menyelesaikan sinkronisasi Gradle dependencies.
4. **Jalankan Aplikasi:**
   * Pilih target Emulator atau Device fisik (Android 8.0 / API 26 ke atas).
   * Klik tombol **Run ▶️** (atau tekan `Shift + F10`).
