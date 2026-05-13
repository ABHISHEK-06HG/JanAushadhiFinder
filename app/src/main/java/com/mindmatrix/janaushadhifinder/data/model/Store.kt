package com.mindmatrix.janaushadhifinder.data.model

// UC-JAF-03: Simulated store data (no live GPS per SRD OOS-02)
data class Store(
    val id: Int,
    val name: String,
    val address: String,
    val distanceKm: Double,
    val isOpen: Boolean,
    val openTime: String = "9:00 AM",
    val closeTime: String = "8:00 PM",
    val phone: String = ""
)

object StoreRepository {
    // Simulated Jan-Aushadhi Kendras (SRD §4 — Google Maps API simulated)
    val stores = listOf(
        Store(1, "JAK — Mysuru Central", "Near KR Hospital, Mysuru", 1.2, true, phone = "0821-2412345"),
        Store(2, "JAK — Vijayanagar", "Vijayanagar Main Road, Mysuru", 3.8, true, phone = "0821-2567890"),
        Store(3, "JAK — Hebbal", "Hebbal Ring Road, Mysuru", 6.1, false, openTime = "9:00 AM"),
        Store(4, "JAK — Saraswathipuram", "Saraswathipuram, Mysuru", 4.5, true, phone = "0821-2445678"),
        Store(5, "JAK — Jayalakshmipuram", "Jayalakshmipuram, Mysuru", 5.2, true, phone = "0821-2398765"),
        Store(6, "JAK — Kuvempunagar", "Kuvempunagar, Mysuru", 7.8, false, openTime = "10:00 AM"),
        Store(7, "JAK — Nazarbad", "Nazarbad Main Road, Mysuru", 2.9, true, phone = "0821-2476543"),
        Store(8, "JAK — Bannimantap", "Bannimantap, Mysuru", 8.4, true, phone = "0821-2512345"),
        Store(9, "JAK — Yadavgiri", "Yadavgiri, Mysuru", 9.1, false, openTime = "9:30 AM"),
        Store(10, "JAK — Gokulam", "Gokulam 3rd Stage, Mysuru", 6.8, true, phone = "0821-2534567")
    )
}
