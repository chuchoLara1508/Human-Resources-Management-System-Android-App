package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Departamento.newInstance] factory method to
 * create an instance of this fragment.
 */
class Departamento(codigo:String,rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
     lateinit var clave:String
     var nombre:String=""
     var descripcion:String=""
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
        val view:View=inflater.inflate(R.layout.fragment_departamento, container, false)
        var boton:Button
        var clean:Button
        var nombre:EditText
        var descripcion:EditText
        var nombreconsultado=""
        nombre=view.findViewById(R.id.departamento)
        descripcion=view.findViewById(R.id.descripcion)
        boton=view.findViewById(R.id.registro)
        clean=view.findViewById(R.id.limpiar)
        var consulta=view.findViewById<Button>(R.id.consulta_depto)
        var fuenteprueba: Typeface
        var fuente1:String="fuentes/fuente1.ttf"
        fuenteprueba=Typeface.createFromAsset(getActivity()?.getAssets() ,fuente1)
        nombre.setTypeface(fuenteprueba)
        descripcion.setTypeface(fuenteprueba)
        boton.setTypeface(fuenteprueba)
        clean.setTypeface(fuenteprueba)
        consulta.setTypeface(fuenteprueba)
       if(!this.clave.equals("")){
           boton.setText("Editar")
           getActivity()?.let {
               this.datosDepartamento(it,{
                    nombre.setText(it[0].toString())
                   nombreconsultado=it[0].toString()
                   descripcion.setText(it[1].toString())
               })
           }
       }
        boton.setOnClickListener{
           //Para registrar el departamento

            if(nombre.text.toString().equals("")){
                Toast.makeText(getActivity(),"Por favor ingrese un nombre",Toast.LENGTH_LONG).show()
            }
            else{
                this.nombre = nombre.text.toString()
                this.descripcion = descripcion.text.toString()
                var coincidencias=0
                getActivity()?.let { it1 ->
                    this.nombresDepartamentos(it1,{
                        if(it.size>0) {
                            println("Nombre de la clave de consulta que llegó: "+nombreconsultado)
                            if(this.clave.equals("")){
                                for(i in 0..it.size-1) {
                                    if (!it[i].toString().equals("")) {
                                        if (this.nombre.uppercase().equals(it[i].toString().uppercase())) {
                                            coincidencias++
                                        }
                                    }
                                }
                            }
                            else{
                                if(!nombreconsultado.uppercase().equals(this.nombre.uppercase())){
                                    for(i in 0..it.size-1) {
                                        if (!it[i].toString().equals("")) {
                                            if (this.nombre.uppercase().equals(it[i].toString().uppercase())) {
                                                coincidencias++
                                            }
                                        }
                                    }
                                }
                            }
                            if(coincidencias>0){
                                Toast.makeText(getActivity(),"El departamento ya está registrado",Toast.LENGTH_LONG).show()
                                nombre.setText("")
                                descripcion.setText("")
                            }
                            else{
                                if(!this.clave.equals("")){
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
                                else {
                                    getActivity()?.let { it1 -> registroDepto(it1) }
                                }
                            }
                        }
                    })
                }
            }
        }
        clean.setOnClickListener{
            nombre.setText("")
            descripcion.setText("")
        }
        consulta.setOnClickListener {
            var principal=(activity as Principal)
            principal.replaceFragment(ConsultaDepartamento(this.claveRol),"Consulta de departamentos")
        }
        return view
    }

    fun eliminaDepto(context: Context,clave:String){
        var cone=Conexiones(context)
        var arregloValores= arrayListOf<Any>(clave)
        var arregloNombres= arrayListOf<Any>("clave")
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlQuitaDepto.php",context)
    }

    fun aceptar() {
        getActivity()?.let { it2 -> this.editaDepto(it2) }
    }

    fun cancelar() {
        val t:Toast=Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG)
        t.show()
    }

    fun datosDepartamento(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtener("rh/control/obtenerDatosDepa.php",nombres,valores,context,this.clave,"clave",{
            callbacks(it)
        })
    }

    fun nombresDepartamentos(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("0","","","")
        var nombres= arrayListOf<Any>("palabra","ordenar","pagina","tabla")
        cone.obtenerDatos("rh/control/nombreDepa.php",nombres,valores,context,{
            callbacks(it)
        })
    }
    fun datosDepartamentos(context: Context,callbacks:(objeto:ArrayList<Array<Any>>) -> Unit,palabra:String,ordenado:Int){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(palabra,ordenado)
        var nombres= arrayListOf<Any>("palabra","ordenado")
        cone.tabla("rh/control/departamentos.php",nombres,valores,context,{
            callbacks(it)
        })

    }
    fun registroDepto(context: Context){
        this.clave=this.claveAlea(5)
        var cone=Conexiones(context)
        var arregloNombres= arrayListOf<Any>("clave","nombre","descripcion")
        var arregloValores= arrayListOf<Any>(this.clave,this.nombre,this.descripcion)
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlRegDepto.php",context)
        this.clave=""
    }

    fun editaDepto(context: Context){
        var cone=Conexiones(context)
        var arregloNombres= arrayListOf<Any>("clave","nombre","descripcion")
        var arregloValores= arrayListOf<Any>(this.clave,this.nombre,this.descripcion)
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlEditDepto.php",context)
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
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Departamento.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Departamento("","").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}