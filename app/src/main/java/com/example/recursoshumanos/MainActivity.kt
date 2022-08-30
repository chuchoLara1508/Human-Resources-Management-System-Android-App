package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var usuario=findViewById<EditText>(R.id.usuario)
        var contra=findViewById<EditText>(R.id.contra)
        var inicio=findViewById<Button>(R.id.inicio)
        var olvida=findViewById<TextView>(R.id.olvide)
        //cambiar tipos de letra
        var fuenteprueba: Typeface
        var fuente1:String="fuentes/fuente1.ttf"
        fuenteprueba=Typeface.createFromAsset(getAssets(),fuente1)
        usuario.setTypeface(fuenteprueba)
        contra.setTypeface(fuenteprueba)
        olvida.setTypeface(fuenteprueba)
        var subrayado=SpannableString("¿Olvidaste tu contraseña?")
        subrayado.setSpan(UnderlineSpan(),0,subrayado.length,0)
        olvida.setText(subrayado)
        olvida.setOnClickListener {
            olvida.setTextColor(Color.rgb(46, 204, 113))
            var intento=Intent(this,RecuperaCuenta::class.java)
            startActivity(intento)
        }
        inicio.setTypeface(fuenteprueba)
        inicio.setOnClickListener(){
            if(usuario.text.toString().equals("")||contra.text.toString().equals("")){
                Toast.makeText(this,"Por favor ingrese todos los datos",Toast.LENGTH_LONG).show()
            }
            else{
                //validar aquí que el usuario sea correcto
                var usua=Usuario("")
                usua.usuario=usuario.text.toString()
                usua.contra=contra.text.toString()
                usua.validaSesion(this,{
                    if(it[0].toString().toInt()==1) {
                        println("Es uno")
                        var carga = android.content.Intent(this, com.example.recursoshumanos.Principal::class.java)
                        carga.putExtra("usua", it[3].toString())
                        usua.buscaRol(this,usua.usuario,{
                            carga.putExtra("rol",it[0].toString())
                            usua.buscarClaveRol(this,usua.usuario,{
                                carga.putExtra("ClaveRol",it[0].toString())
                                startActivity(carga)
                                finish()
                            })
                        })
                    }
                    else{
                        var alerta= androidx.appcompat.app.AlertDialog.Builder(this)
                        alerta.setMessage("Usuario y/o contraseña incorrectos")
                        alerta.setPositiveButton("Aceptar"){dialog,which->
                            findViewById<EditText>(R.id.contra).setText("")
                        }
                        alerta.show()
                    }
                })
            }
        }
    }
}