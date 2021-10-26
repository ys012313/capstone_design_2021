package com.cm.taxi.fcm


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.cm.taxi.L
import com.cm.taxi.MainActivity
import com.cm.taxi.R
import com.cm.taxi.util.Prefer
import com.cm.taxi.util.Prefer.KEY_PUSH_TOKEN
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random


class PushMessagingService : FirebaseMessagingService() {
    private val random = java.util.Random()

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "com.cm.taxi"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        L.i(":::::token = $token")
        Prefer.set(KEY_PUSH_TOKEN, token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        L.i(":::::::::::::[PUSH GET DATA] " + remoteMessage.data)


        if (remoteMessage.data.isNotEmpty()) {
            val data = remoteMessage.data
            val titleData: String = data["title"] ?: ""
            val messageData: String = data["body"] ?: ""

            L.i(":::titleData " + titleData)
            L.i(":::messageData " + messageData)
            onShowNotification(titleData, messageData);
        }

    }


    //앱이 B/G상에서 받는 푸쉬 노티피케이션과 동일한 노티 생성
    private fun onShowNotification(title: String, message: String) {
        L.i("::::::::::::[F/G Push]:::::::::::")
        val id: Int = random.nextInt(Int.MAX_VALUE)
        val context = applicationContext
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT
        )


        val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(Color.parseColor("#4195e6"))
            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_noti
                )
            ) //BitMap 이미지 요구
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
        L.e("id = $id")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setSmallIcon(R.drawable.ic_noti) //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            val channelName: CharSequence = getString(R.string.app_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                channelName,
                importance
            )
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        } else {

            builder.setSmallIcon(R.drawable.ic_noti)
        }
        notificationManager.notify(id, builder.build())
    }
}