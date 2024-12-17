package com.cashflowtracker.miranda.utils

import com.cashflowtracker.miranda.R

enum class TransactionType(val type: Int) {
    OUTPUT(R.string.transaction_type_output),
    INPUT(R.string.transaction_type_input),
    TRANSFER(R.string.transaction_type_transfer);

    companion object {
        fun getType(type: String): Int {
            return TransactionType.valueOf(type).type
        }
    }
}

enum class CategoryClass(val stars: Char, val label: Int) {
    /** One Star */
    NECESSITY('1', R.string.category_class_necessity),

    /** Two Stars */
    CONVENIENCE('2', R.string.category_class_convenience),

    /** Three Stars */
    LUXURY('3', R.string.category_class_luxury),

    UNRANKED('0', R.string.category_class_unranked)
}

/** Default categories
 *  @param category Category name, depends from language
 *  @param icon Path of category icon
 *  @param type Category type (stars)
 */
enum class DefaultCategories(
    override val category: Int,
    override val icon: Int,
    val type: CategoryClass,
    override val description: Int = 0
) : DescriptionCategory {
    BANK(R.string.category_bank, R.drawable.ic_account_balance, CategoryClass.NECESSITY),
    CAR(R.string.category_car, R.drawable.ic_directions_car, CategoryClass.NECESSITY),
    RENT(R.string.category_rent, R.drawable.ic_apartment, CategoryClass.NECESSITY),
    FAMILY(R.string.category_family, R.drawable.ic_family_restroom, CategoryClass.NECESSITY),
    FEES(R.string.category_fees, R.drawable.ic_paid, CategoryClass.NECESSITY),
    FOOD(R.string.category_food, R.drawable.ic_local_pizza, CategoryClass.NECESSITY),
    FUEL(R.string.category_fuel, R.drawable.ic_local_gas_station, CategoryClass.NECESSITY),
    HEALTHCARE(
        R.string.category_healthcare,
        R.drawable.ic_ecg_heart,
        CategoryClass.NECESSITY,
        R.string.category_healthcare_description
    ),
    HOUSEHOLD(R.string.category_household, R.drawable.ic_cottage, CategoryClass.NECESSITY),
    INSURANCE(
        R.string.category_insurance,
        R.drawable.ic_heap_snapshot_large,
        CategoryClass.NECESSITY
    ),
    JOB(R.string.category_job, R.drawable.ic_work, CategoryClass.NECESSITY),
    PHONE_INTERNET(
        R.string.category_phone_internet,
        R.drawable.ic_language,
        CategoryClass.NECESSITY
    ),
    FINES(R.string.category_fines, R.drawable.ic_receipt_long, CategoryClass.NECESSITY),
    SUPPLEMENTS(R.string.category_supplements, R.drawable.ic_medication, CategoryClass.NECESSITY),
    TRANSPORT(
        R.string.category_transport,
        R.drawable.ic_directions_subway,
        CategoryClass.NECESSITY
    ),

    MEAL(R.string.category_meal, R.drawable.ic_ramen_dining, CategoryClass.CONVENIENCE),
    PERSONAL_CARE(
        R.string.category_personal_care,
        R.drawable.ic_health_and_beauty,
        CategoryClass.CONVENIENCE
    ),
    APP_SOFTWARE(R.string.category_app_software, R.drawable.ic_sdk, CategoryClass.CONVENIENCE),
    BOOKS(R.string.category_books, R.drawable.ic_menu_book, CategoryClass.CONVENIENCE),
    ELECTRONICS(
        R.string.category_electronics,
        R.drawable.ic_charging_station,
        CategoryClass.CONVENIENCE
    ),
    FRIENDS(R.string.category_friends, R.drawable.ic_group, CategoryClass.CONVENIENCE),
    GIFTS(
        R.string.category_gifts,
        R.drawable.ic_featured_seasonal_and_gifts,
        CategoryClass.CONVENIENCE
    ),
    LOST(
        R.string.category_lost,
        R.drawable.ic_indeterminate_question_box,
        CategoryClass.CONVENIENCE
    ),
    OTHER(R.string.category_other, R.drawable.ic_pending, CategoryClass.CONVENIENCE),
    SHIPPING(R.string.category_shipping, R.drawable.ic_package_2, CategoryClass.CONVENIENCE),
    SPORT(R.string.category_sport, R.drawable.ic_sports_and_outdoors, CategoryClass.CONVENIENCE),
    CRYPTO(R.string.category_crypto, R.drawable.ic_currency_bitcoin, CategoryClass.CONVENIENCE),
    ACCESSORIES(
        R.string.category_accessories,
        R.drawable.ic_media_output,
        CategoryClass.CONVENIENCE
    ),
    CAFE(R.string.category_cafe, R.drawable.ic_local_cafe, CategoryClass.CONVENIENCE),
    CLOTHING(R.string.category_clothing, R.drawable.ic_apparel, CategoryClass.CONVENIENCE),

    CINEMA(R.string.category_cinema, R.drawable.ic_movie, CategoryClass.LUXURY),
    BET(R.string.category_bet, R.drawable.ic_casino, CategoryClass.LUXURY),
    ENTERTAINMENT(
        R.string.category_entertainment,
        R.drawable.ic_local_activity,
        CategoryClass.LUXURY
    ),
    FUN(R.string.category_fun, R.drawable.ic_celebration, CategoryClass.LUXURY),
    GAMING(R.string.category_gaming, R.drawable.ic_stadia_controller, CategoryClass.LUXURY),
    HOBBY(R.string.category_hobby, R.drawable.ic_downhill_skiing, CategoryClass.LUXURY),
    HOTEL(R.string.category_hotel, R.drawable.ic_hotel, CategoryClass.LUXURY),
    PUB(R.string.category_pub, R.drawable.ic_local_bar, CategoryClass.LUXURY),
    MUNCHIES(R.string.category_munchies, R.drawable.ic_icecream, CategoryClass.LUXURY),
    RESTAURANT(R.string.category_restaurant, R.drawable.ic_restaurant, CategoryClass.LUXURY),
    STUFF(R.string.category_stuff, R.drawable.ic_widgets, CategoryClass.LUXURY),
    TRAVEL(R.string.category_travel, R.drawable.ic_travel, CategoryClass.LUXURY);

    companion object {
        fun getCategory(categoryName: String): Int {
            return DefaultCategories.entries.find {
                it.name == categoryName
            }?.category ?: 0
        }

        fun getIcon(categoryName: String): Int {
            return DefaultCategories.entries.find {
                it.name == categoryName
            }?.icon ?: R.drawable.ic_default_empty
        }

        fun getType(categoryName: String): CategoryClass {
            return DefaultCategories.entries.find {
                it.name == categoryName
            }?.type ?: CategoryClass.UNRANKED
        }
    }
}