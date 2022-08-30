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
 * Use the [Empleado.newInstance] factory method to
 * create an instance of this fragment.
 */
class Empleado(codigo:String,rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var clave:String
    var nombre:String=""
    var numero:String=""
    var curp:String=""
    var rfc:String=""
    var direccion:String=""
    var n_cuenta:String=""
    var puesto:String=""
    var fecha:String=""
    var nivel:String=""
    var genero:String=""
    var pais:String=""
    var clavePuesto=""
    var clavePais:Int=0
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
        var view=inflater.inflate(R.layout.fragment_empleado, container, false)
        var nombre:EditText
        var apellidoP:EditText
        var apellidoM:EditText
        var numeroTele:EditText
        var rfc:EditText
        var curp:EditText
        var calle:EditText
        var numeroInterior:EditText
        var numeroExterior:EditText
        var colonia:EditText
        var codigo:EditText
        var cuenta:EditText
        var puesto:Spinner
        var fecha:EditText
        var nivel:Spinner
        //genero queda pendiente
        var masculino=view.findViewById<RadioButton>(R.id.sex01)
        var femenino=view.findViewById<RadioButton>(R.id.sex02)
        var pais:Spinner
        var registro:Button
        var limpia:Button
        nombre=view.findViewById(R.id.nombre)
        apellidoP=view.findViewById(R.id.apep)
        apellidoM=view.findViewById(R.id.apem)
        numeroTele=view.findViewById(R.id.num)
        rfc=view.findViewById(R.id.rfc)
        curp=view.findViewById(R.id.curp)
        calle=view.findViewById(R.id.calle)
        numeroInterior=view.findViewById(R.id.numInt)
        numeroExterior=view.findViewById(R.id.numExt)
        colonia=view.findViewById(R.id.colonia)
        codigo=view.findViewById(R.id.codigo)
        cuenta=view.findViewById(R.id.cuenta)
        puesto=view.findViewById(R.id.puestoLista)
        fecha=view.findViewById(R.id.fecha)
        nivel=view.findViewById(R.id.nivelLista)
        pais=view.findViewById(R.id.paisLista)
        registro=view.findViewById(R.id.reg)
        limpia=view.findViewById(R.id.limpia)
        var consulta=view.findViewById<Button>(R.id.consulta_empleado)
        var fuenteprueba: Typeface
        var fuente1:String="fuentes/fuente1.ttf"
        fuenteprueba=Typeface.createFromAsset(getActivity()?.getAssets() ,fuente1)
        nombre.setTypeface(fuenteprueba)
        apellidoP.setTypeface(fuenteprueba)
        apellidoM.setTypeface(fuenteprueba)

        var arregloPuestos= arrayListOf<Any>("Seleccione")
        var arregloClavesPuestos= arrayListOf<Any>()
        var nivelEscolar= arrayListOf<Any>("Seleccione","Universidad","Maestría","Doctorado")
        var paises= arrayListOf<Any>("Seleccione")
        var arregloClavesPais= arrayListOf<Any>()

        if(!this.clave.equals("")){
            registro.setText("Editar")
            getActivity()?.let {
                this.datosEmpleado(it,{

                    var name=it[0].toString().split(" ").toTypedArray()
                    if(name.size==2){
                        apellidoP.setText(name[0])
                        nombre.setText(name[1])
                    }
                    if(name.size==3){
                        apellidoP.setText(name[0])
                        apellidoM.setText(name[1])
                        nombre.setText(name[2])
                    }
                    if(name.size==4){
                        apellidoP.setText(name[0])
                        apellidoM.setText(name[1])
                        nombre.setText(name[2]+" "+name[3])
                    }
                    if(name.size==5){
                        apellidoP.setText(name[0])
                        apellidoM.setText(name[1])
                        nombre.setText(name[2]+" "+name[3]+" "+name[4])
                    }

                    numeroTele.setText(it[1].toString())
                    curp.setText(it[2].toString())
                    rfc.setText(it[3].toString())
                    //direccion quedará desahbilitada
                    var etiqueta1:TextView=view.findViewById(R.id.eticalle)
                    var etiqueta2:TextView=view.findViewById(R.id.etinumext)
                    var etiqueta3:TextView=view.findViewById(R.id.etinumI)
                    var etiqueta4:TextView=view.findViewById(R.id.eticol)
                    var etiqueta5:TextView=view.findViewById(R.id.eticodigo)
                    etiqueta1.setText("Calle: ")
                    etiqueta2.setText("Número enterior: ")
                    etiqueta3.setText("Número ixterior: ")
                    etiqueta4.setText("Colonia: ")
                    etiqueta5.setText("Código postal: ")
                    calle.isEnabled=false
                    numeroInterior.isEnabled=false
                    numeroExterior.isEnabled=false
                    colonia.isEnabled=false
                    codigo.isEnabled=false
                    var puestoquellega=it[6].toString()
                    var nivelquellega=it[8].toString()
                    var paisquellega=it[10].toString()
                    getActivity()?.let {
                        this.clavePuestos(it,{
                            for(i in 0..it.size-1){
                                arregloClavesPuestos.add(it[i].toString())
                            }
                        })
                    }
                    getActivity()?.let {
                        this.nombresPuestos(it, {
                            for (i in 0..it.size - 1) {
                                arregloPuestos.add(it[i].toString())
                            }
                            var adaptador =
                                getActivity()?.let { it1 ->
                                    ArrayAdapter(
                                        it1,
                                        android.R.layout.simple_spinner_item,
                                        arregloPuestos
                                    )
                                }
                            puesto.adapter = adaptador
                            for(i in 1..arregloPuestos.size-1){
                                if(puestoquellega.equals(arregloPuestos[i].toString())){
                                    puesto.setSelection(i)
                                }
                            }
                        })
                    }

                    var adaptador=getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, nivelEscolar) }
                    nivel.adapter = adaptador
                    for(i in 1..nivelEscolar.size-1){
                        if(nivelquellega.equals(nivelEscolar[i].toString())){
                            nivel.setSelection(i)
                        }
                    }
                    getActivity()?.let {
                        this.nombrePais(it,{
                            for(i in 0..it.size-1){
                                paises.add(it[i].toString())
                            }
                            var adaptador=getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, paises) }
                            pais.adapter = adaptador
                            for(i in 1..paises.size-1){
                                if(paisquellega.equals(paises[i].toString())){
                                    pais.setSelection(i)
                                }
                            }
                        })
                    }

                    getActivity()?.let {
                        this.clavePais(it,{
                            for(i in 0..it.size-1){
                                arregloClavesPais.add(it[i].toString())
                            }
                        })
                    }
                    cuenta.setText(it[5].toString())

                    fecha.setText(it[7].toString())
                    if(it[9].toString().equals("Masculino")){
                        masculino.isChecked=true
                    }
                    if(it[9].toString().equals("Femenino")){
                        femenino.isChecked=true
                    }
                })
            }
        }
        else {
            getActivity()?.let {
                this.clavePuestos(it, {
                    for (i in 0..it.size - 1) {
                        arregloClavesPuestos.add(it[i].toString())
                    }
                })
            }
            getActivity()?.let {
                this.nombresPuestos(it, {
                    for (i in 0..it.size - 1) {
                        arregloPuestos.add(it[i].toString())
                    }
                    var adaptador =
                        getActivity()?.let { it1 ->
                            ArrayAdapter(
                                it1,
                                android.R.layout.simple_spinner_item,
                                arregloPuestos
                            )
                        }
                    puesto.adapter = adaptador
                })
            }
            var adaptador =
                getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, nivelEscolar) }
            nivel.adapter = adaptador

            getActivity()?.let {
                this.nombrePais(it, {
                    for (i in 0..it.size - 1) {
                        paises.add(it[i].toString())
                    }
                    var adaptador =
                        getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, paises) }
                    pais.adapter = adaptador
                })
            }

            getActivity()?.let {
                this.clavePais(it, {
                    for (i in 0..it.size - 1) {
                        arregloClavesPais.add(it[i].toString())
                    }
                })
            }
        }
        registro.setOnClickListener {
            var indicePuesto=0
            var indiceNivel=0
            var indicePais=0
            indicePuesto=puesto.selectedItemPosition
            indiceNivel=nivel.selectedItemPosition
            indicePais=pais.selectedItemPosition
            if(!this.clave.equals("")){
                if (
                    nombre.text.toString().equals("") || apellidoP.text.toString().equals("")
                    || numeroTele.text.toString().equals("") || cuenta.text.toString().equals("")
                    || indicePuesto == 0 || indiceNivel == 0 || indicePais == 0 || fecha.text.toString().equals("")
                ) {
                    Toast.makeText(getActivity(), "Por favor complete los campos para editar", Toast.LENGTH_LONG)
                        .show()
                } else {
                    this.nombre =
                        apellidoP.text.toString() + " " + apellidoM.text.toString() + " " + nombre.text.toString()//campo nombre
                    this.numero = numeroTele.text.toString()//telefono
                    this.curp = curp.text.toString().uppercase()//curp
                    this.rfc = rfc.text.toString().uppercase()//rfc
                    this.n_cuenta = cuenta.text.toString()//numero de cuenta
                    this.puesto = ""//puesto
                    this.fecha = fecha.text.toString()//fecha nacimiento
                    this.nivel = ""//nivel escolar
                    if (masculino.isChecked) {
                        this.genero = "Masculino"//genero
                    }
                    if (femenino.isChecked) {
                        this.genero = "Femenino"//genero
                    }
                    //encontrar puesto y clave
                    for (i in 0..arregloClavesPuestos.size - 1) {
                        if (i == indicePuesto - 1) {
                            this.puesto = arregloPuestos[i + 1].toString()
                            this.clavePuesto = arregloClavesPuestos[indicePuesto - 1].toString()
                        }
                    }
                    for (i in 0..nivelEscolar.size - 1) {
                        if (i == indiceNivel - 1) {
                            this.nivel = nivelEscolar[i + 1].toString()
                        }
                    }
                    for (i in 0..arregloClavesPais.size - 1) {
                        if (i == indicePais - 1) {
                            this.pais = paises[i + 1].toString()
                            this.clavePais = arregloClavesPais[i].toString().toInt()
                        }
                    }
                    //editar empleado si esto es cierto
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
            else {
                if (
                    nombre.text.toString().equals("") || apellidoP.text.toString().equals("")
                    || numeroTele.text.toString().equals("") || calle.text.toString().equals("")
                    || numeroExterior.text.toString().equals("") || colonia.text.toString().equals("")
                    || codigo.text.toString().equals("") || cuenta.text.toString().equals("")
                    || indicePuesto == 0 || indiceNivel == 0 || indicePais == 0 || fecha.text.toString().equals("")
                ) {
                    Toast.makeText(getActivity(), "Por favor complete todos los campos obligatorios", Toast.LENGTH_LONG)
                        .show()
                } else {
                    this.nombre =
                        apellidoP.text.toString() + " " + apellidoM.text.toString() + " " + nombre.text.toString()//campo nombre
                    this.numero = numeroTele.text.toString()//telefono
                    this.curp = curp.text.toString().uppercase()//curp
                    this.rfc = rfc.text.toString().uppercase()//rfc
                    if (!numeroInterior.text.toString().equals("")) {//direccion

                        this.direccion =
                            calle.text.toString() + " ext #" + numeroExterior.text.toString() + " int #" + numeroInterior.text.toString() + " Col." + colonia.text.toString() + " ,CP." + codigo.text.toString()
                    } else {
                        this.direccion =
                            calle.text.toString() + " ext #" + numeroExterior.text.toString() + " Col." + colonia.text.toString() + " ,CP." + codigo.text.toString()
                    }
                    this.n_cuenta = cuenta.text.toString()//numero de cuenta
                    this.puesto = ""//puesto
                    this.fecha = fecha.text.toString()//fecha nacimiento
                    this.nivel = ""//nivel escolar
                    if (masculino.isChecked) {
                        this.genero = "Masculino"//genero
                    }
                    if (femenino.isChecked) {
                        this.genero = "Femenino"//genero
                    }
                    //encontrar puesto y clave
                    for (i in 0..arregloClavesPuestos.size - 1) {
                        if (i == indicePuesto - 1) {
                            this.puesto = arregloPuestos[i + 1].toString()
                            this.clavePuesto = arregloClavesPuestos[indicePuesto - 1].toString()
                        }
                    }
                    for (i in 0..nivelEscolar.size - 1) {
                        if (i == indiceNivel - 1) {
                            this.nivel = nivelEscolar[i + 1].toString()
                        }
                    }
                    for (i in 0..arregloClavesPais.size - 1) {
                        if (i == indicePais - 1) {
                            this.pais = paises[i + 1].toString()
                            this.clavePais = arregloClavesPais[i].toString().toInt()
                        }
                    }
                    getActivity()?.let { it1 -> this.registroEmpleado(it1) }
                }
            }
        }
        limpia.setOnClickListener {
            nombre.setText("")
            apellidoP.setText("")
            apellidoM.setText("")
            numeroTele.setText("")
            curp.setText("")
            rfc.setText("")
            calle.setText("")
            numeroExterior.setText("")
            numeroInterior.setText("")
            colonia.setText("")
            codigo.setText("")
            cuenta.setText("")
            fecha.setText("")
            puesto.setSelection(0)
            pais.setSelection(0)
            nivel.setSelection(0)
        }
        consulta.setOnClickListener {
            var principal=(activity as Principal)
            principal.replaceFragment(ConsultaEmpleado(this.claveRol),"Consulta de empleados")
        }
        return view
    }

    fun datosEmpleados(context: Context,callbacks:(objeto:ArrayList<Array<Any>>) -> Unit,palabra:String){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(palabra)
        var nombres= arrayListOf<Any>("palabra")
        cone.tabla("rh/control/empleados.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun eliminaEmpleado(context: Context,clave:String){
        var cone=Conexiones(context)
        var arregloValores= arrayListOf<Any>(clave)
        var arregloNombres= arrayListOf<Any>("clave")
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlQuitaEmpleado.php",context)
    }

    fun aceptar() {
        getActivity()?.let { it1 -> this.editaEmpleado(it1) }
    }

    fun cancelar() {
        val t:Toast=Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG)
        t.show()
    }

    fun datosEmpleado(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtener("rh/control/obtenerDatosEmpleado.php",nombres,valores,context,this.clave,"clave",{
            callbacks(it)
        })
    }

    fun editaEmpleado(context: Context){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(this.clave,this.nombre,this.numero,this.curp,this.rfc,this.n_cuenta,this.puesto,this.fecha,this.nivel,this.genero,this.pais,this.clavePuesto,this.clavePais.toInt())
        var nombres= arrayListOf<Any>("clave","nombre","telefono","curp","rfc","cuenta","puesto","fecha","nivel","genero","pais","clvPuesto","clvPais")
        cone.nuevo(valores,nombres,"rh/control/ctrlEditEmpleado.php",context)
    }

    fun registroEmpleado(context: Context){
        this.clave=this.claveAlea(5)
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(this.clave,this.nombre,this.numero,this.curp,this.rfc,this.direccion,this.n_cuenta,this.puesto,this.fecha,this.nivel,this.genero,this.pais,this.clavePuesto,this.clavePais.toInt())
        var nombres= arrayListOf<Any>("clave","nombre","telefono","curp","rfc","direccion","cuenta","puesto","fecha","nivel","genero","pais","clvPuesto","clvPais")
        cone.nuevo(valores,nombres,"rh/control/ctrlRegEmpleado.php",context)
        this.clave=""
    }

    fun nombresPuestos(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("oiqwe","palitos","descripcion de los palitos")
        var nombres= arrayListOf<Any>("clave","nombre","descripcion")
        cone.obtenerDatos("rh/control/nombrePuesto.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun clavePuestos(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("oiqwe","palitos","descripcion de los palitos")
        var nombres= arrayListOf<Any>("clave","nombre","descripcion")
        cone.obtenerDatos("rh/control/clavePuesto.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun clavePais(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("oiqwe","palitos","descripcion de los palitos")
        var nombres= arrayListOf<Any>("clave","nombre","descripcion")
        cone.obtenerDatos("rh/control/clavePais.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun nombrePais(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("oiqwe","palitos","descripcion de los palitos")
        var nombres= arrayListOf<Any>("clave","nombre","descripcion")
        cone.obtenerDatos("rh/control/nombrePais.php",nombres,valores,context,{
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

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Empleado.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Empleado("","").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}