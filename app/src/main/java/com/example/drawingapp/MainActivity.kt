package com.example.drawingapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.media.Image
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {

    private lateinit var drawingView: DrawingView
    private lateinit var mImageButtonCurrentPaint:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutPaintColors:LinearLayout = findViewById(R.id.linearLayout)


        drawingView = findViewById(R.id.drawing_view)
        drawingView.setSizeforBrush(20.toFloat())
        mImageButtonCurrentPaint = linearLayoutPaintColors[0]  as ImageButton
        mImageButtonCurrentPaint.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.pallete_pressed))


        val brush: ImageButton = findViewById(R.id.brush)
        brush.setOnClickListener{
            brushSizeChooseDialog()
        }

    }

    private fun brushSizeChooseDialog(){
        val dialog:View = LayoutInflater.from(this).inflate(R.layout.dialog_brushsize , null)
        val builder:AlertDialog = AlertDialog.Builder(this).create()
        builder.setView(dialog)

        val smallBtn:ImageButton = dialog.findViewById(R.id.smallBrush)
        val mediumBtn:ImageButton = dialog.findViewById(R.id.mediumBrush)
        val largeBtn:ImageButton = dialog.findViewById(R.id.largeBrush)

        smallBtn.setOnClickListener{
            drawingView.setSizeforBrush(10.toFloat())
            builder.dismiss()
        }

        mediumBtn.setOnClickListener{
            drawingView.setSizeforBrush(20.toFloat())
            builder.dismiss()
        }

        largeBtn.setOnClickListener{
            drawingView.setSizeforBrush(30.toFloat())
            builder.dismiss()
        }

        builder.show()

    }

    fun paintClicked(view: View){
        if(view != mImageButtonCurrentPaint){
            val imageButton = view as ImageButton
            val colorTag = imageButton.tag.toString()
            drawingView.setColor(colorTag)
            imageButton.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.pallete_pressed))
            mImageButtonCurrentPaint.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.pallet_normal))

            mImageButtonCurrentPaint = view
        }
    }
}