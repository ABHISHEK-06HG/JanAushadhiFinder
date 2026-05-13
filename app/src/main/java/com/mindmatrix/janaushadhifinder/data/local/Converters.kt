package com.mindmatrix.janaushadhifinder.data.local

import androidx.room.TypeConverter
import com.mindmatrix.janaushadhifinder.data.model.IntakeStatus
import com.mindmatrix.janaushadhifinder.data.model.RepeatType

class Converters {
    @TypeConverter
    fun fromRepeatType(value: RepeatType): String = value.name

    @TypeConverter
    fun toRepeatType(value: String): RepeatType = RepeatType.valueOf(value)

    @TypeConverter
    fun fromIntakeStatus(value: IntakeStatus): String = value.name

    @TypeConverter
    fun toIntakeStatus(value: String): IntakeStatus = IntakeStatus.valueOf(value)
}
