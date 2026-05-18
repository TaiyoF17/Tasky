package com.example.taskyapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val showCancelButton: Boolean = false,
    private val onCancelClick: ((Task) -> Unit)? = null,
    private val onCompleteClick: ((Task) -> Unit)? = null,
    private val onItemClick: ((Task) -> Unit)? = null
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    data class Task(
        val id: Int, 
        val title: String, 
        val desc: String, 
        val address: String, 
        val payment: Double,
        val requester: String? = null // Nuevo campo para el nombre del solicitante
    )

    class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvTaskTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvTaskDesc)
        val tvAddress: TextView = view.findViewById(R.id.tvTaskAddress)
        val tvPayment: TextView = view.findViewById(R.id.tvTaskPayment)
        val tvRequester: TextView = view.findViewById(R.id.tvTaskRequester) // Nuevo TextView en item_task.xml
        val btnCancel: Button = view.findViewById(R.id.btnCancelTask)
        val btnComplete: Button = view.findViewById(R.id.btnCompleteTask)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]
        holder.tvTitle.text = task.title
        holder.tvDesc.text = task.desc
        holder.tvAddress.text = task.address
        holder.tvPayment.text = "$${String.format("%.2f", task.payment)}"
        
        if (task.requester != null) {
            holder.tvRequester.visibility = View.VISIBLE
            holder.tvRequester.text = "Solicitado por: ${task.requester}"
        } else {
            holder.tvRequester.visibility = View.GONE
        }

        if (showCancelButton) {
            holder.btnCancel.visibility = View.VISIBLE
            holder.btnCancel.setOnClickListener {
                onCancelClick?.invoke(task)
            }
        } else {
            holder.btnCancel.visibility = View.GONE
        }

        holder.btnComplete.setOnClickListener {
            onCompleteClick?.invoke(task)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(task)
        }
    }

    override fun getItemCount() = tasks.size

    fun removeTask(task: Task) {
        val index = tasks.indexOf(task)
        if (index != -1) {
            tasks.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}