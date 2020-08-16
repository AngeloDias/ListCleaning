package br.com.training.android.listcleaning

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action.equals(MyService.emailThreadCleaningAction)) {
            context.startService(Intent(context, MyService::class.java))
        }
    }

}
