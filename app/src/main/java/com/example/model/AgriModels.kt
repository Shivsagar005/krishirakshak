package com.example.model

enum class UserRole(val labelEn: String, val labelHi: String) {
    FARMER("Farmer (किसान)", "किसान (Farmer)"),
    EXPERT("Agri Expert (विशेषज्ञ)", "कृषि विशेषज्ञ (Expert)"),
    GOVERNMENT("Govt Command (सरकार)", "सरकारी डैशबोर्ड (Govt)")
}

enum class AppLanguage {
    ENGLISH,
    HINDI
}

enum class RiskLevel(val labelEn: String, val labelHi: String) {
    LOW("Low Risk", "कम जोखिम"),
    MODERATE("Moderate Risk", "मध्यम जोखिम"),
    HIGH("High Risk", "उच्च जोखिम"),
    CRITICAL("Critical Risk", "अति गंभीर जोखिम")
}

enum class SeverityLevel(val labelEn: String, val labelHi: String) {
    MILD("Mild", "हल्का"),
    MODERATE("Moderate", "मध्यम"),
    SEVERE("Severe", "गंभीर"),
    CRITICAL("Critical", "अत्यंत गंभीर")
}

enum class CropStage(val labelEn: String, val labelHi: String) {
    SOWING("Sowing", "बुवाई"),
    GERMINATION("Germination", "अंकुरण"),
    SEEDLING("Seedling", "पौध"),
    VEGETATIVE("Vegetative", "वानस्पतिक"),
    FLOWERING("Flowering", "फूल आना"),
    FRUITING("Fruiting", "फल लगना"),
    MATURITY("Maturity", "परिपक्वता"),
    HARVEST("Harvest", "कटाई")
}

enum class ScanCategory {
    DISEASE,
    PEST,
    NUTRIENT_DEFICIENCY,
    HEALTHY
}

enum class ScanStatus(val labelEn: String, val labelHi: String) {
    AI_PRELIMINARY("AI Preliminary", "एआई प्रारंभिक"),
    PENDING_EXPERT("Pending Expert Review", "विशेषज्ञ समीक्षा लंबित"),
    EXPERT_VERIFIED("Expert Verified", "विशेषज्ञ द्वारा सत्यापित"),
    REJECTED("Needs Retake", "पुनः फोटो आवश्यक")
}

data class Farm(
    val id: String,
    val name: String,
    val village: String,
    val district: String,
    val state: String,
    val areaAcres: Double,
    val soilType: String,
    val irrigationType: String,
    val lat: Double,
    val lng: Double,
    val cropsCount: Int = 1
)

data class CropInstance(
    val id: String,
    val farmId: String,
    val cropName: String,
    val localHindiName: String,
    val variety: String,
    val stage: CropStage,
    val sowingDate: String,
    val healthStatus: String,
    val riskScore: Int
)

data class RiskFactorContribution(
    val factorName: String,
    val factorNameHindi: String,
    val valueText: String,
    val contributionPct: Int,
    val isWarning: Boolean
)

data class IpmAdvisory(
    val immediateAction: String,
    val culturalControl: String,
    val biologicalControl: String,
    val chemicalCaution: String,
    val monitoringIntervalHours: Int = 24
)

data class ScanResult(
    val id: String,
    val cropName: String,
    val cropHindiName: String,
    val detectedIssue: String,
    val issueHindi: String,
    val scientificName: String,
    val category: ScanCategory,
    val confidence: Float,
    val severity: SeverityLevel,
    val riskScore: Int,
    val riskLevel: RiskLevel,
    var status: ScanStatus,
    val dateFormatted: String,
    val symptoms: List<String>,
    val contributingFactors: List<RiskFactorContribution>,
    val ipmAdvisory: IpmAdvisory,
    var expertNotes: String? = null,
    var verifiedBy: String? = null,
    val farmName: String,
    val locationName: String,
    val sampleImageKey: String = "tomato_early_blight"
)

data class EarlyWarningAlert(
    val id: String,
    val title: String,
    val titleHindi: String,
    val crop: String,
    val message: String,
    val messageHindi: String,
    val priority: RiskLevel,
    val recommendedAction: String,
    val recommendedActionHindi: String,
    val timestamp: String
)

data class HotspotCluster(
    val id: String,
    val regionDistrict: String,
    val state: String,
    val crop: String,
    val threat: String,
    val riskLevel: RiskLevel,
    val caseCount: Int,
    val verifiedRatioPct: Int,
    val lat: Double,
    val lng: Double,
    val operationalAction: String,
    val outbreakTrend: String
)

data class WeatherSnapshot(
    val tempCelsius: Int = 31,
    val feelsLikeCelsius: Int = 35,
    val condition: String = "Humid & Overcast",
    val conditionHindi: String = "आर्द्र और बादलों वाला",
    val humidityPct: Int = 84,
    val rainProbPct: Int = 65,
    val windKmH: Int = 14,
    val leafWetnessHours: Int = 6,
    val isFungalRiskElevated: Boolean = true
)

data class IotTelemetry(
    val soilMoisturePct: Int = 42,
    val leafWetnessPct: Int = 82,
    val soilPh: Float = 6.7f,
    val electricalConductivity: Float = 1.4f,
    val whiteflyTrapCount: Int = 23,
    val trapThresholdLimit: Int = 15,
    val lastSyncTime: String = "12 mins ago"
)

data class ChatMessage(
    val id: String,
    val sender: String,
    val text: String,
    val timestamp: String,
    val isFromAi: Boolean
)
