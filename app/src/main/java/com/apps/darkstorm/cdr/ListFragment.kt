package com.apps.darkstorm.cdr

import android.app.AlertDialog
import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import org.jetbrains.anko.act
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.find

class ListFragment: Fragment(){
    var dice = false
    lateinit var recycle: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater?.inflate(R.layout.recycler_fragment,container,false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val toolbar = act.find<Toolbar>(R.id.toolbar)
        if(dice)
            toolbar.titleResource = R.string.dice_group_nav_drawer
        else
            toolbar.titleResource = R.string.die_nav_drawer
        (act.application as CDR).fab.setStatic(R.drawable.add){
            if(dice)
                fragmentManager.beginTransaction().replace(R.id.content_main,DiceEdit.newInstance((act.application as CDR).addNewGroup()))
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("New Group").commit()
            else
                fragmentManager.beginTransaction().replace(R.id.content_main,DieEdit.newInstance((act.application as CDR).addNewDie()))
                        .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("New Die").commit()
        }
        recycle = view?.find(R.id.recycler)!!
        recycle.adapter = listAdapter()
        val linlay = LinearLayoutManager(act)
        linlay.orientation = LinearLayoutManager.VERTICAL
        recycle.layoutManager = linlay
    }
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.searchable, menu)
        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {return true}
            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0 != null)
                    (recycle.adapter as listAdapter).handleQuery(p0)
                return true
            }
        })
    }
    inner class listAdapter: RecyclerView.Adapter<listAdapter.ViewHolder>(){
        var searchString = ""
        override fun getItemCount() = when(dice){
            true->(act.application as CDR).getDice(searchString).size
            else->(act.application as CDR).getDies(searchString).size
        }
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ViewHolder(act.layoutInflater.inflate(R.layout.list_item,parent,false))
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            if(dice){
                holder.v.find<TextView>(R.id.name).text = (act.application as CDR).getDice(searchString)[position].getName()
                holder.v.setOnClickListener {
                    fragmentManager.beginTransaction().replace(R.id.content_main,DiceEdit.newInstance((act.application as CDR).getDice(searchString)[holder.adapterPosition]))
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Editing").commit()
                }
                holder.v.setOnLongClickListener {
                    val b = AlertDialog.Builder(act)
                    b.setMessage(R.string.delete_confirmation)
                    b.setPositiveButton(android.R.string.yes,{_,_->
                        println(holder.adapterPosition)
                        (act.application as CDR).removeDiceAt(searchString,holder.adapterPosition)
                        this.notifyItemRemoved(holder.adapterPosition)
                    }).setNegativeButton(android.R.string.no,{_,_->}).show()
                    true
                }
            }else{
                holder.v.find<TextView>(R.id.name).text = (act.application as CDR).getDies(searchString)[position].getName()
                holder.v.setOnClickListener {
                    fragmentManager.beginTransaction().replace(R.id.content_main,DieEdit.newInstance((act.application as CDR).getDies(searchString)[holder.adapterPosition]))
                            .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out).addToBackStack("Editing").commit()
                }
                holder.v.setOnLongClickListener {
                    val b = AlertDialog.Builder(act)
                    b.setMessage(R.string.delete_confirmation)
                    b.setPositiveButton(android.R.string.yes,{_,_->
                        println(holder.adapterPosition)
                        (act.application as CDR).removeDieAt(searchString,holder.adapterPosition)
                        this.notifyItemRemoved(holder.adapterPosition)
                    }).setNegativeButton(android.R.string.no,{_,_->}).show()
                    true
                }
            }
        }
        fun handleQuery(str: String){
            searchString = str
            this.notifyDataSetChanged()
        }
        inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v)
    }
    companion object {
        fun newInstance(dice: Boolean): ListFragment{
            val lf = ListFragment()
            lf.dice = dice
            return lf
        }
    }
}