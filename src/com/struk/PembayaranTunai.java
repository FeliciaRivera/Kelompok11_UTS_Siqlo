/*
 * Program Proyek CLI dari Class Diagram Struk Siqlo
 * @author: Deio Castello Sujati (825250002), Felicia Rivera (825250003), MUhammad Ubait Dhaifullah (825250012)
 */

package com.struk;

// ini bikin kelas PembayaranTunai yang extends dengan sifat sifat dari kelas Pembayaran.
// kalau tadi urusan kartu, nah kelas yang ini khusus buat nanganin transaksi cash
public class PembayaranTunai extends Pembayaran {

    // kodingan ini buat nyimpen data jumlah uang fisik yang diserahin pembeli ke kasir
    // seperti biasa, diset private biar aman dan sesuai sama prinsip OOP
    private float uangDiterima;

    // kalau ini tugasnya nyiapin data pas pembeli mutusin buat bayar cash.
    public PembayaranTunai(float jumlahBayar, String tanggalBayar, float uangDiterima) {
        // kodingan 'super' ini ngelempar urusan total tagihan dan tanggal ke kelas induk.
        super(jumlahBayar, tanggalBayar);
        this.uangDiterima = uangDiterima;
    }

    // juga tetep make set dan get buat ngatur atau ngambil data uang yang diterima dari luar kelas kalau dibutuhin
    public void setUangDiterima(float uangDiterima) { this.uangDiterima = uangDiterima; }
    public float getUangDiterima() { return uangDiterima; }

    /* nah, ini penerapan Method Overriding, 
    * pembayaran dianggap sah atau berhasil asalkan uang yang dikasih pembeli jumlahnya lebih besar atau sama dengan total tagihan
    */
    @Override
    public boolean konfirmasiPembayaran() {
        return uangDiterima >= getJumlahBayar();
    }

    /* Ini method baru khusus buat ngitung uang kembalian.
    * ini yang namanya penerapan prinsip Encapsulation
    * ketika yang pegang uang diterima, dan total tagihan itu adalah objek kelas ini, ya dialah yang paling berhak dan tau cara ngitung kembaliannya
    */
    public float hitungKembalian() {
        float kembalian = uangDiterima - getJumlahBayar();
        return kembalian > 0 ? kembalian : 0f;
    }
}
