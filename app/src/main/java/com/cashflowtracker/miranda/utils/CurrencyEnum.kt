package com.cashflowtracker.miranda.utils

enum class CurrencyEnum(val label: String, val symbol: String, val presets: List<Int>) {
    EUR("Euro", "€", listOf(1, 2, 5, 10, 20, 50, 100)),
    USD("US Dollar", "$", listOf(1, 5, 10, 20, 50, 100)),
    GBP("British Pound", "£", listOf(1, 5, 10, 20, 50)),
    JPY("Japanese Yen", "¥", listOf(100, 500, 1000, 2000, 5000, 10000)),
    AUD("Australian Dollar", "$", listOf(1, 5, 10, 20, 50, 100)),
    CAD("Canadian Dollar", "$", listOf(1, 5, 10, 20, 50, 100)),
    CHF("Swiss Franc", "Fr", listOf(1, 5, 10, 20, 50, 100, 200)),
    CNY("Chinese Yuan", "¥", listOf(1, 5, 10, 20, 50, 100)),
    HKD("Hong Kong Dollar", "$", listOf(10, 20, 50, 100, 500, 1000)),
    NZD("New Zealand Dollar", "$", listOf(5, 10, 20, 50, 100)),
    RUB("Russian Ruble", "₽", listOf(10, 50, 100, 200, 500, 1000, 5000)),
    SGD("Singapore Dollar", "$", listOf(2, 5, 10, 50, 100)),
    SEK("Swedish Krona", "kr", listOf(1, 5, 10, 20, 50, 100, 500)),
    ZAR("South African Rand", "R", listOf(10, 20, 50, 100, 200)),
    KRW("South Korean Won", "₩", listOf(1000, 5000, 10000, 50000)),
    ISK("Icelandic Krona", "kr", listOf(1, 5, 10, 50, 100, 500, 1000)),
    INR("Indian Rupee", "₹", listOf(1, 2, 5, 10, 20, 50, 100, 200, 500)),
    MXN("Mexican Peso", "$", listOf(10, 20, 50, 100, 200, 500, 1000)),
    BRL("Brazilian Real", "R$", listOf(2, 5, 10, 20, 50, 100, 200)),
    ARS("Argentine Peso", "$", listOf(10, 20, 50, 100, 200, 500, 1000)),
    PLN("Polish Zloty", "zł", listOf(1, 2, 5, 10, 20, 50, 100, 200)),
    NOK("Norwegian Krone", "kr", listOf(1, 5, 10, 50, 100, 200, 500)),
    DKK("Danish Krone", "kr", listOf(1, 2, 5, 10, 20, 50, 100, 200, 500)),
    HUF("Hungarian Forint", "Ft", listOf(500, 1000, 2000, 5000, 10000, 20000)),
    CZK("Czech Koruna", "Kč", listOf(1, 2, 5, 10, 20, 50, 100, 200, 500, 1000, 2000)),
    TRY("Turkish Lira", "₺", listOf(5, 10, 20, 50, 100, 200)),
    AED("United Arab Emirates Dirham", "د.إ", listOf(5, 10, 20, 50, 100, 200, 500)),
    SAR("Saudi Riyal", "﷼", listOf(1, 5, 10, 50, 100, 200, 500)),
    MYR("Malaysian Ringgit", "RM", listOf(1, 5, 10, 20, 50, 100)),
    THB("Thai Baht", "฿", listOf(20, 50, 100, 500, 1000)),
    PHP("Philippine Peso", "₱", listOf(20, 50, 100, 200, 500, 1000)),
    IDR("Indonesian Rupiah", "Rp", listOf(1000, 2000, 5000, 10000, 20000, 50000, 100000)),
    VND(
        "Vietnamese Dong",
        "₫",
        listOf(1000, 2000, 5000, 10000, 20000, 50000, 100000, 200000, 500000)
    ),
    BDT("Bangladeshi Taka", "৳", listOf(2, 5, 10, 20, 50, 100, 500)),
    PKR("Pakistani Rupee", "₨", listOf(10, 20, 50, 100, 500, 1000)),
    LKR("Sri Lankan Rupee", "Rs", listOf(10, 20, 50, 100, 500, 1000, 2000)),
    ILS("Israeli Shekel", "₪", listOf(20, 50, 100, 200)),
    EGP("Egyptian Pound", "£", listOf(5, 10, 20, 50, 100, 200)),
    NGN("Nigerian Naira", "₦", listOf(50, 100, 200, 500, 1000)),
    KES("Kenyan Shilling", "KSh", listOf(50, 100, 200, 500, 1000))
}

enum class CountriesEnum(val label: String, val currency: CurrencyEnum) {
    // Eurozone Countries
    AUSTRIA("Austria", CurrencyEnum.EUR),
    BELGIUM("Belgium", CurrencyEnum.EUR),
    CROATIA("Croatia", CurrencyEnum.EUR),
    CYPRUS("Cyprus", CurrencyEnum.EUR),
    ESTONIA("Estonia", CurrencyEnum.EUR),
    FINLAND("Finland", CurrencyEnum.EUR),
    FRANCE("France", CurrencyEnum.EUR),
    GERMANY("Germany", CurrencyEnum.EUR),
    GREECE("Greece", CurrencyEnum.EUR),
    IRELAND("Ireland", CurrencyEnum.EUR),
    ITALY("Italy", CurrencyEnum.EUR),
    LATVIA("Latvia", CurrencyEnum.EUR),
    LITHUANIA("Lithuania", CurrencyEnum.EUR),
    LUXEMBOURG("Luxembourg", CurrencyEnum.EUR),
    MALTA("Malta", CurrencyEnum.EUR),
    NETHERLANDS("Netherlands", CurrencyEnum.EUR),
    PORTUGAL("Portugal", CurrencyEnum.EUR),
    SLOVAKIA("Slovakia", CurrencyEnum.EUR),
    SLOVENIA("Slovenia", CurrencyEnum.EUR),
    SPAIN("Spain", CurrencyEnum.EUR),

    // USD-based Countries
    UNITED_STATES("United States", CurrencyEnum.USD),
    PUERTO_RICO("Puerto Rico", CurrencyEnum.USD),
    GUAM("Guam", CurrencyEnum.USD),
    MARSHALL_ISLANDS("Marshall Islands", CurrencyEnum.USD),
    EL_SALVADOR("El Salvador", CurrencyEnum.USD),
    ECUADOR("Ecuador", CurrencyEnum.USD),
    EAST_TIMOR("East Timor", CurrencyEnum.USD),
    ZIMBABWE("Zimbabwe", CurrencyEnum.USD),

    // AUD-based Countries
    AUSTRALIA("Australia", CurrencyEnum.AUD),
    NAURU("Nauru", CurrencyEnum.AUD),
    KIRIBATI("Kiribati", CurrencyEnum.AUD),
    TUVALU("Tuvalu", CurrencyEnum.AUD),

    // CHF-based Country
    SWITZERLAND("Switzerland", CurrencyEnum.CHF),
    LIECHTENSTEIN("Liechtenstein", CurrencyEnum.CHF),

    // DKK-based Country
    DENMARK("Denmark", CurrencyEnum.DKK),
    GREENLAND("Greenland", CurrencyEnum.DKK),

    ARGENTINA("Argentina", CurrencyEnum.ARS),
    BANGLADESH("Bangladesh", CurrencyEnum.BDT),
    BRAZIL("Brazil", CurrencyEnum.BRL),
    CANADA("Canada", CurrencyEnum.CAD),
    CHINA("China", CurrencyEnum.CNY),
    CZECH_REPUBLIC("Czech Republic", CurrencyEnum.CZK),
    EGYPT("Egypt", CurrencyEnum.EGP),
    HONG_KONG("Hong Kong", CurrencyEnum.HKD),
    HUNGARY("Hungary", CurrencyEnum.HUF),
    ICELAND("Iceland", CurrencyEnum.ISK),
    INDIA("India", CurrencyEnum.INR),
    INDONESIA("Indonesia", CurrencyEnum.IDR),
    ISRAEL("Israel", CurrencyEnum.ILS),
    JAPAN("Japan", CurrencyEnum.JPY),
    KENYA("Kenya", CurrencyEnum.KES),
    MALAYSIA("Malaysia", CurrencyEnum.MYR),
    MEXICO("Mexico", CurrencyEnum.MXN),
    NEW_ZEALAND("New Zealand", CurrencyEnum.NZD),
    NIGERIA("Nigeria", CurrencyEnum.NGN),
    NORWAY("Norway", CurrencyEnum.NOK),
    PAKISTAN("Pakistan", CurrencyEnum.PKR),
    PHILIPPINES("Philippines", CurrencyEnum.PHP),
    POLAND("Poland", CurrencyEnum.PLN),
    RUSSIA("Russia", CurrencyEnum.RUB),
    SAUDI_ARABIA("Saudi Arabia", CurrencyEnum.SAR),
    SINGAPORE("Singapore", CurrencyEnum.SGD),
    SOUTH_AFRICA("South Africa", CurrencyEnum.ZAR),
    SOUTH_KOREA("South Korea", CurrencyEnum.KRW),
    SRI_LANKA("Sri Lanka", CurrencyEnum.LKR),
    SWEDEN("Sweden", CurrencyEnum.SEK),
    UNITED_ARAB_EMIRATES("United Arab Emirates", CurrencyEnum.AED),
    UNITED_KINGDOM("United Kingdom", CurrencyEnum.GBP),
    THAILAND("Thailand", CurrencyEnum.THB),
    TURKEY("Turkey", CurrencyEnum.TRY),
    VIETNAM("Vietnam", CurrencyEnum.VND)
}