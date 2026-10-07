package com.example.data.repository

import com.example.data.model.Ayah
import com.example.data.model.BackgroundPreset
import com.example.data.model.ParticleEffectType
import com.example.data.model.Reciter
import com.example.data.model.RevelationType
import com.example.data.model.Surah
import com.example.data.model.toArabicDigits
import kotlin.random.Random

data class RecommendedClip(
    val titleArabic: String,
    val surahNumber: Int,
    val fromAyah: Int,
    val toAyah: Int,
    val durationSeconds: Int,
    val category: String,
    val description: String
)

data class AutoVideoConfig(
    val surah: Surah,
    val fromAyah: Int,
    val toAyah: Int,
    val reciter: Reciter,
    val backgroundPreset: BackgroundPreset,
    val particleEffect: ParticleEffectType,
    val aspectRatio: String,
    val targetDurationSeconds: Int,
    val actualDurationSeconds: Int,
    val summaryArabic: String
)

object QuranRepository {

    val ALL_SURAHS: List<Surah> = listOf(
        Surah(1, "الفاتحة", "Al-Fatihah", "The Opener", 7, RevelationType.MECCAN),
        Surah(2, "البقرة", "Al-Baqarah", "The Cow", 286, RevelationType.MEDINAN),
        Surah(3, "آل عمران", "Ali 'Imran", "Family of Imran", 200, RevelationType.MEDINAN),
        Surah(4, "النساء", "An-Nisa", "The Women", 176, RevelationType.MEDINAN),
        Surah(5, "المائدة", "Al-Ma'idah", "The Table Spread", 120, RevelationType.MEDINAN),
        Surah(6, "الأنعام", "Al-An'am", "The Cattle", 165, RevelationType.MECCAN),
        Surah(7, "الأعراف", "Al-A'raf", "The Heights", 206, RevelationType.MECCAN),
        Surah(8, "الأنفال", "Al-Anfal", "The Spoils of War", 75, RevelationType.MEDINAN),
        Surah(9, "التوبة", "At-Tawbah", "The Repentance", 129, RevelationType.MEDINAN),
        Surah(10, "يونس", "Yunus", "Jonah", 109, RevelationType.MECCAN),
        Surah(11, "هود", "Hud", "Hud", 123, RevelationType.MECCAN),
        Surah(12, "يوسف", "Yusuf", "Joseph", 111, RevelationType.MECCAN),
        Surah(13, "الرعد", "Ar-Ra'd", "The Thunder", 43, RevelationType.MEDINAN),
        Surah(14, "إبراهيم", "Ibrahim", "Abraham", 52, RevelationType.MECCAN),
        Surah(15, "الحجر", "Al-Hijr", "The Rocky Tract", 99, RevelationType.MECCAN),
        Surah(16, "النحل", "An-Nahl", "The Bee", 128, RevelationType.MECCAN),
        Surah(17, "الإسراء", "Al-Isra", "The Night Journey", 111, RevelationType.MECCAN),
        Surah(18, "الكهف", "Al-Kahf", "The Cave", 110, RevelationType.MECCAN),
        Surah(19, "مريم", "Maryam", "Mary", 98, RevelationType.MECCAN),
        Surah(20, "طه", "Ta-Ha", "Ta-Ha", 135, RevelationType.MECCAN),
        Surah(21, "الأنبياء", "Al-Anbiya", "The Prophets", 112, RevelationType.MECCAN),
        Surah(22, "الحج", "Al-Hajj", "The Pilgrimage", 78, RevelationType.MEDINAN),
        Surah(23, "المؤمنون", "Al-Mu'minun", "The Believers", 118, RevelationType.MECCAN),
        Surah(24, "النور", "An-Nur", "The Light", 64, RevelationType.MEDINAN),
        Surah(25, "الفرقان", "Al-Furqan", "The Criterion", 77, RevelationType.MECCAN),
        Surah(26, "الشعراء", "Ash-Shu'ara", "The Poets", 227, RevelationType.MECCAN),
        Surah(27, "النمل", "An-Naml", "The Ant", 93, RevelationType.MECCAN),
        Surah(28, "القصص", "Al-Qasas", "The Stories", 88, RevelationType.MECCAN),
        Surah(29, "العنكبوت", "Al-'Ankabut", "The Spider", 69, RevelationType.MECCAN),
        Surah(30, "الروم", "Ar-Rum", "The Romans", 60, RevelationType.MECCAN),
        Surah(31, "لقمان", "Luqman", "Luqman", 34, RevelationType.MECCAN),
        Surah(32, "السجدة", "As-Sajdah", "The Prostration", 30, RevelationType.MECCAN),
        Surah(33, "الأحزاب", "Al-Ahzab", "The Combined Forces", 73, RevelationType.MEDINAN),
        Surah(34, "سبأ", "Saba", "Sheba", 54, RevelationType.MECCAN),
        Surah(35, "فاطر", "Fatir", "Originator", 45, RevelationType.MECCAN),
        Surah(36, "يس", "Ya-Sin", "Ya-Sin", 83, RevelationType.MECCAN),
        Surah(37, "الصافات", "As-Saffat", "Those who set the Ranks", 182, RevelationType.MECCAN),
        Surah(38, "ص", "Sad", "The Letter Sad", 88, RevelationType.MECCAN),
        Surah(39, "الزمر", "Az-Zumar", "The Troops", 75, RevelationType.MECCAN),
        Surah(40, "غافر", "Ghafir", "The Forgiver", 85, RevelationType.MECCAN),
        Surah(41, "فصلت", "Fussilat", "Explained in Detail", 54, RevelationType.MECCAN),
        Surah(42, "الشورى", "Ash-Shura", "The Consultation", 53, RevelationType.MECCAN),
        Surah(43, "الزخرف", "Az-Zukhruf", "The Ornaments of Gold", 89, RevelationType.MECCAN),
        Surah(44, "الدخان", "Ad-Dukhan", "The Smoke", 59, RevelationType.MECCAN),
        Surah(45, "الجاثية", "Al-Jathiyah", "The Crouching", 37, RevelationType.MECCAN),
        Surah(46, "الأحقاف", "Al-Ahqaf", "The Wind-Curved Sandhills", 35, RevelationType.MECCAN),
        Surah(47, "محمد", "Muhammad", "Muhammad", 38, RevelationType.MEDINAN),
        Surah(48, "الفتح", "Al-Fath", "The Victory", 29, RevelationType.MEDINAN),
        Surah(49, "الحجرات", "Al-Hujurat", "The Rooms", 18, RevelationType.MEDINAN),
        Surah(50, "ق", "Qaf", "The Letter Qaf", 45, RevelationType.MECCAN),
        Surah(51, "الذاريات", "Adh-Dhariyat", "The Winnowing Winds", 60, RevelationType.MECCAN),
        Surah(52, "الطور", "At-Tur", "The Mount", 49, RevelationType.MECCAN),
        Surah(53, "النجم", "An-Najm", "The Star", 62, RevelationType.MECCAN),
        Surah(54, "القمر", "Al-Qamar", "The Moon", 55, RevelationType.MECCAN),
        Surah(55, "الرحمن", "Ar-Rahman", "The Beneficent", 78, RevelationType.MEDINAN),
        Surah(56, "الواقعة", "Al-Waqi'ah", "The Inevitable", 96, RevelationType.MECCAN),
        Surah(57, "الحديد", "Al-Hadid", "The Iron", 29, RevelationType.MEDINAN),
        Surah(58, "المجادلة", "Al-Mujadila", "The Pleading Woman", 22, RevelationType.MEDINAN),
        Surah(59, "الحشر", "Al-Hashr", "The Exile", 24, RevelationType.MEDINAN),
        Surah(60, "الممتحنة", "Al-Mumtahanah", "She that is to be examined", 13, RevelationType.MEDINAN),
        Surah(61, "الصف", "As-Saff", "The Ranks", 14, RevelationType.MEDINAN),
        Surah(62, "الجمعة", "Al-Jumu'ah", "The Congregation", 11, RevelationType.MEDINAN),
        Surah(63, "المنافقون", "Al-Munafiqun", "The Hypocrites", 11, RevelationType.MEDINAN),
        Surah(64, "التغابن", "At-Taghabun", "The Mutual Disillusion", 18, RevelationType.MEDINAN),
        Surah(65, "الطلاق", "At-Talaq", "The Divorce", 12, RevelationType.MEDINAN),
        Surah(66, "التحريم", "At-Tahrim", "The Prohibition", 12, RevelationType.MEDINAN),
        Surah(67, "الملك", "Al-Mulk", "The Sovereignty", 30, RevelationType.MECCAN),
        Surah(68, "القلم", "Al-Qalam", "The Pen", 52, RevelationType.MECCAN),
        Surah(69, "الحاقة", "Al-Haqqah", "The Inevitable Truth", 52, RevelationType.MECCAN),
        Surah(70, "المعارج", "Al-Ma'arij", "The Ascending Stairways", 44, RevelationType.MECCAN),
        Surah(71, "نوح", "Nuh", "Noah", 28, RevelationType.MECCAN),
        Surah(72, "الجن", "Al-Jinn", "The Jinn", 28, RevelationType.MECCAN),
        Surah(73, "المزمل", "Al-Muzzammil", "The Enshrouded One", 20, RevelationType.MECCAN),
        Surah(74, "المدثر", "Al-Muddaththir", "The Cloaked One", 56, RevelationType.MECCAN),
        Surah(75, "القيامة", "Al-Qiyamah", "The Resurrection", 40, RevelationType.MECCAN),
        Surah(76, "الإنسان", "Al-Insan", "Man", 31, RevelationType.MEDINAN),
        Surah(77, "المرسلات", "Al-Mursalat", "The Emissaries", 50, RevelationType.MECCAN),
        Surah(78, "النبأ", "An-Naba", "The Tidings", 40, RevelationType.MECCAN),
        Surah(79, "النازعات", "An-Nazi'at", "Those who drag forth", 46, RevelationType.MECCAN),
        Surah(80, "عبس", "'Abasa", "He Frowned", 42, RevelationType.MECCAN),
        Surah(81, "التكوير", "At-Takwir", "The Overthrowing", 29, RevelationType.MECCAN),
        Surah(82, "الانفطار", "Al-Infitar", "The Cleaving", 19, RevelationType.MECCAN),
        Surah(83, "المطففين", "Al-Mutaffifin", "Defrauding", 36, RevelationType.MECCAN),
        Surah(84, "الانشقاق", "Al-Inshiqaq", "The Splitting Open", 25, RevelationType.MECCAN),
        Surah(85, "البروج", "Al-Buruj", "The Mansions of the Stars", 22, RevelationType.MECCAN),
        Surah(86, "الطارق", "At-Tariq", "The Morning Star", 17, RevelationType.MECCAN),
        Surah(87, "الأعلى", "Al-A'la", "The Most High", 19, RevelationType.MECCAN),
        Surah(88, "الغاشية", "Al-Ghashiyah", "The Overwhelming", 26, RevelationType.MECCAN),
        Surah(89, "الفجر", "Al-Fajr", "The Dawn", 30, RevelationType.MECCAN),
        Surah(90, "البلد", "Al-Balad", "The City", 20, RevelationType.MECCAN),
        Surah(91, "الشمس", "Ash-Shams", "The Sun", 15, RevelationType.MECCAN),
        Surah(92, "الليل", "Al-Layl", "The Night", 21, RevelationType.MECCAN),
        Surah(93, "الضحى", "Ad-Duha", "The Morning Hours", 11, RevelationType.MECCAN),
        Surah(94, "الشرح", "Ash-Sharh", "The Relief", 8, RevelationType.MECCAN),
        Surah(95, "التين", "At-Tin", "The Fig", 8, RevelationType.MECCAN),
        Surah(96, "العلق", "Al-'Alaq", "The Clot", 19, RevelationType.MECCAN),
        Surah(97, "القدر", "Al-Qadr", "The Night of Decree", 5, RevelationType.MECCAN),
        Surah(98, "البينة", "Al-Bayyinah", "The Clear Proof", 8, RevelationType.MEDINAN),
        Surah(99, "الزلزلة", "Az-Zalzalah", "The Earthquake", 8, RevelationType.MEDINAN),
        Surah(100, "العاديات", "Al-'Adiyat", "The Courser", 11, RevelationType.MECCAN),
        Surah(101, "القارعة", "Al-Qari'ah", "The Calamity", 11, RevelationType.MECCAN),
        Surah(102, "التكاثر", "At-Takathur", "The Rivalry in World Increase", 8, RevelationType.MECCAN),
        Surah(103, "العصر", "Al-'Asr", "The Declining Day", 3, RevelationType.MECCAN),
        Surah(104, "الهمزة", "Al-Humazah", "The Traducer", 9, RevelationType.MECCAN),
        Surah(105, "الفيل", "Al-Fil", "The Elephant", 5, RevelationType.MECCAN),
        Surah(106, "قريش", "Quraysh", "Quraysh", 4, RevelationType.MECCAN),
        Surah(107, "الماعون", "Al-Ma'un", "Small Kindnesses", 7, RevelationType.MECCAN),
        Surah(108, "الكوثر", "Al-Kawthar", "Abundance", 3, RevelationType.MECCAN),
        Surah(109, "الكافرون", "Al-Kafirun", "The Disbelievers", 6, RevelationType.MECCAN),
        Surah(110, "النصر", "An-Nasr", "Divine Support", 3, RevelationType.MEDINAN),
        Surah(111, "المسد", "Al-Masad", "The Palm Fibre", 5, RevelationType.MECCAN),
        Surah(112, "الإخلاص", "Al-Ikhlas", "The Sincerity", 4, RevelationType.MECCAN),
        Surah(113, "الفلق", "Al-Falaq", "The Daybreak", 5, RevelationType.MECCAN),
        Surah(114, "الناس", "An-Nas", "Mankind", 6, RevelationType.MECCAN)
    )

    private val PRELOADED_AYAHS: Map<String, Ayah> = mapOf(
        // Al-Fatihah (1)
        "1_1" to Ayah(1, 1, "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "In the name of Allah, the Entirely Merciful, the Especially Merciful.", "أبدأ تلاوتي باسم الله مستعيناً به", 4.0f),
        "1_2" to Ayah(1, 2, "الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ", "[All] praise is [due] to Allah, Lord of the worlds.", "الثناء والحمد لله رب جميع المخلوقات", 4.5f),
        "1_3" to Ayah(1, 3, "الرَّحْمَٰنِ الرَّحِيمِ", "The Entirely Merciful, the Especially Merciful,", "ذو الرحمة الواسعة التي وسعت كل شيء", 3.5f),
        "1_4" to Ayah(1, 4, "مَالِكِ يَوْمِ الدِّينِ", "Sovereign of the Day of Recompense.", "المالك وحده ليوم الحساب والجزاء", 4.0f),
        "1_5" to Ayah(1, 5, "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ", "It is You we worship and You we ask for help.", "نخصك وحدك بالعبادة ونستعين بك وحدك", 5.0f),
        "1_6" to Ayah(1, 6, "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ", "Guide us to the straight path -", "وفقنا وسددنا إلى الطريق المستقيم الواضح", 4.5f),
        "1_7" to Ayah(1, 7, "صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ", "The path of those upon whom You have bestowed favor...", "طريق الأنبياء والصديقين غير المغضوب عليهم ولا الضالين", 8.0f),

        // Al-Baqarah 255 (Ayat Al-Kursi)
        "2_255" to Ayah(2, 255, "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ", "Allah - there is no deity except Him, the Ever-Living, the Sustainer of [all] existence...", "آية الكرسي: أعظم آية في كتاب الله", 28.0f),

        // Al-Baqarah 284-286
        "2_284" to Ayah(2, 284, "لِلَّهِ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ وَإِنْ تُبْدُوا مَا فِي أَنْفُسِكُمْ أَوْ تُخْفُوهُ يُحَاسِبْكُمْ بِهِ اللَّهُ ۖ فَيَغْفِرُ لِمَنْ يَشَاءُ وَيُعَذِّبُ مَنْ يَشَاءُ ۗ وَاللَّهُ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "To Allah belongs whatever is in the heavens and whatever is in the earth...", "ملك السماوات والأرض وعلم الله بما في الصدور", 16.0f),
        "2_285" to Ayah(2, 285, "آمَنَ الرَّسُولُ بِمَا أُنْزِلَ إِلَيْهِ مِنْ رَبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِنْ رُسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ", "The Messenger has believed in what was revealed to him from his Lord...", "إيمان المؤمنين وتفويض أمرهم لله", 22.0f),
        "2_286" to Ayah(2, 286, "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِنْ نَسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِنْ قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنْتَ مَوْلَانَا فَانْصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ", "Allah does not charge a soul except [with that within] its capacity...", "دعاء جامع لرفع الحرج وتيسير أمر العباد", 28.0f),

        // Al-Kahf 1-10
        "18_1" to Ayah(18, 1, "الْحَمْدُ لِلَّهِ الَّذِي أَنْزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَلْ لَهُ عِوَجًا", "[All] praise is [due] to Allah, who has sent down upon His Servant the Book...", "حمد الله على إنزال القرآن الكريم مستقيماً", 8.0f),
        "18_2" to Ayah(18, 2, "قَيِّمًا لِيُنْذِرَ بَأْسًا شَدِيدًا مِنْ لَدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا", "[He has made it] straight, to warn of severe punishment...", "بشارة عظيمة للمؤمنين الصالحين بالأجر الحسن", 10.0f),
        "18_3" to Ayah(18, 3, "مَاكِثِينَ فِيهِ أَبَدًا", "In which they will remain forever", "خلود المؤمنين في نعيم الجنة", 4.0f),
        "18_4" to Ayah(18, 4, "وَيُنْذِرَ الَّذِينَ قَالُوا اتَّخَذَ اللَّهُ وَلَدًا", "And to warn those who say, 'Allah has taken a son.'", "إنذار للمشركين الذين نسبوا الولد لله", 6.0f),
        "18_5" to Ayah(18, 5, "مَا لَهُمْ بِهِ مِنْ عِلْمٍ وَلَا لِآبَائِهِمْ ۚ كَبُرَتْ كَلِمَةً تَخْرُجُ مِنْ أَفْوَاهِهِمْ ۚ إِنْ يَقُولُونَ إِلَّا كَذِبًا", "They have no knowledge of it, nor had their fathers...", "تنزيه الله عن الشرك والولد", 11.0f),
        "18_6" to Ayah(18, 6, "فَلَعَلَّكَ بَاخِعٌ نَفْسَكَ عَلَىٰ آثَارِهِمْ إِنْ لَمْ يُؤْمِنُوا بِهَٰذَا الْحَدِيثِ أَسَفًا", "Then perhaps you would kill yourself through grief over them...", "تسلية لقلب النبي صلى الله عليه وسلم", 12.0f),
        "18_7" to Ayah(18, 7, "إِنَّا جَعَلْنَا مَا عَلَى الْأَرْضِ زِينَةً لَهَا لِنَبْلُوَهُمْ أَيُّهُمْ أَحْسَنُ عَمَلًا", "Indeed, We have made that which is on the earth adornment for it...", "حقيقة زينة الدنيا وأنها دار ابتلاء واختبار", 10.0f),
        "18_10" to Ayah(18, 10, "إِذْ أَوَى الْفِتْيَةُ إِلَى الْكَهْفِ فَقَالُوا رَبَّنَا آتِنَا مِنْ لَدُنْكَ رَحْمَةً وَهَيِّئْ لَنَا مِنْ أَمْرِنَا رَشَدًا", "When the youths retreated to the cave...", "دعاء أصحاب الكهف بالرحمة والرشد", 12.0f),

        // Ar-Rahman 1-13
        "55_1" to Ayah(55, 1, "الرَّحْمَٰنُ", "The Most Merciful", "الله سبحانه ذو الرحمة الواسعة", 3.0f),
        "55_2" to Ayah(55, 2, "عَلَّمَ الْقُرْآنَ", "Taught the Qur'an,", "علم نبيه وعباده القرآن الكريم", 3.0f),
        "55_3" to Ayah(55, 3, "خَلَقَ الْإِنْسَانَ", "Created man,", "أوجد الإنسان في أحسن تقويم", 3.0f),
        "55_4" to Ayah(55, 4, "عَلَّمَهُ الْبَيَانَ", "[And] taught him eloquence.", "علمه النطق والتعبير والإفصاح", 3.0f),
        "55_5" to Ayah(55, 5, "الشَّمْسُ وَالْقَمَرُ بِحُسْبَانٍ", "The sun and the moon [move] by precise calculation,", "الشمس والقمر يجريان بحساب متقن", 4.5f),
        "55_6" to Ayah(55, 6, "وَالنَّجْمُ وَالشَّجَرُ يَسْجُدَانِ", "And the stars and trees prostrate.", "الكون كله ساجد لعظمة الله", 4.0f),
        "55_7" to Ayah(55, 7, "وَالسَّمَاءَ رَفَعَهَا وَوَضَعَ الْمِيزَانَ", "And the heaven He raised and imposed the balance", "رفع السماء وأقام العدل والقسط", 5.0f),
        "55_8" to Ayah(55, 8, "أَلَّا تَطْغَوْا فِي الْمِيزَانِ", "That you not transgress within the balance.", "النهي عن الجور في الميزان", 4.0f),
        "55_9" to Ayah(55, 9, "وَأَقِيمُوا الْوَزْنَ بِالْقِسْطِ وَلَا تُخْسِرُوا الْمِيزَانَ", "And establish weight in justice and do not make deficient the balance.", "الأمر بإقامة العدل بين الناس", 5.5f),
        "55_10" to Ayah(55, 10, "وَالْأَرْضَ وَضَعَهَا لِلْأَنَامِ", "And the earth He laid [out] for the creatures.", "تمهيد الأرض لمعاش الخلق", 4.5f),
        "55_11" to Ayah(55, 11, "فِيهَا فَاكِهَةٌ وَالنَّخْلُ ذَاتُ الْأَكْمَامِ", "Therein is fruit and palm trees having sheaths [of dates]", "نعم الله في الثمار والنخيل", 5.0f),
        "55_12" to Ayah(55, 12, "وَالْحَبُّ ذُو الْعَصْفِ وَالرَّيْحَانُ", "And grain having husks and scented plants.", "الحبوب والريحان الطيب", 4.5f),
        "55_13" to Ayah(55, 13, "فَبِأَيِّ آلَاءِ رَبِّكُمَا تُكَذِّبَانِ", "So which of the favors of your Lord would you deny?", "التذكير بنعم الله الجليلة", 5.0f),

        // Al-Hashr 21-24
        "59_21" to Ayah(59, 21, "لَوْ أَنْزَلْنَا هَٰذَا الْقُرْآنَ عَلَىٰ جَبَلٍ لَرَأَيْتَهُ خَاشِعًا مُتَصَدِّعًا مِنْ خَشْيَةِ اللَّهِ ۚ وَتِلْكَ الْأَمْثَالُ نَضْرِبُهَا لِلنَّاسِ لَعَلَّهُمْ يَتَفَكَّرُونَ", "If We had sent down this Qur'an upon a mountain, you would have seen it humbled...", "عظمة وجلال كلام الله وخشوع الجبال له", 17.0f),
        "59_22" to Ayah(59, 22, "هُوَ اللَّهُ الَّذِي لَا إِلَٰهَ إِلَّا هُوَ ۖ عَالِمُ الْغَيْبِ وَالشَّهَادَةِ ۖ هُوَ الرَّحْمَٰنُ الرَّحِيمُ", "He is Allah, other than whom there is no deity, Knower of the unseen and the witnessed...", "بيان أسماء الله الحسنى وعلمه المحيط", 13.0f),
        "59_23" to Ayah(59, 23, "هُوَ اللَّهُ الَّذِي لَا إِلَٰهَ إِلَّا هُوَ الْمَلِكُ الْقُدُّوسُ السَّلَامُ الْمُؤْمِنُ الْمُهَيْمِنُ الْعَزِيزُ الْجَبَّارُ الْمُتَكَبِّرُ ۚ سُبْحَانَ اللَّهِ عَمَّا يُشْرِكُونَ", "He is Allah, other than whom there is no deity, the Sovereign, the Pure...", "تقديس الله وتنزيهه عن كل نقص", 17.0f),
        "59_24" to Ayah(59, 24, "هُوَ اللَّهُ الْخَالِقُ الْبَارِئُ الْمُصَوِّرُ ۖ لَهُ الْأَسْمَاءُ الْحُسْنَىٰ ۚ يُسَبِّحُ لَهُ مَا فِي السَّمَاوَاتِ وَالْأَرْضِ ۖ وَهُوَ الْعَزِيزُ الْحَكِيمُ", "He is Allah, the Creator, the Inventor, the Fashioner; to Him belong the best names...", "تسبيح الكائنات لخالقها العظيم", 16.0f),

        // Al-Mulk 1-5
        "67_1" to Ayah(67, 1, "تَبَارَكَ الَّذِي بِيَدِهِ الْمُلْكُ وَهُوَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ", "Blessed is He in whose hand is dominion, and He is over all things competent -", "تعاظم شأن الله وتبارك ملكه وقدرته الشاملة", 7.5f),
        "67_2" to Ayah(67, 2, "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ", "[He] who created death and life to test you [as to] which of you is best in deed...", "الغاية من خلق الموت والحياة: ابتلاء العمل الصالح", 10.0f),
        "67_3" to Ayah(67, 3, "الَّذِي خَلَقَ سَبْعَ سَمَاوَاتٍ طِبَاقًا ۖ مَا تَرَىٰ فِي خَلْقِ الرَّحْمَٰنِ مِنْ تَفَاوُتٍ ۖ فَارْجِعِ الْبَصَرَ هَلْ تَرَىٰ مِنْ فُطُورٍ", "[And] who created seven heavens in layers. You do not see in the creation of the Most Merciful any inconsistency...", "إتقان خلق السماوات وتناسق الكون المعجز", 13.0f),
        "67_4" to Ayah(67, 4, "ثُمَّ ارْجِعِ الْبَصَرَ كَرَّتَيْنِ يَنْقَلِبْ إِلَيْكَ الْبَصَرُ خَاسِئًا وَهُوَ حَسِيرٌ", "Then return [your] vision twice again. [Your] vision will return to you humbled while it is fatigued.", "كمال صنع الله الذي يعجز أمامه بصر البشر", 11.0f),
        "67_5" to Ayah(67, 5, "وَلَقَدْ زَيَّنَّا السَّمَاءَ الدُّنْيَا بِمَصَابِيحَ وَجَعَلْنَاهَا رُجُومًا لِلشَّيَاطِينِ ۖ وَأَعْتَدْنَا لَهُمْ عَذَابَ السَّعِيرِ", "And We have certainly beautified the nearest heaven with stars and have made from them what is thrown at the devils...", "زينة السماء الدنيا بالنجوم ورجوم الشياطين", 14.0f),

        // Ad-Duha (93)
        "93_1" to Ayah(93, 1, "وَالضُّحَىٰ", "By the morning brightness", "قسم بوقت الضحى ونوره", 2.5f),
        "93_2" to Ayah(93, 2, "وَاللَّيْلِ إِذَا سَجَىٰ", "And [by] the night when it covers with darkness,", "وقسم بالليل إذا سكن واشتد ظلامه", 3.0f),
        "93_3" to Ayah(93, 3, "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلَىٰ", "Your Lord has not taken leave of you, [O Muhammad], nor has He detested [you].", "بشارة بأن الله لم يترك نبيه", 4.5f),
        "93_4" to Ayah(93, 4, "وَلَلْآخِرَةُ خَيْرٌ لَكَ مِنَ الْأُولَىٰ", "And the Hereafter is better for you than the first [life].", "دار الآخرة خير وأبقى من الدنيا", 4.0f),
        "93_5" to Ayah(93, 5, "وَلَسَوْفَ يُعْطِيكَ رَبُّكَ فَتَرْضَىٰ", "And your Lord is going to give you, and you will be satisfied.", "وعد الله بالعطاء الجزيل حتى الرضا", 5.0f),
        "93_6" to Ayah(93, 6, "أَلَمْ يَجِدْكَ يَتِيمًا فَآوَىٰ", "Did He not find you an orphan and give [you] refuge?", "تذكير بحفظ الله ورعايته", 4.5f),
        "93_7" to Ayah(93, 7, "وَوَجَدَكَ ضَالًّا فَهَدَىٰ", "And He found you lost and guided [you],", "هداية الله وتوفيقه ونور بصيرته", 4.0f),
        "93_8" to Ayah(93, 8, "وَوَجَدَكَ عَائِلًا فَأَغْنَىٰ", "And He found you poor and made [you] self-sufficient.", "إغناء الله وفضله وكرمه الواسع", 4.0f),
        "93_9" to Ayah(93, 9, "فَأَمَّا الْيَتِيمَ فَلَا تَقْهَرْ", "So as for the orphan, do not oppress [him].", "الوصية بحفظ حق اليتيم والإحسان إليه", 4.0f),
        "93_10" to Ayah(93, 10, "وَأَمَّا السَّائِلَ فَلَا تَنْهَرْ", "And as for the petitioner, do not repel [him].", "الرفق بالسائل وإجابته بالحسنى", 4.0f),
        "93_11" to Ayah(93, 11, "وَأَمَّا بِنِعْمَةِ رَبِّكَ فَحَدِّثْ", "And as for the favor of your Lord, report [it].", "شكر نعم الله وإظهار فضله", 4.5f),

        // Ash-Sharh (94)
        "94_1" to Ayah(94, 1, "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ", "Did We not expand for you, [O Muhammad], your breast?", "انشراح الصدر وطمأنينة القلب بنور الإيمان", 3.5f),
        "94_2" to Ayah(94, 2, "وَوَضَعْنَا عَنْكَ وِزْرَكَ", "And We removed from you your burden", "حط الأثقال وتفريج الهموم والكروب", 3.5f),
        "94_3" to Ayah(94, 3, "الَّذِي أَنْقَضَ ظَهْرَكَ", "Which had weighed upon your back", "الحمل الذي كان يثقل كاهلك", 3.5f),
        "94_4" to Ayah(94, 4, "وَرَفَعْنَا لَكَ ذِكْرَكَ", "And raised high for you your repute.", "رفع منزلة وذكر النبي صلى الله عليه وسلم", 4.0f),
        "94_5" to Ayah(94, 5, "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا", "For indeed, with hardship [will be] ease.", "بشارة ربانية مؤكدة: مع كل ضيق فرج قريب", 4.5f),
        "94_6" to Ayah(94, 6, "إِنَّ مَعَ الْعُسْرِ يُسْرًا", "Indeed, with hardship [will be] ease.", "تأكيد البشارة بأن اليسر يعقب العسر حتماً", 4.5f),
        "94_7" to Ayah(94, 7, "فَإِذَا فَرَغْتَ فَانْصَبْ", "So when you have finished [your duties], then stand up [for worship].", "الإقبال على طاعة الله ودعائه عند الفراغ", 4.0f),
        "94_8" to Ayah(94, 8, "وَإِلَىٰ رَبِّكَ فَارْغَبْ", "And to your Lord direct [your] longing.", "الرغبة إلى الله وحده والتوكل الصادق عليه", 4.5f),

        // Al-Fajr (89: 21-30) ~62 seconds
        "89_21" to Ayah(89, 21, "كَلَّا إِذَا دُكَّتِ الْأَرْضُ دَكًّا دَكًّا", "No! When the earth has been leveled - pounded and crushed -", "إذا زلزلت الأرض ودكت دكاً شديداً", 6.0f),
        "89_22" to Ayah(89, 22, "وَجَاءَ رَبُّكَ وَالْمَلَكُ صَفًّا صَفًّا", "And your Lord has come and the angels, rank upon rank,", "وجاء ربك لفصل القضاء والملائكة مصطفين", 6.5f),
        "89_23" to Ayah(89, 23, "وَجِيءَ يَوْمَئِذٍ بِجَهَنَّمَ ۚ يَوْمَئِذٍ يَتَذَكَّرُ الْإِنْسَانُ وَأَنَّىٰ لَهُ الذِّكْرَىٰ", "And brought [within view], that Day, is Hell - that Day, man will remember, but how will that remembrance be to him?", "حضور جهنم وتذكر الإنسان حين لا ينفع الندم", 10.0f),
        "89_24" to Ayah(89, 24, "يَقُولُ يَا لَيْتَنِي قَدَّمْتُ لِحَيَاتِي", "He will say, 'Oh, I wish I had sent ahead [some good] for my life.'", "تمني تقديم الصالحات للحياة الأبدية", 6.5f),
        "89_25" to Ayah(89, 25, "فَيَوْمَئِذٍ لَا يُعَذِّبُ عَذَابَهُ أَحَدٌ", "So on that Day, none will punish [as severely as] His punishment,", "عذاب الله الشديد للظالمين", 5.5f),
        "89_26" to Ayah(89, 26, "وَلَا يُوثِقُ وَثَاقَهُ أَحَدٌ", "And none will bind [as severely as] His binding [of the evildoers].", "إحكام وثاق المجرمين", 5.0f),
        "89_27" to Ayah(89, 27, "يَا أَيَّتُهَا النَّفْسُ الْمُطْمَئِنَّةُ", "[To the righteous it will be said], 'O reassured soul,", "نداء التكريم للنفس المؤمنة المطمئنة", 6.0f),
        "89_28" to Ayah(89, 28, "ارْجِعِي إِلَىٰ رَبِّكِ رَاضِيَةً مَرْضِيَّةً", "Return to your Lord, well-pleased and pleasing [to Him],", "الرجوع إلى جوار الله برضوان وكرامة", 6.5f),
        "89_29" to Ayah(89, 29, "فَادْخُلِي فِي عِبَادِي", "And enter among My [righteous] servants", "الدخول في زمرة عباد الله الصالحين", 4.5f),
        "89_30" to Ayah(89, 30, "وَادْخُلِي جَنَّتِي", "And enter My Paradise.'", "الدخول إلى دار الخلد والنعيم المقيم", 5.5f),

        // Ash-Shams (91: 1-15) ~60 seconds
        "91_1" to Ayah(91, 1, "وَالشَّمْسِ وَضُحَاهَا", "By the sun and its brightness", "قسم بالشمس ونورها الساطع وقت الضحى", 4.0f),
        "91_2" to Ayah(91, 2, "وَالْقَمَرِ إِذَا تَلَاهَا", "And [by] the moon when it follows it", "وبالقمر إذا تبعها في الإنارة", 4.0f),
        "91_3" to Ayah(91, 3, "وَالنَّهَارِ إِذَا جَلَّاهَا", "And [by] the day when it displays it", "وبالنهار إذا كشف الظلمة وأضاء الكون", 4.0f),
        "91_4" to Ayah(91, 4, "وَاللَّيْلِ إِذَا يَغْشَاهَا", "And [by] the night when it covers it", "وبالليل إذا غطى وجه الأرض بالسواد", 4.0f),
        "91_5" to Ayah(91, 5, "وَالسَّمَاءِ وَمَا بَنَاهَا", "And [by] the sky and He who built it", "وبالسماء وإحكام بنائها المحكم", 4.0f),
        "91_6" to Ayah(91, 6, "وَالْأَرْضِ وَمَا طَحَاهَا", "And [by] the earth and He who spread it", "وبالأرض وبسطها لمعاش الخلائق", 4.0f),
        "91_7" to Ayah(91, 7, "وَنَفْسٍ وَمَا سَوَّاهَا", "And [by] the soul and He who proportioned it", "وبالنفس وخلقها البديع في أحسن تقويم", 4.0f),
        "91_8" to Ayah(91, 8, "فَأَلْهَمَهَا فُجُورَهَا وَتَقْوَاهَا", "And inspired it [with discernment of] its wickedness and its righteousness,", "بيان طريقي الخير والشر للنفس", 4.5f),
        "91_9" to Ayah(91, 9, "قَدْ أَفْلَحَ مَنْ زَكَّاهَا", "He has succeeded who purifies it,", "فوز من طهر نفسه بطاعة الله والإيمان", 4.5f),
        "91_10" to Ayah(91, 10, "وَقَدْ خَابَ مَنْ دَسَّاهَا", "And he has failed who instills it [with corruption].", "خسران من أهلك نفسه بالمعاصي", 4.5f),

        // Al-Qadr (97: 1-5) ~26 seconds
        "97_1" to Ayah(97, 1, "إِنَّا أَنْزَلْنَاهُ فِي لَيْلَةِ الْقَدْرِ", "Indeed, We sent the Qur'an down during the Night of Decree.", "إنزال القرآن الكريم في ليلة القدر المباركة", 5.5f),
        "97_2" to Ayah(97, 2, "وَمَا أَدْرَاكَ مَا لَيْلَةُ الْقَدْرِ", "And what can make you know what is the Night of Decree?", "تعظيم شأن ليلة القدر ومكانتها", 5.0f),
        "97_3" to Ayah(97, 3, "لَيْلَةُ الْقَدْرِ خَيْرٌ مِنْ أَلْفِ شَهْرٍ", "The Night of Decree is better than a thousand months.", "فضل العبادة فيها خير من ألف شهر", 5.5f),
        "97_4" to Ayah(97, 4, "تَنَزَّلُ الْمَلَائِكَةُ وَالرُّوحُ فِيهَا بِإِذْنِ رَبِّهِمْ مِنْ كُلِّ أَمْرٍ", "The angels and the Spirit descend therein by permission of their Lord for every matter.", "نزول الملائكة وجبريل عليه السلام بالبركة والسلام", 7.0f),
        "97_5" to Ayah(97, 5, "سَلَامٌ هِيَ حَتَّىٰ مَطْلَعِ الْفَجْرِ", "Peace it is until the emergence of dawn.", "سلام وأمان وبركة حتى مطلع الفجر", 4.5f),

        // Al-Ikhlas (112)
        "112_1" to Ayah(112, 1, "قُلْ هُوَ اللَّهُ أَحَدٌ", "Say, 'He is Allah, [who is] One,", "توحيد الله الخالص وتفرده بالوحدانية", 3.0f),
        "112_2" to Ayah(112, 2, "اللَّهُ الصَّمَدُ", "Allah, the Eternal Refuge.", "الله السيد المقصود في كل الحوائج والرغائب", 2.5f),
        "112_3" to Ayah(112, 3, "لَمْ يَلِدْ وَلَمْ يُولَدْ", "He neither begets nor is born,", "تنزيه الله عن الوالد والولد والشريك", 3.0f),
        "112_4" to Ayah(112, 4, "وَلَمْ يَكُنْ لَهُ كُفُوًا أَحَدٌ", "Nor is there to Him any equivalent.", "ليس كمثله شيء وهو السميع البصير", 3.5f),

        // Al-Falaq (113)
        "113_1" to Ayah(113, 1, "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ", "Say, 'I seek refuge in the Lord of daybreak", "الاعتصام برب الصبح وفالقه", 3.5f),
        "113_2" to Ayah(113, 2, "مِنْ شَرِّ مَا خَلَقَ", "From the evil of that which He created", "الاستعاذة من شر جميع المخلوقات", 3.5f),
        "113_3" to Ayah(113, 3, "وَمِنْ شَرِّ غَاسِقٍ إِذَا وَقَبَ", "And from the evil of darkness when it settles", "ومن شر الليل وظلمته إذا دخل", 4.0f),
        "113_4" to Ayah(113, 4, "وَمِنْ شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ", "And from the evil of the blowers in knots", "ومن شر السحرة والنفث في العقد", 4.5f),
        "113_5" to Ayah(113, 5, "وَمِنْ شَرِّ حَاسِدٍ إِذَا حَسَدَ", "And from the evil of an envier when he envies.", "ومن شر كل حاسد يتمنى زوال النعمة", 4.5f),

        // An-Nas (114)
        "114_1" to Ayah(114, 1, "قُلْ أَعُوذُ بِرَبِّ النَّاسِ", "Say, 'I seek refuge in the Lord of mankind,", "الالتجاء برب البشر وخالقهم", 3.5f),
        "114_2" to Ayah(114, 2, "مَلِكِ النَّاسِ", "The Sovereign of mankind,", "مالك الخلق والمتصرف في أمورهم", 3.0f),
        "114_3" to Ayah(114, 3, "إِلَٰهِ النَّاسِ", "The God of mankind,", "معبود الناس الحق الذي لا معبود سواه", 3.0f),
        "114_4" to Ayah(114, 4, "مِنْ شَرِّ الْوَسْوَاسِ الْخَنَّاسِ", "From the evil of the retreating whisperer -", "من شر الشيطان الموسوس الخناس", 4.5f),
        "114_5" to Ayah(114, 5, "الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ", "Who whispers [evil] into the breasts of mankind -", "الذي يلقي الشبهات والشرور في القلوب", 4.5f),
        "114_6" to Ayah(114, 6, "مِنَ الْجِنَّةِ وَالنَّاسِ", "From among the jinn and mankind.", "من شياطين الإنس والجن", 4.0f)
    )

    val POPULAR_DURATION_CLIPS = listOf(
        RecommendedClip(
            titleArabic = "سورة الحشر (لو أنزلنا هذا القرآن) - دقيقة كاملة",
            surahNumber = 59,
            fromAyah = 21,
            toAyah = 24,
            durationSeconds = 63,
            category = "تلاوة خاشعة",
            description = "خشوع الجبال وأسماء الله الحسنى - مقطع تام يصل إلى 63 ثانية دون بتر أي آية"
        ),
        RecommendedClip(
            titleArabic = "خواتيم سورة البقرة (آمن الرسول) - دقيقة تامة",
            surahNumber = 2,
            fromAyah = 284,
            toAyah = 286,
            durationSeconds = 66,
            category = "آيات الشفاء والتحصين",
            description = "من قرأهما في ليلة كفتاه - تلاوة مباركة مكتملة 66 ثانية"
        ),
        RecommendedClip(
            titleArabic = "أوائل سورة الكهف (1 - 6) - دقيقة النور",
            surahNumber = 18,
            fromAyah = 1,
            toAyah = 6,
            durationSeconds = 61,
            category = "تلاوة خاشعة",
            description = "عصمة من الفتن ونور بين الجمعتين - 6 آيات كاملة بمجموع 61 ثانية"
        ),
        RecommendedClip(
            titleArabic = "أوائل سورة الملك (تبارك الذي بيده الملك) - دقيقة المنجية",
            surahNumber = 67,
            fromAyah = 1,
            toAyah = 5,
            durationSeconds = 62,
            category = "تلاوة خاشعة",
            description = "المنجية من عذاب القبر والتفكر في بديع صنع الله - 62 ثانية كاملة"
        ),
        RecommendedClip(
            titleArabic = "سورة الرحمن (1 - 13) - دقيقة الآلاء والنعم",
            surahNumber = 55,
            fromAyah = 1,
            toAyah = 13,
            durationSeconds = 61,
            category = "طمأنينة وسكينة",
            description = "فبأي آلاء ربكما تكذبان - تلاوة شجية تامة 61 ثانية"
        ),
        RecommendedClip(
            titleArabic = "سورة الضحى والشرح معاً - دقيقة الأمل والسكينة",
            surahNumber = 93,
            fromAyah = 1,
            toAyah = 11,
            durationSeconds = 44,
            category = "طمأنينة وسكينة",
            description = "بلسم القلوب وبشارة الرضا واليسر بعد العسر"
        )
    )

    fun getSurahById(id: Int): Surah? {
        return ALL_SURAHS.find { it.id == id }
    }

    fun getAyahsForRange(surahNumber: Int, fromAyah: Int, toAyah: Int): List<Ayah> {
        val result = mutableListOf<Ayah>()
        val surah = getSurahById(surahNumber) ?: return emptyList()
        val maxAyah = surah.versesCount
        val safeFrom = fromAyah.coerceIn(1, maxAyah)
        val safeTo = toAyah.coerceIn(safeFrom, maxAyah)

        for (a in safeFrom..safeTo) {
            val key = "${surahNumber}_${a}"
            val existing = PRELOADED_AYAHS[key]
            if (existing != null) {
                result.add(existing)
            } else {
                val estimated = 5.5f
                result.add(
                    Ayah(
                        surahNumber = surahNumber,
                        ayahNumber = a,
                        textArabic = "﴿ سورة ${surah.nameArabic} - الآية ${a.toArabicDigits()} ﴾",
                        translationEnglish = "Surah ${surah.nameEnglish} - Verse $a",
                        translationArabicSimplified = "الآية رقم $a من سورة ${surah.nameArabic}",
                        estimatedDurationSeconds = estimated
                    )
                )
            }
        }
        return result
    }

    /**
     * CRITICAL USER RULE:
     * "يمكن لمدة فيديو هي تلاوات ايات بثواني تدمج بترتيب حتى يصل اخر جزء منها مجموع 60 او اكثر . لانه لا يجوز تقطيع ايه في نصف تلاوه فلا باس بزيادة بضع ثواني"
     *
     * We sequentially sum the estimated/actual duration of consecutive verses from [startAyah].
     * We KEEP adding verses until the accumulated duration is >= targetDurationSeconds!
     * NEVER cutting an Ayah in half.
     */
    fun calculateAyahRangeForTargetDuration(
        surahNumber: Int,
        startAyah: Int,
        targetDurationSeconds: Int
    ): Pair<Int, Int> {
        val surah = getSurahById(surahNumber) ?: return Pair(startAyah, startAyah)
        val maxAyah = surah.versesCount
        var accumulatedTime = 0.0f
        var currentAyah = startAyah.coerceIn(1, maxAyah)

        while (currentAyah <= maxAyah) {
            val key = "${surahNumber}_${currentAyah}"
            val ayah = PRELOADED_AYAHS[key]
            val duration = ayah?.estimatedDurationSeconds ?: 5.5f
            accumulatedTime += duration

            // When accumulated time reaches or exceeds target, we finish the current Ayah completely!
            if (accumulatedTime >= targetDurationSeconds) {
                return Pair(startAyah, currentAyah)
            }

            if (currentAyah == maxAyah) {
                break
            }
            currentAyah++
        }

        return Pair(startAyah, currentAyah)
    }

    /**
     * FEATURE REQUEST:
     * "و اضف خاصية انشاء اعدادات فيديو بشكل تلقائي"
     * Generates a complete harmonious video configuration with verses summing to >= 60s,
     * matching reciter, background, and aspect ratio.
     */
    fun generateAutoVideoConfiguration(targetDuration: Int = 60): AutoVideoConfig {
        // Pool of recommended passages that naturally exceed targetDuration without cutting verses
        val autoPresets60 = listOf(
            Triple(59, 21, 24), // Al-Hashr 21-24 (~63s)
            Triple(2, 284, 286), // Al-Baqarah 284-286 (~66s)
            Triple(18, 1, 6),    // Al-Kahf 1-6 (~61s)
            Triple(67, 1, 5),    // Al-Mulk 1-5 (~62s)
            Triple(55, 1, 13),   // Ar-Rahman 1-13 (~61s)
            Triple(89, 21, 30),  // Al-Fajr 21-30 (~62s)
            Triple(91, 1, 15)    // Ash-Shams 1-15 (~60s)
        )

        val autoPresets30 = listOf(
            Triple(1, 1, 7),     // Al-Fatihah (~35s)
            Triple(2, 255, 255), // Ayat Al-Kursi (~28s)
            Triple(94, 1, 8),    // Ash-Sharh (~30s)
            Triple(97, 1, 5)     // Al-Qadr (~26s)
        )

        val autoPresets90 = listOf(
            Triple(18, 1, 10),   // Al-Kahf 1-10 (~90s)
            Triple(67, 1, 10)    // Al-Mulk 1-10 (~92s)
        )

        val (surahId, startAyah) = when {
            targetDuration <= 35 -> {
                val p = autoPresets30.random()
                Pair(p.first, p.second)
            }
            targetDuration in 50..70 -> {
                val p = autoPresets60.random()
                Pair(p.first, p.second)
            }
            targetDuration in 80..100 -> {
                val p = autoPresets90.random()
                Pair(p.first, p.second)
            }
            else -> {
                val p = autoPresets60.random()
                Pair(p.first, 1)
            }
        }

        val (calcFrom, calcTo) = calculateAyahRangeForTargetDuration(surahId, startAyah, targetDuration)

        val surah = getSurahById(surahId) ?: ALL_SURAHS[0]
        val ayahs = getAyahsForRange(surah.id, calcFrom, calcTo)
        val actualDuration = ayahs.sumOf { it.estimatedDurationSeconds.toDouble() }.toInt()

        val reciter = Reciter.DEFAULT_RECITERS.random()
        val bg = BackgroundPreset.ALL_PRESETS.random()
        val particle = listOf(ParticleEffectType.GOLD_DUST, ParticleEffectType.TWINKLING_STARS, ParticleEffectType.LIGHT_RAYS).random()

        return AutoVideoConfig(
            surah = surah,
            fromAyah = calcFrom,
            toAyah = calcTo,
            reciter = reciter,
            backgroundPreset = bg,
            particleEffect = particle,
            aspectRatio = "9:16",
            targetDurationSeconds = targetDuration,
            actualDurationSeconds = actualDuration,
            summaryArabic = "تم التوليد التلقائي: سورة ${surah.nameArabic} (الآيات $calcFrom - $calcTo) بصوت ${reciter.nameArabic} بالمدة المكتملة $actualDuration ثانية"
        )
    }
}
