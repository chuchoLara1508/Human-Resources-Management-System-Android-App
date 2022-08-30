package com.example.recursoshumanos

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class RecuperaCuenta : AppCompatActivity() {
     lateinit var correo:TextView
     lateinit var recuperar:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recupera_cuenta)
        correo=findViewById(R.id.correoRecupera)
        recuperar=findViewById(R.id.busca)
        var fuenteprueba: Typeface
        var fuente1:String="fuentes/fuente1.ttf"
        fuenteprueba=Typeface.createFromAsset(getAssets(),fuente1)
        correo.setTypeface(fuenteprueba)
        recuperar.setTypeface(fuenteprueba)
        recuperar.setOnClickListener {
            if(correo.text.toString().equals("")){
                Toast.makeText(this,"Ingrese un correo electrónico",Toast.LENGTH_LONG).show()
            }
            else{
                var usuario=Usuario("")
                usuario.obtenerCorreo(this,{
                    var coincidencias=0
                    for(i in 0..it.size-1){
                        if(it[i].toString().equals(correo.text.toString())){
                            coincidencias++
                        }
                    }
                    if(coincidencias==1){
                        this.enviaCorreo(this,correo.text.toString())
                    }
                    else{
                        Toast.makeText(this,"El correo electrónico no existe",Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
    fun enviaCorreo(context:Context,correo:String){
        var conex=Conexiones(context)
        var usua=Usuario("")
        var nuevaContra=usua.claveAlea(5)
        var nombres= arrayListOf<Any>("correo","contra")
        var valores= arrayListOf<Any>(correo,nuevaContra)
        conex.nuevo(valores,nombres,"rh/control/ctrlRecuperaCuenta.php",context)
    }
}