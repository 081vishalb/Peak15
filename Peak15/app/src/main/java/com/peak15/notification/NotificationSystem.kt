package com.peak15.notification

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.peak15.MainActivity
import com.peak15.Peak15Application.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit

// ─── Notification IDs ─────────────────────────────────────────────────────────

object NotificationIds {
    const val MORNING_REMINDER   = 1001
    const val WATER_REMINDER     = 1002
    const val SUPPLEMENT_EVENING = 1003
    const val PELVIC_REMINDER    = 1004
}

// ─── Notification Content ─────────────────────────────────────────────────────

object NotificationContent {
    val morningMessages = listOf(
        "Good morning. Day ${"{day}"} starts now. Sunlight first." ,
        "Your Peak15 protocol for Day ${"{day}"} is ready. Start with the morning routine.",
        "Day ${"{day}"}. The only workout you'll regret is the one you didn't do.",
        "Rise and perform. Your 15-day program is working — don't break the chain."
    )

    val waterMessages = listOf(
        "Hydration check — have you hit your water target today?",
        "Drink 250ml now. Dehydration reduces performance and testosterone.",
        "Water reminder — you're at ${"{current}"}L of your ${"{target}"}L target.",
        "Your tissues, joints, and blood vessels need water. Drink now."
    )

    val supplementMessages = listOf(
        "Evening supplement stack: Zinc 15mg, Magnesium Glycinate 300mg before bed.",
        "Time for your evening supplements. Magnesium improves sleep quality.",
        "Supplement reminder: Zinc with dinner, Magnesium before bed."
    )

    val pelvicMessages = listOf(
        "Pelvic floor session today: ${"{duration}"} minutes. Open the app to start.",
        "Don't forget your pelvic floor training. 15 minutes for lasting results.",
        "Kegel + Reverse Kegel reminder. Your pelvic floor work today matters."
    )
}

// ─── Morning Reminder Worker ──────────────────────────────────────────────────

@HiltWorker
class MorningReminderWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted params  : WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val day = inputData.getInt(KEY_DAY, 1)

        showNotification(
            context     = applicationContext,
            id          = NotificationIds.MORNING_REMINDER,
            channelId   = CHANNEL_MORNING,
            title       = "Peak15 · Day $day",
            body        = "Start your morning routine. Sunlight → breathing → cold shower.",
            priority    = NotificationCompat.PRIORITY_DEFAULT
        )
        return Result.success()
    }

    companion object {
        const val KEY_DAY = "day"
        const val WORK_NAME = "morning_reminder"
    }
}

// ─── Water Reminder Worker ────────────────────────────────────────────────────

@HiltWorker
class WaterReminderWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted params  : WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        showNotification(
            context   = applicationContext,
            id        = NotificationIds.WATER_REMINDER,
            channelId = CHANNEL_WATER,
            title     = "Hydration Reminder",
            body      = "Drink 250ml now. Your body is 60% water — every cell depends on it.",
            priority  = NotificationCompat.PRIORITY_LOW
        )
        return Result.success()
    }
}

// ─── Supplement Reminder Worker ───────────────────────────────────────────────

@HiltWorker
class SupplementReminderWorker @AssistedInject constructor(
    @Assisted context : Context,
    @Assisted params  : WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        showNotification(
            context   = applicationContext,
            id        = NotificationIds.SUPPLEMENT_EVENING,
            channelId = CHANNEL_SUPPLEMENT,
            title     = "Evening Supplements",
            body      = "Zinc 15mg with dinner · Magnesium Glycinate 300mg before bed.",
            priority  = NotificationCompat.PRIORITY_DEFAULT
        )
        return Result.success()
    }
}

// ─── Notification Scheduler ───────────────────────────────────────────────────

object NotificationScheduler {

    fun scheduleMorningReminder(context: Context, hour: Int = 7, minute: Int = 0, day: Int = 1) {
        val data = workDataOf(MorningReminderWorker.KEY_DAY to day)

        val request = PeriodicWorkRequestBuilder<MorningReminderWorker>(
            24, TimeUnit.HOURS
        )
            .setInputData(data)
            .setInitialDelay(calculateInitialDelay(hour, minute), TimeUnit.MILLISECONDS)
            .setConstraints(Constraints.Builder().build())
            .addTag(MorningReminderWorker.WORK_NAME)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                MorningReminderWorker.WORK_NAME,
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
    }

    fun scheduleWaterReminders(context: Context, intervalHours: Int = 2) {
        val request = PeriodicWorkRequestBuilder<WaterReminderWorker>(
            intervalHours.toLong(), TimeUnit.HOURS
        )
            .setInitialDelay(intervalHours.toLong(), TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "water_reminder",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
    }

    fun scheduleEveningSupplements(context: Context, hour: Int = 19, minute: Int = 0) {
        val request = PeriodicWorkRequestBuilder<SupplementReminderWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(calculateInitialDelay(hour, minute), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniquePeriodicWork(
                "supplement_reminder",
                ExistingPeriodicWorkPolicy.UPDATE,
                request
            )
    }

    fun cancelAll(context: Context) {
        WorkManager.getInstance(context).cancelAllWork()
    }

    private fun calculateInitialDelay(hour: Int, minute: Int): Long {
        val now = java.util.Calendar.getInstance()
        val target = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, hour)
            set(java.util.Calendar.MINUTE, minute)
            set(java.util.Calendar.SECOND, 0)
        }
        if (target.before(now)) {
            target.add(java.util.Calendar.DAY_OF_MONTH, 1)
        }
        return target.timeInMillis - now.timeInMillis
    }
}

// ─── Notification Helper ──────────────────────────────────────────────────────

private fun showNotification(
    context  : Context,
    id       : Int,
    channelId: String,
    title    : String,
    body     : String,
    priority : Int = NotificationCompat.PRIORITY_DEFAULT
) {
    val tapIntent = PendingIntent.getActivity(
        context, 0,
        Intent(context, MainActivity::class.java),
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    val notification = NotificationCompat.Builder(context, channelId)
        .setSmallIcon(android.R.drawable.ic_dialog_info) // Replace with actual icon
        .setContentTitle(title)
        .setContentText(body)
        .setStyle(NotificationCompat.BigTextStyle().bigText(body))
        .setPriority(priority)
        .setContentIntent(tapIntent)
        .setAutoCancel(true)
        .build()

    try {
        NotificationManagerCompat.from(context).notify(id, notification)
    } catch (e: SecurityException) {
        // POST_NOTIFICATIONS permission not granted
    }
}

// ─── Boot Receiver ────────────────────────────────────────────────────────────

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-schedule notifications after device reboot
            NotificationScheduler.scheduleMorningReminder(context)
            NotificationScheduler.scheduleWaterReminders(context)
            NotificationScheduler.scheduleEveningSupplements(context)
        }
    }
}
