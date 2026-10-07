package com.example.data.model

data class Reciter(
    val id: String,
    val nameArabic: String,
    val nameEnglish: String,
    val subStyle: String,
    val serverFolder: String,
    val bitrate: String = "128kbps",
    val description: String = ""
) {
    fun getAudioUrl(surahNumber: Int, ayahNumber: Int): String {
        val s = surahNumber.toString().padStart(3, '0')
        val a = ayahNumber.toString().padStart(3, '0')
        return "https://everyayah.com/data/$serverFolder/$s$a.mp3"
    }

    companion object {
        val DEFAULT_RECITERS = listOf(
            Reciter(
                id = "alafasy",
                nameArabic = "مشاري راشد العفاسي",
                nameEnglish = "Mishary Rashid Alafasy",
                subStyle = "ترتيل هادئ وخاشع",
                serverFolder = "Alafasy_128kbps",
                description = "صوت ندي متقن لأحكام التلاوة والوقف"
            ),
            Reciter(
                id = "abdul_basit_murattal",
                nameArabic = "عبد الباسط عبد الصمد",
                nameEnglish = "Abdul Basit Murattal",
                subStyle = "المصحف المرتل",
                serverFolder = "Abdul_Basit_Murattal_192kbps",
                description = "صوت السماء وقارئ القرن العشرين"
            ),
            Reciter(
                id = "abdul_basit_mujawwad",
                nameArabic = "عبد الباسط عبد الصمد (مجوّد)",
                nameEnglish = "Abdul Basit Mujawwad",
                subStyle = "المصحف المجوّد الخاشع",
                serverFolder = "Abdul_Basit_Mujawwad_128kbps",
                description = "تلاوة مجودة خاشعة ذات مقامات عالية"
            ),
            Reciter(
                id = "al_minshawi",
                nameArabic = "محمد صديق المنشاوي",
                nameEnglish = "Mohamed Siddiq Al-Minshawi",
                subStyle = "الترتيل الباكي الخاشع",
                serverFolder = "Minshawy_Murattal_128kbps",
                description = "صوت شجي يفيض بالخشوع والسكينة"
            ),
            Reciter(
                id = "al_hussary",
                nameArabic = "محمود خليل الحصري",
                nameEnglish = "Mahmoud Khalil Al-Hussary",
                subStyle = "شيخ المقارئ المصرية",
                serverFolder = "Husary_128kbps",
                description = "الإتقان التام لمخارج الحروف والتجويد"
            ),
            Reciter(
                id = "al_ghamdi",
                nameArabic = "سعد الغامدي",
                nameEnglish = "Saad Al-Ghamdi",
                subStyle = "ترتيل مميز وسريع",
                serverFolder = "Ghamadi_40kbps",
                description = "تلاوة عذبة محبوبة لجميع الأعمار"
            ),
            Reciter(
                id = "al_muaiqly",
                nameArabic = "ماهر المعيقلي",
                nameEnglish = "Maher Al-Muaiqly",
                subStyle = "إمام الحرم المكي الشريف",
                serverFolder = "Maher_AlMuaiqly_64kbps",
                description = "نبرة حجازية روحانية ومؤثرة"
            ),
            Reciter(
                id = "al_shatri",
                nameArabic = "أبو بكر الشاطري",
                nameEnglish = "Abu Bakr Al-Shatri",
                subStyle = "ترتيل حزين وخاشع",
                serverFolder = "Abu_Bakr_Ash-Shaatree_128kbps",
                description = "نغمات شجية ترق لها القلوب"
            )
        )
    }
}
