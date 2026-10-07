/*
 * Program Proyek CLI dari Class Diagram Struk Siqlo
 * @author: Deio Castello Sujati (825250002), Felicia Rivera (825250003), MUhammad Ubait Dhaifullah (825250012)
 */

package com.struk;

import java.util.ArrayList;
import java.util.Locale;

// kelas Struk ini ibarat mesin kasir nya
// dia yang bakal ngumpulin semua barang, ngitung total, dan wajib bisa nyetak
public class Struk implements CetakStruk {

    // code ini buat nyimpen data data dasar struk kayak kode toko, nomor resi, dan waktu transaksi
    // semuanya di set private biar datanya nggak gampang bocor atau diubah sembarangan dari luar
    private String kodeToko;
    private String nomorResi;
    private String tanggal;
    private String waktu;

    /* kalau code ini nunjukin konsep Association (Hubungan antar kelas)
    * ceritanya 1 lembar struk bisa nyimpen banyak Barang makanya pakai ArrayList, dan 1 lembar Struk cuma punya 1 jenis Pembayaran
    */
    private ArrayList<Barang> daftarBarang;
    private Pembayaran pembayaran;

    // code ini buat nyimpen angka angka penting, subtotal, diskon, dan total akhir
    private float subtotal;
    private float diskon;
    private float total;

    private static final int LEBAR = 48;   // lebar kertas struk

    // >>> KONSTANTA LOGO KOTAK <<<
    private static final int SISI_LOGO = 80;   // ukuran logo kotak (pixel)
    private static final int STEPS_LOGO = 6;   // jumlah kotak per baris/kolom

     // ini tugasnya buat nyalain mesin pas ada transaksi baru
    // disini kita ngisi data awal struk sekaligus nyiapin keranjang kosong buat nampung barang
    public Struk(String kodeToko, String nomorResi, String tanggal, String waktu) {
        this.kodeToko = kodeToko;
        this.nomorResi = nomorResi;
        this.tanggal = tanggal;
        this.waktu = waktu;
        this.daftarBarang = new ArrayList<>();
        this.subtotal = 0f;
        this.diskon = 0f;
        this.total = 0f;
    }

    // metode ini ibarat kasir lagi nge scan barang, setiap ada barang baru, dimasukin ke 'daftarBarang'
    public void tambahBarang(Barang barang) {
        daftarBarang.add(barang);
    }

    // metode ini buat nyatet si pembeli ini bayarnya pakai apa (Tunai atau Non Tunai)
    public void setPembayaran(Pembayaran pembayaran) {
        this.pembayaran = pembayaran;
    }

    public void setKodeToko(String kodeToko) { this.kodeToko = kodeToko; }
    public void setDiskon(float diskon) { this.diskon = diskon; }

    public String getKodeToko() { return kodeToko; }
    public String getNomorResi() { return nomorResi; }
    public String getTanggal() { return tanggal; }
    public String getWaktu() { return waktu; }
    public ArrayList<Barang> getDaftarBarang() { return daftarBarang; }
    public Pembayaran getPembayaran() { return pembayaran; }
    public float getSubtotal() { return subtotal; }
    public float getDiskon() { return diskon; }
    public float getTotal() { return total; }

    /* code ini buat ngitung total belanjaan.
    * harga Siqlo itu TAX INCLUSIVE (Pajak udah masuk di harga)
    * jadi, di metode ini kita cukup ngitung total harga biasa aja
    * nilai pajaknya nggak perlu ditambahin lagi ke total biar pelanggannya nggak kena tagih pajak dua kali
    */
    public float hitungTotal() {
        subtotal = 0f;
        for (Barang b : daftarBarang) {
            subtotal += b.hitungSubtotal();
        }
        total = subtotal - diskon;
        return total;
    }

    /* ini implementasi wajib dari interface CetakStruk
    * code ini bakal ngerakit struk secara utuh dari atas sampai bawah,
    * nanti hasilnya bakal dimunculin ke jendela pop up dan terminal.
    */
    @Override
    public void cetak() {
        StringBuilder sb = new StringBuilder();

        // KOP STRUK
        sb.append(tengah("SIQLO")).append("\n");
        sb.append(tengah("WWW.SIQLO.COM")).append("\n\n");
        sb.append("Siqlo PIK Avenue\n");
        sb.append("Jl. Pantai Indah Barat No.1 RT004 RW 003\n");
        sb.append("Kamal Muara, Penjaringan\n");
        sb.append("Jakarta 14470\n");
        sb.append("PT. Fast Retailing Indonesia\n");
        sb.append("Tel: 021-29752950\n\n");
        sb.append(tengah("** Receipt **")).append("\n\n");

        sb.append(tanggal).append("\n");
        sb.append(kolom("<" + kodeToko + ">", "[" + waktu + "]")).append("\n\n");

        int unitTerjual = 0;
        for (Barang b : daftarBarang) {
            String hargaT = rupiah(b.getHarga()) + " T";

            if (b instanceof Pakaian) {
                Pakaian p = (Pakaian) b;
                unitTerjual += p.getQty();
                sb.append("  ").append(p.getNamaBarang()).append("\n");
                sb.append(String.format("  %-19s%3d%24s",
                        p.getKodeBarang(), p.getQty(), hargaT)).append("\n");

            } else if (b instanceof Aksesoris) {
                Aksesoris a = (Aksesoris) b;
                sb.append(String.format("  %-22s%24s", a.getNamaBarang(), hargaT)).append("\n");

            } else {
                sb.append(String.format("  %-22s%24s", b.getNamaBarang(), hargaT)).append("\n");
            }
        }

        sb.append(garis('-')).append("\n");

        // REKAP HARGA
        sb.append(String.format("  %-19s%3d", "Unit yang terjual", unitTerjual)).append("\n");
        sb.append(kolom("Subtotal", rupiah(subtotal))).append("\n");
        if (diskon > 0) {
            sb.append(kolom("  Welcome Coupon " + rupiah(diskon), "-" + rupiah(diskon))).append("\n");
        }
        sb.append(kolom("TOTAL", rupiah(total))).append("\n\n");

        // METODE PEMBAYARAN
        sb.append("Metode Pembayaran\n");

        Integer nomorPelanggan = null;   // >>> Integer (bukan String) <<<

        if (pembayaran instanceof PembayaranNonTunai) {
            PembayaranNonTunai pnt = (PembayaranNonTunai) pembayaran;
            sb.append(kolom("Online card payment", rupiah(pnt.getJumlahBayar()))).append("\n");
            sb.append("  ").append(pnt.getJenisKartu()).append("\n");
            nomorPelanggan = pnt.getNomorKartu();   // int → Integer (autoboxing)

        } else if (pembayaran instanceof PembayaranTunai) {
            PembayaranTunai pt = (PembayaranTunai) pembayaran;
            sb.append(kolom("Tunai", rupiah(pt.getJumlahBayar()))).append("\n");
            sb.append(kolom("  Uang diterima", rupiah(pt.getUangDiterima()))).append("\n");
            sb.append(kolom("  Kembalian", rupiah(pt.hitungKembalian()))).append("\n");
        }

        // Hitung pajak
        float hargaSebelumPajak = total / (1 + Barang.TARIF_PPN);
        float totalHarga = (float) Math.floor(hargaSebelumPajak);
        float dppNilaiLain = (float) Math.floor(hargaSebelumPajak * 11 / 12);
        float ppn = total - totalHarga;

        sb.append("Tax inclusive:\n");
        sb.append("  Total harga ").append(rupiah(totalHarga)).append("\n");
        sb.append("    DPP nilai lain ").append(rupiah(dppNilaiLain)).append("\n");
        sb.append(kolom("    PPN", rupiah(ppn))).append("\n\n");

        // IDENTITAS TRANSAKSI
        if (nomorPelanggan != null) {
            // Masking: tampilkan 4 digit terakhir saja
            String masked = "******" + String.format("%04d", nomorPelanggan % 10000);
            sb.append(kolom("No. Pelanggan", masked)).append("\n");
        }
        sb.append(kolom("Receipt no.", nomorResi)).append("\n");
        sb.append(garis('=')).append("\n");

        System.out.println(sb);
        tampilkanJendela(sb.toString());
    }


    private String kolom(String kiri, String kanan) {
        if (kanan.isEmpty()) return kiri;
        int spasi = LEBAR - kiri.length() - kanan.length();
        if (spasi < 1) spasi = 1;
        return kiri + " ".repeat(spasi) + kanan;
    }

    private String tengah(String teks) {
        int spasi = (LEBAR - teks.length()) / 2;
        return " ".repeat(Math.max(spasi, 0)) + teks;
    }

    private String garis(char c) {
        return String.valueOf(c).repeat(LEBAR);
    }

    private String rupiah(float nilai) {
        return String.format(Locale.US, "Rp %,.0f", nilai);
    }


    private void tampilkanJendela(String isiStruk) {
        javax.swing.JFrame frame = new javax.swing.JFrame("Struk - Siqlo PIK Avenue");
        frame.setSize(480, 720);
        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new java.awt.BorderLayout());

        // >>> PANEL ATAS: LOGO KOTAK + TULISAN SIQLO <<<
        javax.swing.JPanel panelAtas = new javax.swing.JPanel();
        panelAtas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 10));
        panelAtas.setBackground(java.awt.Color.WHITE);

        // Gambar logo kotak pola catur
        LogoKotak logoPanel = new LogoKotak(SISI_LOGO, STEPS_LOGO);
        panelAtas.add(logoPanel);

        // Tulisan SIQLO di samping logo
        javax.swing.JLabel labelSiqlo = new javax.swing.JLabel("SIQLO");
        labelSiqlo.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 36));
        labelSiqlo.setForeground(new java.awt.Color(230, 0, 18));   // merah Siqlo
        panelAtas.add(labelSiqlo);

        frame.add(panelAtas, java.awt.BorderLayout.NORTH);

        // >>> TEKS STRUK DI TENGAH <<<
        javax.swing.JTextArea textArea = new javax.swing.JTextArea(isiStruk);
        textArea.setEditable(false);
        textArea.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 12));
        textArea.setMargin(new java.awt.Insets(10, 10, 10, 10));

        frame.add(new javax.swing.JScrollPane(textArea), java.awt.BorderLayout.CENTER);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // >>> INNER CLASS: Panel logo kotak pola catur <<<
    private static class LogoKotak extends javax.swing.JPanel {
        private final int sisi;
        private final int steps;

        public LogoKotak(int sisi, int steps) {
            this.sisi = sisi;
            this.steps = steps;
            setPreferredSize(new java.awt.Dimension(sisi, sisi));
            setBackground(java.awt.Color.WHITE);
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);

            // Gambar kotak-kotak pola catur
            int bs = sisi / steps;
            for (int i = 0; i < steps; i++) {
                for (int j = 0; j < steps; j++) {
                    if ((i + j) % 2 == 0) {
                        g.setColor(java.awt.Color.BLACK);
                    } else {
                        g.setColor(java.awt.Color.WHITE);
                    }
                    g.fillRect(i * bs, j * bs, bs, bs);
                }
            }

            // Bingkai luar
            g.setColor(java.awt.Color.BLACK);
            g.drawRect(0, 0, sisi - 1, sisi - 1);
        }
    }
}