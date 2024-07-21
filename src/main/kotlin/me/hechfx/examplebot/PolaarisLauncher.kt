package me.hechfx.examplebot

import kotlinx.coroutines.runBlocking
import java.io.File

object PolaarisLauncher {
    @JvmStatic
    fun main(args: Array<String>) {
        val tokenFile = File("./token.txt")

        if (!tokenFile.exists()) {
            tokenFile.createNewFile()
            tokenFile.writeText("your_token_here")
            println("Token file created! Please, put your token in token.txt")
            return
        }

        val client = PolaarisBot(tokenFile)

        runBlocking {
            client.start()
        }
    }
}