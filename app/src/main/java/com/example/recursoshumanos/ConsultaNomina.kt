package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
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
 * Use the [ConsultaNomina.newInstance] factory method to
 * create an instance of this fragment.
 */
class ConsultaNomina(rol:String) : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var claveRol:String

    init{
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
        var view=inflater.inflate(R.layout.fragment_consulta_nomina, container, false)
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
        var tabla=view.findViewById<TableLayout>(R.id.tbnominas)
        tabla?.removeAllViews()
        var nomina=Nomina("","")
        view.findViewById<TextView>(R.id.cNo).setText("N°")
        view.findViewById<TextView>(R.id.cNombre).setText("Nombre")
        view.findViewById<TextView>(R.id.cFI).setText("Fecha de inicio")
        view.findViewById<TextView>(R.id.cFF).setText("Fecha de fin")
        view.findViewById<TextView>(R.id.cPago).setText("Pago")
        view.findViewById<TextView>(R.id.cAcciones).setText("Acciones")
        getActivity()?.let {
            nomina.datosNominas(it,{
                for(i in 0..it.size-1){
                    val registro=LayoutInflater.from(getActivity()).inflate(R.layout.table_row_nominas,null,false)
                    val colNumero=registro.findViewById<View>(R.id.numero_nomina) as TextView
                    val colNombre=registro.findViewById<View>(R.id.nombre_empleado_nomina) as TextView
                    val colFechaI=registro.findViewById<View>(R.id.fecha_inicio_nomina) as TextView
                    val colFin=registro.findViewById<View>(R.id.fecha_fin_nomina) as TextView
                    val colTotal=registro.findViewById<View>(R.id.total_nomina) as TextView
                    val colEditar=registro.findViewById<ImageButton>(R.id.colEditar)
                    val colEliminar=registro.findViewById<ImageButton>(R.id.colEliminar)
                    val colPDF=registro.findViewById<ImageButton>(R.id.colPDF)
                    colNumero.text=(i+1).toString()
                    colNombre.text=it[i][2].toString()
                    colFechaI.text=it[i][4].toString()
                    colFin.text=it[i][5].toString()
                    colTotal.text="$"+it[i][18].toString()
                    colEditar.id=i
                    colEliminar.id=i
                    var editar=""
                    var eliminar=""
                    var clavepdf=""
                    clavepdf=it[i][0].toString()
                    editar=it[i][0].toString()
                    eliminar=it[i][0].toString()
                    var usua=Usuario("")
                    colEditar.setOnClickListener {
                        getActivity()?.let { it2 ->
                            usua.actualizarRP(it2,this.claveRol,{
                                if(it[1].toString().toInt()==1){
                                    var principal=(activity as Principal)
                                    principal.replaceFragment(Nomina(editar,this.claveRol),"Editar nómina")
                                }
                                if(it[1].toString().toInt()==0){
                                    Toast.makeText(getActivity(),"No tiene permisos para editar",Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                    colEliminar.setOnClickListener {
                        getActivity()?.let { it2 ->
                            usua.eliminarRP(it2,this.claveRol,{
                                if(it[1].toString().toInt()==1){
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
                                if(it[1].toString().toInt()==0){
                                    Toast.makeText(getActivity(),"No tiene permisos para eliminar",Toast.LENGTH_LONG).show()
                                }
                            })
                        }
                    }
                    colPDF.setOnClickListener {
                       var intento=Intent(getActivity(),PDF::class.java)
                        intento.putExtra("clavePDF",clavepdf)
                        startActivity(intento)
                    }
                    tabla?.addView(registro)
                }
            },palabra)
        }
    }

    fun aceptar(clave:String,rol: String) {
       var nomina=Nomina("",rol)
        getActivity()?.let { nomina.eliminaNomina(it,clave) }
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
         * @return A new instance of fragment ConsultaNomina.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ConsultaNomina("").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}