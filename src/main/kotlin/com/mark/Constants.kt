package com.mark

import com.mark.tasks.CacheTask
import com.mark.tasks.impl.PackMaps
import com.mark.tasks.impl.PackModels
import com.mark.tasks.impl.objects.PackItems
import com.mark.tasks.impl.objects.PackNpcs
import com.mark.tasks.impl.objects.PackObjects
import java.io.File


val DATA_LOCATION = File("./data")
val CACHE_LOCATION = File(DATA_LOCATION,"cache")
val RAW_CACHE_LOCATION = File(DATA_LOCATION,"raw-cache")

fun getCustomDir(loc : String): File {
    return File(RAW_CACHE_LOCATION,loc)
}

val tasks : MutableList<CacheTask> = listOf(
    PackModels(getCustomDir("models")),
    PackMaps(getCustomDir("maps")),
    PackItems(File(getCustomDir("definitions"),"items")),
    PackObjects(File(getCustomDir("definitions"),"objects")),
    PackNpcs(File(getCustomDir("definitions"),"npcs")),
    PackSprites(getCustomDir("sprites")),
    PackDats(getCustomDir("dats"))
).toMutableList()