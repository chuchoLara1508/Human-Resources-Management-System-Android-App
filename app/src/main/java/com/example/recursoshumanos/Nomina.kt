package com.example.recursoshumanos

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.isVisible
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Nomina.newInstance] factory method to
 * create an instance of this fragment.
 */
class Nomina(codigo:String,rol:String) : Fragment() {

    // TODO: Rename and change types of parameters

    private var param1: String? = null
    private var param2: String? = null
    lateinit var clave:String
    var claveE:String=""
    var nombreE:String=""
    var puesto:String=""
    var fechaI:String=""
    var fechaF:String=""
    var fechaPago:String=""
    var horas:Int=0
    var incapacidad:String=""
    var dias:Int=0
    var descuentoISR:Double=0.0
    var descuentoIMSS:Double=0.0
    var descuentoInc:Double=0.0
    var pagoDia:Double=0.0
    var pagoHora:Double=0.0
    var totalDescuentos:Double=0.0
    var totalHoras:Int=0
    var totalDias:Int=0
    var total:Double=0.0
    lateinit var claveRol:String
    init {
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

        var view=inflater.inflate(R.layout.fragment_nomina, container, false)
        var claveE:EditText=view.findViewById(R.id.clave_empleado)
        var busqueda:Button=view.findViewById(R.id.buscar_clave)
        var buscar:Button=view.findViewById(R.id.consulta_nomina)
        var etiqueta1:TextView=view.findViewById(R.id.eti)
        var encontrado:EditText=view.findViewById(R.id.nombre_encontrado)
        var etiqueta2:TextView=view.findViewById(R.id.etifec)
        var fecInicio:EditText=view.findViewById(R.id.fecha_inicio)
        var etiqueta3:TextView=view.findViewById(R.id.etifechafin)
        var fecFin:EditText=view.findViewById(R.id.fecha_fin)
        var etiqueta4:TextView=view.findViewById(R.id.etifecp)
        var fecPago:EditText=view.findViewById(R.id.fecha_pago)
        var etiqueta5:TextView=view.findViewById(R.id.etihorasdiarias)
        var horasDiarias:EditText=view.findViewById(R.id.horas_diarias)
        var etiqueta6:TextView=view.findViewById(R.id.etiinca)
        var incapacidad:Spinner=view.findViewById(R.id.incapacidadLista)
        var etiqueta7:TextView=view.findViewById(R.id.etiincap)
        var diasInc:EditText=view.findViewById(R.id.dias_incapacidad)
        var etiqueta8:TextView=view.findViewById(R.id.etiisr)
        var descISR:EditText=view.findViewById(R.id.descuento_isr)
        var etiqueta9:TextView=view.findViewById(R.id.etiimss)
        var descIMSS:EditText=view.findViewById(R.id.descuento_imss)
        var registro:Button=view.findViewById(R.id.reg)
        var incapacidades= arrayListOf<Any>("Seleccione")
        var descuentos= arrayListOf<Any>()
        if(!this.clave.equals("")){
            claveE.isEnabled=false
            busqueda.isEnabled=false
            etiqueta1.isVisible=true
            encontrado.isVisible=true
            encontrado.isEnabled=false
            etiqueta2.isVisible=true
            fecInicio.isVisible=true
            etiqueta3.isVisible=true
            fecFin.isVisible=true
            etiqueta4.isVisible=true
            fecPago.isVisible=true
            etiqueta5.isVisible=true
            horasDiarias.isVisible=true
            etiqueta6.isVisible=true
            incapacidad.isVisible=true
            etiqueta7.isVisible=true
            diasInc.isVisible=true
            etiqueta8.isVisible=true
            descISR.isVisible=true
            etiqueta9.isVisible=true
            descIMSS.isVisible=true
            descISR.isVisible=true
            registro.isVisible=true
            buscar.isVisible=true
            registro.setText("Editar")

            getActivity()?.let {
                this.datosNomina(it,{
                    var incapacidadquellega=""
                    incapacidadquellega=it[6].toString()
                    claveE.setText(it[0].toString())
                    encontrado.setText(it[1].toString())
                    fecInicio.setText(it[2].toString())
                    fecFin.setText(it[3].toString())
                    fecPago.setText(it[4].toString())
                    horasDiarias.setText(it[5].toString())
                    diasInc.setText(it[7].toString())
                    descISR.setText(it[8].toString())
                    descIMSS.setText(it[9].toString())
                    getActivity()?.let { it1 ->
                        this.buscarDescuentos(it1,{
                            for(i in 0..it.size-1){
                                descuentos.add(it[i].toString())
                            }
                        })
                    }
                    getActivity()?.let { it1 ->
                        this.buscarIncapacidades(it1,{
                            for(i in 0..it.size-1){
                                incapacidades.add(it[i].toString())
                            }
                            var adaptador=getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, incapacidades) }
                            incapacidad.adapter=adaptador
                            for(i in 1..incapacidades.size-1){
                                if(incapacidadquellega.uppercase().equals(incapacidades[i].toString().uppercase())){
                                    incapacidad.setSelection(i)
                                }
                            }
                            registro.setOnClickListener {
                                var indice=incapacidad.selectedItemPosition
                                if(
                                    fecInicio.text.toString().equals("")||fecFin.text.toString().equals("")
                                    ||fecPago.text.toString().equals("")||horasDiarias.text.toString().equals("")
                                    ||indice==0||diasInc.text.toString().equals("")||descISR.text.toString().equals("")
                                    ||descIMSS.text.toString().equals("")
                                ){
                                    Toast.makeText(getActivity(),"Complete los campos",Toast.LENGTH_LONG).show()
                                }
                                else if(diasInc.text.toString().toInt()<0||diasInc.text.toString().toInt()>5
                                    ||descISR.text.toString().toDouble()<0.0||descIMSS.text.toString().toDouble()<0.0
                                    ||horasDiarias.text.toString().toInt()<=0
                                ){
                                    Toast.makeText(getActivity(),"El/los valor(es) ingresado(s) no son permitidos",Toast.LENGTH_LONG).show()
                                }
                                else{
                                    //calcular nomina y registrar
                                    this.claveE=claveE.text.toString()
                                    this.fechaI=fecInicio.text.toString()
                                    this.fechaF=fecFin.text.toString()
                                    this.fechaPago=fecPago.text.toString()
                                    this.horas=horasDiarias.text.toString().toInt()
                                    this.dias=diasInc.text.toString().toInt()
                                    this.descuentoISR=descISR.text.toString().toDouble()
                                    this.descuentoIMSS=descIMSS.text.toString().toDouble()
                                    var arrPuestos= arrayListOf<Any>()
                                    getActivity()?.let { it2 ->
                                        this.buscaPuestoEmpleado(it2,{
                                            this.puesto=it[0].toString()
                                            var valor=0//va a verificar si el nombre coincide
                                            getActivity()?.let { it2 ->
                                                this.buscaPuesto(it2,{
                                                    for(i in 0..it.size-1){
                                                        arrPuestos.add(it[i].toString())
                                                    }
                                                    for(i in 0..arrPuestos.size-1){
                                                        if(this.puesto.equals(arrPuestos[i].toString())){
                                                            valor=1//si coincide buscamos en la bd el pago correspondiente al puesto
                                                            break
                                                        }
                                                    }
                                                    if(valor==1){
                                                        for(i in 1..incapacidades.size-1){
                                                            if(i==indice){
                                                                this.incapacidad=incapacidades[i].toString()
                                                                this.descuentoInc=((descuentos[i-1].toString().toDouble()/100)*this.pagoDia)*this.dias
                                                            }
                                                        }
                                                        getActivity()?.let { it3 ->
                                                            this.buscaPagoPuesto(it3,{
                                                                this.pagoHora=it[0].toString().toDouble()
                                                                this.pagoDia=(this.pagoHora*this.horas).toDouble()
                                                                this.totalDescuentos=descuentoISR.toDouble()+descuentoIMSS.toDouble()+this.descuentoInc.toDouble()
                                                                var diaIni=0
                                                                var diaFin=0
                                                                diaIni=this.fechaI.substring(8,10).toInt()
                                                                diaFin=this.fechaF.substring(8,10).toInt()
                                                                var diferencia=diaFin-diaIni
                                                                this.totalDias=diferencia
                                                                this.total=(this.pagoDia*this.totalDias)-this.totalDescuentos
                                                                if(this.total<=0.0){
                                                                    this.total=0.0
                                                                }
                                                                this.totalHoras=this.totalDias*this.horas
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
                                                            })
                                                        }
                                                    }
                                                })
                                            }
                                        })
                                    }
                                }
                            }
                        })
                    }
                })
            }
        }
        else{
            busqueda.setOnClickListener {
                getActivity()?.let { it1 ->
                    this.buscarIncapacidades(it1,{
                        for(i in 0..it.size-1){
                            incapacidades.add(it[i].toString())
                        }
                        var adaptador=getActivity()?.let { it1 -> ArrayAdapter(it1, android.R.layout.simple_spinner_item, incapacidades) }
                        incapacidad.adapter=adaptador
                    })
                }
                getActivity()?.let { it1 ->
                    this.buscarDescuentos(it1,{
                        for(i in 0..it.size-1){
                            descuentos.add(it[i].toString())
                        }
                    })
                }
                if(claveE.text.toString().equals("")){
                    Toast.makeText(getActivity(),"Por favor ingrese una clave",Toast.LENGTH_LONG).show()
                    etiqueta1.isVisible=false
                    encontrado.isVisible=false
                    etiqueta2.isVisible=false
                    fecInicio.isVisible=false
                    etiqueta3.isVisible=false
                    fecFin.isVisible=false
                    etiqueta4.isVisible=false
                    fecPago.isVisible=false
                    etiqueta5.isVisible=false
                    horasDiarias.isVisible=false
                    etiqueta6.isVisible=false
                    incapacidad.isVisible=false
                    etiqueta7.isVisible=false
                    diasInc.isVisible=false
                    etiqueta8.isVisible=false
                    descISR.isVisible=false
                    etiqueta9.isVisible=false
                    descIMSS.isVisible=false
                    descISR.isVisible=false
                    registro.isVisible=false
                    buscar.isVisible=false
                    encontrado.setText("")
                }
                else if(claveE.text.toString().length!=5){
                    Toast.makeText(getActivity(),"La clave no es válida",Toast.LENGTH_LONG).show()
                    etiqueta1.isVisible=false
                    encontrado.isVisible=false
                    etiqueta2.isVisible=false
                    fecInicio.isVisible=false
                    etiqueta3.isVisible=false
                    fecFin.isVisible=false
                    etiqueta4.isVisible=false
                    fecPago.isVisible=false
                    etiqueta5.isVisible=false
                    horasDiarias.isVisible=false
                    etiqueta6.isVisible=false
                    incapacidad.isVisible=false
                    etiqueta7.isVisible=false
                    diasInc.isVisible=false
                    etiqueta8.isVisible=false
                    descISR.isVisible=false
                    etiqueta9.isVisible=false
                    descIMSS.isVisible=false
                    descISR.isVisible=false
                    registro.isVisible=false
                    buscar.isVisible=false
                    encontrado.setText("")
                }
                else{
                    this.claveE=claveE.text.toString()
                    getActivity()?.let { it1 ->
                        buscarClave(it1){
                            if(!it[0].toString().equals("")){
                                if(it[0].toString().toInt()==1){
                                    encontrado.setText(it[1].toString())
                                    this.nombreE=it[1].toString()
                                    etiqueta1.isVisible=true
                                    encontrado.isVisible=true
                                    encontrado.isEnabled=false
                                    etiqueta2.isVisible=true
                                    fecInicio.isVisible=true
                                    etiqueta3.isVisible=true
                                    fecFin.isVisible=true
                                    etiqueta4.isVisible=true
                                    fecPago.isVisible=true
                                    etiqueta5.isVisible=true
                                    horasDiarias.isVisible=true
                                    etiqueta6.isVisible=true
                                    incapacidad.isVisible=true
                                    etiqueta7.isVisible=true
                                    diasInc.isVisible=true
                                    etiqueta8.isVisible=true
                                    descISR.isVisible=true
                                    etiqueta9.isVisible=true
                                    descIMSS.isVisible=true
                                    descISR.isVisible=true
                                    registro.isVisible=true
                                    buscar.isVisible=true
                                    registro.setOnClickListener {
                                        var indice=incapacidad.selectedItemPosition
                                        if(
                                            fecInicio.text.toString().equals("")||fecFin.text.toString().equals("")
                                            ||fecPago.text.toString().equals("")||horasDiarias.text.toString().equals("")
                                            ||indice==0||diasInc.text.toString().equals("")||descISR.text.toString().equals("")
                                            ||descIMSS.text.toString().equals("")
                                        ){
                                            Toast.makeText(getActivity(),"Complete los campos",Toast.LENGTH_LONG).show()
                                        }
                                        else if(diasInc.text.toString().toInt()<0||diasInc.text.toString().toInt()>5
                                            ||descISR.text.toString().toDouble()<0.0||descIMSS.text.toString().toDouble()<0.0
                                            ||horasDiarias.text.toString().toInt()<=0
                                        ){
                                            Toast.makeText(getActivity(),"El/los valor(es) ingresado(s) no son permitidos",Toast.LENGTH_LONG).show()
                                        }
                                        else{
                                            //calcular nomina y registrar
                                            this.fechaI=fecInicio.text.toString()
                                            this.fechaF=fecFin.text.toString()
                                            this.fechaPago=fecPago.text.toString()
                                            this.horas=horasDiarias.text.toString().toInt()
                                            this.dias=diasInc.text.toString().toInt()
                                            this.descuentoISR=descISR.text.toString().toDouble()
                                            this.descuentoIMSS=descIMSS.text.toString().toDouble()
                                            var arrPuestos= arrayListOf<Any>()
                                            getActivity()?.let { it2 ->
                                                this.buscaPuestoEmpleado(it2,{
                                                this.puesto=it[0].toString()
                                                var valor=0//va a verificar si el nombre coincide
                                                getActivity()?.let { it2 ->
                                                    this.buscaPuesto(it2,{
                                                        for(i in 0..it.size-1){
                                                            arrPuestos.add(it[i].toString())
                                                        }
                                                        for(i in 0..arrPuestos.size-1){
                                                            if(this.puesto.equals(arrPuestos[i].toString())){
                                                                valor=1//si coincide buscamos en la bd el pago correspondiente al puesto
                                                                break
                                                            }
                                                        }
                                                        if(valor==1){
                                                            getActivity()?.let { it3 ->
                                                                this.buscaPagoPuesto(it3,{
                                                                    this.pagoHora=it[0].toString().toDouble()
                                                                    this.pagoDia=(this.pagoHora*this.horas).toDouble()
                                                                    for(i in 1..incapacidades.size-1){
                                                                        if(i==indice){
                                                                            this.incapacidad=incapacidades[i].toString()
                                                                            this.descuentoInc=((descuentos[i-1].toString().toDouble()/100)*this.pagoDia)*this.dias
                                                                        }
                                                                    }
                                                                    this.totalDescuentos=descuentoISR.toDouble()+descuentoIMSS.toDouble()+this.descuentoInc.toDouble()
                                                                    var diaIni=0
                                                                    var diaFin=0
                                                                    diaIni=this.fechaI.substring(8,10).toInt()
                                                                    diaFin=this.fechaF.substring(8,10).toInt()
                                                                    var diferencia=diaFin-diaIni
                                                                    this.totalDias=diferencia
                                                                    this.total=(this.pagoDia*this.totalDias)-this.totalDescuentos
                                                                    if(this.total<=0.0){
                                                                        this.total=0.0
                                                                    }
                                                                    this.totalHoras=this.totalDias*this.horas
                                                                    getActivity()?.let { it4 -> this.registraNomina(it4) }
                                                                })
                                                            }
                                                        }
                                                    })
                                                }
                                            })
                                        }
                                    }
                               }
                           }
                            else{
                                Toast.makeText(getActivity(),"No existe un empleado con esta clave",Toast.LENGTH_LONG).show()
                               etiqueta1.isVisible=false
                               encontrado.isVisible=false
                               etiqueta2.isVisible=false
                               fecInicio.isVisible=false
                               etiqueta3.isVisible=false
                               fecFin.isVisible=false
                               etiqueta4.isVisible=false
                               fecPago.isVisible=false
                               etiqueta5.isVisible=false
                               horasDiarias.isVisible=false
                               etiqueta6.isVisible=false
                               incapacidad.isVisible=false
                               etiqueta7.isVisible=false
                               diasInc.isVisible=false
                               etiqueta8.isVisible=false
                               descISR.isVisible=false
                               etiqueta9.isVisible=false
                               descIMSS.isVisible=false
                               descISR.isVisible=false
                               registro.isVisible=false
                               buscar.isVisible=false
                               encontrado.setText("")
                           }
                        }
                    }
                }
            }
        }
        }
        buscar.setOnClickListener {
            var principal=(activity as Principal)
            principal.replaceFragment(ConsultaNomina(this.claveRol),"Consulta de nóminas")
        }
        return view
    }

    fun datosNominas(context: Context,callbacks:(objeto:ArrayList<Array<Any>>) -> Unit,palabra:String){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(palabra)
        var nombres= arrayListOf<Any>("palabra")
        cone.tabla("rh/control/nominas.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun eliminaNomina(context: Context,clave:String){
        var cone=Conexiones(context)
        var arregloValores= arrayListOf<Any>(clave)
        var arregloNombres= arrayListOf<Any>("clave")
        cone.nuevo(arregloValores,arregloNombres,"rh/control/ctrlQuitaNomina.php",context)
    }

    fun aceptar() {
        getActivity()?.let { it1 -> this.editaNomina(it1) }
    }

    fun cancelar() {
        val t:Toast=Toast.makeText(getActivity(),"Cancelado",Toast.LENGTH_LONG)
        t.show()
    }

    fun datosNomina(context:Context,callbacks:(ArrayList<Any>)->Unit){
        var conex=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        conex.obtener("rh/control/datosNomina.php",nombres,valores,context,this.clave,"clave",{
            callbacks(it)
        })
    }

    fun buscaPagoPuesto(context:Context,callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtener("rh/control/pagoPuestos.php",nombres,valores,context,this.puesto,"puestito",{
            callbacks(it)
        })
    }
    fun buscaPuestoEmpleado(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtener("rh/control/puestoEmpleado.php",nombres,valores,context,this.claveE,"clv",{
            callbacks(it)
        })
    }
    fun buscaPuesto(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>("")
        var nombres= arrayListOf<Any>("")
        cone.obtenerDatos("rh/control/nombresPuestos.php",nombres,valores,context,{
            callbacks(it)
        })
    }
    fun registraNomina(context: Context){
        this.clave=this.claveAlea(5)
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(this.clave,this.claveE,this.nombreE,this.puesto,this.fechaI,this.fechaF,this.fechaPago,this.horas,this.incapacidad,this.dias,this.descuentoISR,this.descuentoIMSS,this.descuentoInc,this.pagoDia,this.pagoHora,this.totalDescuentos,this.totalHoras,this.totalDias,this.total)
        var nombres= arrayListOf<Any>("clv","clvE","nombre","puesto","fecI","fecF","fecP","horas","inc","dias","descISR","descIMSS","descInc","pdia","phora","desc","thoras","tdias","total")
        cone.nuevo(valores,nombres,"rh/control/ctrlRegNomina.php",context)
        this.clave=""
    }

    fun editaNomina(context: Context){
        var cone=Conexiones(context)
        var valores= arrayListOf<Any>(this.clave,this.fechaI,this.fechaF,this.fechaPago,this.horas,this.incapacidad,this.dias,this.descuentoISR,this.descuentoIMSS,this.descuentoInc,this.pagoDia,this.pagoHora,this.totalDescuentos,this.totalHoras,this.totalDias,this.total)
        var nombres= arrayListOf<Any>("clv","fecI","fecF","fecP","horas","inc","dias","descISR","descIMSS","descInc","pdia","phora","desc","thoras","tdias","total")
        cone.nuevo(valores,nombres,"rh/control/ctrlEditNomina.php",context)
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

    fun buscarClave(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var conex=Conexiones(context)
        var valores= arrayListOf<Any>(this.claveE)
        var nombres= arrayListOf<Any>("clave")
        conex.obtener("rh/control/claveEmpleado.php",nombres,valores,context,this.claveE,"claveEmp",{
            callbacks(it)
        })
    }

    fun buscarIncapacidades(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var conex=Conexiones(context)
        var valores= arrayListOf<Any>("","","")
        var nombres= arrayListOf<Any>("","","")
        conex.obtenerDatos("rh/control/nombreInc.php",nombres,valores,context,{
            callbacks(it)
        })
    }

    fun buscarDescuentos(context: Context, callbacks:(ArrayList<Any>)->Unit){
        var conex=Conexiones(context)
        var valores= arrayListOf<Any>("","","")
        var nombres= arrayListOf<Any>("","","")
        conex.obtenerDatos("rh/control/descInc.php",nombres,valores,context,{
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
         * @return A new instance of fragment Nomina.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Nomina("","").apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}