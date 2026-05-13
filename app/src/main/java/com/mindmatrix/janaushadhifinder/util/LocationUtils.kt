package com.mindmatrix.janaushadhifinder.util

import android.location.Location
import kotlin.math.roundToInt

object LocationUtils {
    val indianStates = listOf(
        "Andaman and Nicobar Islands", "Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bihar", 
        "Chandigarh", "Chhattisgarh", "Dadra and Nagar Haveli and Daman and Diu", "Delhi", "Goa", 
        "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", 
        "Kerala", "Ladakh", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", 
        "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", 
        "Telangana", "Tripura", "Uttar Pradesh", "Uttarakhand", "West Bengal"
    ).sorted()

    val stateCityMap = mapOf(
        "Andhra Pradesh" to listOf("Visakhapatnam", "Vijayawada", "Guntur", "Kurnool", "Nellore", "Tirupati", "Machilipatnam", "Chittoor", "Srikakulam", "Eluru", "Kadapa", "Anantapur"),
        "Arunachal Pradesh" to listOf("Itanagar", "Pasighat", "Along"),
        "Assam" to listOf("Guwahati", "Dibrugarh", "Jorhat", "Silchar", "Nagaon", "Barpeta", "Tinsukia", "North Lakhimpur", "Goalpara"),
        "Bihar" to listOf("Patna", "Muzaffarpur", "Gaya", "Bhagalpur", "Darbhanga", "Purnia", "Begusarai", "Samastipur", "Bihar Sharif", "Munger", "Siwan"),
        "Chhattisgarh" to listOf("Raipur", "Bilaspur", "Bhilai", "Rajnandgaon", "Korba", "Jagdalpur"),
        "Goa" to listOf("Panaji", "Mapusa", "Margao", "Calangute"),
        "Gujarat" to listOf("Ahmedabad", "Surat", "Vadodara", "Rajkot", "Gandhinagar", "Bhavnagar", "Junagadh", "Anand", "Surendranagar", "Porbandar"),
        "Haryana" to listOf("Gurugram", "Faridabad", "Ambala", "Hisar", "Rohtak", "Sonipat", "Panipat", "Karnal", "Yamunanagar", "Jhajjar"),
        "Himachal Pradesh" to listOf("Shimla", "Dharamsala", "Mandi", "Solan", "Kullu", "Bilaspur", "Una"),
        "Jharkhand" to listOf("Ranchi", "Dhanbad", "Jamshedpur", "Bokaro Steel City", "Hazaribagh", "Giridih"),
        "Karnataka" to listOf("Bengaluru", "Mysuru", "Hubballi", "Mangaluru", "Belagavi", "Shivamogga", "Tumakuru", "Kalaburagi", "Ballari", "Raichur", "Udupi", "Chikkamagaluru"),
        "Kerala" to listOf("Thiruvananthapuram", "Kochi", "Thrissur", "Kozhikode", "Kollam", "Palakkad", "Kannur", "Malappuram", "Alappuzha"),
        "Madhya Pradesh" to listOf("Bhopal", "Indore", "Jabalpur", "Gwalior", "Ujjain", "Rewa", "Sagar", "Satna", "Morena"),
        "Maharashtra" to listOf("Mumbai", "Pune", "Nagpur", "Nashik", "Chhatrapati Sambhajinagar", "Solapur", "Kolhapur", "Amravati", "Latur", "Jalgaon", "Nanded"),
        "Manipur" to listOf("Imphal", "Thoubal"),
        "Meghalaya" to listOf("Shillong", "Nongstoin"),
        "Mizoram" to listOf("Aizawl", "Lunglei"),
        "Nagaland" to listOf("Kohima", "Dimapur", "Wokha"),
        "Odisha" to listOf("Bhubaneswar", "Cuttack", "Sambalpur", "Puri", "Berhampur", "Rourkela", "Koraput", "Baripada", "Bolangir"),
        "Punjab" to listOf("Ludhiana", "Amritsar", "Jalandhar", "Patiala", "Bathinda", "Hoshiarpur", "Sangrur", "Fatehgarh Sahib"),
        "Rajasthan" to listOf("Jaipur", "Jodhpur", "Udaipur", "Kota", "Ajmer", "Bikaner", "Bharatpur", "Alwar", "Sikar", "Nagaur", "Sawai Madhopur"),
        "Sikkim" to listOf("Gangtok", "Gyalshing"),
        "Tamil Nadu" to listOf("Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Vellore", "Tirunelveli", "Erode", "Thanjavur", "Kancheepuram", "Dindigul", "Cuddalore", "Virudhunagar"),
        "Telangana" to listOf("Hyderabad", "Secunderabad", "Warangal", "Karimnagar", "Nizamabad", "Khammam"),
        "Tripura" to listOf("Agartala", "Udaipur"),
        "Uttar Pradesh" to listOf("Lucknow", "Agra", "Kanpur", "Varanasi", "Prayagraj", "Ghaziabad", "Meerut", "Noida", "Bareilly", "Gorakhpur", "Moradabad", "Sultanpur", "Azamgarh", "Aligarh", "Jhansi", "Mathura"),
        "Uttarakhand" to listOf("Dehradun", "Haridwar", "Haldwani", "Rudrapur", "Kotdwar", "Gopeshwar"),
        "West Bengal" to listOf("Kolkata", "Howrah", "Barasat", "Bardhaman", "Siliguri", "Krishnanagar", "Berhampore", "Bankura", "Kharagpur", "Cooch Behar", "Malda", "Suri"),
        "Andaman and Nicobar Islands" to listOf("Port Blair", "Car Nicobar"),
        "Chandigarh" to listOf("Chandigarh"),
        "Dadra and Nagar Haveli and Daman and Diu" to listOf("Daman", "Silvassa", "Diu"),
        "Delhi" to listOf("Delhi"),
        "Jammu and Kashmir" to listOf("Srinagar", "Jammu", "Baramulla", "Anantnag"),
        "Ladakh" to listOf("Leh", "Kargil"),
        "Lakshadweep" to listOf("Kavaratti", "Agatti Island"),
        "Puducherry" to listOf("Puducherry", "Karaikal", "Mahe")
    )

    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)
        return results[0]
    }

    fun formatDistance(meters: Float): String {
        return if (meters < 1000) {
            "${meters.roundToInt()} m"
        } else {
            String.format("%.1f km", meters / 1000f)
        }
    }
}
