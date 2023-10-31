package com.mark

import com.mark.tasks.CacheTask
import java.io.File
import java.nio.file.Path
import kotlin.io.path.Path

val DEFAULT_PATH: File = Path("data", "cache").toFile()

class Builder(var type : TaskType) {

    var extraTasks: List<CacheTask> = emptyList()
    var cacheLocation: File = DEFAULT_PATH
    var cacheRevision: Int = -1
    var js5Revision: Int = -1
    var js5Ports: List<Int> = listOf(443, 43594, 50000)
    var supportPrefetch: Boolean = true
    fun extraTasks(vararg type: CacheTask) = apply { this.extraTasks = type.toList() }

    fun extraTasks(types: List<CacheTask>) = apply { this.extraTasks = types }

    fun cacheLocation(cacheLocation: File) = apply { this.cacheLocation = cacheLocation }
    fun js5Revision(rev: Int) = apply { this.js5Revision = rev }
    fun cacheRevision(rev: Int) = apply { this.cacheRevision = rev }
    fun js5Ports(ports: List<Int>) = apply { this.js5Ports = ports }
    fun supportPrefetch(state: Boolean) = apply { this.supportPrefetch = state }
    fun build(): Application {

        if (type == TaskType.RUN_JS5) {
            if (js5Revision == -1) error("Please define a js5 Revision your network is using with js5Revision(rev)")
        }
        if (type == TaskType.UPDATE_REV) {
            if(cacheRevision == -1) logger.info { "Using Cache Rev [-1] this will download latest osrs cache when updating revs" }
        }

        return Application(this)
    }

    override fun toString(): String {
        return "Builder(type=$type, extraTasks=$extraTasks, ${if (extraTasks.isEmpty()) "No extra tasks defined" else ""}" +
                "cacheLocation=$cacheLocation ${if (cacheLocation == DEFAULT_PATH) "Is your Path Correct?" else ""}, " +
                "cacheRevision=$cacheRevision ${if (cacheRevision == -1) "will download latest OSRS cache" else ""}, " +
                "js5Revision=$js5Revision, " +
                "js5Ports=$js5Ports)"
    }


}