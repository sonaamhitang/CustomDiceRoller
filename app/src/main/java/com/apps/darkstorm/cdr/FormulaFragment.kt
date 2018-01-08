package com.apps.darkstorm.cdr

import android.app.Fragment
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.apps.darkstorm.cdr.dice.DiceFormula
import org.jetbrains.anko.act
import org.jetbrains.anko.appcompat.v7.titleResource
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class FormulaFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.formula_new, container, false)

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        val toolbar = act.find<Toolbar>(R.id.toolbar)
        toolbar.titleResource = R.string.formula_nav_drawer
        val disp = v.find<EditText>(R.id.display)
        v.find<Button>(R.id.clear).setOnClickListener { disp.text.delete(0,disp.text.length)}
        disp.showSoftInputOnFocus = false
        v.find<Button>(R.id.one).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"1") }
        v.find<Button>(R.id.two).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"2") }
        v.find<Button>(R.id.three).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"3") }
        v.find<Button>(R.id.four).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"4") }
        v.find<Button>(R.id.five).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"5") }
        v.find<Button>(R.id.six).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"6") }
        v.find<Button>(R.id.seven).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"7") }
        v.find<Button>(R.id.eight).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"8") }
        v.find<Button>(R.id.nine).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"9") }
        v.find<Button>(R.id.zero).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"0") }
        v.find<Button>(R.id.plus).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"+") }
        v.find<Button>(R.id.minus).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"-") }
        v.find<Button>(R.id.dee).setOnClickListener { disp.text.delete(disp.selectionStart,disp.selectionEnd).insert(disp.selectionStart,"d") }
        v.find<ImageView>(R.id.back).setOnClickListener {
            if(disp.text.isNotEmpty()){
                if(disp.selectionStart!=0){
                    if(disp.hasSelection())
                        disp.text.delete(disp.selectionStart,disp.selectionEnd)
                    else
                        disp.text.delete(disp.selectionStart-1,disp.selectionStart)
                }
            }
        }
        v.find<Button>(R.id.add_dice).setOnClickListener { toast("Coming soon") }
        v.find<Button>(R.id.add_group).setOnClickListener { toast("Coming soon") }
        (act.application as CDR).fab.setStatic(R.drawable.die_roll,{
            if(disp.text.toString() == "")
                toast("Please type something in")
            else
                DiceFormula.solve(disp.text.toString()).showDialog(activity,"Formula is invalid")
        })
    }
}
