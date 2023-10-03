package edu.utec.uy.semana3.fragments

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import edu.utec.uy.semana3.R
import edu.utec.uy.semana3.databinding.FragmentHomeBinding
import edu.utec.uy.semana3.model.UserData
import edu.utec.uy.semana3.view.UserAdapter

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding: FragmentHomeBinding
    private lateinit var addsBtn: FloatingActionButton
    private lateinit var recv: RecyclerView
    private lateinit var userList: ArrayList<UserData>
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        val addsBtn = view.findViewById<FloatingActionButton>(R.id.addingBtn)
        val recv = view.findViewById<RecyclerView>(R.id.mRecycler)
        userList = ArrayList()
        userAdapter = UserAdapter(requireContext(), userList)

        recv.layoutManager = LinearLayoutManager(requireContext())
        recv.adapter = userAdapter

        addsBtn.setOnClickListener { addInfo() }
    }

    private fun init(view: View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    private fun addInfo() {
        val inflater = LayoutInflater.from(requireContext())
        val v = inflater.inflate(R.layout.add_product, null)
        val userNo = v.findViewById<EditText>(R.id.userNo)
        val userName = v.findViewById<EditText>(R.id.userName)
        val addDialog = AlertDialog.Builder(requireContext())
        addDialog.setView(v)
        addDialog.setPositiveButton("Aceptar") { dialog, _ ->
            val names = userName.text.toString()
            val numbers = userNo.text.toString()
            userList.add(UserData(userName = "Nombre: $names", userMb = "Precio del producto: $numbers"))
            userAdapter.notifyDataSetChanged()
            Toast.makeText(requireContext(), "Producto agregado con éxito", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(requireContext(), "Solicitud Cancelada", Toast.LENGTH_SHORT).show()
        }
        addDialog.create()
        addDialog.show()
    }
}
