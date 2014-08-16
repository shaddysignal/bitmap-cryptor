package bcrypt

import scala.io._

object Main {

    def main(args: Array[String]) {
        if (args.size < 2) {
            println("Usage: $1 $2; $1 - secret data, $2 - secret key")
            return
        }

        var secretFile = args(0)
        var secretKey = args(1)

        var outputFile =
            BitmapCrypt.crypt(secretFile, secretKey)
        println(s"result: $outputFile")
    }

}

// vim: set ts=4 sw=4 et:
