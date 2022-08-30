package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import kotlin.random.Random


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Puesto.newInstance] factory method to
 * create an instance of this fragment.
 */
class Puesto(codigo:String,rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var clave:String
    lateinit var nombre:String
    lateinit var descripcion:String
    lateinit var departamento:String
    var pago:Int=0
    lateinit var claveRol:String
    lateinit var claveDepto:String
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
       var view:View= inflater.inflate(R.layout.fragment_puesto, container, false)
        var nombre:EditText
        var descripcion:EditText
        var departamento:Spinner
        var pago:EditText
        var registrar:Button
        var limpiar:Button
        var nombreconsultado=""
        nombre=view.findViewById(R.id.puesto)
        descripcion=view.findViewById(R.id.desc)
        departamento=view.findViewById(R.id.departamentoLista)
        pago=view.findViewById(R.id.pago)
        registrar=view.findViewById(R.id.registro)
        limpiar=view.findViewById(R.id.limpiar)
        var consulta=view.findViewById<Button>(R.id.consulta_puesto)
        var fuenteprueba: Typeface
        var fuente1:String="fuentes/fuente1.ttf"
        fuenteprueba= Typeface.createFromAsset(getActivity()?.getAssets() ,fuente1)
        nombre.setTypeface(fuenteprueba)
        descripcion.setTypeface(fuenteprueba)
        pago.setTypeface(fuenteprueba)
        registrar.setTypeface(fuenteprueba)
        limpiar.setTypeface(fuenteprueba)
        consulta.setTypeface(fuenteprueba)
        var claves= arrayListOf<Any>()
        getActivity()?.let {
            this.clavesDepartamentos(it,{
                if(!it[0].toString().equals("")){
                    for(i in 0..it.size-1){
                        claves.add(it[i].toString())
                    }
                }
            })
        }
        getActivity()?.let {
            this.nombresDepartamentos(it,{
                if(!it[0].toString().equals("")) {
                    var deptos = arrayListOf<Any>("Seleccione")
                    for(i in 0..it.size-1){
                         deptos.add(it[i].toString())
                    }
                    var adaptador =
                        getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, deptos) }
                    departamento.adapter = adaptador
                    if(!this.clave.equals("")){
                        registrar.setText("Editar")
                        getActivity()?.let {
                            this.datosPuesto(it,{
                                nombre.setText(it[0].toString())
                                nombreconsultado=it[0].toString()
                                descripcion.setText(it[1].toString())
                                for(i in 1..deptos.size-1){
                                    if(it[2].toString().equals(deptos[i].toString())){
                                        departamento.setSelection(i)
                                    }
                                }
                                pago.setText(it[3].toString())
                            })
                        }
                    }

                    registrar.setOnClickListener {
                        var indice:Int=departamento.selectedItemPosition
                        if (nombre.text.toString().equals("") || descripcion.text.toString().equals("") || pago.text.toString().equals("")||indice==0) {
                            Toast.makeText(getActivity(), "Seleccione un departamento y complete los campos", Toast.LENGTH_LONG)
                                .show()
                        }
                        else {
                            this.nombre=nombre.text.toString()
                            this.descripcion=descripcion.text.toString()
                            this.pago=pago.text.toString().toInt()
                            //this.departamento y this.claveDepto
                            for(i in  0..claves.size-1){
                                if(i==indice-1){
                                    this.departamento=deptos[i+1].toString()
                                    this.claveDepto=claves[i].toString()
                                }
                            }
                            var coincidencias=0
                            getActivity()?.let { it1 ->
                                this.nombresPuestos(it1,{
                                    if(it.size>0){
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
                                            Toast.makeText(getActivity(),"El puesto ya está registrado",Toast.LENGTH_LONG).show()
                                            nombre.setText("")
                                            descripcion.setText("")
                                            departamento.setSelection(0)
                                            pago.setText("")
                                        }
                                        else{
                                            if(this.pago>=20&&this.pago<=100) {
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
                                                else{
                                                    getActivity()?.let { it1 -> registroPuesto(it1) }
                                                }
                                            }
                                            else{
                                                Toast.makeText(getActivity(),"El pago deberá estar entre 20 y 100",Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
                                })
                            }
                        }
                    }
                    limpiar.setOnClickListener {
                        nombre.setText("")
                        descripcion.setText("")
                        pago.setText("")
                        departamento.setSelection(0)
                    }
                    consulta.setOnClickListener {
                        var principal=(activity as Principal)
                        principal.replaceFragment(ConsultaPuesto(this.claveRol),"Consulta de puestos")
                    }
                }
                else{
                    Toast.makeText(getActivity(), "No hay departamentos registrados", Toast.LENGTH_LONG).show()
                    registrar.isEnabled=false
                }
            })
        }
        return view
    }

    fun datosPuestos(context: Context,callbacks:(objeto:ArrayList<Array<Any>>) -> Unit,palabra:String,rango:Int){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(palabra,rango)
        var nombres= arrayListOf<Any>("palabra","rango")
        cone.tabla("rh/control/puestos.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun eliminaPuesto(context: Context,clave:String){
        var cone=Conexiones(context)
        var arregloValores= arrayListOf<Any>(clave)
        var arregloNombres= arrayListOf<Any>("clave")
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlQuitaPuesto.php",context)
    }

    fun aceptar() {
        getActivity()?.let { it2 -> this.editaPuesto(it2) }
    }

    fun cancelar() {
        val t:Toast=Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG)
        t.show()
    }

    fun datosPuesto(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtener("rh/control/obtenerDatosPuesto.php",nombres,valores,context,this.clave,"clave",{
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

    fun nombresPuestos(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("oiqwe","palitos","descripcion de los palitos")
        var nombres= arrayListOf<Any>("clave","nombre","descripcion")
        cone.obtenerDatos("rh/control/nombrePuesto.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun clavesDepartamentos(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("0","","","")
        var nombres= arrayListOf<Any>("palabra","ordenar","pagina","tabla")
        cone.obtenerDatos("rh/control/claveDepa.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun editaPuesto(context: Context){
        var cone=Conexiones(context)
        var arregloNombres= arrayListOf<Any>("clave","nombre","descripcion","departamento","pago","clvDepto")
        var arregloValores= arrayListOf<Any>(this.clave,this.nombre,this.descripcion,this.departamento,this.pago,this.claveDepto)
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlEditPuesto.php",context)
    }

    fun registroPuesto(context: Context){
        this.clave=this.claveAlea(5)
        var cone=Conexiones(context)
        var arregloNombres= arrayListOf<Any>("clave","nombre","descripcion","departamento","pago","clvDepto")
        var arregloValores= arrayListOf<Any>(this.clave,this.nombre,this.descripcion,this.departamento,this.pago,this.claveDepto)
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlRegPuesto.php",context)
        this.clave=""
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
         * @return A new instance of fragment Puesto.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Puesto("","").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}