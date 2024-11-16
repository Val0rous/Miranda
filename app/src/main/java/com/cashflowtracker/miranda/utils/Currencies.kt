package com.cashflowtracker.miranda.utils

enum class Currencies(
    val label: String,
    val symbol: String,
    val presets: List<Int> = emptyList(),
    val showDecimals: Boolean = true
) {
    EUR("Euro", "€", listOf(1, 2, 5, 10, 20, 50, 100)),
    USD("US Dollar", "$", listOf(1, 5, 10, 20, 50, 100)),
    GBP("British Pound", "£", listOf(1, 5, 10, 20, 50)),
    JPY("Japanese Yen", "¥", listOf(100, 500, 1000, 2000, 5000, 10000), false),
    AUD("Australian Dollar", "A$", listOf(1, 5, 10, 20, 50, 100)),
    CAD("Canadian Dollar", "C$", listOf(1, 5, 10, 20, 50, 100)),
    CHF("Swiss Franc", "Fr", listOf(1, 5, 10, 20, 50, 100, 200)),
    CNY("Chinese Yuan", "CN¥", listOf(1, 5, 10, 20, 50, 100)),
    HKD("Hong Kong Dollar", "HK$", listOf(10, 20, 50, 100, 500, 1000)),
    NZD("New Zealand Dollar", "NZ$", listOf(5, 10, 20, 50, 100)),
    RUB("Russian Ruble", "₽", listOf(10, 50, 100, 200, 500, 1000, 5000)),
    SGD("Singapore Dollar", "S$", listOf(2, 5, 10, 50, 100)),
    SEK("Swedish Krona", "Skr", listOf(1, 5, 10, 20, 50, 100, 500)),
    ZAR("South African Rand", "R", listOf(10, 20, 50, 100, 200)),
    KRW("South Korean Won", "₩", listOf(1000, 5000, 10000, 50000), false),
    ISK("Icelandic Krona", "Ikr", listOf(1, 5, 10, 50, 100, 500, 1000)),
    INR("Indian Rupee", "₹", listOf(1, 2, 5, 10, 20, 50, 100, 200, 500)),
    MXN("Mexican Peso", "MX$", listOf(10, 20, 50, 100, 200, 500, 1000)),
    BRL("Brazilian Real", "R$", listOf(2, 5, 10, 20, 50, 100, 200)),
    ARS("Argentine Peso", "$", listOf(10, 20, 50, 100, 200, 500, 1000)),
    PLN("Polish Zloty", "zł", listOf(1, 2, 5, 10, 20, 50, 100, 200)),
    NOK("Norwegian Krone", "Nkr", listOf(1, 5, 10, 50, 100, 200, 500)),
    DKK("Danish Krone", "kr.", listOf(1, 2, 5, 10, 20, 50, 100, 200, 500)),
    HUF("Hungarian Forint", "Ft", listOf(500, 1000, 2000, 5000, 10000, 20000)),
    CZK("Czech Koruna", "Kč", listOf(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000)),
    TRY("Turkish Lira", "₺", listOf(5, 10, 20, 50, 100, 200)),
    AED("United Arab Emirates Dirham", "د.إ", listOf(5, 10, 20, 50, 100, 200, 500)),
    SAR("Saudi Riyal", "﷼", listOf(1, 5, 10, 50, 100, 200, 500)),
    MYR("Malaysian Ringgit", "RM", listOf(1, 5, 10, 20, 50, 100)),
    THB("Thai Baht", "฿", listOf(20, 50, 100, 500, 1000)),
    PHP("Philippine Peso", "₱", listOf(20, 50, 100, 200, 500, 1000)),
    IDR("Indonesian Rupiah", "Rp", listOf(1000, 2000, 5000, 10000, 20000, 50000, 100000), false),
    VND(
        "Vietnamese Dong",
        "₫",
        listOf(1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000),
        false
    ),
    BDT("Bangladeshi Taka", "৳", listOf(2, 5, 10, 20, 50, 100, 500)),
    PKR("Pakistani Rupee", "₨", listOf(10, 20, 50, 100, 500, 1000)),
    LKR("Sri Lankan Rupee", "Rs", listOf(10, 20, 50, 100, 500, 1000, 2000)),
    ILS("Israeli Shekel", "₪", listOf(20, 50, 100, 200)),
    EGP("Egyptian Pound", "£", listOf(5, 10, 20, 50, 100, 200)),
    NGN("Nigerian Naira", "₦", listOf(50, 100, 200, 500, 1000)),
    KES("Kenyan Shilling", "KSh", listOf(50, 100, 200, 500, 1000)),
    BTC("Bitcoin", "₿");

    companion object {
        fun get(name: String): Currencies {
            return entries.find { it.name == name } ?: USD   // Default to USD
        }
    }
}