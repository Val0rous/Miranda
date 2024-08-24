package com.cashflowtracker.miranda.utils

import com.cashflowtracker.miranda.R

enum class CategoryClass(val stars: Char) {
    /** One Star */
    NECESSITY('1'),

    /** Two Stars */
    CONVENIENCE('2'),

    /** Three Stars */
    LUXURY('3'),
}

/** Default categories
 *  @param category Category name, depends from language
 *  @param icon Path of category icon
 *  @param type Category type (stars)
 */
enum class DefaultCategories(val category: String, val icon: Int, val type: CategoryClass) {
    BANK("Bank", R.drawable.ic_account_balance, CategoryClass.NECESSITY),
    CAR("Car", R.drawable.ic_directions_car, CategoryClass.NECESSITY),
    RENT("Rent", R.drawable.ic_apartment, CategoryClass.NECESSITY),
    FAMILY("Family", R.drawable.ic_family_restroom, CategoryClass.NECESSITY),
    FEES("Fees", R.drawable.ic_paid, CategoryClass.NECESSITY),
    FOOD("Food", R.drawable.ic_local_pizza, CategoryClass.NECESSITY),
    FUEL("Fuel", R.drawable.ic_local_gas_station, CategoryClass.NECESSITY),
    HEALTHCARE("Healthcare", R.drawable.ic_ecg_heart, CategoryClass.NECESSITY),
    HOUSEHOLD("Household", R.drawable.ic_cottage, CategoryClass.NECESSITY),
    INSURANCE("Insurance", R.drawable.ic_heap_snapshot_large, CategoryClass.NECESSITY),
    JOB("Job", R.drawable.ic_work, CategoryClass.NECESSITY),
    PHONE_INTERNET("Phone/Internet", R.drawable.ic_language, CategoryClass.NECESSITY),
    FINES("Fines", R.drawable.ic_receipt_long, CategoryClass.NECESSITY),
    SUPPLEMENTS("Supplements", R.drawable.ic_medication, CategoryClass.NECESSITY),
    TRANSPORT("Transport", R.drawable.ic_directions_subway, CategoryClass.NECESSITY),

    MEAL("Meal", R.drawable.ic_ramen_dining, CategoryClass.CONVENIENCE),
    PERSONAL_CARE("Personal Care", R.drawable.ic_health_and_beauty, CategoryClass.CONVENIENCE),
    APP_SOFTWARE("App/Software", R.drawable.ic_sdk, CategoryClass.CONVENIENCE),
    BOOKS("Books", R.drawable.ic_menu_book, CategoryClass.CONVENIENCE),
    ELECTRONICS("Electronics", R.drawable.ic_charging_station, CategoryClass.CONVENIENCE),
    FRIENDS("Friends", R.drawable.ic_group, CategoryClass.CONVENIENCE),
    GIFTS("Gifts", R.drawable.ic_featured_seasonal_and_gifts, CategoryClass.CONVENIENCE),
    LOST("Lost", R.drawable.ic_indeterminate_question_box, CategoryClass.CONVENIENCE),
    OTHER("Other", R.drawable.ic_pending, CategoryClass.CONVENIENCE),
    SHIPPING("Shipping", R.drawable.ic_package_2, CategoryClass.CONVENIENCE),
    SPORT("Sport", R.drawable.ic_sports_and_outdoors, CategoryClass.CONVENIENCE),
    CRYPTO("Crypto", R.drawable.ic_currency_bitcoin, CategoryClass.CONVENIENCE),
    ACCESSORIES("Accessories", R.drawable.ic_media_output, CategoryClass.CONVENIENCE),
    CAFE("Café", R.drawable.ic_local_cafe, CategoryClass.CONVENIENCE),
    CLOTHING("Clothing", R.drawable.ic_apparel, CategoryClass.CONVENIENCE),

    CINEMA("Cinema", R.drawable.ic_movie, CategoryClass.LUXURY),
    BET("Bet", R.drawable.ic_casino, CategoryClass.LUXURY),
    ENTERTAINMENT("Entertainment", R.drawable.ic_local_activity, CategoryClass.LUXURY),
    FUN("Fun", R.drawable.ic_celebration, CategoryClass.LUXURY),
    GAMING("Gaming", R.drawable.ic_stadia_controller, CategoryClass.LUXURY),
    HOBBY("Hobby", R.drawable.ic_downhill_skiing, CategoryClass.LUXURY),
    HOTEL("Hotel", R.drawable.ic_hotel, CategoryClass.LUXURY),
    PUB("Pub", R.drawable.ic_local_bar, CategoryClass.LUXURY),
    MUNCHIES("Munchies", R.drawable.ic_icecream, CategoryClass.LUXURY),
    RESTAURANT("Restaurant", R.drawable.ic_restaurant, CategoryClass.LUXURY),
    STUFF("Stuff", R.drawable.ic_widgets, CategoryClass.LUXURY),
    TRAVEL("Travel", R.drawable.ic_travel, CategoryClass.LUXURY);

    companion object {
        fun getIcon(category: String): Int {
            return DefaultCategories.entries.find {
                it.category == category
            }?.icon ?: R.drawable.ic_default_empty
        }
    }
}