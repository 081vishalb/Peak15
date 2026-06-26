package com.peak15.data.local

import com.peak15.domain.model.*

/**
 * Hardcoded 15-day program data.
 * In production this could be loaded from JSON assets, but keeping offline-first
 * with compile-time data ensures zero network dependency.
 */
object ProgramDataSource {

    val phases = listOf(
        Phase.FOUNDATION to (1..5),
        Phase.BUILD to (6..10),
        Phase.PEAK to (11..15)
    )

    fun getDayProgram(day: Int): DayProgram = when (day) {
        1 -> day1()
        2 -> day2()
        3 -> day3()
        4 -> day4()
        5 -> day5()
        6 -> day6()
        7 -> day7()
        8 -> day8()
        9 -> day9()
        10 -> day10()
        11 -> day11()
        12 -> day12()
        13 -> day13()
        14 -> day14()
        15 -> day15()
        else -> day1()
    }

    fun getAllDays(): List<DayProgram> = (1..15).map { getDayProgram(it) }

    // ─── Phase 1: Foundation ─────────────────────────────────────────────────

    private fun day1() = DayProgram(
        day = 1, phase = Phase.FOUNDATION, title = "Baseline & Reset", badge = "Foundation",
        morningRoutine = "Wake at 6:30am. 10 min sunlight exposure immediately — boosts testosterone via circadian rhythm regulation. 5 min diaphragmatic breathing: inhale 4s, hold 4s, exhale 6s. Cold face splash 30s. Weigh yourself and record your baseline. 10 min light stretching of hips and groin.",
        workout = WorkoutPlan(
            name = "Baseline Activation", type = WorkoutType.CARDIO,
            exercises = listOf(
                Exercise("d1_e1","Brisk Walk","1","20 min",0,"Walk at a pace where you can hold conversation but feel slightly breathless. Zone 2 cardio.",listOf("Cardiovascular system","Legs"),ExerciseCategory.CARDIO),
                Exercise("d1_e2","Bodyweight Squat",3,"15",60,"Feet shoulder-width apart. Lower until thighs are parallel. Push through heels.",listOf("Glutes","Quads","Pelvic floor"),ExerciseCategory.COMPOUND),
                Exercise("d1_e3","Hip Flexor Stretch",3,"45s/side",30,"Kneeling lunge position. Tuck pelvis, lean forward slightly until you feel anterior hip stretch.",listOf("Hip flexors"),ExerciseCategory.MOBILITY)
            ),
            totalDurationMinutes = 35, notes = "Day 1 is assessment, not maximum effort. Record all baseline metrics."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 15,
            exercises = listOf(
                PelvicExercise("pf1_e1","Foundation Kegel",PelvicExerciseType.KEGEL,3,3,3,10,"Lie on your back, knees bent. Imagine stopping urine mid-stream. Contract 3s, release fully 3s. Focus on feeling the RELEASE as clearly as the contraction."),
                PelvicExercise("pf1_e2","Reverse Kegel Introduction",PelvicExerciseType.REVERSE_KEGEL,0,5,3,8,"Belly breath in — let the pelvic floor gently descend and open. Do NOT push hard. This is a subtle opening and release sensation.")
            ),
            notes = "The reverse Kegel is equally important as the Kegel for erection quality. Master the distinction between contraction and full release.",
            focusType = PelvicFocusType.STRENGTH
        ),
        nutrition = NutritionPlan(
            totalCalories = 2800, proteinGrams = 180, carbGrams = 300, fatGrams = 80,
            meals = listOf(
                Meal("d1_m1","Breakfast","7:30 AM",listOf("3 whole eggs","Spinach omelette","1 cup oats with berries","Black coffee"),35,"High-quality protein and slow-release carbohydrates for sustained morning energy."),
                Meal("d1_m2","Lunch","12:30 PM",listOf("200g grilled chicken breast","150g brown rice","Large mixed salad","Olive oil dressing"),50,"Lean protein for muscle repair. Complex carbs for afternoon energy."),
                Meal("d1_m3","Dinner","7:00 PM",listOf("200g salmon","Roasted broccoli","Sweet potato"),45,"Omega-3 for endothelial function. Broccoli contains DIM which supports healthy oestrogen metabolism."),
                Meal("d1_m4","Snack","4:00 PM",listOf("30g walnuts","1 banana"),8,"Walnuts contain L-arginine. Banana provides potassium and fast carbs.")
            ),
            waterTargetLiters = 3.5f,
            specialFoods = listOf("Salmon (Omega-3)", "Walnuts (L-arginine)", "Broccoli (DIM)")
        ),
        sleepTarget = 8,
        mentalPerformance = "10 min body-scan meditation. Write 3 physical strengths without qualifying them. This sets your psychological baseline.",
        supplements = listOf(
            Supplement("Zinc","15mg","With dinner","Testosterone cofactor. Most men are deficient. Direct role in T synthesis.",EvidenceLevel.STRONG),
            Supplement("Vitamin D3","2000 IU","With fat-containing meal","Vitamin D receptor found on Leydig cells. Deficiency linked to low testosterone.",EvidenceLevel.STRONG),
            Supplement("Magnesium Glycinate","300mg","Before bed","Improves sleep quality and free testosterone. Glycinate form for better absorption.",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Alcohol — disrupts REM sleep and testosterone production",
            "Pornography — begin dopamine reset immediately, non-negotiable",
            "Processed sugar — spikes insulin, suppresses testosterone",
            "Sitting for more than 60 minutes without a 5-minute walk"
        ),
        whyItWorks = "Day 1 establishes baselines without overwhelming the body. Cortisol elevation from overtraining directly suppresses testosterone via the HPA axis. Pelvic floor awareness training begins — most men have hypertonic (too tight) floors, meaning they need to learn to RELAX as much as contract.",
        cardioTarget = CardioTarget(CardioType.WALK, 20, HeartRateZone.ZONE2, "Easy conversational pace. This is nervous system calibration, not fitness building.")
    )

    private fun day2() = DayProgram(
        day = 2, phase = Phase.FOUNDATION, title = "Aerobic Base", badge = "Foundation",
        morningRoutine = "6:30am wake. 10 min sunlight. Box breathing 5 min. 5 min neck rolls and cervical spine mobility. Cold shower last 60 seconds — boosts norepinephrine and alertness.",
        workout = WorkoutPlan(
            name = "Zone 2 Foundation", type = WorkoutType.CARDIO,
            exercises = listOf(
                Exercise("d2_e1","Zone 2 Cardio","1","30 min",0,"Run, cycle or brisk walk at 60–65% max HR. Hold a conversation but feel slightly breathless.",listOf("Heart","Lungs","Legs"),ExerciseCategory.CARDIO),
                Exercise("d2_e2","Glute Bridge",3,"20",45,"Lie on back, feet flat. Drive hips up, squeezing glutes at the top. Directly activates pelvic floor.",listOf("Glutes","Pelvic floor","Hamstrings"),ExerciseCategory.COMPOUND),
                Exercise("d2_e3","Dead Bug",3,"10/side",45,"Lie on back, arms up, knees at 90°. Lower opposite arm and leg simultaneously without arching lower back.",listOf("Core","Hip flexors"),ExerciseCategory.CORE)
            ),
            totalDurationMinutes = 45, notes = "Post-workout: 30g whey protein + banana within 30 minutes."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 15,
            exercises = listOf(
                PelvicExercise("pf2_e1","5-Second Kegel",PelvicExerciseType.KEGEL,5,5,3,10,"Contract 5 seconds, release FULLY 5 seconds. Full release is as important as contraction."),
                PelvicExercise("pf2_e2","Elevator Kegel",PelvicExerciseType.ELEVATOR,8,8,3,5,"Contract 25%, 50%, 75%, 100% in stages. Then release in stages. Builds nuanced neuromuscular control.")
            ),
            notes = "Reverse Kegel during belly breath: inhale deeply, let pelvic floor descend and open.",
            focusType = PelvicFocusType.CONTROL
        ),
        nutrition = NutritionPlan(
            totalCalories = 2800, proteinGrams = 180, carbGrams = 310, fatGrams = 82,
            meals = listOf(
                Meal("d2_m1","Breakfast","7:30 AM",listOf("Same as Day 1","Add: 1 tbsp extra virgin olive oil to salad","Pre-workout: banana 30 min before exercise"),35,"Oleic acid in olive oil supports testosterone production."),
                Meal("d2_m2","Lunch","12:30 PM",listOf("200g chicken breast","Brown rice","Large salad with olive oil"),50,""),
                Meal("d2_m3","Post-workout","10:00 AM",listOf("30g whey protein","1 banana","250ml water"),30,"Critical recovery window — within 30 minutes of training."),
                Meal("d2_m4","Dinner","7:00 PM",listOf("200g salmon","Roasted sweet potato","Steamed broccoli"),42,"")
            ),
            waterTargetLiters = 3.5f,
            specialFoods = listOf("Olive oil (oleic acid)", "Banana (potassium, fast carbs)")
        ),
        sleepTarget = 8,
        mentalPerformance = "2 min power posing in mirror post-shower. Read 20 pages of an empowering book. No social media after 8pm.",
        supplements = listOf(
            Supplement("Zinc","15mg","With dinner","Testosterone cofactor",EvidenceLevel.STRONG),
            Supplement("Vitamin D3","2000 IU","With lunch","Hormone synthesis",EvidenceLevel.STRONG),
            Supplement("Magnesium Glycinate","300mg","Before bed","Sleep quality",EvidenceLevel.STRONG),
            Supplement("Omega-3 Fish Oil","2g EPA+DHA","With largest meal","Endothelial function, anti-inflammatory, cardiovascular health",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Cycling with a hard saddle for more than 30 minutes — pudendal nerve compression",
            "Ejaculation today — conserve sexual energy during build phase",
            "Alcohol",
            "Pornography — Day 2 of reset"
        ),
        whyItWorks = "Zone 2 cardio improves mitochondrial density, endothelial function, and nitric oxide production — all directly linked to erection quality. The cardiovascular system is the biological foundation of sexual performance.",
        cardioTarget = CardioTarget(CardioType.RUN, 30, HeartRateZone.ZONE2, "60–65% max HR. Can hold conversation.")
    )

    private fun day3() = DayProgram(
        day = 3, phase = Phase.FOUNDATION, title = "Strength Introduction", badge = "Foundation",
        morningRoutine = "6:30am. Sunlight 10 min. 5 min breathing. Neck strengthening: isometric holds — hand on forehead push/resist (flexion), hand on back of head (extension), lateral resistance both sides. 4 directions × 10s × 3 reps each.",
        workout = WorkoutPlan(
            name = "Strength Session A", type = WorkoutType.STRENGTH,
            exercises = listOf(
                Exercise("d3_e1","Goblet Squat",3,"12",90,"Hold dumbbell at chest. Deep squat — pelvic floor loads during descent. Pause 1s at bottom.",listOf("Quads","Glutes","Pelvic floor","Core"),ExerciseCategory.COMPOUND),
                Exercise("d3_e2","Romanian Deadlift",3,"10",90,"Hinge at hips with soft knees. Feel hamstring stretch. Drive hips forward to stand.",listOf("Hamstrings","Glutes","Lower back"),ExerciseCategory.COMPOUND),
                Exercise("d3_e3","Push-up",3,"Max",60,"Full range of motion. Chest to floor. Scale to knees if needed.",listOf("Chest","Shoulders","Triceps"),ExerciseCategory.COMPOUND),
                Exercise("d3_e4","Plank",3,"30s",45,"Forearms or hands. Neutral spine. Do not let hips sag.",listOf("Core","Shoulders"),ExerciseCategory.CORE),
                Exercise("d3_e5","Hip Thrust",3,"15",60,"Upper back on bench. Drive through heels. Squeeze glutes hard at top. Peak pelvic floor activation.",listOf("Glutes","Pelvic floor","Hamstrings"),ExerciseCategory.COMPOUND)
            ),
            totalDurationMinutes = 50, notes = "Rest 90s between sets. Record weights used."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 20,
            exercises = listOf(
                PelvicExercise("pf3_e1","Quick Flicks",PelvicExerciseType.QUICK_FLICK,1,1,3,10,"Rapid contract and full release — 10 in a row. Builds fast-twitch pelvic floor fibers important for ejaculatory control."),
                PelvicExercise("pf3_e2","Sustained Hold",PelvicExerciseType.KEGEL,10,10,3,8,"10s contract, 10s full reverse Kegel. Done lying down, knees bent.")
            ),
            notes = "Do these lying down with knees bent for best isolation.",
            focusType = PelvicFocusType.STRENGTH
        ),
        nutrition = NutritionPlan(
            totalCalories = 2850, proteinGrams = 185, carbGrams = 305, fatGrams = 82,
            meals = listOf(
                Meal("d3_m1","Breakfast","7:30 AM",listOf("Overnight oats","Full-fat Greek yogurt","Berries","Chia seeds","Honey"),30,""),
                Meal("d3_m2","Lunch","12:30 PM",listOf("200g lean beef mince stir-fry","Bok choy","Quinoa"),50,""),
                Meal("d3_m3","Dinner","7:00 PM",listOf("Whole chicken thigh (skin on)","Roasted asparagus","White rice"),45,"Skin-on chicken thigh is a zinc source."),
                Meal("d3_m4","Snack","4:00 PM",listOf("2 boiled eggs","Apple"),16,"")
            ),
            waterTargetLiters = 3.5f,
            specialFoods = listOf("Chicken thigh (zinc)", "Quinoa (complete protein)", "Asparagus (folate)")
        ),
        sleepTarget = 8,
        mentalPerformance = "Write a confidence inventory: list 10 physical or personal strengths without qualifying them. No 'buts'. This is evidence-based cognitive restructuring.",
        supplements = listOf(
            Supplement("Zinc","15mg","With dinner","",EvidenceLevel.STRONG),
            Supplement("Vitamin D3","2000 IU","With lunch","",EvidenceLevel.STRONG),
            Supplement("Magnesium Glycinate","300mg","Before bed","",EvidenceLevel.STRONG),
            Supplement("Omega-3 Fish Oil","2g","With largest meal","",EvidenceLevel.STRONG),
            Supplement("Ashwagandha KSM-66","600mg","With dinner","Cortisol reduction, testosterone support. Begins working within 1–2 weeks.",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Pornography — Day 3 of reset",
            "Masturbation — dopamine conservation",
            "Alcohol",
            "Caffeine after 2pm",
            "Sitting cross-legged for extended periods"
        ),
        whyItWorks = "Compound resistance training is among the most evidence-supported interventions for raising free testosterone and HGH. The hip thrust specifically recruits glutes and pelvic floor simultaneously — training both erection-supporting structures at once.",
        cardioTarget = CardioTarget(CardioType.WALK, 15, HeartRateZone.ZONE2, "Warm-up only today. Walk to and from gym.")
    )

    private fun day4() = DayProgram(
        day = 4, phase = Phase.FOUNDATION, title = "Mobility & Recovery", badge = "Foundation",
        morningRoutine = "6:30am. Sunlight. Breathing. 10 min full-body mobility flow: cat-cow, world's greatest stretch, pigeon pose, thoracic rotation, deep squat hold. Hip mobility is critical — restricted hips create compensatory pelvic tension.",
        workout = WorkoutPlan(
            name = "Active Recovery", type = WorkoutType.ACTIVE_RECOVERY,
            exercises = listOf(
                Exercise("d4_e1","Yoga Flow","1","40 min",0,"Focus on hip flexors, adductors, and thoracic spine. YouTube: search for 'hip opener yoga 40 minutes'.",listOf("Full body"),ExerciseCategory.MOBILITY),
                Exercise("d4_e2","Foam Rolling","1","20 min",0,"Glutes, hip flexors, thoracic spine, IT band. Hold on tender spots 30–60 seconds.",listOf("Fascial tissue"),ExerciseCategory.MOBILITY),
                Exercise("d4_e3","Light Walk","1","20 min",0,"Easy pace. Fresh air. No intensity.",listOf("Cardiovascular recovery"),ExerciseCategory.CARDIO)
            ),
            totalDurationMinutes = 80, notes = "Recovery is not skipping. Adaptation occurs during rest, not training."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 15,
            exercises = listOf(
                PelvicExercise("pf4_e1","Deep Release Breathing",PelvicExerciseType.BREATHING,0,10,1,10,"5 min lying with knees bent. Deep belly breaths — pelvic floor descends on every inhale. Simply observe without forcing."),
                PelvicExercise("pf4_e2","Tension-Release Contrast",PelvicExerciseType.REVERSE_KEGEL,5,10,3,10,"5s tension, 10s full release. The long release builds capacity for voluntary relaxation.")
            ),
            notes = "Today is a RELEASE-focused pelvic session. Hypertonic (too tight) pelvic floors cause more issues than weak ones.",
            focusType = PelvicFocusType.RELEASE
        ),
        nutrition = NutritionPlan(
            totalCalories = 2700, proteinGrams = 165, carbGrams = 295, fatGrams = 82,
            meals = listOf(
                Meal("d4_m1","Breakfast","7:30 AM",listOf("Same base as Day 1"),30,""),
                Meal("d4_m2","Lunch","12:30 PM",listOf("200g chicken","150g brown rice","Large salad","Add: 200g cooked beetroot or beet juice","Add: 30g dark chocolate 70%+"),50,"Beets contain dietary nitrates → nitric oxide → vasodilation. Dark chocolate contains flavonoids."),
                Meal("d4_m3","Dinner","7:00 PM",listOf("200g salmon","Roasted sweet potato","Add: turmeric to rice — anti-inflammatory"),42,""),
                Meal("d4_m4","Snack","4:00 PM",listOf("Greek yogurt","Berries","Honey"),15,"")
            ),
            waterTargetLiters = 3.5f,
            specialFoods = listOf("Beetroot (dietary nitrates → NO)", "Dark chocolate 70%+ (flavonoids)", "Turmeric (curcumin, anti-inflammatory)")
        ),
        sleepTarget = 8,
        mentalPerformance = "Visualization exercise: 10 min eyes closed. Vividly picture performing with complete confidence, calmness, and physical capability. Athletes use this exact technique before competition — same neural pathways as physical practice.",
        supplements = listOf(
            Supplement("Zinc","15mg","With dinner","",EvidenceLevel.STRONG),
            Supplement("Vitamin D3","2000 IU","With lunch","",EvidenceLevel.STRONG),
            Supplement("Magnesium Glycinate","300mg","Before bed","",EvidenceLevel.STRONG),
            Supplement("Omega-3 Fish Oil","2g","With dinner","",EvidenceLevel.STRONG),
            Supplement("Ashwagandha KSM-66","600mg","With dinner","",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Intense training — CNS needs recovery",
            "Stress rumination",
            "Tight underwear (restricts scrotal temperature regulation)",
            "Pornography — Day 4 of reset"
        ),
        whyItWorks = "Scrotal temperature directly affects testicular function. Briefs that hold the testes close to the body chronically suppress testosterone compared to looser alternatives. Pelvic floor release work is supported by physiotherapy research for improving erectile and sexual function.",
        cardioTarget = CardioTarget(CardioType.WALK, 20, HeartRateZone.ZONE2, "Easy, restorative walk. No intensity target.")
    )

    private fun day5() = DayProgram(
        day = 5, phase = Phase.FOUNDATION, title = "Confidence Sprint", badge = "Foundation",
        morningRoutine = "6:30am. Sunlight. Cold shower full 2 minutes — habituating the nervous system to discomfort builds mental resilience. Posture check: wall stand — heels, glutes, upper back, head touching wall. Practice walking away maintaining this posture.",
        workout = WorkoutPlan(
            name = "Strength Session B", type = WorkoutType.STRENGTH,
            exercises = listOf(
                Exercise("d5_e1","Deadlift",3,"8",120,"Hinge pattern. Drive through floor. Maintain neutral spine. Most testosterone-stimulating exercise.",listOf("Full posterior chain","Core"),ExerciseCategory.COMPOUND),
                Exercise("d5_e2","Bench Press / Dumbbell Press",3,"10",90,"Full range of motion. Control the descent.",listOf("Chest","Shoulders","Triceps"),ExerciseCategory.COMPOUND),
                Exercise("d5_e3","Bent-over Row",3,"10",90,"Hinge at 45°. Pull elbows back. Builds back width — critical for visual presence.",listOf("Back","Biceps","Rear delts"),ExerciseCategory.COMPOUND),
                Exercise("d5_e4","Reverse Lunge",3,"12/leg",60,"Step back, lower knee to just above floor. Challenges balance and unilateral strength.",listOf("Quads","Glutes","Pelvic floor"),ExerciseCategory.COMPOUND),
                Exercise("d5_e5","Face Pull / Band Pull-apart",3,"20",45,"External rotation at shoulder. Critical for posture and appearance.",listOf("Rear delts","Rotator cuff","Traps"),ExerciseCategory.ISOLATION),
                Exercise("d5_e6","Hollow Body Hold",3,"20s",45,"Posterior pelvic tilt, arms overhead, legs extended. Core anti-extension.",listOf("Core","Hip flexors"),ExerciseCategory.CORE)
            ),
            totalDurationMinutes = 55, notes = "End of Phase 1 strength test. Record all weights."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 20,
            exercises = listOf(
                PelvicExercise("pf5_e1","Quick Flicks",PelvicExerciseType.QUICK_FLICK,1,1,3,10,"10 rapid contractions per set."),
                PelvicExercise("pf5_e2","Elevator Hold",PelvicExerciseType.ELEVATOR,8,8,3,5,"Full staircase up and down."),
                PelvicExercise("pf5_e3","Sustained 10s Hold",PelvicExerciseType.KEGEL,10,10,4,8,"Strongest effort today. Quality over speed."),
                PelvicExercise("pf5_e4","Reverse Kegel Breathing",PelvicExerciseType.BREATHING,0,10,1,5,"Close pelvic session with pure release breathing.")
            ),
            notes = "Complete all 4 exercise types today. 20 minutes total.",
            focusType = PelvicFocusType.STRENGTH
        ),
        nutrition = NutritionPlan(
            totalCalories = 2900, proteinGrams = 190, carbGrams = 310, fatGrams = 85,
            meals = listOf(
                Meal("d5_m1","Pre-workout Breakfast","7:00 AM",listOf("Oats","Protein powder","Banana"),35,"45 minutes before training."),
                Meal("d5_m2","Post-workout","9:30 AM",listOf("40g protein shake","50g fast carbs (white rice or banana)"),40,"Within 30 minutes of training."),
                Meal("d5_m3","Lunch","1:00 PM",listOf("200g ground beef (or oysters for zinc)","Sweet potato","Greens"),50,""),
                Meal("d5_m4","Dinner","7:00 PM",listOf("200g salmon","Brown rice","200ml pomegranate juice","Large salad"),42,"Pomegranate inhibits arginase → increased nitric oxide production.")
            ),
            waterTargetLiters = 3.5f,
            specialFoods = listOf("Pomegranate juice (arginase inhibitor → NO)", "Oysters or beef (zinc)", "Sweet potato (complex carbs)")
        ),
        sleepTarget = 8,
        mentalPerformance = "Affirmation drill: stand in front of a mirror for 3 minutes and speak 5 identity-level statements aloud: I am calm. I am strong. I am in control. I perform at my best. I am present. Research on pre-performance self-talk shows measurable performance gains.",
        supplements = listOf(
            Supplement("Zinc","15mg","With dinner","End of Week 1",EvidenceLevel.STRONG),
            Supplement("Vitamin D3","2000 IU","With lunch","",EvidenceLevel.STRONG),
            Supplement("Magnesium Glycinate","300mg","Before bed","",EvidenceLevel.STRONG),
            Supplement("Omega-3","2g","With largest meal","",EvidenceLevel.STRONG),
            Supplement("Ashwagandha KSM-66","600mg","With dinner","",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Pornography — Day 5, dopamine sensitivity actively resetting",
            "Alcohol",
            "Masturbation"
        ),
        whyItWorks = "End of Phase 1. By Day 5: nitric oxide pathways begin upregulating, pelvic floor neuromuscular awareness is establishing, testosterone is trending upward, dopamine reset is underway, and sleep architecture is improving. The body is primed for Phase 2 intensification.",
        cardioTarget = CardioTarget(CardioType.WALK, 15, HeartRateZone.ZONE2, "Walk as warm-up and cool-down only.")
    )

    // ─── Phase 2: Build ──────────────────────────────────────────────────────

    private fun day6() = DayProgram(
        day = 6, phase = Phase.BUILD, title = "Intensify Cardio", badge = "Build",
        morningRoutine = "6:30am. Sunlight 10 min. Box breathing 5 min. Cold shower full 3 min. Neck exercises. Posture wall-stand check.",
        workout = WorkoutPlan(
            name = "HIIT + Compound Strength", type = WorkoutType.HIIT,
            exercises = listOf(
                Exercise("d6_e1","HIIT Sprint Intervals","8","30s on / 90s off",90,"All-out effort for 30 seconds. Easy recovery for 90 seconds. 8 rounds total = 16 minutes.",listOf("Full cardiovascular system","Legs"),ExerciseCategory.CARDIO),
                Exercise("d6_e2","Deadlift",3,"8",120,"Maintain form from Day 5. Aim to add 2.5–5kg.",listOf("Full posterior chain"),ExerciseCategory.COMPOUND),
                Exercise("d6_e3","Hip Thrust",4,"15",60,"Add load. Barbell across hip crease or heavy dumbbell.",listOf("Glutes","Pelvic floor"),ExerciseCategory.COMPOUND),
                Exercise("d6_e4","Bulgarian Split Squat",3,"10/leg",90,"Rear foot elevated. Deep stretch at bottom.",listOf("Quads","Glutes","Balance"),ExerciseCategory.COMPOUND)
            ),
            totalDurationMinutes = 60, notes = "HIIT proven to increase testosterone acutely and over time."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 20,
            exercises = listOf(
                PelvicExercise("pf6_e1","Standing Kegels",PelvicExerciseType.KEGEL,5,5,3,10,"Standing with feet hip-width apart. Contract pelvic floor while keeping glutes and thighs fully relaxed. Harder and more functional than lying."),
                PelvicExercise("pf6_e2","Advanced Reverse Kegel",PelvicExerciseType.REVERSE_KEGEL,0,8,3,8,"During belly breath, a gentle downward push-out sensation. This is the controlled release critical for sustained erection maintenance.")
            ),
            notes = "Standing Kegels are more functionally relevant. Focus on glute/thigh relaxation during contraction.",
            focusType = PelvicFocusType.CONTROL
        ),
        nutrition = NutritionPlan(
            totalCalories = 3000, proteinGrams = 195, carbGrams = 325, fatGrams = 88,
            meals = listOf(
                Meal("d6_m1","Breakfast","7:30 AM",listOf("4 eggs scrambled","Avocado on sourdough","Fruit"),38,""),
                Meal("d6_m2","Lunch","12:30 PM",listOf("250g ground beef","Quinoa","Kimchi (probiotics)"),52,"Kimchi: gut microbiome → testosterone axis."),
                Meal("d6_m3","Dinner","7:00 PM",listOf("200g seared tuna","Edamame","Brown rice","Seaweed salad"),48,"Seaweed contains iodine for thyroid function."),
                Meal("d6_m4","Post-workout","9:30 AM",listOf("Protein shake 40g","50g carbs"),40,"")
            ),
            waterTargetLiters = 3.5f,
            specialFoods = listOf("Kimchi (probiotics)", "Seaweed (iodine)", "Edamame (plant protein)")
        ),
        sleepTarget = 8,
        mentalPerformance = "Social confidence drill: have a 10-minute conversation with a stranger today. Eye contact goal: hold 3–4 seconds before natural break. Systematic desensitization for social anxiety.",
        supplements = listOf(
            Supplement("Zinc","15mg","With dinner","",EvidenceLevel.STRONG),
            Supplement("Vitamin D3","2000 IU","With lunch","",EvidenceLevel.STRONG),
            Supplement("Magnesium Glycinate","300mg","Before bed","",EvidenceLevel.STRONG),
            Supplement("Omega-3","2g","With dinner","",EvidenceLevel.STRONG),
            Supplement("Ashwagandha KSM-66","600mg","With dinner","",EvidenceLevel.STRONG),
            Supplement("L-Citrulline","3g","Pre-workout on training days","Arginine precursor → nitric oxide → vasodilation. Better absorbed than arginine. RCT evidence for erection quality and exercise performance.",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Training to failure every session — cortisol spike suppresses testosterone",
            "Ejaculation — especially during intensification phase",
            "Pornography — Day 6"
        ),
        whyItWorks = "L-Citrulline has RCT-level evidence for improving erection hardness in mild erectile dysfunction, and enhancing exercise performance via improved blood flow. HIIT raises testosterone more acutely than steady-state cardio.",
        cardioTarget = CardioTarget(CardioType.HIIT_SPRINT, 20, HeartRateZone.ZONE5, "8 rounds: 30s sprint, 90s rest.")
    )

    private fun day7() = DayProgram(
        day = 7, phase = Phase.BUILD, title = "Full Body & Posture", badge = "Build",
        morningRoutine = "6:30am. Sunlight. Breathing. Check posture and do 5 minutes of thoracic extension over a foam roller. Standing tall with an open chest directly affects how you appear and how you feel.",
        workout = WorkoutPlan(
            name = "Full Body Circuit + Incline Walk", type = WorkoutType.STRENGTH,
            exercises = listOf(
                Exercise("d7_e1","Squat Jumps","3","45s on / 15s off",15,"Explosive. Land softly.",listOf("Legs","Power"),ExerciseCategory.COMPOUND),
                Exercise("d7_e2","Push-ups","3","45s",15,"",listOf("Chest","Shoulders"),ExerciseCategory.COMPOUND),
                Exercise("d7_e3","Reverse Lunges","3","45s",15,"",listOf("Quads","Glutes"),ExerciseCategory.COMPOUND),
                Exercise("d7_e4","Pike Push-ups","3","45s",15,"Elevate hips. Shoulder press pattern.",listOf("Shoulders","Triceps"),ExerciseCategory.COMPOUND),
                Exercise("d7_e5","Banded Hip Abduction","3","45s",15,"Resistance band above knees. Side step or seated.",listOf("Glute med","External rotators"),ExerciseCategory.ISOLATION),
                Exercise("d7_e6","Incline Treadmill Walk","1","20 min",0,"8–12% incline. 5–6 km/h. Low cortisol, high glute activation.",listOf("Glutes","Cardiovascular"),ExerciseCategory.CARDIO)
            ),
            totalDurationMinutes = 55, notes = "Circuit format: 45s on, 15s rest, 3 rounds. Then incline walk."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 15,
            exercises = listOf(
                PelvicExercise("pf7_e1","Functional Integration",PelvicExerciseType.KEGEL,3,3,1,10,"Practice Kegel contractions during the LAST 3 REPS of each squat set. Pelvic floor activates reflexively during exertion — training this coordination directly improves ejaculatory control.")
            ),
            notes = "Key insight: integrate pelvic floor contractions with resistance training. This is the real-world functional pattern.",
            focusType = PelvicFocusType.INTEGRATION
        ),
        nutrition = NutritionPlan(
            totalCalories = 3000, proteinGrams = 195, carbGrams = 320, fatGrams = 88,
            meals = listOf(
                Meal("d7_m1","Breakfast","7:30 AM",listOf("3 eggs","Spinach","Brown rice","Garlic (add to cooking)"),35,"Garlic: allicin inhibits cortisol in testes, increases LH → testosterone."),
                Meal("d7_m2","Lunch","12:30 PM",listOf("250g chicken","Quinoa","Large dark leafy greens","Pomegranate juice 200ml"),52,""),
                Meal("d7_m3","Dinner","7:00 PM",listOf("200g lean beef","Roasted vegetables with garlic","Sweet potato"),48,""),
                Meal("d7_m4","Snack","4:00 PM",listOf("Greek yogurt","Kiwi","Walnuts"),15,"")
            ),
            waterTargetLiters = 4.0f,
            specialFoods = listOf("Garlic (allicin → LH → testosterone)", "Dark leafy greens (L-arginine, folate, magnesium)", "Pomegranate juice")
        ),
        sleepTarget = 8,
        mentalPerformance = "Gratitude journaling: write 10 specific gratitude items. Research shows this reduces cortisol and increases oxytocin — both beneficial for sexual function and confidence.",
        supplements = listOf(
            Supplement("Full stack continues","","","See previous days",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Soy protein isolate in large quantities (phytoestrogens)",
            "Flaxseed in large amounts",
            "Pornography",
            "Very hot baths or saunas more than 15 min (temporary testosterone suppression)"
        ),
        whyItWorks = "Posture correction is functional performance preparation. An open chest and upright carriage signals confidence to others AND to yourself — embodied cognition research shows posture changes internal hormonal state.",
        cardioTarget = CardioTarget(CardioType.WALK, 20, HeartRateZone.ZONE2, "Incline treadmill walk at 8–12% grade.")
    )

    private fun day8() = DayProgram(
        day = 8, phase = Phase.BUILD, title = "Endurance & Mental", badge = "Build",
        morningRoutine = "6:30am. Sunlight. Breathing 10 min. Cold shower 3 min. Neck exercises. Add: 5 min face massage and jaw muscle release. Masseter tension is linked to pelvic floor tension via the craniosacral fascial chain.",
        workout = WorkoutPlan(
            name = "Cardio Endurance + Upper Body", type = WorkoutType.CARDIO,
            exercises = listOf(
                Exercise("d8_e1","Zone 2 Steady State","1","45 min",0,"HR 130–145 bpm. Builds the aerobic engine. VO2max improvement begins around Day 7–10.",listOf("Cardiovascular system"),ExerciseCategory.CARDIO),
                Exercise("d8_e2","Pull-up / Band Pull-down",3,"Max",90,"Builds back width. Posture and visual presence.",listOf("Lats","Biceps","Rear delts"),ExerciseCategory.COMPOUND),
                Exercise("d8_e3","Face Pull",3,"20",45,"External rotation. Rear delt and rotator cuff health.",listOf("Rear delts","Rotator cuff"),ExerciseCategory.ISOLATION),
                Exercise("d8_e4","Prone Cobra",3,"30s",30,"Lie face down. Lift chest, arms externally rotate. Thoracic extension.",listOf("Spinal erectors","Rear delts"),ExerciseCategory.MOBILITY)
            ),
            totalDurationMinutes = 65, notes = "The 45-min Zone 2 is the primary adaptation stimulus today."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 20,
            exercises = listOf(
                PelvicExercise("pf8_e1","Stop-Start Control",PelvicExerciseType.REVERSE_KEGEL,0,10,3,5,"Practice the sensation of 90% arousal threshold using mental imagery only, then use a sustained reverse Kegel to reset. Builds neurological inhibitory control — the mechanism that prevents premature ejaculation.")
            ),
            notes = "Stop-start neurological training: the most evidence-supported technique for ejaculatory control (Masters & Johnson). No physical stimulation required — mental imagery only.",
            focusType = PelvicFocusType.CONTROL
        ),
        nutrition = NutritionPlan(
            totalCalories = 3000, proteinGrams = 195, carbGrams = 320, fatGrams = 88,
            meals = listOf(
                Meal("d8_m1","Breakfast","7:30 AM",listOf("4 eggs","Whole-grain toast","Smoked salmon","Avocado"),38,""),
                Meal("d8_m2","Lunch","12:30 PM",listOf("200g mackerel or sardines","Brown rice","Large salad"),48,"Fatty fish: omega-3 specifically improves endothelial function."),
                Meal("d8_m3","Dinner","7:00 PM",listOf("200g chicken","Sweet potato","Greens"),45,""),
                Meal("d8_m4","Snack","4:00 PM",listOf("Handful pumpkin seeds","Greek yogurt"),12,"Pumpkin seeds: zinc and magnesium.")
            ),
            waterTargetLiters = 4.0f,
            specialFoods = listOf("Mackerel/sardines (omega-3, endothelial function)", "Pumpkin seeds (zinc, magnesium)")
        ),
        sleepTarget = 8,
        mentalPerformance = "Performance anxiety drill: write every specific fear about performance. Then for each fear, write one evidence-based counter-statement. This is CBT-based cognitive restructuring — most evidence-supported technique for performance anxiety.",
        supplements = listOf(
            Supplement("Full daily stack","","","Continue all from Day 6",EvidenceLevel.STRONG),
            Supplement("Panax Ginseng","200mg","Morning","Moderate evidence for erectile function and energy. Safe for short-term use.",EvidenceLevel.MODERATE)
        ),
        thingsToAvoid = listOf(
            "Energy drinks with taurine + caffeine combo (cortisol crash)",
            "Social media for more than 15 min/day",
            "Pornography — Day 8"
        ),
        whyItWorks = "Jaw and facial tension connects via the craniosacral fascial chain to pelvic floor tension — emerging research from manual therapy. The stop-start technique was developed by Masters and Johnson and remains the most evidence-supported ejaculatory control method.",
        cardioTarget = CardioTarget(CardioType.RUN, 45, HeartRateZone.ZONE2, "HR 130–145 bpm. Could speak a sentence but it's uncomfortable.")
    )

    private fun day9() = DayProgram(
        day = 9, phase = Phase.BUILD, title = "Peak Strength Day", badge = "Build",
        morningRoutine = "6:30am. Cold shower. Breathing. Carbohydrate-rich breakfast today — glycogen loading for peak training session.",
        workout = WorkoutPlan(
            name = "Peak Strength Session", type = WorkoutType.STRENGTH,
            exercises = listOf(
                Exercise("d9_e1","Barbell Squat",4,"6",120,"Heavy. Controlled. 3s descent, 1s pause, drive up. Full depth if possible.",listOf("Quads","Glutes","Core","Pelvic floor"),ExerciseCategory.COMPOUND),
                Exercise("d9_e2","Deadlift",3,"5",150,"True peak effort. Best testosterone response of any exercise.",listOf("Full posterior chain","Core"),ExerciseCategory.COMPOUND),
                Exercise("d9_e3","Weighted Hip Thrust",4,"10",90,"Add meaningful load. Peak pelvic floor activation under load.",listOf("Glutes","Pelvic floor"),ExerciseCategory.COMPOUND),
                Exercise("d9_e4","Overhead Press",3,"8",90,"Seated or standing. Press directly overhead.",listOf("Shoulders","Triceps","Core"),ExerciseCategory.COMPOUND),
                Exercise("d9_e5","Pull-up",3,"Max",90,"Weighted if more than 10 reps unweighted.",listOf("Lats","Biceps"),ExerciseCategory.COMPOUND)
            ),
            totalDurationMinutes = 65, notes = "Rest 2 minutes between sets. Record all weights. Post-workout: 20 min light cardio to flush lactic acid."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 10,
            exercises = listOf(
                PelvicExercise("pf9_e1","Recovery Breathing",PelvicExerciseType.BREATHING,0,10,1,10,"10 min pure slow diaphragmatic breathing. Let pelvic floor fully descend on every inhale. Ideal on heavy strength days when pelvic floor is already heavily recruited under load.")
            ),
            notes = "Recovery pelvic session after heavy training. Breathing only.",
            focusType = PelvicFocusType.RELEASE
        ),
        nutrition = NutritionPlan(
            totalCalories = 3300, proteinGrams = 205, carbGrams = 380, fatGrams = 88,
            meals = listOf(
                Meal("d9_m1","Pre-workout Breakfast","7:00 AM",listOf("Large oats","Banana","Honey","Protein powder"),38,"Carb-load for peak performance."),
                Meal("d9_m2","Post-workout","After training",listOf("40g protein shake","White rice 200g cooked","Banana"),40,"Critical: within 30 minutes."),
                Meal("d9_m3","Lunch","1:30 PM",listOf("250g lean red beef","Roasted sweet potato","Salad"),52,"Red meat: creatine, zinc, iron, B12."),
                Meal("d9_m4","Dinner","7:00 PM",listOf("200g chicken","Quinoa","Greens"),45,"")
            ),
            waterTargetLiters = 4.0f,
            specialFoods = listOf("Red meat (natural creatine, zinc)", "White rice (fast glycogen refuel)", "Honey (natural carbs, pre-workout)")
        ),
        sleepTarget = 9,
        mentalPerformance = "Visualization x2: 5 min performance visualization, 5 min calm-in-control self visualization. Your brain uses the same neural pathways for mental rehearsal as physical practice.",
        supplements = listOf(
            Supplement("Full daily stack","","","",EvidenceLevel.STRONG),
            Supplement("Creatine Monohydrate","5g","Post-workout shake","Most researched supplement in sports science. Consistent safety and efficacy data. Raises DHT alongside testosterone. Strength, power, muscle fullness.",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Training to complete failure — leave 1–2 reps in reserve on main lifts",
            "Skipping post-workout nutrition",
            "Less than 8 hours sleep after this session"
        ),
        whyItWorks = "Creatine monohydrate is the most thoroughly researched performance supplement. For sexual health: DHT (dihydrotestosterone) is a potent androgen derived from testosterone. Creatine supplementation modestly raises DHT alongside testosterone, contributing to androgenic benefits.",
        cardioTarget = CardioTarget(CardioType.WALK, 20, HeartRateZone.ZONE2, "Post-workout lactic acid flush. Easy walk.")
    )

    private fun day10() = DayProgram(
        day = 10, phase = Phase.BUILD, title = "Integration & Reset", badge = "Build",
        morningRoutine = "6:30am. 15 min sunrise meditation outside. Breathing — 15 min 4-7-8 pattern: inhale 4s, hold 7s, exhale 8s. Cold shower. Full body stretch.",
        workout = WorkoutPlan(
            name = "Active Recovery + Swimming", type = WorkoutType.ACTIVE_RECOVERY,
            exercises = listOf(
                Exercise("d10_e1","Yoga Flow / Mobility","1","40 min",0,"Focus on hip flexors and adductor release. These directly affect pelvic floor function.",listOf("Hip flexors","Adductors","Thoracic spine"),ExerciseCategory.MOBILITY),
                Exercise("d10_e2","Swimming (if available)","1","20 min",0,"Best full-body recovery exercise. Zero impact. Encourages diaphragmatic breathing.",listOf("Full body"),ExerciseCategory.CARDIO),
                Exercise("d10_e3","Foam Rolling","1","20 min",0,"All major groups.",listOf("Fascia"),ExerciseCategory.MOBILITY)
            ),
            totalDurationMinutes = 80, notes = "End of Phase 2. Full self-assessment today."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 20,
            exercises = listOf(
                PelvicExercise("pf10_e1","Phase 2 Assessment",PelvicExerciseType.KEGEL,10,10,3,10,"Test: can you (1) contract and hold 10s, (2) do 10 quick flicks in a row, (3) perform a clear felt reverse Kegel? Compare to Day 1 baseline."),
                PelvicExercise("pf10_e2","Reverse Kegel Mastery",PelvicExerciseType.REVERSE_KEGEL,0,10,3,8,"Extend reverse Kegel duration. Full opening sensation.")
            ),
            notes = "Review and assessment. Identify your weakest technique and note it.",
            focusType = PelvicFocusType.CONTROL
        ),
        nutrition = NutritionPlan(
            totalCalories = 2700, proteinGrams = 165, carbGrams = 295, fatGrams = 82,
            meals = listOf(
                Meal("d10_m1","Breakfast","7:30 AM",listOf("Bone broth if available (collagen, glycine)","Oats","Berries"),25,"Glycine supports sleep quality and connective tissue."),
                Meal("d10_m2","Lunch","12:30 PM",listOf("Large Greek salad","Feta","Olives","Grilled vegetables","Olive oil"),38,""),
                Meal("d10_m3","Dinner","7:00 PM",listOf("200g oily fish","Brown rice","Steamed greens"),42,""),
                Meal("d10_m4","Snack","4:00 PM",listOf("Dark chocolate 70%+","Berries","Walnuts"),10,"")
            ),
            waterTargetLiters = 3.5f,
            specialFoods = listOf("Bone broth (glycine, collagen)", "Olives (monounsaturated fat, testosterone)", "Dark chocolate 70%+ (flavonoids, dopamine)")
        ),
        sleepTarget = 8,
        mentalPerformance = "Mentor self journaling: write advice from your ideal confident self to your current self. Ethan Kross research: self-distancing reduces anxiety and improves performance.",
        supplements = listOf(
            Supplement("Full daily stack","","","",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "New supplements or foods within 5 days of performance (allergy risk)",
            "Overtraining signs: sleep disturbance, mood drop, resting HR elevated by 7+ bpm"
        ),
        whyItWorks = "Phase 2 complete. By Day 10: cardiovascular efficiency measurably improved, pelvic floor control substantially developed, testosterone elevated from consistent resistance training, dopamine sensitivity reset from pornography abstinence, and sleep architecture deepened. Phase 3 is refinement and peak.",
        cardioTarget = CardioTarget(CardioType.SWIMMING, 20, HeartRateZone.ZONE2, "Swimming if available. Otherwise easy walk.")
    )

    // ─── Phase 3: Peak ────────────────────────────────────────────────────────

    private fun day11() = DayProgram(
        day = 11, phase = Phase.PEAK, title = "Peak Performance Mode", badge = "Peak",
        morningRoutine = "6:30am. Full morning protocol — now feels natural. Add: 10 min guided breathwork (Wim Hof or box breathing). Cold shower 3 min. Posture check. Neck exercises.",
        workout = WorkoutPlan(
            name = "Performance Strength + HIIT", type = WorkoutType.STRENGTH,
            exercises = listOf(
                Exercise("d11_e1","Deadlift",3,"5",120,"Maintain peak strength.",listOf("Full posterior chain"),ExerciseCategory.COMPOUND),
                Exercise("d11_e2","Hip Thrust",4,"12",75,"Loaded. Peak pelvic floor under load.",listOf("Glutes","Pelvic floor"),ExerciseCategory.COMPOUND),
                Exercise("d11_e3","Pull-ups",3,"Max",90,"Weighted if able.",listOf("Lats","Biceps"),ExerciseCategory.COMPOUND),
                Exercise("d11_e4","HIIT Cardio","8","20 min",90,"Pump-focused. Higher rep ranges, shorter rest for vascularity.",listOf("Cardiovascular","Full body"),ExerciseCategory.CARDIO)
            ),
            totalDurationMinutes = 55, notes = "Shift emphasis: everything now serves acute appearance and performance rather than pure building."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 20,
            exercises = listOf(
                PelvicExercise("pf11_e1","Arousal-Phase Control",PelvicExerciseType.KEGEL,5,5,3,10,"Practice pelvic floor contractions during arousal using mental imagery only. Goal: strong contraction that supports erection, followed by controlled reverse Kegel release. This IS the neurological pattern of erection control. 10 repetitions."),
                PelvicExercise("pf11_e2","Sustained Performance Hold",PelvicExerciseType.KEGEL,15,10,3,5,"15s contract, 10s release. Building endurance.")
            ),
            notes = "Advanced control training. No physical stimulation — mental imagery only.",
            focusType = PelvicFocusType.INTEGRATION
        ),
        nutrition = NutritionPlan(
            totalCalories = 3100, proteinGrams = 200, carbGrams = 350, fatGrams = 88,
            meals = listOf(
                Meal("d11_m1","Breakfast","7:30 AM",listOf("4 eggs","Oats","Banana","Black coffee"),38,""),
                Meal("d11_m2","Lunch","12:30 PM",listOf("250g chicken","Brown rice (increase carbs 20% today)","Sweet potato","200ml beet juice"),50,"Beet juice: acute nitric oxide elevation 2–3 hours after, lasting 4–6 hours."),
                Meal("d11_m3","Dinner","7:00 PM",listOf("200g salmon","Quinoa","Large salad","200ml pomegranate juice"),48,""),
                Meal("d11_m4","Snack","4:00 PM",listOf("Greek yogurt","Mixed berries","Dark chocolate"),12,"")
            ),
            waterTargetLiters = 4.0f,
            specialFoods = listOf("Beet juice (dietary nitrates → acute NO)", "Pomegranate juice (arginase inhibitor)","Complex carbs (serotonin, muscle fullness)")
        ),
        sleepTarget = 8,
        mentalPerformance = "Social rehearsal: record yourself on video for 2 minutes just speaking. Review posture, eye line, vocal projection. Athletes call this film study — watching yourself objectively to identify and fix patterns.",
        supplements = listOf(
            Supplement("Full stack","","","All supplements from Day 9",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Any new medications or recreational substances",
            "Alcohol in any quantity for the last 5 days"
        ),
        whyItWorks = "Beet juice research demonstrates acute nitric oxide elevation 2–3 hours after consumption lasting 4–6 hours — the same mechanism as PDE5 inhibitor medications (Viagra) but naturally via dietary nitrates and vasodilation.",
        cardioTarget = CardioTarget(CardioType.HIIT_SPRINT, 20, HeartRateZone.ZONE4, "8 rounds HIIT for vascularity and cardiovascular peak.")
    )

    private fun day12() = DayProgram(
        day = 12, phase = Phase.PEAK, title = "Appearance & Confidence", badge = "Peak",
        morningRoutine = "6:30am. Full protocol. Add: 5 min facial exercises — jaw, cheek lift, brow tension release. Grooming is performance preparation. Cold water rinse, SPF moisturizer.",
        workout = WorkoutPlan(
            name = "Aesthetic Pump Session", type = WorkoutType.STRENGTH,
            exercises = listOf(
                Exercise("d12_e1","Dumbbell Press",3,"15",45,"Moderate weight, high reps, 45s rest. Creates pump and visual fullness.",listOf("Chest","Shoulders"),ExerciseCategory.COMPOUND),
                Exercise("d12_e2","Wide Grip Cable Row / Dumbbell Row",3,"15",45,"Back width emphasis.",listOf("Lats","Rhomboids"),ExerciseCategory.COMPOUND),
                Exercise("d12_e3","Lateral Raise",3,"15",45,"Shoulder width.",listOf("Medial delt"),ExerciseCategory.ISOLATION),
                Exercise("d12_e4","Bicep Curl",3,"15",45,"",listOf("Biceps"),ExerciseCategory.ISOLATION),
                Exercise("d12_e5","Hanging Leg Raise",3,"15",60,"Abs.",listOf("Core","Hip flexors"),ExerciseCategory.CORE),
                Exercise("d12_e6","Steady Cardio","1","20 min",0,"Moderate pace. Vascularity.",listOf("Cardiovascular"),ExerciseCategory.CARDIO)
            ),
            totalDurationMinutes = 50, notes = "High reps, short rest, pump-focused. Visual fullness and vascularity are the goal."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 15,
            exercises = listOf(
                PelvicExercise("pf12_e1","Wall-Stand Pelvic Integration",PelvicExerciseType.KEGEL,5,5,3,10,"Pelvic floor contractions while standing against wall in correct posture — functional integration with real-world stance.")
            ),
            notes = "Functional integration: pelvic floor with posture. This is the real-world performance position.",
            focusType = PelvicFocusType.INTEGRATION
        ),
        nutrition = NutritionPlan(
            totalCalories = 2900, proteinGrams = 195, carbGrams = 320, fatGrams = 78,
            meals = listOf(
                Meal("d12_m1","Breakfast","7:30 AM",listOf("4 eggs","Lean meat","Oats","Fruit"),38,"Reduce sodium today — less subcutaneous water retention."),
                Meal("d12_m2","Lunch","12:30 PM",listOf("250g grilled chicken","White rice","Asparagus","No added salt"),52,"Lower sodium: avoid processed foods today."),
                Meal("d12_m3","Dinner","7:00 PM",listOf("200g white fish","Steamed vegetables","Brown rice"),42,""),
                Meal("d12_m4","Snack","4:00 PM",listOf("Fruit bowl","Protein shake"),12,"")
            ),
            waterTargetLiters = 4.0f,
            specialFoods = listOf("Low sodium foods (reduce water retention)", "High water vegetables (asparagus, cucumber)")
        ),
        sleepTarget = 8,
        mentalPerformance = "Pre-performance mental script: write how you want to feel and perform in 3–5 present-tense sentences. Read it aloud morning and evening. Sports psychologists call this a performance routine — it primes the nervous system.",
        supplements = listOf(
            Supplement("Full stack continues","","","",EvidenceLevel.STRONG),
            Supplement("Vitamin C","500mg","With breakfast","Antioxidant, cortisol buffer",EvidenceLevel.MODERATE)
        ),
        thingsToAvoid = listOf(
            "Binge eating or large meals",
            "Gas-producing foods (beans, large amounts of cruciferous vegetables)",
            "High sodium foods",
            "Late alcohol"
        ),
        whyItWorks = "Sodium reduction in the final days reduces subcutaneous fluid. Combined with adequate carbohydrate, muscles look fuller and skin looks tighter — the same protocol used by physique competitors before competition.",
        cardioTarget = CardioTarget(CardioType.RUN, 20, HeartRateZone.ZONE3, "Moderate steady-state for vascularity.")
    )

    private fun day13() = DayProgram(
        day = 13, phase = Phase.PEAK, title = "Nervous System Tune", badge = "Peak",
        morningRoutine = "6:30am. Full protocol. Extra breathing: 15 min. Cold shower. Notice: your response to cold should be significantly faster than Day 1 — evidence of nervous system training over 13 days.",
        workout = WorkoutPlan(
            name = "Light Maintenance Taper", type = WorkoutType.ACTIVE_RECOVERY,
            exercises = listOf(
                Exercise("d13_e1","Hip Thrust",2,"10",60,"Light weight. Movement pattern only.",listOf("Glutes"),ExerciseCategory.COMPOUND),
                Exercise("d13_e2","Push-up",2,"15",45,"",listOf("Chest"),ExerciseCategory.COMPOUND),
                Exercise("d13_e3","Deadlift (light)",2,"5",90,"50% of working weight. Movement groove only.",listOf("Posterior chain"),ExerciseCategory.COMPOUND),
                Exercise("d13_e4","Yoga / Stretch","1","20 min",0,"Full body mobility.",listOf("Full body"),ExerciseCategory.MOBILITY),
                Exercise("d13_e5","Light Walk","1","20 min",0,"",listOf("Cardiovascular"),ExerciseCategory.CARDIO)
            ),
            totalDurationMinutes = 45, notes = "NO new training stimulus today. The adaptation from Days 1–12 is locked in. Avoid soreness."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 25,
            exercises = listOf(
                PelvicExercise("pf13_e1","Quick Flicks Polish",PelvicExerciseType.QUICK_FLICK,1,1,3,10,""),
                PelvicExercise("pf13_e2","Elevator Full Run",PelvicExerciseType.ELEVATOR,8,8,3,5,""),
                PelvicExercise("pf13_e3","Reverse Kegel Mastery",PelvicExerciseType.REVERSE_KEGEL,0,10,3,8,""),
                PelvicExercise("pf13_e4","Arousal Phase Practice",PelvicExerciseType.KEGEL,5,8,3,10,"Mental imagery. Full protocol.")
            ),
            notes = "Consolidation session: run through every technique learned. This is neuromuscular polishing.",
            focusType = PelvicFocusType.INTEGRATION
        ),
        nutrition = NutritionPlan(
            totalCalories = 2900, proteinGrams = 190, carbGrams = 325, fatGrams = 80,
            meals = listOf(
                Meal("d13_m1","Breakfast","7:30 AM",listOf("4 eggs","Turkey breast (L-arginine)","Oats"),40,""),
                Meal("d13_m2","Lunch","12:30 PM",listOf("250g chicken","Pumpkin seeds (zinc, arginine)","Quinoa"),50,""),
                Meal("d13_m3","Dinner","7:00 PM",listOf("200g salmon","Peanuts or mixed nuts","Brown rice"),48,""),
                Meal("d13_m4","Snack","4:00 PM",listOf("Full-fat dairy (casein protein)","Berries"),15,"")
            ),
            waterTargetLiters = 4.0f,
            specialFoods = listOf("Turkey breast (L-arginine)", "Pumpkin seeds (zinc, arginine)", "Full-fat dairy (casein)")
        ),
        sleepTarget = 9,
        mentalPerformance = "Social confidence: make direct, extended eye contact during every interaction today. Confident body language is now automatic — consciously reinforce it. Open chest, relaxed shoulders, feet shoulder-width apart.",
        supplements = listOf(
            Supplement("Full stack","","","",EvidenceLevel.STRONG)
        ),
        thingsToAvoid = listOf(
            "Intense training",
            "New foods",
            "Alcohol",
            "Late caffeine (after 1pm)"
        ),
        whyItWorks = "The taper before peak performance is established sports science. Athletes reduce volume 2–3 days before competition. Physiological adaptation continues even as training decreases — this is supercompensation. Muscles are glycogen-loaded, nervous system is fresh.",
        cardioTarget = CardioTarget(CardioType.WALK, 20, HeartRateZone.ZONE2, "Easy walk only. Restorative.")
    )

    private fun day14() = DayProgram(
        day = 14, phase = Phase.PEAK, title = "Pre-Day Preparation", badge = "Peak",
        morningRoutine = "6:30am. Sunlight. Extra-long breathing session: 20 min. Cool (not cold) shower today — avoid unnecessary cortisol spike. Grooming and appearance preparation.",
        workout = WorkoutPlan(
            name = "Minimal Activation Only", type = WorkoutType.ACTIVE_RECOVERY,
            exercises = listOf(
                Exercise("d14_e1","Bodyweight Squat",2,"10",30,"Light activation only.",listOf("Legs"),ExerciseCategory.COMPOUND),
                Exercise("d14_e2","Hip Mobility Flow","1","10 min",0,"Full hip circles, pigeon pose, deep squat.",listOf("Hips"),ExerciseCategory.MOBILITY),
                Exercise("d14_e3","Shoulder Rolls & Thoracic Rotation","1","5 min",0,"Posture preparation.",listOf("Shoulders","Thoracic"),ExerciseCategory.MOBILITY)
            ),
            totalDurationMinutes = 20, notes = "10–15 minutes only. Enough to feel activated and blood-pumped, not enough to tire or create soreness."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 10,
            exercises = listOf(
                PelvicExercise("pf14_e1","Pre-Performance Release",PelvicExerciseType.BREATHING,0,10,1,10,"10 min focused on awareness and complete release. Goal today: RELAXED, neutral pelvic floor. Not hypertonic, not hypotonic. Full release is the goal."),
                PelvicExercise("pf14_e2","5 Slow Contractions",PelvicExerciseType.KEGEL,5,8,1,5,"End with full release after each. Primer only.")
            ),
            notes = "Light pelvic session. Relaxation and awareness — not training. Primed, not strained.",
            focusType = PelvicFocusType.RELEASE
        ),
        nutrition = NutritionPlan(
            totalCalories = 2800, proteinGrams = 185, carbGrams = 310, fatGrams = 80,
            meals = listOf(
                Meal("d14_m1","Breakfast","7:30 AM",listOf("Lean protein: eggs or chicken","Complex carbs: oats","Fruit"),35,"Easy to digest. No experimentation."),
                Meal("d14_m2","Lunch","12:30 PM",listOf("Easy-to-digest: white rice","Grilled fish","Salad"),45,""),
                Meal("d14_m3","Dinner","7:00 PM",listOf("Light meal","Avoid heavy red meat","Avoid gas-producing foods","200ml beet juice","200ml pomegranate juice"),30,"Beet and pomegranate juice today for acute NO tomorrow."),
                Meal("d14_m4","Snack","4:00 PM",listOf("Fruit","Small protein shake"),12,"")
            ),
            waterTargetLiters = 4.0f,
            specialFoods = listOf("Beet juice (NO boost for tomorrow)", "Pomegranate juice (tomorrow's NO)", "Easy-digest foods (comfort)")
        ),
        sleepTarget = 9,
        mentalPerformance = "Final visualization: 20 min full-detail mental rehearsal. Calm, confident, focused. Then LET IT GO. Over-preparation creates anxiety. Trust the process. Distract yourself with entertainment before bed.",
        supplements = listOf(
            Supplement("Ashwagandha KSM-66","600mg","Evening","Last dose before performance day",EvidenceLevel.STRONG),
            Supplement("Zinc","15mg","With dinner","",EvidenceLevel.STRONG),
            Supplement("Magnesium Glycinate","400mg","Before bed","Extra for sleep quality tonight",EvidenceLevel.STRONG),
            Supplement("L-Theanine","200mg","Before bed if mind is racing","Non-drowsy relaxation for better sleep",EvidenceLevel.MODERATE)
        ),
        thingsToAvoid = listOf(
            "Alcohol — even one drink disrupts REM testosterone synthesis",
            "Pornography — Day 14 of reset, do not break now",
            "Staying up late",
            "Overthinking",
            "Masturbation"
        ),
        whyItWorks = "Sleep is when testosterone is synthesized. Peak testosterone occurs during early morning REM sleep hours. A full, quality sleep the night before directly affects next-day testosterone, cognitive performance, and erection quality.",
        cardioTarget = CardioTarget(CardioType.WALK, 15, HeartRateZone.ZONE2, "Easy short walk. Fresh air only.")
    )

    private fun day15() = DayProgram(
        day = 15, phase = Phase.PEAK, title = "Performance Day", badge = "Peak",
        morningRoutine = "Wake naturally or at planned time. Sunlight immediately. 10 min breathing — slow, parasympathetic activation. Warm (not cold) shower. Eat moderate breakfast 2–3 hours before performance: eggs, oats, banana. Not too full.",
        workout = WorkoutPlan(
            name = "Light Activation Only", type = WorkoutType.ACTIVE_RECOVERY,
            exercises = listOf(
                Exercise("d15_e1","Bodyweight Squats","2","10",30,"Light blood flow only.",listOf("Legs"),ExerciseCategory.COMPOUND),
                Exercise("d15_e2","Band Pull-apart","2","15",30,"Posture activation.",listOf("Rear delts"),ExerciseCategory.ISOLATION),
                Exercise("d15_e3","Light Push-ups","2","10",30,"Movement pattern.",listOf("Chest"),ExerciseCategory.COMPOUND)
            ),
            totalDurationMinutes = 15, notes = "15 minutes only. Enough for blood flow, not enough to fatigue."
        ),
        pelvicFloor = PelvicFloorSession(
            totalMinutes = 10,
            exercises = listOf(
                PelvicExercise("pf15_e1","Pre-Performance Prime",PelvicExerciseType.REVERSE_KEGEL,0,8,1,5,"Start with full release."),
                PelvicExercise("pf15_e2","5 Slow Contractions",PelvicExerciseType.KEGEL,5,5,1,5,"Slow, deliberate. End with full release."),
                PelvicExercise("pf15_e3","Final Release",PelvicExerciseType.BREATHING,0,10,1,3,"3 deep breaths. Full pelvic floor opening. Relaxed, ready state.")
            ),
            notes = "Primes the neuromuscular circuit in a relaxed, ready state. Start with release. End with release.",
            focusType = PelvicFocusType.RELEASE
        ),
        nutrition = NutritionPlan(
            totalCalories = 2600, proteinGrams = 170, carbGrams = 285, fatGrams = 78,
            meals = listOf(
                Meal("d15_m1","Pre-Performance Breakfast","2–3h before",listOf("Eggs","Oats","Banana","One good coffee or matcha"),35,"Familiar foods only. Nothing experimental."),
                Meal("d15_m2","Pre-Performance","2–3h before",listOf("200ml pomegranate juice","200ml beet juice"),2,"Nitric oxide prime. Take 2–3 hours before performance."),
                Meal("d15_m3","Stay hydrated","Throughout",listOf("Sip water regularly"),0,"Do not overhydrate — bloating.")
            ),
            waterTargetLiters = 3.5f,
            specialFoods = listOf("Beet juice (acute NO — 2–3h before)", "Pomegranate juice (acute NO)", "Familiar comfortable foods only")
        ),
        sleepTarget = 9,
        mentalPerformance = "Pre-performance routine: read your performance script. Take 5 slow breaths. Say your affirmations aloud. Remind yourself: 15 days of preparation. Your body knows what to do. Perform one physical anchor cue that signals to your nervous system it's time to perform. This is standard sports psychology pre-performance protocol.",
        supplements = listOf(
            Supplement("L-Citrulline","3g","With breakfast","Acute NO boost. Timing: 60–90 min before.",EvidenceLevel.STRONG),
            Supplement("Panax Ginseng","200mg","Morning","Energy and erectile support.",EvidenceLevel.MODERATE)
        ),
        thingsToAvoid = listOf(
            "New foods or supplements",
            "Alcohol",
            "Excessive masturbation before performance",
            "Checking social media obsessively",
            "Rushing",
            "Anxiety spirals — the body is prepared"
        ),
        whyItWorks = "15 days of deliberate preparation: cardiovascular efficiency improved, pelvic floor neuromuscular control built, testosterone elevated, dopamine sensitivity reset, sleep architecture optimized, confidence systematically built through evidence-based interventions. Performance anxiety is the final variable — managed by breathing, routine, and trust in the process.",
        cardioTarget = CardioTarget(CardioType.WALK, 10, HeartRateZone.ZONE2, "Optional short walk for calm and fresh air only.")
    )
}
