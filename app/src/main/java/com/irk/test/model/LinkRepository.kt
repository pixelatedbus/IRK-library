package com.irk.test.model

import com.irk.test.model.BoyerMoore.search

object LinkRepository {
    private const val BASE_URL = "https://informatika.stei.itb.ac.id/~rinaldi.munir/"
    private const val MATDIS_BASE_URL = "$BASE_URL/Matdis"
    private const val ALGEO_BASE_URL = "$BASE_URL/AljabarGeometri"
    private const val STIMA_BASE_URL = "$BASE_URL/Stmik/2025"

    val allReferences = listOf(
        ReferenceItem(
            title = "Pengantar Aljabar Linier dan Geometri (2024)",
            url = "$ALGEO_BASE_URL/Algeo-00-Pengantar-Aljabar-Geometri-(2024).pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Review matriks (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-01-Review-Matriks-2023.pdf",
            material = Material.ALGEO

        ),
        ReferenceItem(
            title = "Matriks eselon (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-02-Matriks-Eselon-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Sistem persamaan linier (Bagian 1: Metode eliminasi Gauss) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-03-Sistem-Persamaan-Linier-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Sistem persamaan linier (Bagian 2: Tiga kemungkinan solusi sistem persamaan linier (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-04-Tiga-Kemungkinan-Solusi-SPL-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Sistem persamaan linier (Bagian 3: Metode eliminasi Gauss-Jordan) (Update 2024)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-05-Sistem-Persamaan-Linier-2-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Contoh-contoh pemodelan sistem persamaan linier dalam dunia nyata dan sains (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-06-Aplikasi-SPL-1-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Contoh aplikasi eliminasi Gauss di dalam metode numerik (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-07-Aplikasi-SPL-2-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Pengantar pemrograman dengan Bahasa Java (Update 2024)",
            url = "$ALGEO_BASE_URL/Algeo-10-Pengantar-Pemrograman-dengan-Bahasa-Java-2024.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Vektor di ruang Euclidean (Bagian 1) (Update 2024)",
            url = "$ALGEO_BASE_URL/Algeo-11-Vektor-di-Ruang-Euclidean-Bag1-2024.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Vektor di ruang Euclidean (Bagian 2) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-12-Vektor-di-Ruang-Euclidean-Bag2-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Vektor di ruang Euclidean (Bagian 3) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-13-Vektor-di-Ruang-Euclidean-Bag3-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Aplikasi dot product pada information retrieval (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-14-Aplikasi-dot-product-pada-IR-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Ruang vektor umum (Bagian 1) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-15-Ruang-vektor-umum-Bagian1-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Ruang vektor umum (Bagian 2) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-16-Ruang-vektor-umum-Bagian2-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Ruang vektor umum (Bagian 3) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-17-Ruang-vektor-umum-Bagian3-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Ruang vektor umum (Bagian 4) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-18-Ruang-vektor-umum-Bagian4-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Nilai Eigen dan Vektor Eigen (Bagian 1) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-19-Nilai-Eigen-dan-Vektor-Eigen-Bagian1-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Nilai Eigen dan Vektor Eigen (Bagian 2) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-20-Nilai-Eigen-dan-Vektor-Eigen-Bagian2-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Singular Value Decomposition (SVD) (Bagian 1) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-21-Singular-value-decomposition-Bagian1-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Singular Value Decomposition (SVD) (Bagian 2) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-22-Singular-value-decomposition-Bagian2-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Dekomposisi LU (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-23-Dekomposisi-LU-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Dekomposisi QR (Materi baru 2024)",
            url = "$ALGEO_BASE_URL/Algeo-23b-Dekomposisi-QR-2024.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Aljabar kompleks (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-24-Aljabar-Kompleks-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Aljabar quaternion (Bagian 1) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-25-Aljabar-Quaternion-Bagian1-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Aljabar geometri (Bagian 1) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-27-Aljabar-Geometri-Bagian1-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Aljabar geometri (Bagian 2) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-28-Aljabar-Geometri-Bagian2-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Perkalian geometri (Bagian 1) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-29-Perkalian-Geometri-Bagian1-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Perkalian geometri (Bagian 2) (Update 2023)",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-30-Perkalian-Geometri-Bagian2-2023.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Pembahasan soal aljabar geometri dan perkalian geometri",
            url = "$ALGEO_BASE_URL/2023-2024/Algeo-31-Pembahasan-Soal-Aljabar-dan-Perkalian-Geometri.pdf",
            material = Material.ALGEO
        ),
        ReferenceItem(
            title = "Pengantar Matematika Diskrit (Versi update 2025)",
            url = "$MATDIS_BASE_URL/00-Pengantar-Matematika-Diskrit-2025.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Logika (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/01-Logika-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Himpunan (bagian 1) (Versi update 2025)",
            url = "$MATDIS_BASE_URL/02-Himpunan(2025)-1.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Himpunan (bagian 2) (Versi update 2025)",
            url = "$MATDIS_BASE_URL/03-Himpunan(2025)-2.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Himpunan (bagian 3) (Versi update 2025)",
            url = "$MATDIS_BASE_URL/04-Himpunan(2025)-3.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Relasi dan fungsi (Bagian 1) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/05-Relasi-dan-Fungsi-Bagian1-(2024).pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Relasi dan Fungsi (Bagian 2: Relasi inversi, komposisi relasi, relasi n-ary, fungsi) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/06-Relasi-dan-Fungsi-Bagian2-(2024).pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Relasi dan Fungsi (Bagian 3: fungsi khusus, relasi kesetaraan, relasi pengurutan parsial, dan klosur relasi) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/07-Relasi-dan-Fungsi-Bagian3-(2024).pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Induksi matematika (Bagian 1) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/08-Induksi-matematik-bagian1-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Induksi matematika (Bagian 2) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/09-Induksi-matematik-bagian2-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Deretan, rekursi, dan relasi rekurens (Bagian 1) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/10-Deretan, rekursi-dan-relasi-rekurens-(Bagian1)-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Deretan, rekursi, dan relasi rekurens (Bagian 2) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/11-Deretan, rekursi-dan-relasi-rekurens-(Bagian2)-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Aljabar Boolean (Bagian 1)",
            url = "$MATDIS_BASE_URL/2024-2025/12-Aljabar-Boolean-(2024)-bagian1.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Aljabar Boolean (Bagian 2)",
            url = "$MATDIS_BASE_URL/2024-2025/13-Aljabar-Boolean-(2024)-bagian2.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Aljabar Boolean (Bagian 3)",
            url = "$MATDIS_BASE_URL/2024-2025/14-Aljabar-Boolean-(2024)-bagian3.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Teori Bilangan (Bagian 1) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/15-Teori-Bilangan-Bagian1-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Teori Bilangan (Bagian 2) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/16-Teori-Bilangan-Bagian2-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Teori Bilangan (Bagian 3) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/17-Teori-Bilangan-Bagian3-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Kombinatorika (Bagian 1) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/18-Kombinatorika-Bagian1-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Kombinatorika (Bagian 2) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/19-Kombinatorika-Bagian2-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Graf (Bagian 1) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/20-Graf-Bagian1-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Graf (Bagian 2) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/21-Graf-Bagian2-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Graf (Bagian 3) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/22-Graf-Bagian3-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Pohon (Bagian 1) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/23-Pohon-Bag1-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Pohon (Bagian 2) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/24-Pohon-Bag2-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Kompleksitas algoritma (Bagian 1) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/25-Kompleksitas-Algoritma-Bagian1-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Kompleksitas algoritma (Bagian 2) (Versi update 2024)",
            url = "$MATDIS_BASE_URL/2024-2025/26-Kompleksitas-Algoritma-Bagian2-2024.pdf",
            material = Material.MATDIS
        ),
        ReferenceItem(
            title = "Pengantar Strategi Algoritma (Versi baru 2025)",
            url = "$STIMA_BASE_URL/01-Pengantar-Strategi-Algoritma-(2025).pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Brute Force (Bagian 1) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/02-Algoritma-Brute-Force-(2025)-Bag1.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Brute Force (Bagian 2) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/03-Algoritma-Brute-Force-(2025)-Bag2.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Greedy (Bagian 1) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/04-Algoritma-Greedy-(2025)-Bag1.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Greedy (Bagian 2) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/05-Algoritma-Greedy-(2025)-Bag2.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Greedy (Bagian 3) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/06-Algoritma-Greedy-(2025)-Bag3.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Divide and Conquer (Bagian 1) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/07-Algoritma-Divide-and-Conquer-(2025)-Bagian1.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Divide and Conquer (Bagian 2) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/08-Algoritma-Divide-and-Conquer-(2025)-Bagian2.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Divide and Conquer (Bagian 3) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/09-Algoritma-Divide-and-Conquer-(2025)-Bagian3.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Divide and Conquer (Bagian 4) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/10-Algoritma-Divide-and-Conquer-(2025)-Bagian4.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Decrease and Conquer (Bagian 1) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/11-Algoritma-Decrease-and-Conquer-2025-Bagian1.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma Decrease and Conquer (Bagian 2) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/12-Algoritma-Decrease-and-Conquer-2025-Bagian2.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Breadth First Search (BFS) dan Depth First Search (DFS) - Bagian 1 (Versi baru 2025)",
            url = "$STIMA_BASE_URL/13-BFS-DFS-(2025)-Bagian1.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Breadth First Search (BFS) dan Depth First Search (DFS) - Bagian 2 (Versi baru 2025)",
            url = "$STIMA_BASE_URL/14-BFS-DFS-(2025)-Bagian2.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma runut-balik (backtracking) (Bagian 1) (Versi baru 2025)",
            url = "$STIMA_BASE_URL/15-Algoritma-backtracking-(2025)-Bagian1.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma runut-balik (backtracking) (Bagian 2) (Versi update 2025)",
            url = "$STIMA_BASE_URL/16-Algoritma-backtracking-(2025)-Bagian2.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma branch and bound (Bagian 1) (Versi update 2025)",
            url = "$STIMA_BASE_URL/17-Algoritma-Branch-and-Bound-(2025)-Bagian1.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma branch and bound (Bagian 2) (Versi update 2025)",
            url = "$STIMA_BASE_URL/18-Algoritma-Branch-and-Bound-(2025)-Bagian2.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma branch and bound (Bagian 3) (Versi update 2025)",
            url = "$STIMA_BASE_URL/19-Algoritma-Branch-and-Bound-(2025)-Bagian3.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Algoritma branch and bound (Bagian 4) (Versi update 2025)",
            url = "$STIMA_BASE_URL/20-Algoritma-Branch-and-Bound-(2025)-Bagian4.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Penentuan rute (Route/Path Planning) - Bagian 1",
            url = "$STIMA_BASE_URL/21-Route-Planning-(2025)-Bagian1.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Penentuan rute (Route/Path Planning) - Bagian 2",
            url = "$STIMA_BASE_URL/22-Route-Planning-(2025)-Bagian2.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Pencocokan string (string matching) dengan algoritma brute force, KMP, Boyer-Moore",
            url = "$STIMA_BASE_URL/23-Pencocokan-string-(2025).pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Pencocokan string dengan regular expression (regex)",
            url = "$STIMA_BASE_URL/24-String-Matching-dengan-Regex-(2025).pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Program Dinamis (Dynamic Programming) - Bagian 1",
            url = "$STIMA_BASE_URL/25-Program-Dinamis-(2025)-Bagian1.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Program Dinamis (Dynamic Programming) - Bagian 2",
            url = "$STIMA_BASE_URL/26-Program-Dinamis-(2025)-Bagian2.pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Teori P, NP, dan NP-Complete (Bagian 1)",
            url = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Stmik/2019-2020/Teori-P-NP-dan-NPC-(Bagian 1).pdf",
            material = Material.STIMA
        ),
        ReferenceItem(
            title = "Teori P, NP, dan NP-Complete (Bagian 2)",
            url = "https://informatika.stei.itb.ac.id/~rinaldi.munir/Stmik/2019-2020/Teori-P-NP-dan-NPC-(Bagian 2).pdf",
            material = Material.STIMA
        )
    )
}