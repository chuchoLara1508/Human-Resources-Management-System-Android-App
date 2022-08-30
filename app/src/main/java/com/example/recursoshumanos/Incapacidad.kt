package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Incapacidad.newInstance] factory method to
 * create an instance of this fragment.
 */
class Incapacidad(codigo:String,rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var clave:String
    var nombre:String=""
    var desc:Int=0
    lateinit var claveRol:String
    init{
        this.clave=codigo
        this.claveRol=rol
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_incapacidad, container, false)
        var nombre:EditText=view.findViewById(R.id.incapacidad)
        var desc:EditText=view.findViewById(R.id.descuento)
        var registro:Button=view.findViewById(R.id.registro)
        var limpiar:Button=view.findViewById(R.id.limpiar)
        var consulta=view.findViewById<Button>(R.id.consulta_inc)
        var nombreconsultado=""
        if(!this.clave.equals("")){
            registro.setText("Editar")
            getActivity()?.let {
                this.datosInc(it,{
                    nombreconsultado=it[0].toString()
                    nombre.setText(it[0].toString())
                    desc.setText(it[1].toString())
                })
            }
        }
        registro.setOnClickListener {
            if(nombre.text.toString().equals("")||desc.text.toString().equals("")){
                Toast.makeText(getActivity(),"Por favor ingrese un nombre y descuento",Toast.LENGTH_LONG).show()
            }
            else{
                if(desc.text.toString().toInt()>=0&&desc.text.toString().toInt()<=100){
                    this.nombre=nombre.text.toString()
                    this.desc=desc.text.toString().toInt()
                    var nomina=Nomina("","")
                    var coincidencias=0
                    getActivity()?.let { it1 ->
                        nomina.buscarIncapacidades(it1,{
                            if(this.clave.equals("")){
                                for(i in 0..it.size-1){
                                    if(nombre.text.toString().uppercase().equals(it[i].toString().uppercase())){
                                        coincidencias++
                                    }
                                }
                                if(coincidencias>0){
                                    nombre.setText("")
                                    desc.setText("")
                                    Toast.makeText(getActivity(),"La incapacidad ya existe, no registrada",Toast.LENGTH_LONG).show()
                                }
                                else{
                                    getActivity()?.let { it1 -> this.registroIncapacidad(it1) }
                                }
                            }
                            else{
                                if(!nombreconsultado.uppercase().equals(this.nombre.uppercase())){
                                    println("Entra a la condición de registro")
                                    for(i in 0..it.size-1){
                                        if (!it[i].toString().equals("")) {
                                            if (this.nombre.uppercase().equals(it[i].toString().uppercase())) {
                                                coincidencias++
                                            }
                                        }
                                    }
                                    if(coincidencias>0){
                                        nombre.setText("")
                                        desc.setText("")
                                        Toast.makeText(getActivity(),"La incapacidad ya existe",Toast.LENGTH_LONG).show()
                                    }
                                    else{
                                        val dialogo1: AlertDialog.Builder = AlertDialog.Builder(getActivity())
                                        dialogo1.setTitle("Confirmación de editado")
                                        dialogo1.setMessage("¿Desea editar este registro?")
                                        dialogo1.setCancelable(false)
                                        dialogo1.setPositiveButton("Confirmar",
                                            DialogInterface.OnClickListener {
                                                    dialogo1, id -> aceptar()
                                            })
                                        dialogo1.setNegativeButton("Cancelar",
                                            DialogInterface.OnClickListener { dialogo1, id -> cancelar() })
                                        dialogo1.show()
                                    }
                                }
                                else{
                                    val dialogo1: AlertDialog.Builder = AlertDialog.Builder(getActivity())
                                    dialogo1.setTitle("Confirmación de editado")
                                    dialogo1.setMessage("¿Desea editar este registro?")
                                    dialogo1.setCancelable(false)
                                    dialogo1.setPositiveButton("Confirmar",
                                        DialogInterface.OnClickListener {
                                                dialogo1, id -> aceptar()
                                        })
                                    dialogo1.setNegativeButton("Cancelar",
                                        DialogInterface.OnClickListener { dialogo1, id -> cancelar() })
                                    dialogo1.show()
                                }
                            }
                        })
                    }
                }
                else{
                    Toast.makeText(getActivity(),"El porcentaje de descuento debe oscilar entre 0 y 100",Toast.LENGTH_LONG).show()
                }
            }
        }
        limpiar.setOnClickListener {
            nombre.setText("")
            desc.setText("")
        }
        consulta.setOnClickListener {
            var principal=(activity as Principal)
            principal.replaceFragment(ConsultaIncapacidad(this.claveRol),"Consulta de incapacidades")
        }
        return view
    }

    fun datosIncapacidades(context: Context,callbacks:(objeto:ArrayList<Array<Any>>) -> Unit,palabra:String,rango:Int){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(palabra,rango)
        var nombres= arrayListOf<Any>("palabra","rango")
        cone.tabla("rh/control/incapacidades.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun eliminaIncapacidad(context: Context,clave:String){
        var cone=Conexiones(context)
        var arregloValores= arrayListOf<Any>(clave)
        var arregloNombres= arrayListOf<Any>("clave")
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlQuitaInc.php",context)
    }

    fun aceptar() {
        getActivity()?.let { it2 -> this.editaIncapacidad(it2) }
    }

    fun cancelar() {
        val t:Toast=Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG)
        t.show()
    }

    fun datosInc(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("oiqwe","palitos","descripcion de los palitos")
        var nombres= arrayListOf<Any>("clave","nombre","descripcion")
        cone.obtener("rh/control/obtenerDatosInc.php",nombres,valores,context,this.clave,"clave",{
            callbacks(it)
        })
    }
    fun claveAlea(max:Int):String{
        var cadena=""
        val caract="abcdefghijklmnopqrstuvwxyz0123456789."
        for(i in 1..max){
            var caracAle:Int= Random.nextInt(0,caract.length-1)
            cadena+=caract.substring(caracAle,caracAle+1)
        }
        return cadena
    }

    fun editaIncapacidad(context: Context){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(this.clave,this.nombre,this.desc)
        var nombres= arrayListOf<Any>("clave","nombre","descuento")
        cone.nuevo(valores,nombres,"rh/control/ctrlEditaIncapacidad.php",context)
    }

    fun registroIncapacidad(context: Context){
        this.clave=this.claveAlea(5)
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(this.clave,this.nombre,this.desc)
        var nombres= arrayListOf<Any>("clave","nombre","descuento")
        cone.nuevo(valores,nombres,"rh/control/ctrlRegIncapacidad.php",context)
        this.clave=""
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Incapacidad.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Incapacidad("","").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}