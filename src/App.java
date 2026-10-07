/*
 * Program Proyek CLI dari Class Diagram Struk Siqlo
 * @author: Deio Castello Sujati (825250002), Felicia Rivera (825250003), MUhammad Ubait Dhaifullah (825250012)
 */

import com.struk.Barang;
import com.struk.Pakaian;
import com.struk.Aksesoris;
import com.struk.Pembayaran;
import com.struk.PembayaranTunai;
import com.struk.PembayaranNonTunai;
import com.struk.Struk;
import com.struk.CetakStruk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

/*
 * Main Program - UTS Pemrograman Berorientasi Objek (Proyek CLI)
 * Studi kasus: Struk belanja Siqlo
 * -----------------------------------------------------------------
 * - Katalog barang           : dibaca dari file src/barang.txt
 * - Kasir pilih barang       : interaktif lewat CLI (Scanner)
 * - Input metode pembayaran  : interaktif lewat CLI (Scanner)
 * - Object dibuat polymorphic sesuai pilihan/tipe data
 * - Struk & konfirmasi ditampilkan lewat interface CetakStruk
 * - Hasil akhir struk disimpan ke file src/struk_output.txt (append)
 */
public class App {

    private static final String KODE_TOKO = "1510";
    private static final String NOMOR_KASIR = "31";
    private static final String NOMOR_URUT = "0098";

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("=== Program Struk Belanja Siqlo (UTS PBO) ===\n");

        LocalDateTime sekarang = LocalDateTime.now();

        String tanggal = sekarang.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String waktu = sekarang.format(DateTimeFormatter.ofPattern("HH:mm"));
        String nomorResi = KODE_TOKO + "-"
                + sekarang.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                + "-" + NOMOR_KASIR + "-" + NOMOR_URUT;

        Struk struk = new Struk(KODE_TOKO, nomorResi, tanggal, waktu);

        // 1) Muat katalog barang dari file ke list (belum masuk struk)
        ArrayList<Barang> katalog = muatKatalogBarang("src/barang.txt");

        // 2) Kasir pilih barang satu per satu dari katalog
        pilihBarangDariKatalog(input, struk, katalog);

        // 3) Subtotal & total dihitung setelah barang dipilih
        struk.setDiskon(25000f); // Welcome Coupon
        struk.hitungTotal();
        System.out.printf(Locale.US, "%nTotal yang harus dibayar : Rp %,.0f%n%n", struk.getTotal());

        // 4) CLI: Metode pembayaran (Tunai / Non-Tunai)
        Pembayaran pembayaran = pilihMetodePembayaran(input, struk.getTotal(), tanggal);
        struk.setPembayaran(pembayaran);

        // 5) Konfirmasi pembayaran
        if (pembayaran.konfirmasiPembayaran()) {

            if (pembayaran instanceof PembayaranTunai) {
                PembayaranTunai pt = (PembayaranTunai) pembayaran;
                System.out.println("Pembayaran tunai diterima.");
                System.out.printf(Locale.US, "Uang diterima : Rp %,.0f%n", pt.getUangDiterima());
                System.out.printf(Locale.US, "Kembalian     : Rp %,.0f%n", pt.hitungKembalian());
                if (pt.hitungKembalian() == 0f) {
                    System.out.println("(uang pas, tidak ada kembalian)");
                }
                System.out.println();
            }

            if (pembayaran instanceof CetakStruk) {
                System.out.println("(Pembayaran online terverifikasi, menampilkan konfirmasi...)\n");
                CetakStruk konfirmasiOnline = (CetakStruk) pembayaran;
                konfirmasiOnline.cetak();
            }

            CetakStruk tampilan = struk;
            tampilan.cetak();

            simpanStrukKeFile(struk, "src/struk_output.txt");

        } else {
            if (pembayaran instanceof PembayaranTunai) {
                PembayaranTunai pt = (PembayaranTunai) pembayaran;
                float kurang = pt.getJumlahBayar() - pt.getUangDiterima();
                System.out.printf(Locale.US,
                        "Pembayaran gagal: uang kurang Rp %,.0f. Transaksi dibatalkan.%n", kurang);
            } else {
                System.out.println("Pembayaran gagal diverifikasi.");
            }
        }

        input.close();
    }

    /*
     * Membaca file src/barang.txt baris per baris, lalu membuat object
     * Pakaian atau Aksesoris (polymorphic) tergantung kolom pertama.
     * Hasilnya dikembalikan sebagai ArrayList<Barang> (katalog),
     * BELUM dimasukkan ke struk. Kasir yang akan memilih.
     *
     * Format per baris:
     * TIPE,kodeBarang,namaBarang,qty,harga,keterangan
     */
    private static ArrayList<Barang> muatKatalogBarang(String namaFile) {
        ArrayList<Barang> katalog = new ArrayList<>();

        try {
            File fileBarang = new File(namaFile);
            Scanner reader = new Scanner(fileBarang);

            while (reader.hasNextLine()) {
                String baris = reader.nextLine();
                if (baris.trim().isEmpty()) continue;

                String[] data = baris.split(",");
                String tipe = data[0].trim();
                String kode = data[1].trim();
                String nama = data[2].trim();
                int qty = Integer.parseInt(data[3].trim());
                float harga = Float.parseFloat(data[4].trim());
                String keterangan = data[5].trim();

                // Polymorphic object creation
                Barang barang;
                if (tipe.equalsIgnoreCase("PAKAIAN")) {
                    barang = new Pakaian(kode, nama, qty, harga, keterangan);
                } else {
                    barang = new Aksesoris(kode, nama, qty, harga, keterangan);
                }

                katalog.add(barang);
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File " + namaFile + " tidak ditemukan.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Format qty/harga di " + namaFile + " tidak valid.");
            e.printStackTrace();
        }

        return katalog;
    }

    /*
     * Menu CLI interaktif: kasir memilih barang dari katalog satu per satu.
     * Ketik nomor barang, atau 0 untuk selesai.
     */
    private static void pilihBarangDariKatalog(Scanner input, Struk struk, ArrayList<Barang> katalog) {
        if (katalog.isEmpty()) {
            System.out.println("Katalog barang kosong. Transaksi dibatalkan.");
            return;
        }

        System.out.println("=== Daftar Barang Tersedia ===");
        for (int i = 0; i < katalog.size(); i++) {
            Barang b = katalog.get(i);
            String tipe = (b instanceof Pakaian) ? "Pakaian" : "Aksesoris";
            System.out.printf(Locale.US, "%2d. [%-10s] %-25s Rp %,.0f%n",
                    i + 1, tipe, b.getNamaBarang(), b.getHarga());
        }
        System.out.println(" 0. Selesai memilih\n");  

        while (true) {
            System.out.print("Masukkan nomor barang (0 = selesai): ");
            String line = input.nextLine().trim();

            int pilihan;
            try {
                pilihan = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Input tidak valid, coba lagi.\n");
                continue;
            }

            if (pilihan == 0) break;

            if (pilihan < 1 || pilihan > katalog.size()) {
                System.out.println("Nomor tidak ada di daftar, coba lagi.\n");
                continue;
            }

            Barang dipilih = katalog.get(pilihan - 1);
            struk.tambahBarang(dipilih);

            System.out.printf(Locale.US, "  + %s (Rp %,.0f) ditambahkan ke struk.%n%n",
                    dipilih.getNamaBarang(), dipilih.getHarga());
        }
    }

    /*
     * Menu CLI interaktif untuk memilih metode pembayaran.
     * Mengembalikan object Pembayaran (polymorphic).
     */
    private static Pembayaran pilihMetodePembayaran(Scanner input, float total, String tanggal) {
        Pembayaran pembayaran = null;

        while (pembayaran == null) {
            System.out.println("Pilih metode pembayaran:");
            System.out.println("  1. Tunai");
            System.out.println("  2. Non-Tunai (Kartu)");
            System.out.print("Masukkan pilihan (1/2): ");
            String pilihan = input.nextLine().trim();

            if (pilihan.equals("1")) {
                // --- Tunai ---
                System.out.print("Masukkan jumlah uang diterima: Rp");
                try {
                    float uangDiterima = Float.parseFloat(input.nextLine().trim());
                    pembayaran = new PembayaranTunai(total, tanggal, uangDiterima);
                } catch (NumberFormatException e) {
                    System.out.println("Input tidak valid, coba lagi.\n");
                }

            } else if (pilihan.equals("2")) {
                // --- Non-Tunai ---
                System.out.print("Masukkan nomor kartu (hanya angka, maks 10 digit): ");
                int nomorKartu;
                try {
                    nomorKartu = Integer.parseInt(input.nextLine().trim());
                } catch (NumberFormatException e) {
                    System.out.println("Nomor kartu harus angka. Coba lagi.\n");
                    continue;
                }
                System.out.print("Masukkan jenis kartu (mis. JCB): ");
                String jenisKartu = input.nextLine().trim();
                pembayaran = new PembayaranNonTunai(total, tanggal, nomorKartu, jenisKartu);

            } else {
                System.out.println("Pilihan tidak dikenali, coba lagi.\n");
            }
        }
        return pembayaran;
    }

    /*
     * Menulis ringkasan struk ke file dengan mode APPEND.
     * Jadi history transaksi sebelumnya tetap ada.
     * Ditambah 1 baris kosong sebagai pemisah antar transaksi.
     */
    private static void simpanStrukKeFile(Struk struk, String namaFile) {
        try {
            FileWriter fw = new FileWriter(namaFile, true); // append

            fw.write("Kode Toko   : " + struk.getKodeToko() + "\n");
            fw.write("No. Resi    : " + struk.getNomorResi() + "\n");
            fw.write("Tanggal     : " + struk.getTanggal() + "  Jam: " + struk.getWaktu() + "\n");
            fw.write("------------------------------------\n");

            for (Barang b : struk.getDaftarBarang()) {
                fw.write(b.getNamaBarang() + " x" + b.getQty()
                        + " = Rp" + String.format(Locale.US, "%,.0f", b.hitungSubtotal()) + "\n");
            }

            fw.write("------------------------------------\n");
            fw.write("Subtotal    : Rp" + String.format(Locale.US, "%,.0f", struk.getSubtotal()) + "\n");
            fw.write("Diskon      : Rp" + String.format(Locale.US, "%,.0f", struk.getDiskon()) + "\n");
            fw.write("TOTAL       : Rp" + String.format(Locale.US, "%,.0f", struk.getTotal()) + "\n");

            Pembayaran p = struk.getPembayaran();
            if (p instanceof PembayaranNonTunai) {
                PembayaranNonTunai pnt = (PembayaranNonTunai) p;
                fw.write("Metode Bayar: Non-Tunai (" + pnt.getJenisKartu() + ")\n");
            } else if (p instanceof PembayaranTunai) {
                PembayaranTunai pt = (PembayaranTunai) p;
                fw.write("Metode Bayar: Tunai\n");
                fw.write("Uang Diterima: Rp" + String.format(Locale.US, "%,.0f", pt.getUangDiterima()) + "\n");
                fw.write("Kembalian   : Rp" + String.format(Locale.US, "%,.0f", pt.hitungKembalian()) + "\n");
            }

            fw.write("\n");

            fw.close();
            System.out.println("Struk berhasil disimpan ke " + namaFile);

        } catch (IOException e) {
            System.out.println("Gagal menyimpan struk ke file.");
            e.printStackTrace();
        }
    }
}