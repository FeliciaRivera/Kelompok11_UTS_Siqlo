/*
 * Program Proyek CLI dari Class Diagram Struk Siqlo
 * @author: Deio Castello Sujati (825250002), Felicia Rivera (825250003), MUhammad Ubait Dhaifullah (825250012)
 */

package com.struk;

// code ini buat bikin kelas pakaian yang extends semua sifat dan fungsi dari kelas induknya, yaitu kelas Barang
public class Pakaian extends Barang {

    // kalau ini untuk nyimpen data tambahan khusus buat pakaian
    // sama kayak sebelumnya, dibikin private biar datanya terlindungi dan nggak sembarangan diganti dari luar
    private String kategori;

    // tugas code ini buat ngisi data data awal pas kita mau nyatet baju baru ke dalam sistem
    public Pakaian(String kodeBarang, String namaBarang, int qty, float harga, String kategori) {

        // Code 'super' ini wajib dipanggil buat ngelempar tugas ke kelas induk (Barang)
        super(kodeBarang, namaBarang, qty, harga);
        this.kategori = kategori;
    }

    // code ini dipakai kalau kita mau ngubah atau masukin data kategori yang baru.
    public void setKategori(String kategori) { this.kategori = kategori; }

    // kalau code ini dipakai jika kita mau ngambil atau nampilin data kategori yang udah disimpen tadi
    public String getKategori() { return kategori; }

    // override abstract method dari Barang
    // dan juga pakaian menghitung subtotal dengan rumus qty * harga
    @Override
    public float hitungSubtotal() {
        return getQty() * getHarga();
    }
}
