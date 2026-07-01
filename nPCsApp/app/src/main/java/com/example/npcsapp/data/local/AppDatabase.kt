package com.example.npcsapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.npcsapp.data.local.entities.BuildComponentEntity
import com.example.npcsapp.data.local.entities.BuildEntity
import com.example.npcsapp.data.local.entities.CPUCoolerEntity
import com.example.npcsapp.data.local.entities.CPUEntity
import com.example.npcsapp.data.local.entities.CaseEntity
import com.example.npcsapp.data.local.entities.GPUEntity
import com.example.npcsapp.data.local.entities.MarketItemEntity
import com.example.npcsapp.data.local.entities.MotherboardEntity
import com.example.npcsapp.data.local.entities.PSUEntity
import com.example.npcsapp.data.local.entities.RAMEntity
import com.example.npcsapp.data.local.entities.StorageEntity
import com.example.npcsapp.data.local.entities.user.UserEntity
import com.example.npcsapp.data.local.entities.user.ChatEntity
import com.example.npcsapp.data.local.entities.user.MessageEntity
import com.example.npcsapp.data.local.entities.user.ChatParticipantEntity

@Database(
    entities = [
        GPUEntity::class,
        CPUEntity::class,
        MotherboardEntity::class,
        RAMEntity::class,
        StorageEntity::class,
        PSUEntity::class,
        CaseEntity::class,
        CPUCoolerEntity::class,
        BuildEntity::class,
        BuildComponentEntity::class,
        UserEntity::class,
        ChatEntity::class,
        MessageEntity::class,
        ChatParticipantEntity::class,
        MarketItemEntity::class
    ],
    version = 10,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun GPUDao(): GPUDao
    abstract fun CPUDao(): CPUDao
    abstract fun MotherboardDao(): MotherboardDao
    abstract fun RAMDao(): RAMDao
    abstract fun StorageDao(): StorageDao
    abstract fun PSUDao(): PSUDao
    abstract fun CaseDao(): CaseDao
    abstract fun CPUCoolerDao(): CPUCoolerDao
    abstract fun BuildDao(): BuildDao
    abstract fun BuildComponentDao(): BuildComponentDao
    abstract fun UserDao(): UserDao
    abstract fun ChatDao(): ChatDao
    abstract fun MarketItemDao(): MarketItemDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pc_component_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
