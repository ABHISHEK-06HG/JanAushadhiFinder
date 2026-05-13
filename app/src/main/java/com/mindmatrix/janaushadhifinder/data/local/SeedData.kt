package com.mindmatrix.janaushadhifinder.data.local

import com.mindmatrix.janaushadhifinder.data.local.entity.FavoriteLocationEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.MedicineEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.StoreEntity

object SeedData {
    val medicines = listOf(
        MedicineEntity(brandName = "Crocin 500", genericName = "Paracetamol 500mg", composition = "Paracetamol IP 500mg", category = "Pain & Fever", priceBrand = 38.0, priceGeneric = 10.50),
        MedicineEntity(brandName = "Dolo 650", genericName = "Paracetamol 650mg", composition = "Paracetamol IP 650mg", category = "Pain & Fever", priceBrand = 42.0, priceGeneric = 12.0),
        MedicineEntity(brandName = "Metformin SR 500", genericName = "Metformin 500mg SR", composition = "Metformin Hydrochloride IP 500mg", category = "Diabetes", priceBrand = 90.0, priceGeneric = 22.40),
        MedicineEntity(brandName = "Telma 40", genericName = "Telmisartan 40mg", composition = "Telmisartan IP 40mg", category = "Hypertension", priceBrand = 145.0, priceGeneric = 38.0),
        MedicineEntity(brandName = "Pantop 40", genericName = "Pantoprazole 40mg", composition = "Pantoprazole Sodium IP 40mg", category = "Gastroenterology", priceBrand = 92.0, priceGeneric = 18.20),
        MedicineEntity(brandName = "Amoxil 500", genericName = "Amoxicillin 500mg", composition = "Amoxicillin Trihydrate IP 500mg", category = "Antibiotics", priceBrand = 148.0, priceGeneric = 40.0),
        MedicineEntity(brandName = "Cetrizine 10", genericName = "Cetirizine 10mg", composition = "Cetirizine Dihydrochloride IP 10mg", category = "Allergy", priceBrand = 38.0, priceGeneric = 5.20),
        MedicineEntity(brandName = "Glycomet 500", genericName = "Metformin 500mg", composition = "Metformin IP 500mg", category = "Diabetes", priceBrand = 85.0, priceGeneric = 15.0),
        MedicineEntity(brandName = "Aspirin 75", genericName = "Aspirin 75mg", composition = "Acetylsalicylic Acid IP 75mg", category = "Cardiology", priceBrand = 30.0, priceGeneric = 8.0)
    )

    val stores = listOf(
        StoreEntity(
            name = "Pradhan Mantri Bhartiya Janaushadhi Kendra - Devaraja Mohalla",
            address = "D.No 1234, JLB Road, Devaraja Mohalla, Mysuru, Karnataka 570001",
            latitude = 12.3082,
            longitude = 76.6450,
            phoneNumber = "0821-2420000",
            openingTime = "09:00 AM",
            closingTime = "09:00 PM"
        ),
        StoreEntity(
            name = "PMBJK - Kuvempunagar",
            address = "Shop No 45, M-Block, Kuvempunagar, Mysuru, Karnataka 570023",
            latitude = 12.2850,
            longitude = 76.6230,
            phoneNumber = "0821-2345678",
            openingTime = "09:30 AM",
            closingTime = "08:30 PM"
        ),
        StoreEntity(
            name = "Janaushadhi Kendra - Majestic",
            address = "Platform No 1 Side, Majestic Bus Stand, Bengaluru, Karnataka 560009",
            latitude = 12.9772,
            longitude = 77.5708,
            phoneNumber = "080-22221111",
            openingTime = "06:00 AM",
            closingTime = "11:00 PM"
        ),
        StoreEntity(
            name = "PMBJK - Jayanagar 4th Block",
            address = "Shopping Complex, Jayanagar, Bengaluru, Karnataka 560011",
            latitude = 12.9284,
            longitude = 77.5828,
            phoneNumber = "080-26665555",
            openingTime = "09:00 AM",
            closingTime = "10:00 PM"
        )
    )

    val locations = listOf(
        FavoriteLocationEntity(name = "Mysuru Palace", latitude = 12.3051, longitude = 76.6552),
        FavoriteLocationEntity(name = "Vidhana Soudha", latitude = 12.9796, longitude = 77.5906)
    )
}
