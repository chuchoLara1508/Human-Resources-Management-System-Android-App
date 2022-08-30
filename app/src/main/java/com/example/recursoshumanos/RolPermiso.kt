package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RolPermiso.newInstance] factory method to
 * create an instance of this fragment.
 */
class RolPermiso(codigo:String,rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var clave:String
    var claveRol=""
    var clavePermiso=""
    var guardar=0
    var actualizar=0
    var eliminar=0
    lateinit var rol: String
    init{
        this.clave=codigo
        this.rol=rol
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
        var view=inflater.inflate(R.layout.fragment_rol_permiso, container, false)
            //cargamos los roles y modulos en los spinners
        var rol:Spinner=view.findViewById(R.id.rolLista)
        var arreglosRol= arrayListOf<Any>("Seleccione")
        var clavesRol= arrayListOf<Any>()
        var modulo=view.findViewById<Spinner>(R.id.moduloLista)
        var arreglosModulo= arrayListOf<Any>("Seleccione")
        var clavesModulo= arrayListOf<Any>()
        var altas=view.findViewById<CheckBox>(R.id.alta)
        var bajas:CheckBox=view.findViewById(R.id.baja)
        var edita=view.findViewById<CheckBox>(R.id.modifica)
        var consulta=view.findViewById<Button>(R.id.consulta_rol_permiso)
        var rolquellega=""
        var moduloquellega=""
        if(!this.clave.equals("")){
            getActivity()?.let {
                this.traeClvModuloPorClave(it,{
                    moduloquellega=it[0].toString()
                })
            }
            getActivity()?.let {
                this.traeClvRolPorClave(it,{
                    rolquellega=it[0].toString()
                })
            }
        }
        getActivity()?.let {
            this.obtenerRoles(it,{
                for(i in 0..it.size-1){
                    arreglosRol.add(it[i].toString())
                }
                var adaptador=getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, arreglosRol) }
                rol.adapter=adaptador
                if(!this.clave.equals("")){
                    for(i in 1..arreglosRol.size-1){
                        if(rolquellega.uppercase().equals(arreglosRol[i].toString().uppercase())){
                            rol.setSelection(i)
                        }
                    }
                }
            })
        }
        getActivity()?.let {
            this.obtenerModulos(it,{
                for(i in 0..it.size-1){
                    arreglosModulo.add(it[i].toString())
                }
                var adaptador=getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, arreglosModulo) }
                modulo.adapter=adaptador
                if(!this.clave.equals("")){
                    for(i in 1..arreglosModulo.size-1){
                        if(moduloquellega.uppercase().equals(arreglosModulo[i].toString().uppercase())){
                            modulo.setSelection(i)
                        }
                    }
                }
            })
        }
        getActivity()?.let {
            this.obtenerClavesRoles(it,{
                for(i in 0..it.size-1){
                    clavesRol.add(it[i].toString())
                }
            })
        }

        getActivity()?.let {
            this.obtenerClavesModulos(it,{
                for(i in 0..it.size-1){
                    clavesModulo.add(it[i].toString())
                }
            })
        }
        var regPermiso:Button=view.findViewById(R.id.registrar)
        if(!this.clave.equals("")){
            regPermiso.setText("Editar")
            rol.isEnabled=false
            modulo.isEnabled=false
            //colocar altas bajas edita segun esté en la BD
            getActivity()?.let {
                this.traeGuardar(it,{
                    if(it[0].toString().toInt()==1){
                        altas.isChecked=true
                    }
                })
            }
            getActivity()?.let {
                this.traeActu(it,{
                    if(it[0].toString().toInt()==1){
                        edita.isChecked=true
                    }
                })
            }

            getActivity()?.let {
                this.traeQuita(it,{
                    if(it[0].toString().toInt()==1){
                        bajas.isChecked=true
                    }
                })
            }
        }
        regPermiso.setOnClickListener {
            var indice=rol.selectedItemPosition
            var indice2=modulo.selectedItemPosition
            if(indice==0||indice2==0){
                Toast.makeText(getActivity(),"Por favor seleccione un rol y módulo",Toast.LENGTH_LONG).show()
            }
            else{
                for(i in 0..clavesRol.size-1){
                    if(i==indice-1){
                        this.claveRol=clavesRol[i].toString()
                    }
                }
                for(i in 0..clavesModulo.size-1){
                    if(i==indice2-1){
                        this.clavePermiso=clavesModulo[i].toString()
                    }
                }
                if(altas.isChecked){
                    this.guardar=1
                }
                else{
                    this.guardar=0
                }
                if(bajas.isChecked){
                    this.eliminar=1
                }
                else{
                    this.eliminar=0
                }
                if(edita.isChecked){
                    this.actualizar=1
                }
                else {
                    this.actualizar = 0
                }
                if(this.clave.equals("")){
                    getActivity()?.let { it1 ->
                        this.contarRegistrosRoles(it1,{
                            if(it[0].toString().toInt()<6){
                                getActivity()?.let { it2 ->
                                this.contarRegistrosModulos(it2,{
                                    if(it[0].toString().toInt()<3){
                                        getActivity()?.let { it3 -> this.registroRolPermiso(it3) }
                                    }
                                    else{
                                        Toast.makeText(getActivity(),"Ya están registrados todos los permisos para este módulo",Toast.LENGTH_LONG).show()
                                        }
                                    })
                                }
                            }
                            else{
                                Toast.makeText(getActivity(),"Ya están registrados todos los permisos",Toast.LENGTH_LONG).show()
                            }
                        })
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
        }
        consulta.setOnClickListener {
            var principal=(activity as Principal)
            principal.replaceFragment(ConsultaRolPermiso(this.rol),"Consulta de roles y permisos")
        }
        return view
    }

    fun rolesmodulos(context: Context,callbacks:(objeto:ArrayList<Array<Any>>) -> Unit,rol:Int,modulo:Int){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(rol,modulo)
        var nombres= arrayListOf<Any>("rol","modulo")
        cone.tabla("rh/control/rolespermisos.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun aceptar() {
        getActivity()?.let { it1 -> this.editarRolPermiso(it1) }
    }

    fun cancelar() {
        val t:Toast=Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG)
        t.show()
    }

    fun traeGuardar(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtener("rh/control/traeGuardar.php",nombres,valores,context,this.clave,"clave",{
            callbacks(it)
        })
    }

    fun traeActu(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtener("rh/control/traeActu.php",nombres,valores,context,this.clave,"clave",{
            callbacks(it)
        })
    }

    fun traeQuita(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtener("rh/control/traeQuita.php",nombres,valores,context,this.clave,"clave",{
            callbacks(it)
        })
    }

    fun traeClvRolPorClave(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtener("rh/control/traeClvRol.php",nombres,valores,context,this.clave,"clave",{
            callbacks(it)
        })
    }

    fun traeClvModuloPorClave(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtener("rh/control/traeClvModulo.php",nombres,valores,context,this.clave,"clave",{
            callbacks(it)
        })
    }

    fun contarRegistrosRoles(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtener("rh/control/contarRol.php",nombres,valores,context,this.claveRol,"clvrol",{
            callbacks(it)
        })
    }

    fun contarRegistrosModulos(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtener("rh/control/contarPer.php",nombres,valores,context,this.clavePermiso,"clvmodulo",{
            callbacks(it)
        })
    }

    fun obtenerClavesRoles(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtenerDatos("rh/control/obtenerClavesRoles.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun obtenerClavesModulos(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtenerDatos("rh/control/obtenerClavesModulos.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun obtenerRoles(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtenerDatos("rh/control/obtenerRoles.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun obtenerModulos(context: Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("")
        var valores= arrayListOf<Any>("")
        cone.obtenerDatos("rh/control/obtenerModulos.php",nombres,valores,context,{
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

    fun registroRolPermiso(context: Context){
        this.clave=this.claveAlea(5)
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("clave","clave_rol","clave_modulo","guardar","actu","elimina")
        var valores= arrayListOf<Any>(this.clave,this.claveRol,this.clavePermiso,this.guardar,this.actualizar,this.eliminar)
        cone.nuevo(valores,nombres,"rh/control/ctrlRegRolPermiso.php",context)
        this.clave=""
    }

    fun editarRolPermiso(context: Context){
        var cone=Conexiones(context)
        var nombres= arrayListOf<Any>("clave","clave_rol","clave_modulo","guardar","actu","elimina")
        var valores= arrayListOf<Any>(this.clave,this.claveRol,this.clavePermiso,this.guardar,this.actualizar,this.eliminar)
        cone.nuevo(valores,nombres,"rh/control/ctrlEditRP.php",context)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RolPermiso.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RolPermiso("","").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}