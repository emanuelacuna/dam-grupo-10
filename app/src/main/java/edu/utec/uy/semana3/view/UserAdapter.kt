package edu.utec.uy.semana3.view

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import edu.utec.uy.semana3.R
import edu.utec.uy.semana3.model.UserData

class UserAdapter(val c: Context, val userList: ArrayList<UserData>) :RecyclerView.Adapter<UserAdapter.UserViewHolder>()
{
    // Clase interna para mantener las vistas de cada elemento de la lista

    inner class UserViewHolder(val v:View):RecyclerView.ViewHolder(v){
        var name:TextView
        var mbNum: TextView
        var mMenu: ImageView

        init {
            name = v.findViewById<TextView>(R.id.mName)
            mbNum = v.findViewById<TextView>(R.id.mPrice)
            mMenu = v.findViewById(R.id.mMenu)
            mMenu.setOnClickListener { popupMenu(it) }
        }

        private fun popupMenu(v:View){
            val position = userList[adapterPosition]
            val popupMenu = PopupMenu(c,v)
            popupMenu.inflate(R.menu.show_menu)

            // Configurar el comportamiento al hacer clic en elementos del menú

            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{

                        // Crear un cuadro de diálogo para editar el producto

                        val v = LayoutInflater.from(c).inflate(R.layout.add_product, null)
                        val name = v.findViewById<EditText>(R.id.userName)
                        val number = v.findViewById<EditText>(R.id.userNo)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Aceptar"){
                                    dialog,_->
                                // Actualizar los datos del producto con los valores ingresados

                                position.userName = name.text.toString()
                                position.userMb = number.text.toString()
                                notifyDataSetChanged()
                                Toast.makeText(c, "Producto actualizado", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("Cancelar"){
                                    dialog, _->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }

                    R.id.delete->{
                        AlertDialog.Builder(c)
                            // Crear un cuadro de diálogo de confirmación para eliminar el producto

                            .setTitle("Delete")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Estas seguro/a de borrar el producto?")
                            .setPositiveButton("Si"){
                                    dialog, _->
                                // Eliminar el producto de la lista y notificar al adapter

                                userList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(c, "Se ha eliminado el producto", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No"){
                                    dialog, _->
                                dialog.dismiss()
                            }
                            .create()
                            .show()
                        true
                    }
                    else -> true
                }
            }
            popupMenu.show()

            // Forzar la visualización de iconos en el menú contextual
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu= popup.get(popupMenu)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu,  true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item, parent, false)
        return UserViewHolder(v)

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        val newList = userList [position]
        holder.name.text = newList.userName
        holder.mbNum.text = newList.userMb

    }

}