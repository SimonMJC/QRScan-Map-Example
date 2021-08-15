package com.joopda.qrscanexample.components

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MarkerEventListener(val context: Context): MapView.POIItemEventListener {
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {}
    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
        //x DEPRECATED
    }
    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        val builder = AlertDialog.Builder(context)
        val itemList = arrayOf("매장정보", "매장상세위치", "취소")
        builder.setTitle("${p1?.itemName}")
        builder.setItems(itemList) { dialog, which ->
            when(which){
                0 -> Toast.makeText(context, "매장 정보", Toast.LENGTH_SHORT).show()
                1 -> p0?.removePOIItem(p1)
                2 -> dialog.dismiss()
            }
        }
        builder.show()

    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        //x 마커의 속성 중 isDraggable = true 일 때 마커를 이동시켰을 경우
    }
}