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
 * Use the [ConsultaUsuario.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultaUsuario(rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view=inflater.inflate(R.layout.fragment_consulta_usuario, container, false)
        var pal=view.findViewById<EditText>(R.id.palabra)
        var buscar=view.findViewById<Button>(R.id.buscar)
        var palabra=""
        buscar.setOnClickListener {
            palabra=pal.text.toString()
            this.cargarTabla(view,palabra)
        }
        this.cargarTabla(view,"")
        return view
    }

    fun cargarTabla(view: View,palabra:String){
        var tabla=view.findViewById<TableLayout>(R.id.tbusuarios)
        tabla?.removeAllViews()
        var usuario=Usuario("")
        view.findViewById<TextView>(R.id.cNo).setText("N°")
        view.findViewById<TextView>(R.id.cEmpleado).setText("Empleado")
        view.findViewById<TextView>(R.id.cUsuario).setText("Usuario")
        view.findViewById<TextView>(R.id.cCorreo).setText("Correo electrónico")
        view.findViewById<TextView>(R.id.cAcciones).setText("Eliminar")
        getActivity()?.let {
            usuario.datosUsuarios(it,{
                for(i in 0..it.size-1){
                    val registro=LayoutInflater.from(getActivity()).inflate(R.layout.table_row_usuarios,null,false)
                    val colNumero=registro.findViewById<View>(R.id.colNumero) as TextView
                    val colNombre=registro.findViewById<View>(R.id.colNombre) as TextView
                    val colUsuario=registro.findViewById<View>(R.id.colUsuario) as TextView
                    val colCorreo=registro.findViewById<View>(R.id.colCorreo) as TextView
                    val colEliminar=registro.findViewById<ImageButton>(R.id.colEliminar)
                    colNumero.text=(i+1).toString()
                    colNombre.text=it[i][2].toString()
                    colUsuario.text=it[i][3].toString()
                    colCorreo.text=it[i][5].toString()
                    colEliminar.id=i
                    var eliminar=""
                    eliminar=it[i][0].toString()
                    var usua=Usuario("")
                    colEliminar.setOnClickListener {
                        getActivity()?.let { it1 ->
                            usua.eliminarRP(it1,this.claveRol,{
                                if(it[3].toString().toInt()==1){
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
                                if(it[3].toString().toInt()==0){
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

    fun aceptar(clave:String,rol: String) {
        var usua=Usuario(rol)
        getActivity()?.let { usua.eliminaUsuario(it,clave) }
        view?.let { this.cargarTabla(it,"") }
        val ft = fragmentManager!!.beginTransaction()
        ft.detach(this).attach(this).commit()
    }

    fun cancelar() {
        val t: Toast = Toast.makeText(getActivity(),"Cancelado", Toast.LENGTH_LONG)
        t.show()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ConsultaUsuario.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultaUsuario("").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}