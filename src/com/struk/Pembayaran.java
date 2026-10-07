/*
 * Program Proyek CLI dari Class Diagram Struk Siqlo
 * @author: Deio Castello Sujati (825250002), Felicia Rivera (825250003), MUhammad Ubait Dhaifullah (825250012)
 */

package com.struk;

// ini untuk bikin blueprint alias kerangka dasar buat urusan pembayaran
// karena ini 'abstract class', kita nggak bisa nyatet pembayaran secara langsung dari kelas ini, 
// harus pakai kelas anaknya nanti misalnya pakai kelas PembayaranTunai atau PembayaranNonTunai
public abstract class Pembayaran {

    // code ini buat nyimpen data dasar pembayaran, yaitu nominal uang yang dibayar dan tanggal transaksinya
    // tetep dibikin private, biar datanya aman
    private float jumlahBayar;
    private String tanggalBayar;

    // tugas code ini buat ngisi data awal nominal dan tanggal pas kita pertama kali memproses transaksi
    public Pembayaran(float jumlahBayar, String tanggalBayar) {
        this.jumlahBayar = jumlahBayar;
        this.tanggalBayar = tanggalBayar;
    }

    // seperti biasa ini ibarat jalan keluar masuk buat ngubah (set) atau ngambil (get) data jumlah dan tanggal bayar dari luar kelas
    public void setJumlahBayar(float jumlahBayar) { this.jumlahBayar = jumlahBayar; }
    public void setTanggalBayar(String tanggalBayar) { this.tanggalBayar = tanggalBayar; }

    public float getJumlahBayar() { return jumlahBayar; }
    public String getTanggalBayar() { return tanggalBayar; }

    /* nah, code ini namanya Abstract Method. 
    * disini kita cuma bikin aturan wajib kalau setiap pembayaran itu harus bisa dikonfirmasi
    */
    public abstract boolean konfirmasiPembayaran();
}
