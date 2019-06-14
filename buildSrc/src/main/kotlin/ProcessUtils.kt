import java.io.BufferedReader
import java.io.InputStreamReader

fun String.execute(): Process = Runtime.getRuntime().exec(this)

val Process.text: String get() = BufferedReader(InputStreamReader(inputStream)).readLine()