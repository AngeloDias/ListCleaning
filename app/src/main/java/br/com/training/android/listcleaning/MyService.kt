package br.com.training.android.listcleaning

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import br.com.training.android.listcleaning.utils.RemoveDupsOnEmailThread
import br.com.training.android.listcleaning.utils.SinglyLinkedList

class MyService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var receiver: BroadcastReceiver? = null

    companion object {
        const val emailThreadTag = "emailThread"
        const val emailAction = "br.com.training.android.listcleaning.MyService.EMAIL_ACTION"
        const val appPath = "br.com.training.android.listcleaning"
    }

    override fun onCreate() {
        super.onCreate()

//        receiver =

        val filter = IntentFilter()

        filter.addAction(emailAction)

//        registerReceiver()

        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        override fun handleMessage(msg: Message) {
            try {
                var list = msg.data.getParcelable<SinglyLinkedList<String>>(emailThreadTag)

                if (list != null) {
                    list = RemoveDupsOnEmailThread.removeDupsUsingPointers(list)
                }

            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }

            stopSelf(msg.arg1)
        }
    }

}
