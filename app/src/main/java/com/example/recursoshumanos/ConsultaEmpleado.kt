package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConsultaEmpleado.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultaEmpleado(rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var claveRol:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    init{
        this.claveRol=rol
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_consulta_empleado, container, false)
        var pal=view.findViewById<EditText>(R.id.palabra)
        var buscar=view.findViewById<Button>(R.id.buscar)
        var palabra=""
        buscar.setOnClickListener {
            palabra=pal.text.toString()
            this.cargarTabla(view,palabra)
        }
        this.cargarTabla(view,palabra)
        return view
    }

    fun cargarTabla(view:View,palabra:String){
        var tabla=view.findViewById<TableLayout>(R.id.tbempleados)
        tabla?.removeAllViews()
        var emple=Empleado("","")
        var usua=Usuario("")
        view.findViewById<TextView>(R.id.cNo).setText("N°")
        view.findViewById<TextView>(R.id.cClave).setText("Clave")
        view.findViewById<TextView>(R.id.cNombre).setText("Nombre")
        view.findViewById<TextView>(R.id.cTel).setText("Teléfono")
        view.findViewById<TextView>(R.id.cPuesto).setText("Puesto que desepmeña")
        view.findViewById<TextView>(R.id.cAcciones).setText("Acciones")
        getActivity()?.let {
            emple.datosEmpleados(it,{
                for(i in 0..it.size-1){
                    val registro=LayoutInflater.from(getActivity()).inflate(R.layout.table_row_empleados,null,false)
                    val colNumero=registro.findViewById<View>(R.id.numero_empleado) as TextView
                    val colClave=registro.findViewById<View>(R.id.claveempleado) as TextView
                    val colNombre=registro.findViewById<View>(R.id.nombreempleado) as TextView
                    val colTel=registro.findViewById<View>(R.id.numero_telefono) as TextView
                    val colPuest=registro.findViewById<View>(R.id.puesto_empleado) as TextView
                    val colEditar=registro.findViewById<ImageButton>(R.id.colEditar)
                    val colEliminar=registro.findViewById<ImageButton>(R.id.colEliminar)
                    colNumero.text=(i+1).toString()
                    colClave.text=it[i][0].toString()
                    colNombre.text=it[i][1].toString()
                    colTel.text=it[i][2].toString()
                    colPuest.text=it[i][7].toString()
                    colEditar.id=i
                    colEliminar.id=i
                    var editar=""
                    var eliminar=""
                    editar=it[i][0].toString()
                    eliminar=it[i][0].toString()
                    colEditar.setOnClickListener{
                        getActivity()?.let { it1 ->
                            usua.actualizarRP(it1,this.claveRol,{
                                if(it[4].toString().toInt()==1){
                                    var principal=(activity as Principal)
                                    principal.replaceFragment(Empleado(editar,this.claveRol),"Editar empleado")
                                }
                                if(it[4].toString().toInt()==0){
                                    Toast.makeText(getActivity(),"No tiene permisos para editar",Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                    colEliminar.setOnClickListener {
                        getActivity()?.let { it1 ->
                            usua.eliminarRP(it1,this.claveRol,{
                                if(it[4].toString().toInt()==1){
                                    val dialogo1: AlertDialog.Builder = AlertDialog.Builder(getActivity())
                                    dialogo1.setTitle("Confirmación de borrado")
                                    dialogo1.setMessage("¿Desea eliminar este registro?")
                                    dialogo1.setCancelable(false)
                                    dialogo1.setPositiveButton("Confirmar",
                                        DialogInterface.OnClickListener { dialogo1, id -> aceptar(eliminar,this.claveRol) })
                                    dialogo1.setNegativeButton("Cancelar",
                                        DialogInterface.OnClickListener { dialogo1, id -> cancelar() })
                                    dialogo1.show()
                                }
                                if(it[4].toString().toInt()==0){
                                    Toast.makeText(getActivity(),"No tiene permisos para eliminar",Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                    tabla?.addView(registro)
                }
            },palabra)
        }
    }
    fun aceptar(clave:String,rol:String) {
        var empleado=Empleado("",rol)
        getActivity()?.let { empleado.eliminaEmpleado(it,clave) }
        view?.let { this.cargarTabla(it,"") }
        val ft = fragmentManager!!.beginTransaction()
        ft.detach(this).attach(this).commit()
    }

    fun cancelar() {
        val t:Toast=Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG)
        t.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConsultaEmpleado.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultaEmpleado("").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}