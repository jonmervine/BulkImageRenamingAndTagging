import arrow.core.Either
import com.darkmage530.birat.file.File
import com.darkmage530.birat.file.ImageError
import org.apache.commons.codec.digest.DigestUtils
import java.io.FileInputStream

fun getMd5(image: File) =
    Either.catch {
        FileInputStream(image.fileReference).use { fis ->
            DigestUtils.md5Hex(fis)
        }
    }.mapLeft {
        ImageError.Md5Error(it)
    }


typealias Md5 = String