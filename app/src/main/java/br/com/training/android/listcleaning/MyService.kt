package br.com.training.android.listcleaning

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import br.com.training.android.listcleaning.utils.RemoveDupsOnEmailThread
import br.com.training.android.listcleaning.utils.SinglyLinkedList

class MyService : Service() {
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private var receiver: MyReceiver? = null

    companion object {
        const val emailThreadTag = "emailThread"
        const val emailThreadCleaningAction = "br.com.training.android.listcleaning.MyService.EMAIL_ACTION"
    }

    override fun onCreate() {
        super.onCreate()

        receiver = MyReceiver()

        val filter = IntentFilter()

        filter.addAction(emailThreadCleaningAction)

        registerReceiver(receiver, filter)

        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
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
                    msg.data.putParcelable(emailThreadTag, list)
                }

            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt()
            }

            stopSelf(msg.arg1)
        }
    }

}
