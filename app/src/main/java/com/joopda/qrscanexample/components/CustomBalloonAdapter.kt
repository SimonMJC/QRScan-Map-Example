package com.joopda.qrscanexample.components

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.joopda.qrscanexample.R
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import java.util.zip.Inflater

open class CustomBalloonAdapter(inflater: LayoutInflater): CalloutBalloonAdapter {
    private val mCallOutBalloon: View = inflater.inflate(R.layout.custom_balloon, null)
    private val name: TextView = mCallOutBalloon.findViewById(R.id.ball_tv_name)
    private val address: TextView = mCallOutBalloon.findViewById(R.id.ball_tv_address)
    private val otherInfo: TextView = mCallOutBalloon.findViewById(R.id.ball_tv_other)
    override fun getCalloutBalloon(p0: MapPOIItem?): View {
        name.text = p0?.itemName
        address.text = "getCalloutBalloon"
        otherInfo.text = p0?.tag.toString()
        return mCallOutBalloon
    }

    override fun getPressedCalloutBalloon(p0: MapPOIItem?): View {
//        address.text = "getPressedCalloutBalloon"
        return mCallOutBalloon
    }
}