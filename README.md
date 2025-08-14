# IRK Library

Aplikasi Android ini adalah implementasi dari berbagai konsep fundamental dalam Lab IRK (Ilmu Rekayasa Komputer), yang dirancang sebagai alat bantu belajar visual dan interaktif.

---

## Technology Stack
Aplikasi ini dibangun menggunakan tumpukan teknologi modern untuk pengembangan Android.

* **Bahasa Pemrograman**: [Kotlin](https://kotlinlang.org/)
* **Toolkit UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Arsitektur**: [MVVM (Model-View-ViewModel)](https://developer.android.com/jetpack/guide)pengguna.
* **IDE**: [Android Studio](https://developer.android.com/studio)
* **Desain UI**: [Material Design 3](https://m3.material.io/)

---

## ⚙️ Panduan Instalasi (Installation Guide)
Untuk menjalankan proyek ini di lingkungan pengembangan lokal Anda, ikuti langkah-langkah berikut:

1.  **Clone Repositori**
    ```bash
    git clone https://github.com/pixelatedbus/IRK-library.git
    ```
2.  **Buka di Android Studio**
    * Buka Android Studio (versi terbaru direkomendasikan).
    * Pilih **File > Open** dan arahkan ke direktori tempat Anda meng-clone repositori.
3.  **Sync Gradle**
    * Android Studio akan secara otomatis menyinkronkan proyek dengan file `build.gradle.kts`. Tunggu hingga proses ini selesai untuk mengunduh semua dependensi yang diperlukan.
4.  **Jalankan Aplikasi**
    * Pilih perangkat (emulator atau perangkat fisik yang terhubung).
    * Klik tombol **Run 'app'**  untuk membangun dan menginstal aplikasi.

---

## Features Overview
Aplikasi ini dibagi menjadi lima halaman utama, masing-masing dengan fungsionalitasnya sendiri.

### Halaman 1: Matriks & SPL
Halaman ini menyediakan kalkulator matriks lengkap dengan visualisasi langkah-langkah untuk operasi kompleks.
* **Operasi Dasar**: Penjumlahan, Pengurangan, dan Perkalian.
* **Operasi Lanjutan**: Determinan, Invers, dan Eliminasi Gauss-Jordan.
* **Penyelesaian SPL**: Menyelesaikan Sistem Persamaan Linear menggunakan Kaidah Cramer atau Gauss-Jordan, dengan penanganan untuk solusi unik, banyak solusi (parametrik), dan tanpa solusi.
* **Input Dinamis**: Pengguna dapat menentukan dimensi matriks, dan grid input akan menyesuaikan secara otomatis.

[Gambar](./additional/matrik.png)

### Halaman 2: Kriptografi
Alat untuk mengenkripsi dan mendekripsi teks menggunakan algoritma klasik dan modern, dengan penjelasan matematis di setiap langkah.
* **Caesar Cipher**: Enkripsi dan dekripsi dengan pergeseran kunci yang dapat disesuaikan.
* **RSA Algorithm**:
    * Pembuatan kunci publik dan privat dari dua bilangan prima (p dan q).
    * Enkripsi plaintext dan dekripsi ciphertext.
* **Visualisasi Langkah**: Setiap langkah perhitungan ditampilkan dalam kartu yang mudah dibaca.

[Gambar](./additional/kripto.png)

### Halaman 3: Huffman Coding
Visualisasi interaktif dari algoritma Huffman Coding untuk kompresi data.
* **Analisis Frekuensi**: Tabel yang menunjukkan frekuensi kemunculan setiap karakter.
* **Konstruksi Pohon**: Animasi langkah demi langkah yang menunjukkan bagaimana pohon Huffman dibangun dari daun hingga ke akar.
* **Visualisasi Pohon**: Tampilan pohon Huffman yang digambar secara kustom pada `Canvas`.
* **Tabel Kode**: Tabel yang menampilkan kode biner Huffman untuk setiap karakter.

[Gambar](./additional/hufman.png)

### Halaman 4: Referensi
Pustaka digital materi kuliah IRK dengan fungsi pencarian yang efisien.
* **Daftar Materi**: Berisi tautan ke PDF materi kuliah Aljabar Geometri, Matematika Diskrit, dan Strategi Algoritma.
* **Filter**: Pengguna dapat memfilter materi berdasarkan mata kuliah.
* **Pencarian Cepat**: Fungsi pencarian judul menggunakan algoritma Boyer-Moore untuk hasil yang instan.
* **Tampilan PDF**: PDF dibuka menggunakan Intent untuk ditampilkan di aplikasi eksternal.

[Gambar](./additional/refere.png)

### Halaman 5: Tentang Saya
Halaman  yang berisi informasi tentang pengembang aplikasi (saya).

---

## Architecture Overview
Aplikasi ini dibangun menggunakan arsitektur **MVVM (Model-View-ViewModel)** untuk memastikan pemisahan tanggung jawab yang jelas dan kode yang dapat diuji.

* **Model**: Lapisan ini berisi logika bisnis inti dan struktur data. Ini adalah kelas Kotlin murni yang tidak bergantung pada Android SDK.
    * Contoh: `Matrix.kt`, `Crypto.kt`, `Huffman.kt`, `LinkRepository.kt`.
* **View**: Lapisan ini bertanggung jawab untuk menampilkan UI kepada pengguna. Dibangun menggunakan Jetpack Compose, lapisan ini bersifat "bodoh" (dumb) dan hanya bereaksi terhadap perubahan state dari ViewModel.
    * Contoh: `MatrixScreen.kt`, `CryptoScreen.kt`, `HuffmanScreen.kt`.
* **ViewModel**: Bertindak sebagai jembatan antara Model dan View. ViewModel mengambil data dari Model, mengubahnya menjadi state yang siap ditampilkan oleh UI (`UiState`), dan menangani semua interaksi pengguna.
    * Contoh: `MatrixViewModel.kt`, `CryptoViewModel.kt`, `HuffmanViewModel.kt`.

Alur data bersifat searah (Unidirectional Data Flow): **View** mengirimkan event ke **ViewModel**, **ViewModel** memperbarui **State**, dan **View** secara otomatis mengamati dan menampilkan **State** yang baru. Main activity mengandung navigation bar di bawah.

---

## Algorithm Implementation

### Matriks & SPL
* **Perkalian Matriks**:
    * **Brute Force**: Implementasi standar perkalian matriks dengan tiga loop bersarang. Algoritma ini mudah dipahami namun memiliki kompleksitas waktu O(n³), yang kurang efisien untuk matriks berukuran besar.
    * **Strassen's Algorithm (Divide and Conquer)**: Metode yang lebih cepat secara asimtotik dengan kompleksitas O(n^log2(7)). Algoritma ini bekerja dengan membagi matriks menjadi empat sub-matriks yang lebih kecil dan secara rekursif melakukan 7 perkalian (bukan 8 seperti pada metode standar) untuk menghitung hasilnya, sehingga mengurangi jumlah total operasi.
* **Eliminasi Gauss-Jordan**: Mengubah matriks augmented menjadi bentuk eselon baris tereduksi untuk menemukan solusi SPL.
* **Determinan**: Dihitung dengan mengubah matriks menjadi bentuk eselon baris dan mengalikan elemen diagonal, sambil melacak jumlah pertukaran baris.
* **Invers Matriks**: Ditemukan dengan mengaugmentasi matriks dengan matriks identitas dan menerapkan eliminasi Gauss-Jordan.

### Huffman Coding
* **Konstruksi Pohon**: Menggunakan `PriorityQueue` untuk secara efisien menemukan dan menggabungkan dua node dengan frekuensi terendah pada setiap langkah, membangun pohon dari daun ke akar.
* **Visualisasi**: Posisi (x, y) setiap node dihitung di ViewModel. Posisi-y ditentukan oleh kedalaman node, sedangkan posisi-x dihitung dari bawah ke atas untuk memastikan parent selalu berada di tengah anak-anaknya.

### Pencocokan String
* **Boyer-Moore**: Digunakan di halaman Referensi untuk pencarian judul. Algoritma ini mengimplementasikan heuristik "bad character" untuk melakukan pergeseran besar pada setiap ketidakcocokan, membuatnya sangat efisien untuk pencarian teks. Tabel *bad character* dibuat menggunakan `Map<Char, Int>` untuk fleksibilitas dan keterbacaan.

## Identity
Lutfi Hakim Yusra 13523084