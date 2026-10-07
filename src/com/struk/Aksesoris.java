/*
 * Program Proyek CLI dari Class Diagram Struk Siqlo
 * @author: Deio Castello Sujati (825250002), Felicia Rivera (825250003), MUhammad Ubait Dhaifullah (825250012)
 */

package com.struk;

// ini untuk bikin kelas aksesoris yang extends dengan sifat sifat dan atribut dari kelas barang
public class Aksesoris extends Barang {

   // pada baris ini untuk nyimpen informasi spesifik, yaitu jenis aksesorisnya, sengaja dibikin private biar datanya aman dan nggak sembarangan diubah dari luar
    private String jenis;

    // kalau yang ini tugasnya untuk ngasih nilai awal waktu kita mau bikin objek Aksesoris baru nanti
    public Aksesoris(String kodeBarang, String namaBarang, int qty, float harga, String jenis) {
        super(kodeBarang, namaBarang, qty, harga);
        this.jenis = jenis;
    }

    // code ini dipakai kalau kita mau ngubah atau masukin data jenis aksesoris yang baru.
    public void setJenis(String jenis) { this.jenis = jenis; }

    // kalau yang ini dipakai kalau kita mau ngambil atau nampilin data jenis aksesoris yang udah disimpen.
    public String getJenis() { return jenis; }

    // override abstract method dari Barang 
    // sedangkan bawahnya untuk menghitung subtotal dengan rumus qty * harga
    @Override
    public float hitungSubtotal() {
        return getQty() * getHarga();
    }
}