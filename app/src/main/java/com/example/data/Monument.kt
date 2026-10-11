package com.example.data

import java.util.Locale

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val nativeName: String,
    val flag: String,
    val locale: Locale,
    val isRtl: Boolean = false
) {
    ARABIC("ar", "العربية", "العربية", "🇪🇬", Locale("ar", "EG"), true),
    ENGLISH("en", "English", "English", "🇬🇧", Locale.ENGLISH, false),
    FRENCH("fr", "Français", "Français", "🇫🇷", Locale.FRENCH, false),
    GERMAN("de", "Deutsch", "Deutsch", "🇩🇪", Locale.GERMAN, false),
    SPANISH("es", "Español", "Español", "🇪🇸", Locale("es", "ES"), false),
    ITALIAN("it", "Italiano", "Italiano", "🇮🇹", Locale.ITALIAN, false),
    RUSSIAN("ru", "Русский", "Русский", "🇷🇺", Locale("ru", "RU"), false),
    CHINESE("zh", "Chinese", "中文", "🇨🇳", Locale.SIMPLIFIED_CHINESE, false),
    JAPANESE("ja", "Japanese", "日本語", "🇯🇵", Locale.JAPANESE, false),
    TURKISH("tr", "Turkish", "Türkçe", "🇹🇷", Locale("tr", "TR"), false),
    KOREAN("ko", "Korean", "한국어", "🇰🇷", Locale.KOREAN, false),
    PORTUGUESE("pt", "Portuguese", "Português", "🇧🇷", Locale("pt", "BR"), false),
    POLISH("pl", "Polish", "Polski", "🇵🇱", Locale("pl", "PL"), false),
    HINDI("hi", "Hindi", "हिन्दी", "🇮🇳", Locale("hi", "IN"), false);
}

enum class HistoricalEraCategory(
    val key: String,
    val periodSpanAr: String,
    val periodSpanEn: String,
    val iconGlyph: String,
    val approximateStartYear: Int
) {
    ALL("all", "3100 ق.م - العصر الحديث", "3100 BC - Present", "🌐", -3100),
    PHARAONIC("pharaonic", "3100 ق.م - 332 ق.م", "3100 BC - 332 BC", "🏛️", -3100),
    GRECO_ROMAN("greco_roman", "332 ق.م - 395 م", "332 BC - 395 AD", "🏛️", -332),
    COPTIC("coptic", "45 م - 641 م", "45 AD - 641 AD", "⛪", 45),
    ISLAMIC("islamic", "641 م - 1805 م", "641 AD - 1805 AD", "🕌", 641),
    MODERN("modern", "1805 م - العصر الحالي", "1805 AD - Present", "🏰", 1805);

    fun title(lang: AppLanguage): String = when (this) {
        ALL -> AppStrings.eraAll(lang)
        PHARAONIC -> AppStrings.eraPharaonic(lang)
        GRECO_ROMAN -> AppStrings.eraGrecoRoman(lang)
        COPTIC -> AppStrings.eraCoptic(lang)
        ISLAMIC -> AppStrings.eraIslamic(lang)
        MODERN -> AppStrings.eraModern(lang)
    }

    fun periodSpan(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> periodSpanAr
        else -> periodSpanEn
    }

    companion object {
        fun fromKey(key: String): HistoricalEraCategory {
            return values().firstOrNull { it.key.equals(key, ignoreCase = true) } ?: ALL
        }
    }
}

data class Monument(
    val id: String,
    val nameAr: String,
    val nameEn: String,
    val subtitleAr: String,
    val subtitleEn: String = "",
    val eraAr: String,
    val eraEn: String = "",
    val cityAr: String,
    val cityEn: String = "",
    val latitude: Double,
    val longitude: Double,
    val rating: Float,
    val durationMin: Int,
    val ticketPriceEgpAdult: Int,
    val ticketPriceEgpStudent: Int,
    val ticketPriceForeignerAdult: Int,
    val openingHoursAr: String,
    val openingHoursEn: String = "",
    val shortDescAr: String,
    val shortDescEn: String = "",
    val detailedHistoryAr: String,
    val detailedHistoryEn: String = "",
    val highlightsAr: List<String>,
    val highlightsEn: List<String> = emptyList(),
    val visitorTipsAr: List<String>,
    val visitorTipsEn: List<String> = emptyList(),
    val audioDurationSeconds: Int,
    val audioScriptAr: String,
    val audioScriptEn: String = "",
    val drawableResId: Int? = null,
    val arArtifactId: String? = null,
    val funFactAr: String = "",
    val funFactEn: String = "",
    val eraCategory: String = "pharaonic",
    val approximateYearBce: Int = -2500,
    val yearDisplayAr: String = "",
    val yearDisplayEn: String = "",
    // Multi-language custom overrides if needed
    val translations: Map<String, Map<String, String>> = emptyMap()
) {
    fun name(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> nameAr
            AppLanguage.ENGLISH -> if (nameEn.isNotEmpty()) nameEn else nameAr
            else -> MonumentTranslations.getMonumentTranslation(id, lang)?.name
                ?: translations[lang.code]?.get("name")
                ?: (if (nameEn.isNotEmpty()) nameEn else nameAr)
        }
    }

    fun subtitle(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> subtitleAr
            AppLanguage.ENGLISH -> if (subtitleEn.isNotEmpty()) subtitleEn else subtitleAr
            else -> MonumentTranslations.getMonumentTranslation(id, lang)?.subtitle
                ?: translations[lang.code]?.get("subtitle")
                ?: (if (subtitleEn.isNotEmpty()) subtitleEn else subtitleAr)
        }
    }

    fun era(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> eraAr
            AppLanguage.ENGLISH -> if (eraEn.isNotEmpty()) eraEn else eraAr
            else -> MonumentTranslations.getMonumentTranslation(id, lang)?.era
                ?: translations[lang.code]?.get("era")
                ?: (if (eraEn.isNotEmpty()) eraEn else eraAr)
        }
    }

    fun city(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> cityAr
            AppLanguage.ENGLISH -> if (cityEn.isNotEmpty()) cityEn else cityAr
            else -> MonumentTranslations.getMonumentTranslation(id, lang)?.city
                ?: translations[lang.code]?.get("city")
                ?: (if (cityEn.isNotEmpty()) cityEn else cityAr)
        }
    }

    fun openingHours(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> openingHoursAr
            else -> if (openingHoursEn.isNotEmpty()) openingHoursEn else openingHoursAr
        }
    }

    fun shortDesc(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> shortDescAr
            AppLanguage.ENGLISH -> if (shortDescEn.isNotEmpty()) shortDescEn else shortDescAr
            else -> MonumentTranslations.getMonumentTranslation(id, lang)?.shortDesc
                ?: translations[lang.code]?.get("shortDesc")
                ?: (if (shortDescEn.isNotEmpty()) shortDescEn else shortDescAr)
        }
    }

    fun detailedHistory(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> detailedHistoryAr
            AppLanguage.ENGLISH -> if (detailedHistoryEn.isNotEmpty()) detailedHistoryEn else detailedHistoryAr
            else -> MonumentTranslations.getMonumentTranslation(id, lang)?.detailedHistory
                ?: translations[lang.code]?.get("detailedHistory")
                ?: (if (detailedHistoryEn.isNotEmpty()) detailedHistoryEn else detailedHistoryAr)
        }
    }

    fun highlights(lang: AppLanguage): List<String> {
        return when (lang) {
            AppLanguage.ARABIC -> highlightsAr
            AppLanguage.ENGLISH -> if (highlightsEn.isNotEmpty()) highlightsEn else highlightsAr
            else -> if (highlightsEn.isNotEmpty()) highlightsEn else highlightsAr
        }
    }

    fun visitorTips(lang: AppLanguage): List<String> {
        return when (lang) {
            AppLanguage.ARABIC -> visitorTipsAr
            AppLanguage.ENGLISH -> if (visitorTipsEn.isNotEmpty()) visitorTipsEn else visitorTipsAr
            else -> if (visitorTipsEn.isNotEmpty()) visitorTipsEn else visitorTipsAr
        }
    }

    fun audioScript(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> audioScriptAr
            AppLanguage.ENGLISH -> if (audioScriptEn.isNotEmpty()) audioScriptEn else audioScriptAr
            else -> MonumentTranslations.getMonumentTranslation(id, lang)?.audioScript
                ?: translations[lang.code]?.get("audioScript")
                ?: (if (audioScriptEn.isNotEmpty()) audioScriptEn else audioScriptAr)
        }
    }

    fun funFact(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> funFactAr
            AppLanguage.ENGLISH -> if (funFactEn.isNotEmpty()) funFactEn else funFactAr
            else -> MonumentTranslations.getMonumentTranslation(id, lang)?.funFact
                ?: translations[lang.code]?.get("funFact")
                ?: (if (funFactEn.isNotEmpty()) funFactEn else funFactAr)
        }
    }

    fun yearDisplay(lang: AppLanguage): String {
        return when (lang) {
            AppLanguage.ARABIC -> if (yearDisplayAr.isNotEmpty()) yearDisplayAr else {
                if (approximateYearBce < 0) "${-approximateYearBce} ق.م" else "$approximateYearBce م"
            }
            else -> if (yearDisplayEn.isNotEmpty()) yearDisplayEn else {
                if (approximateYearBce < 0) "${-approximateYearBce} BC" else "$approximateYearBce AD"
            }
        }
    }

    fun eraCategoryEnum(): HistoricalEraCategory = HistoricalEraCategory.fromKey(eraCategory)
}

data class ArHotspot(
    val id: String,
    val titleAr: String,
    val titleEn: String = "",
    val descriptionAr: String,
    val descriptionEn: String = "",
    val xFraction: Float,
    val yFraction: Float,
    val translations: Map<String, Pair<String, String>> = emptyMap() // code -> (title, desc)
) {
    fun title(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> titleAr
        AppLanguage.ENGLISH -> if (titleEn.isNotEmpty()) titleEn else titleAr
        else -> translations[lang.code]?.first ?: (if (titleEn.isNotEmpty()) titleEn else titleAr)
    }

    fun description(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> descriptionAr
        AppLanguage.ENGLISH -> if (descriptionEn.isNotEmpty()) descriptionEn else descriptionAr
        else -> translations[lang.code]?.second ?: (if (descriptionEn.isNotEmpty()) descriptionEn else descriptionAr)
    }
}

data class ArArtifact(
    val id: String,
    val titleAr: String,
    val titleEn: String = "",
    val originalYearAr: String,
    val originalYearEn: String = "",
    val descriptionAr: String,
    val descriptionEn: String = "",
    val reconstructedViewName: String,
    val reconstructedViewNameEn: String = "",
    val originalColorViewName: String,
    val originalColorViewNameEn: String = "",
    val architecturalViewName: String,
    val architecturalViewNameEn: String = "",
    val hotspots: List<ArHotspot>,
    val audioStoryAr: String,
    val audioStoryEn: String = "",
    val translations: Map<String, Map<String, String>> = emptyMap()
) {
    fun title(lang: AppLanguage): String {
        if (lang == AppLanguage.ARABIC) return titleAr
        MonumentTranslations.getArtifactTranslation(id, lang)?.title?.let { return it }
        return translations[lang.code]?.get("title") ?: (if (titleEn.isNotEmpty()) titleEn else titleAr)
    }

    fun originalYear(lang: AppLanguage): String {
        if (lang == AppLanguage.ARABIC) return originalYearAr
        MonumentTranslations.getArtifactTranslation(id, lang)?.originalYear?.let { return it }
        return translations[lang.code]?.get("year") ?: (if (originalYearEn.isNotEmpty()) originalYearEn else originalYearAr)
    }

    fun description(lang: AppLanguage): String {
        if (lang == AppLanguage.ARABIC) return descriptionAr
        MonumentTranslations.getArtifactTranslation(id, lang)?.description?.let { return it }
        return translations[lang.code]?.get("desc") ?: (if (descriptionEn.isNotEmpty()) descriptionEn else descriptionAr)
    }

    fun reconstructedViewName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> reconstructedViewName
        else -> if (reconstructedViewNameEn.isNotEmpty()) reconstructedViewNameEn else reconstructedViewName
    }

    fun originalColorViewName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> originalColorViewName
        else -> if (originalColorViewNameEn.isNotEmpty()) originalColorViewNameEn else originalColorViewName
    }

    fun architecturalViewName(lang: AppLanguage): String = when (lang) {
        AppLanguage.ARABIC -> architecturalViewName
        else -> if (architecturalViewNameEn.isNotEmpty()) architecturalViewNameEn else architecturalViewName
    }

    fun audioStory(lang: AppLanguage): String {
        if (lang == AppLanguage.ARABIC) return audioStoryAr
        MonumentTranslations.getArtifactTranslation(id, lang)?.audioStory?.let { return it }
        return translations[lang.code]?.get("audioStory") ?: (if (audioStoryEn.isNotEmpty()) audioStoryEn else audioStoryAr)
    }
}
