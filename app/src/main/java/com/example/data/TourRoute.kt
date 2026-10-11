package com.example.data

data class RouteStop(
    val monumentId: String,
    val order: Int,
    val stopTitleAr: String,
    val stopTitleEn: String = "",
    val stopDurationMinutes: Int,
    val walkingTimeFromPreviousMin: Int,
    val stopHighlightsAr: String,
    val stopHighlightsEn: String = "",
    val latitude: Double,
    val longitude: Double,
    val translations: Map<String, Pair<String, String>> = emptyMap()
) {
    fun stopTitle(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> stopTitleAr
        AppLanguage.ENGLISH -> if (stopTitleEn.isNotEmpty()) stopTitleEn else stopTitleAr
        else -> translations[lang.code]?.first ?: (if (stopTitleEn.isNotEmpty()) stopTitleEn else stopTitleAr)
    }

    fun stopHighlights(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> stopHighlightsAr
        AppLanguage.ENGLISH -> if (stopHighlightsEn.isNotEmpty()) stopHighlightsEn else stopHighlightsAr
        else -> translations[lang.code]?.second ?: (if (stopHighlightsEn.isNotEmpty()) stopHighlightsEn else stopHighlightsAr)
    }
}

data class TourRoute(
    val id: String,
    val titleAr: String,
    val titleEn: String = "",
    val subtitleAr: String,
    val subtitleEn: String = "",
    val regionAr: String,
    val regionEn: String = "",
    val totalDurationHours: Float,
    val totalDistanceKm: Float,
    val difficultyAr: String,
    val difficultyEn: String = "",
    val transportTypeAr: String,
    val transportTypeEn: String = "",
    val estimatedBudgetEgp: Int,
    val descriptionAr: String,
    val descriptionEn: String = "",
    val stops: List<RouteStop>,
    val essentialTipsAr: List<String>,
    val essentialTipsEn: List<String> = emptyList(),
    val polylinePoints: List<Pair<Double, Double>>,
    val translations: Map<String, Map<String, String>> = emptyMap()
) {
    fun title(lang: AppLanguage): String {
        if (lang == AppLanguage.ARABIC) return titleAr
        MonumentTranslations.getRouteTranslation(id, lang)?.title?.let { return it }
        return translations[lang.code]?.get("title") ?: (if (titleEn.isNotEmpty()) titleEn else titleAr)
    }

    fun subtitle(lang: AppLanguage): String {
        if (lang == AppLanguage.ARABIC) return subtitleAr
        MonumentTranslations.getRouteTranslation(id, lang)?.subtitle?.let { return it }
        return translations[lang.code]?.get("subtitle") ?: (if (subtitleEn.isNotEmpty()) subtitleEn else subtitleAr)
    }

    fun region(lang: AppLanguage): String {
        if (lang == AppLanguage.ARABIC) return regionAr
        MonumentTranslations.getRouteTranslation(id, lang)?.region?.let { return it }
        return translations[lang.code]?.get("region") ?: (if (regionEn.isNotEmpty()) regionEn else regionAr)
    }

    fun difficulty(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> difficultyAr
        AppLanguage.ENGLISH -> if (difficultyEn.isNotEmpty()) difficultyEn else "Moderate"
        AppLanguage.FRENCH -> "Moyen"
        AppLanguage.GERMAN -> "Mittel"
        AppLanguage.SPANISH -> "Moderado"
        AppLanguage.ITALIAN -> "Medio"
        AppLanguage.RUSSIAN -> "Средний"
        AppLanguage.CHINESE -> "适中"
        AppLanguage.JAPANESE -> "普通"
        AppLanguage.TURKISH -> "Orta"
        AppLanguage.KOREAN -> "보통"
        AppLanguage.PORTUGUESE -> "Moderado"
        AppLanguage.POLISH -> "Umiarkowany"
        AppLanguage.HINDI -> "मध्यम"
    }

    fun transportType(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> transportTypeAr
        AppLanguage.ENGLISH -> if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.FRENCH -> translations["fr"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.GERMAN -> translations["de"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.SPANISH -> translations["es"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.ITALIAN -> translations["it"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.RUSSIAN -> translations["ru"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.CHINESE -> translations["zh"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.JAPANESE -> translations["ja"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.TURKISH -> translations["tr"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.KOREAN -> translations["ko"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.PORTUGUESE -> translations["pt"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.POLISH -> translations["pl"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
        AppLanguage.HINDI -> translations["hi"]?.get("transport") ?: if (transportTypeEn.isNotEmpty()) transportTypeEn else transportTypeAr
    }

    fun description(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> descriptionAr
        AppLanguage.ENGLISH -> if (descriptionEn.isNotEmpty()) descriptionEn else descriptionAr
        else -> translations[lang.code]?.get("desc") ?: (if (descriptionEn.isNotEmpty()) descriptionEn else descriptionAr)
    }

    fun essentialTips(lang: AppLanguage) = when (lang) {
        AppLanguage.ARABIC -> essentialTipsAr
        else -> if (essentialTipsEn.isNotEmpty()) essentialTipsEn else essentialTipsAr
    }
}
