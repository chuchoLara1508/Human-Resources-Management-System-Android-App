package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isGone
import androidx.fragment.app.Fragment


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ConsultaDepartamento.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultaDepartamento(rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var clave:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    init{
        this.clave=rol
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view=inflater.inflate(R.layout.fragment_consulta_departamento, container, false)
        // Inflate the layout for this fragment
        var ordena= arrayListOf<Any>("Sin ordenar","A-Z(Nombre)","A-Z(Descripción)","Z-A(Nombre)","Z-A(Descripción)")
        var lista=view.findViewById<Spinner>(R.id.ordenar)
        var adaptador =
            getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, ordena) }
        lista.adapter = adaptador
        var pal=view.findViewById<EditText>(R.id.palabra)
        var buscar=view.findViewById<Button>(R.id.buscar)
        var palabra=""
        var seleccionado=0
        buscar.setOnClickListener {
            seleccionado=lista.selectedItemPosition
            palabra=pal.text.toString()
            this.cargarTabla(view,palabra,seleccionado)
        }
       this.cargarTabla(view,"",0)
        return view
    }

    fun cargarTabla(view: View,palabra:String,ordenar:Int){
        var tabla=view.findViewById<TableLayout>(R.id.tbdepartamentos)
        tabla?.removeAllViews()
        var deptos=Departamento("","")
        var usua=Usuario("")
        var msj:TextView=view.findViewById(R.id.mensaje)
        msj.isGone=true
        view.findViewById<TextView>(R.id.cNo).isGone=false
        view.findViewById<TextView>(R.id.cNombre).isGone=false
        view.findViewById<TextView>(R.id.cDesc).isGone=false
        view.findViewById<TextView>(R.id.cAcciones).isGone=false
        view.findViewById<TextView>(R.id.cNo).setText("N°")
        view.findViewById<TextView>(R.id.cNombre).setText("Nombre")
        view.findViewById<TextView>(R.id.cDesc).setText("Descripción")
        view.findViewById<TextView>(R.id.cAcciones).setText("Acciones")
        getActivity()?.let {
            deptos.datosDepartamentos(it,{
                for(i in 0..it.size-1){
                    val registro =
                        LayoutInflater.from(getActivity()).inflate(R.layout.table_row_departamentos, null, false)
                    val colNumero = registro.findViewById<View>(R.id.colNumero) as TextView
                    val colNombre = registro.findViewById<View>(R.id.colNombre) as TextView
                    val colDescripcion = registro.findViewById<View>(R.id.colDescripcion) as TextView
                    val colEditar = registro.findViewById<ImageButton>(R.id.colEditar)
                    val colEliminar = registro.findViewById<ImageButton>(R.id.colEliminar)
                    colNumero.text = (i + 1).toString()
                    colNombre.text = it[i][1].toString()
                    colDescripcion.text = it[i][2].toString()
                    colEditar.id = i
                    var edita = ""
                    var elimina = ""
                    edita = it[i][0].toString()
                    elimina = it[i][0].toString()
                    colEliminar.id = i
                    colEditar.setOnClickListener {
                        getActivity()?.let { it2 ->
                            usua.actualizarRP(it2, this.clave, {
                                if (it[2].toString().toInt() == 1) {
                                    var principal = (activity as Principal)
                                    principal.replaceFragment(
                                        Departamento(edita, this.clave),
                                        "Editar departamento"
                                    )
                                }
                                if (it[2].toString().toInt() == 0) {
                                    Toast.makeText(
                                        getActivity(),
                                        "No tiene permisos para editar",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                        }
                    }
                    colEliminar.setOnClickListener {
                        //colocar cuadro de confirmación si es positivo eliminar, sino no hacer nada
                        getActivity()?.let { it2 ->
                            usua.eliminarRP(it2, this.clave, {
                                if (it[2].toString().toInt() == 1) {
                                    val dialogo1: AlertDialog.Builder = AlertDialog.Builder(getActivity())
                                    dialogo1.setTitle("Confirmación de borrado")
                                    dialogo1.setMessage("¿Desea eliminar este registro?")
                                    dialogo1.setCancelable(false)
                                    dialogo1.setPositiveButton("Confirmar",
                                        DialogInterface.OnClickListener { dialogo1, id ->
                                            aceptar(
                                                elimina,
                                                this.clave,
                                            )
                                        })
                                    dialogo1.setNegativeButton("Cancelar",
                                        DialogInterface.OnClickListener { dialogo1, id -> cancelar() })
                                    dialogo1.show()
                                }
                                if (it[2].toString().toInt() == 0) {
                                    Toast.makeText(
                                        getActivity(),
                                        "No tiene permisos para eliminar",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            })
                        }
                    }
                    tabla?.addView(registro)
                }
            },palabra,ordenar)
        }
    }

    fun aceptar(clave:String,rol:String){
        var depto=Departamento("",rol)
        getActivity()?.let { depto.eliminaDepto(it,clave) }
        view?.let { this.cargarTabla(it,"",0) }
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
         * @return A new instance of fragment ConsultaDepartamento.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultaDepartamento("").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}