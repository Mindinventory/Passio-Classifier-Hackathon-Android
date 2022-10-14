package com.mindinventory.dermatai.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.mindinventory.dermatai.common.extensions.loadImage
import com.mindinventory.dermatai.data.DiseaseModel
import com.mindinventory.dermatai.databinding.RowSearchBinding

/**
 * This class uses for display disease list
 */
class SearchAdapter(private val handleDiseaseClick: (DiseaseModel) -> Unit) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>(), Filterable {

    var diseases: ArrayList<DiseaseModel> = ArrayList()
    var diseasesFiltered: ArrayList<DiseaseModel> = ArrayList()

    /**
     * Uses for add disease
     */
    @SuppressLint("NotifyDataSetChanged")
    fun addData(list: List<DiseaseModel>) {
        diseases = list as ArrayList<DiseaseModel>
        diseasesFiltered = diseases
        notifyDataSetChanged()
    }

    inner class SearchViewHolder(private val rowSearchBinding: RowSearchBinding) :
        ViewHolder(rowSearchBinding.root) {

        fun bind(position: Int) {
            with(rowSearchBinding) {
                diseasesFiltered[position].apply {
                    imagepath.let {
                        ivDisease.loadImage(it)
                    }
                    tvDiseaseName.text = diseasename
                    tvOverview.text = description

                    clDisease.setOnClickListener {
                        handleDiseaseClick.invoke(this)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            RowSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return diseasesFiltered.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                diseasesFiltered = if (charString.isEmpty()) diseases else {
                    val filteredList = ArrayList<DiseaseModel>()
                    diseases.filter {
                        (it.diseasename.trim().replace(" ","").lowercase().contains(charString.trim().replace(" ","").lowercase(),true))
                    }.forEach { filteredList.add(it) }
                    filteredList
                }
                return FilterResults().apply { values = diseasesFiltered }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                diseasesFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<DiseaseModel>
                notifyDataSetChanged()
            }
        }
    }
}