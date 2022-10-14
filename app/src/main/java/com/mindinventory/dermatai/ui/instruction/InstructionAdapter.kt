package com.mindinventory.dermatai.ui.instruction

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mindinventory.dermatai.data.InstructionModel
import com.mindinventory.dermatai.databinding.RowInstructionBinding

/**
 * This class uses for display instructions
 */
class InstructionAdapter(private val instructions: ArrayList<InstructionModel>) :
    RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder>() {

    inner class InstructionViewHolder(private val rowInstructionBinding: RowInstructionBinding) :
        ViewHolder(rowInstructionBinding.root) {
        fun bind(position: Int) {
            val instruction = instructions[position]
            with(rowInstructionBinding)
            {
                val index = (position + 1).toString()
                tvIndex.text = index
                tvInstructionTitle.text = instruction.title
                tvInstructionDesc.text = instruction.description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InstructionViewHolder {
        return InstructionViewHolder(
            RowInstructionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InstructionViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return instructions.size
    }

}