package com.example.recursoshumanos

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Usuario.newInstance] factory method to
 * create an instance of this fragment.
 */
class Usuario(rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var clave:String
    lateinit var clave_rol:String
    lateinit var nombre:String
    lateinit var usuario:String
    lateinit var contra:String
    lateinit var correo:String
    lateinit var clave_empleado:String
    lateinit var claveRol:String
    init {
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
        var view=inflater.inflate(R.layout.fragment_usuario, container, false)
        var empleado=view.findViewById<Spinner>(R.id.empleadoLista)
        var arreglosEmpleado= arrayListOf<Any>()
        var clavesEmpleado= arrayListOf<Any>()
        var rol=view.findViewById<Spinner>(R.id.rolLista)
        var arreglosRol= arrayListOf<Any>("Seleccione")
        var clavesRol= arrayListOf<Any>()
        var rp=RolPermiso("","")
        var correo:EditText=view.findViewById(R.id.correo)
        var consulta=view.findViewById<Button>(R.id.consulta_usuario)
        getActivity()?.let {
            rp.obtenerRoles(it,{
                for(i in 0..it.size-1){
                    arreglosRol.add(it[i].toString())
                }
                var adaptador=getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, arreglosRol) }
                rol.adapter=adaptador
            })
        }
        getActivity()?.let {
            rp.obtenerClavesRoles(it,{
                for(i in 0..it.size-1){
                    clavesRol.add(it[i].toString())
                }
            })
        }
        getActivity()?.let {
            this.buscarNombreEmpleado(it,{
                for(i in 0..it.size-1){
                    arreglosEmpleado.add(it[i].toString())
                }
                getActivity()?.let { it1 ->
                    this.buscarClaveEmpleado(it1) {
                        for (i in 0..it.size - 1) {
                            clavesEmpleado.add(it[i].toString())
                        }
                        var combina = arrayListOf<Any>("Seleccione")
                        for (i in 0..arreglosEmpleado.size - 1) {
                            combina.add(clavesEmpleado[i].toString() + " | " + arreglosEmpleado[i].toString())
                        }
                        var adaptador = getActivity()?.let { it1 ->
                            ArrayAdapter(
                                it1,
                                android.R.layout.simple_spinner_item,
                                combina
                            )
                        }
                        empleado.adapter = adaptador
                        var registro: Button = view.findViewById(R.id.ingresar)
                        registro.setOnClickListener {
                            var indice1 = empleado.selectedItemPosition
                            var indice2 = rol.selectedItemPosition
                            if (indice1 == 0 || indice2 == 0 || correo.text.toString().equals("")) {
                                Toast.makeText(getActivity(), "Por favor complete el formulario", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                for (i in 0..arreglosEmpleado.size - 1) {
                                    if (i == indice1 - 1) {
                                        this.nombre = arreglosEmpleado[i].toString()
                                        this.clave_empleado = clavesEmpleado[i].toString()
                                    }
                                    if (i == indice2 - 1) {
                                        this.clave_rol = clavesRol[i].toString()
                                    }
                                }
                                this.contra = this.claveAlea(5)
                                this.correo = correo.text.toString()
                              //validar que el correo no exista
                                var encontrado=0
                                getActivity()?.let { it2 ->
                                    this.obtenerCorreo(it2,{
                                        for(i in 0..it.size-1){
                                            if(it[i].toString().equals(this.correo)){
                                                encontrado++
                                            }
                                        }
                                        if(encontrado==0) {
                                            getActivity()?.let { it3 ->
                                                this.datosUsuarios(it3,{
                                                    getActivity()?.let { it2 ->
                                                        var usuarios=0
                                                        for(i in 0..it.size-1){
                                                            if(this.clave_empleado.equals(it[i][6].toString())){
                                                                usuarios++
                                                            }
                                                        }
                                                        if(usuarios==0) {
                                                            this.registraUsuario(it2)
                                                        }
                                                        else{
                                                            Toast.makeText(getActivity(),"Un empleado no puede tener más de 1 cuenta",Toast.LENGTH_LONG).show()
                                                        }
                                                    }
                                                },"")
                                            }
                                        }
                                        else{
                                            Toast.makeText(getActivity(),"El correo electrónico ya existe",Toast.LENGTH_LONG).show()
                                        }
                                    })
                                }
                            }
                        }
                        var limpia: Button = view.findViewById(R.id.clean)
                        limpia.setOnClickListener {
                            empleado.setSelection(0)
                            rol.setSelection(0)
                            correo.setText("")
                        }
                        consulta.setOnClickListener {
                            var principal = (activity as Principal)
                            principal.replaceFragment(ConsultaUsuario(this.claveRol), "Consulta de usuarios")
                        }
                    }
                }
            })
        }
        return view
    }

    fun datosUsuarios(context: Context,callbacks:(objeto:ArrayList<Array<Any>>) -> Unit,palabra:String){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(palabra)
        var nombres= arrayListOf<Any>("palabra")
        cone.tabla("rh/control/usuarios.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun obtenerCorreo(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>()
        cone.obtenerDatos("rh/control/obtenerCorreo.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun buscaRol(context: Context,valor:String,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(valor)
        var nombres= arrayListOf<Any>("usuario")
        cone.obtener("rh/control/obtenerRol.php",nombres,valores,context,valor,"usuari",{
            callbacks(it)
        })

    }

    fun guardarRP(context: Context,nombre:String,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtener("rh/control/guardarRolUsuario.php",valores,nombres,context,nombre,"rol",{
            callbacks(it)
        })
    }

    fun actualizarRP(context: Context,nombre:String,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtener("rh/control/actualizarRolUsuario.php",valores,nombres,context,nombre,"rol",{
            callbacks(it)
        })
    }

    fun eliminarRP(context: Context,nombre:String,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtener("rh/control/eliminarRolUsuario.php",valores,nombres,context,nombre,"rol",{
            callbacks(it)
        })
    }

    fun buscarClaveRol(context: Context,nombre:String,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtener("rh/control/buscarRolUsuario.php",valores,nombres,context,nombre,"usuario",{
            callbacks(it)
        })
    }

    fun eliminaUsuario(context: Context,clave:String){
        var cone=Conexiones(context)
        var arregloValores= arrayListOf<Any>(clave)
        var arregloNombres= arrayListOf<Any>("clave")
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlQuitaUsuario.php",context)
    }

    fun registraUsuario(context: Context){
        var cone=Conexiones(context)
        this.clave=this.claveAlea(5)
        var valores= arrayListOf<Any>(this.clave,this.clave_rol,this.nombre,this.contra,this.correo,this.clave_empleado)
        var nombres= arrayListOf<Any>("clave","claverol","nombre","contra","correo","claveempleado")
        cone.nuevo(valores,nombres,"rh/control/ctrlRegUsuario.php",context)
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

    fun buscarNombreEmpleado(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>()
        cone.obtenerDatos("rh/control/obtenerNombreEmpleado.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun buscarClaveEmpleado(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>()
        cone.obtenerDatos("rh/control/obtenerClaveEmpleado.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun validaSesion(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(this.usuario,this.contra)
        var nombres= arrayListOf<Any>("usu","pass")
        cone.obtenerDatos("rh/control/ctrlValidaUsuario.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Usuario.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Usuario("").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}