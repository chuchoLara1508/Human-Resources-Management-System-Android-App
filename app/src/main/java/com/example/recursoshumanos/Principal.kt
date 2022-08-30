package com.example.recursoshumanos


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import java.math.BigInteger
import java.security.MessageDigest


class Principal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawer:DrawerLayout
    private lateinit var toogle:ActionBarDrawerToggle
    lateinit var sis:TextView
    lateinit var name:TextView
    lateinit var tipoUsu:TextView
    lateinit var clave:String
    lateinit var rolU:String
    var claveRol:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_principal)
        clave= getIntent().getStringExtra("usua").toString()
        rolU=getIntent().getStringExtra("rol").toString()
        claveRol=getIntent().getStringExtra("ClaveRol").toString()
        name=findViewById<TextView>(R.id.usua)
        name.setText("")
        name.setText("Bienvenido/a: "+clave)
        tipoUsu=findViewById(R.id.tipoUsuario)
        tipoUsu.setText("")
        tipoUsu.setText(rolU.toString())
         sis=findViewById<TextView>(R.id.sistema)
        sis.setText("")
        sis.setText("SISTEMA INTEGRAL DE RECURSOS HUMANOS")
        var fuenteprueba: Typeface
        var fuente1:String="fuentes/fuente1.ttf"
        fuenteprueba= Typeface.createFromAsset(getAssets(),fuente1)
        name.setTypeface(fuenteprueba)
        tipoUsu.setTypeface(fuenteprueba)
        sis.setTypeface(fuenteprueba)
        val toolbar:androidx.appcompat.widget.Toolbar=findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)
        drawer=findViewById(R.id.drawer_layout)
        toogle= ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toogle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        val navigationView:NavigationView=findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener(this)

    }

    @SuppressLint("RestrictedApi")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        var usua=Usuario("")
        when(item.itemId){
            R.id.nav_item_one-> {
                sis.setText("SISTEMA INTEGRAL DE RECURSOS HUMANOS")
                tipoUsu.setText(rolU.toString())
                name.setText("Bienvenido/a: "+clave)
                replaceFragment(Inicio(),"Recursos Humanos")
            }
            R.id.nav_item_two-> {
                getActivity(this)?.let {
                    usua.guardarRP(it,this.claveRol,{
                        sis.setText("")
                        tipoUsu.setText("")
                        name.setText("")
                        if(it[2].toString().toInt()==1){
                            replaceFragment(Departamento("",this.claveRol), "Registrar departamento")
                        }
                        if(it[2].toString().toInt()==0){
                            replaceFragment(ConsultaDepartamento(this.claveRol),"Consulta de departamentos")
                        }
                    })
                }
            }
            R.id.nav_item_three->{
                getActivity(this)?.let {
                    usua.guardarRP(it,this.claveRol,{
                        sis.setText("")
                        tipoUsu.setText("")
                        name.setText("")
                        if(it[0].toString().toInt()==1){
                            replaceFragment(Puesto("",this.claveRol), "Registrar puesto")
                        }
                        if(it[0].toString().toInt()==0){
                            replaceFragment(ConsultaPuesto(this.claveRol),"Consulta de puestos")
                        }
                    })
                }
            }
            R.id.nav_item_four-> {
                getActivity(this)?.let {
                    usua.guardarRP(it,this.claveRol,{
                        sis.setText("")
                        tipoUsu.setText("")
                        name.setText("")
                        if(it[4].toString().toInt()==1){
                            replaceFragment(Empleado("",this.claveRol),"Registrar empleado")
                        }
                        if(it[4].toString().toInt()==0){
                            replaceFragment(ConsultaEmpleado(this.claveRol),"Consulta de empleados")
                        }
                    })
                }
            }
            R.id.nav_item_five-> {
                getActivity(this)?.let {
                    usua.guardarRP(it,this.claveRol,{
                        sis.setText("")
                        tipoUsu.setText("")
                        name.setText("")
                        if(it[5].toString().toInt()==1){
                            replaceFragment(Incapacidad("",this.claveRol),"Registrar incapacidad")
                        }
                        if(it[5].toString().toInt()==0){
                            replaceFragment(ConsultaIncapacidad(this.claveRol),"Consulta de incapacidades")
                        }
                    })
                }
            }
            R.id.nav_item_six->{
                getActivity(this)?.let {
                    usua.guardarRP(it,this.claveRol,{
                        sis.setText("")
                        tipoUsu.setText("")
                        name.setText("")
                        if(it[1].toString().toInt()==1){
                            val dialogo1: AlertDialog.Builder = AlertDialog.Builder(this)
                            dialogo1.setTitle("Confirmación de acción")
                            dialogo1.setMessage("¿Qué acción desea realizar?")
                            dialogo1.setPositiveButton("Registrar",
                                DialogInterface.OnClickListener { dialogo1, id ->
                                    replaceFragment(Nomina("",this.claveRol),"Registrar nómina")
                                })
                            dialogo1.setNegativeButton("Consultar",
                                DialogInterface.OnClickListener { dialogo1, id ->
                                    replaceFragment(ConsultaNomina(this.claveRol),"Consulta de nóminas")
                                })
                            dialogo1.show()
                        }
                        if(it[1].toString().toInt()==0){
                            replaceFragment(ConsultaNomina(this.claveRol),"Consulta de nóminas")
                        }
                    })
                }
            }
            R.id.nav_item_seven->{
                sis.setText("")
                tipoUsu.setText("")
                name.setText("")
                if(this.rolU.equals("SuperAdministrador de RH")){
                    replaceFragment(RolPermiso("",this.rolU),"Registrar roles y permisos")
                }
                else {
                    replaceFragment(ConsultaRolPermiso(this.rolU),"Consulta de roles y permisos")
                }
            }
            R.id.nav_item_eight->{
                getActivity(this)?.let {
                    usua.guardarRP(it,this.claveRol,{
                        sis.setText("")
                        tipoUsu.setText("")
                        name.setText("")
                        if(it[3].toString().toInt()==1){
                            replaceFragment(Usuario(this.claveRol),"Registrar usuario")
                        }
                        if(it[3].toString().toInt()==0){
                            replaceFragment(ConsultaUsuario(this.claveRol),"Consulta de usuarios")
                        }
                    })
                }
            }
            R.id.nav_item_nine->{
                val dialogo1: AlertDialog.Builder = AlertDialog.Builder(this)
                dialogo1.setTitle("Confirmación de salida")
                dialogo1.setMessage("¿Desea salir de la aplicación?")
                dialogo1.setCancelable(false)
                dialogo1.setPositiveButton("Confirmar",
                    DialogInterface.OnClickListener{ dialogo1, id ->
                        var ventaNueva= Intent(this,MainActivity::class.java)
                        startActivity(ventaNueva)
                        finish()
                    }
                )
                dialogo1.setNegativeButton("Cancelar",
                    DialogInterface.OnClickListener { dialogo1, id ->  }
                   )
                dialogo1.show()
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

     fun replaceFragment(fragment: Fragment,title:String){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.framelayout,fragment)
        fragmentTransaction.commit()
        drawer.closeDrawers()
        setTitle(title)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toogle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toogle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}