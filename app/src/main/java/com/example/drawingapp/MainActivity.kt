package com.example.drawingapp

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var openGallery:ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult())

        {
            result ->
                if (result.resultCode == RESULT_OK && result.data!=null) {
                    val imageBack: ImageView = findViewById(R.id.iv_background)
                    imageBack.setImageURI(result.data?.data)
                }
        }

    private val requestPermission:ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value

                if (isGranted) {
//                    if (permissionName == android.Manifest.permission.READ_EXTERNAL_STORAGE) {
//                        Toast.makeText(
//                            applicationContext,
//                            "Permission Granted for Storage",
//                            Toast.LENGTH_LONG
//                        ).show()

                    val pickIntent: Intent = Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    openGallery.launch(pickIntent)

                    } else {
                        if (permissionName == Manifest.permission.READ_EXTERNAL_STORAGE) {
                            Toast.makeText(
                                applicationContext,
                                "Permission Denied!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }

    val camaraAndLocationResult:ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()){
            permissions ->
                permissions.entries.forEach{
                    val permissionName = it.key
                    val isGranted = it.value

                    if(isGranted){
                        if (permissionName == android.Manifest.permission.ACCESS_FINE_LOCATION){
                            Toast.makeText(applicationContext , "Permission Granted for Location" , Toast.LENGTH_LONG).show()
                        }
                        else if(permissionName == android.Manifest.permission.ACCESS_COARSE_LOCATION){
                            Toast.makeText(applicationContext , "Permission Granted For Coarse Location" , Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(applicationContext , "Permission Granted for Camara!" , Toast.LENGTH_LONG).show()
                        }
                    }
                    else{
                        if (permissionName == android.Manifest.permission.ACCESS_FINE_LOCATION){
                            Toast.makeText(applicationContext , "Permission denied Location" , Toast.LENGTH_LONG).show()
                        }
                        else if(permissionName == android.Manifest.permission.ACCESS_COARSE_LOCATION){
                            Toast.makeText(applicationContext , "Permission Denied  Coarse Location" , Toast.LENGTH_LONG).show()
                        }
                        else{
                            Toast.makeText(applicationContext , "Permission denied Camara!" , Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

    private lateinit var drawingView: DrawingView
    private lateinit var mImageButtonCurrentPaint:ImageButton
    private lateinit var imagePicker:ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutPaintColors:LinearLayout = findViewById(R.id.linearLayout)
        val imagePicker:ImageButton = findViewById(R.id.imagePicker)

        imagePicker.setOnClickListener{

            //Permission for Camara and Location

//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M && shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)){
//                showRationalDialog("Permission Demo requires camara access" , "Camara Cannot be used because Camara access is denied" )
//            }
//            else{
//            camaraAndLocationResult.launch(arrayOf(android.Manifest.permission.CAMERA , android.Manifest.permission.ACCESS_FINE_LOCATION))
//            }
            requestStoragePermission()
        }

        undo.setOnClickListener{
            drawingView.onClickUndo()
        }

        drawingView = findViewById(R.id.drawing_view)
        drawingView.setSizeforBrush(20.toFloat())
        mImageButtonCurrentPaint = linearLayoutPaintColors[0]  as ImageButton
        mImageButtonCurrentPaint.setImageDrawable(ContextCompat.getDrawable(this , R.drawable.pallete_pressed))


        val brush: ImageButton = findViewById(R.id.brush)
        brush.setOnClickListener{
            brushSizeChooseDialog()
        }

    }

    private fun requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.READ_EXTERNAL_STORAGE)){
            showRationalDialog("Kids Drawing App" , "Kids Drawing App" + "needs to Access Your External Storage")
        }
        else{
            requestPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
        }
    }

    fun brushSizeChooseDialog(){
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

    private fun showRationalDialog(title:String, message:String){
        val builder:AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
            .setPositiveButton("Cancel")
            {dialog , _->dialog.dismiss()}

        builder.create().show()
    }

}




