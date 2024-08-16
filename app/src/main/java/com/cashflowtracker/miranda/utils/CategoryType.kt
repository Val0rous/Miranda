package com.cashflowtracker.miranda.utils

enum class CategoryClass(val stars: Char) {
    /** One Star */
    NECESSITY('1'),

    /** Two Stars */
    CONVENIENCE('2'),

    /** Three Stars */
    LUXURY('3'),
}

data class Category(
    val name: String,  // Category name, depends from language
    val icon: String,  // Path of category icon
    val type: CategoryClass,  // Category type (stars)
    val label: String  // Category label, language independent
)

enum class DefaultCategories(val category: Category) {
    BANK(Category("Bank", "bank", CategoryClass.NECESSITY, "Bank")),
    CAR(Category("Car", "car", CategoryClass.NECESSITY, "Car")),
    RENT(Category("Rent", "rent", CategoryClass.NECESSITY, "Rent")),
    FAMILY(Category("Family", "family", CategoryClass.NECESSITY, "Family")),
    FEES(Category("Fees", "fees", CategoryClass.NECESSITY, "Fees")),
    FOOD(Category("Food", "food", CategoryClass.NECESSITY, "Food")),
    FUEL(Category("Fuel", "fuel", CategoryClass.NECESSITY, "Fuel")),
    HEALTHCARE(Category("Healthcare", "healthcare", CategoryClass.NECESSITY, "Healthcare")),
    HOUSEHOLD(Category("Household", "household", CategoryClass.NECESSITY, "Household")),
    INSURANCE(Category("Insurance", "insurance", CategoryClass.NECESSITY, "Insurance")),
    JOB(Category("Job", "job", CategoryClass.NECESSITY, "Job")),
    PHONE_INTERNET(
        Category(
            "Phone/Internet",
            "phone_internet",
            CategoryClass.NECESSITY,
            "Phone/Internet"
        )
    ),
    FINES(Category("Fines", "fines", CategoryClass.NECESSITY, "Fines")),
    SUPPLEMENTS(Category("Supplements", "supplements", CategoryClass.NECESSITY, "Supplements")),
    TRANSPORT(Category("Transport", "transport", CategoryClass.NECESSITY, "Transport")),

    MEAL(Category("Meal", "meal", CategoryClass.CONVENIENCE, "Meal")),
    PERSONAL_CARE(
        Category(
            "Personal Care",
            "personal_care",
            CategoryClass.CONVENIENCE,
            "Personal Care"
        )
    ),
    APP_SOFTWARE(
        Category(
            "App/Software",
            "app_software",
            CategoryClass.CONVENIENCE,
            "App/Software"
        )
    ),
    BOOKS(Category("Books", "books", CategoryClass.CONVENIENCE, "Books")),
    ELECTRONICS(Category("Electronics", "electronics", CategoryClass.CONVENIENCE, "Electronics")),
    FRIENDS(Category("Friends", "friends", CategoryClass.CONVENIENCE, "Friends")),
    GIFTS(Category("Gifts", "gifts", CategoryClass.CONVENIENCE, "Gifts")),
    LOST(Category("Lost", "lost", CategoryClass.CONVENIENCE, "Lost")),
    OTHER(Category("Other", "other", CategoryClass.CONVENIENCE, "Other")),
    SHIPPING(Category("Shipping", "shipping", CategoryClass.CONVENIENCE, "Shipping")),
    SPORT(Category("Sport", "sport", CategoryClass.CONVENIENCE, "Sport")),
    CRYPTO(Category("Crypto", "crypto", CategoryClass.CONVENIENCE, "Crypto")),
    ACCESSORIES(Category("Accessories", "accessories", CategoryClass.CONVENIENCE, "Accessories")),
    CAFE(Category("Café", "cafe", CategoryClass.CONVENIENCE, "Café")),
    CLOTHING(Category("Clothing", "clothing", CategoryClass.CONVENIENCE, "Clothing")),

    CINEMA(Category("Cinema", "cinema", CategoryClass.LUXURY, "Cinema")),
    BET(Category("Bet", "bet", CategoryClass.LUXURY, "Bet")),
    ENTERTAINMENT(
        Category(
            "Entertainment",
            "entertainment",
            CategoryClass.LUXURY,
            "Entertainment"
        )
    ),
    FUN(Category("Fun", "fun", CategoryClass.LUXURY, "Fun")),
    GAMING(Category("Gaming", "gaming", CategoryClass.LUXURY, "Gaming")),
    HOBBY(Category("Hobby", "hobby", CategoryClass.LUXURY, "Hobby")),
    HOTEL(Category("Hotel", "hotel", CategoryClass.LUXURY, "Hotel")),
    PUB(Category("Pub", "pub", CategoryClass.LUXURY, "Pub")),
    MUNCHIES(Category("Munchies", "munchies", CategoryClass.LUXURY, "Munchies")),
    RESTAURANT(Category("Restaurant", "restaurant", CategoryClass.LUXURY, "Restaurant")),
    STUFF(Category("Stuff", "stuff", CategoryClass.LUXURY, "Stuff")),
    TRAVEL(Category("Travel", "travel", CategoryClass.LUXURY, "Travel")),
}