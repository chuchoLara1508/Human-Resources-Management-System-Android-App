package com.example.recursoshumanos

import android.content.Context
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class Conexiones(context: Context) {
    lateinit var rutaP:String
    var queue: RequestQueue
    init{
        rutaP="http://192.168.42.140/"
        queue=Volley.newRequestQueue(context)
    }
    fun nuevo(valores:ArrayList<Any>,nombres:ArrayList<Any>, rutaScript:String,context: Context){
        var arrNom:ArrayList<Any> = nombres
        //arreglo sear llenado con los valores que se usaran para el registro, es una lista de valores
        val arreglo:ArrayList<Any> = valores
        //la constante queue es un objeto de la clase Volley implmentando el metodo newRequestQueue
        val queue = Volley.newRequestQueue(context)
        //determinamos la ruta sel script que sera ejecutado
        val ruta = rutaP+""+rutaScript
        val stringRequest = object: StringRequest(
            Request.Method.POST,
            ruta,
            Response.Listener { response ->
                var strResp = response.toString()
                Toast.makeText(context, ""+strResp, Toast.LENGTH_LONG).show()
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, ""+error.toString(), Toast.LENGTH_LONG).show()
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                for(i in 0..(arreglo.size-1)){
                    params.put(""+arrNom[i].toString(), ""+arreglo[i].toString())
                }
                return params
            }
        }
        stringRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })
        queue.add(stringRequest)
    }//cierre de la funcion nuevo

    fun obtenerDatos(ruta:String, campos:ArrayList<Any>, valores:ArrayList<Any>, context: Context, callbacks:(arreglo:ArrayList<Any>) -> Unit){
        var entrega = arrayListOf<Any>()
        var queue = Volley.newRequestQueue(context)
        var param = HashMap<String, String>()
        for(params in 0 ..(campos.size-1)){
            param.put(""+campos[params].toString(), ""+valores[params].toString())
        }
        var datos = JSONObject(param as Map<*, *>)
        var ruta = rutaP+""+ruta
        println("Rut: "+ruta.toString())
        var jsonObject = JsonObjectRequest(
            Request.Method.POST,
            ruta,
            datos,
            Response.Listener { response ->
                var objeto = JSONObject(response.toString())
                var l=0
                var fila = objeto.getJSONObject(l.toString())
                    var unitario = JSONObject(fila.toString())
                    for (posi in 0..(unitario.length() - 1)) {
                        entrega.add(unitario.get(posi.toString()))
                    }
                    callbacks(entrega)
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "Error-"+error.toString(), Toast.LENGTH_LONG).show()
            }
        )//cierre del jsonObjectRequest
        queue.add(jsonObject)
    }//termina validaRegistro

    fun obtener(ruta:String, campos:ArrayList<Any>, valores:ArrayList<Any>, context: Context,valor:String,cadena:String, callbacks:(arreglo:ArrayList<Any>) -> Unit){
        var entrega = arrayListOf<Any>()
        var queue = Volley.newRequestQueue(context)
        var param = HashMap<String, String>()
        for(params in 0 ..(campos.size-1)){
            param.put(""+campos[params].toString(), ""+valores[params].toString())
        }
        var datos = JSONObject(param as Map<*, *>)
        var ruta = rutaP+""+ruta+"?"+cadena+"="+valor
        println(ruta)
        var jsonObject = JsonObjectRequest(
            Request.Method.GET,
            ruta,
            datos,
            Response.Listener { response ->
                var objeto = JSONObject(response.toString())
                var l=0
                var fila = objeto.getJSONObject(l.toString())
                var unitario = JSONObject(fila.toString())
                for (posi in 0..(unitario.length() - 1)) {
                    entrega.add(unitario.get(posi.toString()))
                }
                callbacks(entrega)
            },
            Response.ErrorListener { error ->
                Toast.makeText(context, "Error-"+error.toString(), Toast.LENGTH_LONG).show()
            }
        )//cierre del jsonObjectRequest
        queue.add(jsonObject)
    }//termina validaRegistro

    fun tabla(ruta:String, campos:ArrayList<Any>, valores:ArrayList<Any>, context: Context, callbacks:(objeto:ArrayList<Array<Any>>) -> Unit){
        var tabla:ArrayList<Array<Any>> = arrayListOf<Array<Any>>()
        var queue = Volley.newRequestQueue(context)
        var param = HashMap<String, String>()
        for(params in 0 ..(campos.size-1)){
            param.put(""+campos[params].toString(), ""+valores[params].toString())
        }
        var datos = JSONObject(param as Map<*, *>)
        var ruta = rutaP+""+ruta
        var jsonObject = JsonObjectRequest(
            Request.Method.POST,
            ruta,
            datos,
            Response.Listener { response ->
                var arreglo = JSONObject(response.toString())
                var l=0
                while(l<arreglo.length()) {
                    var fila = arreglo.getJSONObject(l.toString())
                    var unitario = JSONObject(fila.toString())
                    var entrega= arrayListOf<Any>()
                    for(posi in 0..(unitario.length()-1)){
                        entrega.add(unitario.get(posi.toString()))
                    }
                    l++
                    tabla.add(entrega.toTypedArray())
                }
                callbacks(tabla)
            },
            Response.ErrorListener { error ->
                callbacks(tabla)
            }
        )//cierre del jsonObjectRequest
        jsonObject.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })
        queue.add(jsonObject)
    }//termina validaRegistro

    fun creaPDF(valores:ArrayList<Any>,nombres:ArrayList<Any>, rutaScript:String,context: Context,callbacks:(bandera:Boolean)->Unit){
        var arrNom:ArrayList<Any> = nombres
        val arreglo:ArrayList<Any> = valores
        val queue = Volley.newRequestQueue(context)
        val ruta = rutaP+""+rutaScript
        val stringRequest = object: StringRequest(
            Request.Method.POST,
            ruta,
            Response.Listener { response ->
                var strResp = response.toString()
                callbacks(response.toBoolean())
            },
            Response.ErrorListener { error ->
                callbacks(false)
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                for(i in 0..(arreglo.size-1)){
                    params.put(""+arrNom[i].toString(), ""+arreglo[i].toString())
                }
                return params
            }
        }
        stringRequest.setRetryPolicy(object : RetryPolicy {
            override fun getCurrentTimeout(): Int {
                return 50000
            }

            override fun getCurrentRetryCount(): Int {
                return 50000
            }

            @Throws(VolleyError::class)
            override fun retry(error: VolleyError) {
            }
        })
        queue.add(stringRequest)
    }
}