package uji.kom.bkforujikom.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pelanggaran(
    var nama: String? = null,
    var tgl: String? = null,
    var wali: String? = null,
    var pelanggaran: String? = null,
    var ket: String? = null,
    var kelas: String? = null
) : Parcelable