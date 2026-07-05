UMNU HUB
UMNU HUB adalah aplikasi media sosial berbasis Android yang dibuat untuk lingkungan Universitas Ma'arif NU. Aplikasi ini memungkinkan pengguna untuk berbagi postingan, memberikan like, dan berkomentar sehingga memudahkan komunikasi antar mahasiswa.
Fitur
-Login pengguna 
-Registrasi akun 
-Membuat postingan 
-Menampilkan daftar postingan 
-Memberikan Like 
-Memberikan Komentar 
-Profil pengguna 
-Logout 
Tech Stack & Library
Frontend
-Java 
-Android Studio 
-XML Layout 
Backend
-PHP 
-MySQL 
-XAMPP (Apache & MySQL) 
Library
-Volley (HTTP Request) 
-RecyclerView 
-CardView 
-Material Components 
Cara Menjalankan Aplikasi
Persyaratan
-Android Studio 
-JDK 
-XAMPP 
-MySQL 
-Perangkat Android atau Emulator 
Langkah Instalasi
1.Clone repository 
  git clone https://github.com/annajih17/UMNUHUB.git
2.Buka project menggunakan Android Studio. 
3.Jalankan XAMPP kemudian aktifkan: 
-Apache 
-MySQL 
3.Import database MySQL ke phpMyAdmin. 
  Link download database dan API
  https://drive.google.com/drive/folders/1_RAmS2bz67A1VEC4JTrQv4cfMctupNlX?usp=drive_link
4.Salin folder backend PHP ke direktori: 
  xampp/htdocs/

Cek IP WIFI
-Tekan Win + R.
  Ketik cmd, lalu tekan Enter.
  Ketik:
    ipconfig
-Cari bagian Wireless LAN adapter Wi-Fi.
-Lihat nilai IPv4 Address, misalnya:
  IPv4 Address. . . . . . . . . . : 192.168.1.15

Android Studio
1.Ubah alamat IP pada file Java sesuai IP komputer server, misalnya: 
  http://192.168.x.x/umnuhub/
2.Build dan jalankan aplikasi pada emulator atau perangkat Android yang berada dalam jaringan WiFi yang sama. 
Struktur Proyek
UMNUHUB/
├── app/
├── java/
├── res/
├── AndroidManifest.xml
└── build.gradle

Demo Aplikasi
Video demo dapat diakses melalui:
YouTube:
  https://youtube.com/shorts/gEdBmaX7BW0?si=yEzR7TPTjq6Iuhpo
Repository
  https://github.com/annajih17/UMNUHUB
Pengembang
Kelompok Proyek UMNU HUB
-Muhammad Fathin Abi Putra (TI1024112)
-Muhammad Najih Sahal (TI1024110)
-Yusron Fadjri (TI1024098)
-Wafiq Zuliani Jazilatun Nafisa (TI1024109)
-Eka Nurmadania Izati (TI1024114)
