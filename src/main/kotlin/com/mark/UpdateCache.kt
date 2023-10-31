package com.mark

import com.mark.Builder
import com.mark.TaskType
import com.mark.tasks.CacheTask
import com.mark.tasks.impl.RemoveXteas

object UpdateCache {
    fun init() {
        val updateTasks: MutableList<CacheTask> = listOf(RemoveXteas()).toMutableList()
        updateTasks.addAll(tasks)

        val app = Builder(TaskType.UPDATE_REV)
            .cacheLocation(CACHE_LOCATION)
            .cacheRevision(217)
            .extraTasks(updateTasks).build()
        app.initialize()
    }

}

fun main(args: Array<String>) {
    UpdateCache.init()
}
