package com.mark

import com.displee.cache.CacheLibrary
import com.mark.tasks.CacheTask
import com.mark.util.getFiles
import com.mark.util.progress
import mu.KotlinLogging
import java.io.File

val getIdxForFile = mapOf(
    "interface.dat" to 0,
    "media.dat" to 1,
    "index.dat" to 2,
    "b12_full.dat" to 3,
    "p11_full.dat" to 4,
    "p12_full.dat" to 5,
    "q8_full.dat" to 6,
)

class PackDats(private val datsDir : File) : CacheTask() {
    override fun init(library: CacheLibrary) {
        val datsSize = getFiles(datsDir,"dat","idx").size
        val progressDats = progress("Packing Dats", datsSize)

        if (datsSize != 0) {
            getFiles(datsDir,"dat","idx").forEach {

                try {



                    if (getIdxForFile.contains(it.name)) {
                        library.put(2, 41, getIdxForFile[it.name]!!, it.readBytes())
                        progressDats.extraMessage = it.name
                        progressDats.step()
                    } else {
                        println("Unable to pack ${it.name} its missing an index")
                    }

                }catch (e : Exception) {
                    e.printStackTrace()
                    println("Unable to pack ${it.name} is the name a int?")
                }
            }

            progressDats.close()


        }
    }

}