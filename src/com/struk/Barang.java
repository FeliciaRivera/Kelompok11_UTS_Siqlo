/*
 * Program Proyek CLI dari Class Diagram Struk Siqlo
 * @author: Deio Castello Sujati (825250002), Felicia Rivera (825250003), MUhammad Ubait Dhaifullah (825250012)
 */

package com.struk;

// ini untuk bikin blueprint atau kerangka utama untuk barang, kodingan 'abstract' di sini maksudnya kelas ini cuma jadi cetakan induk aja, 
// jadi kita nggak bisa bikin objek langsung dari kelas ini (harus lewat kelas anaknya kayak Pakaian atau Aksesoris)
public abstract class Barang {

    // pada baris ini digunakan untuk nyimpen data data dasar dari barang yang dibeli dengan isian (kode, nama, jumlah, harga).
    // dibikin private supaya datanya aman dan nggak sembarangan diacak acak dari luar kelas
    private String kodeBarang;
    private String namaBarang;
    private int qty;
    private float harga;

    // kalau yang ini untuk nentuin tarif pajak tetap yaitu 11% (0.11)
    // sengaja dipakein 'static final' biar nilainya paten alias nggak bisa diubah ubah lagi, dan berlaku buat semua barang
    public static final float TARIF_PPN = 0.11f;

    // tugas code ini untuk ngisi data data awal (inisialisasi) waktu kita mau nyatet barang baru ke dalam sistem struk
    public Barang(String kodeBarang, String namaBarang, int qty, float harga) {
        this.kodeBarang = kodeBarang;
        this.namaBarang = namaBarang;
        this.qty = qty;
        this.harga = harga;
    }

    // code ini ibarat pintu akses, dipakai buat masukin data (set) 
    // atau nampilin/ngambil data (get) dari variabel private yang udah kita bikin di atas
    public void setKodeBarang(String kodeBarang) { this.kodeBarang = kodeBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }
    public void setQty(int qty) { this.qty = qty; }
    public void setHarga(float harga) { this.harga = harga; }

    public String getKodeBarang() { return kodeBarang; }
    public String getNamaBarang() { return namaBarang; }
    public int getQty() { return qty; }
    public float getHarga() { return harga; }

    // abstract method.
    // artinya: setiap barang WAJIB punya cara hitung subtotal sendiri,
    // dan kelas induk (Barang) tidak ikut campur soal rumusnya.
    public abstract float hitungSubtotal();


    // harga di struk Uniqlo itu sifatnya TAX INCLUSIVE (pajak udah digabung ke dalam harga barang)
    // makanya kodingan ini pakai rumus khusus untuk "menarik keluar" nilai pajaknya dari harga total yang udah nyampur
    public float hitungPPN() {
        return hitungSubtotal() * TARIF_PPN / (1 + TARIF_PPN);
    }
}
