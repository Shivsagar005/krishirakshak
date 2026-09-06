package com.example.data

import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

object MockAndDemoRepository {

    private val _currentRole = MutableStateFlow(UserRole.FARMER)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentLanguage = MutableStateFlow(AppLanguage.ENGLISH)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _weather = MutableStateFlow(
        WeatherSnapshot(
            tempCelsius = 31,
            feelsLikeCelsius = 36,
            condition = "Warm & Humid with Rain Risk",
            conditionHindi = "उष्ण और आर्द्र, बारिश की संभावना",
            humidityPct = 84,
            rainProbPct = 68,
            windKmH = 12,
            leafWetnessHours = 6,
            isFungalRiskElevated = true
        )
    )
    val weather: StateFlow<WeatherSnapshot> = _weather.asStateFlow()

    private val _farms = MutableStateFlow(
        listOf(
            Farm(
                id = "farm-1",
                name = "Surya Agro Farm - Plot #1",
                village = "Bakshi Ka Talab",
                district = "Lucknow",
                state = "Uttar Pradesh",
                areaAcres = 3.5,
                soilType = "Alluvial Loam (दोमट)",
                irrigationType = "Drip & Tube-well",
                lat = 26.9654,
                lng = 80.9341,
                cropsCount = 2
            ),
            Farm(
                id = "farm-2",
                name = "Ganga Basin Green Acre",
                village = "Fatehpur",
                district = "Barabanki",
                state = "Uttar Pradesh",
                areaAcres = 5.0,
                soilType = "Sandy Clay Loam",
                irrigationType = "Canal Irrigated",
                lat = 27.0142,
                lng = 81.2589,
                cropsCount = 1
            )
        )
    )
    val farms: StateFlow<List<Farm>> = _farms.asStateFlow()

    private val _crops = MutableStateFlow(
        listOf(
            CropInstance(
                id = "crop-1",
                farmId = "farm-1",
                cropName = "Tomato",
                localHindiName = "टमाटर",
                variety = "Pusa Ruby Hybrid",
                stage = CropStage.FLOWERING,
                sowingDate = "15 July 2026",
                healthStatus = "High Disease Risk (Blight Susceptible)",
                riskScore = 78
            ),
            CropInstance(
                id = "crop-2",
                farmId = "farm-1",
                cropName = "Rice",
                localHindiName = "धान",
                variety = "Basmati PB-1121",
                stage = CropStage.VEGETATIVE,
                sowingDate = "28 June 2026",
                healthStatus = "Good Health (Watch Blast)",
                riskScore = 32
            ),
            CropInstance(
                id = "crop-3",
                farmId = "farm-2",
                cropName = "Cotton",
                localHindiName = "कपास",
                variety = "Bt Cotton RCH-659",
                stage = CropStage.FRUITING,
                sowingDate = "10 May 2026",
                healthStatus = "Pest Alert (Whitefly / Bollworm)",
                riskScore = 64
            )
        )
    )
    val crops: StateFlow<List<CropInstance>> = _crops.asStateFlow()

    private val _scans = MutableStateFlow(
        listOf(
            ScanResult(
                id = "scan-101",
                cropName = "Tomato",
                cropHindiName = "टमाटर",
                detectedIssue = "Early Blight",
                issueHindi = "अगेती झुलसा रोग (अर्ली ब्लाइट)",
                scientificName = "Alternaria solani",
                category = ScanCategory.DISEASE,
                confidence = 0.89f,
                severity = SeverityLevel.MODERATE,
                riskScore = 78,
                riskLevel = RiskLevel.HIGH,
                status = ScanStatus.PENDING_EXPERT,
                dateFormatted = "Today, 07:15 AM",
                symptoms = listOf(
                    "Concentric dark brown rings ('target board' pattern) on lower leaves",
                    "Surrounding chlorotic yellow halos",
                    "Early leaf defoliation on lower stems"
                ),
                contributingFactors = listOf(
                    RiskFactorContribution("High Relative Humidity", "अत्यधिक सापेक्ष आर्द्रता", "84% (Ideal for fungal spore germination)", 30, true),
                    RiskFactorContribution("Leaf Wetness Duration", "पत्तियों के गीले रहने का समय", "6 consecutive hours", 25, true),
                    RiskFactorContribution("Crop Stage Vulnerability", "फसल अवस्था सुभेद्यता", "Flowering Stage (Stress period)", 25, true),
                    RiskFactorContribution("Regional Hotspot Proximity", "निकटवर्ती रोग केंद्र", "Barabanki cluster 18 km away", 20, true)
                ),
                ipmAdvisory = IpmAdvisory(
                    immediateAction = "Prune and safely destroy lower infected leaves. Do not compost infected foliage.",
                    culturalControl = "Avoid overhead sprinkler irrigation; water at base in early morning to minimize canopy wetness.",
                    biologicalControl = "Apply bio-control agent Trichoderma viride / harzianum (5g/L) or Bacillus subtilis foliar spray.",
                    chemicalCaution = "If outbreak intensifies, consult local KVK/agri officer for registered copper oxychloride 50 WP (2.5g/L). Always follow recommended pre-harvest intervals.",
                    monitoringIntervalHours = 24
                ),
                expertNotes = null,
                verifiedBy = null,
                farmName = "Surya Agro Farm - Plot #1",
                locationName = "Lucknow, UP",
                sampleImageKey = "tomato_early_blight"
            ),
            ScanResult(
                id = "scan-102",
                cropName = "Cotton",
                cropHindiName = "कपास",
                detectedIssue = "Whitefly Infestation & Leaf Curl Risk",
                issueHindi = "सफेद मक्खी एवं पर्ण कुंचन (लीफ कर्ल)",
                scientificName = "Bemisia tabaci",
                category = ScanCategory.PEST,
                confidence = 0.84f,
                severity = SeverityLevel.SEVERE,
                riskScore = 82,
                riskLevel = RiskLevel.CRITICAL,
                status = ScanStatus.EXPERT_VERIFIED,
                dateFormatted = "Yesterday, 04:30 PM",
                symptoms = listOf(
                    "High density of nymphs and adults on leaf undersides",
                    "Upward curling and thickening of leaf margins",
                    "Sticky honeydew secretion with black sooty mold"
                ),
                contributingFactors = listOf(
                    RiskFactorContribution("Pest Trap Density", "कीट ट्रैप में संख्या", "23 flies/trap (Threshold is 15)", 35, true),
                    RiskFactorContribution("Elevated Temperature", "अधिक तापमान", "34°C dry spells accelerate breeding", 25, true),
                    RiskFactorContribution("Crop Stage Susceptibility", "फसल अवस्था", "Square & boll development", 25, true),
                    RiskFactorContribution("Vector Potential", "विषाणु प्रसारक क्षमता", "CLCuV transmission risk very high", 15, true)
                ),
                ipmAdvisory = IpmAdvisory(
                    immediateAction = "Install yellow sticky traps (20 per acre) at canopy level to monitor and mass trap adults.",
                    culturalControl = "Maintain weed-free field borders (especially Sida, Xanthium) which serve as alternate hosts.",
                    biologicalControl = "Spray Neem seed kernel extract (NSKE 5%) or Azadirachtin 1500 ppm @ 5 ml/L.",
                    chemicalCaution = "Consult KVK before using selective insecticides. Do not apply synthetic pyrethroids which induce resurgence.",
                    monitoringIntervalHours = 12
                ),
                expertNotes = "Diagnosis confirmed by Dr. Priya Sharma. Field inspection scheduled for Barabanki cluster.",
                verifiedBy = "Dr. Priya Sharma (ICAR-IARI Senior Pathologist)",
                farmName = "Ganga Basin Green Acre",
                locationName = "Barabanki, UP",
                sampleImageKey = "cotton_pest"
            ),
            ScanResult(
                id = "scan-103",
                cropName = "Rice",
                cropHindiName = "धान",
                detectedIssue = "Healthy Foliage",
                issueHindi = "स्वस्थ फसल (कोई रोग नहीं)",
                scientificName = "Oryza sativa",
                category = ScanCategory.HEALTHY,
                confidence = 0.94f,
                severity = SeverityLevel.MILD,
                riskScore = 18,
                riskLevel = RiskLevel.LOW,
                status = ScanStatus.EXPERT_VERIFIED,
                dateFormatted = "3 days ago",
                symptoms = listOf(
                    "Vigorous emerald green turgid blades",
                    "No blast lesions or sheath rot detected",
                    "Uniform tiller development"
                ),
                contributingFactors = listOf(
                    RiskFactorContribution("Foliar Integrity", "पत्तियों का स्वास्थ्य", "Uniform green, no necrosis", 40, false),
                    RiskFactorContribution("Balanced Nutrition", "संतुलित पोषण", "Adequate chlorophyll index", 30, false),
                    RiskFactorContribution("Moderate Weather", "अनुकूल मौसम", "Good wind circulation", 30, false)
                ),
                ipmAdvisory = IpmAdvisory(
                    immediateAction = "Maintain standard split-nitrogen schedule. Avoid excessive urea.",
                    culturalControl = "Maintain 2-3 cm water level; practice alternate wetting and drying (AWD).",
                    biologicalControl = "Encourage beneficial spiders and dragonflies in the bunds.",
                    chemicalCaution = "No chemical application warranted.",
                    monitoringIntervalHours = 48
                ),
                expertNotes = "Healthy crop verified. Recommended continued scouting during upcoming panicle emergence.",
                verifiedBy = "Dr. Priya Sharma",
                farmName = "Surya Agro Farm - Plot #1",
                locationName = "Lucknow, UP",
                sampleImageKey = "rice_healthy"
            )
        )
    )
    val scans: StateFlow<List<ScanResult>> = _scans.asStateFlow()

    private val _alerts = MutableStateFlow(
        listOf(
            EarlyWarningAlert(
                id = "alert-1",
                title = "High Fungal Blight Outbreak Risk",
                titleHindi = "उच्च कवक झुलसा रोग (ब्लाइट) प्रकोप चेतावनी",
                crop = "Tomato (टमाटर)",
                message = "Forecasted intermittent showers + 84% humidity over next 48h creates ideal condition for Alternaria spores. Inspect lower leaves immediately.",
                messageHindi = "आगामी 48 घंटों में हल्की वर्षा व 84% आर्द्रता फफूंद बीजाणुओं के लिए अत्यंत अनुकूल है। तुरंत निचले पत्तों की जांच करें।",
                priority = RiskLevel.HIGH,
                recommendedAction = "Apply preventive Trichoderma foliar spray. Ensure field drainage.",
                recommendedActionHindi = "ट्राइकोडर्मा का निवारक छिड़काव करें। जल निकासी सुनिश्चित करें।",
                timestamp = "10 mins ago"
            ),
            EarlyWarningAlert(
                id = "alert-2",
                title = "Whitefly Vector Hotspot Alert",
                titleHindi = "सफेद मक्खी कीट प्रकोप चेतावनी",
                crop = "Cotton & Vegetables (कपास व सब्जियां)",
                message = "Barabanki district smart traps recorded 23 whiteflies/trap, crossing economic threshold level (ETL).",
                messageHindi = "बाराबंकी जिले के स्मार्ट ट्रैप में सफेद मक्खियों की संख्या 23 दर्ज की गई है, जो आर्थिक सीमा पार कर चुकी है।",
                priority = RiskLevel.CRITICAL,
                recommendedAction = "Deploy yellow sticky traps. Check undersides of leaves.",
                recommendedActionHindi = "पीले चिपचिपे ट्रैप लगाएं। पत्तों की निचली सतह की जांच करें।",
                timestamp = "1 hour ago"
            ),
            EarlyWarningAlert(
                id = "alert-3",
                title = "Optimal Weather Window for Bio-Pesticide",
                titleHindi = "जैव कीटनाशक छिड़काव हेतु अनुकूल समय",
                crop = "All Crops (समस्त फसलें)",
                message = "Wind speeds below 12 km/h and overcast sky this afternoon are ideal for bio-control agent survival.",
                messageHindi = "हवा की गति 12 किमी/घं से कम और छांव वाला मौसम जैव कीटनाशकों के प्रभावी असर के लिए उपयुक्त है।",
                priority = RiskLevel.LOW,
                recommendedAction = "Schedule planned bio-sprays before 04:00 PM.",
                recommendedActionHindi = "अपराह्न 04:00 बजे से पूर्व जैव छिड़काव पूर्ण करें।",
                timestamp = "3 hours ago"
            )
        )
    )
    val alerts: StateFlow<List<EarlyWarningAlert>> = _alerts.asStateFlow()

    private val _hotspots = MutableStateFlow(
        listOf(
            HotspotCluster(
                id = "hotspot-1",
                regionDistrict = "Barabanki",
                state = "Uttar Pradesh",
                crop = "Tomato & Chilli",
                threat = "Early Blight & Gemini Virus Cluster",
                riskLevel = RiskLevel.CRITICAL,
                caseCount = 142,
                verifiedRatioPct = 86,
                lat = 26.9270,
                lng = 81.1834,
                operationalAction = "Dispatch mobile KVK squad. Distribute subsidized bio-fungicide packets.",
                outbreakTrend = "Surging (+31% in 72h)"
            ),
            HotspotCluster(
                id = "hotspot-2",
                regionDistrict = "Sitapur",
                state = "Uttar Pradesh",
                crop = "Rice",
                threat = "Bacterial Leaf Blight (Xanthomonas)",
                riskLevel = RiskLevel.HIGH,
                caseCount = 89,
                verifiedRatioPct = 78,
                lat = 27.5612,
                lng = 80.6829,
                operationalAction = "Advise farmers to drain standing water and reduce excess urea fertilization.",
                outbreakTrend = "Stable Cluster"
            ),
            HotspotCluster(
                id = "hotspot-3",
                regionDistrict = "Raebareli",
                state = "Uttar Pradesh",
                crop = "Potato & Mustard",
                threat = "Aphid Colony Proliferation",
                riskLevel = RiskLevel.MODERATE,
                caseCount = 54,
                verifiedRatioPct = 65,
                lat = 26.2303,
                lng = 81.2409,
                operationalAction = "Monitor smart trap telemetry. Advise neem oil 1500ppm border sprays.",
                outbreakTrend = "Contained"
            ),
            HotspotCluster(
                id = "hotspot-4",
                regionDistrict = "Ludhiana",
                state = "Punjab",
                crop = "Wheat & Maize",
                threat = "Yellow Rust (Puccinia) Early Signs",
                riskLevel = RiskLevel.HIGH,
                caseCount = 97,
                verifiedRatioPct = 91,
                lat = 30.9010,
                lng = 75.8573,
                operationalAction = "Issue state advisory to inspect stripe rust symptoms in sub-mountainous blocks.",
                outbreakTrend = "Early Warning Active"
            ),
            HotspotCluster(
                id = "hotspot-5",
                regionDistrict = "Nashik",
                state = "Maharashtra",
                crop = "Grapes & Onion",
                threat = "Downy Mildew & Purple Blotch",
                riskLevel = RiskLevel.CRITICAL,
                caseCount = 178,
                verifiedRatioPct = 88,
                lat = 19.9975,
                lng = 73.7898,
                operationalAction = "Deploy meteorological micro-climate sensors. Activate grower SMS warning grid.",
                outbreakTrend = "Surging (+18%)"
            )
        )
    )
    val hotspots: StateFlow<List<HotspotCluster>> = _hotspots.asStateFlow()

    private val _iotData = MutableStateFlow(
        IotTelemetry(
            soilMoisturePct = 44,
            leafWetnessPct = 82,
            soilPh = 6.8f,
            electricalConductivity = 1.35f,
            whiteflyTrapCount = 23,
            trapThresholdLimit = 15,
            lastSyncTime = "Synced 8m ago via Solar Gateway"
        )
    )
    val iotData: StateFlow<IotTelemetry> = _iotData.asStateFlow()

    private val _chatMessages = MutableStateFlow(
        listOf(
            ChatMessage(
                id = "1",
                sender = "Krishi AI",
                text = "Namaste Ramesh ji! 🙏 I am Krishi AI, your agricultural intelligence assistant. I am linked to your farm in Lucknow (Weather: 31°C, Humidity 84%). How can I protect your crops today?\n\nनमस्ते रमेश जी! मैं आपका कृषि सहायक हूँ। आज मैं आपकी फसल सुरक्षा में क्या सहायता कर सकता हूँ?",
                timestamp = "08:00 AM",
                isFromAi = true
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    fun setRole(role: UserRole) {
        _currentRole.value = role
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun addFarm(name: String, village: String, district: String, state: String, areaAcres: Double, soilType: String) {
        val newFarm = Farm(
            id = "farm-${UUID.randomUUID().toString().take(6)}",
            name = name,
            village = village,
            district = district,
            state = state,
            areaAcres = areaAcres,
            soilType = soilType,
            irrigationType = "Drip & Canal",
            lat = 26.8467 + (Math.random() - 0.5) * 0.1,
            lng = 80.9462 + (Math.random() - 0.5) * 0.1,
            cropsCount = 1
        )
        _farms.update { listOf(newFarm) + it }
    }

    fun addCrop(farmId: String, cropName: String, hindiName: String, variety: String, stage: CropStage) {
        val newCrop = CropInstance(
            id = "crop-${UUID.randomUUID().toString().take(6)}",
            farmId = farmId,
            cropName = cropName,
            localHindiName = hindiName,
            variety = variety,
            stage = stage,
            sowingDate = "August 2026",
            healthStatus = "Monitoring Active",
            riskScore = 45
        )
        _crops.update { listOf(newCrop) + it }
    }

    fun submitScan(
        cropName: String,
        cropHindiName: String,
        detectedIssue: String,
        issueHindi: String,
        category: ScanCategory,
        confidence: Float,
        severity: SeverityLevel,
        symptoms: List<String>,
        sampleKey: String = "tomato_early_blight"
    ): ScanResult {
        // Multi-factor early warning risk calculation
        val humidity = _weather.value.humidityPct
        val rainProb = _weather.value.rainProbPct
        val isHumid = humidity > 75
        val isRainLikely = rainProb > 50
        val isDisease = category == ScanCategory.DISEASE || category == ScanCategory.PEST

        var calculatedScore = 20
        if (isDisease) calculatedScore += (confidence * 35).toInt()
        if (isHumid) calculatedScore += 22
        if (isRainLikely) calculatedScore += 16
        if (severity == SeverityLevel.SEVERE || severity == SeverityLevel.CRITICAL) calculatedScore += 12
        calculatedScore = calculatedScore.coerceIn(10, 96)

        val riskLevel = when {
            calculatedScore >= 76 -> RiskLevel.CRITICAL
            calculatedScore >= 51 -> RiskLevel.HIGH
            calculatedScore >= 26 -> RiskLevel.MODERATE
            else -> RiskLevel.LOW
        }

        val result = ScanResult(
            id = "scan-${UUID.randomUUID().toString().take(6)}",
            cropName = cropName,
            cropHindiName = cropHindiName,
            detectedIssue = detectedIssue,
            issueHindi = issueHindi,
            scientificName = if (cropName == "Tomato") "Alternaria solani" else "Phytophthora infestans",
            category = category,
            confidence = confidence,
            severity = severity,
            riskScore = calculatedScore,
            riskLevel = riskLevel,
            status = if (confidence < 0.85f) ScanStatus.PENDING_EXPERT else ScanStatus.AI_PRELIMINARY,
            dateFormatted = "Just now",
            symptoms = symptoms,
            contributingFactors = listOf(
                RiskFactorContribution("AI Vision Pattern Match", "एआई विज़न लक्षण पहचान", "${(confidence * 100).toInt()}% match to symptom database", 35, isDisease),
                RiskFactorContribution("Ambient Humidity", "पर्यावरणीय आर्द्रता", "$humidity% (Spore multiplication risk)", 25, isHumid),
                RiskFactorContribution("Rainfall Probability", "वर्षा की संभावना", "$rainProb% precipitation index", 20, isRainLikely),
                RiskFactorContribution("Crop Stage Vulnerability", "फसल अवस्था", "Active Growth / Flowering", 20, true)
            ),
            ipmAdvisory = IpmAdvisory(
                immediateAction = "Inspect surrounding plants for early lesions. Remove severely spotted leaves.",
                culturalControl = "Maintain proper plant spacing (45-60cm) to maximize aeration.",
                biologicalControl = "Foliar spray of Pseudomonas fluorescens or Trichoderma @ 5g per litre of water.",
                chemicalCaution = "Reserve contact fungicides only if disease crosses 10% canopy threshold. Strictly consult KVK.",
                monitoringIntervalHours = 24
            ),
            expertNotes = null,
            verifiedBy = null,
            farmName = _farms.value.firstOrNull()?.name ?: "Surya Agro Farm",
            locationName = "Lucknow, UP",
            sampleImageKey = sampleKey
        )

        _scans.update { listOf(result) + it }
        return result
    }

    fun expertValidateScan(scanId: String, approved: Boolean, notes: String, modifiedDiagnosis: String? = null) {
        _scans.update { list ->
            list.map { scan ->
                if (scan.id == scanId) {
                    scan.copy(
                        status = if (approved) ScanStatus.EXPERT_VERIFIED else ScanStatus.REJECTED,
                        expertNotes = notes,
                        verifiedBy = "Dr. Priya Sharma (ICAR-IARI Extension Officer)",
                        detectedIssue = modifiedDiagnosis ?: scan.detectedIssue
                    )
                } else scan
            }
        }
    }

    fun addChatMessage(text: String, isFromAi: Boolean) {
        val msg = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = if (isFromAi) "Krishi AI" else "Farmer Ramesh",
            text = text,
            timestamp = "Just now",
            isFromAi = isFromAi
        )
        _chatMessages.update { it + msg }
    }
}
