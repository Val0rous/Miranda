package com.cashflowtracker.miranda.utils

import com.cashflowtracker.miranda.R

enum class Currencies(
    val label: Int,
    val symbol: String,
    val presets: List<Int> = emptyList(),
    val showDecimals: Boolean = true
) {
    EUR(R.string.currency_euro, "€", listOf(1, 2, 5, 10, 20, 50, 100)),
    USD(R.string.currency_us_dollar, "$", listOf(1, 5, 10, 20, 50, 100)),
    GBP(R.string.currency_british_pound, "£", listOf(1, 5, 10, 20, 50)),
    JPY(R.string.currency_japanese_yen, "¥", listOf(100, 500, 1000, 2000, 5000, 10000), false),
    AUD(R.string.currency_australian_dollar, "A$", listOf(1, 5, 10, 20, 50, 100)),
    CAD(R.string.currency_canadian_dollar, "C$", listOf(1, 5, 10, 20, 50, 100)),
    CHF(R.string.currency_swiss_franc, "Fr", listOf(1, 5, 10, 20, 50, 100, 200)),
    CNY(R.string.currency_chinese_yuan, "CN¥", listOf(1, 5, 10, 20, 50, 100)),
    HKD(R.string.currency_hong_kong_dollar, "HK$", listOf(10, 20, 50, 100, 500, 1000)),
    NZD(R.string.currency_new_zealand_dollar, "NZ$", listOf(5, 10, 20, 50, 100)),
    RUB(R.string.currency_russian_ruble, "₽", listOf(10, 50, 100, 200, 500, 1000, 5000)),
    SGD(R.string.currency_singapore_dollar, "S$", listOf(2, 5, 10, 50, 100)),
    SEK(R.string.currency_swedish_krona, "Skr", listOf(1, 5, 10, 20, 50, 100, 500)),
    ZAR(R.string.currency_south_african_rand, "R", listOf(10, 20, 50, 100, 200)),
    KRW(R.string.currency_south_korean_won, "₩", listOf(1000, 5000, 10000, 50000), false),
    ISK(R.string.currency_icelandic_krona, "Ikr", listOf(1, 5, 10, 50, 100, 500, 1000)),
    INR(R.string.currency_indian_rupee, "₹", listOf(1, 2, 5, 10, 20, 50, 100, 200, 500)),
    MXN(R.string.currency_mexican_peso, "MX$", listOf(10, 20, 50, 100, 200, 500, 1000)),
    BRL(R.string.currency_brazilian_real, "R$", listOf(2, 5, 10, 20, 50, 100, 200)),
    ARS(R.string.currency_argentine_peso, "$", listOf(10, 20, 50, 100, 200, 500, 1000)),
    PLN(R.string.currency_polish_zloty, "zł", listOf(1, 2, 5, 10, 20, 50, 100, 200)),
    NOK(R.string.currency_norwegian_krone, "Nkr", listOf(1, 5, 10, 50, 100, 200, 500)),
    DKK(R.string.currency_danish_krone, "kr.", listOf(1, 2, 5, 10, 20, 50, 100, 200, 500)),
    HUF(R.string.currency_hungarian_forint, "Ft", listOf(500, 1000, 2000, 5000, 10000, 20000)),
    CZK(
        R.string.currency_czech_koruna,
        "Kč",
        listOf(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000)
    ),
    TRY(R.string.currency_turkish_lira, "₺", listOf(5, 10, 20, 50, 100, 200)),
    AED(R.string.currency_united_arab_emirates_dirham, "د.إ", listOf(5, 10, 20, 50, 100, 200, 500)),
    SAR(R.string.currency_saudi_riyal, "﷼", listOf(1, 5, 10, 50, 100, 200, 500)),
    MYR(R.string.currency_malaysian_ringgit, "RM", listOf(1, 5, 10, 20, 50, 100)),
    THB(R.string.currency_thai_baht, "฿", listOf(20, 50, 100, 500, 1000)),
    PHP(R.string.currency_philippine_peso, "₱", listOf(20, 50, 100, 200, 500, 1000)),
    IDR(
        R.string.currency_indonesian_rupiah,
        "Rp",
        listOf(1000, 2000, 5000, 10000, 20000, 50000, 100000),
        false
    ),
    VND(
        R.string.currency_vietnamese_dong,
        "₫",
        listOf(1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000),
        false
    ),
    BDT(R.string.currency_bangladeshi_taka, "৳", listOf(2, 5, 10, 20, 50, 100, 500)),
    PKR(R.string.currency_pakistani_rupee, "₨", listOf(10, 20, 50, 100, 500, 1000)),
    LKR(R.string.currency_sri_lankan_rupee, "Rs", listOf(10, 20, 50, 100, 500, 1000, 2000)),
    ILS(R.string.currency_israeli_shekel, "₪", listOf(20, 50, 100, 200)),
    EGP(R.string.currency_egyptian_pound, "£", listOf(5, 10, 20, 50, 100, 200)),
    NGN(R.string.currency_nigerian_naira, "₦", listOf(50, 100, 200, 500, 1000)),
    KES(R.string.currency_kenyan_shilling, "KSh", listOf(50, 100, 200, 500, 1000)),
    BTC(R.string.currency_bitcoin, "₿");

    companion object {
        fun get(name: String): Currencies {
            return entries.find { it.name == name } ?: USD   // Default to USD
        }
    }
}