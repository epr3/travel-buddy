package com.ase.eu.travel_buddy;


import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class TraseuDateConverter {

    @TypeConverter
    public static Date toDate(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long toLong(Date value) {
        return value == null ? null : value.getTime();
    }
}
