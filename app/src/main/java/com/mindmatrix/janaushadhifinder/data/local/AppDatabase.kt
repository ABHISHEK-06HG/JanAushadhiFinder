package com.mindmatrix.janaushadhifinder.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mindmatrix.janaushadhifinder.data.local.dao.LocationDao
import com.mindmatrix.janaushadhifinder.data.local.dao.MedicineDao
import com.mindmatrix.janaushadhifinder.data.local.dao.ReminderDao
import com.mindmatrix.janaushadhifinder.data.local.dao.StoreDao
import com.mindmatrix.janaushadhifinder.data.local.entity.*

import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        MedicineEntity::class,
        ReminderEntity::class,
        ReminderTimeEntity::class,
        IntakeRecordEntity::class,
        StoreEntity::class,
        FavoriteLocationEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun reminderDao(): ReminderDao
    abstract fun storeDao(): StoreDao
    abstract fun locationDao(): LocationDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jan_aushadhi_db"
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        seedDatabase(context)
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        seedDatabase(context)
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        private fun seedDatabase(context: Context) {
            CoroutineScope(Dispatchers.IO).launch {
                val database = getInstance(context)
                // Only seed if empty to avoid duplicates
                if (database.medicineDao().getCount() == 0) {
                    database.medicineDao().insertAll(SeedData.medicines)
                }
                if (database.storeDao().getCount() == 0) {
                    database.storeDao().insertStores(SeedData.stores)
                }
                if (database.locationDao().getCount() == 0) {
                    SeedData.locations.forEach {
                        database.locationDao().insertLocation(it)
                    }
                }
            }
        }
    }
}
