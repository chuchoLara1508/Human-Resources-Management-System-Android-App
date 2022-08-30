package com.example.recursoshumanos

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.github.barteksc.pdfviewer.listener.OnErrorListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class PDF() : AppCompatActivity() {
    var nomina:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        var clv=getIntent().getStringExtra("clavePDF").toString()
        this.nomina=clv
        var cone=Conexiones(this)
        this.muestraPDF(this, {
            var pdf:PDFView=findViewById(R.id.pdfView)
            var progreso:CircularProgressIndicator=findViewById(R.id.progress_circular)
            progreso.setVisibility(View.VISIBLE)
            var load=LoadpdffromUrl(pdf,progreso,this.nomina,this)
            LoadpdffromUrl(pdf,progreso,this.nomina,this).execute(cone.rutaP+"rh/nominas/"+this.nomina+".pdf")
        })
    }

    class LoadpdffromUrl(pdfV: PDFView,progress_circu:CircularProgressIndicator,clave:String,contexto: Context) : AsyncTask<String?, Void?, InputStream?>(),
        SoundPool.OnLoadCompleteListener, MediaPlayer.OnErrorListener, OnLoadCompleteListener, OnErrorListener {
        lateinit var pdfView:PDFView
        lateinit var nomina:String
        lateinit var progress_circular: CircularProgressIndicator
        lateinit var context: Context
        init{
            this.pdfView=pdfV
            this.nomina=clave
            this.progress_circular=progress_circu
            this.context=contexto
        }
         override fun doInBackground(vararg p0: String?): InputStream? {
            var inputStream: InputStream? = null
             var cone=Conexiones(this.context)
            try {
                val url = URL(cone.rutaP+"rh/nominas/"+this.nomina+".pdf")
                val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
                if (urlConnection.responseCode == 200) {
                    inputStream = BufferedInputStream(urlConnection.inputStream)
                }
            } catch (e: IOException) {
                //method to handle errors.
                e.printStackTrace()
                return null
            }
            return inputStream
        }

        override fun onPostExecute(inputStream: InputStream?) {
            //after the executing async task we load pdf in to pdfview.
            pdfView.fromStream(inputStream).onLoad(this).onError(this).load()
        }

        override fun onError(t: Throwable?) {
            progress_circular.setVisibility(View.GONE);
        }

        override fun loadComplete(nbPages: Int) {
            progress_circular.setVisibility(View.GONE);
        }

        override fun onLoadComplete(p0: SoundPool?, p1: Int, p2: Int) {
            progress_circular.setVisibility(View.GONE);
        }

        override fun onError(p0: MediaPlayer?, p1: Int, p2: Int): Boolean {
            TODO("Not yet implemented")
        }

    }

    fun muestraPDF(context: Context,callbacks:(bandera:Boolean)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(this.nomina)
        var nombres= arrayListOf<Any>("clave")
        cone.creaPDF(valores,nombres,"rh/control/nomina.php",context,{
            callbacks(it)
        })
    }

}