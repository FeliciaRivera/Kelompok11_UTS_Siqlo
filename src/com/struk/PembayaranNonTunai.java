/*
 * Program Proyek CLI dari Class Diagram Struk Siqlo
 * @author: Deio Castello Sujati (825250002), Felicia Rivera (825250003), MUhammad Ubait Dhaifullah (825250012)
 */

package com.struk;

import java.util.Locale;


// kelas ini spesial karena nerapin dua konsep OOP sekaligus
// pertama ada Inheritance (extends Pembayaran), Mewarisi sifat sifat dasar dari kelas induk Pembayaran
// kedua Interface (implements CetakStruk), Wajib ngikutin "kontrak" dari CetakStruk buat nyediain fitur cetak.
public class PembayaranNonTunai extends Pembayaran implements CetakStruk {

    // code ini buat nyimpen data spesifik kartu pelanggan, nomor dan jenis kartunya
    // tetap diset private, biar datanya aman
    private int nomorKartu;  
    private String jenisKartu;

    // ini tugasnya buat nyiapin data awal pas user pilih bayar pakai kartu
    public PembayaranNonTunai(float jumlahBayar, String tanggalBayar,
                               int nomorKartu, String jenisKartu) {
        // code 'super' ini buat menyampaikan data jumlah dan tanggal bayar biar diurus sama kelas induk Pembayaran         
        super(jumlahBayar, tanggalBayar);
        // kalau yang ini buat ngurus data nomor dan jenis kartu di kelas ini sendiri
        this.nomorKartu = nomorKartu;
        this.jenisKartu = jenisKartu;
    }

    // masih sama ini buat ngatur dan ngambil data kartu kalau sewaktu waktu dibutuhin
    public void setNomorKartu(int nomorKartu) { this.nomorKartu = nomorKartu; }
    public void setJenisKartu(String jenisKartu) { this.jenisKartu = jenisKartu; }

    public int getNomorKartu() { return nomorKartu; }
    public String getJenisKartu() { return jenisKartu; } 

    @Override
    public boolean konfirmasiPembayaran() {
        return nomorKartu > 0;    // kalau nomor kartunya ada isinya, berarti transaksinya valid
    }

    @Override
    public void cetak() {
        StringBuilder sb = new StringBuilder();
        sb.append("================================================\n");
        sb.append("             TRANSAKSI BERHASIL\n");
        sb.append("================================================\n");
        sb.append(String.format("%-24s%24s", "Status", "PAID")).append("\n");
        sb.append(String.format("%-24s%24s", "Metode Pembayaran", "Online card payment")).append("\n");
        sb.append(String.format("%-24s%24s", "  Jenis Kartu", jenisKartu)).append("\n");
        sb.append(String.format("%-24s%24d", "  No. Pelanggan", nomorKartu)).append("\n");
        //                                         ^^^ %d untuk int
        sb.append(String.format("%-24s%24s", "Jumlah Bayar",
                String.format(Locale.US, "Rp %,.0f", getJumlahBayar()))).append("\n");
        sb.append(String.format("%-24s%24s", "Tanggal", getTanggalBayar())).append("\n");
        sb.append("================================================\n");

        // code ini buat nampilin teks yang udah disusun tadi ke terminal sebagai backup.
        System.out.println(sb);

        javax.swing.JFrame frame = new javax.swing.JFrame("Transaksi Berhasil");
        frame.setSize(480, 280);
        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);

        javax.swing.JTextArea textArea = new javax.swing.JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        textArea.setMargin(new java.awt.Insets(10, 10, 10, 10));

        frame.add(new javax.swing.JScrollPane(textArea));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}