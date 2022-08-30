package com.example.recursoshumanos

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
 * Use the [ConsultaRolPermiso.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultaRolPermiso(rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var rol:String
    init{
        this.rol=rol
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_consulta_rol_permiso, container, false)
        var listarol=view.findViewById<Spinner>(R.id.rol_list)
        var listamodulo=view.findViewById<Spinner>(R.id.modulo_list)
        var roles= arrayListOf<Any>("Todos","SuperAdministrador de RH","Administrador de RH","Empleado de RH")
        var modulos= arrayListOf<Any>("Todos","Puestos","Nóminas","Departamentos","Usuarios","Empleados","Incapacidades")
        var adaptador=getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, roles) }
        listarol.adapter=adaptador
        var adaptador2=getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, modulos) }
        listamodulo.adapter=adaptador2
        var boton=view.findViewById<Button>(R.id.buscar)
        var srol=0
        var smodulo=0
        boton.setOnClickListener {
            srol=listarol.selectedItemPosition
            smodulo=listamodulo.selectedItemPosition
            println("Rol: "+srol.toString()+" | Módulo: "+smodulo.toString())
            this.cargarTabla(view,srol,smodulo)
        }
       this.cargarTabla(view,0,0)
        return view
    }

    fun cargarTabla(view: View,rol:Int,modulo:Int){
        var tabla=view.findViewById<TableLayout>(R.id.tbrolespermisos)
        tabla?.removeAllViews()
        var rp=RolPermiso("","")
        view.findViewById<TextView>(R.id.cNo).setText("N°")
        view.findViewById<TextView>(R.id.cRol).setText("Rol")
        view.findViewById<TextView>(R.id.cModulo).setText("Módulo")
        view.findViewById<TextView>(R.id.cGuardar).setText("Alta")
        view.findViewById<TextView>(R.id.cEditar).setText("Editar")
        view.findViewById<TextView>(R.id.cBorrar).setText("Borrar")
        view.findViewById<TextView>(R.id.cAcciones).setText("Actualizar")
        getActivity()?.let {
            rp.rolesmodulos(it,{
                for(i in 0..it.size-1){
                    val registro=LayoutInflater.from(getActivity()).inflate(R.layout.table_row_rolpermiso,null,false)
                    val colNumero=registro.findViewById<View>(R.id.numero_rp) as TextView
                    val colNombreRol=registro.findViewById<View>(R.id.nombre_rol) as TextView
                    val colNombreModulo=registro.findViewById<View>(R.id.nombre_modulo) as TextView
                    val colGuardar=registro.findViewById<View>(R.id.guardar) as TextView
                    val colActualizar=registro.findViewById<View>(R.id.actualizar) as TextView
                    val colEliminar=registro.findViewById<View>(R.id.eliminar) as TextView
                    val colEditar=registro.findViewById<ImageButton>(R.id.colEditar)
                    colNumero.text=(i+1).toString()
                    colNombreRol.text=it[i][1].toString()
                    colNombreModulo.text=it[i][2].toString()
                    colGuardar.text= it[i][3].toString()
                    colActualizar.text=it[i][4].toString()
                    colEliminar.text=it[i][5].toString()
                    colEditar.id=i
                    var editar=it[i][0].toString()
                    colEditar.setOnClickListener {
                        if(this.rol.equals("SuperAdministrador de RH")) {
                            var principal = (activity as Principal)
                            principal.replaceFragment(RolPermiso(editar, this.rol), "Editar roles y permisos")
                        }
                        else{
                            Toast.makeText(getActivity(),"No tiene permisos para editar", Toast.LENGTH_LONG).show()
                        }
                    }
                    tabla?.addView(registro)
                }
            },rol,modulo)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConsultaRolPermiso.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultaRolPermiso("").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}