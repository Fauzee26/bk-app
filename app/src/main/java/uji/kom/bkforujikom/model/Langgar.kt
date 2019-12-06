package uji.kom.bkforujikom.model

class Langgar {
    var isSelected: Boolean = false
    var langgar: String? = null

    fun getLangggar(): String {
        return langgar.toString()
    }

    fun setLangggar(animal: String) {
        this.langgar = animal
    }

    fun getSelecteds(): Boolean {
        return isSelected
    }

    fun setSelecteds(selected: Boolean) {
        isSelected = selected
    }
}