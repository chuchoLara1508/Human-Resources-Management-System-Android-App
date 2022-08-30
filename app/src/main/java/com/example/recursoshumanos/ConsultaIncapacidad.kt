package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.DialogInterface
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
 * Use the [ConsultaIncapacidad.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultaIncapacidad(rol:String) : Fragment() {
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
        var view=inflater.inflate(R.layout.fragment_consulta_incapacidad, container, false)
        var rangos= arrayListOf<Any>("Todos","<20%",">=20% | <40%",">=40% | <60%",">=60% | <80%",">=80% | <=100%")
        var rango=view.findViewById<Spinner>(R.id.rango_descuento)
        var adaptador =
            getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, rangos) }
        rango.adapter = adaptador
        var pal=view.findViewById<EditText>(R.id.palabra)
        var buscar=view.findViewById<Button>(R.id.buscar)
        var palabra=""
        var seleccionado=0
        buscar.setOnClickListener {
            seleccionado = rango.selectedItemPosition
            palabra = pal.text.toString()
            this.cargarTabla(view,palabra,seleccionado)
        }
        this.cargarTabla(view,"",0)
        return view
    }

    fun cargarTabla(view: View,palabra:String,rango:Int){
        var tabla=view.findViewById<TableLayout>(R.id.tbincapacidades)
        tabla?.removeAllViews()
        var inc=Incapacidad("","")
        view.findViewById<TextView>(R.id.cNo).setText("N°")
        view.findViewById<TextView>(R.id.cNombre).setText("Nombre")
        view.findViewById<TextView>(R.id.cDescuento).setText("Descuento/día")
        view.findViewById<TextView>(R.id.cAcciones).setText("Acciones")
        getActivity()?.let {
            inc.datosIncapacidades(it,{
                for(i in 0..it.size-1){
                    val registro=LayoutInflater.from(getActivity()).inflate(R.layout.table_row_incapacidades,null,false)
                    val colNumero=registro.findViewById<View>(R.id.numero_incapacidad) as TextView
                    val colNombre=registro.findViewById<View>(R.id.nombreincapacidad) as TextView
                    val colDescuento=registro.findViewById<View>(R.id.descuento_incapacidad) as TextView
                    val colEditar=registro.findViewById<ImageButton>(R.id.colEditar)
                    val colEliminar=registro.findViewById<ImageButton>(R.id.colEliminar)
                    colNumero.text=(i+1).toString()
                    colNombre.text=it[i][1].toString()
                    colDescuento.text=it[i][2].toString()+"%"
                    var edita=""
                    var elimina=""
                    colEditar.id=i
                    colEliminar.id=i
                    var inca=Incapacidad("","")
                    edita=it[i][0].toString()
                    elimina=it[i][0].toString()
                    var usua=Usuario("")
                    colEditar.setOnClickListener {
                        getActivity()?.let { it2 ->
                            usua.actualizarRP(it2,this.claveRol,{
                                if(it[5].toString().toInt()==1) {
                                    var principal=(activity as Principal)
                                    principal.replaceFragment(Incapacidad(edita,this.claveRol),"Editar incapacidad")
                                }
                                if(it[5].toString().toInt()==0){
                                    Toast.makeText(getActivity(),"No tiene permisos para editar",Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                    colEliminar.setOnClickListener {
                        getActivity()?.let { it2 ->
                            usua.eliminarRP(it2,this.claveRol,{
                                if(it[5].toString().toInt()==1) {
                                    val dialogo1: AlertDialog.Builder = AlertDialog.Builder(getActivity())
                                    dialogo1.setTitle("Confirmación de borrado")
                                    dialogo1.setMessage("¿Desea eliminar este registro?")
                                    dialogo1.setCancelable(false)
                                    dialogo1.setPositiveButton("Confirmar",
                                        DialogInterface.OnClickListener { dialogo1, id -> aceptar(elimina,this.claveRol) })
                                    dialogo1.setNegativeButton("Cancelar",
                                        DialogInterface.OnClickListener { dialogo1, id -> cancelar() })
                                    dialogo1.show()
                                }
                                if(it[5].toString().toInt()==0){
                                    Toast.makeText(getActivity(),"No tiene permisos para eliminar",Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                    tabla?.addView(registro)
                }
            },palabra,rango)
        }
    }

    fun aceptar(clave:String,rol:String) {
       var inc=Incapacidad("",rol)
        getActivity()?.let { inc.eliminaIncapacidad(it,clave) }
        view?.let { this.cargarTabla(it,"",0) }
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
         * @return A new instance of fragment ConsultaIncapacidad.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultaIncapacidad("").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}