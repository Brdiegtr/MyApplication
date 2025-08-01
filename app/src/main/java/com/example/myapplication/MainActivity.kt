package com.example.myapplication
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etNombre: EditText
    private lateinit var btnAgregar: Button
    private lateinit var listViewNombres: ListView

    private lateinit var sharedPreferences: SharedPreferences
    private val PREFS_NAME = "MisPrefs"
    private val KEY_NOMBRES = "nombres_guardados"

    private var listaNombres = ArrayList<String>()
    private lateinit var adapter: NombresAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etNombre = findViewById(R.id.etNombre)
        btnAgregar = findViewById(R.id.btnAgregar)
        listViewNombres = findViewById(R.id.listViewNombres)

        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        listaNombres = cargarNombres()

        adapter = NombresAdapter(listaNombres)
        listViewNombres.adapter = adapter

        // Agregado: Mostrar Toast al pulsar un nombre en la lista
        listViewNombres.setOnItemClickListener { parent, view, position, id ->
            val nombreSeleccionado = listaNombres[position]
            Toast.makeText(this, "Seleccionaste: $nombreSeleccionado", Toast.LENGTH_SHORT).show()
        }

        btnAgregar.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingrese un nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            listaNombres.add(nombre)
            adapter.notifyDataSetChanged()
            guardarNombres(listaNombres)
            etNombre.setText("")
        }
    }

    private fun guardarNombres(nombres: ArrayList<String>) {
        val sb = StringBuilder()
        for (n in nombres) {
            sb.append("$n,")
        }
        if (sb.isNotEmpty()) sb.deleteCharAt(sb.length - 1)
        sharedPreferences.edit().putString(KEY_NOMBRES, sb.toString()).apply()
    }

    private fun cargarNombres(): ArrayList<String> {
        val nombresStr = sharedPreferences.getString(KEY_NOMBRES, "") ?: ""
        val lista = ArrayList<String>()
        if (nombresStr.isNotEmpty()) {
            val arr = nombresStr.split(",")
            lista.addAll(arr)
        }
        return lista
    }

    inner class NombresAdapter(private val nombres: ArrayList<String>) : BaseAdapter() {

        override fun getCount(): Int = nombres.size

        override fun getItem(position: Int): Any = nombres[position]

        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: android.view.View?, parent: android.view.ViewGroup): android.view.View {
            val view = convertView ?: layoutInflater.inflate(R.layout.item_lista, parent, false)

            val imgIcono = view.findViewById<ImageView>(R.id.imgIcono)
            val tvNombre = view.findViewById<TextView>(R.id.tvNombre)

            tvNombre.text = nombres[position]
            imgIcono.setImageResource(R.mipmap.ic_launcher)

            return view
        }
    }

}