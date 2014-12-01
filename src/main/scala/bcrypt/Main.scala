package bcrypt

import scala.io._

object Main {

    def main(args: Array[String]) {
        if (args.size < 3) {
            println("Usage: crypt $1 $2; $1 - secret data, $2 - secret key")
            println("Usage: decrypt $1 $2; $1 - crypt file, $2 - secret key")
            return
        }

        var command = args(0)
        var secretFile = args(1)
        var secretKey = args(2)

        if (command == "crypt") {
            var outputFile = BitmapCrypt.crypt(secretFile, secretKey)
            println(s"result: $outputFile")
        } else if (command == "decrypt") {
            println(BitmapCrypt.decrypt(secretFile, secretKey))
        }
        else println("fail")
    }

}

// vim: set ts=4 sw=4 et:
